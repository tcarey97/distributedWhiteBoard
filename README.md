# distributedWhiteBoard

This is an application where multiple distributed users can draw simultaneously on a single shared interactive canvas. Users are able to work on a drawing together in real time, without appreciable delays between making and observing edits. 

This application follows a client-server architechture; it contains a (single) multi-threaded server implementing a thread-per-connection architechture. The system is written in Java and all communication takes place via TCP Socket programming. 

Startup/operational model:

1 - the first user creates a whiteboard and becomes the whiteboard's manager.

\> java -jar CreateWhiteBoard.jar \<serverIPAddress> \<serverPort> \<managerUsername>
  
2 - other users can ask to join the whiteboard application at anytime by inputting the server's IP address and port number

\> java -jar JoinWhiteBoard.jar \<serverIPAddress> \<serverPort> \<clientUsername>
  
3 - a notification will be delivered to the manager if any peer joins. The peer can only join if the manager approves.

4 - When the manager quits, the application will be terminated. All peers get a message notifying them.

Key elements of GUI:
- shapes: the white board supports drawing lines, circles, ovals and rectangles
- text inputting: users are allowed to type text anywhere inside the white board
- users are allowed to choose their favorite colour to draw the above features. 16 colours are available.

Manager operations:
- only the manager can create a new whiteboard, open a previously saved one, save the current one and close the application.
- A file menu is available to the manager of the white board with new, open, save and saveAs and close.
- The manager is also able to kick out certain peers/users.

Other characteristics:
- users provide a username when joining the whiteboard. When the whiteboard is displayed, the client user interface displays the unique usernames of other users who are currently editing the same whiteboard. 
- all users see the same image of the whiteboard and have the priviledge of doing all the drawing operations.
- clients may connect and disconnect at any time.



