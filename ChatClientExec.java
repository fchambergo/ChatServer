
/**
 * The client follows the Chat Protocol which is as follows.
 * When the server sends "SUBMITNAME" the client replies with the
 * desired screen name.  The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are
 * already in use.  When the server sends a line beginning
 * with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all
 * chatters connected to the server.  When the server sends a
 * line beginning with "MESSAGE " then all characters following
 * this string should be displayed in its message area.
 * 
 * @author frank chambergo
 */

public class ChatClientExec implements ChatClientExecInterface{
	
	/**
     * create new ChatClient and run in its own thread
     */
	@Override
	public void startClient() throws Exception{
		
		Thread thread = new Thread(){
			
            public void run(){
				
		         while (true){  	 
		        	ChatClient client = new ChatClient();
		        	client.run(); 	   		        	 	   		        	 
		         }
			}		
		};
		thread.start();
	}	
}