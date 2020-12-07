import myPackage.GameJDBC;

import javafx.application.Application;
import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label; 
import javafx.scene.layout.FlowPane;
import javafx.scene.control.*; 
import javafx.scene.layout.*; 
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.*;
import javafx.event.*;
import java.sql.*;


public class aimGame extends Application 
{
    public static long startTime = 0;
    public static String time = "0";
    public static boolean check = false;
   
    public static void main(String[] args) 
    {
        launch(args);
    }


    @Override
    public void start(Stage theStage) 
    {
        theStage.setTitle( "Aim Training Game" );
          
        Image target = new Image( "target.png" );

        GridPane root = new GridPane();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );
       
        
        Canvas canvas = new Canvas( 500, 500 );
        root.getChildren().add( canvas );

        hitBox targetData = new hitBox(100,100,32);
        IntValue numHit = new IntValue(0);
        
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font theFont = Font.font( "Verdana", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);

        Button button = new Button("Restart");
        button.setMaxSize(100, 50);
        root.add(button, 0, 5);
                     
        button.setOnAction(actionEvent ->{
             root.getChildren().clear();
             root.getChildren().addAll(canvas, button);
             check = false;
             numHit.value = 0;
             time = "0";
             startTime = 0;
         });


        theScene.setOnMouseClicked(
            new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent e)
                {
                     long elapsedTime;
                     long elapsedSeconds;
                     long secondsDisplay;
                    

                     
                     Button sub = new Button("submit");
                     sub.setMaxSize(100, 50);
                     
                     Button finish = new Button("Submit Score?");
                     
                     
                     TextField username = new TextField ();
                     PasswordField pass = new PasswordField ();
                     username.setMaxSize(200, 38);
                     pass.setMaxSize(200, 38);

  
                                        
                     sub.setOnAction(actionEvent ->{
                           String u = username.getText();
                           String p = pass.getText();
                           
                           try
                           { 
                                 GameJDBC app = new GameJDBC();
                                 app.openJDBC();
                                 app.sendScore(u, p, "AimTrainer", time, false, false);  //All the connections get closed in here
                                 
                                 //Set player's score to 0 to begin with. Every time you play it will get reset.
                                 time = "0";
                                 //elapsedTime = 0;
                                 //elapsedSeconds = 0;
                                 //secondsDisplay = 0;

                                 
                          } 
                          catch(Exception ex) 
                          { 
                              System.err.println(ex); 
                          } 
                     });

                     
                     finish.setOnAction(actionEvent ->{
                        root.getChildren().remove(finish);
                        root.getChildren().remove(canvas);                      
                        root.add(new Label("Username: "), 0, 0);
                        root.add(username, 1, 0);
                        root.add(new Label("Password:"), 0, 1);
                        root.add(pass, 1, 1);
                        root.add(sub, 0, 2);
                     });
                     

                
                    if (targetData.hit( e.getX(), e.getY() ) && (numHit.value <30))
                    {
                        if(numHit.value == 0)
                        {
                           startTime = System.currentTimeMillis();
                           check = true;
                        }
                        
                        double x = 50 + 400 * Math.random(); 
                        double y = 50 + 350 * Math.random();
                        targetData.setCenter(x,y);
                        numHit.value++; 
                    }  
                       
                    if (numHit.value == 30 && check)
                    {
                        
                        check = false;
                        
                        gc.setFill( new Color(0, 0.85, 1.0, 1.0) );
                        gc.fillRect(0,0, 512,512);
                        gc.setFill( Color.RED );
                       
                        elapsedTime = System.currentTimeMillis() - startTime;
                        elapsedSeconds = elapsedTime / 1000;
                        secondsDisplay = elapsedSeconds % 60;
                        time = secondsDisplay + "." + elapsedTime % 1000;
                        String output = "You hit all thirty targets\n in " + time + " seconds";
                        gc.fillText( output, 25, 200);
                        gc.strokeText( output, 25, 200 );
                        
                        root.add(finish, 0, 0);
                     }       
                }
            });




        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                if(numHit.value < 30 )
                {
                  gc.setFill( Color.TEAL );
                  gc.fillRect(0,0, 512,512);
                  gc.drawImage( target,targetData.getX() - targetData.getRadius(),
                                targetData.getY() - targetData.getRadius());

                  gc.setFill( Color.RED );
                  
                  if (check)
                  {
                      long elapsedTime = System.currentTimeMillis() - startTime;
                      long elapsedSeconds = elapsedTime / 1000;
                      long secondsDisplay = elapsedSeconds % 60;
                  
                      time = " "+secondsDisplay + "." + elapsedTime % 1000;
                      gc.fillText( time, 10, 480 );
                      gc.strokeText( time, 10, 480 );
                    }
                    
                   String pointsText = numHit.value + "/30 Hit";
                   gc.fillText( pointsText, 190, 450 );
                   gc.strokeText( pointsText, 190, 450 );
                  }
            }
        }.start();

        theStage.show();
    }
}
