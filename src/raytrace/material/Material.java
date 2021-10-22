/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.material;

import kvmath.graphics.Vec3G;
import raytrace.HitRecord;
import raytrace.Ray;

/**
 *
 * @author callum
 */
public interface Material {    
    Vec3G scatterRay(Ray ray, HitRecord rec);
}
