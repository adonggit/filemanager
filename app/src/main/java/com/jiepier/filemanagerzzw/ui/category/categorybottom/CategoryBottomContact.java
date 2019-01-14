package com.jiepier.filemanagerzzw.ui.category.categorybottom;

import com.jiepier.filemanagerzzw.base.BasePresenter;
import com.jiepier.filemanagerzzw.base.BaseView;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class CategoryBottomContact {

    interface View extends BaseView{

        void showDialog();

        void dimissDialog();

        void setData(ArrayList<String> list);

        void selectAll();

        void clearSelect();

        void setDataByObservable(Observable<ArrayList<String>> observable);
    }

    interface Presenter extends BasePresenter<View>{

        void setIndex(int index);

        void onItemClick(String path);

    }
}
