package com.github.lewiswatson.demo.error.rest.model;

import com.github.lewiswatson.demo.error.model.Outcome;
import java.util.Map;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class SimulationResponse {
  Integer iterations;
  @Singular
  Map<Outcome, Integer> counts;
}
