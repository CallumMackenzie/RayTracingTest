/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.renderer;

import raytrace.HitRecord;
import raytrace.Ray;
import raytrace.RayCamera;
import raytrace.Scene;
import kvmath.graphics.Vec3G;

/**
 *
 * @author callum
 */
public class RayRenderer extends Renderer {

    final int[] pixels;

    public RayRenderer(RayCamera c, Scene s, int[] pixels) {
        this.setCamera(c);
        this.setScene(s);
        this.pixels = pixels;
    }

    protected Vec3G rayColor(Ray r, int depth) {
        if (depth <= 0) {
            return new Vec3G();
        }
        float tMin = 0.001f, tMax = Float.MAX_VALUE / 8;
        HitRecord hit = this.getScene().closestRayHit(r, tMin, tMax);
        if (hit != null) {
//            System.out.println(hit);
            Vec3G attentuation = hit.hit.getMaterial().scatterRay(r, hit);
            Vec3G light = new Vec3G();
            for (var lightObj : this.getScene().getLights()) {
                light.addEquals(lightObj.calcLightRay(this.getScene(),
                        new Ray(r.origin, r.direction), tMin, tMax, depth - 1));
            }
            if (attentuation != null) {
                return this.rayColor(r, depth - 1).mulEquals(attentuation, light);
            }
            return new Vec3G();
        }
        return Vec3G.filledWith(1f);
    }

    @Override
    public Vec3G renderPixel(int r, int c) {
        Vec3G color = new Vec3G();
        for (int s = 0; s < this.getCamera().getSamples(); ++s) {
            color.addEquals(this.rayColor(this.getCamera().rayAt(c, r),
                    this.getCamera().getBounces()));
        }
        return color;
    }

    @Override
    protected Vec3G postShader(Vec3G col, int r, int c) {
        Vec3G ret = col.mulEquals(1.f / this.getCamera().getSamples()).sqrt();
        return ret;
    }

    @Override
    protected void internalRender() {
        this.renderSectionTo(pixels, 0, 0,
                this.getCamera().getWidth(), this.getCamera().getHeight());
    }
}
