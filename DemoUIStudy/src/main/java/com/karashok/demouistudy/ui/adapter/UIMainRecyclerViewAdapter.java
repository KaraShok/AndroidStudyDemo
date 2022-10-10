package com.karashok.demouistudy.ui.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.karashok.demouistudy.R;
import com.karashok.demouistudy.ui.entity.UIMainRecyclerEntity;

import java.util.List;

/**
 * @author KaraShokZ
 * @since 02-20-2022
 */
public class UIMainRecyclerViewAdapter extends BaseQuickAdapter<UIMainRecyclerEntity, BaseViewHolder> {

    public UIMainRecyclerViewAdapter(@Nullable List<UIMainRecyclerEntity> data) {
        super(R.layout.item_ui_main, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, UIMainRecyclerEntity entity) {
        holder.setText(R.id.item_ui_main_btn,entity.btnTextStr)
                .addOnClickListener(R.id.item_ui_main_btn);
    }

}
