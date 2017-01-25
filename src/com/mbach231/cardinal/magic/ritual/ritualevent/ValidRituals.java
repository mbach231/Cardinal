package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.CardinalLogger.LogID;
import com.mbach231.cardinal.ConfigManager;
import com.mbach231.cardinal.items.CustomItemListener;

import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import com.mbach231.cardinal.magic.ritual.circles.CircleSizes;
import com.mbach231.cardinal.magic.ritual.circles.RitualCircle;
import com.mbach231.cardinal.magic.ritual.ritualevent.MoonPhase.MoonPhaseEn;
import com.mbach231.cardinal.magic.ritual.RitualListener;
import com.mbach231.cardinal.magic.ritual.structures.Structure;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.inventory.ItemStack;

public class ValidRituals {

    RitualFactory ritualFactory;
    // Used to retrieve a ritual to perform event
    String validString = "";

    static Map<String, Ritual> ritualMap;

    FileConfiguration config_;

    public ValidRituals() {

        validString += "Created new valid string!\n";
        ritualFactory = new RitualFactory();

        this.config_ = ConfigManager.getRitualConfig();
        ritualMap = new HashMap();

        Set<Map.Entry<String, Object>> ritualList = config_.getConfigurationSection("rituals").getValues(false).entrySet();

        if (ritualList != null) {
            String ritualName;
            Ritual ritual;
            for (Map.Entry<String, Object> entry : ritualList) {
                ritualName = entry.getKey();
                ritual = loadRitual(config_, "rituals", ritualName);

                if (ritual != null) {
                    ritualMap.put(ritualName, ritual);
                }
            }
        } else {
            CardinalLogger.log(LogID.Initialization, "Ritual list is null!");
        }
    }

    private Ritual loadRitual(FileConfiguration config, String path, String name) {
        ritualFactory.resetFactory();
        ritualFactory.setName(name);

        RitualEvent ritualEvent;
        String valueStr;
        Material material;
        EntityType entityType;
        int num;
        String newPath = path + "." + name + ".";

        try {

            if (config.contains(newPath + "circles.small")
                    || config.contains(newPath + "circles.medium")
                    || config.contains(newPath + "circles.large")) {

                if (config.contains(newPath + "circles.small")) {
                    valueStr = config.getString(newPath + "circles.small");
                    if ((material = Material.valueOf(valueStr)) != null) {
                        ritualFactory.addCircle(CircleSizes.CircleSizeEn.SMALL, material);
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", invalid small circle material: " + valueStr);
                        return null;
                    }
                }
                if (config.contains(newPath + "circles.medium")) {
                    valueStr = config.getString(newPath + "circles.medium");
                    if ((material = Material.valueOf(valueStr)) != null) {
                        ritualFactory.addCircle(CircleSizes.CircleSizeEn.MEDIUM, material);
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", invalid medium circle material: " + valueStr);
                        return null;
                    }
                }
                if (config.contains(newPath + "circles.large")) {
                    valueStr = config.getString(newPath + "circles.large");
                    if ((material = Material.valueOf(valueStr)) != null) {
                        ritualFactory.addCircle(CircleSizes.CircleSizeEn.LARGE, material);
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", invalid large circle material: " + valueStr);
                        return null;
                    }
                }
            } else {
                CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", does not contain circles.");
                return null;
            }
        } catch (Exception e) {
        }

        if (config.contains(newPath + "sacrifices")) {

            //List<String> sacrificeList = (List<String>) config.getList(newPath + "sacrifices");
            Set<Map.Entry<String, Object>> sacrificeList = config.getConfigurationSection(newPath + "sacrifices").getValues(false).entrySet();

            String sacrificeStr;
            for (Map.Entry<String, Object> sacrificeEntry : sacrificeList) {
                sacrificeStr = sacrificeEntry.getKey();

                ItemStack customItem = CustomItemListener.getItem(sacrificeStr);
                if (customItem != null) {
                    num = config.getInt(newPath + "sacrifices." + sacrificeStr);
                    customItem.setAmount(num);
                    ritualFactory.addSacrifice(customItem, num);
                    material = null;
                } else {

                    try {
                        material = Material.valueOf(sacrificeStr);
                        num = config.getInt(newPath + "sacrifices." + sacrificeStr);
                        ritualFactory.addSacrifice(material, num);
                    } catch (Exception e) {
                        material = null;
                    }
                    
                    if (material == null) {
                        try {
                            entityType = EntityType.valueOf(sacrificeStr);
                            num = config.getInt(newPath + "sacrifices." + sacrificeStr);
                            ritualFactory.addSacrifice(entityType, num);
                        } catch (Exception e) {
                            CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", invalid sacrifice: " + sacrificeStr);
                            return null;
                        }
                    }
                }

            }

        } else {
            CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", does not contain sacrifices.");
            return null;
        }

        try {

            if (config.contains(newPath + "event")) {

                String[] eventArgs = config.getString(newPath + "event").replace(" ", "").split(",");
                ritualEvent = loadRitualEvent(eventArgs);

                if (ritualEvent != null) {
                    ritualFactory.addEvent(ritualEvent);
                } else {
                    CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", event could not be instantiated.");
                }

            } else {
                CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", does not contain event.");
                return null;
            }

            if (config.contains(newPath + "time")) {
                String[] timeArgs = config.getString(newPath + "time").replace(" ", "").split(",");
                if (timeArgs.length == 2) {
                    Integer startTime = Integer.parseInt(timeArgs[0]);
                    Integer endTime = Integer.parseInt(timeArgs[1]);
                    ritualFactory.addTimeRange(startTime, endTime);
                } else {
                    CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", invalid time range.");
                    return null;
                }
            }

            if (config.contains(newPath + "biomes")) {

                List<String> biomeList = config.getStringList(newPath + "biomes");

                Biome biome;
                for (String biomeStr : biomeList) {

                    if ((biome = Biome.valueOf(biomeStr)) != null) {
                        ritualFactory.addRequiredBiome(biome);
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", invalid biome.");
                        return null;
                    }
                }
            }

            if (config.contains(newPath + "num-players")) {
                int numPlayers = config.getInt(newPath + "players");
                ritualFactory.addNumPlayersRequirement(numPlayers);
            }

            if (config.contains(newPath + "moon-phases")) {
                List<String> moonPhaseList = config.getStringList(newPath + "moon-phases");

                MoonPhaseEn moonPhase;
                for (String moonPhaseStr : moonPhaseList) {

                    if ((moonPhase = MoonPhaseEn.valueOf(moonPhaseStr)) != null) {
                        ritualFactory.addRequiredMoonPhase(MoonPhase.getMoonPhase(moonPhase));
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ", invalid moon phase.");
                        return null;
                    }
                }
            }

            return ritualFactory.constructRitual();

        } catch (Exception e) {
            CardinalLogger.log(LogID.Initialization, "Failed to load ritual " + name + ": " + e.getMessage());
        }

        return null;

    }

    private RitualEvent loadRitualEvent(String[] args) {

        if (args.length > 0) {
            String eventName = args[0];

            try {

                if (eventName.equalsIgnoreCase("Enchant")) {
                    if (args.length == 4) {
                        Material material = Material.valueOf(args[1]);
                        Enchantment enchantment = Enchantment.getByName(args[2]);
                        Integer level = Integer.parseInt(args[3]);
                        if (material != null && enchantment != null) {
                            return new Enchant(material, enchantment, level);
                        } else {
                            CardinalLogger.log(LogID.Initialization, "Failed to load ritual event Enchant, invalid args.");
                        }
                    }
                } else if (eventName.equalsIgnoreCase("DetectMagic")) {
                    if (args.length == 3) {
                        Long minutes = Long.parseLong(args[1]);
                        Integer distance = Integer.parseInt(args[2]);
                        return new DetectMagic(minutes, distance);
                    }
                } else if (eventName.equalsIgnoreCase("SuppressArea")) {
                    if (args.length == 3) {
                        Long minutes = Long.parseLong(args[1]);
                        Integer distance = Integer.parseInt(args[2]);
                        return new SuppressArea(minutes, distance);
                    }
                } else if (eventName.equalsIgnoreCase("Shout")) {
                    if (args.length == 3) {
                        Integer numPages = Integer.parseInt(args[1]);
                        Integer distance = Integer.parseInt(args[2]);
                        return new Shout(numPages, distance);
                    }
                } else if (eventName.equalsIgnoreCase("SpawnEntity")) {
                    if (args.length == 2 || args.length == 3) {
                        EntityType entityType = EntityType.valueOf(args[1]);

                        if (entityType != null) {
                            if (args.length == 2) {
                                return new SpawnEntity(entityType);
                            } else {
                                Variant variant = Variant.valueOf(args[2]);
                                if (variant != null) {
                                    return new SpawnEntity(entityType, variant);
                                } else {
                                    CardinalLogger.log(LogID.Initialization, "Failed to load ritual event SpawnEntity, invalid variant.");
                                }
                            }
                        } else {
                            CardinalLogger.log(LogID.Initialization, "Failed to load ritual event SpawnEntity, invalid entity.");
                        }
                    }
                } else if (eventName.equalsIgnoreCase("SpawnItem")) {
                    if (args.length == 3 || args.length == 4) {
                        Material material = Material.valueOf(args[1]);
                        Integer num = Integer.parseInt(args[2]);
                        if (material != null) {
                            if (args.length == 3) {
                                return new SpawnItem(new ItemStack(material, num));
                            } else {
                                EntityType type = EntityType.valueOf(args[3]);
                                if (type != null) {
                                    return new SpawnItem(new ItemStack(material, num, type.getTypeId()));
                                } else {
                                    CardinalLogger.log(LogID.Initialization, "Failed to load ritual event SpawnItem, invalid entity type.");
                                }
                            }
                        } else {
                            CardinalLogger.log(LogID.Initialization, "Failed to load ritual event SpawnItem, invalid args.");
                        }
                    }
                } else if (eventName.equalsIgnoreCase("RandomTeleport")) {
                    if (args.length == 2) {
                        Integer distance = Integer.parseInt(args[1]);
                        return new RandomTeleport(distance);
                    }
                } else if (eventName.equalsIgnoreCase("SpiritTunnelOpen")) {
                    if (args.length == 2) {
                        Long duration = Long.parseLong(args[1]);
                        return new SpiritTunnelOpen(duration);
                    }
                } else if (eventName.equalsIgnoreCase("SpiritTunnelTravel")) {
                    if (args.length == 1) {
                        return new SpiritTunnelTravel();
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual event SpiritTunnelTravel, invalid args.");
                    }
                } else if (eventName.equalsIgnoreCase("TeleportBed")) {
                    if (args.length == 1) {
                        return new TeleportBed();
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual event TeleportBed, invalid args.");
                    }
                } else if (eventName.equalsIgnoreCase("SetTime")) {
                    if (args.length == 2) {
                        Integer time = Integer.parseInt(args[1]);
                        return new SetTime(time);
                    }
                } else if (eventName.equalsIgnoreCase("FreezeTime")) {
                    if (args.length == 1) {
                        return new FreezeTime();
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual event FreezeTime, invalid args.");
                    }
                } else if (eventName.equalsIgnoreCase("ResumeTime")) {
                    if (args.length == 1) {
                        return new ResumeTime();
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual event ResumeTime, invalid args.");
                    }
                } else if (eventName.equalsIgnoreCase("ClearWeather")) {
                    if (args.length == 1) {
                        return new ClearWeather();
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual event ClearWeather, invalid args.");
                    }
                } else if (eventName.equalsIgnoreCase("StormyWeather")) {
                    if (args.length == 1) {
                        return new StormyWeather();
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load ritual event StormyWeather, invalid args.");
                    }
                } else if (eventName.equalsIgnoreCase("CreateStructure")) {
                    if (args.length == 3) {

                        Structure structure = loadStructure(args[1]);
                        boolean buildSafely = Boolean.parseBoolean(args[2]);

                        if (structure != null) {
                            return new CreateStructure(structure, buildSafely);
                        }

                    }
                }

            } catch (Exception e) {
                CardinalLogger.log(LogID.Initialization, "Failed to load ritual event " + eventName + ": " + e.getMessage());
            }
        } else {
            CardinalLogger.log(LogID.Initialization, "Failed to load ritual event, no args.");
        }

        return null;
    }

    private Structure loadStructure(String structName) {

        String path = "structures." + structName;
        Structure struct = new Structure();
        Material material;
        int x;
        int y;
        int z;
        try {
            if (config_.contains(path)) {

                List<String> blockList = config_.getStringList(path);

                for (String blockStr : blockList) {
                    String[] blockArgs = blockStr.replace(" ", "").split(",");

                    if (blockArgs.length == 4) {
                        material = Material.valueOf(blockArgs[0]);
                        x = Integer.parseInt(blockArgs[1]);
                        y = Integer.parseInt(blockArgs[2]);
                        z = Integer.parseInt(blockArgs[3]);
                        struct.addStructureBlock(material, x, y, z);
                    } else {
                        CardinalLogger.log(LogID.Initialization, "Failed to load structure " + structName + ", invalid arg length.");
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            CardinalLogger.log(LogID.Initialization, "Failed to load structure " + structName + ": " + e.getMessage());
        }

        return struct;
    }

    public Ritual getRitual(String ritualName) {
        return ritualMap.get(ritualName);
    }

    public static Collection<Ritual> getRituals() {
        return ritualMap.values();
    }

    // Check if found circles and sacrifices correspond to a ritual
    public Ritual getRitual(Map<CircleSizes.CircleSizeEn, RitualCircle> foundCircles,
            Set<Sacrifice> foundSacrifices) {

        // Iterate through rituals
        for (Map.Entry<String, Ritual> entry : ritualMap.entrySet()) {

            Ritual ritual = entry.getValue();

            // If a ritual exists that uses circles and sacrifices, return ritual type
            if (ritual.usesCircles(foundCircles) && ritual.usesSacrifices(foundSacrifices)) {
                return ritual;
            }
        }
        // No ritual exists that uses these circles and sacrifices together
        return null;
    }

}
