package solution.problem8;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import solution.problem8.SudokuBoard.FieldPosition;
import solution.problem8.SudokuBoard.FieldValue;

public class SudokuSolver
{

  public static void main(String[] args) throws Exception
  {
    File file = new File("board08.txt");
    SudokuBoard board = SudokuBoard.loadBoard(file);
    board.print();
    
    System.out.println("Solve game: ");
    for(int i=0; i<10; i++)
    {
      long start = System.currentTimeMillis();
      List<SudokuBoard> boards = solve(board);
      System.out.println("Duration " + (System.currentTimeMillis() - start) + " [ms]");
      System.out.println("Found " + boards.size() );
      //boards.forEach( b -> b.print() );
    }
    System.out.println("done sequential");
  }

  
  public static List<SudokuBoard> solve(SudokuBoard board)
  {  
     if( board.isComplete() )
     {
       return List.of(board);
     }
     
     FieldPosition nextFreeField = board.getFreeFieldPosition();
     Set<FieldValue> candidates = board.getFieldValueCandidates(nextFreeField.row, nextFreeField.col);
     
     if( candidates.isEmpty() )
     {
       return Collections.emptyList();
     }
     
     List<SudokuBoard> resultList = new ArrayList<>();
     for( FieldValue field : candidates )
     {
        SudokuBoard newBoard = SudokuBoard.newCopy(board);
        newBoard.setFieldValue(nextFreeField.row, nextFreeField.col, field );
        newBoard.pack();
        resultList.addAll( solve(newBoard) );
     }
     
     return resultList;
   }
}
