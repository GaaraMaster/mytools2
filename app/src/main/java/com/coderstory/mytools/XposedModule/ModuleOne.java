package com.coderstory.mytools.XposedModule;

import android.content.Context;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ModuleOne implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    private static ClassLoader mClassLoader;
    //patch函数
    private static void patchCodeTwo(String paramString1, String paramString2, Object... paramVarArgs) {
        try {
            XposedHelpers.findAndHookMethod(Class.forName(paramString1), paramString2, paramVarArgs);
            return;
        } catch (ClassNotFoundException paramString3) {
            log(" Class "+paramString1+"not found! Skipping...");
            return;
        } catch (NoSuchMethodError paramString4) {
            log(" Method "+paramString2+"found! Skipping...");
        }
    }
    //log记录
    private static void log(String paramString) {
        XposedBridge.log("assistant  " + paramString);
    }
    //主函数
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam paramLoadPackageParam)
            throws Throwable {
        mClassLoader = paramLoadPackageParam.classLoader;
        DRM();
        thmemHook(paramLoadPackageParam);
        blockad(paramLoadPackageParam);
    }
    //主题-入口
    private static void thmemHook(XC_LoadPackage.LoadPackageParam paramLoadPackageParam)
    {

        if (paramLoadPackageParam.packageName.equals("com.android.thememanager")) {
            zhifu();
            yanzheng();
            tongzhi();
        }
    }
    //去除下拉菜单搜索
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam paramInitPackageResourcesParam)
            throws Throwable {
        if (paramInitPackageResourcesParam.packageName.equals("com.android.systemui")) {
            paramInitPackageResourcesParam.res.setReplacement(paramInitPackageResourcesParam.packageName, "bool", "config_show_statusbar_search", Boolean.valueOf(false));
        }
    }
    //主题-DRM验证
    private static void DRM() {

        patchCodeTwo("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, String.class, "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
        patchCodeTwo("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
        patchCodeTwo("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
        patchCodeTwo("miui.drm.DrmManager", "isPermanentRights", new Object[]{"miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
        patchCodeTwo("miui.drm.DrmManager", "isPermanentRights", new Object[]{File.class, XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
        patchCodeTwo("miui.drm.DrmManager", "isLegal", new Object[]{File.class, File.class, XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
        patchCodeTwo("miui.drm.DrmManager", "isLegal", new Object[]{String.class, File.class, XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
    }
    //主题-本地验证
    private static void yanzheng() {
        try {
            Class.forName("miui.resourcebrowser.controller.online.DrmService", false, mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.controller.online.DrmService", mClassLoader, "isLegal", new Object[]{"miui.resourcebrowser.model.Resource", new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    return Boolean.valueOf(true);
                }
            }});
            try {

                Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, mClassLoader);
                XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler", mClassLoader, "isLegal", new Object[]{new XC_MethodReplacement() {
                    protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        return Boolean.valueOf(true);
                    }
                }});
                try {

                    Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, mClassLoader);
                    XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler", mClassLoader, "onCheckResourceRightEventBeforeRealApply", new Object[]{new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                                throws Throwable {
                            XposedHelpers.setBooleanField(paramAnonymousMethodHookParam.thisObject, "mIsLegal", true);
                        }
                    }});
                    return;
                } catch (NoSuchMethodError localNoSuchMethodError1) {
                    log(localNoSuchMethodError1.getMessage());
                } catch (ClassNotFoundException localClassNotFoundException1) {
                    log(localClassNotFoundException1.getMessage());
                }
            } catch (NoSuchMethodError localClassNotFoundException2) {
                log(localClassNotFoundException2.getMessage());
                return;
            } catch (ClassNotFoundException localClassNotFoundException2) {
                log(localClassNotFoundException2.getMessage());
                return;
            }
        } catch (NoSuchMethodError localNoSuchMethodError3) {
            log(localNoSuchMethodError3.getMessage());
            return;
        } catch (ClassNotFoundException localClassNotFoundException3) {
            log(localClassNotFoundException3.getMessage());
            return;
        }
    }
    //主题-设置为已购买
    private static void tongzhi() {
        try {
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.Resource", mClassLoader, "isProductBought", new Object[]{XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
            try {

                XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.ResourceOnlineProperties", mClassLoader, "setProductBought", new Object[]{Boolean.TYPE, new XC_MethodReplacement() {
                    protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        Object[] arrayOfObject = new Object[1];
                        arrayOfObject[0] = Boolean.valueOf(true);
                        paramAnonymousMethodHookParam.args = arrayOfObject;
                        return XposedBridge.invokeOriginalMethod(paramAnonymousMethodHookParam.method, paramAnonymousMethodHookParam.thisObject, arrayOfObject);
                    }
                }});
                try {

                    XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.ResourceOnlineProperties", mClassLoader, "isProductBought", new Object[]{XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
                    return;
                } catch (NoSuchMethodError localNoSuchMethodError1) {

                    log(localNoSuchMethodError1.getMessage());
                    return;


                } catch (Exception localException1) {

                    log(localException1.getMessage());
                    return;

                }
            } catch (NoSuchMethodError localNoSuchMethodError2) {
                log(localNoSuchMethodError2.getMessage());
                return;
            } catch (Exception localException2) {
                log(localException2.getMessage());
                return;
            }
        } catch (NoSuchMethodError localNoSuchMethodError3) {
            log(localNoSuchMethodError3.getMessage());
            return;
        } catch (Exception localException3) {
            log(localException3.getMessage());


            return;
        }
    }
    //主题-在线验证
    private static void zhifu() {
        try {
            Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler", mClassLoader, "isAuthorizedResource", new Object[]{XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});
            try {

                Class.forName("miui.resourcebrowser.controller.online.NetworkHelper", false, mClassLoader);
                XposedHelpers.findAndHookMethod("miui.resourcebrowser.controller.online.NetworkHelper", mClassLoader, "validateResponseResult", new Object[]{Integer.TYPE, InputStream.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult((InputStream) paramAnonymousMethodHookParam.args[1]);
                    }
                }});

            } catch (NoSuchMethodError localNoSuchMethodError1) {

                log(localNoSuchMethodError1.getMessage());
                return;

            } catch (ClassNotFoundException localClassNotFoundException1) {

                log(localClassNotFoundException1.getMessage());
                return;
            }
        } catch (NoSuchMethodError localNoSuchMethodError2) {
            log(localNoSuchMethodError2.getMessage());
            return;
        } catch (ClassNotFoundException localClassNotFoundException2) {
            log(localClassNotFoundException2.getMessage());
            return;
        }

    }
    //移除部分miui广告
    private static void blockad(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) throws Throwable {
        if (paramLoadPackageParam.packageName.equals("com.android.providers.downloads.ui")) {


            try {
                XposedHelpers.findAndHookMethod("com.android.providers.downloads.ui.recommend.config.ADConfig", paramLoadPackageParam.classLoader, "OSSupportAD", new Object[]{new XC_MethodHook() {
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(Boolean.valueOf(false));
                    }
                }});

            } catch (Throwable paramLoadPackageParam11) {
                XposedBridge.log(paramLoadPackageParam11);
                return;
            }
        }
        if (paramLoadPackageParam.packageName.equals("com.android.fileexplorer")) {
            try {
                XposedHelpers.findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", paramLoadPackageParam.classLoader, "supportAd", new Object[]{new XC_MethodHook() {
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(Boolean.valueOf(false));
                    }
                }});
            } catch (Throwable localThrowable2) {
                try {
                    XposedHelpers.findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", paramLoadPackageParam.classLoader, "isAdEnable", new Object[]{Context.class, String.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                                throws Throwable {
                            paramAnonymousMethodHookParam.setResult(Boolean.valueOf(false));
                        }
                    }});
                } catch (Throwable localThrowable3) {
                    try {
                        for (; ; ) {
                            XposedHelpers.findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", paramLoadPackageParam.classLoader, "isStickerEnable", new Object[]{Context.class, new XC_MethodHook() {
                                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                                        throws Throwable {
                                    paramAnonymousMethodHookParam.setResult(Boolean.valueOf(false));
                                }
                            }});
                            try {
                                XposedHelpers.findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", paramLoadPackageParam.classLoader, "isVideoEnable", new Object[]{Context.class, new XC_MethodHook() {
                                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                                            throws Throwable {
                                        paramAnonymousMethodHookParam.setResult(Boolean.valueOf(false));
                                    }
                                }});
                                return;
                            } catch (Throwable paramLoadPackageParam4) {
                                XposedBridge.log(paramLoadPackageParam4);
                                return;
                            }
                        }
                    } catch (Throwable localThrowable13) {
                        for (; ; ) {
                            XposedBridge.log(localThrowable13);
                        }
                    }
                }
            }
        }
        if (paramLoadPackageParam.packageName.equals("com.miui.weather2")) {
            try {
                XposedHelpers.findAndHookMethod("com.miui.weather2.tools.ToolUtils", paramLoadPackageParam.classLoader, "checkCommericalStatue", new Object[]{Context.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(Boolean.valueOf(false));
                    }
                }});
                return;
            } catch (Throwable paramLoadPackageParam3) {
                XposedBridge.log(paramLoadPackageParam3);
                return;
            }
        }
        if (paramLoadPackageParam.packageName.equals("com.android.quicksearchbox")) {
            try {
                XposedHelpers.findAndHookMethod("com.android.quicksearchbox.util.HotWordsUtil", paramLoadPackageParam.classLoader, "setHotQueryView", new Object[]{"com.android.quicksearchbox.ui.HotQueryView", new XC_MethodHook() {
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(null);
                    }
                }});
            } catch (Throwable paramLoadPackageParam2) {
                XposedBridge.log(paramLoadPackageParam2);
                return;
            }
        }

        try {
            XposedHelpers.findAndHookMethod("com.miui.optimizecenter.result.DataModel", paramLoadPackageParam.classLoader, "post", new Object[]{Map.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    paramAnonymousMethodHookParam.setResult("");
                }
            }});
        } catch (Throwable paramLoadPackageParam1) {
            XposedBridge.log(paramLoadPackageParam1);
        }
    }
    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam) throws Throwable {
    }
}



