
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;
import javafx.scene.layout.BorderPane;


public class GUI extends Application {
	
	// Declares variables to hold the labels for the instructions that will be displayed
	Label instructionTitle;
	Label instructions;
	
	//Declare variables for buttons 
	Button startServer;
	Button startClient;
	Button exit;
	
	// Counter to make sure the server can only be started once
	int counter = 0;

	public static void main(String[] args) {
        launch(args);
    }
	
	/**
	 * Sets the layout of the GUI, with all the labels, textfields, and buttons that the chat room application will need to perform all its functions. 
	 */
	@Override
	public void start(Stage stage)
	{

		BorderPane layout = new BorderPane();
		
		//make instruction title 
		instructionTitle = new Label("Chat Room Controller");
		instructionTitle.setFont(new Font(20));
		
		//list instructions
		instructions = new Label("1. Start the server.\n"
								+ "2. Start a client.\n"
								+ "3. Enter a screen name in the client's GUI\n"
								+ "4. Start more clients.\n"
								+ "5. Enter a message in a client's GUI\n");

		//create buttons
		startServer = new Button("Start the _Server");
		startServer.setMnemonicParsing(true);
		startServer.setTooltip(new Tooltip("Click to start the server."));
		
		startClient = new Button("Start each _Client"); //letter C will be mnemonic for Start Client button
		startClient.setMnemonicParsing(true);
		startClient.setTooltip(new Tooltip("Click to start a client.")); 
		
		exit = new Button("_Exit"); //letter E will be mnemonic for Exit button
		exit.setMnemonicParsing(true);
		exit.setTooltip(new Tooltip("Click to close program."));
			
		//give action to buttons
		startServer.setOnAction(new ButtonEventHandler());
		startClient.setOnAction(new ButtonEventHandler());	
		exit.setOnAction(new ButtonEventHandler());	
		
		//create a pane that holds instructions
		VBox instructionPane = new VBox(5);
		instructionPane.setAlignment(Pos.CENTER_LEFT);
		instructionPane.setPadding(new Insets(10,200,10,60));
		instructionPane.setStyle("-fx-border-color: gray;");
		instructionPane.getChildren().addAll(instructionTitle, instructions);
		
		//create a pan that holds buttons
		HBox buttonPane = new HBox(5);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setPadding(new Insets(10, 10, 10, 10));
		buttonPane.getChildren().addAll(startServer, startClient, exit);
			
		//create a pan that holds the instruction and buttone panes
		VBox wholePane = new VBox(5);
		wholePane.setAlignment(Pos.CENTER);
		wholePane.getChildren().addAll(instructionPane, buttonPane);
		
		//set the whole pane to the center
		layout.setCenter(wholePane); 
		
		// Set displayPane as root of scene and set the scene on the stage
		Scene scene = new Scene(layout);
		stage.setTitle("Chat Room Controller");
		stage.setScene(scene);
		stage.show();	
	}

	class ButtonEventHandler implements EventHandler<ActionEvent>{
		public void handle (ActionEvent e){
			//if start server button is selected
			if (e.getSource() == startServer){
				if(counter < 1){ //if server has started for the first time
					ChatServerExec server = new ChatServerExec();
					
					server.startServer(7777);//start server with port
					
					// display JOption Pane message if server has started
					JOptionPane.showMessageDialog(null, "The server has been started.");
					
					counter++;
				}
				//if server has already started, another one cannot be started
				else{
					JOptionPane.showMessageDialog(null, "Cannot start more than one server.");	
				}
			}
			//if start client button is selected
			else if (e.getSource() == startClient){
				//client can only be made if server has been started
				if(counter >= 1){ //more than one client can be made
					
					ChatClientExec client = new ChatClientExec();
					//create a client
					try{
						client.startClient();
					}
					catch (Exception ex){
						ex.printStackTrace();
					}
				}
				// if the server has not been started, then display message saying to start it first.
				else{
					JOptionPane.showMessageDialog(null, "Start the server first.");	
				}	
			}
			else if (e.getSource() == exit){ //close program if selected
				System.exit(0);	
			}
		}
	}
}
