# MultiUserChat_Socket
## Server --- Base Conummication Framework
1. Need a ServerSocket to accept connection. 
    - Socket clientSocket = serverSocket.accept(); <br>
2. Read / Send Data
    - Socket(client) <br>
         * InputStream inputStream = clientSocket.getInputStream(); <br>
         * OutputStream outputStream = clientSocket.getOutputStream(); <br>
3. Need a Worker Thread to handle client **multiple** connections <br>
    - If we do not have the other threads to handle the communication, the main thread will block 
      and we only able to handle one client at a time. <br>
      
## Relation between User & Server
1. User -> Server
    - login / logoff
2. Server -> User
    - online / offline
3. User -> User
    - Direct or Chatroom messages
    
## Commands
1. **login** <username> <password>
2. **logoff** / **quit**
3. **msg** <the person you want to talk to> <message>
    - Ann: 
        * msg Bob How are you?  —— Sent
    - Bob: 
        * msg Ann How are you?  —— Recieve
        * msg Ann I am fine. How about you, Bob?  —— Sent
4. **join** #<group_name>
    - join #team1
    - msg #team1 Hi everyone  —— Sent
    - msg #team1:<login>Hi everyone  —— Recieve
        * Everyone in the group will receive the message
5. **leave** #<group_name>


## GUI
1. create an User Pane
    - take the client
    - create a window with user list
2. In order to work
    - add itself : addUserStatusListener
        * Tell you online or offline
        * Build user list model
        * List model is rendered by using JList —— pass the model to the JList
        * Create a user interface
3. Another GUI — Double click on username
        - List <br>
            * Represent the conversation
        - Field <br>
            * What msg you want to sent to this user
            
## Interact with Telnet and GUI msg window
You type the msg by yourself : “msg”.   “sent to whom”   “msg body” <br>
You receive the msg 		: “msg” “sent from whom” “msg body” <br>

## Function
1. Allow mutiple users
2. Offer customized version of username and password  
3. Avoid duplicate username
4. Notify every user when someone login or logoff
5. Having a group to chat with
6. Simple GUI with a list of onlin user, login pages, and message frame

## Reference
Learn from youtube video from FullStackMastery. <br>

