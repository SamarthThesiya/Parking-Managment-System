package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    private static final String SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES";

    public static final String ACCESS_TOKEN     = "ACCESS_TOKEN";
    public static final String FIREBASE_USER_ID = "FIREBASE_USER_ID";

    public static void setSharedPreferense(String key, String value, Context context) {

        SharedPreferences        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor            = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreference(String key, Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public static void clear(Context context) {
        SharedPreferences        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor            = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
