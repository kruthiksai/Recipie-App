package com.kruthik.mvvmretrofit.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.kruthik.mvvmretrofit.models.Recipe;
import com.kruthik.mvvmretrofit.repositories.recipierepository;

import java.util.List;

public class MainViewmodel extends ViewModel {
    private recipierepository mrecipierepository;
    boolean viewingrecipies;
    boolean isperformingquery;
    public MutableLiveData<List<Recipe>> mRecipie=new MutableLiveData<>();

    public MainViewmodel(){
mrecipierepository=recipierepository.getInstance();
viewingrecipies=false;
isperformingquery=false;
        Log.d("viewmodel","created");
    }

    public boolean isViewingrecipies() {
        return viewingrecipies;
    }

    public void setViewingrecipies(boolean viewingrecipies) {

        this.viewingrecipies = viewingrecipies;
    }

    public LiveData<List<Recipe>> getRecipie(){
        return mrecipierepository.getRecipies();
    }

    public void searchRecipieApi(String query,int pagenumber){
        viewingrecipies=true;
        isperformingquery=true;
        mrecipierepository.searchRecipieApi(query,pagenumber);
    }

    public void searchnextpage(){
        if(!isperformingquery && viewingrecipies && !isexhauded().getValue()){


        mrecipierepository.searchnextpage();
        }
    }


    public boolean isIsperformingquery() {
        return isperformingquery;
    }

    public void setIsperformingquery(boolean isperformingquery) {
        this.isperformingquery = isperformingquery;
    }


public void canecelrequet(){
        mrecipierepository.cancelrequest();
 isperformingquery=false;
}
public LiveData<Boolean> isexhauded(){
        return mrecipierepository.isexhauster();
}
}
