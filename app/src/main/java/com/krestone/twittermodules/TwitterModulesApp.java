package com.krestone.twittermodules;


import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class TwitterModulesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String twitterKey = getString(R.string.twitter_key);
        String twitterSecret = getString(R.string.twitter_secret);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterKey, twitterSecret);
        Fabric.with(this, new Twitter(authConfig));
    }
}
