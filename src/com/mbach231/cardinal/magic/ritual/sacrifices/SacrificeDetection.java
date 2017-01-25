package com.mbach231.cardinal.magic.ritual.sacrifices;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.items.CustomItemListener;
import com.mbach231.cardinal.magic.ritual.circles.CircleSizes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class SacrificeDetection {

    private final int DETECTION_RADIUS = CircleSizes.PILLAR_RADIUS;
    public String detectionString_;
    Map<Material, Integer> materialSacrificeMap_;
    Map<ItemStack, Integer> customItemSacrificeMap_;
    Map<EntityType, Integer> creatureSacrificeMap_;

    Map<Material, Set<Entity>> itemMap_;
    Map<ItemStack, Set<Entity>> customItemMap_;
    Map<EntityType, Set<Entity>> creatureMap;

    Set<Sacrifice> detectedSacrifices_;
    Set<Item> detectedItems_;
    Set<Item> detectedCustomItems_;
    Set<Creature> detectedCreatures_;
    Set<Item> purgeItems_;
    Set<Item> purgeCustomItems_;
    Set<Creature> purgeCreatures_;

    public SacrificeDetection() {

        detectionString_ = "";

        materialSacrificeMap_ = new HashMap();
        customItemSacrificeMap_ = new HashMap();
        creatureSacrificeMap_ = new HashMap();

        itemMap_ = new HashMap();
        customItemMap_ = new HashMap();
        creatureMap = new HashMap();

        detectedSacrifices_ = new HashSet();
        detectedItems_ = new HashSet();
        detectedCustomItems_ = new HashSet();
        detectedCreatures_ = new HashSet();

        purgeItems_ = new HashSet();
        purgeCustomItems_ = new HashSet();
        purgeCreatures_ = new HashSet();

    }

    public Set<Sacrifice> getSacrifices() {
        return detectedSacrifices_;
    }

    private void initializeDetections() {

        detectionString_ = "";

        materialSacrificeMap_.clear();
        customItemSacrificeMap_.clear();
        creatureSacrificeMap_.clear();
        detectedSacrifices_.clear();
        detectedItems_.clear();
        detectedCustomItems_.clear();
        detectedCreatures_.clear();
    }

    private void updateSacrifices(Entity entity) {
        if (entity instanceof Creature) {
            updateSacrifices((Creature) entity);
        } else if (entity instanceof Item) {
            updateSacrifices((Item) entity);
        }
    }

    private void updateSacrifices(Creature creature) {

        detectedCreatures_.add(creature);

        EntityType creatureType = creature.getType();

        Set<Entity> creatureSet;
        if (creatureMap.containsKey(creatureType)) {
            creatureSet = creatureMap.get(creatureType);
        } else {
            creatureSet = new HashSet();
        }

        creatureSet.add(creature);
        creatureMap.put(creatureType, creatureSet);

        if (!creatureSacrificeMap_.containsKey(creature.getType())) {
            creatureSacrificeMap_.put(creature.getType(), 1);
        } else {
            creatureSacrificeMap_.put(creature.getType(), 1 + creatureSacrificeMap_.get(creature.getType()));
        }
    }

    private void updateSacrifices(Item item) {
        detectedItems_.add(item);
        ItemStack itemStack = item.getItemStack();
        if (CustomItemListener.isCustomItem(itemStack)) {
            detectedCustomItems_.add(item);

            Set<Entity> itemSet;
            if (customItemMap_.containsKey(itemStack)) {
                itemSet = customItemMap_.get(itemStack);
            } else {
                itemSet = new HashSet();
            }
            itemSet.add(item);
            customItemMap_.put(itemStack, itemSet);

            if (!customItemSacrificeMap_.containsKey(itemStack)) {
                customItemSacrificeMap_.put(itemStack, itemStack.getAmount());
            } else {
                customItemSacrificeMap_.put(itemStack, itemStack.getAmount() + customItemSacrificeMap_.get(itemStack));
            }
        } else {
            
            Set<Entity> itemSet;
            if (itemMap_.containsKey(itemStack.getType())) {
                itemSet = itemMap_.get(itemStack.getType());
            } else {
                itemSet = new HashSet();
            }
            itemSet.add(item);
            itemMap_.put(itemStack.getType(), itemSet);

            if (!materialSacrificeMap_.containsKey(itemStack.getType())) {
                materialSacrificeMap_.put(itemStack.getType(), itemStack.getAmount());
            } else {
                materialSacrificeMap_.put(itemStack.getType(), itemStack.getAmount() + materialSacrificeMap_.get(itemStack.getType()));
            }
        }
    }

    private void updateSacrifices() {
        for (Map.Entry<Material, Set<Entity>> entry : itemMap_.entrySet()) {
            detectedSacrifices_.add(new Sacrifice(entry.getKey(), entry.getValue()));
        }
        itemMap_.clear();

        for (Map.Entry<ItemStack, Set<Entity>> entry : customItemMap_.entrySet()) {
            detectedSacrifices_.add(new Sacrifice(entry.getKey(), entry.getValue()));
        }
        customItemMap_.clear();
        
        for (Map.Entry<EntityType, Set<Entity>> entry : creatureMap.entrySet()) {
            detectedSacrifices_.add(new Sacrifice(entry.getKey(), entry.getValue()));
        }
        creatureMap.clear();
        /*
         for (Map.Entry<Material, Integer> entry : materialSacrificeMap.entrySet()) {
         detectedSacrifices.add(new Sacrifice(entry.getKey(), entry.getValue()));
         }

         for (Map.Entry<EntityType, Integer> entry : creatureSacrificeMap.entrySet()) {
         detectedSacrifices.add(new Sacrifice(entry.getKey(), entry.getValue()));
         }
         */
    }

    public void detect(Block block) {
        initializeDetections();

        Location blockLocation = block.getLocation();
        Entity[] nearbyEntities = getNearbyEntities(blockLocation, DETECTION_RADIUS);

        double distance;

        for (Entity entity : nearbyEntities) {

            // If not an item, skip
            if (!(entity instanceof Item) && !(entity instanceof Creature)) {
                continue;
            }

            distance = Math.sqrt(Math.pow(blockLocation.getBlockX() - entity.getLocation().getBlockX(), 2)
                    + Math.pow(blockLocation.getBlockZ() - entity.getLocation().getBlockZ(), 2));
            // If entity is in range, add as sacrifices
            if (distance < (double) DETECTION_RADIUS) {
                updateSacrifices(entity);
            }
        }

        updateSacrifices();
    }

    private Entity[] getNearbyEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {

                    if (l.getWorld() != e.getWorld()) {
                        continue;
                    }

                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) {
                        radiusEntities.add(e);
                    }
                }
            }
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public boolean hasSacrifices() {
        return !detectedSacrifices_.isEmpty();
    }

    public void purgeDetectedEntities(Set<Sacrifice> sacrifices) {

        purgeDetectedItems(sacrifices);

        purgeDetectedCreatures(sacrifices);

    }

    private void purgeDetectedItems(Set<Sacrifice> sacrifices) {
        Set<Item> tempDetectedItems = detectedItems_;

        // Iterate through all valid sacrifices
        for (Sacrifice sacrifice : sacrifices) {

            if (sacrifice.isItem()) {
                if (materialSacrificeMap_.containsKey(sacrifice.getMaterial())) {
                    int numItemsNeeded = materialSacrificeMap_.get(sacrifice.getMaterial());
                    Item foundItem;
                    while (numItemsNeeded > 0) {

                        foundItem = null;
                        for (Item item : tempDetectedItems) {
                            if (item.getItemStack().getType() == sacrifice.getMaterial()) {
                                foundItem = item;
                                break;
                            }
                        }
                        // Don't think this should be reachable
                        if (foundItem == null) {
                            break;
                        }
                        purgeItems_.add(foundItem);
                        detectedItems_.remove(foundItem);
                        numItemsNeeded--;
                    }
                }

            } else if (sacrifice.isCustomItem()) {
                ItemStack customItem = sacrifice.getCustomItem();
                if (customItemSacrificeMap_.containsKey(customItem)) {
                    int numItemsNeeded = customItemSacrificeMap_.get(customItem);
                    Item foundItem;

                    while (numItemsNeeded > 0) {

                        foundItem = null;
                        for (Item item : tempDetectedItems) {
                            if (item.getItemStack().getItemMeta().equals(customItem.getItemMeta())) {
                                foundItem = item;
                                break;
                            }
                        }
                        // Don't think this should be reachable
                        if (foundItem == null) {
                            break;
                        }
                        purgeCustomItems_.add(foundItem);
                        detectedCustomItems_.remove(foundItem);
                        numItemsNeeded--;
                    }
                }
            }
        }

        for (Item item : purgeItems_) {
            item.remove();
        }
        purgeItems_.clear();

        for (Item item : purgeCustomItems_) {
            item.remove();
        }
        purgeCustomItems_.clear();

    }

    private void purgeDetectedCreatures(Set<Sacrifice> sacrifices) {
        Set<Creature> tempDetectedCreatures = detectedCreatures_;

        // Iterate through all valid sacrifices
        for (Sacrifice sacrifice : sacrifices) {

            if (!creatureSacrificeMap_.containsKey(sacrifice.getCreatureType())) {
                continue;
            }

            int numNeeded = creatureSacrificeMap_.get(sacrifice.getCreatureType());
            Creature foundCreature;
            while (numNeeded > 0) {

                foundCreature = null;
                for (Creature creature : tempDetectedCreatures) {
                    if (creature.getType() == sacrifice.getCreatureType()) {
                        foundCreature = creature;
                        break;
                    }
                }
                // Don't think this should be reachable
                if (foundCreature == null) {
                    break;
                }
                purgeCreatures_.add(foundCreature);
                detectedCreatures_.remove(foundCreature);
                numNeeded--;
            }

        }
        for (Creature creature : purgeCreatures_) {
            creature.remove();
        }
        purgeCreatures_.clear();

    }

}
