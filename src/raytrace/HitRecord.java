/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace;

import raytrace.hittable.RayIntersectable;
import kvmath.graphics.Vec3G;

/**
 *
 * @author callum
 */
public class HitRecord {

    public float t;
    public Vec3G point;
    public Vec3G normal;
    public RayIntersectable hit;
    public boolean frontFace = false;

    public HitRecord(Ray r, float t, Vec3G norm, RayIntersectable hit) {
        this.t = t;
        this.point = r.at(t);
        this.normal = norm;
        this.hit = hit;
        this.frontFace = Vec3G.dot(norm, r.direction) <= 0;
    }

    public HitRecord(Ray r, float t, Vec3G pt, Vec3G norm, RayIntersectable hit) {
        this.hit = hit;
        this.point = pt;
        this.normal = norm;
        this.t = t;
        this.frontFace = Vec3G.dot(norm, r.direction) <= 0;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("HitRecord { hit = ")
                .append(this.hit)
                .append(", normal = ")
                .append(this.normal)
                .append(", point = ")
                .append(this.point)
                .append(", frontFace = ")
                .append(this.frontFace)
                .append(", t = ")
                .append(this.t)
                .append(" }")
                .toString();
    }

}
