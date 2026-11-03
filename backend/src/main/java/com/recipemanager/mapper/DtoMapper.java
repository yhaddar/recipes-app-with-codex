package com.recipemanager.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface DtoMapper<S, T> {

    T toResponse(S source);

    default List<T> toResponses(Collection<S> sources) {
        Objects.requireNonNull(sources, "sources must not be null");
        return sources.stream()
                .map(this::toResponse)
                .toList();
    }
}
