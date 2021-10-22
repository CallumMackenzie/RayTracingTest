/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raytrace.renderer;

import raytrace.RayCamera;
import raytrace.Scene;

/**
 *
 * @author callum
 */
public class RayThreadRenderer extends RayRenderer {

    public int maxThreadWait = 2000;
    public volatile int threadPollWaitTime = 0;
    private final Thread[] renderThreads;
    private BusyThreadSectionRenderer[] sectionRenderers;

    private volatile boolean rendering = false;
    protected volatile int threadsDoneRendering = 0;

    protected final class BusyThreadSectionRenderer extends SectionRenderer {

        private volatile boolean doneRendering = true;

        public BusyThreadSectionRenderer(SectionRenderer r) {
            this((RayThreadRenderer) r.renderer, r.pixels, r.x, r.y, r.w, r.h);
        }

        public BusyThreadSectionRenderer(RayThreadRenderer r, int[] px, int x, int y, int w, int h) {
            super(r, px, x, y, w, h);
        }

        public void reset() {
            this.doneRendering = false;
        }

        @Override
        public void run() {
            RayThreadRenderer rtr = (RayThreadRenderer) this.renderer;
            while (rtr.isValid()) {
                if (rtr.threadPollWaitTime != 0) {
                    try {
                        Thread.sleep(rtr.threadPollWaitTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (rtr.rendering && !this.doneRendering) {
                    this.render();
                    this.doneRendering = true;
                    ++threadsDoneRendering;
                }
            }
        }

        public void render() {
            this.renderer.renderSectionTo(this.pixels, this.x, this.y, this.w, this.h);
        }
    }

    public RayThreadRenderer(RayCamera cam, Scene s, int[] pixels, int nThreads) {
        super(cam, s, pixels);
        this.renderThreads = new Thread[nThreads - 1];
        this.sectionRenderers = new BusyThreadSectionRenderer[nThreads];
        SectionRenderer[] sRends = this.getSectionRenderers(pixels, nThreads);
        for (int i = 0; i < nThreads; ++i) {
            this.sectionRenderers[i] = new BusyThreadSectionRenderer(sRends[i]);
        }
        for (int i = 0; i < nThreads - 1; ++i) {
            this.renderThreads[i] = new Thread(this.sectionRenderers[i + 1]);
            this.renderThreads[i].setName("BusySectionRenderThread" + i);
        }
    }

    @Override
    public void internalRender() {
        this.rendering = true;
        this.threadsDoneRendering = 0;
        for (int i = 0; i < this.renderThreads.length; ++i) {
            switch (this.renderThreads[i].getState()) {
            case NEW:
                this.renderThreads[i].start();
                break;
            case TERMINATED:
                System.err.println("Render thread "
                        + this.renderThreads[i].getName()
                        + " unexpectedly terminated, resetting thread.");
                this.renderThreads[i] = new Thread(this.sectionRenderers[i + 1]);
                this.renderThreads[i].setName("BusyResetSectionRenderThread" + i);
                this.renderThreads[i].start();
            default:
                break;
            }
        }
        for (var renderer : this.sectionRenderers) {
            renderer.reset();
        }
        this.sectionRenderers[0].render();
        long startWait = System.currentTimeMillis();
        while (this.threadsDoneRendering < this.renderThreads.length) {
            if (System.currentTimeMillis() - startWait > this.maxThreadWait) {
                System.err.println("Thread taking too long, breaking...");
                break;
            }
        }
        this.rendering = false;
    }
}
