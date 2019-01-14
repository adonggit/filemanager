package com.jiepier.filemanagerzzw.ui.common;

import com.jiepier.filemanagerzzw.base.BasePresenter;
import com.jiepier.filemanagerzzw.base.BaseView;

/**
 * Created by JiePier on 16/12/14.
 */

public interface CommonContact {

    interface View extends BaseView{

        void setLongClick(boolean longClick);

        void clearSelect();

        void showSnackBar(String content);

        void refreshAdapter();

        void allChoiceClick();
    }

    interface Presenter extends BasePresenter<View> {

        void onItemClick(String filePath, String parentPath);

    }
}
