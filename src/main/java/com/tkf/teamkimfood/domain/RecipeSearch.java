package com.tkf.teamkimfood.domain;

public class RecipeSearch {
    private String userNickName;
    private String RecipeTitle;

    //게터세터
    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getRecipeTitle() {
        return RecipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        RecipeTitle = recipeTitle;
    }
}
