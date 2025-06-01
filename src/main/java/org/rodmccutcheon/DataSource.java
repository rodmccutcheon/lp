package org.rodmccutcheon;

import java.util.stream.Stream;

public interface DataSource<T> extends AutoCloseable {
    Stream<T> stream();
}
