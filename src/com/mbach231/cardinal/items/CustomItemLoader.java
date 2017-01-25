package com.mbach231.cardinal.items;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.items.enchanting.EnchantmentManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/**
 *
 *
 */
public class CustomItemLoader {

    private final FileConfiguration config_;
    private static Map<String, ItemStack> customItemMap_;
    private final Map<ItemStack, Map<ItemStack, ItemStack>> customBrewMap_;

    private enum RecipeType {

        SHAPED,
        SHAPELESS,
        BREWING
    }

    CustomItemLoader() {

        customItemMap_ = new HashMap();
        customBrewMap_ = new HashMap();

        config_ = ConfigManager.getItemsConfig();

        Set<Map.Entry<String, Object>> itemNameSet = config_.getConfigurationSection("Items").getValues(false).entrySet();

        for (Map.Entry<String, Object> entry : itemNameSet) {
            loadItem(entry.getKey());
        }

        for (Map.Entry<String, Object> entry : itemNameSet) {
            loadRecipe(entry.getKey());
        }

    }

    public Map<String, ItemStack> getCustomItemMap() {
        return customItemMap_;
    }

    public Map<ItemStack, Map<ItemStack, ItemStack>> getCustomBrewMap() {
        return customBrewMap_;
    }

    private void loadItem(String name) {
        String path = "Items." + name;

        Material material = Material.valueOf(config_.getString(path + ".Material"));
        boolean glowing;

        if (config_.contains(path + ".Glowing")) {
            glowing = config_.getBoolean(path + ".Glowing");
        } else {
            glowing = false;
        }

        ItemStack item = new ItemStack(material);
        item = applyName(name, item);

        if (glowing) {
            item = applyGlow(item);
        }

        /*
         if(material.equals(Material.POTION)) {
         PotionMeta potionMeta = ((PotionMeta)item.getItemMeta());
         }
         */
        customItemMap_.put(name, item);

    }

    private void loadRecipe(String name) {

        Recipe recipe = null;
        String path = "Items." + name + ".Recipe";
        if (customItemMap_.containsKey(name) && config_.contains(path)) {
            ItemStack item = customItemMap_.get(name);
            path = path + ".";

            try {
                RecipeType recipeType = RecipeType.valueOf(config_.getString(path + "RecipeType"));

                boolean loadRecipe = true;

                switch (recipeType) {
                    case SHAPED:
                        recipe = loadShapedRecipe(path, item);
                        break;

                    case SHAPELESS:
                        recipe = loadShapelessRecipe(path, item);
                        break;

                    case BREWING:
                        loadRecipe = false;
                        loadBrewingRecipe(path, item);
                        break;

                    default:
                        CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load recipe using RecipeType: " + recipeType);
                }

                if (loadRecipe && recipe != null) {
                    Bukkit.getServer().addRecipe(recipe);
                } else if (loadRecipe && recipe == null) {
                    CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load recipe for: " + name);
                }

            } catch (Exception e) {

            }

        }
    }

    private Recipe loadShapedRecipe(String path, ItemStack item) {
        CustomShapedRecipe shapedRecipe = new CustomShapedRecipe(item);
        shapedRecipe.shape(config_.getStringList(path + "Shape").toArray(new String[0]));

        for (String ingredientStr : config_.getConfigurationSection(path + "Ingredients").getValues(false).keySet()) {

            char key = config_.getString(path + "Ingredients." + ingredientStr).charAt(0);

            // Attempt to load item as a material
            try {
                shapedRecipe.setIngredient(key, Material.valueOf(ingredientStr));
                continue;
            } catch (Exception e) {
            }

            // If here, attempt to load custom item
            if (customItemMap_.containsKey(ingredientStr)) {
                shapedRecipe.setIngredient(key, customItemMap_.get(ingredientStr));
            } else {
                return null;
            }
        }

        return shapedRecipe;
    }

    private Recipe loadShapelessRecipe(String path, ItemStack item) {
        CustomShapelessRecipe shapelessRecipe = new CustomShapelessRecipe(item);

        for (String ingredientStr : config_.getConfigurationSection(path + "Ingredients").getValues(false).keySet()) {

            int amount = config_.getInt(path + "Ingredients." + ingredientStr);

            // Attempt to load item as a material
            try {
                shapelessRecipe.addIngredient(amount, Material.valueOf(ingredientStr));
                continue;
            } catch (Exception e) {
            }

            // If here, attempt to load custom item
            if (customItemMap_.containsKey(ingredientStr)) {
                shapelessRecipe.addIngredient(amount, customItemMap_.get(ingredientStr));
            } else {
                return null;
            }
        }

        return shapelessRecipe;
    }

    private ItemStack loadItemStack(String itemName) {

        if (customItemMap_.containsKey(itemName)) {
            return customItemMap_.get(itemName);
        } else {

            try {
                ItemStack item = new ItemStack(Material.valueOf(itemName),1);
                
                // Set metadata if potion
                if(item.getType().equals(Material.POTION)) {
                    PotionMeta meta = (PotionMeta)item.getItemMeta();
                    meta.setBasePotionData(new PotionData(PotionType.WATER));
                    item.setItemMeta(meta);
                }
                
                return item;
            } catch (Exception e) {

            }

        }
        return null;
    }

    private void loadBrewingRecipe(String path, ItemStack item) {
        ItemStack ingredient = loadItemStack(config_.getString(path + "Ingredient"));

        if (ingredient == null) {
            CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load ingredent for: " + path);
            return;
        }

        ItemStack content = loadItemStack(config_.getString(path + "Content"));

        if (content == null) {
            CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load content for: " + path);
            return;
        }

        addBrew(ingredient, content, item);
    }

    private void addBrew(ItemStack ingredient, ItemStack content, ItemStack result) {

        Map<ItemStack, ItemStack> ingredientMap;
        if (customBrewMap_.containsKey(ingredient)) {
            ingredientMap = customBrewMap_.get(ingredient);
        } else {
            ingredientMap = new HashMap();
        }
        ingredientMap.put(content, result);
        customBrewMap_.put(ingredient, ingredientMap);
    }

    private ItemStack applyName(String name, ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private ItemStack applyGlow(ItemStack itemStack) {
        return EnchantmentManager.applyGlow(itemStack);
    }

}
