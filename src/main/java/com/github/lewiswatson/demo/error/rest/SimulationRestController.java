package com.github.lewiswatson.demo.error.rest;

import com.github.lewiswatson.demo.error.model.Outcome;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.github.lewiswatson.demo.error.rest.model.SimulationParams;
import com.github.lewiswatson.demo.error.rest.model.SimulationResponse;
import com.github.lewiswatson.demo.error.service.Simulator;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SimulationRestController {

  private final Simulator simulator;

  @PostMapping(value = "simulate", produces = "application/json")
  public ResponseEntity<SimulationResponse> createPayment(@RequestBody SimulationParams simulationParams) {

    List<Pair<Outcome, Double>> odds = new ArrayList<>();
    odds.add(new Pair<>(Outcome.HAPPY_PATH, simulationParams.getHappyPathOdds()));
    odds.add(new Pair<>(Outcome.HAPPY_PATH_2, simulationParams.getHappyPath2Odds()));
    odds.add(new Pair<>(Outcome.VALIDATION_ERROR, simulationParams.getValidationErrorOdds()));
    odds.add(new Pair<>(Outcome.SLOW_CAT_REPO_UPDATE, simulationParams.getSlowCatRepoUpdateOdds()));
    odds.add(new Pair<>(Outcome.CALL_TO_X_SERVICE_FAILED, simulationParams.getCallToXServiceFailedOdds()));
    odds.add(new Pair<>(Outcome.CALL_TO_X_SERVICE_FAILED_PERSISTENTLY, simulationParams.getCallToXServiceFailedPersistentlyOdds()));

    SimulationResponse simulationResponse = simulator.simulate(simulationParams.getIterations(), odds);

    return new ResponseEntity<>(simulationResponse, HttpStatus.OK);
  }

}
