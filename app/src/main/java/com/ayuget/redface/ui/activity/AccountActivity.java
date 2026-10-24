/*
 * Copyright 2015 Ayuget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ayuget.redface.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ayuget.redface.R;
import com.ayuget.redface.account.RedfaceAccountManager;
import com.ayuget.redface.account.UserManager;
import com.ayuget.redface.data.api.hfr.HFRAuthenticator;
import com.ayuget.redface.data.api.model.User;
import com.ayuget.redface.data.rx.EndlessObserver;
import com.ayuget.redface.data.rx.SubscriptionHandler;
import com.ayuget.redface.databinding.ActivityAccountBinding;
import com.ayuget.redface.network.HTTPClientProvider;
import com.ayuget.redface.ui.UIConstants;
import com.ayuget.redface.ui.misc.SnackbarHelper;

import javax.inject.Inject;

import timber.log.Timber;

public class AccountActivity extends BaseActivity {
    private ActivityAccountBinding binding;

    @Inject
    HFRAuthenticator authenticator;

    @Inject
    RedfaceAccountManager accountManager;

    @Inject
    UserManager userManager;

    @Inject
    HTTPClientProvider httpClientProvider;

    private final SubscriptionHandler<User, Boolean> loginSubscriptionHandler = new SubscriptionHandler<>();

    private boolean isReloginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.signInButton.setOnClickListener(v -> onLoginAttempt());

        if (getIntent() != null) {
            Timber.d("Got some intent data");
            isReloginMode = getIntent().getBooleanExtra(UIConstants.ARG_RELOGIN_MODE, false);
        }

        if (isReloginMode) {
            Timber.d("Relogin mode");
            binding.reloginInstructions.setVisibility(View.VISIBLE);
        }
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
    }

    protected void onLoginAttempt() {
        final String username = binding.username.getText().toString().trim();

        if (username.isEmpty()) {
            binding.username.setError(getString(R.string.login_username_empty));
            return;
        } else {
            binding.username.setError(null);
        }

        final String pwd = binding.password.getText().toString().trim();
        if (pwd.isEmpty()) {
            binding.password.setError(getString(R.string.login_password_empty));
            return;
        }

        Timber.d("Login attempt for user '%s'", username);

        final User user = new User(username, pwd);

        // Clearing cookies is necessary because cookies are cached in persistent storage
        // and not overwritter at every request (for obvious performance reasons).
        // In this case, we want "fresh" cookies, because we might be in the process of logging in
        // again.
        httpClientProvider.clearUserCookies(user);

        subscribe(loginSubscriptionHandler.load(user, authenticator.login(user), new EndlessObserver<>() {
            @Override
            public void onNext(Boolean loginWorked) {

                if (loginWorked) {
                    Timber.d("Login is successful !!");

                    // If we are logging in again (change of credentials for example), do
                    // not add a new account
                    if (!accountManager.hasAccount(user)) {
                        accountManager.addAccount(user);
                    }

                    userManager.setActiveUser(user);

                    SnackbarHelper.make(AccountActivity.this, R.string.login_successful).show();

                    Intent intent = new Intent(AccountActivity.this, TopicsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Timber.d("Error while logging in");
                    SnackbarHelper.make(AccountActivity.this, R.string.login_failed).show();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e(throwable, "Unknown error while logging");
            }
        }));
    }


}
