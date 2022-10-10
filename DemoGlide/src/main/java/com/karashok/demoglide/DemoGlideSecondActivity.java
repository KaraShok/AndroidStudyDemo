package com.karashok.demoglide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karashok.demoglide.glide.Glide;

public class DemoGlideSecondActivity extends AppCompatActivity {

    String[] url = {"https://scpic.chinaz.net/files/pic/pic9/202112/apic37600.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/201911/zzpic21409.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/202107/apic33798.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_glide_second);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ImageAdapter adapter = new ImageAdapter();
        recyclerView.setAdapter(adapter);
    }

    private final class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.test_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.title.setText(
                    String.valueOf(position));
            holder.imageView.setTag(position);
            Glide.with(DemoGlideSecondActivity.this)
                    .load(url[position % url.length])
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        public final class ViewHolder extends RecyclerView.ViewHolder {

            private final ImageView imageView;
            private final TextView title;

            ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.text);
                imageView = itemView.findViewById(R.id.icon);
            }
        }
    }
}