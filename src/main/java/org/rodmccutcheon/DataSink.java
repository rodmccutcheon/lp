package org.rodmccutcheon;

import java.util.List;

public interface DataSink<T> {
    void write(String filename, List<T> record);
}
