package org.overrun.real2d.world.block;

import org.overrun.real2d.util.reg.Registry;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Blocks {
    public static final Block AIR = reg(0, "air_block", new AirBlock());
    public static final Block GRASS_BLOCK = reg(1, "grass_block", new Block());
    public static final Block STONE = reg(2, "stone", new Block());

    public static void init() {
    }

    private static <T extends Block> T reg(int rawId, String id, T block) {
        return Registry.BLOCK.set(rawId, id, block);
    }
}
