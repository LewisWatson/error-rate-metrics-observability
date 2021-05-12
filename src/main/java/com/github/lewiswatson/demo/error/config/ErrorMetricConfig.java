package com.github.lewiswatson.demo.error.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ErrorMetricConfig {

  private final MeterRegistry meterRegistry;

  @Bean
  Counter validationErrorCounter() {
    return createErrorCounter("validation error", false);
  }

  @Bean
  Counter slowCatRepoUpdateCounter() {
    return createErrorCounter("slow cat repo update", false);
  }

  @Bean
  Counter callToXServiceFailedCounter() {
   return createErrorCounter("call to x service failed", false);
  }

  @Bean
  Counter callToXServiceFailedPersistentlyCounter() {
    return createErrorCounter("call to x service failed", true);
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
