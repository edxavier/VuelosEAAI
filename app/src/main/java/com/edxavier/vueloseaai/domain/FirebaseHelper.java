package com.edxavier.vueloseaai.domain;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public class FirebaseHelper {

    private FirebaseAnalytics mFirebaseAnalytics;

    private static class singletonHolder{
        private static final FirebaseHelper INSTANCE = new FirebaseHelper();
    }

    public FirebaseHelper() {
        //this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
