package com.halabang.kewpm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Joey on 8/6/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TokenTAG", "Refreshed token: " + token);
        saveTokenToPrefs(token);
    }

    private void saveTokenToPrefs(String _token)
    {
        // Access Shared Preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Save to SharedPreferences
        editor.putString("registration_id", _token);
        editor.apply();
    }


//    private void sendRegistrationToServer(String token) {
//        //SharedPreferenceUtils.getInstance(this).setValue("FCM_Token", token);
//    }

//    public String getDeviceToken() {
//        return SharedPreferenceUtils
//                .getInstance(this)
//                .getStringValue("FCM_Token", "").equals("")
//                ? FirebaseInstanceId.getInstance().getToken()
//                : SharedPreferenceUtils.getInstance(this).getStringValue("FCM_Token", "");
//    }
}
