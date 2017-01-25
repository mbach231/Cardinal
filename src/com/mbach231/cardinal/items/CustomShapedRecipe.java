

package com.mbach231.cardinal.items;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 *
 * 
 */
public class CustomShapedRecipe extends ShapedRecipe {

    private final Map<Character, ItemStack> ingredientMap_;
    
    public CustomShapedRecipe(ItemStack result) {
        super(result);
        ingredientMap_ = new HashMap();
    }
    
    @Override
    public Map<Character, ItemStack> getIngredientMap() {
        return ingredientMap_;
    }
    
    @Override
    public ShapedRecipe setIngredient(char key, Material ingredient) {
        ingredientMap_.put(key, new ItemStack(ingredient));
        return this;
    }
    
    public ShapedRecipe setIngredient(char key, ItemStack itemStack) {
        ingredientMap_.put(key, itemStack);
        return this;
    }

}
