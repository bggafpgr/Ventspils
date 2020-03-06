package solution.problem9;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;


public class Main
{

  public static void main(String[] args) throws IOException
  {
    File infile = new File("Alice.jpg");
    
    long globalStart = System.currentTimeMillis();
    
    System.out.println("load image an convert it to gray scale");
    long start = System.currentTimeMillis();
    int[][] image = IOUtils.loadImageAndConvertToGrayScale(infile);
    long end = System.currentTimeMillis();
    System.out.println("end loading " + (end - start) + " [ms]" );
   
    
    System.out.println("start process");
    start = System.currentTimeMillis();
    int[][] newImage = new int[image.length][image[0].length];
    
    //for(int i=1; i< image.length-1;i++)
    IntStream.range(1, image.length-1).parallel().forEach( i-> 
    {
      for(int j=1; j< image[0].length-1;j++)
      {
         double grad = Sobel.meanGradiant(image, i, j);
         int grayValue = Math.min((int) grad,255);
         newImage[i][j] = grayValue < 150 ? 0 : grayValue;
      }
    }
    );
    
    System.out.println("end process " + (System.currentTimeMillis() - start) + " [ms]" );
  
    
    
    System.out.println("store image");
    start = System.currentTimeMillis();
    File outfile = new File("EdgePicture.jpg");
    IOUtils.storeImage(outfile, newImage);
    System.out.println("end store " + (System.currentTimeMillis() - start) + " [ms]" );
    
    
    System.out.println("done in " + (System.currentTimeMillis() - globalStart) + " [ms]");
  }

}
