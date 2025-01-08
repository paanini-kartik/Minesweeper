/*

This is a class for the inner board. It holds the number values and mines attached to the square, and its functionality pertains to setting these values and reading them.

Programmed by: Paanini Kartik

*/

import java.util.*;
import java.lang.Math;

class InnerBoard
{

  // Fields

  private int dimension; // Holds dimensions of the board
  private char[][] board; // Stores the inner board

  // Contructor

  /*
  * Constructor used to initialize object with a given dimension. The dimension additionally initializes the board field.
  * This counts as a setter for the marking scheme.
  * @param dimension The selected dimension of the board.
  */
  
  public InnerBoard(int dimension)
  {
    this.dimension = dimension;
    board = new char[dimension][dimension]; // Sets board with rows and columns equivalent to dimensions
  }

  // Methods

  /**
  * Returns the dimension.
  * Return method Paanini Kartik #1, getter.
  * @return int value holding the dimensions of the board
  */

  public int getDimension()
  {
    return dimension;
  }

  /**
  * This method returns the inner board.
  * Return method Paanini Kartik #2, getter.
  * @return A 2D char array containing the values of the inner board.
  */

  public char[][] getBoard()
  {
    return board;
  } // End getBoard

  /**
  * Returns the number of mines based on the size of the board.
  * Return method Paanini Kartik #3.
  * @return int value holding the number of mines.
  */

  public int getMineCount()
  {
    
    final double PROPORTIONALITY_CONSTANT = 0.10 + (0.02*Math.sqrt(dimension)); // Proportionality constant dictating how many mines are placed on the board; it is largely arbitrary
    
    return (int) (Math.pow(dimension, 2)*(PROPORTIONALITY_CONSTANT)); // Sets the number of mines, according to the dimension and proportionality constant
    
  }
  
  /**
  * Method to assign values to the board. This is done after the first user selection.
  * Void method Paanini Kartik #1.
  * Method with parameters Paanini Kartik #1.
  * @param rowCoordinate The row coordinate of the square that the user selected, used to determine which squares cannot be a mine.
  * @param colCoordinate The col coordinate of the square that the user selected, used to determine which squares cannot be a mine.
  */
  
  public void initializeBoard(int rowCoordinate, int colCoordinate)
  {

    // Decide locations of mines and place them on board

    placeCharacters(rowCoordinate, colCoordinate);

    // Determine the number of mines surrounding each square and place numbers on tiles accordingly

    placeCharacters();
    
  } // End initializeBoard

  /**
  * This method sets the mines in the board.
  * Void method Paanini Kartik #2.
  * Method with parameters Paanini Kartik #2.
  * @param surroundingSquareCoordinates The coordinates of the surrounding squares of the first selected square.
  * @param rowCoordinate The row coordinate of the first selected square.
  * @param colCoordinate The col coordinate of the first selected square.
  */

  public void placeCharacters(int rowCoordinate, int colCoordinate)
  {
    
    Random rand = new Random(); // Initializing a random number generator

    // Determine the coordinates of the square surrounding the first selected square (used to be added on to the list of coordinates that cannot be a mine)

    int[][] surroundingSquareCoordinates = surroundingSquareCoordinates(rowCoordinate, colCoordinate); // First time it's called
    
    ArrayList<String> impossibleMineCoordinates = new ArrayList<>(); // Stores the rowcol coordinates of squares that cannot be a mine

    // Adding row and col values from surroundingSquareCoordinates to impossibleMineCoordinates

    for (int i = 0; i < surroundingSquareCoordinates.length; i++)
    {
      impossibleMineCoordinates.add(surroundingSquareCoordinates[i][0] + "," + surroundingSquareCoordinates[i][1]);
    }

    impossibleMineCoordinates.add(rowCoordinate + "," + colCoordinate); // Additionally add the first selected square to the list of impossible coordinates

    // Creating ArrayList for storing randomly selected mine coordinates

    ArrayList<Integer> mineRowCoordinates = new ArrayList<>();
    ArrayList<Integer> mineColCoordinates = new ArrayList<>();

    // Randomly select coordinates for the mines

    String mineCoordinatesString; // Stores the coordinates of the mines as a string
    
    int mineRowCoordinate; // Stores the row coordinate of the mine
    int mineColCoordinate; // Stores the col coordinate of the mine
    
    for (int i = 0; i < getMineCount(); i++)
    {

      do // Loops while the randomly selected coordinates are invalid
      {

        // Select mine coordinates

        mineRowCoordinate = rand.nextInt(board.length);
        mineColCoordinate = rand.nextInt(board[0].length);

        // Combine in variable to compare against impossibleMineCoordinates ArrayList elements

        mineCoordinatesString = mineRowCoordinate + "," + mineColCoordinate;

      } while (impossibleMineCoordinates.contains(mineCoordinatesString));

      // Add verified coordinates to ArrayLists

      mineRowCoordinates.add(mineRowCoordinate);
      mineColCoordinates.add(mineColCoordinate);
      
    }

    // Places mines on board

    for (int i = 0; i < mineRowCoordinates.size(); i++)
    {
      board[mineRowCoordinates.get(i)][mineColCoordinates.get(i)] = 'M';
    }

  } // End setMines

  /**
  * This method sets the numbers on the board.
  * Overloaded method Paanini Kartik #2.
  */

  public void placeCharacters()
  {

    // Loop through each cell of the board, with the exception of mines, and determine how many mines are surrounding it

    for (int i = 0; i < board.length; i++)
    {
      for (int j = 0; j < board[0].length; j++)
      {

        // Conditional to exclude mines

        if (board[i][j] != 'M')
        {

          // Assign number to tile
          
          int numMinesTouching = numberOfMines(i, j);
          board[i][j] = (char)(numMinesTouching+48); // Convert to char (48 is ASCII value of 0)
          
        }
        else
        {
          // Do nothing, as the tile is a mine
        }
        
      }
    }
    
  } // End setNumbers

  /**
  * Method to get the values of the surrounding tiles.
  * Return method Paanini Kartik #4.
  * @param surroundingSquareCoordinates Stores the coordinates of the surrounding squares.
  * @return A Character ArrayList holding the values of the surrounding squares.
  */

  public char[] surroundingSquareValues(int[][] surroundingSquareCoordinates)
  {
    
    ArrayList<Character> surroundingValues = new ArrayList<Character>(); // Initializing the ArrayList to store the surrounding values
    
    for (int i = 0; i < surroundingSquareCoordinates.length; i++) // Checking the values of all the surrounding squares
    {
      surroundingValues.add(board[surroundingSquareCoordinates[i][0]][surroundingSquareCoordinates[i][1]]); // Adding the values of the surrounding squares to the ArrayList
    }

    // Convert ArrayList to array (needed for numberOfMines method)

    char[] surroundingValuesArray = new char[surroundingValues.size()]; // Converting the ArrayList to a char array

    for (int i = 0; i < surroundingValues.size(); i++)
    {
      surroundingValuesArray[i] = surroundingValues.get(i).charValue(); // Adding the values of the surrounding squares from the ArrayList to the char array
    }
  
    // Returning values

    return surroundingValuesArray;

  } // End surroundingSquareValues

  /**
  * Method to get the coordinates of all the squares surrounded an inputted square.
  * @param rowCoordinate The row coordinate of the inputted square.
  * @param colCoordinate The col coordinate of the inputted square.
  * @return An int array holding the coordinates of the surrounding squares.
  */

  public int[][] surroundingSquareCoordinates(int rowCoordinate, int colCoordinate)
  {

    int[][] surroundingSquareCoordinates; // Initializing the 2D array to store the coordinates

    ArrayList<Integer> rowCoordinates = new ArrayList<Integer>(); // Stores the row coordinates of the surrounding squares
    ArrayList<Integer> colCoordinates = new ArrayList<Integer>(); // Stores the col coordinates of the surrounding squares

    int[][] surroundingSquareOperator = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}}; // Stores the operations to get to the possible surrounding squares
    
    // Loop through all potential surrounding squares
    
    for (int i = 0; i < surroundingSquareOperator.length; i++)
    {
      
      // Add coordinates

      rowCoordinates.add(rowCoordinate + surroundingSquareOperator[i][0]);
      colCoordinates.add(colCoordinate + surroundingSquareOperator[i][1]);
      
      if (rowCoordinates.get(rowCoordinates.size()-1) < 0 || colCoordinates.get(colCoordinates.size()-1) < 0 || rowCoordinates.get(rowCoordinates.size()-1) > dimension-1 || colCoordinates.get(colCoordinates.size()-1) > dimension-1) // Checks if the surrounding square is out of bounds
      {
        
        // Remove invalid coordinates from ArrayLists

        rowCoordinates.remove(rowCoordinates.size()-1);
        colCoordinates.remove(colCoordinates.size()-1);

      }
      
    }

    // Add the row and col coordinates to the 2D array

    surroundingSquareCoordinates = new int[rowCoordinates.size()][2];

    for (int i = 0; i < surroundingSquareCoordinates.length; i++)
    {
      surroundingSquareCoordinates[i][0] = rowCoordinates.get(i);
      surroundingSquareCoordinates[i][1] = colCoordinates.get(i);
    }

    // Return coordinates

    return surroundingSquareCoordinates;
    
  } // End surroundingSquareCoordinates

  /**
  * Method to get the number of mines surrounding a square.
  * @param rowCoordinate The row coordinate of the square that the user selected.
  * @param colCoordinate The col coordinate of the square that the user selected.
  * @return The number of mines surrounding the square.
  */

  public int numberOfMines(int rowCoordinate, int colCoordinate)
  {
    
    // Find the surrounding values of the cell

    char[] surroundingSquareValues = surroundingSquareValues(surroundingSquareCoordinates(rowCoordinate, colCoordinate));

    // Find how many of them are mines

    int numMines = 0;

    for (char i : surroundingSquareValues)
    {
      if (i == 'M')
      {
        numMines++;
      }
    }

    // Return number of mines

    return numMines;
    
  } // End numberOfMines


  /**
  * Method to check whether a tile is a mine or not.
  * @param rowCoordinate The row-coordinate of the tile (position from left to right).
  * @param colCoordinate The col-coordinate of the tile (position from top to bottom).
  * @return A boolean value indicating whether the tile is a mine or not.
  */
  
  public boolean isMine (int rowCoordinate, int colCoordinate)
  {
    
    if (board[rowCoordinate][colCoordinate] == 'M') // Condition for if the tile is a mine
    {
      return true;
    }
    else // Condition for if the tile is not a mine
    {
      return false;
    }
    
  } // End isMine

}