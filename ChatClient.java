
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.awt.*;
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
