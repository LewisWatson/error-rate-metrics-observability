package com.github.lewiswatson.demo.error.service;

import com.github.lewiswatson.demo.error.model.Outcome;
import com.github.lewiswatson.demo.error.rest.model.SimulationResponse;
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

  private final OutcomeCounter outcomeCounter;

  public SimulationResponse simulate(int iterations, List<Pair<Outcome, Double>> odds) {

    Map<Outcome, Integer> outcomeCounts = new HashMap<>();
    Arrays.asList(Outcome.values()).stream().forEach(outcome -> outcomeCounts.put(outcome, 0));

    EnumeratedDistribution<Outcome> outcomeEnumeratedDistribution = new EnumeratedDistribution<>(odds);

    for(int i=0; i<iterations; i++) {

      Outcome outcome = outcomeEnumeratedDistribution.sample();

      switch (outcome) {
        case HAPPY_PATH:
          log.trace("Happy Path :)");
          outcomeCounter.success("happy-path-1");
          break;
        case HAPPY_PATH_2:
          log.trace("Happy Path 2 :)");
          outcomeCounter.success("happy-path-2");
          break;
        case VALIDATION_ERROR:
          log.debug("{} is not a valid PAN number", "foo");
          outcomeCounter.failure("validation-error");
          break;
        case SLOW_CAT_REPO_UPDATE:
          log.info("slow cat repository update operation (to update {} breed): {}ms", "tabby cat", "612");
          outcomeCounter.failure("slow-cat-repo-update");
          break;
        case CALL_TO_X_SERVICE_FAILED:
          log.info("call to {} service failed, doing {} as a result", "x", "y", new IOException("due to z"));
          outcomeCounter.failure("call to service x failed");
          break;
        case CALL_TO_X_SERVICE_FAILED_PERSISTENTLY:
          log.warn("call to {} service failing persistently after {} attempts", "x", "5");
          outcomeCounter.persistentFailure("call to service x failing persistently");
          break;
      }

      outcomeCounts.put(outcome, outcomeCounts.get(outcome) + 1);
    }

    return SimulationResponse.builder().iterations(iterations).counts(outcomeCounts).build();
  }
}
