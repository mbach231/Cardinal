
package com.mbach231.cardinal.environment.season.climate;

import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.environment.season.Season;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 *
 */
public final class WeatherManager {

    private final Map<String, WeatherState> weatherStateMap_;
    private WeatherState currentState_;

    public WeatherManager(Season season) {
        weatherStateMap_ = new HashMap();
        loadSeason(season);

        String currentStateStr = ClimateDatabaseInterface.getWeatherState();

        // If no valid state exists, set to first in hashmap
        if (currentStateStr == null) {
            for (Map.Entry<String, WeatherState> entry : weatherStateMap_.entrySet()) {
                currentState_ = entry.getValue();
                ClimateDatabaseInterface.saveWeatherState(entry.getKey());
                break;
            }
        } else if (!weatherStateMap_.containsKey(currentStateStr)) {
            for (Map.Entry<String, WeatherState> entry : weatherStateMap_.entrySet()) {
                currentState_ = entry.getValue();
                ClimateDatabaseInterface.saveWeatherState(entry.getKey());
                break;
            }
        } else {
            currentState_ = weatherStateMap_.get(currentStateStr);
        }
    }

    public void loadSeason(Season season) {
        loadConfig(ConfigManager.loadSeasonConfig(season, "weather.yml"));
    }

    private void loadConfig(FileConfiguration config) {
        weatherStateMap_.clear();
        for (String stateName : config.getConfigurationSection("WeatherStates").getValues(false).keySet()) {
            int tempModifier = config.getInt("WeatherStates." + stateName + ".TemperatureModifier");
            double precipitateChance = config.getDouble("WeatherStates." + stateName + ".PrecipitateChance");
            WeatherStateTransition transition = new WeatherStateTransition();

            for (String transitionStateName : config.getConfigurationSection("WeatherStates." + stateName + ".TransitionProbabilities").getValues(false).keySet()) {
                int chance = config.getInt("WeatherStates." + stateName + ".TransitionProbabilities." + transitionStateName);

                transition.addStateTransition(transitionStateName, chance);
            }

            weatherStateMap_.put(stateName, new WeatherState(stateName, tempModifier, precipitateChance, transition));
        }
    }
    
    public void updateWeatherState() {
        currentState_ = weatherStateMap_.get(currentState_.getStateTransition());
        ClimateDatabaseInterface.saveWeatherState(currentState_.getName());
    }

    public int getCurrentTemperature() {
        return currentState_.getTemperatureModifier();
    }
    
    public double getPrecipitateChance() {
        return currentState_.precipitateChance();
    }
    
    public String getWeatherStateString() {
        return currentState_.getName();
    }

}
