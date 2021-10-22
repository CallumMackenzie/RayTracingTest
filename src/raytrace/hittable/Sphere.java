/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.hittable;

import raytrace.hittable.RayIntersectable;
import kvmath.graphics.*;
import raytrace.HitRecord;
import raytrace.material.Material;
import raytrace.Ray;

/**
 *
 * @author callum
 */
public class Sphere implements RayIntersectable {

    public Vec3G origin;
    public float radius;
    public Material material;

    public Sphere(Vec3G origin, float radius, Material material) {
        this.origin = origin;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public float[] rayHitRoots(Ray r, float tMin, float tMax) {
        Vec3G oc = r.origin.sub(this.origin);
        float a = r.directionLengthSquared();
        float halfB = Vec3G.dot(oc, r.direction);
        float ocLen = oc.len();
        float c = ocLen * ocLen - radius * radius;

        float discriminant = halfB * halfB - a * c;
        if (discriminant < 0) {
            return new float[0];
        }
        float sqrtd = (float) Math.sqrt(discriminant);
        float root = (-halfB - sqrtd) / a;
        if (root < tMin || tMax < root) {
            root = (-halfB + sqrtd) / a;
            if (root < tMin || tMax < root) {
                return new float[0];
            }
            return new float[]{root};
        }
        float root2 = (-halfB + sqrtd) / a;
        if (root2 < tMin || tMax < root2) {
            return new float[]{root};
        }
        return new float[]{root, root2};
    }

    @Override
    public HitRecord rayHitRoot(Ray r, float root) {
        Vec3G hitPt = r.at(root);
        return new HitRecord(r, root, hitPt,
                hitPt.sub(this.origin).div(this.radius), this);
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Sphere { ")
                .append("radius = ")
                .append(this.radius)
                .append(", origin = ")
                .append(this.origin)
                .append(" }")
                .toString();
    }
}
