package org.overrun.real2d.world.block;

import org.jetbrains.annotations.Nullable;
import org.overrun.real2d.world.phys.AABBox;

/**
 * @author squid233
 * @since 0.1.0
 */
public class AirBlock extends Block {
    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public @Nullable AABBox getCollision() {
        return null;
    }
}
