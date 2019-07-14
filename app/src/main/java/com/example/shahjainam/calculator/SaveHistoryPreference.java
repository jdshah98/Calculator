package com.example.shahjainam.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.example.shahjainam.calculator.HistoryPreference.expression;

public class SaveHistoryPreference {
    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setExp(Context context, String exp) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        exp = getExp(context) + exp + ",";
        editor.putString(expression,exp);
        editor.apply();
    }

    public static String getExp(Context context) {
        return getPreferences(context).getString(expression,"");
    }

    public static void clear(Context context){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(expression,"");
        editor.apply();
    }
}