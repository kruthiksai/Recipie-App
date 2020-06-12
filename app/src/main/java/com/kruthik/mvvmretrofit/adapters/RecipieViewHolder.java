package com.kruthik.mvvmretrofit.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kruthik.mvvmretrofit.R;

public class RecipieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView title,publisher,socialScore;
    ProgressBar progressBar;
    ImageView image;
    OnRecipieListener onRecipieListener;
    public RecipieViewHolder(@NonNull View itemView,OnRecipieListener onRecipieListener) {
        super(itemView);
        this.onRecipieListener=onRecipieListener;
        progressBar=itemView.findViewById(R.id.progress_bar);
        title=itemView.findViewById(R.id.recipe_title);
        publisher=itemView.findViewById(R.id.recipe_publisher);
        socialScore=itemView.findViewById(R.id.recipe_social_score);
        image=itemView.findViewById(R.id.recipe_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
onRecipieListener.onRecipeClick(getAdapterPosition());
    }
}
