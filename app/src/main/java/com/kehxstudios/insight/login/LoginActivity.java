package com.kehxstudios.insight.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.kehxstudios.insight.IntroActivity;
import com.kehxstudios.insight.MainActivity;
import com.kehxstudios.insight.R;
import com.kehxstudios.insight.binaryTree.BinaryTreeActivity;

/**
 * Created by ReidC on 2017-06-14.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private CheckBox rememberBox;
    private Button forgotButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        rememberBox = (CheckBox) findViewById(R.id.rememberBox);

        forgotButton = (Button) findViewById(R.id.forgotButton);
        loginButton = (Button) findViewById(R.id.loginButton);


        usernameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && usernameField.getText().toString().equals("Username")) {
                    usernameField.setText("");
                } else if (!hasFocus && usernameField.getText().toString().equals("")) {
                    usernameField.setText("Username");
                }
            }
        });

        rememberBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CheckBox", "Clicked");
            }
        });

        forgotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }


    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void save() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!usernameField.getText().toString().equals("Username")) {
            editor.putBoolean("login_checkbox", rememberBox.isChecked());
            if (rememberBox.isChecked()) {
                editor.putString("login_username", usernameField.getText().toString());
            }
        } else {
            editor.putBoolean("login_checkbox", false);
        }
        editor.commit();
    }

    private void load() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        rememberBox.setChecked(sharedPreferences.getBoolean("login_checkbox", false));
        if (rememberBox.isChecked()) {
            usernameField.setText(sharedPreferences.getString("login_username", "Username"));
        }
    }
}
