package com.mbach231.cardinal.environment.disease;

import com.mbach231.cardinal.database.DatabaseInterface;
import com.mbach231.cardinal.database.DatabaseManager;

/**
 *
 *
 */
public class DiseaseDatabaseInterface extends DatabaseInterface {

    @Override
    protected void initializeTables() {

        String sql = "CREATE TABLE IF NOT EXISTS CARDINAL_INFECTED "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " uuid VARCHAR(255) NOT NULL, "
                + " diseaseName VARCHAR(255) NOT NULL, "
                + " timeInfected SIGNED BIGINT NOT NULL, "
                + " timeStageStart SIGNED BIGINT NOT NULL, "
                + " stageNum INTEGER NOT NULL, "
                + " strain VARCHAR(255) NOT NULL, "
                + " entityLoaded TINYINT(1) NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);
        
        sql = "CREATE TABLE IF NOT EXISTS CARDINAL_INFECTED_LOG "
                + "(id INTEGER NOT NULL AUTO_INCREMENT, "
                + " uuidInfector VARCHAR(255) DEFAULT NULL, "
                + " uuidInfected VARCHAR(255) NOT NULL, "
                + " transmissionType VARCHAR(255) NOT NULL, "
                + " diseaseName VARCHAR(255) NOT NULL, "
                + " strain VARCHAR(255) NOT NULL, "
                + " timeInfected SIGNED BIGINT NOT NULL, "
                + " xLocation INTEGER NOT NULL, "
                + " yLocation INTEGER NOT NULL, "
                + " zLocation INTEGER NOT NULL, "
                + " PRIMARY KEY ( id ))";

        DatabaseManager.executeUpdate(sql);
    }
}
