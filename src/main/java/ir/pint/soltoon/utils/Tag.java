package ir.pint.soltoon.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class Tag<K> {
    private HashSet<K> tags = new HashSet<>();

    public Tag(K... tags) {
        this.tags.addAll(Arrays.asList(tags));
    }

    public Tag() {
    }

    public HashSet<K> getTags() {
        return tags;
    }

    public void setTags(HashSet<K> tags) {
        this.tags = tags;
    }
}
