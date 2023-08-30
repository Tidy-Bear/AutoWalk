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

    void toggleAutoWalk();

}
