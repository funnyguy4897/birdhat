package com.birdhat;

/** Snapshot of what the bird animation needs. A fresh one is made each frame because rendering is deferred. */
public class BirdRenderState {
    public final float ageInTicks;
    public final float walkSpeed;
    public final boolean crouching;

    public BirdRenderState(float ageInTicks, float walkSpeed, boolean crouching) {
        this.ageInTicks = ageInTicks;
        this.walkSpeed = walkSpeed;
        this.crouching = crouching;
    }
}
