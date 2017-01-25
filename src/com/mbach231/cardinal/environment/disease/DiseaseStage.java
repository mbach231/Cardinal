package com.mbach231.cardinal.environment.disease;

import com.mbach231.cardinal.environment.disease.effects.DiseaseEffect;
import java.util.Set;
import org.bukkit.entity.LivingEntity;

/**
 *
 *
 */
public class DiseaseStage {

    /*
     * Periodic Interval            (seconds)
     * Duration                     (seconds) [0 seconds = infinite duration]
     * Conact Transmission Rate     (%)
     * Airborne Transmission Rate   (%)
     * Transmission Distance        (m)
     * Death Cures                  (boolean)
     */
    // Frequency of occurance in seconds
    private final long periodicInterval_;
    // Duration of stage in seconds (0 = infinite duration)
    private final long duration_;
    // Percent chance of successful transmission to other nearby entitys
    private final double contactTransmissionRate_;
    // Percent chance of successful transmission to other nearby entitys
    private final double airborneTransmissionRate_;
    // Max distance between 
    private final double transmissionDistance_;
    // If true, entity dying will cure disease
    private final boolean cureOnDeath_;

    private final Set<DiseaseEffect> diseaseEffects_;

    public DiseaseStage(long duration,
            int periodicInterval,
            double contactTransmissionRate,
            double airborneTransmissionRate,
            double transmissionDistance,
            boolean cureOnDeath,
            Set<DiseaseEffect> diseaseEffects) {
        this.duration_ = duration;
        this.periodicInterval_ = periodicInterval;
        this.contactTransmissionRate_ = contactTransmissionRate;
        this.airborneTransmissionRate_ = airborneTransmissionRate;
        this.transmissionDistance_ = transmissionDistance;
        this.cureOnDeath_ = cureOnDeath;
        diseaseEffects_ = diseaseEffects;
    }

    public long getPeriodicInterval() {
        return periodicInterval_;
    }

    public long getDuration() {
        return duration_;
    }

    public double getAirborneTransmissionRate() {
        return airborneTransmissionRate_;
    }

    public double getContactTransmissionRate() {
        return contactTransmissionRate_;
    }

    public double getTransmissionDistance() {
        return transmissionDistance_;
    }

    public boolean getCureOnDeath() {
        return cureOnDeath_;
    }

    public void applyStageEffects(LivingEntity entity) {
        for (DiseaseEffect diseaseEffect : diseaseEffects_) {
            diseaseEffect.applyEffect(entity);
        }
    }

}
