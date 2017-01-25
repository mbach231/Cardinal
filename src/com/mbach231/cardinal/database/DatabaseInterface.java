
package com.mbach231.cardinal.database;

/**
 *
 * 
 */
public abstract class DatabaseInterface {

    protected boolean initialized_ = false;
    
    public DatabaseInterface()
    {
        initializeTables();
        initialized_ = true;
    }
    
    public boolean isInitialized()
    {
        return initialized_;
    }
    
    protected abstract void initializeTables();
    
    
}
