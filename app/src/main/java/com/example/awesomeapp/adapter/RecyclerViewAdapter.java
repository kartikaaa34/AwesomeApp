package com.example.awesomeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awesomeapp.R;
import com.example.awesomeapp.model.Photo;
import com.example.awesomeapp.view.DetailActivity;
import com.example.awesomeapp.view.SplashScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Photo> contentArrayList;
    private int option;
    private static Context context;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public RecyclerViewAdapter(ArrayList<Photo> contentArrayList, int option, Context context) {
        this.contentArrayList = contentArrayList;
        this.option = option;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;

        if (option == 0) {
            View v1 = inflater.inflate(R.layout.item_adapter_horizontal, parent, false);
            viewHolder = new ViewHolder(v1);
        } else {
            View v1 = inflater.inflate(R.layout.item_adapter_vertical, parent, false);
            viewHolder = new ViewHolder(v1);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Photo content = contentArrayList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                ViewHolder contentViewHolder = (ViewHolder) holder;
                try {
                    Picasso.get().load(content.getSrc().getPortrait()).into(contentViewHolder.imageView);
                    contentViewHolder.name.setText(content.getPhotographer());
                    contentViewHolder.url.setText(content.getPhotographerUrl());

                    contentViewHolder.linearLayout.setOnClickListener(view -> {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("image", content.getSrc().getLandscape());
                        intent.putExtra("name", content.getPhotographer());
                        intent.putExtra("url", content.getPhotographerUrl());
                        context.startActivity(intent);
                    });
                } catch (Exception e) {
                    Picasso.get().load("https://images.pexels.com/photos/2014422/pexels-photo-2014422.jpeg").into(contentViewHolder.imageView);
                    contentViewHolder.name.setText(R.string.none);
                    contentViewHolder.url.setText(R.string.none);
                }
                break;
            case LOADING:
                break;

        }
    }

    @Override
    public int getItemCount() {
        return contentArrayList == null ? 0 : contentArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == contentArrayList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Photo r) {
        contentArrayList.add(r);
        notifyItemInserted(contentArrayList.size() - 1);
    }

    public void remove(Photo r) {
        int position = contentArrayList.indexOf(r);
        if (position > -1) {
            contentArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Photo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = contentArrayList.size() - 1;
        Photo result = getItem(position);

        if (result != null) {
            contentArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    Photo getItem(int position) {
        try {
            return contentArrayList.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView name;
        private TextView url;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            url = itemView.findViewById(R.id.url);
            linearLayout = itemView.findViewById(R.id.ll_card);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
