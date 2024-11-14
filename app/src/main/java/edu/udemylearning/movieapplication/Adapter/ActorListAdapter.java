package edu.udemylearning.movieapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.udemylearning.movieapplication.R;

public class ActorListAdapter extends RecyclerView.Adapter<ActorListAdapter.ViewHolder> {
    List<String> images;
    Context context;

    public ActorListAdapter(List<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ActorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_actor,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorListAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(images.get(position))
                .into(holder.actorImageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView actorImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            actorImageView = itemView.findViewById(R.id.itemImageViewHolder);
        }
    }
}
