import java.util.NoSuchElementException;

/**
 * This class manages pods in a hyperloop station, handling first class and economy pods separately.
 */
public class LoopStation {

  protected Track launched; // Track of launched pods

  protected Track waitingEconomy; // Track of economy pods waiting to launch

  protected Track waitingFirst; // Track of first class pods waiting to launch

  /**
   * Creates a new LoopStation with empty tracks for launched and waiting pods
   */
  public LoopStation() {

    launched = new Track();
    waitingEconomy = new Track();
    waitingFirst = new Track();

  }

  /**
   * Removes all malfunctioning pods from the launched track
   * 
   * @return number of malfunctioning pods removed
   */
  public int clearMalfunctioning() {

    boolean runner = true;
    int removeCounter = 0;
    int removeIndex;

    while (runner) {

      // Finding the non functional pod
      removeIndex = launched.findFirstNonFunctional();

      if (removeIndex == -1) { // No non functional pods

        runner = false;
        break;

      } else {

        // Remove the non functional pods
        launched.remove(removeIndex);
        removeCounter++;

      }
    }

    return removeCounter;

  }

  /**
   * Creates a new pod with specified capacity and class, adds it to appropriate waiting track
   * 
   * @param capacity     number of passengers the pod can hold
   * @param isFirstClass true if first class pod, false if economy
   * @return the newly created Pod
   */
  public Pod createPod(int capacity, boolean isFirstClass) {

    // Find class of pod
    int podClass = 1;
    if (isFirstClass) {
      podClass = 0;
    }

    // Creating the pod and adding it to the correct waiting
    Pod newPod = new Pod(capacity, podClass);
    if (isFirstClass) {
      waitingFirst.add(newPod);
    } else {
      waitingEconomy.add(newPod);
    }

    return newPod;

  }

  /**
   * Launches next pod from waiting tracks, prioritizing first class. First class launches from back
   * (LIFO), economy from front (FIFO)
   * 
   * @throws NoSuchElementException if no pods are waiting
   */
  public void launchPod() {

    Pod launch;

    // First class pods launched first
    if (!waitingFirst.isEmpty()) {

      launch = waitingFirst.remove(waitingFirst.size() - 1);
      launched.add(launch);

    } else if (!waitingEconomy.isEmpty()) { // If no first class pods, then launch economy pods

      launch = waitingEconomy.remove(0);
      launched.add(launch);

    } else { // No waiting pods
      throw new NoSuchElementException();
    }

  }

  /**
   * @return number of pods that have been launched
   */
  public int getNumLaunched() {
    return launched.size();
  }

  /**
   * Counts total passengers in all pods (launched and waiting)
   * 
   * @return total number of passengers in the station
   */
  public int getNumPassengers() {

    int total = 0;
    LinkedNode current;

    try {

      // launched
      if (!launched.isEmpty()) {
        current = launched.head;
        for (int i = 0; i < launched.size(); i++) {
          total += current.getPod().getNumPassengers();
          if (current.getNext() != null) {
            current = current.getNext();
          }
        }
      }

      // waitingFirst
      if (!waitingFirst.isEmpty()) {
        current = waitingFirst.head;
        for (int i = 0; i < waitingFirst.size(); i++) {
          total += current.getPod().getNumPassengers();
          if (current.getNext() != null) {
            current = current.getNext();
          }
        }
      }

      // waitingEconomy
      if (!waitingEconomy.isEmpty()) {
        current = waitingEconomy.head;
        for (int i = 0; i < waitingEconomy.size(); i++) {
          total += current.getPod().getNumPassengers();
          if (current.getNext() != null) {
            current = current.getNext();
          }
        }
      }

    } catch (MalfunctioningPodException e) {
      e.printStackTrace();
    }

    return total;

  }

  /**
   * @return total number of pods waiting to be launched
   */
  public int getNumWaiting() {
    return waitingEconomy.size() + waitingFirst.size();
  }

}
