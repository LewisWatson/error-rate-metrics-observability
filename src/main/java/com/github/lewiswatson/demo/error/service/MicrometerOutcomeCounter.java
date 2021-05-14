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
public class MicrometerOutcomeCounter implements OutcomeCounter {

  private final MeterRegistry meterRegistry;
  private final HashMap<String, Counter> successCounters = new HashMap<>();
  private final HashMap<String, Counter> failureCounters = new HashMap<>();
  private final HashMap<String, Counter> persistentFailureErrorCounters = new HashMap<>();

  @Override
  public void success(String category) {
    if(!successCounters.containsKey(category)) {
      successCounters.put(category, Counter.builder("success").tag("category", category).register(meterRegistry));
    }
    successCounters.get(category).increment();
  }

  @Override
  public void failure(String category) {
    if(!failureCounters.containsKey(category)) {
      failureCounters.put(category, createErrorCounter(category, false));
    }
    failureCounters.get(category).increment();
  }

  @Override
  public void persistentFailure(String category) {
    if(!persistentFailureErrorCounters.containsKey(category)) {
      persistentFailureErrorCounters.put(category, createErrorCounter(category, true));
    }
    persistentFailureErrorCounters.get(category).increment();
  }

  private Counter createErrorCounter(String category, boolean isPersistent) {
    return Counter.builder("failure")
        .tags(Arrays.asList(
            Tag.of("category", category),
            Tag.of("persistent", isPersistent? "y":"n")
        ))
        .register(meterRegistry);
  }

}
