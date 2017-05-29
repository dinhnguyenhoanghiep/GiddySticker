package com.giddy.image.giddysticker.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.giddy.image.giddysticker.R;

import java.util.ArrayList;

/**
 * Created by HoangHiep on 5/16/2017.
 */

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ItemSticker> {
    private ArrayList<Integer> stickerResList = new ArrayList<>();
    private Context mContext;

    public StickerAdapter(ArrayList<Integer> stickerResList, Context mContext) {
        this.stickerResList = stickerResList;
        this.mContext = mContext;
    }

    @Override
    public ItemSticker onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item, parent, false);
        return new ItemSticker(view);
    }

    @Override
    public void onBindViewHolder(ItemSticker holder, final int position) {
        Glide.with(mContext)
                .load(stickerResList.get(position))
                .thumbnail(0.2f)
                .crossFade()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.stickerView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("com.receiver.add.sticker");
                intent.putExtra("sticker_id", position);
                mContext.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickerResList.size();
    }

    public class ItemSticker extends RecyclerView.ViewHolder {
        private ImageView stickerView;

        public ItemSticker(View itemView) {
            super(itemView);
            stickerView = (ImageView) itemView.findViewById(R.id.sticker);
        }
    }
}
