
package com.mbach231.cardinal.environment.disease;

import java.util.Map;

/**
 *
 * 
 */
public class DiseaseManager {
    
    private final Map<String, Disease> diseaseMap_;
    
    public DiseaseManager() {
        diseaseMap_ = (new DiseaseLoader()).getDiseases();
    }

}
