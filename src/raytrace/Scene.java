/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace;

import raytrace.light.Light;
import raytrace.hittable.RayIntersectable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author callum
 */
public class Scene {

    private final ArrayList<RayIntersectable> models = new ArrayList<>();
    private final ArrayList<Light> lights = new ArrayList<>();

    public Scene(RayIntersectable... objs) {
        this.models.addAll(Arrays.asList(objs));
    }

    public void addLight(Light light) {
        this.lights.add(light);
    }

    public void addAllIntersectables(RayIntersectable... riss) {
        for (var ris : riss) {
            this.addIntersectable(ris);
        }
    }

    public ArrayList<Light> getLights() {
        return this.lights;
    }

    public ArrayList<RayIntersectable> getIntersectables() {
        return this.models;
    }

    public void addIntersectable(RayIntersectable r) {
        this.models.add(r);
    }

    public void clear() {
        this.lights.clear();
        this.models.clear();
    }

    public HitRecord closestRayHit(Ray ray, float tMin, float tMax) {
        HitRecord closestHit = null;
        for (var hittable : this.getIntersectables()) {
            HitRecord result = hittable.closestRayHit(ray, tMin, tMax);
            if (result != null) {
                if (closestHit == null) {
                    closestHit = result;
                } else if (closestHit.t > result.t) {
                    closestHit = result;
                }
            }
        }
        return closestHit;
    }

    public HitRecord furthestRayHit(Ray ray, float tMin, float tMax) {
        HitRecord furthestHit = null;
        for (var hittable : this.getIntersectables()) {
            HitRecord result = hittable.furthestRayHit(ray, tMin, tMax);
            if (result != null) {
                if (furthestHit == null) {
                    furthestHit = result;
                } else if (furthestHit.t < result.t) {
                    furthestHit = result;
                }
            }
        }
        return furthestHit;
    }

}
