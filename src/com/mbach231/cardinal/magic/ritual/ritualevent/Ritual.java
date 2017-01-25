package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import com.mbach231.cardinal.magic.ritual.circles.RitualCircle;
import java.util.HashMap;
import com.mbach231.cardinal.magic.ritual.circles.CircleSizes.*;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.event.player.PlayerInteractEvent;

public class Ritual {

    private final String name_;
    private final Map<CircleSizeEn, RitualCircle> requiredCircles_;
    private final Set<Sacrifice> requiredSacrifices_;
    private final RitualEvent ritualEvent_;
    private final Set<Biome> requiredBiomes_;
    private final Set<TimeRangeEntry> requiredTime_;
    private final int requiredMoonPhase_;
    private final int requiredNumPlayers_;

    public Ritual(String name,
            RitualCircle smallRitualCircle,
            RitualCircle mediumRitualCircle,
            RitualCircle largeRitualCircle,
            Set<Sacrifice> requiredSacrifices,
            Set<Biome> requiredBiomes,
            Set<TimeRangeEntry> requiredTime,
            int requiredMoonPhase,
            int requiredNumPlayers,
            RitualEvent ritualEvent) {

        this.name_ = name;
        requiredCircles_ = new HashMap();
        requiredCircles_.put(CircleSizeEn.SMALL, smallRitualCircle);
        requiredCircles_.put(CircleSizeEn.MEDIUM, mediumRitualCircle);
        requiredCircles_.put(CircleSizeEn.LARGE, largeRitualCircle);
        this.requiredSacrifices_ = requiredSacrifices;
        this.requiredBiomes_ = requiredBiomes;
        this.requiredTime_ = requiredTime;
        this.requiredMoonPhase_ = requiredMoonPhase;
        this.requiredNumPlayers_ = requiredNumPlayers;
        this.ritualEvent_ = ritualEvent;
    }

    public Ritual(String name,
            Map<CircleSizeEn, RitualCircle> requiredCircles,
            Set<Sacrifice> requiredSacrifices,
            Set<Biome> requiredBiomes,
            Set<TimeRangeEntry> requiredTime,
            int requiredMoonPhase,
            int requiredNumPlayers,
            RitualEvent ritualEvent) {

        this.name_ = name;
        this.requiredCircles_ = requiredCircles;
        this.requiredSacrifices_ = requiredSacrifices;
        this.requiredBiomes_ = requiredBiomes;
        this.requiredTime_ = requiredTime;
        this.requiredMoonPhase_ = requiredMoonPhase;
        this.requiredNumPlayers_ = requiredNumPlayers;
        this.ritualEvent_ = ritualEvent;
    }

    public String getName() {
        return name_;
    }

    public boolean usesCircles(Map<CircleSizeEn, RitualCircle> circles) {

        for (Map.Entry<CircleSizeEn, RitualCircle> entry : circles.entrySet()) {
            // If this circle isn't set, ignore what is set there (if anything)
            if (requiredCircles_.get(entry.getKey()) == null) {
                continue;
            }
            if (!requiredCircles_.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public boolean usesSacrifices(Set<Sacrifice> sacrifices) {
        if (name_.equals("Test Ritual")) {
            for (Sacrifice sacrifice : requiredSacrifices_) {
            }
        }
        
        if (sacrifices.isEmpty()) {
            return false;
        }

        if (sacrifices.size() != requiredSacrifices_.size()) {
            return false;
        }

        for (Sacrifice foundSacrifice : sacrifices) {
            if (!requiredSacrifices_.contains(foundSacrifice)) {
                return false;
            }
        }
        return true;
    }

    public boolean castableInBiome(Biome biome) {

        // If empty, all biomes valid
        if (requiredBiomes_.isEmpty()) {
            return true;
        }

        return requiredBiomes_.contains(biome);
    }

    public boolean castableInMoonPhase(int phase) {

        // If -1, all moon phases
        if (requiredMoonPhase_ == MoonPhase.NO_PHASE) {
            return true;
        }

        return requiredMoonPhase_ == phase;

    }

    public boolean castableAtTime(long time) {

        // If empty, all times valid
        if (requiredTime_.isEmpty()) {
            return true;
        }

        for (TimeRangeEntry entry : requiredTime_) {

            if (entry.getStartTime() < entry.getEndTime()) {

                if (time > entry.getStartTime() && time < entry.getEndTime()) {
                    return true;
                }
            } else {
                if (time > entry.getStartTime() || time < entry.getEndTime()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean castableWithPlayerCount(int numPlayersPresent) {
        return numPlayersPresent >= requiredNumPlayers_;
    }

    public RitualEvent getRitualEvent() {
        return ritualEvent_;
    }

    public void execute(PlayerInteractEvent event, Set<Sacrifice> sacrifices) {
        ritualEvent_.executeEvent(event, sacrifices);
    }

    public Set<Sacrifice> getRequiredSacrifices() {
        return requiredSacrifices_;
    }

    public String getRitualString() {
        String string = ChatColor.BLACK + "";

        if (requiredCircles_.containsKey(CircleSizeEn.SMALL)) {
            string += "S: " + adjStr(requiredCircles_.get(CircleSizeEn.SMALL).getCircleMaterial().toString()) + "\n";
        }
        if (requiredCircles_.containsKey(CircleSizeEn.MEDIUM)) {
            string += "M: " + adjStr(requiredCircles_.get(CircleSizeEn.MEDIUM).getCircleMaterial().toString()) + "\n";
        }
        if (requiredCircles_.containsKey(CircleSizeEn.LARGE)) {
            string += "L: " + adjStr(requiredCircles_.get(CircleSizeEn.LARGE).getCircleMaterial().toString()) + "\n";
        }

        string += "Sacrifices:\n";
        for (Sacrifice sacrifice : requiredSacrifices_) {
            if (sacrifice.isItem()) {
                string += "-" + adjStr(sacrifice.getMaterial().toString()) + " x" + sacrifice.getAmount() + "\n";
            } else if (sacrifice.isCreature()) {
                string += "-" + adjStr(sacrifice.getCreatureType().toString()) + " x" + sacrifice.getAmount() + "\n";
            }
        }

        if (requiredNumPlayers_ > 1) {
            string += "# Players Needed: " + requiredNumPlayers_ + "\n";
        }

        if (!requiredBiomes_.isEmpty()) {
            //string += "Required Biomes:\n";
            for (Biome biome : requiredBiomes_) {
                string += "Biome: " + adjStr(biome.toString()) + "\n";
            }
        }

        if (!requiredTime_.isEmpty()) {
            //string += "Required Times:\n";
            for (TimeRangeEntry timeEntry : requiredTime_) {
                string += "Time: " + timeEntry.getStartTime() + " - " + timeEntry.getEndTime() + "\n";
            }
        }

        if (requiredMoonPhase_ != MoonPhase.NO_PHASE) {
            string += "Phase: " + adjStr(MoonPhase.getMoonPhaseEn(requiredMoonPhase_).toString()) + "\n";
        }

        return string;
    }

    private String adjStr(String str) {
        return WordUtils.capitalizeFully(str.replace("_", " "));
    }
}
