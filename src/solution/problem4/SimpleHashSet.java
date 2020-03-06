package solution.problem4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SuppressWarnings("unchecked")
public class SimpleHashSet<T> implements HashSet<T>
{
  private final int INITIAL_CAPACITY = 10;
  private final int INCREASE_STEP = 10;
  private final double THRESHOLD = 0.7;

  private int capacity = INITIAL_CAPACITY;
  private List<T>[] table;
  private int size = 0;
  
  
  public SimpleHashSet()
  {
    super();
    this.table =  new List[this.capacity];
    Arrays.setAll( this.table, i -> new ArrayList<>() );
  }
  
  
  @Override
  public boolean add(T element)
  {
    if( this.notSufficientSpace() )
    {
      this.resize();
    }
    
    int key = element.hashCode()%table.length;
    List<T> bucket = this.table[key];
    
    if( bucket.contains(element) )
    {
      return false;
    }
    else
    {
      bucket.add(element);
      this.size++;
      return true;
    }
  }

  @Override
  public boolean remove(T element)
  {
      int key = element.hashCode()%table.length;
      List<T> bucket = this.table[key];
      
      boolean  result = bucket.remove(element);
      if( result )
      {
        this.size--;
      }
      
      return result;
  }

  @Override
  public boolean contains(T element)
  {
    int key = element.hashCode()%table.length;
    List<T> bucket = this.table[key];
    return bucket.contains(element);
  }

  @Override
  public int size()
  {
    return this.size;
  }
  
  private boolean notSufficientSpace()
  {
    return ((double) this.size)/this.table.length >= THRESHOLD;
  }
  
  private void resize()
  {
      int newCapacity = this.table.length + INCREASE_STEP; 
      List<T>[] newTable = new List[newCapacity];
      Arrays.setAll( newTable, i -> new ArrayList<>() );
      
      // copy elements into new table
      for(List<T> list : this.table )
      {
        for(T element : list)
        {
          int key = element.hashCode()%newTable.length;
          newTable[key].add(element);
        }
      }
      
      this.table = newTable;
  }
  
}
