package com.prokel.mydataworker.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.prokel.mydataworker.config.Constants;
import com.prokel.mydataworker.model.PegawaiModel;
import com.google.gson.Gson;

public class SessionUtils {
    public static boolean login(Context context, PegawaiModel userModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.KEY_USER_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String userJson = new Gson().toJson(userModel);
        editor.putString(Constants.USER_SESSION, userJson);
        editor.apply();
        return true;
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.KEY_USER_SESSION, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(Constants.USER_SESSION, null);
        if (userJson != null) {
            return true;
        } else {
            return false;
        }
    }

    public static PegawaiModel getLoggedUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.KEY_USER_SESSION, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(Constants.USER_SESSION, null);
        if (userJson != null) {
            PegawaiModel user = new Gson().fromJson(userJson, PegawaiModel.class);
            return user;
        } else
            return null;
    }

    public static boolean logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.KEY_USER_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        return true;
    }
}
