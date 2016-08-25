package com.example.cybercell.bms_upload_pic;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
public class SessionManagement {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "BMFPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_DISID = "disid";
    public static final String KEY_DISNAME = "disname";

    public static final String KEY_PSID = "psid";
    public static final String KEY_PSNAME = "psname";

    public static final String KEY_ACID = "asid";
    public static final String KEY_ACNAME = "asname";

public SessionManagement(Context context)
{
    this._context=context;
    pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    editor = pref.edit();
}
    /**
     * Create login session
     * */
    public void createLoginSession(String disid,String disname){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing Dis Code in pref
        editor.putString(KEY_DISID, disid);
        // Storing Dis Code in pref
        editor.putString(KEY_DISNAME, disname);

        // commit changes
        editor.commit();
    }


    public void createACPSSession(String asid,String asname,String psid,String psname){

        // Storing PS Code in pref
        editor.putString(KEY_PSID, psid);
        // Storing PS Name in pref
        editor.putString(KEY_PSNAME, psname);

        // Storing AC Code in pref
        editor.putString(KEY_ACID, asid);
        // Storing AC Name in pref
        editor.putString(KEY_ACNAME, asname);

        // commit changes
        editor.commit();
    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Starting Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getDistrict(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_DISID, pref.getString(KEY_DISID, null));
        user.put(KEY_DISNAME, pref.getString(KEY_DISNAME, null));
        // return user
        return user;
    }
    public HashMap<String, String> getACPS(){
        HashMap<String, String> acps = new HashMap<String, String>();
        // Polling Station
        acps.put(KEY_PSID, pref.getString(KEY_PSID, null));
        acps.put(KEY_PSNAME, pref.getString(KEY_PSNAME, null));
        // Assembly
        acps.put(KEY_ACID, pref.getString(KEY_ACID, null));
        acps.put(KEY_ACNAME, pref.getString(KEY_ACNAME, null));

        // return user
        return acps;
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
