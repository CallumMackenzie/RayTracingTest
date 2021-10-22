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
public class Lambertian implements Material {

    final static Random rand = new Random();

    public Vec3G albedo;
    public float scatter = 0.4f;

    public Lambertian(Vec3G albedo) {
        this.albedo = albedo;
    }

    public Lambertian(float r, float g, float b) {
        this.albedo = new Vec3G(r, g, b);
    }

    public Lambertian(double r, double g, double b) {
        this((float) r, (float) g, (float) b);
    }

    @Override
    public Vec3G scatterRay(Ray ray, HitRecord rec) {
        Vec3G scatterDir = new Vec3G((rand.nextFloat() - 0.5f),
                (rand.nextFloat() - 0.5f),
                (rand.nextFloat() - 0.5f)).normalize();
        ray.direction = rec.normal.addEquals(scatterDir.mulEquals(this.scatter));
        ray.origin =  rec.point;
        return this.albedo;
    }

}
