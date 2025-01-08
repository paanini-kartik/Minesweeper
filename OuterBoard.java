/*
This is a class for the outer workings of the minesweeper/what will be displayed to the user.
Programmed by: Emmett Burns
*/

class OuterBoard
{

  //**Fields**
  private char[][] board;

  
  //**Methods**

  /** 
   * Sets the dimensions of the board. (Parameter Method 3)
   * @param dimension The number to be use as the length of each side
   */
  public void setBoard(int dimension)
  {
    board = new char[dimension][dimension];

    //Set each board item to the square character
    for(int row = 0; row < board.length; row++)
    {
      for (int col = 0; col < board[row].length; col++)
      {
        board[row][col] = '■';
      }
    }
  } // end setBoard

  /** 
   * Gets the board (Return Method 2)
   * @return the board array
   */
  public char[][] getBoard()
  {
    return board;
  } // end getBoard

  /** 
   * Method to reveal the surrounding squares on the first click (Overload Method 1, Parameter method 4, Return )
   * @param rowCoordinate The row coordinate on the board to reveal the surrounding squares
   * @param colCoordinate The col coordinate on the board to reveal the surrounding squares
   * @param innerboard The InnerBoard object, used to know what is the value of the squares that are being revealed
   * @param isFlag To check that it is the first click
   */
  public void revealTile(int rowCoordinate, int colCoordinate, InnerBoard innerBoard, boolean isFlag)
  {
    //Generate the inner board with its value
    innerBoard.initializeBoard(rowCoordinate, colCoordinate);

    //Get the number of the tile they clicked on and add it to the outer board
    board[rowCoordinate][colCoordinate] = innerBoard.getBoard()[rowCoordinate][colCoordinate];

    //Arrays for the different possible iterations to the row and col values (0,0 is not included as it is the clicked square)
    int[] rowIterations = {1, 1, 1, 0, 0, -1, -1, -1};
    int[] colIterations = {1, 0, -1, 1, -1, 1, 0, -1};

    //Loop to reveal the surrounding squares on the outer board
    for (int i = 0; i < rowIterations.length; i++)
    {
      //Try set the board to the revealed value with the new coordinates
      try
      {
        int newRowCoordinate = rowCoordinate + rowIterations[i];
        int newColCoordinate = colCoordinate + colIterations[i];

        //Try setting value
        board[newRowCoordinate][newColCoordinate] = innerBoard.getBoard()[newRowCoordinate][newColCoordinate];
      }
      catch (Exception e)
      {
        //Do nothing
      }
    }   
    
  } // end revealTile

  /** 
   * Emmett (Overload Method 1) 
   * Method to reveal the surrounding squares on clicks after the first click
   * @param rowCoordinate The row coordinate on the board to reveal the surrounding squares
   * @param colCoordinate The col coordinate on the board to reveal the surrounding squares
   * @param innerboard The InnerBoard object, used to know what is the value of the square that is being revealed
   */
  public void revealTile(int rowCoordinate, int colCoordinate, InnerBoard innerBoard)
  {
    //Check if the squares already been revealed
    if (board[rowCoordinate][colCoordinate] == '■' || board[rowCoordinate][colCoordinate] == '◪') 
    { 
      board[rowCoordinate][colCoordinate] = innerBoard.getBoard()[rowCoordinate][colCoordinate];
    }
    else //The squares is already revealed
    {
      System.out.println("This square has already been revealed");
    }
    
  } // end revealTile

  /** 
   * Method to place a flag on a square of the board
   * @param rowCoordinate The row coordinate on the board to place a flag
   * @param colCoordinate The col coordinate on the board to place a flag
   * @return An updated version of the outerboard to be displayed to the user
   */
  public void addFlag(int rowCoordinate, int colCoordinate)
  {
    //Check if the square can be flagged
    if (board[rowCoordinate][colCoordinate] == '■') 
    {
      board[rowCoordinate][colCoordinate] = '◪';
    }
    else //The squares cant be flagged
    {
      System.out.println("You can't place a flag on that square");
    }
    
  } // end addFlag

  /** 
   * Method to remove a flag from a square on the board
   * @param rowCoordinate The row coordinate on the board to remove a flag
   * @param colCoordinate The col coordinate on the board to remove a flag
   * @return An updated version of the outerboard to be displayed to the user
   */
  public void removeFlag(int rowCoordinate, int colCoordinate)
  {
    //Check if the square can be unflagged
    if (board[rowCoordinate][colCoordinate] == '◪') 
    {
      board[rowCoordinate][colCoordinate] = '■';
    }
    else //The squares cant be flagged
    {
      System.out.println("You can't unflag that square");
    }
    
  } // end flagTile

  /** 
   * Method to check if the user has won the game by clicking on all squares that aren't mines (Overload Method 2, Return Method 3)
   * @param innerboard The InnerBoard object, used to know what the number of mines is
   * @return A boolean value that is true if the user has won the game
   */
  public boolean gameStatus(InnerBoard innerBoard)
  {

    int unrevealedCount = 0;
    boolean isWin = true;

    //Check through each square on the board

    outer: //Place to break out of the loop
    for (int row = 0; row < board.length; row++)
    {
      for (int col = 0; col < board[row].length; col++)
      {
        if (board[row][col] == '■' || board[row][col] == '◪')
        {
          
          unrevealedCount++;

          if (unrevealedCount > innerBoard.getMineCount()) //Check if there are more remaining squares than mines, this disqualifies a win
          {
            isWin = false;
            break outer; //Break out of two loops
          }
          
        }
      }
    }
    
    return isWin;
    
  } //end gameStatus

  /** 
   * Emmett (Overload Method 2, Return Method 4)
   * Method to check if the user has lost the game from clicking on a mine
   * @param rowCoordinate The row coordinate on the board to check if the user has clicked on a mine
   * @param colCoordinate The col coordinate on the board to check if the user has clicked on a mine
   * @param innerboard The InnerBoard object, used to know what is the value of the square that is being checked
   * @return A boolean value that is true if the user has lost the game
   */
  public boolean gameStatus(int rowCoordinate, int colCoordinate, InnerBoard innerBoard)
  {
    
    if (innerBoard.getBoard()[rowCoordinate][colCoordinate] == 'M') //Condition for losing
    {
      return true;
    }
    else
    {
      return false;
    }
  } //end gameStatus

}