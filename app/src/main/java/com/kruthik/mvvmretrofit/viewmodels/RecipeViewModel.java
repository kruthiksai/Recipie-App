package com.kruthik.mvvmretrofit.viewmodels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.kruthik.mvvmretrofit.models.Recipe;
import com.kruthik.mvvmretrofit.repositories.recipierepository;

public class RecipeViewModel extends ViewModel {

    private recipierepository mRecipeRepository;
    private String mRecipeId;
    private boolean mDidRetrieveRecipe;


    public RecipeViewModel() {

        mRecipeRepository = recipierepository.getInstance();
        mDidRetrieveRecipe = false;

    }

    public LiveData<Recipe> getRecipe(){

        return mRecipeRepository.getRecipie();
    }

    public LiveData<Boolean> isRecipeRequestTimedOut(){

        return mRecipeRepository.isRecipieTimeout();

    }

    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchrecipiebyid(recipeId);
    }

    public String getRecipeId() {
        return mRecipeId;
    }

    public void setRetrievedRecipe(boolean retrievedRecipe){
        mDidRetrieveRecipe = retrievedRecipe;
    }

    public boolean didRetrieveRecipe(){
        return mDidRetrieveRecipe;
    }
}
