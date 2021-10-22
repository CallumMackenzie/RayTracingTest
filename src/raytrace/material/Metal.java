/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.material;

import java.util.Random;
import kvmath.graphics.Vec3G;
import raytrace.HitRecord;
import raytrace.Ray;

/**
 *
 * @author callum
 */
public class Metal implements Material {

    private static final Random random = new Random();

    public Vec3G albedo;
    public float fuzz = 0.1f;

    public Metal(Vec3G albedo) {
        this.albedo = albedo;
    }

    public Metal(float r, float g, float b) {
        this.albedo = new Vec3G(r, g, b);
    }

    public Metal(double r, double g, double b) {
        this((float) r, (float) g, (float) b);
    }

    public Metal(float r, float g, float b, float fuzz) {
        this(r, g, b);
        this.fuzz = fuzz;
    }

    public Metal(double r, double g, double b, double fuzz) {
        this((float) r, (float) g, (float) b, (float) fuzz);
    }

    public Metal(Vec3G albedo, float fuzz) {
        this(albedo);
        this.fuzz = fuzz;
    }

    public Metal(Vec3G albedo, double fuzz) {
        this(albedo, (float) fuzz);
    }

    @Override
    public Vec3G scatterRay(Ray ray, HitRecord rec) {
        ray.origin = rec.point;
        ray.direction = Vec3G.reflect(ray.direction, rec.normal)
                .addEquals(new Vec3G(
                        random.nextFloat() - 0.5f,
                        random.nextFloat() - 0.5f,
                        random.nextFloat() - 0.5f
                ).normalize().mulEquals(this.fuzz));
        return this.albedo;
    }

}
