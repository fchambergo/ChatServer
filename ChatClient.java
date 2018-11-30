
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.*;

/**This class creates a chat client box and is able to receive and give messages to/from other client on server
 * 
 * @author frank chambergo
 *
 */
public class ChatClient implements ChatClientInterface{
	
	//to read and send out input and output streams
	private Scanner in = new Scanner(System.in);
    private PrintWriter out;
    
    //java swing variables to create gui for chat
    private JFrame label = new JFrame("Chat Room");
    private JTextField messageTextField = new JTextField(40);
    private JTextArea chatBox = new JTextArea(15, 50);
    private Socket clientSocket;
    
    private static int port; //server port number
    
    public ChatClient(){
    	
    	port = 7777;	
    	
    	label.setVisible(true); //set label visible
    	messageTextField.setEditable(false); //make textfield not be able to write in until screen name is chosen
    	chatBox.setEditable(false); //make chatbox not be able to be edited
        label.getContentPane().add(messageTextField, "North"); //add textfield on top of chatbox
        label.getContentPane().add(new JScrollPane(chatBox), "Center"); //give chatbox a scroll to view and add bottom of textfield
        label.pack();

        messageTextField.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
                out.println(messageTextField.getText());//receive text from user input
                messageTextField.setText(""); //after text is entered, reset textfield empty
            }
        });
    }
    
    /**
     * Prompt for and return the desired screen name.
     * 
     * @return the screen name the user chose
     */
    @Override
    public String getName(){
        return JOptionPane.showInputDialog(label, "Choose a screen name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
    }
    
    
    /**
     * Return the port number that the server uses
     * 
     * @return port - the port number for the client to connect to the server
     */
	@Override
	public int getServerPort(){	
		return port;
	}

    /**
     * Connect to the server and perform actions
     */
    public void run(){    
        
    	try{
           
           clientSocket = new Socket("localhost", getServerPort()); //connect to server
            
          //Use the input and output streams on the socket to communicate with the server  
            in = new Scanner(new InputStreamReader( clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
                   
	        // read the messages from the server and perform the appropriate actions
	        while (true){
	        	//When the server sends "SUBMITNAME" the client will reply with the desired screen name.
	            String line = in.nextLine();
	            if (line.startsWith("SUBMITNAME")){
	                out.println(getName());
	            }             
	            else if (line.startsWith("NAMEACCEPTED")){
	            	messageTextField.setEditable(true); //make textfield editable after screen name is chosen
	            }
	            // When the client receives messages from the server, it will be displayed in the the client's chat box
	            else if (line.startsWith("MESSAGE")){
	            	chatBox.append(line.substring(8) + "\n");
	            }          
	        }
        }
		catch (IOException e){
            e.printStackTrace();
        }
    }
}
