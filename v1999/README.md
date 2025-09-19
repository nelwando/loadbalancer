# loadbalancer 1999

## Architecture Overview

This project implements a simple TCP Load Balancer in Java using, as best as possible, language features that were available in 1999. It listens on a specified port and forwards incoming client connections to a pool of backend endpoints using a pluggable load balancing algorithm (e.g., Round Robin, IP Hash). The system performs health checks on backend endpoints and sends alerts when endpoints go down or recover.

### Main Components

- **Launch**: Entry point for starting the load balancer.
- **LoadBalancer**: Core class that listens for client connections and delegates them to backend endpoints.
- **ClientHandler**: Handles each client connection, selects a backend, and pipes data between client and backend.
- **Algorithm**: Interface for load balancing algorithms (e.g., RoundRobin, IPHash).
- **EndPointService**: Manages backend endpoints, health checks, and failure notifications.
- **AlertService**: Sends alerts on endpoint failures and recoveries.

## How to Launch

### Prerequisites
- Java 8 or higher
- Compiled classes (see `bin/` directory)

### Build (if needed)
If you have source code only, compile with:

```
javac -d bin src/com/neil/harvey/loadbalancer/**/*.java
```

### Run the Load Balancer

From the `v1999` directory, run:

```
java -cp bin com.neil.harvey.loadbalancer.Launch <listenPort> <endpoints> <algorithm> <timeToLive>
```

#### Arguments:
- `listenPort`: Port for the load balancer to listen on (1-65535)
- `endpoints`: Comma-separated list of backend endpoints in the form `host:port` (e.g., `127.0.0.1:9001,127.0.0.1:9002`)
- `algorithm`: Load balancing algorithm to use (`RoundRobin` or `IPHash`)
- `timeToLive`: Health check interval in milliseconds (e.g., `5000`)

#### Example:

```
java -cp bin com.neil.harvey.loadbalancer.Launch 8080 127.0.0.1:9001,127.0.0.1:9002 RoundRobin 5000
```

This will start the load balancer on port 8080, forwarding connections to two backends using the Round Robin algorithm, with health checks every 5 seconds.

## Notes
- Alerts are printed to the console by default (see `SimpleAlertServiceImpl`).
