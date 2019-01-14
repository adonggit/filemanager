package com.jiepier.filemanagerzzw.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanagerzzw.R;
import com.jiepier.filemanagerzzw.constant.AppConstant;
import com.jiepier.filemanagerzzw.event.RefreshEvent;
import com.jiepier.filemanagerzzw.event.TypeEvent;
import com.jiepier.filemanagerzzw.sqlite.SqlUtil;
import com.jiepier.filemanagerzzw.util.RxBus.RxBus;
import com.jiepier.filemanagerzzw.util.ZipUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ZipTask extends AsyncTask<String, Void, List<String>> {

    private final WeakReference<Activity> activity;
    private MaterialDialog dialog;
    private final String zipname;

    public ZipTask(final Activity activity, String name1) {
        this.activity = new WeakReference<>(activity);
        this.zipname = name1;
    }

    @Override
    protected void onPreExecute() {
        final Activity activity = this.activity.get();

        if (activity != null) {
            this.dialog = new MaterialDialog.Builder(activity)
                    .progress(true, 0)
                    .content(activity.getString(R.string.packing))
                    .cancelable(true)
                    .build();

            if (!activity.isFinishing()) {
                this.dialog.show();
            }
        }
    }

    @Override
    protected List<String> doInBackground(String... files) {
        final List<String> failed = new ArrayList<>();

        try {
            ZipUtils.createZip(files, zipname);
            SqlUtil.insert(zipname);
        } catch (Exception e) {
            failed.add(Arrays.toString(files));
        }
        return failed;
    }

    @Override
    protected void onPostExecute(final List<String> failed) {
        super.onPostExecute(failed);
        this.finish(failed);
    }

    @Override
    protected void onCancelled(final List<String> failed) {
        super.onCancelled(failed);
        this.finish(failed);
    }

    private void finish(final List<String> failed) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }

        final Activity activity = this.activity.get();
        if (activity != null && !failed.isEmpty()) {
            Toast.makeText(activity, activity.getString(R.string.cantopenfile),
                    Toast.LENGTH_SHORT).show();
            if (!activity.isFinishing()) {
                dialog.show();
            }
        }
        RxBus.getDefault().post(new RefreshEvent());
        RxBus.getDefault().post(new TypeEvent(AppConstant.REFRESH));
    }
}
