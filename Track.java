/**
 * A doubly-linked list implementation that manages Pods and maintains their order. Implements
 * ListADT interface for Pod objects.
 */
public class Track implements ListADT<Pod> {

  protected LinkedNode head; // First node in the track

  private int size; // Number of pods in the track

  protected LinkedNode tail; // Last node in the track

  /**
   * Checks if the track contains no pods
   * 
   * @return true if track is empty, false otherwise
   */
  @Override
  public boolean isEmpty() {
    if (size == 0 && this.head == null && this.tail == null) {
      return true;
    }
    return false;
  }

  /**
   * Returns the number of pods in the track
   * 
   * @return current size of the track
   */
  @Override
  public int size() {
    return this.size;
  }

  /**
   * Removes all pods from the track
   */
  @Override
  public void clear() {

    this.head = null;
    this.tail = null;
    this.size = 0;

  }

  /**
   * Adds a new pod to the track. First class pods (class 0) are added to front, Economy pods (class
   * 1) are added to back
   * 
   * @param newElement the pod to add
   */
  @Override
  public void add(Pod newElement) {

    LinkedNode newNode = new LinkedNode(newElement);

    try {

      if (newElement.getPodClass() == 1) {

        // add to back if pod is economy class

        // case 1: empty list
        if (this.size == 0) {

          this.head = newNode;
          this.tail = newNode;
          size++;

        }

        // case 2: non empty list
        else {

          newNode.setPrev(this.tail);
          this.tail.setNext(newNode);
          this.tail = newNode;
          size++;

        }

      } else {

        // add to front if pod is first class

        // case 1: empty list
        if (this.size == 0) {

          this.head = newNode;
          this.tail = newNode;
          size++;

        }

        // case 2: non empty list
        else {

          newNode.setNext(this.head);
          this.head.setPrev(newNode);
          this.head = newNode;
          size++;

        }
      }

    } catch (MalfunctioningPodException e) {

      e.printStackTrace();

    }
  }

  /**
   * Returns pod at specified position
   * 
   * @param index position of pod to return
   * @return Pod at specified index
   * @throws IndexOutOfBoundsException if index is invalid
   */
  @Override
  public Pod get(int index) {

    if (index >= size) {
      throw new IndexOutOfBoundsException();
    }

    LinkedNode current = this.head;

    if (index == 0) {
      return current.getPod();
    }

    for (int i = 0; i < index; i++) {
      current = current.getNext();
    }

    return current.getPod();
  }

  /**
   * Checks if a specific pod is in the track
   * 
   * @param toFind pod to search for
   * @return true if pod is found, false otherwise
   */
  @Override
  public boolean contains(Pod toFind) {

    // track is empty
    if (this.size == 0) {
      return false;
    }

    LinkedNode current = this.head;

    for (int i = 0; i < size; i++) {

      if (current.getPod().equals(toFind)) {

        return true;

      } else {

        if (current.getNext() != null) {

          current = current.getNext();

        }
      }
    }

    if (current.getPod().equals(toFind)) {
      return true;
    }

    return false;
  }

  /**
   * Finds index of first non-functional pod in track
   * 
   * @return index of first non-functional pod, or -1 if none found
   */
  public int findFirstNonFunctional() {

    // track is empty
    if (this.size == 0) {
      return -1;
    }

    LinkedNode current = this.head;

    for (int i = 0; i < size; i++) {

      if (!current.getPod().isFunctional()) {

        return i;

      } else {

        if (current.getNext() != null) {
          current = current.getNext();
        }

      }
    }

    return -1;

  }

  /**
   * Removes pod at specified index
   * 
   * @param index position of pod to remove
   * @return the removed Pod
   * @throws IndexOutOfBoundsException if index is invalid
   */
  @Override
  public Pod remove(int index) {

    if (index >= size) {
      throw new IndexOutOfBoundsException();
    }

    LinkedNode current = this.head;

    // Get to the node to remove
    for (int i = 0; i < index; i++) {
      current = current.getNext();
    }

    // Case 1: Only one node in list
    if (size == 1) {
      this.head = null;
      this.tail = null;
      size--;
      return current.getPod();
    }

    // Case 2: Removing head
    if (current == head) {
      this.head = current.getNext();
      if (this.head != null) {
        this.head.setPrev(null);
      }
    }
    // Case 3: Removing tail
    else if (current == tail) {
      this.tail = current.getPrev();
      if (this.tail != null) {
        this.tail.setNext(null);
      }
    }
    // Case 4: Removing from middle
    else {
      current.getPrev().setNext(current.getNext());
      current.getNext().setPrev(current.getPrev());
    }

    size--;
    return current.getPod();
  }

  /**
   * Finds first pod containing specified passenger
   * 
   * @param name passenger name to search for
   * @return index of first pod containing passenger, or -1 if not found
   */
  public int findPassenger(String name) {

    // if the track is empty
    if (this.size == 0) {
      return -1;
    }

    LinkedNode current = this.head;

    try {

      for (int i = 0; i < size; i++) {

        if (current.getPod().containsPassenger(name)) {

          return i;

        } else {

          if (current.getNext() != null) {
            current = current.getNext();
          }

        }

      }

    } catch (MalfunctioningPodException e) {
      e.printStackTrace();
    }

    return -1;

  }

  /**
   * Adds passenger to first available pod of specified class
   * 
   * @param name         passenger name to add
   * @param isFirstClass true for first class, false for economy
   * @return true if passenger was added, false if no space available
   */
  public boolean addPassenger(String name, boolean isFirstClass) {

    // if the track is empty
    if (this.size == 0) {
      return false;
    }

    try {

      // adding passenger in first class
      if (isFirstClass) {

        boolean runner = true;
        LinkedNode current = this.head;

        while (runner) {

          // finding the first empty first class pod
          if (current.getPod().getPodClass() == 0) {

            if (!current.getPod().isFull()) {

              current.getPod().addPassenger(name);
              return true;

            }

            else {

              current = current.getNext();

            }

          }

          else {

            runner = false;
            break;

          }
        }
      }

      else { // adding in economy

        boolean runner = true;
        LinkedNode current = this.tail;

        while (runner) {

          // finding the first empty economy class
          if (current.getPod().getPodClass() == 1) {

            if (!current.getPod().isFull()) {

              current.getPod().addPassenger(name);
              return true;

            }

            else {

              current = current.getPrev();

            }

          }

          else {

            runner = false;
            break;

          }
        }

      }

    } catch (MalfunctioningPodException e) {
      e.printStackTrace();
    }

    return false;

  }

  /**
   * Creates string representation of track contents
   * 
   * @return string showing all pods in track
   */
  public String toString() {

    String toReturn = "";
    LinkedNode current = this.head;

    for (int i = 0; i < size; i++) {

      toReturn += current.getPod().toString() + "\n";

      if (current.getNext() != null) {
        current = current.getNext();
      }

    }

    return toReturn;

  }

}
