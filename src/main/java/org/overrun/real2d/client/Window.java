package org.overrun.real2d.client;

import org.overrun.glutils.wnd.GLFWindow;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Window extends GLFWindow {
    public static final int MBL = GLFW_MOUSE_BUTTON_LEFT;
    public static final int MBM = GLFW_MOUSE_BUTTON_MIDDLE;
    public static final int MBR = GLFW_MOUSE_BUTTON_RIGHT;
    public int mouseX, mouseY;

    /**
     * construct and create window
     *
     * @param width  window width
     * @param height window height
     * @param title  window title
     */
    public Window(int width, int height, String title) {
        super(width, height, title);
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(hWnd, key) == GLFW_PRESS;
    }

    public boolean isMouseDown(int button) {
        return glfwGetMouseButton(hWnd, button) == GLFW_PRESS;
    }
}
