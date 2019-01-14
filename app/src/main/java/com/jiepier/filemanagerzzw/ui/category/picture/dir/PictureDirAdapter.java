package com.jiepier.filemanagerzzw.ui.category.picture.dir;

import android.content.Context;

import com.jiepier.filemanagerzzw.R;
import com.jiepier.filemanagerzzw.base.BaseAdapter;
import com.jiepier.filemanagerzzw.base.BaseViewHolder;

import java.util.List;

/**
 * Created by panruijie on 17/1/19.
 * Email : zquprj@gmail.com
 */

public class PictureDirAdapter extends BaseAdapter<String, BaseViewHolder> {

    private OnItemClickListener mListener;

    public PictureDirAdapter(Context context) {
        super(R.layout.item_image);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {

        holder.loadLocal(R.id.image, item);

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.OnItemClick(holder.getLayoutPosition());
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {

        void OnItemClick(int position);
    }

    public List<String> getData() {
        return mData;
    }
}
