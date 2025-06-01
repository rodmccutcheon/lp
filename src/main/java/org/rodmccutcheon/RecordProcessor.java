package org.rodmccutcheon;

import java.util.List;

public interface RecordProcessor<T, R> {
    List<R> process(List<T> input);
}
