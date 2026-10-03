package fr.luquioz.customworldboarder.models;

import org.bukkit.World;

public class Border {
    private final String name;
    private final String worldName;
    private double centerX;
    private double centerZ;
    private double size;
    private double warningDistance;
    private boolean active;
    private double damagePerTick;
    private double damageRange;
    private String warningParticleColor;
    private String visualParticleColor;
    private boolean borderParticles;
    private boolean borderSounds;
    private boolean borderVisualParticles;
    private String warningTitle;
    private String warningSubtitle;
    private boolean damageMessage;
    private String damageTitle;
    private String damageSubtitle;
    private String kickTitle;
    private String kickSubtitle;
    private boolean isReducing;
    private double initialSize;
    private double targetSize;
    private int reduceDuration;
    private int reduceElapsed;

    public Border(String name, World world, double centerX, double centerZ, double size) {
        this.name = name;
        this.worldName = world.getName();
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.size = size;
        this.warningDistance = 5.0;
        this.active = false;
        this.damagePerTick = 0.0;
        this.damageRange = 100.0;
        this.warningParticleColor = "255,85,0";
        this.visualParticleColor = "0,255,0";
        this.warningDistance = 5.0;
        this.borderParticles = true;
        this.borderSounds = true;
        this.borderVisualParticles = true;
        this.warningTitle = "&c⚠ Zone !";
        this.warningSubtitle = "&cVous sortez de la zone !";
        this.damageMessage = true;
        this.damageTitle = "&cDégâts !";
        this.damageSubtitle = "&cVous perdez des points de vie !";
        this.kickTitle = "&cRetour !";
        this.kickSubtitle = "&cVous avez été renvoyé au centre !";
        this.isReducing = false;
        this.initialSize = size;
        this.targetSize = size;
        this.reduceDuration = 0;
        this.reduceElapsed = 0;
    }

    public String getName() {
        return name;
    }

    public String getWorldName() {
        return worldName;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterZ() {
        return centerZ;
    }

    public void setCenterZ(double centerZ) {
        this.centerZ = centerZ;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getWarningDistance() {
        return warningDistance;
    }

    public void setWarningDistance(double warningDistance) {
        this.warningDistance = warningDistance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getDamagePerTick() {
        return damagePerTick;
    }

    public void setDamagePerTick(double damagePerTick) {
        this.damagePerTick = damagePerTick;
    }

    public double getDamageRange() {
        return damageRange;
    }

    public void setDamageRange(double damageRange) {
        this.damageRange = damageRange;
    }

    public String getWarningParticleColor() {
        return warningParticleColor;
    }

    public void setWarningParticleColor(String warningParticleColor) {
        this.warningParticleColor = warningParticleColor;
    }

    public String getVisualParticleColor() {
        return visualParticleColor;
    }

    public void setVisualParticleColor(String visualParticleColor) {
        this.visualParticleColor = visualParticleColor;
    }

    public boolean isBorderParticles() {
        return borderParticles;
    }

    public void setBorderParticles(boolean borderParticles) {
        this.borderParticles = borderParticles;
    }

    public boolean isBorderSounds() {
        return borderSounds;
    }

    public void setBorderSounds(boolean borderSounds) {
        this.borderSounds = borderSounds;
    }

    public boolean isBorderVisualParticles() {
        return borderVisualParticles;
    }

    public void setBorderVisualParticles(boolean borderVisualParticles) {
        this.borderVisualParticles = borderVisualParticles;
    }

    public String getWarningTitle() {
        return warningTitle;
    }

    public void setWarningTitle(String warningTitle) {
        this.warningTitle = warningTitle;
    }

    public String getWarningSubtitle() {
        return warningSubtitle;
    }

    public void setWarningSubtitle(String warningSubtitle) {
        this.warningSubtitle = warningSubtitle;
    }

    public boolean isDamageMessage() {
        return damageMessage;
    }

    public void setDamageMessage(boolean damageMessage) {
        this.damageMessage = damageMessage;
    }

    public String getDamageTitle() {
        return damageTitle;
    }

    public void setDamageTitle(String damageTitle) {
        this.damageTitle = damageTitle;
    }

    public String getDamageSubtitle() {
        return damageSubtitle;
    }

    public void setDamageSubtitle(String damageSubtitle) {
        this.damageSubtitle = damageSubtitle;
    }

    public String getKickTitle() {
        return kickTitle;
    }

    public void setKickTitle(String kickTitle) {
        this.kickTitle = kickTitle;
    }

    public String getKickSubtitle() {
        return kickSubtitle;
    }

    public void setKickSubtitle(String kickSubtitle) {
        this.kickSubtitle = kickSubtitle;
    }

    public double getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(double initialSize) {
        this.initialSize = initialSize;
    }

    public boolean isReducing() {
        return isReducing;
    }

    public void setReducing(boolean reducing) {
        isReducing = reducing;
    }

    public double getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(double targetSize) {
        this.targetSize = targetSize;
    }

    public int getReduceDuration() {
        return reduceDuration;
    }

    public void setReduceDuration(int reduceDuration) {
        this.reduceDuration = reduceDuration;
    }

    public int getReduceElapsed() {
        return reduceElapsed;
    }

    public void setReduceElapsed(int reduceElapsed) {
        this.reduceElapsed = reduceElapsed;
    }

    public boolean isOutsideBorder(double x, double z) {
        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));
        return distance > size;
    }

    public boolean isInWarningZone(double x, double z) {
        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));
        return distance > (size - warningDistance) && distance <= size;
    }

    public boolean isInsideBorder(double x, double z) {
        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));
        return distance <= size;
    }

    public boolean isInDamageZone(double x, double z) {
        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));
        return distance > size && distance <= (size + damageRange);
    }
}
