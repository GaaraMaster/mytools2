package com.coderstory.mytools;

import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;


public class EnterBlog implements Preference.OnPreferenceClickListener{

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Intent Intent = new Intent();
        Intent.setAction("android.intent.action.VIEW");
        Intent.setData(Uri.parse("http://blog.coderstory.cn"));
        MainActivity.getContext().   startActivity(Intent);
        return false;
    }
}



