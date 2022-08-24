package springboot.catchshop.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import springboot.catchshop.exception.CannotConvertNestedStructureException;

public class NestedConvertHelper<K, E, D> {
    private List<E> entities;
    private Function<E, D> toDto;//entity -> dto
    private Function<E, E> getParent;//parent
    private Function<E, K> getKey;//id
    private Function<D, List<D>> getChildren;//dtoList

    public static <K, E, D> NestedConvertHelper newInstance(List<E> entities, Function<E, D> toDto, Function<E, E> getParent, Function<E, K> getKey, Function<D, List<D>> getChildren) {
        return new NestedConvertHelper<K, E, D>(entities, toDto, getParent, getKey, getChildren);
    }

    private NestedConvertHelper(List<E> entities, Function<E, D> toDto, Function<E, E> getParent, Function<E, K> getKey, Function<D, List<D>> getChildren) {
        this.entities = entities;
        this.toDto = toDto;
        this.getParent = getParent;
        this.getKey = getKey;
        this.getChildren = getChildren;
    }

    public List<D> convert() {
        try {
            return convertInternal();
        } catch (NullPointerException e) {
            throw new CannotConvertNestedStructureException(e.getMessage());
        }
    }

    private List<D> convertInternal() {
        Map<K, D> map = new HashMap<>();
        List<D> roots = new ArrayList<>();

        for (E e : entities) {
            D dto = toDto(e); //entity -> dto
            map.put(getKey(e), dto);
            if (hasParent(e)) {
                E parent = getParent(e);
                K parentKey = getKey(parent);
                D parentDto = map.get(parentKey);//해당 dto 가져오기
                getChildren(parentDto).add(dto);
            } else {
                roots.add(dto);
            }
        }
        return roots;
    }

    private boolean hasParent(E e) {
        return getParent(e) != null;
    }

    private E getParent(E e) {
        return getParent.apply(e);
    }

    private D toDto(E e) {
        return toDto.apply(e);
    }

    private K getKey(E e) {
        return getKey.apply(e);
    }

    private List<D> getChildren(D d) {
        return getChildren.apply(d);
    }
}
