/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.light;

import kvmath.graphics.Vec3G;
import raytrace.Ray;
import raytrace.Scene;

/**
 *
 * @author callum
 */
public interface Light {
    public Vec3G calcLightRay(Scene s, Ray r, float tMin, float tMax, int depth);
}
