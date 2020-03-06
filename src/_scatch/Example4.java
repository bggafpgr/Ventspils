package _scatch;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;


public class Example4
{  
  static AtomicInteger counter = new AtomicInteger(0);
  
  @SuppressWarnings("serial")
  static class CountTask extends RecursiveTask<Integer>
  {
    private final BigInteger[] array;
    private final int start;
    private final int end;
    
    CountTask(BigInteger[] array, int start, int end)
    {
      this.array = array;
      this.start = start;
      this.end = end;
      counter.incrementAndGet();
    }

    @Override
    protected Integer compute()
    {
      // Working
      if( this.end - this.start < 1000 )
      {
        int count = 0;
        for(int i = this.start; i < this.end; i++)
        {
          if( this.array[i].isProbablePrime(20) )
            count++;
        }
        return count;
      }
      else
      {
        // Splitting
        int middle = this.start + (this.end - this.start)/2;
        //int middle = (this.start + this.end)/2;
        CountTask left  = new CountTask(this.array, this.start, middle);
        CountTask right = new CountTask(this.array, middle, this.end);
        invokeAll( left, right );
        
        // Combining
        return left.join() + right.join();
      }
    }
    
  }
  
  
  public static void main(String[] args) throws Exception
  {
    final int LEN = 5_000_000;
    BigInteger[] bIntArray = new BigInteger[LEN];
    
    Arrays.parallelSetAll(bIntArray, i -> BigInteger.valueOf(i+1) );
    
    long start = System.currentTimeMillis();
    CountTask rootTask = new CountTask(bIntArray, 0, bIntArray.length );
    ForkJoinPool.commonPool().invoke(rootTask);
    System.out.println("Result " + rootTask.join() );
    long end = System.currentTimeMillis();
    System.out.println("Duration " + (end - start));
    
    System.out.println("Number of task objects " + counter );
    
    System.out.println("done");
  }

}
