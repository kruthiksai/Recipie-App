package com.kruthik.mvvmretrofit.adapters;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.kruthik.mvvmretrofit.MainActivity;
import com.kruthik.mvvmretrofit.R;
import com.kruthik.mvvmretrofit.models.Recipe;
import com.kruthik.mvvmretrofit.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecipieRecylerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
     private List<Recipe> mRecipies;
     private OnRecipieListener onRecipieListener;
     private  static final int Recipie_type=1;
     private static final int Loading_type=2;
     public static final int Category_type=3;
    public static final int Exhausted_type=4;
    public RecipieRecylerAdapter( OnRecipieListener onRecipieListener) {
        this.onRecipieListener = onRecipieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=null;
        switch (i){
            case Recipie_type:
            v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item,viewGroup,false);
            RecipieViewHolder recipieViewHolder=new RecipieViewHolder(v,onRecipieListener);
                return recipieViewHolder;

            case Loading_type:
                v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_list_item,viewGroup,false);

                return new LoadingViewHolder(v);
            case Exhausted_type:
                v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exhaustedlayout,viewGroup,false);

                return new SearchExhaustedViewHolder(v);

            case Category_type:
                v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list_item,viewGroup,false);

                return new CategoryViewHolder(v,onRecipieListener);
                default:
                    v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item,viewGroup,false);
                    return new RecipieViewHolder(v,onRecipieListener);
        }






    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        int itemtype=getItemViewType(i);
        if(itemtype==Recipie_type){
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(viewHolder.itemView.getContext());
            circularProgressDrawable.setStrokeWidth(8f);

            circularProgressDrawable.setCenterRadius(50f);
            circularProgressDrawable.start();

            RequestOptions requestOptions=new RequestOptions().placeholder(circularProgressDrawable);
            Glide.with(viewHolder.itemView.getContext()).setDefaultRequestOptions(requestOptions).load(mRecipies.get(i).getImage_url()).into( ((RecipieViewHolder)viewHolder).image);

            ((RecipieViewHolder)viewHolder).title.setText(mRecipies.get(i).getTitle());

            ((RecipieViewHolder)viewHolder).publisher.setText(mRecipies.get(i).getPublisher());
            ((RecipieViewHolder)viewHolder).socialScore.setText(String.valueOf(Math.round(mRecipies.get(i).getSocial_rank())));

        }
        else if(itemtype==Category_type){
            RequestOptions requestOptions=new RequestOptions().placeholder(R.drawable.ic_launcher_background);
Uri uripath=Uri.parse("android.resource://com.kruthik.mvvmretrofit/drawable/"+mRecipies.get(i).getImage_url());
            Glide.with(viewHolder.itemView.getContext()).setDefaultRequestOptions(requestOptions).load(uripath).into( ((CategoryViewHolder)viewHolder).circleImageView);

            ((CategoryViewHolder)viewHolder).Categorytitle.setText(mRecipies.get(i).getTitle());


        }
         }

    @Override
    public int getItemCount() {
        if(mRecipies!=null  ){
            return mRecipies.size();
        }
        return 0;
    }
    public void  setmRecipies(List<Recipe> recipe){
        mRecipies=recipe;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(mRecipies.get(position).getSocial_rank()==-1){
            return Category_type;
        }
        else if(mRecipies.get(position).getTitle().equals("Loading....")){
            return Loading_type;
        }
        else if(mRecipies.get(position).getTitle().equals("Exhausted....")){
            return Exhausted_type;
        }

        else if(position==mRecipies.size()-1 && position!=0 && !mRecipies.get(position).getTitle().equals("Exhausted....")  ){
            return Loading_type;
       }
        else {
            return Recipie_type;
        }
    }

    public void displayloading(){

        if(!isLoading()){
            Recipe recipe=new Recipe();
            recipe.setTitle("Loading....");
            List<Recipe> loadingList=new ArrayList<Recipe>();
            loadingList.add(recipe);
            mRecipies=loadingList;
            notifyDataSetChanged();
        }

    }
    public void displaycategories(){
        List<Recipe> temp=new ArrayList<Recipe>();
        for(int i=0;i< Constants.DEFAULT_SEARCH_CATEGORIES.length;i++){
            Recipe recipe=new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            temp.add(recipe);
        }
        mRecipies=temp;
        notifyDataSetChanged();
    }
    public Recipe getselectitem(int position){
        if(mRecipies!=null){
            if(mRecipies.size()>0){
                return mRecipies.get(position);
            }
        }
        return null;
    }
    public  void setqueryexhausted(){
        for(Recipe recipe:mRecipies){
            if(recipe.getTitle().equals("Exhausted....")){
                return;
            }
        }
        hideLoading();
        Recipe exhustedRecipie=new Recipe();
        exhustedRecipie.setTitle("Exhausted....");
        mRecipies.add(exhustedRecipie);
        notifyDataSetChanged();
    }
    private  void hideLoading(){
        if(isLoading()){
            for(Recipe recipe:mRecipies){
                if(recipe.getTitle().equals("Loading....")){
                    mRecipies.remove(recipe);
                }

            }
            notifyDataSetChanged();
        }

    }
    private boolean isLoading(){
        if(mRecipies!=null) {
            if (mRecipies.size() > 0) {
                if (mRecipies.get(mRecipies.size() - 1).getTitle().equals("Loading....")) {
                    return true;
                }
            }
        }

            return false;

    }
}
