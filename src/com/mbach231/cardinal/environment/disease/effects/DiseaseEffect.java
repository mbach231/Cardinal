package com.mbach231.cardinal.environment.disease.effects;

import org.bukkit.entity.LivingEntity;

/**
 *
 *
 */
abstract public class DiseaseEffect {

    private DiseaseConditions conditions_;

    DiseaseEffect() {
        conditions_ = null;
    }

    public void setConditions(DiseaseConditions conditions) {
        conditions_ = conditions;
    }

    public void applyEffect(LivingEntity entity) {

        if (conditions_ != null) {
            if (!conditions_.fulfillsConditions(entity)) {
                return;
            }
        }

        applyEffectToLivingEntity(entity);
    }

    abstract protected void applyEffectToLivingEntity(LivingEntity entity);
}
