package com.iceteaviet.fastfoodfinder.ui.login

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.iceteaviet.fastfoodfinder.R
import com.iceteaviet.fastfoodfinder.ui.custom.processbutton.ActionProcessButton
import com.iceteaviet.fastfoodfinder.utils.isValidEmail
import com.iceteaviet.fastfoodfinder.utils.isValidPassword
import kotlinx.android.synthetic.main.dialog_register.*

/**
 * Created by MyPC on 11/29/2016.
 */
class EmailRegisterDialog : DialogFragment(), View.OnClickListener {
    private var mListener: OnRegisterCompleteListener? = null

    fun setOnRegisterCompleteListener(listener: OnRegisterCompleteListener) {
        mListener = listener
    }

    @Nullable
    override fun onCreateView(@NonNull inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_register, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        //dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(@NonNull view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_sign_up.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_sign_up -> {
                onSignUpButtonClicked()
            }
        }
    }

    // TODO: Optimize checking logic
    private fun onSignUpButtonClicked() {
        btn_sign_up.setMode(ActionProcessButton.Mode.ENDLESS)
        btn_sign_up.progress = 1
        setInputEnabled(false)

        if (isValidEmail(input_email.text.toString())) {
            if (isValidPassword(input_password.text.toString())) {
                if (input_password.text.toString().equals(input_repassword.text.toString())) {
                    // Begin account register

                } else {
                    input_layout_repassword.error = getString(R.string.confirm_pwd_not_match)
                }
            } else {
                input_layout_password.error = getString(R.string.invalid_password)
            }
        } else {
            input_layout_email.error = getString(R.string.invalid_email)
        }

        btn_sign_up.progress = 0
        setInputEnabled(true)
    }

    private fun setInputEnabled(enabled: Boolean) {
        input_layout_email.isEnabled = enabled
        input_layout_password.isEnabled = enabled
        input_layout_repassword.isEnabled = enabled
        input_email.isEnabled = enabled
        input_password.isEnabled = enabled
        input_repassword.isEnabled = enabled
    }

    interface OnRegisterCompleteListener {
        fun onSuccess()
        fun onErrror()
    }

    companion object {
        private const val RESULT_LOAD_IMAGE = 1

        fun newInstance(): EmailRegisterDialog {
            val frag = EmailRegisterDialog()
            val args = Bundle()
            frag.arguments = args
            return frag
        }
    }

}
