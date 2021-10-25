package org.overrun.real2d.client;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Main implements Runnable, AutoCloseable {
    private static final Real2D client = Real2D.INSTANCE;

    public void start() {
        client.init();
        run();
    }

    public void render(double delta) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        if (client.worldRenderer != null) {
            client.worldRenderer.renderPick(delta);
        }
        glDisable(GL_DEPTH_TEST);
        client.window.swapBuffers();
    }

    public void tick() {
        client.tick();
    }

    @Override
    public void run() {
        double lastTime = glfwGetTime() * 1000;
        int frames = 0;
        while (!client.window.shouldClose()) {
            client.timer.advanceTime();
            for (int i = 0; i < client.timer.ticks; ++i) {
                tick();
            }
            render(client.timer.delta);
            glfwPollEvents();
            ++frames;
            while (glfwGetTime() * 1000 >= lastTime + 1000) {
                client.window.setTitle(
                        client.appendTitle(
                                String.format(" FPS: %d", frames)));
                lastTime += 1000L;
                frames = 0;
            }
        }
    }

    @Override
    public void close() {
        client.close();
    }

    public static void main(String[] args) {
        try (Main main = new Main()) {
            main.start();
        }
    }
}
