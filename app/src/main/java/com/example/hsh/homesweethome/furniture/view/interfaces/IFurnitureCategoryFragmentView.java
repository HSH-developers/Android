package com.example.hsh.homesweethome.furniture.view.interfaces;

import com.example.hsh.homesweethome.Models.Furniture;

import java.util.ArrayList;

public interface IFurnitureCategoryFragmentView {
    void displayCategoryFurniture(ArrayList<Furniture> furniture, ArrayList<String> locations);
    void displayFurnitureCategory(String category);
    void setFilterView();
}
