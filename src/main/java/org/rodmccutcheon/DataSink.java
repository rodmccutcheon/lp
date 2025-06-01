package org.rodmccutcheon;

import java.util.stream.Stream;

public interface DataSink<T> extends AutoCloseable {
    void write(T record);
    void writeAll(Stream<T> records);
}
