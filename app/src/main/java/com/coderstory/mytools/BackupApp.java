package com.coderstory.mytools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.yalantis.phoenix.PullToRefreshView;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BackupApp extends Fragment {
    private View view;
    private List<AppInfo> appInfoList = new ArrayList<AppInfo>();

    List<PackageInfo> packages = new ArrayList<PackageInfo>();
    AppInfoAdapter adapter = null;
    ListView listView = null;
    AppInfo appInfo = null;
    int mPosition = 0;
    View mView = null;
    com.yalantis.phoenix.PullToRefreshView mPullToRefreshView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_back_app, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new MyTask().execute();
        mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pull_to_refresh);

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

    private void initFruit() {
        appInfoList.clear();
        //packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0 表示是系统应用
        if (packages != null) {
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);
                if (!isBackuped(packageInfo.applicationInfo.packageName)) {
                    if (packageInfo.applicationInfo.sourceDir.startsWith("/data/")) {
                        if (packageInfo.applicationInfo.enabled == true & ApplicationInfo.FLAG_SYSTEM >= 0) {
                            AppInfo appInfo = new AppInfo(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir,String.valueOf( packageInfo.versionName));
                            appInfoList.add(appInfo);
                        } else if (ApplicationInfo.FLAG_SYSTEM >= 0) {
                            AppInfo appInfo = new AppInfo(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()), packageInfo.packageName, true, packageInfo.applicationInfo.sourceDir,String.valueOf( packageInfo.versionName));
                            appInfoList.add(appInfo);
                        }
                    }
                }
            }
        }
    }

    private boolean isBackuped(String packagesName) {
        boolean result = false;
        DirManager.apkAll = DirManager.GetApkFileName("/sdcard/backapp/");
        if (DirManager.apkAll != null) {
            for (String element : DirManager.apkAll)
            {
                if ((packagesName + ".apk").equals(element)) {
                    result = true;
                }
            }
        }
        return result;
    }

    private void initData() {
        packages.clear();
     //   Toast.makeText(getActivity(), R.string.isloading, Toast.LENGTH_LONG).show();
        packages = getActivity().getPackageManager().getInstalledPackages(0);
        initFruit();
    }

    private void showData() {
        adapter = new AppInfoAdapter(getActivity(), R.layout.app_info_item, R.color.disableApp, appInfoList);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                mPosition = position;
                mView = view;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.tips);
                String tipsText = "";
                String BtnText = getString(R.string.BtnOK);
                appInfo = appInfoList.get(mPosition);
                tipsText = "你确定要备份" + appInfo.getName() + "吗？";

                dialog.setMessage(tipsText);
                dialog.setPositiveButton(BtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DirManager.needReload=true;
                        String commandText = "cp -f " + appInfo.getappdir() + " /sdcard/backapp/" + appInfo.getPackageName() + ".apk";
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
                            View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.app_info_item, null);
                            appInfoList.remove(mPosition);
                            adapter.notifyDataSetChanged();
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
                        DirManager.needReload=true;
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


    class MyTask extends AsyncTask<String, Integer, String> {

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

