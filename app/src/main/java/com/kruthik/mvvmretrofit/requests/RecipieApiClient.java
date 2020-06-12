package com.kruthik.mvvmretrofit.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.kruthik.mvvmretrofit.AppExecutors;
import com.kruthik.mvvmretrofit.models.Recipe;
import com.kruthik.mvvmretrofit.responses.RecipeResponse;
import com.kruthik.mvvmretrofit.responses.RecipeSearchResponse;
import com.kruthik.mvvmretrofit.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.kruthik.mvvmretrofit.util.Constants.BASE_URL;
import static com.kruthik.mvvmretrofit.util.Constants.Network_Timeout;

public class RecipieApiClient {
    private RetriveRecipieRunnable retriveRecipieRunnable;
    private RetriveRecipiesRunnable retriveRecipiesRunnable;
    private static final String TAG = "RecipieApiClient";
    private MutableLiveData<List<Recipe>> mRecipies;
    private MutableLiveData<Recipe> mRecipie;
    private  MutableLiveData<Boolean> recipietimeout=new MutableLiveData<>();
    private static RecipieApiClient instance;
    public  static RecipieApiClient getInstance(){
        if(instance==null){
            instance=new RecipieApiClient();

        }
            return instance;

    }
    private RecipieApiClient(){
        mRecipies=new MutableLiveData<>();
        mRecipie=new MutableLiveData<>();
    }
    public LiveData<List<Recipe>> getRecipies(){
        return mRecipies;
    }
    public LiveData<Recipe> getRecipie(){
        return mRecipie;
    }
    public LiveData<Boolean> isRecipieTimeout(){
        return recipietimeout;
    }

    public void searchrecipiesAPi(String query,int pagenumber){
        if(retriveRecipiesRunnable!=null){
            retriveRecipiesRunnable=null;
        }
        retriveRecipiesRunnable=new RetriveRecipiesRunnable(query,pagenumber);
        final Future handler= AppExecutors.getInstance().networkIO().submit(retriveRecipiesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        },Network_Timeout, TimeUnit.MILLISECONDS);
    }
    public void searchrecipiebyid(String recipieID){
        if(retriveRecipieRunnable!=null){
            retriveRecipieRunnable=null;
        }
        retriveRecipieRunnable=new RetriveRecipieRunnable(recipieID);
        final Future handler= AppExecutors.getInstance().networkIO().submit(retriveRecipieRunnable);
        recipietimeout.setValue(false);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
if(handler.isDone()){
Log.d("test","done");
}else{
    recipietimeout.postValue(true);
    handler.cancel(true);
}



            }
        },Network_Timeout, TimeUnit.MILLISECONDS);
    }
    private class RetriveRecipiesRunnable implements Runnable{
            private String query;
            private int pageNumber;
            boolean cancelRequest;

        public RetriveRecipiesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest=false;
        }

        @Override
        public void run() {
            try {
                Response response=getrecipies(query,pageNumber).execute();

                if(cancelRequest){
                    return;
                }
                if(response.code()==200){
                    List<Recipe> list=new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    if(pageNumber==1){

                        mRecipies.postValue(list);

                    }
                    else {
                       // Log.d("exhaust",""+pageNumber);
                        List<Recipe> currentRecipie=mRecipies.getValue();

                        currentRecipie.addAll(list);
                        mRecipies.postValue(currentRecipie);
                    }
                }
                else{

                    String error=response.errorBody().string();
                    Log.e(TAG,"run"+error);
                    mRecipies.postValue(null);
                }
            } catch (IOException e) {

                e.printStackTrace();
                mRecipies.postValue(null);

            }


        }
//private boolean network(){
//
//}
        private Call<RecipeSearchResponse> getrecipies(String query, int pageNumber){
            return ServiceGenerator.getRecipeApi().searchRecipe(BASE_URL,query,String.valueOf(pageNumber));
        }
        private void CancelRequest(){
            Log.d(TAG,"cancel Request : canceling search");
            cancelRequest=true;
        }
    }
    public  void cancelrequest(){
        if(retriveRecipiesRunnable!=null) {


            retriveRecipiesRunnable.cancelRequest = true;

        }
        if(retriveRecipieRunnable!=null) {


            retriveRecipieRunnable.cancelRequest = true;

        }
    }
    private class RetriveRecipieRunnable implements Runnable{
        private String recipieId;

        boolean cancelRequest;

        public RetriveRecipieRunnable(String recipieId) {
            this.recipieId = recipieId;

            cancelRequest=false;
        }

        @Override
        public void run() {
            try {
                Response response=getrecipie(recipieId).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code()==200){
                    Recipe recipe=((RecipeResponse)response.body()).getRecipe();
                    mRecipie.postValue(recipe);
                    return;

                }
                else{
                    String error=response.errorBody().string();
                    Log.e(TAG,"run"+error);
                   mRecipie.postValue(null);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipie.postValue(null);
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return;
            }


        }

        private Call<RecipeResponse> getrecipie(String recipieId){
            return ServiceGenerator.getRecipeApi().getRecipe("",recipieId);
        }
        private void CancelRequest(){
            Log.d(TAG,"cancel Request : canceling search");
            cancelRequest=true;

        }
    }
}
