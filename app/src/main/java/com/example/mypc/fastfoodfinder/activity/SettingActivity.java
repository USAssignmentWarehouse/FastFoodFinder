package com.example.mypc.fastfoodfinder.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.fastfoodfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.tv_setting_sign_out) TextView signOutView;

    @BindView(R.id.sw_languages)
    SwitchCompat swLanguage;

    @BindView(R.id.tv_setting_english)
    TextView tvSettingLanguage;
    private FirebaseAuth mAuth;

    static boolean isVietnamese = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);
        if (!isVietnamese){
            swLanguage.setChecked(true);
        }
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null || mAuth.getCurrentUser() == null || mAuth.getCurrentUser().isAnonymous())
        {
            signOutView.setEnabled(false);
        }

        signOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth != null) {
                    mAuth.signOut();
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        swLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVietnamese)
                {
                    swLanguage.setChecked(true);
                    isVietnamese = false;
                    loadLanguage("vi");
                }
                else {
                    loadLanguage("en");
                    swLanguage.setChecked(false);
                    isVietnamese = true;
                }
            }
        });

        tvSettingLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swLanguage.setChecked(true);
                if (isVietnamese)
                {
                    swLanguage.setChecked(true);
                    isVietnamese = false;
                    loadLanguage("vi");
                }
                else {
                    loadLanguage("en");
                    swLanguage.setChecked(false);
                    isVietnamese = true;
                }
            }
        });
    }
    public void loadLanguage(String lang){
        String languageToLoad = lang;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());
    }
}