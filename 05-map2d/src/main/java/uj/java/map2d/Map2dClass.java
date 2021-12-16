package uj.java.map2d;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Map2dClass<R, C, V> implements Map2D<R, C, V> {

    private final Map<Cell<R, C>, V> map = new HashMap<>();

    @Override
    public V put(R rowKey, C columnKey, V value) {
        if (rowKey != null && columnKey != null) {
            return map.put(new Cell<>(rowKey, columnKey), value);
        }
        throw new NullPointerException();
    }

    @Override
    public V get(R rowKey, C columnKey) {
        return map.get(new Cell<>(rowKey, columnKey));
    }

    @Override
    public V getOrDefault(R rowKey, C columnKey, V defaultValue) {
        return map.getOrDefault(new Cell<>(rowKey, columnKey), defaultValue);
    }

    @Override
    public V remove(R rowKey, C columnKey) {
        return map.remove(new Cell<>(rowKey, columnKey));
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean nonEmpty() {
        return !isEmpty();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Map<C, V> rowView(R rowKey) {
        Map<C, V> result = new HashMap<>();
        for (var entry : map.entrySet()) {
            if (entry.getKey().r().equals(rowKey)) {
                result.put(entry.getKey().c(), entry.getValue());
            }
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Map<R, V> columnView(C columnKey) {
        Map<R, V> result = new HashMap<>();

        for (var entry : map.entrySet()) {
            if (entry.getKey().c().equals(columnKey)) {
                result.put(entry.getKey().r(), entry.getValue());
            }
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public boolean hasValue(V value) {
        return map.containsValue(value);
    }

    @Override
    public boolean hasKey(R rowKey, C columnKey) {
        return map.containsKey(new Cell<>(rowKey, columnKey));
    }

    @Override
    public boolean hasRow(R rowKey) {
        for (Cell<R, C> cell : map.keySet()) {
            if (cell.r().equals(rowKey)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasColumn(C columnKey) {
        for (Cell<R, C> cell : map.keySet()) {
            if (cell.c().equals(columnKey)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Map<R, Map<C, V>> rowMapView() {
        Map<R, Map<C, V>> result = new HashMap<>();
        for (var entry : map.entrySet()) {
            if (result.containsKey(entry.getKey().r())) {
                Map<C, V> tmp = result.get(entry.getKey().r());
                tmp.put(entry.getKey().c(), entry.getValue());
            } else {
                Map<C, V> tmp = new HashMap<>();
                tmp.put(entry.getKey().c(), entry.getValue());
                result.put(entry.getKey().r(), tmp);
            }
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Map<C, Map<R, V>> columnMapView() {
        Map<C, Map<R, V>> result = new HashMap<>();
        for (var entry : map.entrySet()) {
            if (result.containsKey(entry.getKey().c())) {
                Map<R, V> tmp = result.get(entry.getKey().c());
                tmp.put(entry.getKey().r(), entry.getValue());
            } else {
                Map<R, V> tmp = new HashMap<>();
                tmp.put(entry.getKey().r(), entry.getValue());
                result.put(entry.getKey().c(), tmp);
            }
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Map2D<R, C, V> fillMapFromRow(Map<? super C, ? super V> target, R rowKey) {
        for (var entry : map.entrySet()) {
            if (entry.getKey().r().equals(rowKey)) {
                target.put(entry.getKey().c(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public Map2D<R, C, V> fillMapFromColumn(Map<? super R, ? super V> target, C columnKey) {
        for (var entry : map.entrySet()) {
            if (entry.getKey().c().equals(columnKey)) {
                target.put(entry.getKey().r(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map2D<R, C, V> putAll(Map2D<? extends R, ? extends C, ? extends V> source) {
        for (var entry : ((Map2dClass<? extends R, ? extends C, ? extends V>) source).map.entrySet()) {
            put(entry.getKey().r(), entry.getKey().c(), entry.getValue());
        }
        return this;
    }

    @Override
    public Map2D<R, C, V> putAllToRow(Map<? extends C, ? extends V> source, R rowKey) {
        for (var entry : source.entrySet()) {
            put(rowKey, entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public Map2D<R, C, V> putAllToColumn(Map<? extends R, ? extends V> source, C columnKey) {
        for (var entry : source.entrySet()) {
            put(entry.getKey(), columnKey, entry.getValue());
        }
        return this;
    }

    @Override
    public <R2, C2, V2> Map2D<R2, C2, V2> copyWithConversion(
            Function<? super R, ? extends R2> rowFunction,
            Function<? super C, ? extends C2> columnFunction,
            Function<? super V, ? extends V2> valueFunction) {

        Map2D<R2, C2, V2> result = new Map2dClass<>();
        for (var entry : map.entrySet()) {
            result.put(rowFunction.apply(entry.getKey().r()), columnFunction.apply(entry.getKey().c()), valueFunction.apply(entry.getValue()));
        }
        return result;
    }
}