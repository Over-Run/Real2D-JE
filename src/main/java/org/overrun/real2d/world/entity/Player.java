package org.overrun.real2d.world.entity;

import org.overrun.glutils.Textures;
import org.overrun.glutils.wnd.Framebuffer;
import org.overrun.real2d.client.Real2D;
import org.overrun.real2d.client.Window;
import org.overrun.real2d.client.render.world.BlockRenderer;
import org.overrun.real2d.client.render.world.WorldRenderer;
import org.overrun.real2d.world.World;
import org.overrun.real2d.world.block.Block;
import org.overrun.real2d.world.block.Blocks;
import org.overrun.real2d.world.phys.AABBox;

import java.io.Serializable;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.real2d.client.render.world.BlockRenderer.BLOCK_TEX_SIZE;
import static org.overrun.real2d.client.render.world.WorldRenderer.WORLD_RENDER_NML;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Player implements Serializable {
    public static final String TEX_PLAYER = "assets.real2d/textures/entity/player.png";
    public static final float TEX_PLAYER_W = 48.0f;
    public static final float TEX_PLAYER_H = 64.0f;
    public final transient float bbWidth;
    public final transient float bbHeight;
    public World world;
    public float x, y, z;
    public transient float prevX, prevY;
    public transient AABBox bb;
    public transient boolean onGround;
    public transient Block hand = Blocks.GRASS_BLOCK;
    private transient float headXRot = 0.0f;
    private transient float yRot = 0.0f;

    public Player(World world) {
        bbWidth = 0.6f;
        bbHeight = 1.8f;
        this.world = world;
        x = 16.0f;
        y = 9.0f;
        z = 1.0f;
        prevX = x;
        prevY = y;
        bb = new AABBox(x - 0.3f,
                y,
                z,
                x + 0.3f,
                y + bbHeight,
                z + 1);
        onGround = false;
    }

    public void move(float xa, float ya, float speed) {
        float xd = xa * speed;
        float yd = ya * speed;
        float ydOrg = yd;
        List<AABBox> cubes = world.getCubes(bb.expand(xd, yd, 0));
        AABBox copyBB = new AABBox(bb);
        if (xd != 0) {
            for (var c : cubes) {
                bb.move(xd, 0, 0, copyBB);
                if (c.isIntersect(copyBB)) {
                    xd = 0;
                    break;
                }
            }
            x += xd;
            if (x < 0) {
                x = 0;
            }
            if (x > world.width) {
                x = world.width;
            }
        }
        if (yd != 0) {
            for (var c : cubes) {
                bb.move(0, yd, 0, copyBB);
                if (c.isIntersect(copyBB)) {
                    yd = 0;
                    break;
                }
            }
            onGround = ydOrg != yd && ydOrg < 0;
            y += yd;
            if (y < -5) {
                y = -5;
            }
        }
    }

    public void tick() {
        Window window = Real2D.INSTANCE.window;
        Framebuffer fb = Real2D.INSTANCE.framebuffer;
        prevX = x;
        prevY = y;
        float xa = 0;
        float ya = 0;
        if (window.isKeyDown(GLFW_KEY_SPACE)
                || window.isKeyDown(GLFW_KEY_W)) {
            ++ya;
        }
        if (window.isKeyDown(GLFW_KEY_LEFT_SHIFT)
                || window.isKeyDown(GLFW_KEY_S)) {
            --ya;
        }
        if (window.isKeyDown(GLFW_KEY_A)) {
            --xa;
        }
        if (window.isKeyDown(GLFW_KEY_D)) {
            ++xa;
        }
        move(xa, ya, onGround ? 0.03f : 0.04f);
        bb.set(x - 0.3f,
                y,
                z,
                x + 0.3f,
                y + bbHeight,
                z + 1);
        headXRot = window.mouseY - fb.getHeight() * 0.5f;
        yRot = window.mouseX - fb.getWidth() * 0.5f;
        if (headXRot < -90) {
            headXRot = -90;
        }
        if (headXRot > 90) {
            headXRot = 90;
        }
        if (yRot < -90) {
            yRot = -90;
        }
        if (yRot > 90) {
            yRot = 90;
        }
    }

    public void render(double delta) {
        Framebuffer fb = Real2D.INSTANCE.framebuffer;
        glPushMatrix();
        glTranslatef(fb.getWidth() * 0.5f, fb.getHeight() * 0.5f, 20.125f);
        int id = 0;
        try {
            id = Textures.loadAWT(Player.class.getClassLoader(), TEX_PLAYER, GL_NEAREST);
        } catch (Exception e) {
            throw new RuntimeException("!");
        }

        float u0 = 0;
        float u4 = 4 / TEX_PLAYER_W;
        float u8 = 8 / TEX_PLAYER_W;
        float u12 = 12 / TEX_PLAYER_W;
        float u16 = 16 / TEX_PLAYER_W;
        float u20 = 20 / TEX_PLAYER_W;
        float u24 = 24 / TEX_PLAYER_W;
        float u28 = 28 / TEX_PLAYER_W;
        float u32 = 32 / TEX_PLAYER_W;
        float u36 = 36 / TEX_PLAYER_W;
        float u40 = 40 / TEX_PLAYER_W;
        float v0 = 0;
        float v8 = 8 / TEX_PLAYER_H;
        float v16 = 16 / TEX_PLAYER_H;
        float v28 = 28 / TEX_PLAYER_H;
        float v40 = 40 / TEX_PLAYER_H;
        float v52 = 52 / TEX_PLAYER_H;

        glColor3f(1.0f, 1.0f, 1.0f);
        glBindTexture(GL_TEXTURE_2D, id);
        // Head
        glPushMatrix();
        glTranslatef(0, 48, -8);
        glRotatef(headXRot, 1, 0, 0);
        glRotatef(yRot, 0, 1, 0);
        glScalef(WORLD_RENDER_NML, WORLD_RENDER_NML, WORLD_RENDER_NML);
        glBegin(GL_QUADS);
        // front
        glTexCoord2f(u8, v8);
        glVertex3f(-0.25f, 0.5f, 0.25f);
        glTexCoord2f(u8, v16);
        glVertex3f(-0.25f, 0, 0.25f);
        glTexCoord2f(u16, v16);
        glVertex3f(0.25f, 0, 0.25f);
        glTexCoord2f(u16, v8);
        glVertex3f(0.25f, 0.5f, 0.25f);
        // left
        glTexCoord2f(u0, v8);
        glVertex3f(-0.25f, 0.5f, -0.25f);
        glTexCoord2f(u0, v16);
        glVertex3f(-0.25f, 0, -0.25f);
        glTexCoord2f(u8, v16);
        glVertex3f(-0.25f, 0, 0.25f);
        glTexCoord2f(u8, v8);
        glVertex3f(-0.25f, 0.5f, 0.25f);
        // right
        glTexCoord2f(u16, v8);
        glVertex3f(0.25f, 0.5f, 0.25f);
        glTexCoord2f(u16, v16);
        glVertex3f(0.25f, 0, 0.25f);
        glTexCoord2f(u24, v16);
        glVertex3f(0.25f, 0, -0.25f);
        glTexCoord2f(u24, v8);
        glVertex3f(0.25f, 0.5f, -0.25f);
        // top
        glTexCoord2f(u8, v0);
        glVertex3f(-0.25f, 0.5f, 0.25f);
        glTexCoord2f(u8, v8);
        glVertex3f(-0.25f, 0.5f, -0.25f);
        glTexCoord2f(u16, v8);
        glVertex3f(0.25f, 0.5f, -0.25f);
        glTexCoord2f(u16, v0);
        glVertex3f(0.25f, 0.5f, 0.25f);
        // bottom
        glTexCoord2f(u16, v0);
        glVertex3f(-0.25f, 0, 0.25f);
        glTexCoord2f(u16, v8);
        glVertex3f(-0.25f, 0, -0.25f);
        glTexCoord2f(u24, v8);
        glVertex3f(0.25f, 0, -0.25f);
        glTexCoord2f(u24, v0);
        glVertex3f(0.25f, 0, 0.25f);
        glEnd();
        glPopMatrix();

        // Body
        glPushMatrix();
        glTranslatef(0, 48, 0);
        glRotatef(yRot, 0, 1, 0);
        glScalef(WORLD_RENDER_NML, WORLD_RENDER_NML, WORLD_RENDER_NML);
        glBegin(GL_QUADS);
        // front
        glTexCoord2f(u16, v16);
        glVertex3f(-0.25f, 0, 0.125f);
        glTexCoord2f(u16, v28);
        glVertex3f(-0.25f, -0.75f, 0.125f);
        glTexCoord2f(u24, v28);
        glVertex3f(0.25f, -0.75f, 0.125f);
        glTexCoord2f(u24, v16);
        glVertex3f(0.25f, 0, 0.125f);
        // left
        glTexCoord2f(u12, v16);
        glVertex3f(-0.25f, 0, -0.125f);
        glTexCoord2f(u12, v28);
        glVertex3f(-0.25f, -0.75f, -0.125f);
        glTexCoord2f(u16, v28);
        glVertex3f(-0.25f, -0.75f, 0.125f);
        glTexCoord2f(u16, v16);
        glVertex3f(-0.25f, 0, 0.125f);
        // right
        glTexCoord2f(u24, v16);
        glVertex3f(0.25f, 0, 0.125f);
        glTexCoord2f(u24, v28);
        glVertex3f(0.25f, -0.75f, 0.125f);
        glTexCoord2f(u28, v28);
        glVertex3f(0.25f, -0.75f, -0.125f);
        glTexCoord2f(u28, v16);
        glVertex3f(0.25f, 0, -0.125f);
        glEnd();
        glPopMatrix();

        // right arm
        glPushMatrix();
        glTranslatef(0, 48, 0);
        glRotatef(yRot, 0, 1, 0);
        glScalef(WORLD_RENDER_NML, WORLD_RENDER_NML, WORLD_RENDER_NML);
        glBegin(GL_QUADS);
        // front
        glTexCoord2f(u32, v16);
        glVertex3f(-0.5f, 0, 0.125f);
        glTexCoord2f(u32, v28);
        glVertex3f(-0.5f, -0.75f, 0.125f);
        glTexCoord2f(u36, v28);
        glVertex3f(-0.25f, -0.75f, 0.125f);
        glTexCoord2f(u36, v16);
        glVertex3f(-0.25f, 0, 0.125f);
        // left
        glTexCoord2f(u28, v16);
        glVertex3f(-0.5f, 0, -0.125f);
        glTexCoord2f(u28, v28);
        glVertex3f(-0.5f, -0.75f, -0.125f);
        glTexCoord2f(u32, v28);
        glVertex3f(-0.5f, -0.75f, 0.125f);
        glTexCoord2f(u32, v16);
        glVertex3f(-0.5f, 0, 0.125f);
        // right
        glTexCoord2f(u36, v16);
        glVertex3f(-0.25f, 0, 0.125f);
        glTexCoord2f(u36, v28);
        glVertex3f(-0.25f, -0.75f, 0.125f);
        glTexCoord2f(u40, v28);
        glVertex3f(-0.25f, -0.75f, -0.125f);
        glTexCoord2f(u40, v16);
        glVertex3f(-0.25f, 0, -0.125f);
        glEnd();
        glPopMatrix();

        // left arm
        glPushMatrix();
        glTranslatef(0, 48, 0);
        glRotatef(yRot, 0, 1, 0);
        glScalef(WORLD_RENDER_NML, WORLD_RENDER_NML, WORLD_RENDER_NML);
        glBegin(GL_QUADS);
        // front
        glTexCoord2f(u28, v40);
        glVertex3f(0.25f, 0, 0.125f);
        glTexCoord2f(u28, v52);
        glVertex3f(0.25f, -0.75f, 0.125f);
        glTexCoord2f(u32, v52);
        glVertex3f(0.5f, -0.75f, 0.125f);
        glTexCoord2f(u32, v40);
        glVertex3f(0.5f, 0, 0.125f);
        // left
        glTexCoord2f(u24, v40);
        glVertex3f(0.25f, 0, -0.125f);
        glTexCoord2f(u24, v52);
        glVertex3f(0.25f, -0.75f, -0.125f);
        glTexCoord2f(u28, v52);
        glVertex3f(0.25f, -0.75f, 0.125f);
        glTexCoord2f(u28, v40);
        glVertex3f(0.25f, 0, 0.125f);
        // right
        glTexCoord2f(u32, v40);
        glVertex3f(0.5f, 0, 0.125f);
        glTexCoord2f(u32, v52);
        glVertex3f(0.5f, -0.75f, 0.125f);
        glTexCoord2f(u36, v52);
        glVertex3f(0.5f, -0.75f, -0.125f);
        glTexCoord2f(u36, v40);
        glVertex3f(0.5f, 0, -0.125f);
        glEnd();
        glPopMatrix();

        // right leg
        glPushMatrix();
        glRotatef(yRot, 0, 1, 0);
        glScalef(WORLD_RENDER_NML, WORLD_RENDER_NML, WORLD_RENDER_NML);
        glBegin(GL_QUADS);
        // front
        glTexCoord2f(u4, v16);
        glVertex3f(-0.25f, 0.75f, 0.125f);
        glTexCoord2f(u4, v28);
        glVertex3f(-0.25f, 0, 0.125f);
        glTexCoord2f(u8, v28);
        glVertex3f(0, 0, 0.125f);
        glTexCoord2f(u8, v16);
        glVertex3f(0, 0.75f, 0.125f);
        // left
        glTexCoord2f(u0, v16);
        glVertex3f(-0.25f, 0.75f, -0.125f);
        glTexCoord2f(u0, v28);
        glVertex3f(-0.25f, 0, -0.125f);
        glTexCoord2f(u4, v28);
        glVertex3f(-0.25f, 0, 0.125f);
        glTexCoord2f(u4, v16);
        glVertex3f(-0.25f, 0.75f, 0.125f);
        // right
        glTexCoord2f(u8, v16);
        glVertex3f(0, 0.75f, 0.125f);
        glTexCoord2f(u8, v28);
        glVertex3f(0, 0, 0.125f);
        glTexCoord2f(u16, v28);
        glVertex3f(0, 0, -0.125f);
        glTexCoord2f(u16, v16);
        glVertex3f(0, 0.75f, -0.125f);
        glEnd();
        glPopMatrix();

        // left leg
        glPushMatrix();
        glRotatef(yRot, 0, 1, 0);
        glScalef(WORLD_RENDER_NML, WORLD_RENDER_NML, WORLD_RENDER_NML);
        glBegin(GL_QUADS);
        // front
        glTexCoord2f(u16, v40);
        glVertex3f(0, 0.75f, 0.125f);
        glTexCoord2f(u16, v52);
        glVertex3f(0, 0, 0.125f);
        glTexCoord2f(u20, v52);
        glVertex3f(0.25f, 0, 0.125f);
        glTexCoord2f(u20, v40);
        glVertex3f(0.25f, 0.75f, 0.125f);
        // left
        glTexCoord2f(u12, v40);
        glVertex3f(0, 0.75f, -0.125f);
        glTexCoord2f(u12, v52);
        glVertex3f(0, 0, -0.125f);
        glTexCoord2f(u16, v52);
        glVertex3f(0, 0, 0.125f);
        glTexCoord2f(u16, v40);
        glVertex3f(0, 0.75f, 0.125f);
        // right
        glTexCoord2f(u20, v40);
        glVertex3f(0.25f, 0.75f, 0.125f);
        glTexCoord2f(u20, v52);
        glVertex3f(0.25f, 0, 0.125f);
        glTexCoord2f(u24, v52);
        glVertex3f(0.25f, 0, -0.125f);
        glTexCoord2f(u24, v40);
        glVertex3f(0.25f, 0.75f, -0.125f);
        glEnd();
        glPopMatrix();

        // handled block
        int bid = hand.getId();
        float bu0 = BlockRenderer.u0(bid);
        float bv0 = BlockRenderer.v0(bid);
        float bu1 = BlockRenderer.u1(bid);
        float bv1 = BlockRenderer.v1(bid);
        float bts = (float) BLOCK_TEX_SIZE;
        float hbts = (float) BLOCK_TEX_SIZE * 0.5f;
        glBindTexture(GL_TEXTURE_2D, WorldRenderer.blocks);
        glPushMatrix();
        glTranslatef(0, 48, 0);
        glRotatef(yRot, 0, 1, 0);
        glBegin(GL_QUADS);
        glTexCoord2f(bu0, bv0);
        glVertex3f(-12 - hbts, -24, 5);
        glTexCoord2f(bu0, bv1);
        glVertex3f(-12 - hbts, (-bts) - 24, 5);
        glTexCoord2f(bu1, bv1);
        glVertex3f(-12 + hbts, (-bts) - 24, 5);
        glTexCoord2f(bu1, bv0);
        glVertex3f(-12 + hbts, -24, 5);
        glEnd();
        glPopMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);

        glPopMatrix();
    }
}
