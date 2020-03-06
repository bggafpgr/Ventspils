package solution.problem6;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main
{

  public static void main(String[] args)
  {
    BlockingQueue<Optional<BigInteger>> queue1 = new ArrayBlockingQueue<>(100);
    BlockingQueue<Optional<BigInteger>> queue2 = new ArrayBlockingQueue<>(100);

    Runnable producer = () -> {
      try
      {
        for (int i = 1_000_000; i <= 2_000_000; i++)
        {
          BigInteger bInt = BigInteger.valueOf(i);
          queue1.put(Optional.of(bInt));
        }
        
        queue1.put( Optional.empty() );
        queue1.put( Optional.empty() );
      }
      catch (InterruptedException exce)
      {
        exce.printStackTrace();
      }
    };

    Runnable filter = () -> {
      try
      {
        while(true)
        {
          Optional<BigInteger> element = queue1.take();
          if( element.isPresent() )
          {
             if( element.get().isProbablePrime(100) )
             {
               // is prim
               queue2.put(element);
             }
             
          }
          else
          {
            // is empty
            queue2.put(element);
            break;
          }
        }
      }
      catch (InterruptedException exce)
      {
        exce.printStackTrace();
      }
    };

    Runnable consumer = () -> {
      try
      {
        int primCount = 0;
        int stopCount = 0;
        while( true )
        {
          Optional<BigInteger> element = queue2.take();
          if( element.isPresent() )
          {
            primCount++;
          }
          else
          {
            stopCount++;
          }
          
          if( stopCount == 2 )
            break;
        }
        
        System.out.println("number of prims " + primCount );
      }
      catch (InterruptedException exce)
      {
        exce.printStackTrace();
      }
    };

    System.out.println("Start counting");
    
    Thread producerThread = new Thread( producer, "Producer" );
    Thread filter1Thread = new Thread( filter, "Filter 1" );
    Thread filter2Thread = new Thread( filter, "Filter 2" );
    Thread consumerThread = new Thread( consumer, "Consumer" );
    
    producerThread.start();
    filter1Thread.start();
    filter2Thread.start();
    consumerThread.start();
  }

}
