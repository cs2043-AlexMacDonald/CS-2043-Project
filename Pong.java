package application;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane; 
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.*;
import java.sql.*;

public class Pong extends Application {
	
	//variable
	private static final int width = 800;
	private static final int height = 600;
   private static final int width2 = 200;
   private static final int height2 = 200;
	private static final int PLAYER_HEIGHT = 100;
	private static final int PLAYER_WIDTH = 15;
	private static final double BALL_R = 15;
	private double ballYSpeed = 1;
	private double ballXSpeed = 1;
	private double playerYPos = height / 2;
	private double opponentYPos = height / 2;
	private double ballXPos = width / 2;
	private double ballYPos = height / 2;
	private int player = 0;
	private int opponent = 0;
	private boolean gameStarted;
	private int playerXPos = 0;
	private double opponentXPos = width - PLAYER_WIDTH;
   private int randoNum;
   private int min = 1;
   private int max = 5;
   private int limit = 0;
   public boolean check = false;
	
       @Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("PONG");
      GridPane root = new GridPane();
      Scene theScene = new Scene (root);
		//setting the size
      
		Canvas canvas = new Canvas(width, height);
      root.getChildren().add(canvas);
		GraphicsContext gc = canvas.getGraphicsContext2D();
      root.setPrefSize(800, 600);
      

		
		Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc, stage, canvas)));
		//number of cycles in animation INDEFINITE = repeat indefinitely
		tl.setCycleCount(Timeline.INDEFINITE);
		
		//player control
		canvas.setOnMouseMoved(e ->  playerYPos  = e.getY());
		canvas.setOnMouseClicked(e ->  gameStarted = true);
		stage.setScene(new Scene(new StackPane(canvas)));
		stage.show();
		tl.play();
      

	}

	private void run(GraphicsContext gc,  Stage theStage, Canvas canvas) 
   {
      
		//set graphics
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
		
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font(25));
		
		if(gameStarted) 
      {
         //initial ball movement
			ballXPos += ballXSpeed;
			ballYPos += ballYSpeed;
			
			//opponent
			if(ballXPos < width - width  / 4)
         {
				opponentYPos = ballYPos - PLAYER_HEIGHT / 2;
			}  
         else 
         {
				opponentYPos =  ballYPos > opponentYPos + PLAYER_HEIGHT / 2 ?opponentYPos += 1.75: opponentYPos - 1.75;
			}
         
			//making the ball
			gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);
			
		} 
      else 
      {
			//start text
			gc.setStroke(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.strokeText("Left Click to Begin", width / 2, height / 2);
			
			//reset the ball start position 
			ballXPos = width / 2;
			ballYPos = height / 2;
			
			//reset the ball speed and the direction
			ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
			ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
		}
		
		//makes sure the ball stays in the playable area
		if(ballYPos > height || ballYPos < 0) 
      {
         ballYSpeed *=-1;
		}
      
		//if you miss the ball, computer gets a point
		if(ballXPos < playerXPos - PLAYER_WIDTH) {
			opponent++;
			gameStarted = false;
       check = true;
       GridPane root = new GridPane();
       root.setPrefSize(800, 600);
       
              
      TextField username = new TextField ();
      TextField pass = new TextField ();
      username.setMaxSize(200, 38);
      pass.setMaxSize(200, 38);
      
          Button sub = new Button("submit");
          sub.setMaxSize(100, 50);
          
          Button reset = new Button("Reset");
          sub.setMaxSize(100, 50);
                      
          root.add(new Label("Username: "), 0, 0);
          root.add(username, 1, 0);
          root.add(new Label("Password:"), 0, 1);
          root.add(pass, 1, 1);
          root.add(sub, 0, 2);
          root.add(reset, 0, 3);
          
          reset.setOnAction(actionEvent ->{
             root.getChildren().clear();
             root.getChildren().addAll(canvas);
         });
       
       
        sub.setOnAction(actionEvent ->{
              String u = username.getText();
              String p = pass.getText();
                           
              Connection con = null;
                           
              try
              { 
                                // DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); 
  
                  con = DriverManager.getConnection("jnfr",u,p); 
  
                  Statement st = con.createStatement(); 
                  int m = st.executeUpdate(u + p); 
                  if (m == 1) 
                        System.out.println("inserted successfully : "); 
                  else
                     System.out.println("insertion failed"); 
                  con.close(); 
        } 
        catch(Exception ex) 
        { 
            System.err.println(ex); 
        } 
                     });

       
       
       Scene theScene = new Scene (root);
       theStage.setScene(theScene);
       theStage.show();




         
         player = 0;
		}
		
		//if the computer misses the ball, you get a point
		if(ballXPos > opponentXPos + PLAYER_WIDTH) {  
			player++;
			gameStarted = false;
		}
	
		//increase the speed after the ball hits the player
		if( ((ballXPos + BALL_R > opponentXPos) && ballYPos >= opponentYPos && ballYPos <= opponentYPos + PLAYER_HEIGHT) || ((ballXPos < playerXPos + PLAYER_WIDTH) && ballYPos >= playerYPos && ballYPos <= playerYPos + PLAYER_HEIGHT)) 
      {
         limit++;
         if (limit < 11)
         {
            ballYSpeed *= 1.3;
            ballXSpeed *= 1.3;
         }   
            ballXSpeed *= -1;
            ballYSpeed *= -1;
         
            //randomize the ball movement after a player hits it
            randoNum = (int)Math.random()*(max-min+1)+min;
               if(randoNum == 1)
               {
                  ballYSpeed *= -1.30;
               }
               else if(randoNum == 2)
               {
                  ballYSpeed *= -0.25;
               }
               else if(randoNum == 3)
               {
                  ballYSpeed *= -0.6;
               }
               else if (randoNum == 4)
               {
                  ballYSpeed *= -1.0;
               }
               else if (randoNum == 5)
               {
                  ballYSpeed *= 0.0;
               }
               randoNum = 0;
		}  
		   
		//draw score
      gc.fillText("Your score is: " + player, width / 2, 100);
		//draw player and opponent
		gc.fillRect(opponentXPos, opponentYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
		gc.fillRect(playerXPos, playerYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
	}
	
		// start the application
		public static void main(String[] args) {
		launch(args);
		}
}