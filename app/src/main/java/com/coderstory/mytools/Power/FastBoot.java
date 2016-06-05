package com.coderstory.mytools.Power;


import android.preference.Preference;

import com.coderstory.mytools.MainActivity;
import com.coderstory.mytools.R;

/**
 * Created by CoderStory on 2016-05-19.
 */
public class FastBoot implements Preference.OnPreferenceClickListener{

    @Override
    public boolean onPreferenceClick(Preference preference) {
        MainActivity.showTips("reboot bootloader",MainActivity.getContext().getString(R.string.Sure_Enter_Fastboot));
        return true;
    }
}
