

package com.mbach231.cardinal.environment.spawning;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

/**
 *
 * 
 */
public class PotionEffectChance {
    
    private final PotionEffect effect_;
    private final double chance_;
    
    public PotionEffectChance(PotionEffect effect, double chance) {
        effect_ = effect;
        chance_ = chance;
    }
    
    public void applyEffectOnChance(LivingEntity entity) {
        if(Math.random() < chance_) {
            entity.addPotionEffect(effect_);
        }
    }

}
