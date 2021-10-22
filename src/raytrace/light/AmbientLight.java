/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.light;

import kvmath.graphics.Vec3G;
import raytrace.Ray;
import raytrace.Scene;

/**
 *
 * @author callum
 */
public class AmbientLight implements Light {

    public Vec3G color;

    public AmbientLight(Vec3G col) {
        this.color = col;
    }

    public AmbientLight(float r, float g, float b) {
        this(new Vec3G(r, g, b));
    }

    public AmbientLight(double r, double g, double b) {
        this((float) r, (float) g, (float) b);
    }

    @Override
    public Vec3G calcLightRay(Scene s, Ray r, float tMin, float tMax, int depth) {
        return this.color;
    }

}
