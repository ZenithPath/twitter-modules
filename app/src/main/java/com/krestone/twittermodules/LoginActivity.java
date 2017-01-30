package com.krestone.twittermodules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.twitter_login_btn)
    TwitterLoginButton twitterLoginButton;

    @BindView(R.id.logout_btn)
    Button twitterLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        setupLoginBtn();
        setupLogoutBtn();
        configureVisibility();
    }

    private void configureVisibility() {
        TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();

        if (checkSession(twitterSession) && checkCredentials(twitterSession)) {
            twitterLoginButton.setVisibility(View.GONE);
            twitterLogoutBtn.setVisibility(View.VISIBLE);
        } else {
            twitterLoginButton.setVisibility(View.VISIBLE);
            twitterLogoutBtn.setVisibility(View.GONE);
        }
    }

    private boolean checkSession(TwitterSession twitterSession) {
        return twitterSession != null;
    }

    private boolean checkCredentials(TwitterSession twitterSession) {
        TwitterAuthToken authToken = twitterSession.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        return token != null && secret != null && token.length() != 0 && secret.length() != 0;
    }

    private void setupLogoutBtn() {
        twitterLogoutBtn.setOnClickListener(v -> logout());
    }

    private void logout() {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();

        configureVisibility();
    }


    private void setupLoginBtn() {
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.i("onxUserName", result.data.getUserName());
                configureVisibility();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.i("onxFailure", exception.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
