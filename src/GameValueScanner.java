import java.io.*;
import java.util.Scanner;

public class GameValueScanner
{
  public static void main(String[] args) throws IOException 
  {
  	Scanner currentScanner = null;
  	String[] argumentsForTest = new String[4];
  	double currentValue;
  	int gameCounter = 0; //counts how many games happened so far
  	File file = new File("values.txt");
  	try
  	{
  		currentScanner = new Scanner(file);

  		while(currentScanner.hasNextDouble())
  	    {

  	      for (int i = 0; i<3; i++)
  	      {
            currentValue = currentScanner.nextDouble();
            argumentsForTest[i] = Double.toString(currentValue);
  	      }
  	      gameCounter++;
  	      argumentsForTest[3] = Integer.toString(gameCounter);
  	      //System.out.println( currentScanner.nextDouble() );   
  	      SimpleGamePlayer.main(argumentsForTest);

  	    }
  	    //currentScanner.close();
  	}catch (FileNotFoundException e1) {
            e1.printStackTrace();
    }
  }//main
}//class