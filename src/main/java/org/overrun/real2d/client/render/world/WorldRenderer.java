package org.overrun.real2d.client.render.world;

import org.overrun.glutils.Textures;
import org.overrun.glutils.wnd.Framebuffer;
import org.overrun.real2d.client.Real2D;
import org.overrun.real2d.client.Window;
import org.overrun.real2d.world.HitResult;
import org.overrun.real2d.world.World;
import org.overrun.real2d.world.WorldListener;
import org.overrun.real2d.world.block.Block;
import org.overrun.real2d.world.block.Blocks;
import org.overrun.real2d.world.entity.Player;
import org.overrun.real2d.world.phys.AABBox;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class WorldRenderer implements WorldListener, AutoCloseable {
    public static final int WORLD_RENDER_NML = 32;
    public static int blocks;
    private boolean isDirty;
    private final World world;
    private final int list;

    public WorldRenderer(World world) {
        isDirty = true;
        this.world = world;
        list = glGenLists(1);
        if (!glIsTexture(blocks)) {
            try {
                blocks = Textures.loadAWT(WorldRenderer.class.getClassLoader(),
                        "assets.real2d/textures/block/blocks.png",
                        GL_NEAREST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static float unml(float a) {
        return a * WORLD_RENDER_NML;
    }

    public static float xOffset(Player p) {
        return ((float) (int) ((Real2D.INSTANCE.framebuffer.getWidth() * 0.5f) - (int) unml(p.prevX)));
    }

    public static float yOffset(Player p) {
        return ((float) (int) ((Real2D.INSTANCE.framebuffer.getHeight() * 0.5f) - (int) unml(p.prevY)));
    }

    public void markDirty() {
        isDirty = true;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void render(double delta) {
        if (isDirty()) {
            glNewList(list, GL_COMPILE);
            glBegin(GL_QUADS);
            for (int z = 0; z < world.depth; ++z) {
                for (int x = 0; x < world.width; ++x) {
                    for (int y = 0; y < world.height; ++y) {
                        Block block = world.getBlock(x, y, z);
                        if (block != Blocks.AIR) {
                            float light = (1.0f / 30.0f) * world.getLight(x, y, z);
                            if (z != 0) {
                                light += 0.5f;
                            }
                            BlockRenderer.renderBlock(x, y, z, block, light);
                        }
                    }
                }
            }
            glEnd();
            glEndList();
            isDirty = false;
        }
    }

    public void pick() {
        Window window = Real2D.INSTANCE.window;
        Framebuffer fb = Real2D.INSTANCE.framebuffer;
        int mx = window.mouseX;
        int my = fb.getHeight() - window.mouseY;
        boolean selected = false;
        float xo = xOffset(world.player);
        float yo = yOffset(world.player);
        for (int x = 0; x < world.width; ++x) {
            for (int y = 0; y < world.height; ++y) {
                float bx = unml(x);
                float bx1 = bx + WORLD_RENDER_NML;
                float by = unml(y);
                float by1 = by + WORLD_RENDER_NML;
                float obx = bx + xo;
                float obx1 = bx1 + xo;
                float oby = by + yo;
                float oby1 = by1 + yo;
                if (mx >= obx
                        && mx < obx1
                        && my >= oby
                        && my < oby1) {
                    selected = true;
                    Block block = world.getBlock(x, y, Real2D.INSTANCE.selectZ);
                    world.hitResult = new HitResult(x, y, Real2D.INSTANCE.selectZ, block);
                    break;
                }
            }
        }
        // Check outside world
        if (!selected) {
            world.hitResult = null;
        }
    }

    public void renderHit(double delta) {
        float x = world.hitResult.x;
        float y = world.hitResult.y;
        float z = world.hitResult.z;
        AABBox bb = new AABBox();
        AABBox outline = world.hitResult.block.getOutline();
        if (outline != null) {
            outline.move(x, y, z, bb);
        }
        float fz = unml(bb.start_z);
        float fx = unml(bb.start_x);
        float fx1 = unml(bb.end_x);
        float fy = unml(bb.start_y);
        float fy1 = unml(bb.end_y);
        glBegin(GL_LINE_LOOP);
        glColor4f(0.0f, 0.0f, 0.0f, 0.8f);
        glVertex3f(fx + 1, fy1, fz);
        glVertex3f(fx + 1, fy, fz);
        glVertex3f(fx1, fy, fz);
        glVertex3f(fx1, fy1 - 1, fz);
        glEnd();
    }

    public void renderPick(double delta) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glPushMatrix();
        glTranslatef(xOffset(world.player), yOffset(world.player), 0);
        render(delta);
        glBindTexture(GL_TEXTURE_2D, blocks);
        glCallList(list);
        glBindTexture(GL_TEXTURE_2D, 0);
        pick();
        if (world.hitResult != null) {
            renderHit(delta);
        }
        glPopMatrix();

        glDisable(GL_BLEND);

        world.player.render(delta);
    }

    @Override
    public void blockChanged(int x, int y, int z) {
        markDirty();
    }

    @Override
    public void lightColChanged(int x, int z) {
    }

    @Override
    public void close() {
        if (glIsList(list)) {
            glDeleteLists(list, 1);
        }
    }
}
