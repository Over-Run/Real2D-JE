package org.overrun.real2d.world;

/**
 * @author squid233
 * @since 0.1.0
 */
public interface WorldListener {
    void blockChanged(int x, int y, int z);

    void lightColChanged(int x, int z);
}
