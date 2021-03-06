package com.example.hsh.homesweethome.furniture.presenter.interfaces;

import android.content.Context;

import com.example.hsh.homesweethome.furniture.view.interfaces.RecyclerViewHolderCategoryFurniture;

public interface IRecyclerViewAdapterMainPresenter {
    void onBindFurnitureCategoryAtPosition(int position, RecyclerViewHolderCategoryFurniture viewHolder);
    int getFurnitureCategoriesSize();
    void clearCategories();
    Context getActivityContext();
}
