package org.overrun.real2d.client.render.entity;

import org.overrun.glutils.Textures;
import org.overrun.glutils.wnd.Framebuffer;
import org.overrun.real2d.client.Real2D;
import org.overrun.real2d.client.Window;
import org.overrun.real2d.client.render.world.BlockRenderer;
import org.overrun.real2d.client.render.world.WorldRenderer;
import org.overrun.real2d.world.Hand;
import org.overrun.real2d.world.entity.Player;

import static org.lwjgl.opengl.GL11.*;
import static org.overrun.real2d.client.render.world.BlockRenderer.BLOCK_TEX_SIZE;
import static org.overrun.real2d.client.render.world.WorldRenderer.WORLD_RENDER_NML;

/**
 * @author squid233
 * @since 0.1.0
 */
public class PlayerRenderer {
    public static final String TEXTURE = "assets.real2d/textures/entity/player.png";
    public static final float TEX_PLAYER_W = 48.0f;
    public static final float TEX_PLAYER_H = 64.0f;
    private final Player player;
    private float headXRot = 0.0f;
    private float yRot = 0.0f;

    public PlayerRenderer(Player player) {
        this.player = player;
    }

    public void tick() {
        Window window = Real2D.INSTANCE.window;
        Framebuffer fb = Real2D.INSTANCE.framebuffer;
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
        int id;
        try {
            id = Textures.loadAWT(PlayerRenderer.class.getClassLoader(),
                TEXTURE,
                GL_NEAREST);
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

        // item in hand
        int bid = player.getItemInHand(Hand.MAIN_HAND).getId();
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
