/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.material;

import kvmath.graphics.Vec3G;
import raytrace.HitRecord;
import raytrace.Ray;
import java.util.Random;

/**
 *
 * @author callum
 */
public class DiElectric implements Material {

    private static final Random rand = new Random();

    public Vec3G attentuation;
    public double ior;

    public DiElectric() {
        this(Vec3G.filledWith(1));
    }

    public DiElectric(double ior, Vec3G attentuation) {
        this.ior = ior;
        this.attentuation = attentuation;
    }

    public DiElectric(Vec3G at) {
        this(1.5, at);
    }

    @Override
    public Vec3G scatterRay(Ray ray, HitRecord rec) {
        if (Math.abs(this.ior) < 0.001) {
            ray.origin = rec.point;
            return this.attentuation;
        }
        double refractRatio = rec.frontFace
                ? (1.0 / this.ior)
                : (this.ior);

        float cosTheta = Math.min(ray.direction.dot(rec.normal), 1);
        float sinTheta = (float) Math.sqrt(1 - cosTheta * cosTheta);
        boolean cantRefract = refractRatio * sinTheta > 1;

        if (cantRefract) {
            ray.direction = Vec3G.reflect(ray.direction, rec.normal);
        } else {
            ray.direction = Vec3G.refractWithCosine(
                    ray.direction,
                    rec.normal,
                    (float) refractRatio, cosTheta);
        }
        ray.origin = rec.point;
        return this.attentuation;
    }

    public static double reflectance(double cos, double refractiveIndex) {
        double r0 = (1 - refractiveIndex) / (1 + refractiveIndex);
        r0 *= r0;
        return r0 + (1 - r0) * Math.pow((1 - cos), 5);
    }
}
