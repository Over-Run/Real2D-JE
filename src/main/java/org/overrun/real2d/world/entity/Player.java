package org.overrun.real2d.world.entity;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.overrun.real2d.client.Real2D;
import org.overrun.real2d.client.Window;
import org.overrun.real2d.util.reg.Registry;
import org.overrun.real2d.world.Hand;
import org.overrun.real2d.world.World;
import org.overrun.real2d.world.block.Block;
import org.overrun.real2d.world.block.Blocks;
import org.overrun.real2d.world.phys.AABBox;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Player {
    public final float bbWidth;
    public final float bbHeight;
    public World world;
    public float x, y, z;
    public float prevX, prevY;
    public AABBox bb;
    public boolean onGround;
    private final Block[] handItems = {Blocks.AIR, Blocks.GRASS_BLOCK};

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

    public void serialize(JsonWriter out)
        throws IOException {
        out.beginObject()
            .name("x").value(x)
            .name("y").value(y)
            .name("z").value(z)
            .name("onGround").value(onGround)
            .name("handItems");

        out.beginArray()
            .value(handItems[0].getSId())
            .value(handItems[1].getSId());
        out.endArray();

        out.endObject();
    }

    public void deserialize(JsonReader in)
        throws IOException {
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "x":
                    x = (float) in.nextDouble();
                    break;
                case "y":
                    y = (float) in.nextDouble();
                    break;
                case "z":
                    z = (float) in.nextDouble();
                    break;
                case "onGround":
                    onGround = in.nextBoolean();
                    break;
                case "handItems":
                    in.beginArray();
                    for (int i = 0; i < 2; i++) {
                        handItems[i] =
                            Registry.BLOCK.get(in.nextString());
                    }
                    in.endArray();
                    break;
            }
        }
        in.endObject();
    }

    public void load(Player p) {
        x = p.x;
        y = p.y;
        z = p.z;
        setItemInHand(Hand.OFF_HAND, p.getItemInHand(Hand.OFF_HAND));
        setItemInHand(Hand.MAIN_HAND, p.getItemInHand(Hand.MAIN_HAND));
    }

    public Block getItemInHand(Hand hand) {
        return handItems[hand.ordinal()];
    }

    public void setItemInHand(Hand hand, Block item) {
        handItems[hand.ordinal()] = item;
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
    }
}
