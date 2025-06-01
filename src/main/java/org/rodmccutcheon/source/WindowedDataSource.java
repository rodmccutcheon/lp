package org.rodmccutcheon.source;

import java.util.List;
import java.util.stream.Stream;

public interface WindowedDataSource<T> {
    Stream<List<T>> stream();
}
