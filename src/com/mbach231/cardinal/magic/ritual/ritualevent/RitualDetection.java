package com.mbach231.cardinal.magic.ritual.ritualevent;

import com.mbach231.cardinal.CardinalLogger;
import com.mbach231.cardinal.magic.ritual.RitualDatabaseInterface;
import com.mbach231.cardinal.magic.ritual.sacrifices.SacrificeDetection;
import com.mbach231.cardinal.magic.ritual.sacrifices.Sacrifice;
import com.mbach231.cardinal.magic.ritual.circles.CircleMaterials;
import com.mbach231.cardinal.magic.ritual.circles.RitualCircleRelativeLocation;
import com.mbach231.cardinal.magic.ritual.circles.CircleSizes;
import com.mbach231.cardinal.magic.ritual.circles.RitualCircle;
import com.mbach231.cardinal.magic.ritual.circles.RitualCircleDetection;
import com.mbach231.cardinal.magic.ritual.RitualListener;
import static com.mbach231.cardinal.magic.ritual.RitualListener.msgColor;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class RitualDetection {

    private final int DETECTION_RADIUS = CircleSizes.PILLAR_RADIUS;
    private final CircleMaterials validCircleMaterials_;
    private final RitualCircleRelativeLocation validCircleRelativeLocations_;
    private final ValidRituals validRituals_;
    private final RitualCircleDetection circleDetection_;
    private final SacrificeDetection sacrificeDetection_;
    private Ritual foundRitual_;
    
    public static Map<CircleSizes.CircleSizeEn, RitualCircle> detectedCircles;

    public RitualDetection() {
        validCircleRelativeLocations_ = new RitualCircleRelativeLocation();
        validCircleMaterials_ = new CircleMaterials();

        circleDetection_ = new RitualCircleDetection(validCircleMaterials_.getValidCircleMaterials(),
                validCircleRelativeLocations_);

        sacrificeDetection_ = new SacrificeDetection();

        validRituals_ = new ValidRituals();
    }

    public boolean detectRitual(PlayerInteractEvent event) {
        circleDetection_.detect(event.getClickedBlock());
        sacrificeDetection_.detect(event.getClickedBlock());

        /*
        foundRitualType = validRituals.getRitualType(circleDetection.getDetectedCircles(),
                sacrificeDetection.getSacrifices());
        */
        foundRitual_ = validRituals_.getRitual(circleDetection_.getDetectedCircles(),
                sacrificeDetection_.getSacrifices());
        

        // If circles and sacrifices indicate player is trying to cast a ritual,
        // check the other requirements necessary to cast the ritual.
        if (foundRitual_ != null) {
            //Ritual foundRitual = validRituals.getRitual(foundRitualType);

            // Check if area is being suppressed, if so, cancel ritual
            if (RitualDatabaseInterface.ritualMagicSuppressed(event.getClickedBlock().getLocation())) {
                event.getPlayer().sendMessage(msgColor + "Magic is being suppressed in this area!");
                return false;
            }

            if (foundRitual_.castableWithPlayerCount(getNumPlayersInCircle(event.getClickedBlock().getLocation())) == false) {
                event.getPlayer().sendMessage(RitualListener.msgColor + "You need more players present to cast this ritual!");
                return false;
            }

            // If not castable in biome
            if (foundRitual_.castableInBiome(event.getClickedBlock().getBiome()) == false) {
                event.getPlayer().sendMessage(RitualListener.msgColor + "Cannot cast this ritual in this biome!");
                return false;
            }

            // If not castable in this moon phase
            if (foundRitual_.castableInMoonPhase((int) (event.getClickedBlock().getWorld().getFullTime() / 24000) % 8) == false) {
                event.getPlayer().sendMessage(RitualListener.msgColor + "Cannot cast this ritual with this phase of the moon!");
                return false;
            }

            // If not castable at this IG time
            if (foundRitual_.castableAtTime(event.getClickedBlock().getWorld().getTime()) == false) {
                event.getPlayer().sendMessage(RitualListener.msgColor + "Cannot cast this ritual at this hour!");
                return false;
            }

            detectedCircles = circleDetection_.getDetectedCircles();
            return true;
        }
        
        return false;
    }

    public void removeSacrifices() {
        sacrificeDetection_.purgeDetectedEntities(foundRitual_.getRequiredSacrifices());
    }
    
    public Set<Sacrifice> getSacrifices() {
        return sacrificeDetection_.getSacrifices();
    }

    public void executeRitual(PlayerInteractEvent event) {

        if(foundRitual_ != null) {
            foundRitual_.execute(event, sacrificeDetection_.getSacrifices());
            
        }
        //validRituals.getRitual(foundRitualType).execute(event);
    }
    
    // Unused 
    public void refundSacrifices(Location location) {
        for (Sacrifice sacrifice : foundRitual_.getRequiredSacrifices()) {
            sacrifice.refundSacrifice(location);
        }
    }

    public String getRitualName() {
        return foundRitual_.getName();
    }

    private int getNumPlayersInCircle(Location location) {
        int numPlayers = 0;
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distance(location) <= DETECTION_RADIUS) {
                numPlayers++;
            }
        }

        return numPlayers;
    }
}
