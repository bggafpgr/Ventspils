package solution.problem2;

import java.util.concurrent.TimeUnit;

public class Main
{

  private static volatile boolean isRunning = true;
  
  public static void main(String[] args) throws InterruptedException
  {
    Runnable countDownTask = () -> {
      for(int i=10; i > 0; i--)
      {
        System.out.println("Count down " + i );
        delay(1000);
      }
      System.out.println("Stop thread");
      isRunning = false;
    };
    
    Runnable counterTask = () -> {
      int counter = 0;
      while(isRunning)
      {
        counter++;
        delay(500);
      }
      System.out.println("Counter " + counter );
    };

    Thread t1 = new Thread( counterTask );
    Thread t2 = new Thread( countDownTask );
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
