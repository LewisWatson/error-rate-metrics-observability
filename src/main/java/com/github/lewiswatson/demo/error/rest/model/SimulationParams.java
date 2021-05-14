package com.github.lewiswatson.demo.error.rest.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value @AllArgsConstructor
public class SimulationParams {
  Integer iterations;
  Double happyPathOdds, happyPath2Odds, validationErrorOdds, slowCatRepoUpdateOdds, callToXServiceFailedOdds,
      callToXServiceFailedPersistentlyOdds;
}
