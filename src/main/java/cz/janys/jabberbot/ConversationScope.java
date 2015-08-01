package cz.janys.jabberbot;

import cz.janys.jabberbot.task.ClearConversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Martin Janys
 */
public class ConversationScope<K, V> implements Map<K, V> {

    private final Map<K, V> map;
    private final ConversationConfiguration configuration;
    private Future<?> clearing;

    private ConversationScope(Map<K, V> map, ConversationConfiguration configuration) {
        this.map = map;
        this.configuration = configuration;
    }

    public static <K, V> ConversationScope<K, V> create(ConversationConfiguration conversationConfiguration) {
        Map<K, V> map = Collections.synchronizedMap(new HashMap<K, V>());
        return new ConversationScope<K, V>(map, conversationConfiguration);
    }
    public static <K, V> ConversationScope<K, V> create() {
        Map<K, V> map = Collections.synchronizedMap(new HashMap<K, V>());
        return new ConversationScope<K, V>(map, null);
    }

    public V get(K key, V defaultValue) {
        V value = containsKey(key) ? map.get(key) : defaultValue;
        put(key, value);
        return value;
    }

    public void refresh() {
        if (configuration != null) {
            if (clearing != null) {
                clearing.cancel(true);
            }
            Runnable task = new ClearConversation<K, V>(this);
            clearing = Executors.newSingleThreadScheduledExecutor().schedule(task, configuration.getDelay(), TimeUnit.SECONDS);
        }
    }


    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public V get(Object key) {
        return map.get(key);
    }

    public V put(K key, V value) {
        return map.put(key, value);
    }

    public V remove(Object key) {
        return map.remove(key);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    public void clear() {
        map.clear();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public boolean equals(Object o) {
        return map.equals(o);
    }

    public int hashCode() {
        return map.hashCode();
    }

    public V getOrDefault(Object key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        map.replaceAll(function);
    }

    public V putIfAbsent(K key, V value) {
        return map.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    public boolean replace(K key, V oldValue, V newValue) {
        return map.replace(key, oldValue, newValue);
    }

    public V replace(K key, V value) {
        return map.replace(key, value);
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }

    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return map.computeIfPresent(key, remappingFunction);
    }

    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return map.compute(key, remappingFunction);
    }

    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return map.merge(key, value, remappingFunction);
    }
}
