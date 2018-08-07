package com.iceteaviet.fastfoodfinder.data.remote.user.model;

import com.iceteaviet.fastfoodfinder.data.local.user.model.UserEntity;
import com.iceteaviet.fastfoodfinder.data.local.user.model.UserStoreListEntity;
import com.iceteaviet.fastfoodfinder.utils.AppLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius Doan on 11/24/2016.
 */
public class User {
    private String name;
    private String email;
    private String uid;
    private String photoUrl;
    private List<UserStoreList> userStoreLists;

    public User() {
    }

    public User(String name, String email, String photoUrl, String uid, List<UserStoreList> storeLists) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.photoUrl = photoUrl;
        this.userStoreLists = storeLists;
    }

    public User(UserEntity entity) {
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.uid = entity.getUid();
        this.photoUrl = entity.getPhotoUrl();
        this.userStoreLists = new ArrayList<>();
        List<UserStoreListEntity> userStoreListEntities = entity.getUserStoreLists();
        for (int i = 0; i < userStoreListEntities.size(); i++) {
            this.userStoreLists.add(new UserStoreList(userStoreListEntities.get(i)));
        }
    }

    public UserStoreList getFavouriteStoreList() {
        for (int i = 0; i < userStoreLists.size(); i++) {
            if (userStoreLists.get(i).getId() == UserStoreList.ID_FAVOURITE) {
                return userStoreLists.get(i);
            }
        }

        AppLogger.wtf(User.class.getName(), "Cannot find favourite list !!!");
        return null;
    }

    public List<UserStoreList> getUserStoreLists() {
        return userStoreLists;
    }

    public void setUserStoreLists(List<UserStoreList> userStoreLists) {
        this.userStoreLists = userStoreLists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void addStoreList(UserStoreList list) {
        userStoreLists.add(list);
    }

    public void removeStoreList(int position) {
        userStoreLists.remove(position);
    }
}
