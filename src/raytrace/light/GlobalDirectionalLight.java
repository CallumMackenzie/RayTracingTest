/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.light;

import kvmath.graphics.Vec3G;
import raytrace.HitRecord;
import raytrace.Ray;
import raytrace.Scene;

/**
 *
 * @author callum
 */
public class GlobalDirectionalLight implements Light {

    public Vec3G color;
    public Vec3G direction;

    public GlobalDirectionalLight(Vec3G dir, Vec3G col) {
        this.color = col;
        this.direction = dir;
    }

    @Override
    public Vec3G calcLightRay(Scene s, Ray r, float tMin, float tMax, int depth) {
        if (depth <= 0) {
            return new Vec3G();
        }
        r.direction = this.direction.mul(-1);
        HitRecord hr = s.closestRayHit(r, tMin, tMax);
        if (hr != null) {
            return new Vec3G();
        }
        return this.color;
    }
}
