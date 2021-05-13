package com.github.lewiswatson.demo.error.service;

import com.github.lewiswatson.demo.error.model.Error;
import com.github.lewiswatson.demo.error.rest.model.SimulationResponse;
import io.micrometer.core.instrument.Counter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class Simulator {

  private final ErrorCounter errorCounter;

  public SimulationResponse simulate(int iterations, List<Pair<Error, Double>> odds) {

    // initialise behaviour count map for SimulationResponse
    Map<Error, Integer> errorCounts = new HashMap<>();
    Arrays.asList(Error.values()).stream().forEach(error -> errorCounts.put(error, 0));

    EnumeratedDistribution<Error> behaviourEnumeratedDistribution = new EnumeratedDistribution<>(odds);

    for(int i=0; i<iterations; i++) {

      Error error = behaviourEnumeratedDistribution.sample();

      switch (error) {
        case HAPPY_PATH:
          log.trace("Happy Path :)");
          break;
        case VALIDATION_ERROR:
          log.debug("{} is not a valid PAN number", "foo");
          errorCounter.increment("validation-error");
          break;
        case SLOW_CAT_REPO_UPDATE:
          log.info("slow cat repository update operation (to update {} breed): {}ms", "tabby cat", "612");
          errorCounter.increment("slow-cat-repo-update");
          break;
        case CALL_TO_X_SERVICE_FAILED:
          log.info("call to {} service failed, doing {} as a result", "x", "y", new IOException("due to z"));
          errorCounter.increment("call to service x failed");
          break;
        case CALL_TO_X_SERVICE_FAILED_PERSISTENTLY:
          log.warn("call to {} service failing persistently after {} attempts", "x", "5");
          errorCounter.incrementPersistentError("call to service x failing persistently");
          break;
      }

      errorCounts.put(error, errorCounts.get(error) + 1);
    }

    return SimulationResponse.builder().iterations(iterations).counts(errorCounts).build();
  }
}
