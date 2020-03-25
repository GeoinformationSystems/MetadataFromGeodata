import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NestedMap<K, V> {
    private final HashMap<K, NestedMap<K, V>> child;
    private V value;

    public NestedMap() {
        child = new HashMap<>();
        value = null;
    }

    public boolean hasChild(K k) {
        return this.child.containsKey(k);
    }

    public NestedMap<K, V> getChild(K k) {
        return this.child.get(k);
    }

    public void makeChild(K k) {
        this.child.put(k, new NestedMap<>());
    }

    public V getValue() {
        return value;
    }

    public void setKeyValue(K k, NestedMap<K, V> v) {
        this.child.put(k, v);
    }

    public void setValue(V v) {
        value = v;
    }

    public void removeChild(K k) {
        this.child.remove(k);
    }

    public Set<K> keySetChild() {
        return this.child.keySet();
    }

    public Set<Map.Entry<K, NestedMap<K, V>>> entrySetChild() {
        return this.child.entrySet();
    }
}