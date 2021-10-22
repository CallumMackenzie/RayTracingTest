/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace;

import java.awt.Graphics;
import raytrace.renderer.*;
import raytrace.hittable.*;
import java.awt.image.BufferedImage;
import kvmath.graphics.*;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import raytrace.light.*;
import raytrace.material.*;

/**
 *
 * @author callum
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main m = new Main();
        m.run();
//        m.runOneFrame();
        m.dispose();
    }

    private final int width = 350, height = 275;
    private final int dispWidth = 600, dispHeight = 400;
    private final Renderer renderer;
    private final BufferedImage screen = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
    private final int[] imagePixels;
    private final Scene scene = new Scene();
    private final RayCamera camera = new RayCamera(this.width, this.height, 1, 1);

    private final JFrame frame = new JFrame("Ray Tracer - Callum Mackenzie");

    public Main() {
        this.imagePixels = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
        this.setupScene();
        this.renderer = new RayThreadRenderer(this.camera, this.scene, this.imagePixels, 4);
//        ((RayThreadRenderer) this.renderer).threadPollWaitTime = 20;
//        this.renderer = new RayRenderer(this.camera, this.scene, this.imagePixels);

        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setupScene() {
        this.scene.addAllIntersectables(
                new Plane(new Vec3G(0, -1, 0), new Vec3G(0, 0.5, 0),
                        new Lambertian(0.8, 0.9, 0.7)),
                new Sphere(new Vec3G(0, 0, -1), 0.5f, // Center
                        new Metal(0.58, 0.6, 0.6, 0.01)),
                new Sphere(new Vec3G(-0.5, -0.6, 0), 0.44f, // Top
                        new DiElectric(1.5, new Vec3G(1, 1, 1))),
                new Sphere(new Vec3G(-1.1, -0.2, -1), 0.2f, // Right
                        new Lambertian(0.5, 0.3, 0.6))
        );
        this.scene.addLight(new GlobalDirectionalLight(new Vec3G(0, 1, 0), Vec3G.filledWith(0.1)));
        this.scene.addLight(new GlobalDirectionalLight(new Vec3G(0.4, 1, 0.5), Vec3G.filledWith(0.2)));
        this.scene.addLight(new AmbientLight(0.5, 0.5, 0.5));
        this.camera.setOrigin(new Vec3G(0, -0.6, 1));
        this.camera.setRot(new Vec3G(0.1, 0, 0));
        this.camera.setFocalLength(1.2f);
    }

    public void dispose() {
        this.renderer.dispose();
    }

    public void run() {
        this.camera.setSamples(1);
        this.camera.setBounces(10);
        this.frame.setSize(this.dispWidth, this.dispHeight);
        this.frame.setVisible(true);

        long lastFrame = System.currentTimeMillis();
        float delta = 0;
        double ctr = 0;
        Graphics drawPane = this.frame.getGraphics();
        while (true) {
            ctr += 0.5f * delta;
            renderer.render();
            delta = (System.currentTimeMillis() - lastFrame) / 1000.f;
            lastFrame = System.currentTimeMillis();
            {
                Sphere rs = (Sphere) this.scene.getIntersectables().get(3);
                rs.origin.setX(Math.cos(ctr) * 0.7);
                rs.origin.setZ(Math.sin(ctr) * 0.7 - 1);
            }
            if (!this.frame.isDisplayable()) {
                return;
            }
            drawPane.drawImage(screen, 0, 0, this.dispWidth, this.dispHeight, 0, 0,
                    this.screen.getWidth(), this.screen.getHeight(), null);
        }
    }

    public void runOneFrame() {
        this.camera.setSamples(60);
        this.camera.setBounces(80);

        ((RayThreadRenderer) this.renderer).maxThreadWait = Integer.MAX_VALUE;
        renderer.renderLogTimeProgress();
        try {
            ImageIO.write(this.screen, "png", new File("/home/callum/out.png"));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
