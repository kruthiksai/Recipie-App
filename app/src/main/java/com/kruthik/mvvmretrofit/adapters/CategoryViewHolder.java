package com.kruthik.mvvmretrofit.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kruthik.mvvmretrofit.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
CircleImageView circleImageView;
TextView Categorytitle;

    OnRecipieListener listener;
    public CategoryViewHolder(@NonNull View itemView,OnRecipieListener listener) {
        super(itemView);
        this.listener=listener;
        this.circleImageView=itemView.findViewById(R.id.categoryimage);
        Categorytitle=itemView.findViewById(R.id.categorytitle);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
listener.onCatgoryClick(Categorytitle.getText().toString());
    }
}
