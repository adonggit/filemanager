package com.jiepier.filemanagerzzw.ui.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.jiepier.filemanagerzzw.R;
import com.jiepier.filemanagerzzw.base.BaseActivity;
import com.jiepier.filemanagerzzw.util.AnimationUtil;

import butterknife.BindView;

/**
 * Created by JiePier on 16/12/14.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content)
    LinearLayout content;

    @Override
    public int initContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.settings);
    }

    @Override
    public void initUiAndListener() {
        AnimationUtil.showCircularReveal(content, 0, 0, 2, 1500);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

}
