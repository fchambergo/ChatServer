
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
/**
 * A multithreaded chat room server.  
 * The  chat server executive calls startServer, which runs 
 * ChatServer in a thread, in order not to block the GUI.
 * This top-level thread listens for a client to connect, 
 * asks for the client's Screen Name, checks it for duplication,
 * and if ok, starts a new thread by which the server will
 * interact with this client. This thread allows the server
 * to interact with many clients at once.  The names of all
 * clients are kept in a field HashSet<String> names to allow
 * checking for duplicate screen names, and the output streams 
 * are kept in a field named HashSet<PrintWriter> writers, in order
 * that each message can be retransmitted to all the clients.
 * 
 * When a client connects the
 * server requests a screen name by sending the client the
 * text "SUBMITNAME", and keeps requesting a name until
 * a unique one is received.  After a client submits a unique
 * name, the server acknowledges with "NAMEACCEPTED".  Then
 * all messages from that client will be broadcast to all other
 * clients that have submitted a unique screen name.  The
 * broadcast messages are prefixed with "MESSAGE ".
 * 
 * @author frank chambergo
 */
public class ChatServerExec implements ChatServerExecInterface{	
	
	private static int PORT; //Port number of server
    private static HashSet<String> names = new HashSet<String>(); // Maintain a list of screen names in use in this session, to allow checking for duplicate screen names
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();  // Maintain a list of output streams in order so each message can be retransmitted to all the clients.
	
    
    /**
     * Starts an instance of a server in a thread so that GUI thread can continue to operate asynchronously
     * @param port
     */
	@Override
	public void startServer(int port){
		PORT = port;
		Thread thread = new Thread(){ //server thread
			
			@Override 
            public void run(){
				
				try{
					ServerSocket serverSocket = new ServerSocket(PORT); //create new server socket using port
	                
	                while (true){               
	                    try{
	                    	// starts a new thread for each client that is added to the chat room. 
	                        while (true){
	                        	NewClient nc = new NewClient(serverSocket.accept());
	                        	nc.start();
	                        }
	                    }
	                    finally{
	                    	serverSocket.close();
	                    }    
	                }          
				}			
				catch (IOException e){
                    e.printStackTrace();
                }	
			}
		};
		thread.start(); //start thread
	}
	
    /**
     * A Thread class which will be used every time a new client is added into the chat room
     */
	public static class NewClient extends Thread{
		//to read and send out input and output streams
    	private Scanner in = new Scanner(System.in);
        private PrintWriter out;
        
        private Socket socket;
        private String name;

        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public NewClient(Socket socket){
            this.socket = socket;
        }

        /**
         * run thread and get/give input/output from server and clients
         */
        public void run(){
            try{
            	//Use the input and output streams on the socket to communicate with the client 
                in = new Scanner(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                //user name verification
                while (true){
	                out.println("SUBMITNAME");
	                name = in.nextLine();
	                
	                if (name == null){
	                    return;
	                }
	                
	                synchronized (names){
	                	//add name to hashset if it is not already added
	                    if (!names.contains(name)){
	                        names.add(name);
	                        break;
	                    }
	                }
                }
                
                //send screen name to client
                out.println("NAMEACCEPTED");
                writers.add(out);
                
                //when client write in chat box, the message will be sent to server
                while (true){
                    String input = in.nextLine();
                    if (input == null){
                        return;
                    }
                    for (PrintWriter messages : writers){
                    	messages.println("MESSAGE " + name + ": " + input);
                    }
                }
            }
            catch (IOException e){
                System.out.println(e);
            }
            finally{
                	try{
                    socket.close();
                }
                catch (IOException e){
                	System.out.println(e);
                }
            }
        }
    }
}