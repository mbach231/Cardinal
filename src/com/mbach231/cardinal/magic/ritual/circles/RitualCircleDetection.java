package com.mbach231.cardinal.magic.ritual.circles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RitualCircleDetection {

    private Set<Material> allowedMaterials_;
    private Block selectedBlock_;
    private Map<CircleSizes.CircleSizeEn, RitualCircle> detectedCircles_;
    private Location blockLocation_;
    private World currentWorld_;
    private boolean hasSmallCircle_;
    private boolean hasMediumCircle_;
    private boolean hasLargeCircle_;
    private boolean hasPillars_;
    private RitualCircleRelativeLocation relativeLocations_;
    public String detectionString_;

    public RitualCircleDetection(Set<Material> allowedMaterials,
            RitualCircleRelativeLocation relativeLocations) {

        this.allowedMaterials_ = allowedMaterials;
        this.relativeLocations_ = relativeLocations;

        detectedCircles_ = new HashMap();

        initializeDetections();

    }

    private void initializeDetections() {

        detectionString_ = "";

        hasSmallCircle_ = false;
        hasMediumCircle_ = false;
        hasLargeCircle_ = false;
        hasPillars_ = false;

        detectedCircles_.put(CircleSizes.CircleSizeEn.SMALL, null);
        detectedCircles_.put(CircleSizes.CircleSizeEn.MEDIUM, null);
        detectedCircles_.put(CircleSizes.CircleSizeEn.LARGE, null);
    }

    private Material getMaterial(RelativeLocation relativeLocation) {
        return currentWorld_.getBlockAt(blockLocation_.getBlockX() + relativeLocation.getX(),
                blockLocation_.getBlockY() + relativeLocation.getY(),
                blockLocation_.getBlockZ() + relativeLocation.getZ()).getType();
    }

    // Check if material is a valid circle material
    private boolean isValidMaterial(Material material) {
        if (allowedMaterials_.contains(material)) {
            return true;
        }
        return false;
    }

    // Check locations for matching block material
    private boolean isValidStructure(Material circleMaterial, List<RelativeLocation> list) {

        RelativeLocation location;
        for (Iterator it = list.iterator(); it.hasNext();) {
            location = (RelativeLocation) it.next();

            //detectionString += location.toString();


            if (getMaterial(location) != circleMaterial) {

                detectionString_ += "Location: " + location.getX() + ","
                        + location.getY() + "," + location.getZ() + " -- Invalid!\n";
                return false;
            }
            detectionString_ += "Location: " + location.getX() + ","
                    + location.getY() + "," + location.getZ() + " -- Valid!\n";
        }
        return true;
    }

    private void detectSmallCircle() {

        List<RelativeLocation> smallCircleRelativeLocations = relativeLocations_.getSmallCircleLocations();

        //: Initialize comparison material with first material
        Material circleMaterial = getMaterial(smallCircleRelativeLocations.get(0));

        //: If material is invalid, leave circle invalid
        if (!isValidMaterial(circleMaterial)) {
            detectionString_ += "Small Circle: Invalid material\n";
            return;
        }

        if (!isValidStructure(circleMaterial, smallCircleRelativeLocations)) {
            detectionString_ += "Small Circle: Invalid structure\n";
            return;
        }

        RitualCircle detectedCircle = new RitualCircle(CircleSizes.CircleSizeEn.SMALL,
                circleMaterial);

        detectedCircles_.put(CircleSizes.CircleSizeEn.SMALL, detectedCircle);
        hasSmallCircle_ = true;
    }

    private void detectMediumCircle() {

        List<RelativeLocation> mediumCircleRelativeLocations = relativeLocations_.getMediumCircleLocations();

        //: Initialize comparison material with first material
        Material circleMaterial = getMaterial(mediumCircleRelativeLocations.get(0));

        //: If material is invalid, leave circle invalid
        if (!isValidMaterial(circleMaterial)) {
            detectionString_ += "Medium Circle: Invalid material\n";
            return;
        }

        if (!isValidStructure(circleMaterial, mediumCircleRelativeLocations)) {
            detectionString_ += "Medium Circle: Invalid structure\n";
            return;
        }

        RitualCircle detectedCircle = new RitualCircle(CircleSizes.CircleSizeEn.MEDIUM,
                circleMaterial);

        detectedCircles_.put(CircleSizes.CircleSizeEn.MEDIUM, detectedCircle);
        hasMediumCircle_ = true;
    }

    private void detectLargeCircle() {

        List<RelativeLocation> largeCircleRelativeLocations = relativeLocations_.getLargeCircleLocations();

        //: Initialize comparison material with first material
        Material circleMaterial = getMaterial(largeCircleRelativeLocations.get(0));

        //: If material is invalid, leave circle invalid
        if (!isValidMaterial(circleMaterial)) {
            detectionString_ += "Large Circle: Invalid material\n";
            return;
        }

        if (!isValidStructure(circleMaterial, largeCircleRelativeLocations)) {
            detectionString_ += "Large Circle: Invalid structure\n";
            return;
        }

        RitualCircle detectedCircle = new RitualCircle(CircleSizes.CircleSizeEn.LARGE,
                circleMaterial);

        detectedCircles_.put(CircleSizes.CircleSizeEn.LARGE, detectedCircle);
        hasLargeCircle_ = true;
    }

    private void detectPillars() {

        List<RelativeLocation> pillarRelativeLocations = relativeLocations_.getPillarLocations();

        //: Initialize comparison material with redstone block
        Material circleMaterial = Material.REDSTONE_BLOCK;

        if (isValidStructure(circleMaterial, pillarRelativeLocations)) {
            hasPillars_ = true;
        }
    }

    public void detect(Block block) {

        initializeDetections();

        selectedBlock_ = block;
        blockLocation_ = selectedBlock_.getLocation();
        currentWorld_ = selectedBlock_.getWorld();

        detectPillars();
        detectSmallCircle();
        detectMediumCircle();
        detectLargeCircle();

    }

    public boolean hasSmallCircle() {
        return hasSmallCircle_;
    }

    public boolean hasMediumCircle() {
        return hasMediumCircle_;
    }

    public boolean hasLargeCircle() {
        return hasLargeCircle_;
    }

    public boolean hasPillars() {
        return hasPillars_;
    }

    public boolean hasValidCircle() {

        if ((hasPillars_) && (hasSmallCircle_ || hasMediumCircle_ || hasLargeCircle_)) {
            return true;
        }
        return false;
    }

    public Map<CircleSizes.CircleSizeEn, RitualCircle> getDetectedCircles() {
        return detectedCircles_;
    }
}
