package solution.problem5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class FindNonce_Parallel
{

  public static void main(String[] args)
  {
    Callable<String> task = () -> {
      while(Thread.currentThread().isInterrupted() == false )
      {
        String candidate = "Hello Ventspils" + ThreadLocalRandom.current().nextLong();
        if( candidate.hashCode() >= 0 && candidate.hashCode() < 10 )
        {
          return candidate;
        }
      }
      System.out.println("Task stopped");
      return (String) null;
    };
    
    int parallel = Math.max(1, Runtime.getRuntime().availableProcessors()-1);
    List<Callable<String>> tasks = new ArrayList<>();
    for (int i = 0; i < parallel; i++)
    {
      tasks.add( task );
    }
    
    ExecutorService executor = Executors.newFixedThreadPool(parallel);
    CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
    tasks.forEach(completionService::submit);

    try
    {
       String result = completionService.take().get();
       System.out.println( result + " -> " + result.hashCode() );
    } 
    catch (InterruptedException | ExecutionException exce)
    {
      exce.printStackTrace();
    }
    finally 
    {
      executor.shutdownNow();
    }
  }
}
