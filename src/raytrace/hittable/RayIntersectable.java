/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.hittable;

import java.util.Arrays;
import raytrace.HitRecord;
import raytrace.material.Material;
import raytrace.Ray;

/**
 *
 * @author callum
 */
public interface RayIntersectable {

    public HitRecord rayHitRoot(Ray r, float root);

    public float[] rayHitRoots(Ray r, float tMin, float tMax);

    public Material getMaterial();

    public default float closestRayHitRoot(Ray r, float tMin, float tMax) {
        float[] hits = this.rayHitRoots(r, tMin, tMax);
        if (hits.length == 0) {
            return Float.MAX_VALUE;
        }
        Arrays.sort(hits);
        return hits[0];
    }

    public default float furthestRayHitRoot(Ray r, float tMin, float tMax) {
        float[] hits = this.rayHitRoots(r, tMin, tMax);
        if (hits.length == 0) {
            return Float.MAX_VALUE;
        }
        Arrays.sort(hits);
        return hits[hits.length - 1];
    }

    public default HitRecord closestRayHit(Ray r, float tMin, float tMax) {
        float root = this.closestRayHitRoot(r, tMin, tMax);
        if (root < tMin || root > tMax) {
            return null;
        }
        return this.rayHitRoot(r, root);
    }

    public default HitRecord furthestRayHit(Ray r, float tMin, float tMax) {
        float root = this.furthestRayHitRoot(r, tMin, tMax);
        if (root < tMin || root > tMax) {
            return null;
        }
        return this.rayHitRoot(r, root);
    }
}
