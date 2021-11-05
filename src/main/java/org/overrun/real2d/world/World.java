package org.overrun.real2d.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.overrun.real2d.client.Real2D;
import org.overrun.real2d.client.Window;
import org.overrun.real2d.util.reg.Registry;
import org.overrun.real2d.world.block.Block;
import org.overrun.real2d.world.entity.Player;
import org.overrun.real2d.world.phys.AABBox;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.overrun.real2d.client.Window.*;
import static org.overrun.real2d.world.block.Blocks.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class World {
    public static final int WORLD_VER = 1;
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(World.class, new World.Serializer())
        .create();
    public HitResult hitResult;
    private final List<WorldListener> listeners = new ArrayList<>();
    public final Player player = new Player(this);
    public final int width = 48;
    public final int height = 32;
    public final int depth = 2;
    private final Block[] blocks;
    public final int version = WORLD_VER;
    private final int[] lights;

    public World() {
        int sz = width * height * depth;
        blocks = new Block[sz];
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
                        blocks[getIndex(x, y, z)] = b;
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
                Block block = player.getItemInHand(Hand.MAIN_HAND);
                if (window.isMouseDown(MBR)
                    && block != AIR) {
                    setBlock(x, y, z, block);
                }
            } else {
                if (window.isMouseDown(MBL)) {
                    setBlock(x, y, z, AIR);
                } else if (window.isMouseDown(MBM)) {
                    player.setItemInHand(Hand.MAIN_HAND, b);
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
            return blocks[getIndex(x, y, z)];
        }
        return AIR;
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (isInBorder(x, y, z)) {
            int i = getIndex(x, y, z);
            if (blocks[i] == block) {
                return;
            }
            blocks[i] = block;
            calcLights(x, z);
            for (var l : listeners) {
                l.blockChanged(x, y, z);
            }
        }
    }

    public static class Serializer extends TypeAdapter<World> {
        @Override
        public void write(JsonWriter out, World value)
            throws IOException {
            out.beginObject()
                .name("version").value(value.version);
            value.player.serialize(out.name("player"));

            out.name("idMap")
                .beginArray();
            for (var b : Registry.BLOCK) {
                out.value(b.getId())
                    .value(b.getSId());
            }
            out.endArray();

            out.name("blocks").beginArray();
            for (var b : value.blocks) {
                out.value(b.getId());
            }
            out.endArray();

            out.endObject();
        }

        @Override
        public World read(JsonReader in)
            throws IOException {
            World world = new World();
            var idMap = new HashMap<Integer, String>();
            in.beginObject();
            in.nextName();
            in.nextInt();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "player":
                        world.player.deserialize(in);
                        break;
                    case "idMap":
                        in.beginArray();
                        while (in.hasNext()) {
                            idMap.put(in.nextInt(), in.nextString());
                        }
                        in.endArray();
                        break;
                    case "blocks":
                        in.beginArray();
                        for (int i = 0; i < world.blocks.length; i++) {
                            world.blocks[i] =
                                Registry.BLOCK.get(idMap.get(in.nextInt()));
                        }
                        in.endArray();
                        break;
                }
            }
            in.endObject();
            return world;
        }
    }

    public void save() {
        new File("saves/").mkdirs();
        try (var os = new FileOutputStream("saves/level.dat");
             var gos = new GZIPOutputStream(os);
             var osw = new OutputStreamWriter(gos)) {
            gson.toJson(this, osw);
        } catch (IOException e) {
            new IOException("Couldn't write level.dat", e).printStackTrace();
        }
    }

    public boolean load() {
        new File("saves/").mkdirs();
        if (!new File("saves/level.dat").exists()) {
            return false;
        }
        try (var is = new FileInputStream("saves/level.dat");
             var gis = new GZIPInputStream(is);
             var isr = new InputStreamReader(gis)) {
            World world = gson.fromJson(isr, World.class);
            player.load(world.player);
            System.arraycopy(world.blocks,
                0,
                blocks,
                0,
                world.blocks.length);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
