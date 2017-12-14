import java.io.*;
import java.util.Scanner;

public class GameValueScanner
{
  public static void main(String[] args) throws IOException 
  {
  	Scanner currentScanner = null;
    
    
  	String[] argumentsForTest = new String[5];
  	double currentValue;
  	int gameCounter = 0; //counts how many games happened so far
  	File file = new File("values.txt");
  	try
  	{
  		currentScanner = new Scanner(file);

  		while(currentScanner.hasNextDouble())
  	    {

  	      for (int i = 0; i<4; i++)
  	      {
            currentValue = currentScanner.nextDouble();
            argumentsForTest[i] = Double.toString(currentValue);
  	      }
  	      gameCounter++;
  	      argumentsForTest[4] = Integer.toString(gameCounter);
  	      //System.out.println( currentScanner.nextDouble() ); 
          long start_time = System.nanoTime();
  	      SimpleGamePlayer.main(argumentsForTest);
          long end_time = System.nanoTime();
          double difference = (end_time - start_time) / 1e6;
          System.out.println("Time taken for the round is: " + difference + "\n");

  	    }
  	    //currentScanner.close();
  	}catch (FileNotFoundException e1) {
            e1.printStackTrace();
    }
  }//main
}//class