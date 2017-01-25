
package com.mbach231.cardinal.environment.disease;

import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * 
 */
public class InfectionEvent extends Event implements Cancellable {

    public enum TransmissionTypeEn {
        CONTACT,
        AIRBORNE,
        VECTOR_CONTACT,
        VECTOR_AIRBORNE,
        CONSUMPTION,
        MAGIC
    }
    
    private boolean cancelled_;
    
    private LivingEntity infector_;
    private LivingEntity infected_;
    private Disease disease_;
    private TransmissionTypeEn transmissionType_;
    
    public InfectionEvent() {
        
        cancelled_ = false;
    }
    
    @Override
    public HandlerList getHandlers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCancelled() {
        return cancelled_;
    }

    @Override
    public void setCancelled(boolean bln) {
        cancelled_ = bln;
    }

}
