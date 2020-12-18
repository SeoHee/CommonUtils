package kyowon.co.kr.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import kyowon.co.kr.lib.utils.config.Config;

public class PreferenceUtil implements Config {

    private Context mContext;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public PreferenceUtil(Context context) {
        mContext = context;
        if (prefs == null) {
            prefs = mContext.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
            editor = prefs.edit();
        }
    }

    public void putIntExtra(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putStringExtra(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putLongExtra(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public void putBooleanExtra(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public int getIntExtra(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public String getStringExtra(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public long getLongExtra(String key, long defaultValue) {
        return prefs.getLong(key, defaultValue);
    }

    public boolean getBooleanExtra(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    public void removePreference(String key) {
        editor.remove(key).commit();
    }

    public boolean containCheck(String key) {
        return prefs.contains(key);
    }

}
