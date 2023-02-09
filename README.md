# Request-Reply Messaging CLI Application
## Assumptions
- Each client initiates its communication (requests) with the server by requesting a specific operation, receives its response and terminates the connection.
- There is a single server that serves (replies) requests, serving the appropriate response (reply, error or confirmation message) to the requesting client. The server can serve multiple requests (from different clients) simultaneously, being implemented with *RMI technology*, which allows (automatic) multithreading.
- The server remains operational after each client request has been served and maintains history/directory information throughout its operation, regarding user accounts, history and message status. After shuting down, the data is lost, as it is stored only in the system's main memory.
- For the normal operation of the system (not in the case of running *jar* files), it requires compiling first.

## Classes
For the implementation of the project, the following 5 classes and 1 interface were developed in Java lang.

### 1. `class Client`
The `Client` class represents the system user sending requests to the system (i.e. server) for service. The request is created with the command:

    > java Client <server-ip> <server-port> <function-id> <arguments>

The operation of the class is based on *parsing* the input parameters (`String[] args`) and calling the appropriate method assigned to the RMI registry of the server system, i.e. it locates the RMI registry of the server network via `server-ip` and `server-port`, and calls the methods assigned by the expression to the declared `function-id`. The `arguments` constitute a sequence of at least one string, and are *parsed* based on the `function-id`.

Once the `main` function of the class is executed with the appropriate arguments, the server's RMI registry is located, a request is made to execute the registered method, and a response is received, displayed in the terminal.


### 2. `class Server`
The `Server` class represents the system for controlling, managing and implementing the communication between users (i.e. client(s)), answering/fulfilling their requests or serving the requested data. The response (reply) is done by the command:

    > java Server <server-port>.

The operation of the class is based on *establishing* an RMI registry at a specific `port` of the current `ip` address of the Server, in which it associates/matches the signatures of the function methods (functions; see `interface Commands`) with their implementations for operation as defined by the server (see `class CommandExecution`). This is accomplished by mapping the *commands* alias to the *stub* object that frames the method implementations. The client, with the alias that the server defines and registers in its registry, calls the methods pointed to by *stub*, that are of type `Commands`.

Once the `main` class function is executed with the appropriate arguments, the server's RMI registry is created, the `CommandExecution` implementation-class for the `Commands` interface is registered, and the server remains ready to receive and serve requests from one or more clients. It is worth noting that it can serve multiple clients simultaneously, thanks to the multithreading mechanisms of RMI technology.


### 3. `class Account`
The `Account` class represents a typical system user, described by a unique `authToken` identifier, its `username`, a list of messages in the `messageBox` and a message counter `messageCounter`, which acts as a serial number, not updating in case of an message deletion.
The class also includes *getter* type methods for the aforementioned *private* attributes.


### 4. `class Message`
The `Message` class represents a message on the system, described by a `isRead` read state logic variable, the names of the sender `sender` and the receiver `receiver`, the message body `body`, and an identifier code `id` reflecting the next `messageCounter` number of the receiver. It is worth noting that message codes are associated with each user's message list and therefore identify the message in conjunction with the `Account` to whose `messageBox` they belong.
The class includes methods of type *getter* and *setter* for the aforementioned *private* attributes.


### 5. `interface Commands`
The interface `Commands` defines 6 operations (functions) that a user can request through a client. It includes the abstract methods:

- `String createAccount(String username)` to create an account in the system.
- `String showAccounts(int authToken)` for displaying all created accounts on the system.
- `String sendMessage(int authToken, String receiver, String messageBody)` to send a message from one user to another.
- `String showInbox(int authToken)` for displaying the user's mailbox.
- `String readMessage(int authToken, int messageID)` to display (read) a received message from a user.
- ` String deleteMessage(int authToken, int messageID)` to delete a received message from a user.


### 6. `class CommandExecution`
The `CommandExecution` class represents the implementation of the `Commands` interface methods as the server chooses to define them. It also maintains and manages the list of all created user accounts `List<Account> accountList` and a serial number `tokenCounter` (starting at **1001** for the **1st** user) for assigning `authToken` to each new user (`Account`). 

It includes *getter* and *setter* type methods for the *private* attributes of the class and the following helper, *private* methods:
- `static boolean isValidUsername(String name)` which checks if the username a user chooses for the account they create is valid: it starts with an alphabetic character and includes alphanumeric characters only.
- `Account getUserByToken(int authToken)` which returns an object of type `Account` for a given identifier `authToken`, if it corresponds to an existing account.
- `Account getUserByUsername(String username)` which returns an object of type `Account` for a given (alphanumeric) username, if this corresponds to an existing account.
- `Message getMessageById(Account a, int id)` which returns an object of type `Message` from the list of messages of user `a` with message code `id`, if any.

In addition to these helper methods, the six functions (methods) described in the exposition have been implemented (`@Override`), most of which require and rely on **validation checking of the `authToken`** of the user requesting action before performing any activity and sending a *reply*.
> Implementation details can be found in the comments accompanying the code.

Therefore, with this class, the server has defined the implementation of the signatures of the method-functions that clients know and call to submit their *requests*.
