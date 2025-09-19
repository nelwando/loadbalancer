# Test Infrastructure

This project provides simple Java classes for testing client-server communication:
- **TestServer**: A basic TCP server that echoes received messages, prefixed with an identifier.
- **TestClient**: A simple client that connects to the server, sends messages, and prints responses.

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line access (Windows Command Prompt, PowerShell, or Unix shell)

## Compiling the Code

Navigate to the `src` directory and compile the Java files:

```sh
cd testinfrastructure/src
javac -d ../bin com/neil/harvey/server/TestServer.java com/neil/harvey/client/TestClient.java
```

This will place the compiled `.class` files in the `bin` directory.

## Running the TestServer

To start the server, run:

```sh
cd ../bin
java com.neil.harvey.server.TestServer <port> <id>
```

- `<port>`: The port number to listen on (e.g., `9090`)
- `<id>`: An identifier string for the server (e.g., `A`)

**Example:**

```sh
java com.neil.harvey.server.TestServer 9090 A
```

The server will print a message indicating it is listening. It will echo any received lines, prefixing them with the identifier.

## Running the TestClient

In a separate terminal, run:

```sh
cd testinfrastructure/bin
java com.neil.harvey.client.TestClient
```

The client will connect to `127.0.0.1:9090`, send two messages (`hello` and `bye`), and print the responses from the server.

## Example Output

**Server:**
```
Backend A listening on 9090
```

**Client:**
```
Got: [A] hello
Got: [A] bye
```

## Notes
- You can run multiple servers on different ports or with different IDs.
- You can modify the client or server code to test different scenarios.