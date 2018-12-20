package com.example.videolistdemo.pref;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

public class IOPref {

    private static final IOPref instance = new IOPref();
    private static final String PREF_NAME = "IOPref";

    public interface PreferenceKey {
        String isMute="isMute";
    }

    private IOPref() {

    }

    public static IOPref getInstance() {
        return instance;
    }

    public void resetPreference(Context context) {
        SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Save the string into the shared preference.
     *
     * @param context Context object.
     * @param key     Key to save.
     * @param value   String value associated with the key.
     */
    public void saveString(Context context, String key, String value) {
        try {
            SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = info.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Save the string into the shared preference.
     *  @param context Context object.
     * @param key     Key to save.
     * @param value   String value associated with the key.
     */
    public void saveStringSet(Context context, String key, HashSet<String> value) {
        try {
            SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = info.edit();
            editor.putStringSet(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the string value of key from shared preference.
     *
     * @param key      Key whose value need to be searched.
     * @param defValue Default value to return in case no such key exist.
     * @return Value associated with the key.
     */
    public String getString(Context context, String key, String defValue) {
        SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return info.getString(key, defValue);
    }

    /**
     * Get the string value of key from shared preference.
     *
     * @param key      Key whose value need to be searched.
     * @param defValue Default value to return in case no such key exist.
     * @return Value associated with the key.
     */
    public HashSet<String> getStringSet(Context context, String key, HashSet<String> defValue) {
        SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return (HashSet<String>) info.getStringSet(key, defValue);
    }

    /**
     * Save the boolean into the shared preference.
     *
     * @param context Context object.
     * @param key     Key to save.
     * @param value   String value associated with the key.
     */
    public void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Get the boolean value of key from shared preference.
     *
     * @param key      Key whose value need to be searched.
     * @param defValue Default value to return in case no such key exist.
     * @return Value associated with the key.
     */
    public boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return info.getBoolean(key, defValue);
    }

    /**
     * Save the Integer into the shared preference.
     *
     * @param context Context object.
     * @param key     Key to save.
     * @param value   Integer value associated with the key.
     */
    public void saveInt(Context context, String key, int value) {
        SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Get the Integer value of key from shared preference.
     *
     * @param key      Key whose value need to be searched.
     * @param defValue Default value to return in case no such key exist.
     * @return Value associated with the key.
     */
    public int getInt(Context context, String key, int defValue) {
        SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return info.getInt(key, defValue);
    }

    /**
     * Save the Integer into the shared preference.
     *
     * @param context Context object.
     * @param key     Key to save.
     * @param value   Integer value associated with the key.
     */
    public void saveDouble(Context context, String key, Double value) {
        SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.apply();
    }

    /**
     * Get the Integer value of key from shared preference.
     *
     * @param key      Key whose value need to be searched.
     * @param defValue Default value to return in case no such key exist.
     * @return Value associated with the key.
     */
    public double getDouble(Context context, String key, double defValue) {
        SharedPreferences info = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return Double.longBitsToDouble(info.getLong(key, Double.doubleToLongBits(defValue)));
    }

}