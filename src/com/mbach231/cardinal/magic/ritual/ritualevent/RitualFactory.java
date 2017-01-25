
package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import com.mbach231.cardinal.magic.ritual.circles.CircleSizes;
import com.mbach231.cardinal.magic.ritual.circles.RitualCircle;
import com.mbach231.cardinal.magic.ritual.ritualevent.RitualEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;


public final class RitualFactory {

    String factoryString = "";
    
    String name;
    Map<CircleSizes.CircleSizeEn,RitualCircle> circles;
    Set<Sacrifice> sacrifices;
    Set<Biome> biomes;
    Set<TimeRangeEntry> timeRanges;
    RitualEvent event;
    int moonPhase;
    int numPlayers;
    
    public RitualFactory() {
        resetFactory();
    }
    
    public void resetFactory() {
        
        factoryString = "";
        circles = new HashMap();
        sacrifices = new HashSet();
        biomes = new HashSet();
        timeRanges = new HashSet();
        moonPhase = MoonPhase.NO_PHASE;
        numPlayers = 1;
        event = null;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void addCircle(RitualCircle circle) {
        circles.put(circle.getCircleSize(), circle);
    }
    
    public void addCircle(CircleSizes.CircleSizeEn circleSize, Material circleMaterial) {
        circles.put(circleSize, new RitualCircle(circleSize, circleMaterial));
        
        factoryString += "Adding circle: " + circleSize.toString() + ", " + circleMaterial.toString() + "\n";
        
    }
    
    public void addSacrifice(Material material, int amount) {
        sacrifices.add(new Sacrifice(material,amount));
        
        factoryString += "Adding sacrifice: " + material.toString() + ", " + amount + "\n";
    }
    
    public void addSacrifice(ItemStack customItem, int amount) {
        sacrifices.add(new Sacrifice(customItem,amount));
        
        factoryString += "Adding sacrifice: " + customItem.toString() + ", " + amount + "\n";
    }
    
        public void addSacrifice(EntityType type, int amount) {
        sacrifices.add(new Sacrifice(type,amount));
        
        factoryString += "Adding sacrifice: " + type.toString() + ", " + amount + "\n";
    }
    
    public void addRequiredBiome(Biome biome) {
        biomes.add(biome);
    }
    
    public void addTimeRange(long startTime, long endTime) {
        timeRanges.add(new TimeRangeEntry(startTime, endTime));
    }
    
    public void addRequiredMoonPhase(int phase) {
        moonPhase = phase;
    }
    
    public void addEvent(RitualEvent ritualEvent) {
        this.event = ritualEvent;
    }
    
    public void addNumPlayersRequirement(int numPlayers) {
        this.numPlayers = numPlayers;
    }
    
    public Ritual constructRitual() {
        
        Ritual retRitual = new Ritual(name, circles, sacrifices, biomes, timeRanges, moonPhase, numPlayers, event);
        resetFactory();
        return retRitual;
    }
    
    
    
}
