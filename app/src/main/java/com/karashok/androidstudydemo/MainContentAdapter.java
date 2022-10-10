package com.karashok.androidstudydemo;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-19-2022
 */
public class MainContentAdapter extends BaseQuickAdapter<ContentData, BaseViewHolder> {

    public MainContentAdapter(List<ContentData> data) {
        super(R.layout.item_activity_main, data);
//        addChildClickViewIds(R.id.item_content_btn);
    }

    @Override
    protected void convert(BaseViewHolder holder, ContentData contentData) {
        holder.setText(R.id.item_content_btn,contentData.text)
                .addOnClickListener(R.id.item_content_btn);
    }
}
