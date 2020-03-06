package solution.problem3;

import java.util.concurrent.TimeUnit;

public class Main
{
  
  private static volatile boolean sayPing = true;
  private static volatile boolean sayPong = false;
  
  public static void main(String[] args) throws InterruptedException
  {
    Runnable pingTask = () -> {
      for(int i=0; i < 3; i++)
      {
        while( !sayPing );
        // sayPing is true
        
        delay(500);
        System.out.println("ping");
        delay(500);
        
        sayPing = false;
        sayPong = true;
      }
    };
    
    Runnable pongTask = () -> {
      for(int i=0; i < 3; i++)
      {
        while( !sayPong );
        // sayPong is true
        
        delay(500);
        System.out.println("pong");
        delay(500);
        
        sayPong = false;
        sayPing = true;
      }
    };


    Thread t1 = new Thread( pongTask );
    Thread t2 = new Thread( pingTask );
    t1.start();
    t2.start();

  }
  
  private static void delay(int milliseconds)
  {
    try
    {
      TimeUnit.MILLISECONDS.sleep(milliseconds);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }

}
