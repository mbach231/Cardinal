
package com.mbach231.cardinal.environment.disease.effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * 
 */
public class PotionDiseaseEffect extends DiseaseEffect {

    private final PotionEffect effect_;
    
    public PotionDiseaseEffect(PotionEffectType type, int duration, int level) {
        super();
        effect_ = new PotionEffect(type, duration * 20, level);
    }
    
    @Override
    protected void applyEffectToLivingEntity(LivingEntity entity) {
        entity.addPotionEffect(effect_);
    }

}
