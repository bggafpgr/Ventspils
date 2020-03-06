package solution.problem8;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import solution.problem8.SudokuBoard.FieldPosition;
import solution.problem8.SudokuBoard.FieldValue;

public class SudokuSolverForkJoin
{
  @SuppressWarnings("serial")
  private static class SearchTask extends RecursiveTask<List<SudokuBoard>>
  {
    private final SudokuBoard board;
    
    SearchTask(SudokuBoard board)
    {
      this.board = board;
    }

    @Override
    protected List<SudokuBoard> compute()
    {
      if( board.isComplete() )
      {
        return List.of(board);
      }
      
      FieldPosition nextFreeField = board.getFreeFieldPositionWithLeastCandidates();
      Set<FieldValue> candidates = board.getFieldValueCandidates(nextFreeField.row, nextFreeField.col);
      
      if( candidates.isEmpty() )
      {
        return Collections.emptyList();
      }
      
      List<SearchTask> tasks = new ArrayList<>();
      for( FieldValue field : candidates )
      {
         SudokuBoard newBoard = SudokuBoard.newCopy(board);
         newBoard.setFieldValue(nextFreeField.row, nextFreeField.col, field );
         newBoard.pack();
         tasks.add( new SearchTask(newBoard) );
      }
      invokeAll(tasks);
     
      List<SudokuBoard> resultList = new ArrayList<>();
      tasks.forEach( task -> {
          resultList.addAll( task.join() );
      } );
      
      return resultList;
    }
    
  }
  

  public static void main(String[] args) throws Exception
  {
    File file = new File("board10.txt");
    SudokuBoard board = SudokuBoard.loadBoard(file);
    board.print();
    
    System.out.println("Solve game: ");
    
    for(int i=0; i < 10; i++)
    {
      long start = System.currentTimeMillis();
      SearchTask rootTask = new SearchTask(board);
      ForkJoinPool.commonPool().execute(rootTask);
      List<SudokuBoard> boards = rootTask.join();
      System.out.println("Duration " + (System.currentTimeMillis() - start) + " [ms]");
      System.out.println("Found " + boards.size() );
    }
    
    System.out.println("done with ForkJoin");
  }
}
