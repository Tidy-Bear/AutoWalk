package me.ctidy.mcmod.autowalk.api;

/**
 * AutoWalkable
 *
 * @author ctidy
 * @since 2023/8/7
 */
public interface IAutoWalkable {

    boolean isAutoWalkEnabled();

    void startAutoWalk();

    void stopAutoWalk();

    /**
     * Toggle auto walk between start and stop.
     * @see IAutoWalkable#startAutoWalk
     * @see IAutoWalkable#stopAutoWalk
     */
    default void toggleAutoWalk() {
        if (isAutoWalkEnabled()) {
            stopAutoWalk();
        } else {
            startAutoWalk();
        }
    }

    /**
     * Keep player forward when auto walking.
     */
    void autoForward();

    /**
     * From {@link net.minecraftforge.common.extensions.IForgeEntity#getStepHeight()}
     */
    float getStepHeight();

}
