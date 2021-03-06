package com.asha.vrlib.strategy.display;

import com.asha.vrlib.SharkLibrary;
import com.asha.vrlib.common.SharkGLHandler;
import com.asha.vrlib.model.BarrelDistortionConfig;
import com.asha.vrlib.strategy.ModeManager;

/**
 * Created by hzqiujiadi on 16/3/19.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class DisplayModeManager extends ModeManager<AbsDisplayStrategy> implements IDisplayMode {

    public static int[] sModes = {SharkLibrary.DISPLAY_MODE_NORMAL, SharkLibrary.DISPLAY_MODE_GLASS};

    private boolean antiDistortionEnabled;
    private BarrelDistortionConfig barrelDistortionConfig;

    public DisplayModeManager(int mode, SharkGLHandler handler) {
        super(mode, handler);
    }

    @Override
    protected int[] getModes() {
        return sModes;
    }

    @Override
    protected AbsDisplayStrategy createStrategy(int mode) {
        switch (mode){
            case SharkLibrary.DISPLAY_MODE_GLASS:
                return new GlassStrategy();
            case SharkLibrary.DISPLAY_MODE_NORMAL:
            default:
                return new NormalStrategy();
        }
    }

    @Override
    public int getVisibleSize() {
        return getStrategy().getVisibleSize();
    }

    public void setAntiDistortionEnabled(boolean antiDistortionEnabled) {
        this.antiDistortionEnabled = antiDistortionEnabled;
    }

    public boolean isAntiDistortionEnabled() {
        return antiDistortionEnabled;
    }

    public void setBarrelDistortionConfig(BarrelDistortionConfig barrelDistortionConfig) {
        this.barrelDistortionConfig = barrelDistortionConfig;
    }

    public BarrelDistortionConfig getBarrelDistortionConfig() {
        return barrelDistortionConfig;
    }
}
