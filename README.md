# Hyperloop Station Management System 🚄💨

A Java-based hyperloop transportation system that manages pods, passengers, and station operations using custom data structures. Features priority-based launching, malfunction detection, and passenger management across different service classes.

## 🌟 Features

- **Multi-Class Pod System** - First Class and Economy pods with different priorities
- **Priority Launch Queue** - First Class pods launch first (LIFO), Economy pods follow (FIFO)
- **Malfunction Detection** - Automatic detection and removal of non-functional pods
- **Passenger Management** - Add/remove passengers with capacity tracking
- **Doubly-Linked List Implementation** - Custom Track data structure
- **Exception Handling** - Comprehensive error handling for pod malfunctions
- **Testing Suite** - Complete unit tests for all functionality

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- Basic understanding of data structures (linked lists, queues)

### Installation

1. **Download the project files:**
```bash
git clone https://github.com/yourusername/hyperloop-station.git
cd hyperloop-station
```

2. **Compile the project:**
```bash
javac *.java
```

3. **Run the tests:**
```bash
java LoopStationTester
```

4. **Expected output:**
```
testCreatePod: PASS
testLaunchPod: PASS
testClearMalfunctioning: PASS
testGetNums: PASS
ALL TESTS: PASS
```

## 🏗️ System Architecture

### Core Classes

#### 1. **LoopStation.java** - Main Station Management
```java
public class LoopStation {
    protected Track launched;        // Pods that have been launched
    protected Track waitingEconomy;  // Economy pods waiting to launch
    protected Track waitingFirst;    // First class pods waiting to launch
}
```

#### 2. **Track.java** - Doubly-Linked List Implementation
```java
public class Track implements ListADT<Pod> {
    protected LinkedNode head;       // First node in track
    protected LinkedNode tail;       // Last node in track
    private int size;               // Number of pods in track
}
```

#### 3. **Pod.java** - Hyperloop Pod Representation
```java
public class Pod {
    public static final int FIRST = 0;     // First class constant
    public static final int ECONOMY = 1;   // Economy class constant
    private String[] passengerList;        // Passenger array
    private boolean isFunctional;          // Operational status
}
```

#### 4. **LinkedNode.java** - Node for Doubly-Linked List
```java
public class LinkedNode {
    private Pod data;               // Pod stored in node
    private LinkedNode prev;        // Previous node reference
    private LinkedNode next;        // Next node reference
}
```

## 🎮 How the System Works

### Pod Creation and Management

#### Creating Pods
```java
// Create first class pod (capacity 10)
Pod firstClass = station.createPod(10, true);

// Create economy pod (capacity 20)
Pod economy = station.createPod(20, false);
```

#### Adding Passengers
```java
// Add passengers to pods
firstClass.addPassenger("Alice");
firstClass.addPassenger("Bob");
economy.addPassenger("Charlie");
```

### Launch System

#### Priority-Based Launching
1. **First Class Priority**: All first class pods launch before economy
2. **First Class LIFO**: Last first class pod added launches first
3. **Economy FIFO**: First economy pod added launches first

```java
// Launch next pod (prioritizes first class)
station.launchPod();
```

### Malfunction Management

#### Automatic Detection
```java
// Remove all malfunctioning pods
int removed = station.clearMalfunctioning();
System.out.println("Removed " + removed + " malfunctioning pods");
```

## 📊 Data Structure Details

### Track Implementation

#### Doubly-Linked List Structure
```
[Pod A] ↔ [Pod B] ↔ [Pod C] ↔ [Pod D]
   ↑                             ↑
  head                          tail
```

#### Adding Pods by Class
```java
public void add(Pod newElement) {
    if (newElement.getPodClass() == ECONOMY) {
        // Add to back (FIFO for economy)
        addToBack(newElement);
    } else {
        // Add to front (LIFO for first class)
        addToFront(newElement);
    }
}
```

#### Node Operations
```java
// Remove node from middle of list
current.getPrev().setNext(current.getNext());
current.getNext().setPrev(current.getPrev());
```

### Exception Handling

#### MalfunctioningPodException
```java
public class MalfunctioningPodException extends Exception {
    public MalfunctioningPodException(String message) {
        super(message);
    }
}
```

#### Usage in Pod Methods
```java
public int getNumPassengers() throws MalfunctioningPodException {
    if (!isFunctional) {
        throw new MalfunctioningPodException("Pod is malfunctioning!");
    }
    // Return passenger count
}
```

## 🔧 Configuration

### Pod Classes
```java
public static final int FIRST = 0;     // First class pods
public static final int ECONOMY = 1;   // Economy class pods
```

### Capacity Management
```java
// Check if pod is full
if (pod.isFull()) {
    throw new IllegalStateException("Pod is at capacity");
}

// Add passenger if space available
if (!pod.isFull()) {
    pod.addPassenger("Passenger Name");
}
```

### Malfunction Probability
```java
// Pod has 1/20 chance of malfunctioning when checked
public boolean isFunctional() {
    if (randGen.nextInt(20) == 0) {
        isFunctional = false;
    }
    return isFunctional;
}
```

## 🛠️ Development

### Testing Framework

#### Test Categories
1. **Pod Creation Tests**
   - Verify correct pod class assignment
   - Check proper track placement
   - Validate capacity settings

2. **Launch System Tests**
   - Priority ordering verification
   - LIFO/FIFO queue behavior
   - Exception handling for empty queues

3. **Malfunction Tests**
   - Detection accuracy
   - Removal correctness
   - Count verification

4. **Statistics Tests**
   - Passenger counting
   - Pod counting
   - State tracking

#### Running Individual Tests
```java
// Test pod creation
boolean result = LoopStationTester.testCreatePod();
System.out.println("Pod creation test: " + result);

// Test launch system
boolean launchTest = LoopStationTester.testLaunchPod();
System.out.println("Launch test: " + launchTest);
```

### Adding New Features

#### Custom Pod Types
```java
public class ExpressPod extends Pod {
    public static final int EXPRESS = 2;
    
    public ExpressPod(int capacity) {
        super(capacity, EXPRESS);
    }
    
    // Express pods could have priority over first class
}
```

#### Enhanced Tracking
```java
public class EnhancedTrack extends Track {
    private long[] launchTimes;
    private int totalPassengersLaunched;
    
    public double getAverageWaitTime() {
        // Calculate average time between creation and launch
    }
    
    public int getTotalPassengersLaunched() {
        return totalPassengersLaunched;
    }
}
```

#### Passenger Reservations
```java
public class ReservationSystem {
    private Map<String, Pod> reservations;
    
    public boolean makeReservation(String passenger, boolean isFirstClass) {
        // Find available pod and reserve seat
    }
    
    public void cancelReservation(String passenger) {
        // Remove passenger from reserved pod
    }
}
```

## 📈 Performance Characteristics

### Time Complexity
- **Add Pod**: O(1) - Always add to front or back
- **Launch Pod**: O(1) - Remove from front or back
- **Find Passenger**: O(n) - Linear search through pods
- **Clear Malfunctioning**: O(n) - Check each pod once

### Space Complexity
- **Per Pod**: O(capacity) - Array of passenger names
- **Per Track**: O(number of pods) - Linked list nodes
- **Overall**: O(total passengers + total pods)

### Scalability Considerations
- **Large Capacity Pods**: Memory usage scales with pod capacity
- **Many Pods**: Linked list provides efficient insertion/deletion
- **Passenger Search**: Could be optimized with hash tables

## 🎯 Design Patterns

### Template Method Pattern
```java
// ListADT interface defines common operations
public interface ListADT<T> {
    boolean isEmpty();
    int size();
    void clear();
    void add(T element);
    T get(int index);
    boolean contains(T element);
    T remove(int index);
}
```

### Strategy Pattern
```java
// Different launch strategies for different pod classes
public interface LaunchStrategy {
    void addToQueue(Pod pod, Track track);
    Pod removeFromQueue(Track track);
}

public class FirstClassStrategy implements LaunchStrategy {
    // LIFO implementation
}

public class EconomyStrategy implements LaunchStrategy {
    // FIFO implementation
}
```

## 🐛 Troubleshooting

### Common Issues

1. **IndexOutOfBoundsException**
   ```java
   // Always check bounds before accessing
   if (index >= 0 && index < track.size()) {
       Pod pod = track.get(index);
   }
   ```

2. **MalfunctioningPodException**
   ```java
   try {
       int passengers = pod.getNumPassengers();
   } catch (MalfunctioningPodException e) {
       // Handle malfunctioning pod
       System.out.println("Pod is malfunctioning: " + e.getMessage());
   }
   ```

3. **NullPointerException**
   ```java
   // Check for null before operations
   if (head != null && head.getNext() != null) {
       head.getNext().setPrev(null);
   }
   ```

### Debug Techniques

#### Visual Track Inspection
```java
public void debugPrintTrack(Track track) {
    System.out.println("Track contents:");
    for (int i = 0; i < track.size(); i++) {
        Pod pod = track.get(i);
        System.out.println("  Pod " + i + ": Class " + pod.getPodClass() + 
                          ", Passengers: " + pod.getNumPassengers());
    }
}
```

#### State Verification
```java
public void verifyStationState(LoopStation station) {
    System.out.println("Station State:");
    System.out.println("  Launched: " + station.getNumLaunched());
    System.out.println("  Waiting: " + station.getNumWaiting());
    System.out.println("  Passengers: " + station.getNumPassengers());
}
```

## 🔮 Future Enhancements

### Planned Features
- [ ] Real-time pod tracking dashboard
- [ ] Route planning for multiple stations
- [ ] Dynamic pricing based on demand
- [ ] Passenger check-in system
- [ ] Maintenance scheduling
- [ ] Performance analytics

### Advanced Features
- [ ] AI-powered demand prediction
- [ ] Multi-station network routing
- [ ] Real-time traffic optimization
- [ ] Mobile passenger app
- [ ] Emergency evacuation protocols
- [ ] Environmental impact tracking

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add your improvements
4. Run all tests to ensure functionality
5. Submit a pull request

### Contribution Guidelines
- Follow Java coding standards
- Add comprehensive tests for new features
- Document all public methods
- Maintain backward compatibility
- Test exception handling thoroughly

## 🆘 Support

If you encounter issues:

1. Run the test suite to verify system integrity
2. Check for proper exception handling
3. Verify linked list node connections
4. Test with small datasets first
5. Open an issue with detailed error information

---

**Next stop: The future of transportation!** 🚄✨

*Built with ❤️ by Rishabh Aggarwal*

### Academic Integrity Notice
This project was created for educational purposes as part of CS 300 coursework. Please respect academic integrity policies when using this code for learning or reference.
