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
import android.widget.ViewFlipper;

import com.kehxstudios.insight.IntroActivity;
import com.kehxstudios.insight.MainActivity;
import com.kehxstudios.insight.R;
import com.kehxstudios.insight.binaryTree.BinaryTreeActivity;

/**
 * Created by ReidC on 2017-06-14.
 */

public class LoginActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private EditText usernameField, passwordField;
    private CheckBox rememberBox;
    private Button forgotButton, loginButton, createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewFlipper = (ViewFlipper) findViewById(R.id.login_flipper);
        usernameField = (EditText) findViewById(R.id.login_usernameField);
        passwordField = (EditText) findViewById(R.id.login_passwordField);
        rememberBox = (CheckBox) findViewById(R.id.login_rememberBox);
        forgotButton = (Button) findViewById(R.id.login_forgotUserButton);
        loginButton = (Button) findViewById(R.id.login_loginButton);
        createButton = (Button) findViewById(R.id.login_createUserButton);


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
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!usernameField.getText().toString().equals("Username") &&
                        !passwordField.getText().toString().equals("")) {
                    checkLogin();
                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!usernameField.getText().toString().equals("Username") &&
                        !passwordField.getText().toString().equals("")) {
                    createLogin();
                }
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

    private void createLogin() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String usersString = sharedPreferences.getString("savedUsers", "");
        String enteredUser = usernameField.getText().toString();
        String enteredPassword = passwordField.getText().toString();
        if (!usersString.equals("")) {
            String[] users = usersString.split(",");
            for (String user : users) {
                if (user.equals(enteredUser)) {
                    Log.d("Login", "USERNAME IN USE");
                    return;
                }
            }
            usersString += ",";
        }
        usersString += enteredUser;
        editor.putString("savedUsers", usersString);
        editor.putString("user_" + enteredUser, enteredPassword);
        editor.commit();
        Log.d("Login", "USER CREATED");
    }

    private void checkLogin() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String usersString = sharedPreferences.getString("savedUsers", "");
        String enteredUser = usernameField.getText().toString();
        if (!usersString.equals("")) {
            String[] users = usersString.split(",");
            for (String user : users) {
                if (user.equals(enteredUser)) {
                    String enteredPassword = passwordField.getText().toString();
                    String savedPassword = sharedPreferences.getString("user_" + user, "");
                    if (enteredPassword.equals(savedPassword)) {
                        Log.d("Login", "SUCCESSFUL");
                        return;
                    } else {
                        Log.d("Login", "INCORRECT PASSWORD");
                        return;
                    }
                }
            }
            Log.d("Login", "USER NOT FOUND");
            return;
        }
        Log.d("Login", "USERS IS EMPTY");
    }

    private void save() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!usernameField.getText().toString().equals("Username")) {
            editor.putBoolean("login_checkbox", rememberBox.isChecked());
            if (rememberBox.isChecked()) {
                editor.putString("login_saved_username", usernameField.getText().toString());
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
            usernameField.setText(sharedPreferences.getString("login_saved_username", "Username"));
        }
    }
}
