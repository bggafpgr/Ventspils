package _scatch;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


public class Example1
{
  static AtomicInteger result = new AtomicInteger(0);
  
  static class CountTask implements Runnable
  {
    private final BigInteger[] array; 
    private final int start; 
    private final int end; 
    
    CountTask(BigInteger[] array, int start, int end)
    {
      this.array = array;
      this.start = start;
      this.end = end;
    }

    @Override
    public void run()
    {
      int count = 0;
      for(int i=this.start; i < this.end; i++)
      {
        if( this.array[i].isProbablePrime(20) )
        {
          count++;
        }
      }
      
      System.out.println("Count " + count + "  : " + Thread.currentThread().getName() );
      result.addAndGet(count);
    }
  }
  
  
  public static void main(String[] args) throws InterruptedException
  {
    final int LEN = 5_000_000;
    BigInteger[] bIntArray = new BigInteger[LEN];
    
    Arrays.parallelSetAll(bIntArray, i -> BigInteger.valueOf(i+1) );
    
    System.out.println("Start counting");
    long startTime = System.currentTimeMillis();
    
    CountTask task1 = new CountTask( bIntArray, 0, LEN/2);
    CountTask task2 = new CountTask( bIntArray, LEN/2, LEN );
    Thread th1 = new Thread( task1, "Thread for task 1");
    Thread th2 = new Thread( task2, "Thread for task 2");
    
    th1.start();
    th2.start();
    
    th1.join();
    th2.join();
    
    long endTime = System.currentTimeMillis();
    System.out.println("Duration " + (endTime - startTime) + " [ms]");
    System.out.println("Sum " + result );

   
    System.out.println("done");
  }

}
