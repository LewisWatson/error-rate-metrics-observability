package com.github.lewiswatson.demo.error.service;

/**
 * Facilitates tracking of behaviour (both good and bad) grouped by the underlying cause.
 * <p>
 * Implementations of this interface will likely distribute the counters a centralized metrics and
 * alerting tool such as prometheus.
 */
public interface OutcomeCounter {

  /**
   * Facilitates tracking of successful behaviour grouped by category. This can be things
   * that services can automatically handle (e.g. invalid user input resulting in an appropriate
   * error response) or things that cannot be worked around (e.g. calls to a database failing).
   * @param category high level category for the outcome. Lower level details should go into logs
   */
  void success(String category);

  /**
   * Facilitates tracking of unwanted behaviour grouped by category. This can be things
   * that services can automatically handle (e.g. invalid user input resulting in an appropriate
   * error response) or things that cannot be worked around (e.g. calls to a database failing).
   * @param category high level category for the outcome. Lower level details should go into logs
   */
  void failure(String category);

  /**
   * Facilitates tracking of persistent unwanted behaviour grouped by category.
   * This can be things that services can automatically handle (e.g. invalid user input resulting
   * in an appropriate error response) or things that cannot be worked around (e.g. calls to a
   * database failing).
   * @param category high level category for the outcome. Lower level details should go into logs
   */
  void persistentFailure(String category);

}
