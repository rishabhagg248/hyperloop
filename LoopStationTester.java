// TODO complete file header

import java.util.NoSuchElementException;

/**
 * This class tests the LoopStation class, and by extension, the Track class
 */
public class LoopStationTester {

  /**
   * Checks the correctness of the createPod() method. This method should: - create a Pod with the
   * given capacity and podClass - add it to the correct end of the correct Track in the LoopStation
   * - return a reference (shallow copy) to that Pod Note that the tracks in LoopStation are
   * protected, so you may access them directly for testing purposes
   * 
   * @return true if createPod() is functioning correctly, false otherwise
   */
  public static boolean testCreatePod() {
    try {
      LoopStation station = new LoopStation();

      // Test creating first class pod
      Pod firstClass = station.createPod(10, true);
      if (firstClass == null || firstClass.getPodClass() != 0 || station.waitingFirst.size() != 1
          || station.waitingEconomy.size() != 0) {
        return false;
      }

      // Test creating economy pod
      Pod economy = station.createPod(20, false);
      if (economy == null || economy.getPodClass() != 1 || station.waitingFirst.size() != 1
          || station.waitingEconomy.size() != 1) {
        return false;
      }

      // Verify pods were added to correct tracks
      if (!station.waitingFirst.contains(firstClass) || !station.waitingEconomy.contains(economy)) {
        return false;
      }

      // Verify capacity was set correctly
      if (firstClass.getCapacity() != 10 || economy.getCapacity() != 20) {
        return false;
      }

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Checks the correctness of the launchPod() method. This method should: - throw a
   * NoSuchElementException if no pods are waiting to launch - launch first class pods from the END
   * of the waitingFirst track - launch economy class pods from the BEGINNING of the waitingEconomy
   * track - launch ALL first class pods before launching ANY economy class pods Note that the
   * tracks in LoopStation are protected, so you may access them directly for testing purposes
   * 
   * @return true if launchPod() is functioning correctly, false otherwise
   */
  public static boolean testLaunchPod() {
    try {
      LoopStation station = new LoopStation();

      // Test launching with no pods (should throw exception)
      try {
        station.launchPod();
        return false; // Should not reach here
      } catch (NoSuchElementException e) {
        // Expected behavior
      }

      // Create mix of first class and economy pods
      Pod first1 = station.createPod(10, true);
      Pod first2 = station.createPod(10, true);
      Pod econ1 = station.createPod(20, false);
      Pod econ2 = station.createPod(20, false);

      // Launch first class pods, which should follow LIFO order
      station.launchPod(); // Expected to launch first2
      if (station.launched.size() != 1 || station.launched.get(0) != first1) {
        return false;
      }

      station.launchPod(); // Expected to launch first1
      if (station.launched.size() != 2 || station.launched.get(0) != first2) {
        return false;
      }

      // Launch economy pods, which should follow FIFO order
      station.launchPod(); // Expected to launch econ1
      if (station.launched.size() != 3 || station.launched.get(2) != econ1) {
        return false;
      }

      station.launchPod(); // Expected to launch econ2
      if (station.launched.size() != 4 || station.launched.get(3) != econ2) {
        return false;
      }

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Checks the correctness of the clearMalfunctioning() method. This method should: - repeatedly
   * check the launched track for malfunctioning pods - remove those pods correctly - report the
   * number of pods it removed once there are no longer any malfunctioning pods
   * 
   * Things to consider when you are testing:
   * 
   * - there is a protected setNonFunctional() method you may use for testing purposes to ensure
   * that at least one pod is non-functional
   * 
   * - calling isFunctional() on a Pod may cause it to malfunction! You should come up with an
   * alternate way to check whether a Pod is functional, if you have not already.
   * 
   * - verify that the difference in number of pods from before the method was called and after the
   * method was called is equal to the number that it reported
   * 
   * @return true if clearMalfunctioning() is functioning correctly, false otherwise
   */
  public static boolean testClearMalfunctioning() {
    try {
      LoopStation station = new LoopStation();

      // Create and launch some pods
      Pod pod1 = station.createPod(10, true);
      Pod pod2 = station.createPod(10, true);
      Pod pod3 = station.createPod(10, true);

      station.launchPod();
      station.launchPod();
      station.launchPod();

      int initialSize = station.launched.size();

      // Make some pods malfunction
      pod1.setNonFunctional();
      pod3.setNonFunctional();

      // Clear malfunctioning pods
      int removed = station.clearMalfunctioning();

      // Verify correct number of pods were removed
      if (removed != 2) {
        return false;
      }

      // Verify final size is correct
      if (station.launched.size() != initialSize - removed) {
        return false;
      }

      // Verify malfunctioning pods were actually removed
      if (station.launched.contains(pod1) || station.launched.contains(pod3)) {
        return false;
      }

      // Verify the functional pod remains
      if (!station.launched.contains(pod2)) {
        return false;
      }

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Checks the correctness of the three getNumXXX() methods from LoopStation. This will require
   * adding Pods of various types, loading them with passengers, and launching them.
   * 
   * @return true if the getNumXXX() methods are all functioning correctly, false otherwise
   */
  public static boolean testGetNums() {
    try {
      LoopStation station = new LoopStation();

      // Test initial empty state
      if (station.getNumLaunched() != 0 || station.getNumWaiting() != 0
          || station.getNumPassengers() != 0) {
        return false;
      }

      // Create and add passengers to pods
      Pod first1 = station.createPod(3, true);
      first1.addPassenger("Alice");
      first1.addPassenger("Bob");

      Pod econ1 = station.createPod(2, false);
      econ1.addPassenger("Charlie");

      // Test waiting counts
      if (station.getNumWaiting() != 2 || station.getNumPassengers() != 3) {
        return false;
      }

      // Launch a pod and test counts
      station.launchPod();
      if (station.getNumLaunched() != 1 || station.getNumWaiting() != 1
          || station.getNumPassengers() != 3) {
        return false;
      }

      return true;

    } catch (Exception e) {
      return false;
    }
  }

  public static void main(String[] args) {
    boolean test1 = testCreatePod();
    System.out.println("testCreatePod: " + (test1 ? "PASS" : "fail"));

    boolean test2 = testLaunchPod();
    System.out.println("testLaunchPod: " + (test2 ? "PASS" : "fail"));

    boolean test3 = testClearMalfunctioning();
    System.out.println("testClearMalfunctioning: " + (test3 ? "PASS" : "fail"));

    boolean test4 = testGetNums();
    System.out.println("testGetNums: " + (test4 ? "PASS" : "fail"));

    System.out.println("ALL TESTS: " + ((test1 && test2 && test3 && test4) ? "PASS" : "fail"));
  }

}
