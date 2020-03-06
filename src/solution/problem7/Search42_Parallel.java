package solution.problem7;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class Search42_Parallel
{
  @SuppressWarnings("serial")
  private static class Search42 extends RecursiveTask<Integer>
  {
    private final int[] array;
    private final int start;
    private final int end;
    
    Search42(int[] array, int start, int end)
    {
      this.array = array;
      this.start = start;
      this.end = end;
    }

    @Override
    protected Integer compute()
    {
      if( end - start < 100 )
      {
        int count = 0;
        for(int i = start; i < end; i++)
        {
          if( array[i] == 42 )
            count++;
        }
        return count;
      }
      else
      {
        int mid = (start + end)/2;
        Search42 leftTask = new Search42(array, start, mid);
        Search42 rightTask = new Search42(array, mid, end);
        invokeAll(leftTask, rightTask);
        
        return leftTask.join() + rightTask.join();
      }
    }
    
  }
  

  public static void main(String[] args)
  {
    int[] array = new int[100_000];
    Arrays.parallelSetAll(array, i -> ThreadLocalRandom.current().nextInt(1_000));

    Search42 root = new Search42(array, 0, array.length);
    ForkJoinPool.commonPool().execute(root);
    
    System.out.println("Count 42 : " + root.join() );
  }
}
