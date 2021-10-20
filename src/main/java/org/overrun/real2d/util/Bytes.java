package org.overrun.real2d.util;

import java.nio.ByteBuffer;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Bytes {
    public static byte[] getBytes(float f) {
        return ByteBuffer.allocate(Float.BYTES).putFloat(f).array();
    }

    public static byte[] getBytes(int i) {
        return ByteBuffer.allocate(Integer.BYTES).putFloat(i).array();
    }

    public static float toFloat(byte[] bytes) {
        return ByteBuffer.allocate(Float.BYTES).put(bytes).flip().getFloat();
    }

    public static int toInt(byte[] bytes) {
        return ByteBuffer.allocate(Integer.BYTES).put(bytes).flip().getInt();
    }
}
