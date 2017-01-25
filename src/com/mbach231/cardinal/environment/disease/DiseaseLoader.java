package com.mbach231.cardinal.environment.disease;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.disease.effects.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;


/**
 *
 *
 */
public class DiseaseLoader {

    private final Map<String, Disease> diseaseMap_;
    private final FileConfiguration config_;

    private enum DiseaseEffectType {

        POTION
    }

    public DiseaseLoader() {
        config_ = ConfigManager.getDiseasesConfig();
        diseaseMap_ = loadDiseases();
    }

    public Map<String, Disease> getDiseases() {
        return diseaseMap_;
    }

    private Map<String, Disease> loadDiseases() {
        Map<String, Disease> diseaseMap = new HashMap();
        for (String diseaseName : config_.getConfigurationSection("Diseases").getValues(false).keySet()) {
            Disease disease = loadDisease(diseaseName);
            if (disease != null) {
                diseaseMap.put(diseaseName, disease);
                CardinalLogger.log(CardinalLogger.LogID.Initialization, "Loaded disease: " + diseaseName);
            } else {
                CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load disease: " + diseaseName);
            }
        }
        return diseaseMap;
    }

    private Disease loadDisease(String name) {
        String path = "Diseases." + name + ".";

        long immunityTime = config_.getLong(path + "ImmunityDuration");

        List<DiseaseStage> diseaseStages = new ArrayList();

        for (int stageNum = 1; stageNum < Integer.MAX_VALUE; stageNum++) {
            if (config_.contains(path + "Stages." + stageNum)) {

                DiseaseStage stage = loadDiseaseStage(path + "Stages." + stageNum + ".");
                if (stage != null) {
                    diseaseStages.add(stage);
                } else {
                    CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load disease stage " + stageNum + " for " + name);
                }
            } else {
                break;
            }
        }

        if (diseaseStages.isEmpty()) {
            return null;
        }

        return new Disease(name, immunityTime, diseaseStages);
    }

    private DiseaseStage loadDiseaseStage(String path) {


        long duration = config_.getLong(path + "Duration");
        int periodicInterval = config_.getInt(path + "PeriodicInterval");
        double contactTransmissionRate = config_.getDouble(path + "ContactTransmissionRate");
        double airborneTransmissionRate = config_.getDouble(path + "AirborneTransmissionRate");
        int transmissionDistance = config_.getInt(path + "TransmissionDistance");
        boolean cureOnDeath = config_.getBoolean(path + "CureOnDeath");

        Set<DiseaseEffect> diseaseEffects = null;

        if (config_.contains(path + "DiseaseEffects")) {
            diseaseEffects = loadDiseaseEffects(path + "DiseaseEffects.");
        }

        return new DiseaseStage(duration,
                periodicInterval,
                contactTransmissionRate,
                airborneTransmissionRate,
                transmissionDistance,
                cureOnDeath,
                diseaseEffects);
    }

    private Set<DiseaseEffect> loadDiseaseEffects(String path) {

        Set<DiseaseEffect> diseaseEffects = new HashSet();

        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            if (config_.contains(path + i)) {
                String effectType = config_.getString(path + i + ".EffectType");

                DiseaseEffect effect;
                DiseaseEffectType type = DiseaseEffectType.valueOf(effectType);
                switch (type) {
                    case POTION:
                        effect = loadPotionDiseaseEffect(path + i + ".");
                        break;
                    default:
                        CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load disease effect type '" + type + "' at: " + path + i + ".EffectType");
                        continue;
                }

                if (config_.contains(path + "Conditions")) {
                    DiseaseConditions conditions = loadDiseaseConditions(path + "Conditions");
                    if(conditions != null) {
                        effect.setConditions(conditions);
                    }
                }
                
                diseaseEffects.add(effect);
            } else {
                break;
            }
        }

        return diseaseEffects.isEmpty() ? null : diseaseEffects;
    }

    private DiseaseConditions loadDiseaseConditions(String path) {

        DiseaseConditions conditions = new DiseaseConditions();

        for (String conditionName : config_.getConfigurationSection(path).getValues(false).keySet()) {

            switch (conditionName) {
                case "Time":
                    int startTime = config_.getInt(path + ".Time.Start");
                    int endTime = config_.getInt(path + ".Time.Start");
                    conditions.setTimeCondition(startTime, endTime);
                    break;

                case "Outdoors":
                    if (config_.getBoolean(path + ".Outdoors")) {
                        conditions.setOutdoorCondition();
                    }
                    break;

                case "MoonPhase":
                    int moonPhase = config_.getInt(path + ".MoonPhase");
                    conditions.setMoonPhaseCondition(moonPhase);
                    break;

                default:
                    break;
            }

        }

        if(!conditions.hasConditions()) {
            CardinalLogger.log(CardinalLogger.LogID.Initialization, "Failed to load conditions: " + path);
        }
        
        return conditions.hasConditions() ? conditions : null;
    }

    private PotionDiseaseEffect loadPotionDiseaseEffect(String path) {

        PotionEffectType type = PotionEffectType.getByName(config_.getString(path + "PotionType"));
        int level = config_.getInt(path + "Level") - 1;
        int duration = config_.getInt(path + "Duration");
        
        return new PotionDiseaseEffect(type, level, duration);
    }

}
