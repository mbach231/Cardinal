

package com.mbach231.cardinal.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

/**
 *
 * 
 */
public class CustomShapelessRecipe extends ShapelessRecipe {

    private final List<ItemStack> ingredientList_;
    
    public CustomShapelessRecipe(ItemStack result) {
        super(result);
        ingredientList_ = new ArrayList();
    }
    
    @Override
    public List<ItemStack> getIngredientList() {
        return ingredientList_;
    }
    
    @Override
    public ShapelessRecipe addIngredient(Material ingredient) {
        ingredientList_.add(new ItemStack(ingredient));
        return this;
    }
    
    @Override
    public ShapelessRecipe addIngredient(int count, Material ingredient) {
        ingredientList_.add(new ItemStack(ingredient, count));
        return this;
    }
    
    public ShapelessRecipe addIngredient(ItemStack itemStack) {
        ingredientList_.add(itemStack);
        return this;
    }
    
    public ShapelessRecipe addIngredient(int count, ItemStack itemStack) {
        itemStack = itemStack.clone();
        itemStack.setAmount(count);
        ingredientList_.add(itemStack);
        return this;
    }
    

}
