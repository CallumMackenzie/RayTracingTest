/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.hittable;

import kvmath.graphics.Vec3G;
import raytrace.HitRecord;
import raytrace.Ray;
import raytrace.material.Material;

/**
 *
 * @author callum
 */
public class Plane implements RayIntersectable {

    private Material material;
    public Vec3G normal;
    public Vec3G origin;
    
    public Plane(Vec3G norm, Vec3G orig, Material mat) {
        this.normal = norm;
        this.origin = orig;
        this.material = mat;
    }

    @Override
    public HitRecord rayHitRoot(Ray r, float root) {
        return new HitRecord(r, root, new Vec3G(this.normal), this);
    }

    @Override
    public float[] rayHitRoots(Ray r, float tMin, float tMax) {
        float d = this.normal.dot(r.direction);
        if (Math.abs(d) > 0.001) {
            float t = this.origin.sub(r.origin).dot(this.normal) / d;
//            System.out.println("PLANE T: " + t);
            if (t > tMin && t < tMax) {
                return new float[]{t};
            }
        }
        return new float[0];
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

}
