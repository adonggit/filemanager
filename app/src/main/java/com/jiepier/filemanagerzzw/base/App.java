package com.jiepier.filemanagerzzw.base;

import android.content.Context;

import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.Utils;
import com.jiepier.filemanagerzzw.constant.AppConstant;
import com.jiepier.filemanagerzzw.manager.CategoryManager;
import com.jiepier.filemanagerzzw.manager.CleanManager;
import com.jiepier.filemanagerzzw.manager.ScanManager;
import com.jiepier.filemanagerzzw.ui.category.memory.accessibility.MemoryAccessibilityManager;
import com.jiepier.filemanagerzzw.util.LanguageUtil;
import com.jiepier.filemanagerzzw.util.Loger;
import com.jiepier.filemanagerzzw.util.Settings;
import com.jiepier.filemanagerzzw.util.SharedUtil;
import com.lsh.packagelibrary.CasePackageApp;

/**
 * Created by JiePier on 16/12/19.
 */

public class App extends CasePackageApp {

    public static App sContext;
    public static int sHeight;
    public static int sWidth;
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
        //初始化工具类
        Utils.init(this);
        //初始化category各个模块需要的单例
        CategoryManager.init(this);
        //初始化扫描、清理管理类
        ScanManager.init(this);
        CleanManager.init(this);
        //初始化文件初始路径和各种Preferences参数
        Settings.updatePreferences(this);
        //语言工具类初始化,用于语言切换
        LanguageUtil.init(this);
        //开启辅助杀功能
        MemoryAccessibilityManager.getInstance(this);
        //开启debug模式
        Loger.DEBUG = true;

        /*如果是第一次加载，那么数据库里没有数据，那么直接扫描获取数据，否则在主界面通过广播
                扫描完之后再进行数据更新*/
        if (SharedUtil.getBoolean(this, AppConstant.IS_FIRST, true)) {
            CategoryManager.getInstance().update();
            SharedUtil.putString(this, AppConstant.DEFAULT_SCAN_PATH,
                    Settings.getDefaultDir());
            SharedUtil.putBoolean(this, AppConstant.IS_FIRST, false);
        }

        sWidth = ScreenUtils.getScreenWidth();
        sHeight = ScreenUtils.getScreenHeight();

    }

    public static Context getAppContext() {
        return sContext;
    }
}
