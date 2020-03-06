package solution.problem1;

import java.util.concurrent.TimeUnit;

public class Main
{

  public static void main(String[] args) throws InterruptedException
  {
    Runnable task = () -> {
      for(int i=0; i<3; i++)
      {
        System.out.println("Hello from " + Thread.currentThread().getName() );
        delay(1000);
      }
      System.out.println("Bye bye " + Thread.currentThread().getName() );
    };

    Thread t1 = new Thread( task );
    Thread t2 = new Thread( task );
    t1.start();
    t2.start();
    
    t1.join();
    t2.join();
    
    System.out.println("Bye bye from main");
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
