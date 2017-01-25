
package com.mbach231.cardinal.environment.disease;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class Disease {
    
    private final String name_;
    private final long immunityTime_;
    
    private final List<DiseaseStage> diseaseStageList_;

    protected Disease(String name, long immunityTime, List<DiseaseStage> stages) {
        name_ = name;
        immunityTime_ = immunityTime;
        diseaseStageList_ = stages;
    }
    
    public String getName() {
        return name_;
    }
    
    public long getImmunityTime() {
        return immunityTime_;
    }
    
    public DiseaseStage getDiseaseStage(int stageNum) {
        return diseaseStageList_.get(stageNum);
    }

}
