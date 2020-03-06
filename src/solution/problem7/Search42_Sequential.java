package solution.problem7;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Search42_Sequential
{

  public static void main(String[] args)
  {
    int[] array = new int[100_000];
    Arrays.parallelSetAll(array, i -> ThreadLocalRandom.current().nextInt(1_000));

    System.out.println("Count 42 : " + count42(array, 0, array.length) );
  }

  private static int count42(int[] array, int start, int end)
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
      int left = count42(array, start, mid);
      int right = count42(array, mid, end);
      
      return left + right;
    }
  }
  
}
