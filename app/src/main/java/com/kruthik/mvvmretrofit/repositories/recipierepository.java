package com.kruthik.mvvmretrofit.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kruthik.mvvmretrofit.models.Recipe;
import com.kruthik.mvvmretrofit.requests.RecipieApiClient;

import java.util.List;

public class recipierepository {


  private static   recipierepository instance;
  private RecipieApiClient recipieApiClient;
  String query;
  int pagenumber;
  MutableLiveData<Boolean> isexhausted = new MutableLiveData<>();

 MediatorLiveData<List<Recipe>>  res= new MediatorLiveData<List<Recipe>>();


    public static recipierepository getInstance(){
        if(instance==null){
            instance=new recipierepository();

        }
        return instance;
    }
    private recipierepository(){

        recipieApiClient=RecipieApiClient.getInstance();
       initmediator();
    }
    public void initmediator(){

        LiveData<List<Recipe>> recipieApiSource=recipieApiClient.getRecipies();

        res.addSource(recipieApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
if(recipes!=null){
    res.setValue(recipes);
donequery(recipes);
}else{
    donequery(null);
}
            }
        });
    }
    public LiveData<Boolean> isexhauster(){
        return isexhausted;
    }
    public  void donequery(List<Recipe> list){
        if(list!=null){
            if(list.size()%30!=0){
                isexhausted.setValue(true);
            }
        }else{
            isexhausted.setValue(true);
        }

    }

    public LiveData<List<Recipe>> getRecipies(){

        Log.d("livedata","created");
        return res;

    }
    public LiveData<Recipe> getRecipie(){

        return recipieApiClient.getRecipie();
    }
    public void searchRecipieApi(String query,int pagenumber){
        if(pagenumber==0){
            pagenumber=1;
        }



        this.query=query;
        this.pagenumber=pagenumber;
        isexhausted.setValue(false);
        recipieApiClient.searchrecipiesAPi(query,pagenumber);

    }
    public void searchrecipiebyid(String recipieId){
recipieApiClient.searchrecipiebyid(recipieId);
    }
    public void searchnextpage(){
        searchRecipieApi(query,pagenumber+1);
    }

    public  void cancelrequest(){recipieApiClient.cancelrequest();
    }
    public LiveData<Boolean> isRecipieTimeout(){
        return recipieApiClient.isRecipieTimeout();
    }
}
