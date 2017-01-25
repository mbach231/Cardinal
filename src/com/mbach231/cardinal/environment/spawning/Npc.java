
package com.mbach231.cardinal.environment.spawning;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.items.itemdrops.ItemDrop;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 *
 *
 */
public class Npc {

    private final String name_;
    private final EntityType type_;
    Map<EquipmentSlot, ItemStack> equipmentMap_;
    private final Set<PotionEffectChance> onSpawnPotionEffect_;
    private final Set<PotionEffectChance> onAttackPotionEffectSelf_;
    private final Set<PotionEffectChance> onAttackPotionEffectVictim_;
    private final Set<PotionEffectChance> onDamagedPotionEffectSelf_;
    private final Set<PotionEffectChance> onDamagedPotionEffectAttacker_;

    private final Set<ItemDrop> itemDrops_;

    private final int xp_;
    private final int hp_;

    public Npc(String name,
            EntityType type,
            int xp,
            int hp,
            Map<EquipmentSlot, ItemStack> equipmentMap,
            Set<PotionEffectChance> onSpawnPotionEffect,
            Set<PotionEffectChance> onAttackPotionEffectSelf,
            Set<PotionEffectChance> onAttackPotionEffectVictim,
            Set<PotionEffectChance> onDamagedPotionEffectSelf,
            Set<PotionEffectChance> onDamagedPotionEffectAttacker,
            Set<ItemDrop> itemDrops) {
        name_ = name;
        type_ = type;
        xp_ = xp;
        hp_ = hp;
        equipmentMap_ = equipmentMap;
        onSpawnPotionEffect_ = onSpawnPotionEffect;
        onAttackPotionEffectSelf_ = onAttackPotionEffectSelf;
        onAttackPotionEffectVictim_ = onAttackPotionEffectVictim;
        onDamagedPotionEffectSelf_ = onDamagedPotionEffectSelf;
        onDamagedPotionEffectAttacker_ = onDamagedPotionEffectAttacker;
        itemDrops_ = itemDrops;
    }

    public LivingEntity spawnEntity(Location location) {
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, type_);
        entity.setHealth(hp_);

        entity.setCustomName(name_);
        entity.setCustomNameVisible(false);

        if (equipmentMap_ != null) {

            for (Map.Entry<EquipmentSlot, ItemStack> entry : equipmentMap_.entrySet()) {

                switch (entry.getKey()) {
                    case HEAD:
                        entity.getEquipment().setHelmet(entry.getValue());
                        break;
                    case CHEST:
                        entity.getEquipment().setChestplate(entry.getValue());
                        break;
                    case LEGS:
                        entity.getEquipment().setLeggings(entry.getValue());
                        break;
                    case FEET:
                        entity.getEquipment().setBoots(entry.getValue());
                        break;
                    case HAND:
                        entity.getEquipment().setItemInMainHand(entry.getValue());
                        break;
                    case OFF_HAND:
                        entity.getEquipment().setItemInOffHand(entry.getValue());
                        break;
                }
            }
        }
        applyPotionEffects(entity, onSpawnPotionEffect_);

        return entity;
    }

    public void applyOnAttack(EntityDamageByEntityEvent event) {
        LivingEntity npc = (LivingEntity) event.getDamager();
        LivingEntity attacker = (LivingEntity) event.getEntity();

        applyPotionEffects(npc, onAttackPotionEffectSelf_);
        applyPotionEffects(attacker, onAttackPotionEffectVictim_);
    }

    public void applyOnDamaged(EntityDamageByEntityEvent event) {
        LivingEntity npc = (LivingEntity) event.getEntity();
        LivingEntity attacker = (LivingEntity) event.getDamager();

        applyPotionEffects(npc, onDamagedPotionEffectSelf_);
        applyPotionEffects(attacker, onDamagedPotionEffectAttacker_);
    }

    public void applyOnDeath(EntityDeathEvent event) {
        event.getDrops().clear();
        event.getEntity().getEquipment().clear();
        ItemStack randomDrop;
        if (itemDrops_ != null) {
            for (ItemDrop itemDrop : itemDrops_) {

                randomDrop = itemDrop.getRandomizedItemStack();
                if (randomDrop != null) {
                    event.getDrops().add(randomDrop);
                }
            }
        }

        event.setDroppedExp(xp_);
    }

    private void applyPotionEffects(LivingEntity entity, Set<PotionEffectChance> potionEffects) {
        if (potionEffects != null) {
            for (PotionEffectChance chanceEffect : potionEffects) {
                chanceEffect.applyEffectOnChance(entity);
            }
        }
    }
    
    public String getName() {
        return name_;
    }
}
