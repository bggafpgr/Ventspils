package _scatch;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Example3
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
    
    int numOfWorkers = Math.max(1, 4*numOfProc );
    ExecutorService executor = Executors.newFixedThreadPool( numOfWorkers );
    
    int chunkSize = LEN/numOfWorkers;
    List<Future<Integer>> futures = new ArrayList<>();
    for(int i=0; i < numOfWorkers; i++)
    {
      int start = i*chunkSize;
      int end = i < numOfWorkers - 1? (i+1)*chunkSize : LEN;
      CountTask task = new CountTask( bIntArray, start, end);
      futures.add( executor.submit(task) );
    }
 
    int result = 0;
    for( Future<Integer> future : futures )
    {
      result += future.get();
    }
    
    long endTime = System.currentTimeMillis();
    System.out.println("Duration " + (endTime - startTime) + " [ms]");
    System.out.println("Sum " + result );

    executor.shutdown();
    
    System.out.println("done");
  }

}
