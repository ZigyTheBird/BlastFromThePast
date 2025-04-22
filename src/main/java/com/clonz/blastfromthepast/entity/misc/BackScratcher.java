package com.clonz.blastfromthepast.entity.misc;

public interface BackScratcher {

    boolean isBackScratching();

    void setBackScratching(boolean backScratching);

    boolean wantsToScratchBack();

    void scratchedBack();

    void playBackScratchSound();

    void setPreparingToScratchBack(boolean prepare);

    boolean isPreparingToScratchBack();
}
