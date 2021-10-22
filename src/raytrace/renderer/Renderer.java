/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.renderer;

import java.text.DecimalFormat;
import kvmath.graphics.Vec3G;
import raytrace.Camera;
import raytrace.Scene;

/**
 *
 * @author callum
 */
public abstract class Renderer {

    private Camera camera;
    private Scene scene;

    protected volatile long pixelsRendered = 0;
    protected volatile long totalPixels = 0;
    protected volatile boolean rendered = false;
    private volatile boolean isAlive = true;

    protected abstract void internalRender();

    public abstract Vec3G renderPixel(int r, int x);

    protected abstract Vec3G postShader(Vec3G in, int r, int x);

    public final void dispose() {
        this.isAlive = false;
    }

    public final boolean isValid() {
        return this.isAlive;
    }

    /**
     * @return the pixelsRendered
     */
    public final long getPixelsRendered() {
        return pixelsRendered;
    }

    /**
     * @return the totalPixels
     */
    public final long getTotalPixels() {
        return totalPixels;
    }

    /**
     * @return the rendered
     */
    public final boolean isRendered() {
        return rendered;
    }

    /**
     * @return the camera
     */
    public final Camera getCamera() {
        return camera;
    }

    /**
     * @param camera the camera to set
     */
    public final void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * @return the scene
     */
    public final Scene getScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public final void setScene(Scene scene) {
        this.scene = scene;
    }

    public static class SectionRenderer implements Runnable {

        protected volatile int[] pixels;
        protected final int x, y, w, h;
        protected volatile Renderer renderer;

        public SectionRenderer(Renderer r, int[] px, int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.renderer = r;
            this.pixels = px;
        }

        @Override
        public void run() {
            this.renderer.renderSectionTo(pixels, x, y, w, h);
        }

        public int[] getPixels() {
            return this.pixels;
        }

        public int getSrcStart() {
            return this.y * this.w + this.x;
        }
    }

    public static abstract class RenderObserver implements Runnable {

        private volatile Renderer renderer;
        private final int sleepTime;

        public RenderObserver(Renderer r, int sleepTime) {
            this.renderer = r;
            this.sleepTime = sleepTime;
        }

        public Renderer getRenderer() {
            return this.renderer;
        }

        @Override
        public final void run() {
            while (true) {
                try {
                    Thread.sleep(this.sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                if (!this.renderer.isRendered()) {
                    this.observe();
                } else {
                    return;
                }
            }
        }

        public abstract void observe();
    }

    public static final class RenderProgressLogger extends RenderObserver {

        private static final DecimalFormat nf = new DecimalFormat();

        public RenderProgressLogger(Renderer r) {
            super(r, 1000);
        }

        @Override
        public void observe() {
            if (this.getRenderer().getTotalPixels() != 0) {
                double progress = this.getRenderer().getPixelsRendered()
                        / (float) this.getRenderer().getTotalPixels() * 100.f;
                System.out.println("Render progress: "
                        + nf.format(progress)
                        + "%");
            } else {
                System.err.println("No pixels.");
            }
        }

    }

    public final void renderSectionTo(int[] pixels, int x, int y, int w, int h) {
        for (int r = y; r < h + y; ++r) {
            for (int c = x; c < w + x; ++c) {
                try {
                    pixels[r * this.getCamera().getWidth() + c] = this.postShader(
                            this.renderPixel(r, c),
                            r, c)
                            .restrictedColor().rgbAsInt();
                } catch (ArrayIndexOutOfBoundsException e) {
                    return;
                }
                ++this.pixelsRendered;
            }
        }
    }

    protected final SectionRenderer[] getSectionRenderers(int[] pixels, int nSections) {
        SectionRenderer[] ret = new SectionRenderer[nSections];
        if (nSections == 0) {
            return ret;
        }
        int width = this.getCamera().getWidth(), height = this.getCamera().getHeight();
        int heiFrac = width / nSections,
                localOffset = height % nSections;
        ret[0] = new SectionRenderer(this, pixels, 0, 0, width, heiFrac + localOffset);
        for (int i = 1; i < nSections; ++i) {
            ret[i] = new SectionRenderer(this, pixels, 0,
                    i * heiFrac + localOffset, width, heiFrac);
        }
        return ret;
    }

    public final void renderWithObservers(RenderObserver... obs) {
        Thread[] obThreads = new Thread[obs.length];
        for (int i = 0; i < obs.length; ++i) {
            if (obs[i] != null) {
                obThreads[i] = new Thread(obs[i]);
                obThreads[i].start();
            }
        }
        this.render();
    }

    public final void renderLogProgress() {
        this.renderWithObservers(new RenderProgressLogger(this));
    }

    public final void renderLogTime() {
        long start = System.currentTimeMillis();
        this.render();
        start = System.currentTimeMillis() - start;
        System.out.println("Rendered in " + Renderer.millisToRenderTimeStr(start));
    }

    protected static final String millisToRenderTimeStr(long millis) {
        long secs = millis / 1000;
        millis -= secs * 1000;
        long mins = secs / 60;
        secs -= mins * 60;
        long hrs = mins / 60;
        mins -= hrs * 60;
        long days = hrs / 24;
        hrs -= days * 24;
        return (days != 0 ? days + "d " : "")
                + (hrs != 0 ? hrs + "h " : "")
                + (mins != 0 ? mins + "m " : "")
                + (secs != 0 ? secs + "s " : "")
                + (millis + "ms");
    }

    public final void renderLogTimeProgress() {
        long start = System.currentTimeMillis();
        this.renderLogProgress();
        System.out.println("Rendered in "
                + Renderer.millisToRenderTimeStr(System.currentTimeMillis() - start));
    }

    public final void render() {
        this.pixelsRendered = 0;
        this.totalPixels = this.getCamera().getWidth() * this.getCamera().getHeight();
        this.rendered = false;
        this.internalRender();
        this.rendered = true;
    }
}
