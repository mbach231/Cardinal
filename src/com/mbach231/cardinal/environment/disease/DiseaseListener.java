
package com.mbach231.cardinal.environment.disease;

import org.bukkit.event.Listener;

/**
 *
 * 
 */
public class DiseaseListener implements Listener {

    private final DiseaseManager diseaseManager_;
    
    public DiseaseListener() {
        diseaseManager_ = new DiseaseManager();
    }
    
}
