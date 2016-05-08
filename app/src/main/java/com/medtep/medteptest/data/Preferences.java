package com.medtep.medteptest.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alfonso on 08/05/2016.
 */
public class Preferences {

    private static SharedPreferences sSp;

    private static SharedPreferences getSp(Context context) {
        if(sSp == null) sSp = context.getSharedPreferences("sp", 0);
        return sSp;
    }

    public static int getSortColum(Context context) {
        return getSp(context).getInt("sortColumn", 0);
    }

    public static void setSortColum(Context context, int column) {
        getSp(context).edit().putInt("sortColumn", column).commit();
    }

    public static int getSortMode(Context context) {
        return getSp(context).getInt("sortMode", 0);
    }

    public static void setSortMode(Context context, int mode) {
        getSp(context).edit().putInt("sortMode", mode).commit();
    }
}
