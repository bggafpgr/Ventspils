package solution.problem5;

import java.util.concurrent.ThreadLocalRandom;

public class FindNonce
{

  public static void main(String[] args)
  {
    String value = "Hello Ventspils";
    
    System.out.println("Hashcode of >>Hello Ventspils<< : " + value.hashCode() );
    
    long start = System.currentTimeMillis();
    while(true)
    {
      String candidate = "Hello Ventspils" + ThreadLocalRandom.current().nextLong();
      if( candidate.hashCode() >= 0 &&  candidate.hashCode() < 10 )
      {
        String hashStr = String.format ("%010d",  candidate.hashCode() );
        System.out.println("Candidate: " + candidate + " -> " + hashStr );
        break;
      }
    }
    System.out.println("Duration: " + (System.currentTimeMillis()- start) + " [ms]");
  }
}
