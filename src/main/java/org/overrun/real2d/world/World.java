package org.overrun.real2d.world;

import org.overrun.real2d.client.Real2D;
import org.overrun.real2d.client.Window;
import org.overrun.real2d.world.block.Block;
import org.overrun.real2d.world.entity.Player;
import org.overrun.real2d.world.phys.AABBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.overrun.real2d.client.Window.*;
import static org.overrun.real2d.world.block.Blocks.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class World implements Serializable {
    public static final int WORLD_VER = 1;
    private static final int serialVersionUID = WORLD_VER;
    public transient HitResult hitResult;
    private transient final List<WorldListener> listeners = new ArrayList<>();
    public final Player player = new Player(this);
    public transient final int width;
    public transient final int height;
    public transient final int depth;
    private final Block[] world;
    public final int version;
    private transient final int[] lights;

    public World(int w, int h, int d) {
        width = w;
        height = h;
        depth = d;
        version = WORLD_VER;
        int sz = width * height * depth;
        world = new Block[sz];
        lights = new int[sz];
        Arrays.fill(lights, 15);
    }

    public int getIndex(int x, int y, int z) {
        // x + y * width + z * width * height
        return x + (y + z * height) * width;
    }

    public boolean isInBorder(int x, int y, int z) {
        return x >= 0 && x < width
                && y >= 0 && y < height
                && z >= 0 && z < depth;
    }

    public void create() {
        if (!load()) {
            for (int z = 0; z < depth; ++z) {
                for (int x = 0; x < width; ++x) {
                    for (int y = 0; y < height; ++y) {
                        Block b = AIR;
                        if (y < 3) {
                            b = STONE;
                        } else if (y == 3) {
                            b = GRASS_BLOCK;
                        }
                        world[getIndex(x, y, z)] = b;
                    }
                }
            }
            save();
        }
        for (int z = 0; z < depth; ++z) {
            for (int x = 0; x < width; ++x) {
                calcLights(x, z);
            }
        }
    }

    public void tick() {
        if (hitResult != null) {
            Window window = Real2D.INSTANCE.window;
            Block b = hitResult.block;
            int x = hitResult.x;
            int y = hitResult.y;
            int z = hitResult.z;
            if (b == AIR) {
                if (window.isMouseDown(MBR)
                        && player.hand != AIR) {
                    setBlock(x, y, z, player.hand);
                }
            } else {
                if (window.isMouseDown(MBL)) {
                    setBlock(x, y, z, AIR);
                } else if (window.isMouseDown(MBM)) {
                    player.hand = b;
                }
            }
        }
        player.tick();
    }

    public void addListener(WorldListener listener) {
        listeners.add(listener);
    }

    public void calcLights(int x, int z) {
        int light = 15;
        for (int y = height - 1; y >= 0; y--) {
            lights[getIndex(x, y, z)] = light;
            if (light > 0 && getBlock(x, y, z).isOpaque()) {
                --light;
            }
        }
    }

    public int getLight(int x, int y, int z) {
        if (isInBorder(x, y, z)) {
            return lights[getIndex(x, y, z)];
        }
        return 0;
    }

    public List<AABBox> getCubes(AABBox box) {
        List<AABBox> cubes = new ArrayList<>();
        int x0 = (int) box.start_x;
        int x1 = (int) (box.end_x + 1);
        int y0 = (int) box.start_y;
        int y1 = (int) (box.end_y + 1);
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 > width) {
            x1 = width;
        }
        if (y1 > height) {
            y1 = height;
        }
        for (int x = x0; x < x1; ++x) {
            for (int y = y0; y < y1; ++y) {
                AABBox cube = getBlock(x, y, 1).getCollision();
                if (cube != null) {
                    AABBox b = new AABBox();
                    cube.move((float) x, (float) y, 1, b);
                    cubes.add(b);
                }
            }
        }
        return cubes;
    }

    public Block getBlock(int x, int y, int z) {
        if (isInBorder(x, y, z)) {
            return world[getIndex(x, y, z)];
        }
        return AIR;
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (isInBorder(x, y, z)) {
            int i = getIndex(x, y, z);
            if (world[i] == block) {
                return;
            }
            world[i] = block;
            calcLights(x, z);
            for (var l : listeners) {
                l.blockChanged(x, y, z);
            }
        }
    }

    public void save() {
        new File("saves/").mkdirs();
        try (var os = new FileOutputStream("saves/level.dat");
             var oos = new ObjectOutputStream(os)) {
            oos.writeObject(this);
        } catch (IOException e) {
            new IOException("Couldn't open level.dat", e).printStackTrace();
        }
    }

    public boolean load() {
        new File("saves/").mkdirs();
        try (var is = new FileInputStream("saves/level.dat");
             var ois = new ObjectInputStream(is)) {
            World world = (World) ois.readObject();
            player.x = world.player.x;
            player.y = world.player.y;
            player.z = world.player.z;
            System.arraycopy(world.world, 0, this.world, 0, world.world.length);
        } catch (IOException | ClassNotFoundException ignore) {
        }
        return false;
    }
}
