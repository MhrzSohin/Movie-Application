package edu.udemylearning.movieapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import edu.udemylearning.movieapplication.Activities.DetailActivity;
import edu.udemylearning.movieapplication.Domain.GenresItem;
import edu.udemylearning.movieapplication.Domain.ListFilm;
import edu.udemylearning.movieapplication.R;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
     ArrayList<GenresItem> genresItems;
     Context context;

    public CategoryListAdapter(ArrayList<GenresItem> genresItems) {
        this.genresItems = genresItems;
    }

    @NonNull
    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);
        return new ViewHolder(inflate) ;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.ViewHolder holder, int position) {
        holder.titleTxt.setText(genresItems.get(position).getName());
        holder.itemView.setOnClickListener(V->{

        });
    }

    @Override
    public int getItemCount() {
        return genresItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxtCategory);
        }
    }
}
