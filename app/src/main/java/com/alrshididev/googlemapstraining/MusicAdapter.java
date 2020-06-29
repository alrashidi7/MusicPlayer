package com.alrshididev.googlemapstraining;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.viewholder> {
    Context context;
    ArrayList<MusicFiles> musicFiles;

    public MusicAdapter(Context context, ArrayList<MusicFiles> musicFiles) {
        this.context = context;
        this.musicFiles = musicFiles;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
        holder.textView.setText(musicFiles.get(position).getTitle());
        byte[] image = getImage(musicFiles.get(position).getPath());
        if (image!= null){
        Glide.with(context).asBitmap().load(image).into(holder.imageView);
        }else{
            Glide.with(context).load(R.drawable.ic_music).into(holder.imageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PlayActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.musicImage);
            textView = itemView.findViewById(R.id.musicName);
        }
    }
    private byte[] getImage(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] artest = retriever.getEmbeddedPicture();
        retriever.release();
        return artest;
    }


}
