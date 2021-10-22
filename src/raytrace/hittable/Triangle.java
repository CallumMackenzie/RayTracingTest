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
public class Triangle implements RayIntersectable {

    private Material material;
    public Vec3G[] points = new Vec3G[3];
    public Vec3G normal;

    public Triangle(Vec3G[] points, Material mat) {
        this.material = mat;
        this.points = points;
    }

    @Override
    public HitRecord rayHitRoot(Ray r, float root) {
        return null;
    }

    @Override
    public float[] rayHitRoots(Ray r, float tMin, float tMax) {
        float d = Vec3G.dot(this.normal, this.points[0]);
        float t = -(Vec3G.dot(this.normal, r.origin) + d) / Vec3G.dot(this.normal, r.direction);
        return new float[0];
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

}
