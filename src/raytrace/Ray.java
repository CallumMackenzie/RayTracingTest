/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace;

import kvmath.graphics.Vec3G;

/**
 *
 * @author callum
 */
public class Ray {

    public Vec3G origin;
    public Vec3G direction;

    public Ray(Vec3G origin, Vec3G dir) {
        this.origin = origin;
        this.direction = dir.normalized();
    }

    public float directionLengthSquared() {
        float len = this.direction.len();
        return len * len;
    }

    public Vec3G at(float ptLen) {
        return this.origin.add(this.direction.mul(ptLen));
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Ray { direction = ")
                .append(this.direction)
                .append(", origin = ")
                .append(this.origin)
                .append(" }")
                .toString();
    }
}
