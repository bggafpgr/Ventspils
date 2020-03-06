package _scatch;

import java.math.BigInteger;
import java.util.Arrays;

public class Example5
{

  public static void main(String[] args)
  {
    final int LEN = 5_000_000;
    BigInteger[] bIntArray = new BigInteger[LEN];
    
    Arrays.parallelSetAll(bIntArray, i -> BigInteger.valueOf(i+1) );
    
    System.out.println("Start counting");
    
    long start = System.currentTimeMillis();
    long count = Arrays.stream(bIntArray)
          .parallel()
          .filter( bI -> bI.isProbablePrime(20) )
          .count();
    long end = System.currentTimeMillis();
    
    System.out.println("Result " + count );
    System.out.println("Duration " + (end - start) + " [ms]");
  }

}
