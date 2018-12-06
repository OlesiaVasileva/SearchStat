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
import android.widget.TextView;
import android.widget.Toast;

import com.olesix.searchstat.R;
import com.olesix.searchstat.presenter.ILoginPresenter;
import com.olesix.searchstat.presenter.impl.LoginPresenter;
import com.olesix.searchstat.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private ILoginPresenter presenter;
    private EditText login;
    private EditText password;
    private Button loginButton;
    private TextView signUpButton;
    private ProgressBar progressBar;
    private TokenStorage tokenStorage;
    public static final String TAG = "MyLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        signUpButton.setOnClickListener(view -> startNewActivity(SignUpActivity.class));
        loginButton.setOnClickListener((v) -> onClick());
        tokenStorage = TokenStorage.getInstance();
        if (!tokenStorage.loadToken(this).isEmpty()) {
            startNewActivity(MainActivity.class);
            finish();
        }
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            if (presenter == null) {
                presenter = new LoginPresenter(this);
            }
        } else {
            showNoConnectionMessage();
        }
    }

    private void initView() {
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);
        progressBar = findViewById(R.id.progressBar_Login_Screen);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat
                .getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                onClick();
                return true;
            }
            return false;
        });
    }

    private void startNewActivity(Class<?> classActivity) {
        Intent intent = new Intent(this, classActivity);
        startActivity(intent);
    }

    public void onClick() {
        if (login.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, R.string.enter_login_or_password, Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            presenter.authorization(login.getText().toString(), password.getText().toString());
        }
    }

    @Override
    public void hideLoadingIndicator() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNoConnectionToTheServer() {
        Log.d(TAG, "Hет соединения с сервером");
        Toast.makeText(LoginActivity.this, R.string.no_connection_to_the_server, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoConnectionMessage() {
        Log.d(TAG, "Подключите интернет");
        Toast.makeText(LoginActivity.this, R.string.turn_on_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNeedRegisterMessage() {
        Toast.makeText(LoginActivity.this, "You need to register.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveToken(String token) {
        tokenStorage.tokenSave(token, LoginActivity.this);
        startNewActivity(MainActivity.class);
        finish();
    }
}
