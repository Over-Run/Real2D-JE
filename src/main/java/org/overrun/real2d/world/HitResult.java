package org.overrun.real2d.world;

import org.overrun.real2d.world.block.Block;

/**
 * @author squid233
 * @since 0.1.0
 */
public class HitResult {
    public int x, y, z;
    public Block block;

    public HitResult(int x, int y, int z, Block block) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
    }
}
