package com.jiepier.filemanagerzzw.task.callback;

import com.jiepier.filemanagerzzw.bean.JunkInfo;

import java.util.ArrayList;

/**
 * Created by panruijie on 2017/2/22.
 * Email : zquprj@gmail.com
 */

public interface ISysScanCallBack {

    void onBegin();

    void onProgress(JunkInfo junkInfo);

    void onCancel();

    void onFinish(ArrayList<JunkInfo> mChildren);

    void onOverTime();
}
