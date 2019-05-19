package org.igye.sqlexercises.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExercisesUtils {

    public static <A,B> Set<B> map(Set<A> collection, Function<A,B> mapper) {
        return collection.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <A,B> List<B> map(List<A> collection, Function<A,B> mapper) {
        return collection.stream().map(mapper).collect(Collectors.toList());
    }

    public static <A,B> List<B> map(A[] array, Function<A,B> mapper) {
        List<B> res = new ArrayList<>();
        for (A a : array) {
            res.add(mapper.apply(a));
        }
        return res;
    }

    public static <A,B> Set<B> mapToSet(List<A> collection, Function<A,B> mapper) {
        return collection.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <T> Set<T> filter(Set<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toSet());
    }

    public static <T> List<T> filter(List<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T> Set<T> filterToSet(List<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toSet());
    }

    public static <E,K,V> Map<K,V> toMap(Collection<E> collection, Function<E,K> keyExtr, Function<E,V> valueExtr) {
        return collection.stream().collect(Collectors.toMap(keyExtr, valueExtr));
    }

    public static <E,K> Map<K,E> toMap(Collection<E> collection, Function<E,K> keyExtr) {
        return toMap(collection, keyExtr, Function.identity());
    }

    public static <E> List<E> listF(E... elems) {
        return ImmutableList.copyOf(elems);
    }

    public static <E> Set<E> setF(E... elems) {
        return ImmutableSet.copyOf(elems);
    }

    public static <K,V> Map<K,V> mapF(K k1, V v1) {
        Map<K, V> resp = new HashMap<>();
        resp.put(k1, v1);
        return resp;
    }

    public static <K,V> Map<K,V> mapF(K k1, V v1, K k2, V v2) {
        Map<K, V> resp = mapF(k2,v2);
        resp.put(k1, v1);
        return resp;
    }

    public static <K,V> Map<K,V> mapF(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> resp = mapF(k2,v2,k3,v3);
        resp.put(k1, v1);
        return resp;
    }

    public static String readString(String path) throws IOException {
        return IOUtils.toString(
                ExercisesUtils.class.getClassLoader().getResourceAsStream(path),
                StandardCharsets.UTF_8
        );
    }

    public static List<String> readLines(String path) throws IOException {
        return IOUtils.readLines(
                ExercisesUtils.class.getClassLoader().getResourceAsStream(path),
                StandardCharsets.UTF_8
        );
    }

}
