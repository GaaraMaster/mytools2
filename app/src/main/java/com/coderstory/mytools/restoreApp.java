package com.coderstory.mytools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.yalantis.phoenix.PullToRefreshView;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

;


public class restoreApp extends Fragment {
    private View view;
    private List<AppInfo> appInfoList = new ArrayList<AppInfo>();

    List<PackageInfo> packages = new ArrayList<PackageInfo>();
    AppInfoAdapter adapter = null;
    ListView listView = null;
    AppInfo appInfo = null;
    int mposition = 0;
    View mview = null;
    private Context context;
    PullToRefreshView mPullToRefreshView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_restore_app, container, false);
        context = getActivity();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new MyTask().execute();
        mPullToRefreshView = (PullToRefreshView) getActivity(). findViewById(R.id.pull_to_refresh1);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        showData();
                        adapter.notifyDataSetChanged();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void initData() {
        appInfoList= new ArrayList<AppInfo>();
        PackageManager pm=  getActivity().getPackageManager();
        boolean result = false;
        DirManager.apkAll = DirManager.GetApkFileName("/sdcard/backapp/");
        packages=new ArrayList<PackageInfo>();
        for (String item : DirManager.apkAll
                ) {
            PackageInfo packageInfo = DirManager.loadAppInfo(item, getActivity());
            if (packageInfo != null) {
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                //必须设置apk的路径 否则无法读取app的图标和名称
                appInfo.sourceDir ="/sdcard/backapp/"+ item;
                appInfo.publicSourceDir = "/sdcard/backapp/"+ item;
                AppInfo appInfos = new AppInfo(pm.getApplicationLabel(appInfo).toString(), pm.getApplicationIcon(appInfo), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir,String.valueOf( packageInfo.versionName));
                appInfoList.add(appInfos);
            }
        }
    }

    private void showData() {
        adapter = new AppInfoAdapter(getActivity(), R.layout.app_info_item, R.color.disableApp, appInfoList);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mposition = position;
                mview = view;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.tips);
                String tipsText = "";
                String BtnText = getString(R.string.BtnOK);
                appInfo = appInfoList.get(mposition);
                tipsText = "你确定要安装" + appInfo.getName() + "吗？";
                dialog.setMessage(tipsText);
                dialog.setPositiveButton(BtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String commandText = "pm install  " + "/sdcard/backapp/" + appInfo.getPackageName() + ".apk";
                        Log.e("cc", commandText);
                        Process process = null;
                        DataOutputStream os = null;
                        try {
                            String cmd = commandText;
                            process = Runtime.getRuntime().exec("su"); //切换到root帐号
                            os = new DataOutputStream(process.getOutputStream());
                            os.writeBytes(cmd + "\n");
                            os.writeBytes("exit\n");
                            os.flush();
                            process.waitFor();
                        } catch (Exception e) {

                        } finally {
                            try {
                                if (os != null) {
                                    os.close();
                                }
                                process.destroy();
                            } catch (Exception e) {
                            }
                        }
                        closeProgress();
                        Toast.makeText(context, "恭喜 还原成功！", Toast.LENGTH_LONG).show();
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

    }


    public class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);
            showProgress();
        }

        @Override
        protected void onPostExecute(String param) {
            showData();
            getActivity().setProgressBarIndeterminateVisibility(false);
            adapter.notifyDataSetChanged();
            closeProgress();
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            Looper.prepare();
            initData();
            return null;
        }

    }

    private Dialog dialog;

    protected void showProgress() {
        if (dialog == null) {

//		    dialog.setContentView(R.layout.progress_dialog);
            //    dialog.getWindow().setAttributes(params);
            dialog = ProgressDialog.show(getActivity(), getString(R.string.tips), getString(R.string.loadappinfo));

            dialog.show();
        } else {

        }
    }

    //
    protected void closeProgress() {

        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

}

