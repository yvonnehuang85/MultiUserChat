# MultiUserChat_Socket
## Server --- Base Conummication Framework
1. Need a ServerSocket to accept connection. 
    * Socket clientSocket = serverSocket.accept();<br>
2. Read / Send Data
    * Socket(client) <br>
        ->> InputStream inputStream = clientSocket.getInputStream(); <br>
        ->> OutputStream outputStream = clientSocket.getOutputStream(); <br>
3. Need a Worker Thread to handle client **multiple** connections <br>
    * If we do not have the other threads to handle the communication, the main thread will block 
      and we only able to handle one client at a time.
