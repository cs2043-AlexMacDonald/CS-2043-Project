package myPackage;

import java.sql.*;
import javax.sql.*;

public class GameJDBC
{
   Connection con = null;
   
    
   public void openJDBC() 
   {
      try
      {
         Class.forName("com.mysql.jdbc.Driver"); 
         con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinegamesubscription?useSSL=false", "root", ""); // would need to put password in
      }
      catch(Exception e)
      {
         System.err.println("error: " + e);
      }
   
   }
   
   public int getUserID(String username, String password)
   {
      String query = "SELECT user_id FROM users WHERE username = ? AND password = ?";
      int u_id = -1;
      PreparedStatement s = null;
      ResultSet r = null;
      
      try
      {
         s = con.prepareStatement(query);
         
         s.setString(1, username);
         s.setString(2, password);
         
         r = s.executeQuery();
         
         while(r.next())
         {
            u_id = Integer.parseInt(r.getString("user_id"));
         }          
      }
      catch(Exception e)
      {
         System.err.println("error: " + e);
         System.out.println("Something went wrong when getting the user ID");
      }
      finally
      {
         if (r != null)
         {
            try 
            {
               r.close();
            } 
            catch (Exception e) { /* ignored */}
          }
          
          if (s != null) 
          {
             try 
             {
               s.close();
             } 
             catch (Exception e) { /* ignored */}
          }

      }
            
      return u_id;
   }
   
   public double highscore(String user_id, String game_id)
   {
      String query = "SELECT * FROM score WHERE user_id = ? AND game_id = ?";
      double highscore = 0;
      ResultSet r = null;
      PreparedStatement s = null;
      
      try
      {
         s = con.prepareStatement(query);
         
         s.setString(1, user_id);
         s.setString(2, game_id);
         
         r = s.executeQuery(); 
         
         
         while(r.next())
         {
           highscore = Double.parseDouble(r.getString("score")); 
         }
      }
      catch(Exception e)
      {
         System.err.println("error: " + e);
         System.out.println("Something went wrong when checking the highscore");
      }
      finally
      {
         if (r != null)
         {
            try 
            {
               r.close();
            } 
            catch (Exception e) { /* ignored */}
          }
          
          if (s != null) 
          {
             try 
             {
               s.close();
             } 
             catch (Exception e) { /* ignored */}
          }
      }

      
      return highscore;
   }
   
   
   public boolean hasScore(String user_id, String game_id)
   {
      String query = "SELECT * FROM score WHERE user_id = ? AND game_id = ?";
      boolean inTable = false;
      PreparedStatement s = null;
      ResultSet r = null;
      
      try
      {
         s = con.prepareStatement(query);
         
         s.setString(1, user_id);
         s.setString(2, game_id);
         
         r = s.executeQuery(); 
         
         if(r.next() != false)
         {
            inTable = true;
         }
      }
      catch(Exception e)
      {
         System.err.println("error: " + e);
         System.out.println("Something went wrong when checking if there was a score");
      }
      finally
      {
         if (r != null)
         {
            try 
            {
               r.close();
            } 
            catch (Exception e) { /* ignored */}
          }
          
          if (s != null) 
          {
             try 
             {
               s.close();
             } 
             catch (Exception e) { /* ignored */}
          }
      }
      
      return inTable;
   }
   
   
   public void sendScore(String username, String password, String game_id, String score, boolean intScore, boolean wantHighScore)
   {
      String query = "";      
      String u_id = Integer.toString(getUserID(username, password));

      boolean hasAScore = hasScore(u_id, game_id);
      boolean update = false;
      PreparedStatement s = null;
 
      if(!u_id.equals("-1"))
      {
         if(hasAScore)
         {
            double newScore = Double.parseDouble(score);
            double oldScore = highscore(u_id, game_id);
   
            
            //To determine whether the score to be sent is a double or an int value
            if(intScore)
            {
               newScore = (int)newScore;
               oldScore = (int)oldScore;
            }
            
            
            if(wantHighScore && newScore > oldScore)
            {
               query = "UPDATE score SET score = ? WHERE user_id = ? AND game_id = ?";
               update = true;
            }
            else if(!wantHighScore && newScore < oldScore)
            {
               query = "UPDATE score SET score = ? WHERE user_id = ? AND game_id = ?";
               update = true;
            }
            else
            {  
               return; //don't do anything if the new score is less
            }
         }
         else
         {
            query = "INSERT INTO score (user_id, game_id, score) VALUES(?, ?, ?)";
         } 
      }        
      
   
      try
      {
         if(!u_id.equals("-1"))
         {
            s = con.prepareStatement(query);
            
            if(!update)
            {
               s.setString(1, u_id);
               s.setString(2, game_id);
               s.setString(3, score);
            }
            else
            {
               s.setString(1, score);
               s.setString(2, u_id);  
               s.setString(3, game_id);
            }
            
            s.executeUpdate(); 
         }
      }
      catch(Exception e)
      {
         System.err.println("error: " + e);
         System.out.println("Something went wrong while sending the score");
      }
      finally
      {
          if (s != null) 
          {
             try 
             {
               s.close();
             } 
             catch (Exception e) { /* ignored */}
          }
          
          if (con != null) 
          {
              try 
              {
                  con.close();
              } 
              catch (Exception e) { /* ignored */}
          }
      }
   }
   
}