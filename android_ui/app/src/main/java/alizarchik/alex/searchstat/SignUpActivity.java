package alizarchik.alex.searchstat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import alizarchik.alex.searchstat.model.User;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aoalizarchik.
 */

public class SignUpActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private EditText login;
    private EditText password;
    private EditText password2;
    private EditText email;
    private Button buttonSignUp;
    private ProgressBar progressBar;
    public static final String TAG = "MyLogs";
    IRestApi restAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        buttonSignUp.setOnClickListener((v) -> onClick());
    }

    private void initView() {
        buttonSignUp = findViewById(R.id.sign_up_button_RS);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password_2);
        email = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressBar_RS);
    }

    private void startNewActivity(Class<?> classActivity) {
        Intent intent = new Intent(this, classActivity);
        startActivity(intent);
    }

    public void onClick() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = null;
        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://51.15.55.90:8080/restapi-v4/?")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restAPI = retrofit.create(IRestApi.class);
        } catch (Exception io) {
            Log.d(TAG, "no retrofit: " + io.getMessage());
            return;
        }
        // подготовили вызов на сервер
        if (password.getText().toString().equals(password2.getText().toString())) {
            Call<String> call = restAPI.createUser(login.getText().toString(),  password.getText().toString(), email.getText().toString());
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
            if (networkinfo != null && networkinfo.isConnected()) {
                // запускаем
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    registration(call);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "no retrofit: " + e.getMessage());
                }
            } else {
                Log.d(TAG, "Подключите интернет");
                Toast.makeText(this, R.string.turn_on_internet, Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, R.string.enter_password_again, Toast.LENGTH_SHORT).show();
        }
    }

    private void registration(Call<String> call) throws IOException {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response != null) {
                        String data = response.body();
                        Log.d(TAG, "response.body() user: " + data);
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Log.d(TAG, "onResponse error: " + response.code());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
