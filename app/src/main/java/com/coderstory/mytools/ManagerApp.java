package com.coderstory.mytools;

/**
 * Created by cc on 2015/12/25.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;



import java.io.DataOutputStream;
import java.io.IOException;

public class ManagerApp extends ActionBarActivity {

    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mkdir();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_app);
        getSupportActionBar().setElevation(0);
        fragment = new TabFragments();
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.frame_container, fragment)
                    .commit();
        }
    }

    private void mkdir() {
        String commandText = "";
        Log.e("cc", commandText);
        Process process = null;
        DataOutputStream os = null;
        String cmd = "mkdir /sdcard/backapp";
        try {
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
