package com.jiepier.filemanagerzzw.ui.main;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jiepier.filemanagerzzw.R;
import com.jiepier.filemanagerzzw.event.ActionModeTitleEvent;
import com.jiepier.filemanagerzzw.event.AllChoiceEvent;
import com.jiepier.filemanagerzzw.event.ChangeThemeEvent;
import com.jiepier.filemanagerzzw.event.ChoiceFolderEvent;
import com.jiepier.filemanagerzzw.event.CleanActionModeEvent;
import com.jiepier.filemanagerzzw.event.CleanChoiceEvent;
import com.jiepier.filemanagerzzw.event.LanguageEvent;
import com.jiepier.filemanagerzzw.event.MutipeChoiceEvent;
import com.jiepier.filemanagerzzw.event.RefreshEvent;
import com.jiepier.filemanagerzzw.util.ClipBoard;
import com.jiepier.filemanagerzzw.util.FileUtil;
import com.jiepier.filemanagerzzw.util.RxBus.RxBus;
import com.jiepier.filemanagerzzw.util.UUIDUtil;
import com.jiepier.filemanagerzzw.widget.DeleteFilesDialog;
import com.jiepier.filemanagerzzw.widget.DirectoryInfoDialog;
import com.jiepier.filemanagerzzw.widget.RenameDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by JiePier on 16/12/21.
 */

public class MainPresenter implements MainContact.Presenter {

    private CompositeSubscription mCompositeSubscription;
    private MainContact.View mView;
    private String[] mFiles;
    private List<String> mList;
    public static final String ZIP = "zip";
    public static final String UNZIP = "unzip";
    private Context mContext;
    private String unZipPath = "";

    public MainPresenter(Context context){
        mContext = context;
        this.mCompositeSubscription = new CompositeSubscription();

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(MutipeChoiceEvent.class)
                .subscribe(mutipeChoiceEvent -> {

                    mList = mutipeChoiceEvent.getList();
                    mFiles = new String[mList.size()];
                    for (int i = 0; i < mFiles.length; i++) {
                        mFiles[i] = mList.get(i);
                    }

                    mView.cretaeActionMode();

                    final String mSelected = context.getString(R.string._selected);
                    mView.setActionModeTitle(mList.size() + mSelected);

                    if (mList.size() == 0) {
                        mView.finishActionMode();
                    }
                    mView.setChoiceCount(mList.size());
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(CleanActionModeEvent.class)
                .subscribe(event -> {
                    mView.finishActionMode();
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ChoiceFolderEvent.class)
                .subscribe(event -> {
                    unZipPath = event.getFilePath();
                    mView.showFolderDialog(UNZIP);
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ChangeThemeEvent.class)
                .subscribe(event-> {
                    mView.reload();
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ActionModeTitleEvent.class)
                .subscribe(event-> {
                    final String mSelected = context.getString(R.string._selected);
                    mView.setActionModeTitle(event.getCount() + mSelected);
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(LanguageEvent.class)
                .subscribe(event -> {
                    mView.reload();
                }, Throwable::printStackTrace));

    }

    @Override
    public void attachView(@NonNull MainContact.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (mCompositeSubscription.isUnsubscribed()) {
            this.mCompositeSubscription.unsubscribe();
        }
        this.mCompositeSubscription = null;
    }

    @Override
    public void clickCancel() {
        ClipBoard.clear();
        mView.refreshMenu();
    }

    @Override
    public void clickFloderInfo(String currentPath) {
        DirectoryInfoDialog dialog = DirectoryInfoDialog.create(currentPath);
        mView.showDialog(dialog);
    }

    @Override
    public void clickMove() {
        ClipBoard.cutMove(mFiles);
        mView.finishActionMode();
        mView.refreshMenu();
        RxBus.getDefault().post(new CleanChoiceEvent());
    }

    @Override
    public void clickCopy() {
        ClipBoard.cutCopy(mFiles);
        mView.finishActionMode();
        mView.refreshMenu();
        RxBus.getDefault().post(new CleanChoiceEvent());
    }

    @Override
    public void clickDelete() {
        DialogFragment deleteDialog = DeleteFilesDialog.instantiate(mFiles);
        mView.showDialog(deleteDialog);
        RxBus.getDefault().post(new CleanActionModeEvent());
    }

    @Override
    public void clickShare() {
        final ArrayList<Uri> uris = new ArrayList<>(mFiles.length);
        for (String mFile : mFiles) {
            final File selected = new File(mFile);
            if (!selected.isDirectory()) {
                uris.add(Uri.fromFile(selected));
            }
        }
        Intent intent = new Intent();
        //多分享
        if (mFiles.length > 1) {
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("*/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        } else {
            //单分享
            intent.setAction(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
        }
        mView.startShareActivity(intent);
        RxBus.getDefault().post(new CleanActionModeEvent());
    }

    @Override
    public void clickShortCut() {
        mView.createShortCut(mFiles);
        RxBus.getDefault().post(new CleanActionModeEvent());
    }

    @Override
    public void clickOpenMode() {
        FileUtil.openFileByCustom(mContext, new File(mFiles[0]));
        RxBus.getDefault().post(new CleanActionModeEvent());
    }

    @Override
    public void clickZip(String currentPath) {
        mView.showFolderDialog(ZIP);
    }

    @Override
    public void clickRename(String currentPath) {

        DialogFragment renameDialog = RenameDialog.instantiate(currentPath, mList.get(0));
        mView.showDialog(renameDialog);
        RxBus.getDefault().post(new CleanActionModeEvent());
    }

    @Override
    public void clickSelectAll(String currentPath) {
        RxBus.getDefault().post(new AllChoiceEvent(currentPath));
    }

    @Override
    public void folderSelect(FolderChooserDialog dialog, @NonNull File folder) {

        String tag = dialog.getTag();
        if (tag.equals(ZIP)) {
            mView.startZipTask(folder.getAbsolutePath() + File.separator + UUIDUtil.createUUID() + ".zip",
                    mFiles);
        } else {
            mView.startUnZipTask(new File(unZipPath), folder);
        }
        mView.finishActionMode();
        RxBus.getDefault().post(new CleanChoiceEvent());
        RxBus.getDefault().post(new RefreshEvent());
    }

}
