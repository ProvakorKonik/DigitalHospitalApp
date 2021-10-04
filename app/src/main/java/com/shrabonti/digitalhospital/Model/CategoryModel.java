package com.shrabonti.digitalhospital.Model;

public class CategoryModel {
    private String CategoryUID = "NO";
    private String CategoryName = "NO";
    private String CategoryPhotoUrl = "NO";
    private String CategoryBio = "NO";
    private String CategoryCreator = "NO";
    private long CategoryiViews = 0;
    private long CategoryiPriority = 0;
    private long CategoryiClicked = 0;

    public CategoryModel() {
    }

    public CategoryModel(String categoryUID, String categoryName, String categoryPhotoUrl, String categoryBio, String categoryCreator, long categoryiViews, long categoryiPriority, long categoryiClicked) {
        CategoryUID = categoryUID;
        CategoryName = categoryName;
        CategoryPhotoUrl = categoryPhotoUrl;
        CategoryBio = categoryBio;
        CategoryCreator = categoryCreator;
        CategoryiViews = categoryiViews;
        CategoryiPriority = categoryiPriority;
        CategoryiClicked = categoryiClicked;
    }

    public String getCategoryUID() {
        return CategoryUID;
    }

    public void setCategoryUID(String categoryUID) {
        CategoryUID = categoryUID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryPhotoUrl() {
        return CategoryPhotoUrl;
    }

    public void setCategoryPhotoUrl(String categoryPhotoUrl) {
        CategoryPhotoUrl = categoryPhotoUrl;
    }

    public String getCategoryBio() {
        return CategoryBio;
    }

    public void setCategoryBio(String categoryBio) {
        CategoryBio = categoryBio;
    }

    public String getCategoryCreator() {
        return CategoryCreator;
    }

    public void setCategoryCreator(String categoryCreator) {
        CategoryCreator = categoryCreator;
    }

    public long getCategoryiViews() {
        return CategoryiViews;
    }

    public void setCategoryiViews(long categoryiViews) {
        CategoryiViews = categoryiViews;
    }

    public long getCategoryiPriority() {
        return CategoryiPriority;
    }

    public void setCategoryiPriority(long categoryiPriority) {
        CategoryiPriority = categoryiPriority;
    }

    public long getCategoryiClicked() {
        return CategoryiClicked;
    }

    public void setCategoryiClicked(long categoryiClicked) {
        CategoryiClicked = categoryiClicked;
    }
}
