/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace;

import kvmath.graphics.*;
import java.util.Random;

/**
 *
 * @author callum
 */
public class RayCamera extends Camera {

    private static final float VIEWPORT_HEIGHT = 2.f;
    private static final Random random = new Random();

    private Vec3G origin = new Vec3G();
    private Vec3G rot = new Vec3G();
    private float focalLength = 1.f;
    private int samples = 1;
    private int width;
    private int height;
    private int bounces = 1;

    private Vec3G lowerLeftCorner = new Vec3G(), hv = new Vec3G();
    private float aspect = 1.f;
    private Mat4G rotMat;

    public RayCamera(int width, int height, int samples, int bounces) {
        this.width = width;
        this.height = height;
        this.samples = samples;
        this.bounces = bounces;
        this.preCalc();
    }

    public long getExpectedRayCount() {
        return (long) this.getWidth() * (long) this.getHeight() * (long) this.getSamples();
    }

    @Override
    public Ray rayAt(float x, float y) {
        if (this.samples != 1) {
            x += RayCamera.random.nextFloat();
            y += RayCamera.random.nextFloat();
        }
        Vec3G uv = new Vec3G(x / (this.width - 1), y / (this.height - 1), 0);
        Vec3G rayDir = this.lowerLeftCorner.add(
                this.hv.mul(uv))
                .sub(this.origin);
        return new Ray(this.origin, rayDir
                .xyz1().mulMat4(this.rotMat).xyz());
    }

    public final void preCalc() {
        this.rotMat = Mat4G.rotation(this.rot);
        if (this.width > this.height) {
            this.aspect = this.width / (float) this.height;
        } else {
            this.aspect = this.height / (float) this.width;
        }
        this.hv = new Vec3G(VIEWPORT_HEIGHT * this.aspect, VIEWPORT_HEIGHT, 0);
        this.lowerLeftCorner = this.getOrigin().sub(this.hv.mul(0.5f),
                new Vec3G(0, 0, this.getFocalLength()));
    }

    public float getAspect() {
        return this.aspect;
    }

    /**
     * @return the origin
     */
    public Vec3G getOrigin() {
        return new Vec3G(origin);
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(Vec3G origin) {
        this.origin = origin;
        this.preCalc();
    }

    /**
     * @return the rot
     */
    public Vec3G getRot() {
        return new Vec3G(rot);
    }

    /**
     * @param rot the rot to set
     */
    public void setRot(Vec3G rot) {
        this.rot = rot;
        this.rotMat = Mat4G.rotation(rot);
    }

    /**
     * @return the focalLength
     */
    public float getFocalLength() {
        return focalLength;
    }

    /**
     * @param focalLength the focalLength to set
     */
    public void setFocalLength(float focalLength) {
        this.focalLength = focalLength;
        this.preCalc();
    }

    /**
     * @return the samples
     */
    @Override
    public int getSamples() {
        return samples;
    }

    /**
     * @param samples the samples to set
     */
    public void setSamples(int samples) {
        if (samples <= 0) {
            System.err.println("Cannot set camera samples to be less than or equal to zero");
            return;
        }
        this.samples = samples;
    }

    /**
     * @return the width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
        this.preCalc();
    }

    /**
     * @return the height
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
        this.preCalc();
    }

    /**
     * @return the bounces
     */
    @Override
    public int getBounces() {
        return bounces;
    }

    /**
     * @param bounces the bounces to set
     */
    public void setBounces(int bounces) {
        this.bounces = bounces;
    }
}
