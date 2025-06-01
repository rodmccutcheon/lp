package org.rodmccutcheon.sink;

import java.util.List;

public interface DataSink<T> {
    void write(String filename, List<T> record);
}
