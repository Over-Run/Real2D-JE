package org.overrun.real2d.client.render.world;

import org.overrun.real2d.world.block.Block;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class BlockRenderer {
    public static final int BLOCK_TEX_SIZE = 16;
    public static final int BLOCKS_TEX_SIZE = 256;
    public static final int BLOCKS_PER_TEX = BLOCKS_TEX_SIZE / BLOCK_TEX_SIZE;
    public static final float BLOCK_TEX_UV_FACTOR = (float) BLOCK_TEX_SIZE / (float) BLOCKS_TEX_SIZE;

    public static float u0(int id) {
        return ((id - 1) % BLOCKS_PER_TEX) * BLOCK_TEX_UV_FACTOR;
    }

    public static float u1(int id) {
        return (id % BLOCKS_PER_TEX) * BLOCK_TEX_UV_FACTOR;
    }

    public static float v0(int id) {
        int i = (id - 1) / BLOCKS_PER_TEX;
        return i * BLOCK_TEX_UV_FACTOR;
    }

    public static float v1(int id) {
        int i = id / BLOCKS_PER_TEX;
        return (i + 1) * BLOCK_TEX_UV_FACTOR;
    }

    public static void renderBlock(int x, int y, int z, Block block, float light) {
        float xi = WorldRenderer.unml(x);
        float xi1 = WorldRenderer.unml(x + 1);
        float yi = WorldRenderer.unml(y);
        float yi1 = WorldRenderer.unml(y + 1);
        float zi = WorldRenderer.unml(z);
        int id = block.getId();
        float u0 = u0(id);
        float u1 = u1(id);
        float v0 = v0(id);
        float v1 = v1(id);
        glColor3f(light, light, light);
        glTexCoord2f(u0, v0);
        glVertex3f(xi, yi1, zi);
        glTexCoord2f(u0, v1);
        glVertex3f(xi, yi, zi);
        glTexCoord2f(u1, v1);
        glVertex3f(xi1, yi, zi);
        glTexCoord2f(u1, v0);
        glVertex3f(xi1, yi1, zi);
    }
}
