package com.olesix.searchstat.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.olesix.searchstat.model.impl.LoginApi;
import com.olesix.searchstat.model.impl.SignUpApi;
import com.olesix.searchstat.presenter.ISignUpPresenter;
import com.olesix.searchstat.view.ISignUpView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Olesia on 21.04.2018.
 */

public class SignUpPresenter implements ISignUpPresenter {

    private final SignUpApi model;
    private final ISignUpView view;
    private final LoginApi loginModel;
    public static final String TAG = "MyLogs";


    public SignUpPresenter(ISignUpView view) {
        this.view = view;
        model = new SignUpApi();
        loginModel = new LoginApi();
    }

    @Override
    public void registration(String login, String password1, String password2, String email) {
        if (password1.equals(password2)) {
            Call<String> call = model.request(login, password1, email);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        String newUser = response.body();
                        Log.d(TAG, "newUser: " + newUser);
                        authorization(login, password1);
                    } else {
                        Log.d(TAG, "onResponse error: " + response.code());
                        view.showNoConnectionMessage();
                    }
                    view.hideLoadingIndicator();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure " + t.getMessage());
                    view.showNoConnectionToTheServer();
                    view.hideLoadingIndicator();
                }
            });
        } else {
            view.showErrorMessage();
            view.hideLoadingIndicator();
        }
    }

    private void authorization(String login, String password) {
        Call<String> call = loginModel.getToken(login, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    view.saveToken(token);
                } else {
                    Log.d(TAG, "onResponse error: " + response.code());
                    view.showNoConnectionToTheServer();
                }
                view.hideLoadingIndicator();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure " + t.getMessage());
                view.showNoConnectionToTheServer();
                view.hideLoadingIndicator();
            }
        });
    }

}
