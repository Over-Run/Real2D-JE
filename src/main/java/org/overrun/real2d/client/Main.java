package org.overrun.real2d.client;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

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

    @Override
    public void run() {
        double lastTime = glfwGetTime() * 1000;
        int frames = 0;
        while (!client.window.shouldClose()) {
            client.timer.advanceTime();
            for (int i = 0; i < client.timer.ticks; ++i) {
                client.tick();
            }
            client.render();
            client.update();
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
