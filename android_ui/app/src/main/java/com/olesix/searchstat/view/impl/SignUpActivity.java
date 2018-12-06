package com.olesix.searchstat.view.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.olesix.searchstat.R;
import com.olesix.searchstat.presenter.ISignUpPresenter;
import com.olesix.searchstat.presenter.impl.SignUpPresenter;
import com.olesix.searchstat.view.ISignUpView;

/**
 * Created by aoalizarchik.
 */

public class SignUpActivity extends AppCompatActivity implements ISignUpView {

    private ISignUpPresenter presenter;
    private EditText login;
    private EditText password;
    private EditText password2;
    private EditText email;
    private Button buttonSignUp;
    private ProgressBar progressBar;
    private TokenStorage tokenStorage;
    public static final String TAG = "MyLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        buttonSignUp.setOnClickListener((v) -> onClick());

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            if (presenter == null) {
                presenter = new SignUpPresenter(this);
            }
        } else {
            showNoConnectionMessage();
        }
    }

    private void initView() {
        buttonSignUp = findViewById(R.id.sign_up_button_RS);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password_2);
        email = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressBar_Registration_Screen);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat
                .getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        email.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                onClick();
                return true;
            }
            return false;
        });
        tokenStorage = TokenStorage.getInstance();
    }

    public void onClick() {
        if (isValidEmail(email.getText())) {
            progressBar.setVisibility(View.VISIBLE);
            presenter.registration(login.getText().toString(), password.getText().toString(),
                    password2.getText().toString(), email.getText().toString());
        } else {
            Toast.makeText(SignUpActivity.this, R.string.enter_correct_email, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void hideLoadingIndicator() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNoConnectionToTheServer() {
        Log.d(TAG, "Hет соединения с сервером");
        Toast.makeText(SignUpActivity.this, R.string.no_connection_to_the_server, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoConnectionMessage() {
        Log.d(TAG, "Подключите интернет");
        Toast.makeText(SignUpActivity.this, R.string.turn_on_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(SignUpActivity.this, R.string.enter_password_again, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveToken(String token) {
        tokenStorage.tokenSave(token, SignUpActivity.this);
        startNewActivity(MainActivity.class);
        finish();
    }

    private void startNewActivity(Class<?> classActivity) {
        Intent intent = new Intent(this, classActivity);
        startActivity(intent);
    }
}
