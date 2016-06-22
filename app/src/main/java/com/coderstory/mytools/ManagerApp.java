package com.coderstory.mytools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.IOException;

public class ManagerApp extends AppCompatActivity {

    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mkDir();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_app);
        getSupportActionBar().setElevation(0);
        fragment = new TabFragments();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.frame_container, fragment)
                    .commit();

    }

    private void mkDir() {
        String commandText = "";
        Log.e("cc", commandText);
        Process process;
        DataOutputStream os;
        String cmd = "mkdir /sdcard/backapp";
        try {
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
