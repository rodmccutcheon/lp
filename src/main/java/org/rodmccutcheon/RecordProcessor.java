package org.rodmccutcheon;

public interface RecordProcessor<T, R> {
    R process(T input);
}
