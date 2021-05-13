package com.github.lewiswatson.demo.error.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.Arrays;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorCounter {

  private final MeterRegistry meterRegistry;
  private final HashMap<String, Counter> counters = new HashMap<>();
  private final HashMap<String, Counter> persistentErrorCounters = new HashMap<>();

  public void increment(String cause) {
    if(!counters.containsKey(cause)) {
      counters.put(cause, createErrorCounter(cause, false));
    }
    counters.get(cause).increment();
  }

  public void incrementPersistentError(String cause) {
    if(!persistentErrorCounters.containsKey(cause)) {
      persistentErrorCounters.put(cause, createErrorCounter(cause, true));
    }
    persistentErrorCounters.get(cause).increment();
  }

  private Counter createErrorCounter(String cause, boolean isPersistent) {
    return Counter.builder("error")
        .tags(Arrays.asList(
            Tag.of("cause", cause),
            Tag.of("persistent", isPersistent? "y":"n")
        ))
        .register(meterRegistry);
  }

}
