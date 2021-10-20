package org.overrun.real2d.util.reg;

import org.jetbrains.annotations.NotNull;
import org.overrun.real2d.world.block.Block;
import org.overrun.real2d.world.block.Blocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Registry<T> implements Iterable<T> {
    public static final Registry<Block> BLOCK = new Registry<>(Blocks.AIR);

    private final T defaultEntry;
    private final Map<String, T> id2entry = new HashMap<>();
    private final Map<T, String> entry2id = new HashMap<>();
    private final Map<Integer, T> rawId2entry = new LinkedHashMap<>();
    private final Map<T, Integer> entry2rawId = new HashMap<>();
    private int nextId = 0;

    public Registry(T defaultEntry) {
        this.defaultEntry = defaultEntry;
    }

    public Registry() {
        this(null);
    }

    public static <T, R extends T> R register(Registry<T> registry, String id, R entry) {
        return registry.add(id, entry);
    }

    public <R extends T> R set(int rawId, String id, R entry) {
        if (rawId > nextId) {
            nextId = rawId;
        }
        id2entry.put(id, entry);
        entry2id.put(entry, id);
        rawId2entry.put(rawId, entry);
        entry2rawId.put(entry, rawId);
        return entry;
    }

    public <R extends T> R add(String id, R entry) {
        set(nextId, id, entry);
        ++nextId;
        return entry;
    }

    public int get(T entry) {
        return entry2rawId.getOrDefault(entry,
                0);
    }

    public String getSID(T entry) {
        return entry2id.getOrDefault(entry,
                entry2id.get(defaultEntry));
    }

    public String getSID(int rawId) {
        if (rawId2entry.containsKey(rawId)) {
            return getSID(rawId2entry.get(rawId));
        }
        return entry2id.get(defaultEntry);
    }

    public T get(String id) {
        return id2entry.getOrDefault(id, defaultEntry);
    }

    public T get(int rawId) {
        return rawId2entry.getOrDefault(rawId, defaultEntry);
    }

    public Map<T, Integer> rawIds() {
        return entry2rawId;
    }

    public int size() {
        return id2entry.size();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return rawId2entry.values().iterator();
    }
}
