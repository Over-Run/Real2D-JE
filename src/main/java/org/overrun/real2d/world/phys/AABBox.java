package org.overrun.real2d.world.phys;

/**
 * @author squid233
 * @since 0.1.0
 */
public class AABBox {
    public static final AABBox FULL_CUBE = new AABBox(0, 0, 0, 1, 1, 1);
    public float start_x;
    public float start_y;
    public float start_z;
    public float end_x;
    public float end_y;
    public float end_z;

    public AABBox(float start_x,
                  float start_y,
                  float start_z,
                  float end_x,
                  float end_y,
                  float end_z) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.start_z = start_z;
        this.end_x = end_x;
        this.end_y = end_y;
        this.end_z = end_z;
    }

    public AABBox(AABBox b) {
        start_x = b.start_x;
        start_y = b.start_y;
        start_z = b.start_z;
        end_x = b.end_x;
        end_y = b.end_y;
        end_z = b.end_z;
    }

    public AABBox() {
    }

    public void set(AABBox b) {
        start_x = b.start_x;
        start_y = b.start_y;
        start_z = b.start_z;
        end_x = b.end_x;
        end_y = b.end_y;
        end_z = b.end_z;
    }

    public void set(float start_x,
                    float start_y,
                    float start_z,
                    float end_x,
                    float end_y,
                    float end_z) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.start_z = start_z;
        this.end_x = end_x;
        this.end_y = end_y;
        this.end_z = end_z;
    }

    public boolean move(float xoffset,
                        float yoffset,
                        float zoffset,
                        AABBox dst) {
        if (dst != null) {
            dst.start_x = start_x + xoffset;
            dst.start_y = start_y + yoffset;
            dst.start_z = start_z + zoffset;
            dst.end_x = end_x + xoffset;
            dst.end_y = end_y + yoffset;
            dst.end_z = end_z + zoffset;
            return true;
        }
        return false;
    }

    public boolean move(float xoffset,
                        float yoffset,
                        float zoffset) {
        return move(xoffset, yoffset, zoffset, this);
    }

    public AABBox expand(float xoffset,
                         float yoffset,
                         float zoffset) {
        float fsx = start_x;
        float fsy = start_y;
        float fsz = start_z;
        float fex = end_x;
        float fey = end_y;
        float fez = end_z;
        if (xoffset < 0) {
            fsx += xoffset;
        }
        if (xoffset > 0) {
            fex += xoffset;
        }
        if (yoffset < 0) {
            fsy += yoffset;
        }
        if (yoffset > 0) {
            fey += yoffset;
        }
        if (zoffset < 0) {
            fsz += zoffset;
        }
        if (zoffset > 0) {
            fez += zoffset;
        }
        return new AABBox(fsx, fsy, fsz, fex, fey, fez);
    }

    public boolean isXCollide(AABBox b, float axs) {
        if (axs < 0) {
            return b.start_x <= end_x && b.end_x > start_x;
        }
        if (axs > 0) {
            return b.end_x >= start_x && b.start_x < end_x;
        }
        return false;
    }

    public boolean isYCollide(AABBox b, float axs) {
        if (axs < 0) {
            return b.start_y <= end_y && b.end_y > end_y;
        }
        if (axs > 0) {
            return b.end_y >= start_y && b.start_y < start_y;
        }
        return false;
    }

    public boolean isZCollide(AABBox b, float axs) {
        if (axs < 0) {
            return b.start_z <= end_z && b.end_z > end_z;
        }
        if (axs > 0) {
            return b.end_z >= start_z && b.start_z < start_z;
        }
        return false;
    }

    /**
     * Check b is intersected to self.
     *
     * @param b The other box.
     * @return Is b intersect to self
     */
    public boolean isIntersect(AABBox b) {
        return b.start_x < end_x && b.end_x > start_x
                && b.start_y < end_y && b.end_y > start_y
                && b.start_z < end_z && b.end_z > start_z;
    }

    public boolean isIntersect(float x, float y) {
        return x >= start_x && x <= end_x
                && y >= start_y && y <= end_y;
    }
}
