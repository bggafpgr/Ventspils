package solution.problem4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("unchecked")
public class FineGrainLockHashSet<T> implements HashSet<T>
{
  private final int INITIAL_CAPACITY = 10;
  private final int INCREASE_STEP = 10;
  private final double THRESHOLD = 0.7;

  private int capacity = INITIAL_CAPACITY;
  private List<T>[] table;
  private AtomicInteger size = new AtomicInteger(0);
  
  // number of Lock do not increase (constant parallelism)
  private final Lock[] locks;

  public FineGrainLockHashSet()
  {
    super();
    this.table = new List[this.capacity];
    this.locks = new Lock[this.capacity];
    Arrays.setAll(this.table, i -> new ArrayList<>());
    Arrays.setAll(this.locks, i -> new ReentrantLock(true));
  }

  @Override
  public boolean add(T element)
  {
    locks[element.hashCode() % locks.length].lock();
    try
    {
      if (this.notSufficientSpace())
      {
        this.resize();
      }

      int key = element.hashCode() % table.length;
      List<T> bucket = this.table[key];

      if (bucket.contains(element))
      {
        return false;
      } 
      else
      {
        bucket.add(element);
        this.size.incrementAndGet();
        return true;
      }
    }
    finally
    {
      locks[element.hashCode() % locks.length].unlock();
    }

  }

  @Override
  public boolean remove(T element)
  {
    locks[element.hashCode() % locks.length].lock();

    try
    {
      int key = element.hashCode() % table.length;
      List<T> bucket = this.table[key];

      boolean result = bucket.remove(element);
      if (result)
      {
        this.size.decrementAndGet();
      }

      return result;
    }
    finally
    {
      locks[element.hashCode() % locks.length].unlock();
    }
  }

  @Override
  public boolean contains(T element)
  {
    locks[element.hashCode() % locks.length].lock();
    try
    {
      int key = element.hashCode() % table.length;
      List<T> bucket = this.table[key];
      return bucket.contains(element);
    }
    finally
    {
      locks[element.hashCode() % locks.length].unlock();
    }
  }

  @Override
  public synchronized int size()
  {
    return this.size.get();
  }

  private boolean notSufficientSpace()
  {
    return ((double) this.size.get()) / this.table.length >= THRESHOLD;
  }

  private void resize()
  {
    // require all locks to have exclusive access to the table
    for (Lock lock : locks)
      lock.lock();

    try
    {
      // maybe a other thread has allready done the resize
      if( notSufficientSpace() == false)
        return;
      
      int newCapacity = this.table.length + INCREASE_STEP;
      List<T>[] newTable = new List[newCapacity];
      Arrays.setAll(newTable, i -> new ArrayList<>());

      // copy elements into new table
      for (List<T> list : this.table)
      {
        for (T element : list)
        {
          int key = element.hashCode() % newTable.length;
          newTable[key].add(element);
        }
      }

      this.table = newTable;
    }
    finally
    {
      for (Lock lock : locks)
        lock.unlock();
    }
  }
}
