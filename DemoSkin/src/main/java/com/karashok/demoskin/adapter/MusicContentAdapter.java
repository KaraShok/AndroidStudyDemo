package com.karashok.demoskin.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.karashok.demoskin.R;

import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-18-2022
 */
public class MusicContentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MusicContentAdapter(@Nullable List<String> data) {
        super(R.layout.item_music_content, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name,"DemoMusic == " + item);
    }
}
