package org.overrun.real2d.client;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.overrun.glutils.AWTImage;
import org.overrun.glutils.wnd.Framebuffer;
import org.overrun.real2d.client.render.world.WorldRenderer;
import org.overrun.real2d.world.World;
import org.overrun.real2d.world.block.Blocks;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.real2d.world.block.Blocks.GRASS_BLOCK;
import static org.overrun.real2d.world.block.Blocks.STONE;

/**
 * @author squid233
 * @since 0.1.0
 */
public enum Real2D implements AutoCloseable {
    INSTANCE;

    public static final String GAME_VER = "0.1.0";
    public static final int DEF_W = 854;
    public static final int DEF_H = 480;
    public final Timer timer = new Timer(60);
    @Nullable
    public WorldRenderer worldRenderer;
    public Window window;
    public Framebuffer framebuffer;
    public int selectZ = 1;
    @Nullable
    public World world;

    public String appendTitle(String... args) {
        StringBuilder sb = new StringBuilder("Real2D ")
                .append(GAME_VER);
        for (String arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }

    public void tick() {
        if (world != null) {
            world.tick();
        }
    }

    public void init() {
        GLFWErrorCallback.createPrint().set();
        if (!glfwInit()) {
            throw new RuntimeException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = new Window(DEF_W, DEF_H, appendTitle());

        window.keyCb((hWnd, key, scancode, action, mods) -> {
            if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_ESCAPE) {
                    glfwSetWindowShouldClose(hWnd, true);
                }
                if (world != null) {
                    if (key == GLFW_KEY_1) {
                        world.player.hand = GRASS_BLOCK;
                    }
                    if (key == GLFW_KEY_2) {
                        world.player.hand = STONE;
                    }
                    if (key == GLFW_KEY_Z) {
                        if (selectZ == 0) {
                            selectZ = 1;
                        } else if (selectZ == 1) {
                            selectZ = 0;
                        }
                    }
                }
            }
        });
        framebuffer = new Framebuffer();
        framebuffer.cb = (hWnd, width, height) -> {
            glViewport(0, 0, width, height);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, width, 0, height, -100, 100);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
        };
        framebuffer.init(window);
        window.cursorPosCb((hWnd, x, y) -> {
            window.mouseX = (int) x;
            window.mouseY = (int) y;
        });
        glfwSetScrollCallback(window.getHandle(), (hWnd, xoffset, yoffset) -> {
            if (yoffset != 0 && world != null) {
                if (world.player.hand == GRASS_BLOCK) {
                    world.player.hand = STONE;
                } else if (world.player.hand == STONE) {
                    world.player.hand = GRASS_BLOCK;
                }
            }
        });

        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (mode != null) {
            window.setPos((mode.width() - window.getWidth()) >> 1,
                    (mode.height() - window.getHeight()) >> 1);
        }

        ClassLoader cl = Real2D.class.getClassLoader();

        try (var images = GLFWImage.malloc(3)) {
            var img16 = GLFWImage.malloc();
            var img32 = GLFWImage.malloc();
            var img48 = GLFWImage.malloc();

            var pix = AWTImage.getRGB(AWTImage.load(cl, "assets.real2d/icon16.png"));
            var bb = ByteBuffer.allocateDirect(pix.length * 4);
            bb.asIntBuffer().put(pix);
            images.put(0, img16.set(16, 16, bb));

            pix = AWTImage.getRGB(AWTImage.load(cl, "assets.real2d/icon32.png"));
            bb = ByteBuffer.allocateDirect(pix.length * 4);
            bb.asIntBuffer().put(pix);
            images.put(1, img32.set(32, 32, bb));

            pix = AWTImage.getRGB(AWTImage.load(cl, "assets.real2d/icon48.png"));
            bb = ByteBuffer.allocateDirect(pix.length * 4);
            bb.asIntBuffer().put(pix);
            images.put(2, img48.set(48, 48, bb));

            glfwSetWindowIcon(window.getHandle(), images);
            img16.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        window.makeCurr();
        GL.createCapabilities();
        glfwSwapInterval(1);

        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        glEnable(GL_TEXTURE_2D);
        Blocks.init();
        world = new World(48, 32, 2);
        world.create();
        worldRenderer = new WorldRenderer(world);
        world.addListener(worldRenderer);

        timer.advanceTime();

        window.show();
    }

    @Override
    public void close() {
        if (world != null) {
            world.save();
        }
        if (worldRenderer != null) {
            worldRenderer.close();
        }
        window.close();
        glfwTerminate();
        var cb = glfwSetErrorCallback(null);
        if (cb != null) {
            cb.free();
        }
    }
}
