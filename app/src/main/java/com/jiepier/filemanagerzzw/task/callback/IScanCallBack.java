package com.jiepier.filemanagerzzw.task.callback;

import com.jiepier.filemanagerzzw.bean.JunkInfo;

import java.util.ArrayList;

/**
 * Created by panruijie on 2017/2/15.
 * Email : zquprj@gmail.com
 */

public interface IScanCallBack {

    void onBegin();

    void onProgress(JunkInfo junkInfo);

    void onCancel();

    void onFinish(ArrayList<JunkInfo> apkList, ArrayList<JunkInfo> logList,
                  ArrayList<JunkInfo> tempList, ArrayList<JunkInfo> bigFileList);

    void onOverTime();
}