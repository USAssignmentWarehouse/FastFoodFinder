package com.iceteaviet.fastfoodfinder.data

import com.google.firebase.auth.AuthCredential
import com.iceteaviet.fastfoodfinder.App
import com.iceteaviet.fastfoodfinder.data.auth.ClientAuth
import com.iceteaviet.fastfoodfinder.data.domain.store.StoreDataSource
import com.iceteaviet.fastfoodfinder.data.domain.user.UserDataSource
import com.iceteaviet.fastfoodfinder.data.prefs.PreferencesHelper
import com.iceteaviet.fastfoodfinder.data.remote.routing.MapsRoutingApiHelper
import com.iceteaviet.fastfoodfinder.data.remote.store.model.Store
import com.iceteaviet.fastfoodfinder.data.remote.user.model.User
import com.iceteaviet.fastfoodfinder.utils.Constant
import com.iceteaviet.fastfoodfinder.utils.isEmpty
import com.iceteaviet.fastfoodfinder.utils.isValidUserUid
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by tom on 7/9/18.
 */

class AppDataManager(private val localStoreDataSource: StoreDataSource, private val remoteStoreDataSource: StoreDataSource,
                     private val clientAuth: ClientAuth,
                     private val localUserDataSource: UserDataSource, private val remoteUserDataSource: UserDataSource,
                     private val mapsRoutingApiHelper: MapsRoutingApiHelper, private val preferencesHelper: PreferencesHelper) : DataManager {

    private var currentUser: User? = null
    private lateinit var searchHistory: MutableSet<String>

    init {
        Realm.init(App.getContext())
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }

    override fun getLocalStoreDataSource(): StoreDataSource {
        return localStoreDataSource
    }

    override fun getRemoteStoreDataSource(): StoreDataSource {
        return remoteStoreDataSource
    }

    override fun getLocalUserDataSource(): UserDataSource {
        return localUserDataSource
    }

    override fun getRemoteUserDataSource(): UserDataSource {
        return remoteUserDataSource
    }

    override fun getMapsRoutingApiHelper(): MapsRoutingApiHelper {
        return mapsRoutingApiHelper
    }

    override fun getPreferencesHelper(): PreferencesHelper {
        return preferencesHelper
    }

    override fun loadStoresFromServer(): Single<List<Store>> {
        if (clientAuth.isSignedIn()) {
            return remoteStoreDataSource.getAllStores()
        } else {
            return Single.create { emitter ->
                // Not signed in
                clientAuth.signInWithEmailAndPassword(Constant.DOWNLOADER_BOT_EMAIL, Constant.DOWNLOADER_BOT_PWD)
                        .toCompletable()
                        .andThen(remoteStoreDataSource.getAllStores())
                        .subscribeOn(Schedulers.io())
                        .subscribe(object : SingleObserver<List<Store>> {
                            override fun onSubscribe(d: Disposable) {
                                emitter.setDisposable(d)
                            }

                            override fun onSuccess(storeList: List<Store>) {
                                signOut()
                                emitter.onSuccess(storeList)
                            }

                            override fun onError(e: Throwable) {
                                signOut()
                                emitter.onError(e)
                            }
                        })
            }
        }
    }

    override fun getCurrentUserUid(): String {
        var uid = ""

        currentUser?.let {
            uid = it.getUid()
        }

        if (isEmpty(uid))
            uid = clientAuth.getCurrentUserUid()

        return uid
    }

    override fun signUpWithEmailAndPassword(email: String, password: String): Single<User> {
        return clientAuth.signUpWithEmailAndPassword(email, password)
    }

    override fun isSignedIn(): Boolean {
        return clientAuth.isSignedIn()
    }

    override fun signOut() {
        clientAuth.signOut()
        setCurrentUser(null)
    }

    override fun signInWithEmailAndPassword(email: String, password: String): Single<User> {
        return clientAuth.signInWithEmailAndPassword(email, password)
    }

    override fun signInWithCredential(authCredential: AuthCredential): Single<User> {
        return clientAuth.signInWithCredential(authCredential)
    }

    override fun getCurrentUser(): User? {
        if (currentUser == null) {
            val uid = getCurrentUserUid()
            if (isValidUserUid(uid)) {
                try {
                    currentUser = localUserDataSource.getUser(uid)
                            .blockingGet()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return currentUser
    }

    override fun setCurrentUser(user: User?) {
        currentUser = user
        if (user != null && !user.getUid().isEmpty()) {
            localUserDataSource.insertOrUpdate(user)
        }
    }

    override fun getSearchHistories(): MutableSet<String> {
        if (!::searchHistory.isInitialized)
            searchHistory = preferencesHelper.getSearchHistories()

        return searchHistory
    }

    override fun addSearchHistories(searchContent: String) {
        if (!::searchHistory.isInitialized)
            searchHistory = preferencesHelper.getSearchHistories()

        if (searchHistory.contains(searchContent)) {
            // Remove old element to push the most recent search to the top of the list
            searchHistory.remove(searchContent)
        }
        searchHistory.add(searchContent)
        preferencesHelper.setSearchHistories(searchHistory)
    }

    companion object {
        private val TAG = AppDataManager::class.java.simpleName
    }
}