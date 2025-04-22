package com.clonz.blastfromthepast.entity.misc;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public enum TransitioningState implements StateValue {
    INACTIVE(0, false),
    INACTIVE_TO_ACTIVE(1, true),
    ACTIVE(2, false),
    ACTIVE_TO_INACTIVE(3, true);

    private final int id;
    private final boolean transitional;

    TransitioningState(int id, boolean transitional) {
        this.id = id;
        this.transitional = transitional;
    }

    @Override
    public int id() { return this.id; }

    public boolean isTransitional() {
        return this.transitional;
    }

    public static TransitioningState byOrdinal(int pOrdinal) {
        if (pOrdinal < 0 || pOrdinal > values().length) {
            pOrdinal = 0;
        }

        return values()[pOrdinal];
    }

    public static void transition(boolean sleeping, Supplier<TransitioningState> stateGetter, Consumer<TransitioningState> stateSetter) {
        TransitioningState transition = getTransition(stateGetter.get(), sleeping);
        if(transition != null) {
            stateSetter.accept(transition);
        }
    }

    @Nullable
    public static TransitioningState getTransition(TransitioningState previous, boolean active){
        switch (previous){
            case INACTIVE -> {
                if(active){
                    return INACTIVE_TO_ACTIVE;
                }
            }
            case INACTIVE_TO_ACTIVE -> {
                if(!active){
                    return INACTIVE;
                }
            }
            case ACTIVE -> {
                if(!active){
                    return ACTIVE_TO_INACTIVE;
                }
            }
            case ACTIVE_TO_INACTIVE -> {
                if(active){
                    return ACTIVE;
                }
            }
        }
        return null;
    }

    public static class TransitionTicker{
        private final Supplier<TransitioningState> stateGetter;
        private final Consumer<TransitioningState> stateSetter;
        private final int activateDuration;
        private final int deactivateDuration;
        private int countdown;

        public TransitionTicker(Supplier<TransitioningState> stateGetter, Consumer<TransitioningState> stateSetter, int activateDuration, int deactivateDuration){
            this.stateGetter = stateGetter;
            this.stateSetter = stateSetter;
            this.activateDuration = activateDuration;
            this.deactivateDuration = deactivateDuration;
        }

        public void triggerStateChange(){
            switch(this.stateGetter.get()){
                case INACTIVE_TO_ACTIVE -> this.countdown = this.activateDuration;
                case ACTIVE_TO_INACTIVE -> this.countdown = this.deactivateDuration;
                default -> this.countdown = 0;
            }
        }

        public void tick(boolean canUpdateState){
            if (this.countdown > 0 && --this.countdown <= 0) {
                if(canUpdateState){
                    switch (this.stateGetter.get()){
                        case INACTIVE_TO_ACTIVE -> this.stateSetter.accept(TransitioningState.ACTIVE);
                        case ACTIVE_TO_INACTIVE -> this.stateSetter.accept(TransitioningState.INACTIVE);
                    }
                }
            }
        }

    }
}