package org.overrun.real2d.client;

import org.jetbrains.annotations.Contract;
import org.overrun.glutils.AtlasLoom;
import org.overrun.glutils.AtlasLoomAWT;
import org.overrun.glutils.game.Game;

import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.game.GLStateManager.enableTexture2D;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Real2D extends Game {
    protected static Real2D instance;
    public static final String VERSION = "0.1.0";
    private AtlasLoomAWT blockAtlas;

    /**
     * Get the instance of the client.
     *
     * @return {@link #instance}
     */
    @Contract(pure = true)
    public static Real2D getInstance() {
        return instance;
    }

    @Override
    public void create() {
        enableTexture2D();
        blockAtlas = AtlasLoom.awt("block");
    }

    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        super.resize(width, height);
    }
}
