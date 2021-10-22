/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace;

/**
 *
 * @author callum
 */
public abstract class Camera {
    public abstract Ray rayAt(float x, float y);
    
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract int getSamples();
    public abstract int getBounces();
}
