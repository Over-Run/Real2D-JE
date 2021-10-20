package org.overrun.real2d.world.block;

import org.jetbrains.annotations.Nullable;
import org.overrun.real2d.util.reg.Registry;
import org.overrun.real2d.world.phys.AABBox;

import java.io.Serializable;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Block implements Serializable {
    public boolean isOpaque() {
        return true;
    }

    public @Nullable AABBox getOutline() {
        return AABBox.FULL_CUBE;
    }

    public @Nullable AABBox getCollision() {
        return getOutline();
    }

    public final int getId() {
        return Registry.BLOCK.get(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Block)) {
            return false;
        }
        return getId() == ((Block) obj).getId();
    }
}
