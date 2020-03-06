package _scatch;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import _scatch.Example1.CountTask;


public class Example2
{  
  static class CountTask implements Callable<Integer>
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
    public Integer call()
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
      return count;
    }
  }
  
  
  public static void main(String[] args) throws Exception
  {
    final int LEN = 5_000_000;
    BigInteger[] bIntArray = new BigInteger[LEN];
    
    Arrays.parallelSetAll(bIntArray, i -> BigInteger.valueOf(i+1) );
    
    int numOfProc = Runtime.getRuntime().availableProcessors();
    System.out.println("Number of Processor " + numOfProc );
    
    System.out.println("Start counting");
    long startTime = System.currentTimeMillis();
    
    int numOfWorkers = Math.max(1, numOfProc-1 );
    ExecutorService executor = Executors.newFixedThreadPool( numOfWorkers );
    
    CountTask task1 = new CountTask( bIntArray, 0, LEN/2);
    CountTask task2 = new CountTask( bIntArray, LEN/2, LEN );
    
    Future<Integer> future1 = executor.submit(task1);
    Future<Integer> future2 = executor.submit(task2);


    Integer res1 = future1.get();
    Integer res2 = future2.get();
    int result = res1 + res2;
    
    long endTime = System.currentTimeMillis();
    System.out.println("Duration " + (endTime - startTime) + " [ms]");
    System.out.println("Sum " + result );

    executor.shutdown();
    
    System.out.println("done");
  }

}
