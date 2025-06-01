package org.rodmccutcheon.processor;

import java.util.List;

public interface RecordProcessor<T, R> {
    List<R> process(List<T> input);
}
