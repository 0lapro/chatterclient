### Disclaimer
This program is free for learning and educational purposes. 

# Chatterclient
A stand-alone instant messenger client with simple GUI that allows you to chat with other users.

## Semantics
Everything in the program is named to reflect its purpose. This makes writing test cases unambiguous and testing more meaningful.

## Object-Oriented approach
1. The OOP approach separates concerns and allows room for easy improvement so anyone can improve this application as suitable.
2. The interfaces are used to separate common behaviors and let any class implement them when needed.
3. Classes that have common purposes are grouped into the same package.
4. There's also room for inheritance if needed.
5. All classes are loosely coupled to promote indepedence and avoid unneccessary dependencies.

## Multithreading
The threads are managed properly by the executor service and the GUI runs on the Event Dispatching Thread for thread-safety.

## Networking
Socket (called just socket) is the server endpoint used for communicating with the client.
DatagramSocket is the socket for sending and receiving datagram packets.
DatagramPacket is used to implement a connectionless packet delivery service.
InetAddress is the class that represents an Internet Protocol (IP) address.

## How it works
1. One or more client(s)  can join the chat by connecting to the server [GITHUB] (https://github.com/0lapro/chatterserver) with a username.
2. Either the server or client can run independently on the same network and either one of them can start first without affecting each other.
3. Click the connect button and enter your username to start chatting
4. On the same network, you can run the chatterserver and chatterclient on either same or separate computers on Windows or Linux where Java and JDK is installed or you can add or tweak one or few lines of code to add a one-to-one chatting capability and run it on different networks completely.


