package com.jiepier.filemanagerzzw.ui.category.memory;

import com.jiepier.filemanagerzzw.base.BasePresenter;
import com.jiepier.filemanagerzzw.base.BaseView;
import com.jiepier.filemanagerzzw.bean.AppProcessInfo;

import java.util.List;
import java.util.Set;

/**
 * Created by panruijie on 17/1/9.
 * Email : zquprj@gmail.com
 */

public class MemoryContact {

    interface View extends BaseView{

        void showLoadingView();

        void dimissLoadingView();

        void notifityItem();

        void showBoomView();

        void showMemoryClean(String content);

        void setData(List<AppProcessInfo> list);
    }

    interface Presenter extends BasePresenter<View>{

        void getRunningAppInfo();

        void killRunningAppInfo(Set<String> set);
    }
}
