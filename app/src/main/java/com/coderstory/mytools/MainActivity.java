package com.coderstory.mytools;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



import java.io.IOException;


public class MainActivity extends AppCompatActivity    {
    private  static Context mContext=null;
    private  String  commandText="";
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_main);
        View v=this.findViewById(R.id.mian);
        mContext=MainActivity.this;
        Button disiableAppBtn= (Button) findViewById(R.id.Disable_App_Btn);
        disiableAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DisableAppActivity.class);
                startActivity(intent);
            }
        });
        Button managerApp= (Button) findViewById(R.id.ManagerApp);
        managerApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ManagerApp.class);
                startActivity(intent);
            }
        });
        Button   fastbootBtn = (Button) findViewById(R.id.fastboot);
        fastbootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(mContext, "reboot bootloader", getString(R.string.Sure_Enter_Fastboot));
            }
        });
        Button recoveryBtn = (Button) findViewById(R.id.recovery);
        recoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(mContext, "re", getString(R.string.Sure_Enter_Rec));

            }
        });
        //busybox killall system_server
        Button hotRebootBtn = (Button) findViewById(R.id.hotboot);
        hotRebootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(mContext, "busybox killall system_server", getString(R.string.Sure_quickrestart));
            }
        });
        Button shutdownBtn = (Button) findViewById(R.id.shutdown);
        shutdownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(mContext, "reboot -p", getString(R.string.Sure_shoutdown));
            }
        });
        Button rebootBtn = (Button) findViewById(R.id.reboot);
        rebootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(mContext,"reboot",getString(R.string.Sure_Reboot));
            }
        });

        Button about= (Button) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,about.class);
                startActivity(intent);
            }
        });

     Button exitBtn= (Button) findViewById(R.id.exit);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(R.string.tips);
                dialog.setMessage(R.string.Sure_Exit);
                dialog.setPositiveButton(R.string.ExitTips, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       System.exit(0);
                    }
                });

                dialog.setCancelable(true);
                dialog.setNegativeButton(R.string.Btncancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
    }

    /*实现弹窗确定执行某条命令*/
    public  static  void   showTips(final Context mContext,final String commandText,String messageText)
    {
      AlertDialog builder=  new AlertDialog.Builder(mContext)
                .setTitle(R.string.tips)
                .setMessage(messageText)
                .setPositiveButton(R.string.BtnOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String cmd = commandText;
                        try {
                            Runtime.getRuntime().exec(new String[]{ "su", "-c", cmd });
                        } catch (IOException e) {
                            Log.d("su", e.getMessage());
                            new AlertDialog.Builder(mContext).setTitle(R.string.RunError).setMessage(
                                    e.getMessage()).setPositiveButton(R.string.BtnOK, null).show();
                        }
                    }
                })
                .setNegativeButton(R.string.Btncancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消当前对话框
                        dialog.cancel();

                    }
                }).create();
        builder.show();
    }



    /*实现弹窗确定执行某条命令*/
    public  static  void   showTips(final String commandText,String messageText)
    {
        final   Context    mContext=MainActivity.getContext();
        AlertDialog builder=  new AlertDialog.Builder(mContext)
                .setTitle(R.string.tips)
                .setMessage(messageText)
                .setPositiveButton(R.string.BtnOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String cmd = commandText;
                        try {
                            Runtime.getRuntime().exec(new String[]{ "su", "-c", cmd });
                        } catch (IOException e) {
                            Log.d("su", e.getMessage());
                            new AlertDialog.Builder(mContext).setTitle(R.string.RunError).setMessage(
                                    e.getMessage()).setPositiveButton(R.string.BtnOK, null).show();
                        }
                    }
                })
                .setNegativeButton(R.string.Btncancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消当前对话框
                        dialog.cancel();

                    }
                }).create();
        builder.show();
    }


    @Override
    public void onBackPressed() { //直接退出
        System.exit(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, R.string.Exit_Tips, Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public static Context getContext() {
        return mContext;
    }

}
