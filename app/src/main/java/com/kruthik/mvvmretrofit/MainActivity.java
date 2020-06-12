package com.kruthik.mvvmretrofit;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kruthik.mvvmretrofit.adapters.OnRecipieListener;
import com.kruthik.mvvmretrofit.adapters.RecipieRecylerAdapter;
import com.kruthik.mvvmretrofit.models.Recipe;
import com.kruthik.mvvmretrofit.requests.RecipeApi;
import com.kruthik.mvvmretrofit.requests.ServiceGenerator;
import com.kruthik.mvvmretrofit.responses.RecipeResponse;
import com.kruthik.mvvmretrofit.responses.RecipeSearchResponse;
import com.kruthik.mvvmretrofit.util.Constants;
import com.kruthik.mvvmretrofit.util.recyclerviewspacing;
import com.kruthik.mvvmretrofit.viewmodels.MainViewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements OnRecipieListener {
    private static final String TAG = "RecipeListActivity";
    private MainViewmodel mviewmodel ;
    private RecyclerView recyclerView;
     SearchView searchView;
    private RecipieRecylerAdapter recylerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       recyclerView=findViewById(R.id.recipie_list);
        searchView   =findViewById(R.id.searchview);

mviewmodel= ViewModelProviders.of(this).get(MainViewmodel.class);
subscribeobservers();
initrecyclerview();



        initsearchview();
        if(!mviewmodel.isViewingrecipies()){
            displaySearchcategory();
        }
    }

    void initrecyclerview(){
        recylerAdapter=new RecipieRecylerAdapter(this);
        recyclerView.setAdapter(recylerAdapter);
        recyclerviewspacing recyclerviewspacing=new recyclerviewspacing(20);
        recyclerView.addItemDecoration(recyclerviewspacing);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
              if(!recyclerView.canScrollVertically(1)){
mviewmodel.searchnextpage();
              }
            }
        });


    }
    void initsearchview(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                recylerAdapter.displayloading();


                mviewmodel.searchRecipieApi(s,1);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
    private void subscribeobservers(){
        mviewmodel.getRecipie().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
              //  Toast.makeText(getApplicationContext(),"jrr",Toast.LENGTH_LONG).show();
                Log.d("viewmodel","changed");
                if(recipes!=null){
                    if(mviewmodel.isViewingrecipies()){


                    recylerAdapter.setmRecipies(recipes);
                    mviewmodel.setIsperformingquery(false);
                    }
                }


            }
        });
        mviewmodel.isexhauded().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                   // Log.d("exhaust","exhaust");
                   recylerAdapter.setqueryexhausted();
                }
            }
        });
    }
    public void searchRecipieApi(String query,int pagenumber){

        mviewmodel.searchRecipieApi(query,pagenumber);
    }
    private void testRetrofitRequest(){
        searchRecipieApi("chicken",1);
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent= new Intent(this,RecipeActivity.class);
        intent.putExtra("recipe",recylerAdapter.getselectitem(position));
        startActivity(intent);
    }

    @Override
    public void onCatgoryClick(String category) {
        recylerAdapter.displayloading();
        mviewmodel.searchRecipieApi(category,1);
        searchView.clearFocus();
    }
    private  void displaySearchcategory(){
        mviewmodel.setViewingrecipies(false);
        recylerAdapter.displaycategories();
    }

    @Override
    public void onBackPressed() {
        if(mviewmodel.isIsperformingquery()){
mviewmodel.canecelrequet();
        }
        if(mviewmodel.isViewingrecipies()){
            displaySearchcategory();

        }else{
            super.onBackPressed();

        }

    }


}
