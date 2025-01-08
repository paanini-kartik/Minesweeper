/*

This program is a version of Minesweeper that is played by selecting coordinates to reveal, flag, and unflag squares. There are two game modes which can be selected by the user, which are limited time and unlimited time. The limited time mode is where the user is given a limited amount of time to reveal all the squares, and the unlimited time mode is where the user is given an unlimited amount of time to reveal all the squares. The unlimited time gamemode allows users to complete the game as fast as they can and their scores can be logged in a file, which can later be displayed to the user.

Programmed by Paanini Kartik & Emmett Burns.

Last modified: January 22, 2023

*/

import java.util.*;
import java.io.*;
import java.lang.Thread;

class Main 
{
  
  //static variables
  public static Scanner keyboard = new Scanner(System.in); // User input linked to keyboard
  public static boolean isTimerDone; // Needed as a static variable so that CustomTimerTask can access it
  public static final String CLEAR = "\033[H\033[2J"; // Clears the console
  
  public static void main(String[] args) throws InterruptedException
  {
    //Variables 
    String menuSelection;
    String userName = ""; // Prevent not initialized issue

    // Timer system -- creating Timer object

    Timer timer = new Timer(); // Timer object: will schedule the task (object from CustomTimerTask) accordingly

    //Run the login system
    
    try
    {
      checkForFiles();
      userName = login();
    }
    catch (IOException e)
    {
      // Catch IOException that has been thrown
      System.out.println("IOException");
      e.printStackTrace();
      
    }
    catch (InterruptedException e)
    {
      // Catch InterruptedException that has been thrown

      System.out.println("InterruptedException");
      e.printStackTrace();
    }

    //Run the main menu
    while(true)
    {
      
      Thread.sleep(1000); // Sleep for 1 second
      System.out.print(CLEAR); // Clear the console
      System.out.flush();
      Thread.sleep(500); // Sleep for 1/2 second

      //Title
      System.out.println("___  ____                                                   \n|  \\/  (_)                                                  \n| .  . |_ _ __   ___  _____      _____  ___ _ __   ___ _ __ \n| |\\/| | | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n| |  | | | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |   \n\\_|  |_/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|   \n                                           | |              \n                                           |_|              ");
      //Menu Options
      System.out.println("1) Play Minesweeper");
      System.out.println("2) Instructions");
      System.out.println("3) High Scores");
      System.out.println("4) Exit");
      System.out.print("\nPlease enter your selection (1, 2, 3, 4): ");
      menuSelection = keyboard.nextLine();

      //1) Play Minesweeper
      if(menuSelection.equals("1"))
      {

        // Let the user choose the dimensions of the board

        int dimensions = 0; // Int value to store chosen dimensions of the board
        
        do // Loop until input is valid
        {
          
          try // Try to execute code
          {
            System.out.print("How many dimensions would you like the board to be (5-16)?: ");
            dimensions = keyboard.nextInt();
            keyboard.nextLine();
          }
          catch (Exception e) // Catch possible invalid input type
          {
            System.out.println("\nInvalid input type. Please try again.");
            keyboard.nextLine(); // Clear the input buffer (as keyboard.nextLine() in try block didn't execute)
            continue; // Continue to the next iteration of the loop
          }

          if (dimensions < 5 || dimensions > 16) // Check if input is within the range
          {
            System.out.println("\nInvalid input. Please try again.");
          }
        
        } while ((dimensions < 5 || dimensions > 16));

        //Create inner/outerboard objects
        InnerBoard innerBoard = new InnerBoard(dimensions);

        OuterBoard outerBoard = new OuterBoard();
        outerBoard.setBoard(dimensions);

        // Let the user choose between the game modes

        System.out.println("\n1) Limited Timed");
        System.out.println("2) Unlimited Time");
        System.out.print("\nPlease enter your selection (1, 2): ");
        menuSelection = keyboard.nextLine();

        try
        {
          if(menuSelection.equals("1")) // Limited Timed
          {
            playGame(timer, innerBoard, outerBoard);
          }
          else if(menuSelection.equals("2")) // Unlimited Time
          {
            playGame(userName, innerBoard, outerBoard);
          }
          else
          {
            System.out.println("\nInvalid input. Please try again.\n");
          }
        }
        catch (InterruptedException e) // Catch exception that has been thrown
        {
          System.out.println("InterruptedException");
          e.printStackTrace();
        }
        
      }
      //2) Instructions
      else if(menuSelection.equals("2"))
      {
        displayInstructions();
      }
      //3) High Scores
      else if(menuSelection.equals("3"))
      {
        
        System.out.print(CLEAR);
        System.out.flush();

        try
        {
          displayHighScores(); // Order and display high scores
        }
        catch (IOException e)
        {
          // Catch IOException that has been thrown
          System.out.println("IOException");
          e.printStackTrace();
        }
        
        System.out.print("\nPress enter to continue.");
        keyboard.nextLine();
        
        System.out.print(CLEAR);
        System.out.flush();
        
      }
      //4) Exit
      else if(menuSelection.equals("4"))
      {
        //Farewell message
        System.out.println("\n\nThank you for playing Minesweeper!\n\n");

        timer.cancel(); // Cancel the timer

        // Print out title to end

        //Title
        System.out.println("___  ____                                                   \n|  \\/  (_)                                                  \n| .  . |_ _ __   ___  _____      _____  ___ _ __   ___ _ __ \n| |\\/| | | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n| |  | | | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |   \n\\_|  |_/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|   \n                                           | |              \n                                           |_|              ");
        
        //Exit the program
        System.exit(0);
        
      }
      else // Invalid input
      {
        System.out.println("\nInvalid input. Please try again.\n");
      }
      
    }
    
  }

  /**
   * Checks that the necessary files are there.
   * Programmed by Emmett Burns. (Void method 1)
   */
  public static void checkForFiles() throws IOException {
    //Variables
    File file = new File("usernames.txt");
    File file2 = new File("passwords.txt");
    File file3 = new File("highScores.txt");

    // Check that file exists
    if (!file.exists()) {
      System.out.print("The necessary file usernames.txt is missing. The program will now exit.");
      System.exit(0);
    }

    // Check that file2 exists
    if (!file2.exists()) {
      System.out.print("The necessary file passwords.txt is missing. The program will now exit.");
      System.exit(0);
    }

    // Check that file3 exists
    if (!file3.exists()) {
      System.out.print("The necessary file highScores.txt is missing. The program will now exit.");
      System.exit(0);
    }

  } // end checkForFiles

  /**
   * Gets all the information from the files to the arrays.
   * @return A 2d String array with all the information from the 3 files.
   * Programmed by Emmett Burns. (Return Method 1)
   */
  public static String[][] populateArray() throws IOException {

    //Variables
    File file = new File("usernames.txt");
    File file2 = new File("passwords.txt");
    File file3 = new File("highScores.txt");
    int numRows = 0;

    //Create scanner inputfiles
    Scanner inputFile = new Scanner(file);
    Scanner inputFile2 = new Scanner(file2);
    Scanner inputFile3 = new Scanner(file3);

    //Count the number in usernames (will be equal to other files)
    while (inputFile.hasNext())
    {
      inputFile.nextLine();
      numRows++;
    }

    //Close and reopen inputFile
    inputFile.close();
    inputFile = new Scanner(file);

    //Create 2d array to store the data from the 3 files (col 0->usernames, col 1->passwords, col 2->highScores)
    String[][] fileData = new String[numRows][3];

    //Populate the 2d array
    for (int row = 0; row < numRows; row++)
    {
      fileData[row][0] = inputFile.nextLine();
      fileData[row][1] = inputFile2.nextLine();
      fileData[row][2] = inputFile3.nextLine();
    }

    //Close the inputFiles
    inputFile.close();
    inputFile2.close();
    inputFile3.close();

    //Return 2d array
    return fileData;

  } // end populateArray

  /**
   * Goes through the login/create account menu system.
   * Programmed by Emmett Burns
   */
  public static String login() throws IOException, InterruptedException {

    //Variables
    String menuSelection, usernameInput, passwordInput;
    String[][] fileData = populateArray();
    boolean correctPassword = false, moveOn = false, usernameExists = false;

      //Menu loop
      while (true)
      {
        // Wipe/flush screen
        System.out.print(CLEAR);
        System.out.flush();
        
        //Title
        System.out.println("___  ____                                                   \n|  \\/  (_)                                                  \n| .  . |_ _ __   ___  _____      _____  ___ _ __   ___ _ __ \n| |\\/| | | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n| |  | | | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |   \n\\_|  |_/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|   \n                                           | |              \n                                           |_|              ");
        //Menu
        System.out.println("Login or Create a New Account to Continue.\n");
        System.out.println("1) Login");
        System.out.println("2) Create New Account");
        System.out.println("3) Exit");
        System.out.print("Please enter your selection: ");
        menuSelection = keyboard.nextLine();

        if(menuSelection.equals("1") || menuSelection.equals("2") || menuSelection.equals("3")) //Clear screen (do not do this for invalid input)
        {

          Thread.sleep(500); // Wait for 1/2 seconds
          System.out.print(CLEAR); // Clear screen
          System.out.flush();

          //1) Login
          if (menuSelection.equals("1"))
          {
            do 
            {
              //Get the username
              System.out.print("Please enter your username (type 'back' to go back): ");
              usernameInput = keyboard.nextLine();

              // Check if the user wants to go back
              if (usernameInput.equals("back")) 
              {
                break;
              }

              //Get the password
              System.out.print("Please enter your password (type 'back' to go back): ");
              passwordInput = keyboard.nextLine();

              // Check if the user wants to go back
              if (passwordInput.equals("back")) 
              {
                break;
              }

              // Loop through all the usernames in the 2d array and see if the username exists, if it does, then check the passwords column in the array to see if it matches the password inputted

              correctPassword = false;// make sure it resets
              for (int i = 0; i < fileData.length; i++) 
              {
                if (usernameInput.equals(fileData[i][0])) 
                {
                  // If the username exists, check if the password matches
                  if (passwordInput.equals(fileData[i][1])) 
                  {
                    correctPassword = true;
                  }
                }
              }

              // Message if username or password is wrong
              if (correctPassword == false) 
              {
                System.out.println("Incorrect username or password.");
              }
            } while (correctPassword == false); //end of do while loop

            if (correctPassword == true) 
            {
              // break out of the main loop to move onto the actual program
              break;
            }

          }
          //2) Create New Account
          else if (menuSelection.equals("2"))
          {
            // Whipe/flush screen
            System.out.print(CLEAR);
            System.out.flush();

            moveOn = false; // make sure its false
            do //Loop for if the username exists already
            {
              // Get the user to enter the username
              System.out.print("Please enter your username (type 'back' to go back): ");
              usernameInput = keyboard.nextLine();

              // Check that the username isnt already taken by looping through the fileData array
              usernameExists = false; // Make sure its false
              for (int i = 0; i < fileData.length; i++) 
              {
                if (usernameInput.equals(fileData[i][0])) 
                {
                  System.out.println("This username is already taken.");
                  usernameExists = true;
                  break;
                }
              }
            } while (usernameExists && !(usernameInput.equals("back"))); //end of do while loop

            // if the username input isnt 'back' then get the user to enter a password
            if (!(usernameInput.equals("back"))) 
            {
              do 
              {
                // Get the user to enter the password
                System.out.print("Please enter a password that is at least 8 characters (type 'back' to go back): ");
                passwordInput = keyboard.nextLine();

                // Check that the password is at least 8 characters
                if (passwordInput.length() < 8 && !(passwordInput.equals("back"))) 
                {
                  System.out.println("Password must be at least 8 characters long.");
                }

              } while (passwordInput.length() < 8 && !(passwordInput.equals("back")));


              // if the password input isnt 'back' then run update the files with new info for new account
              if (!(passwordInput.equals("back"))) 
              {

                // Print the new username, password, and high score to the files
                //usernames.txt
                FileWriter fw = new FileWriter("usernames.txt", true);
                PrintWriter outputFile = new PrintWriter(fw);
                outputFile.println(usernameInput);
                outputFile.close();

                //passwords.txt
                fw = new FileWriter("passwords.txt", true);
                outputFile = new PrintWriter(fw);
                outputFile.println(passwordInput);
                outputFile.close();

                //highScores.txt
                fw = new FileWriter("highScores.txt", true);
                outputFile = new PrintWriter(fw);
                outputFile.println(0); // defealt highScore that will go in the file
                outputFile.close();

                fileData = populateArray();

                // Allow to move onto main menu
                moveOn = true;
              }
            }

            // Condition to move onto the main menu
            if (moveOn) 
            {
              break;// break out of the main loop to move onto the actual program
            }

          }
          //3) Exit
          else if (menuSelection.equals("3"))
          {
            //Farewell message
            System.out.println("Thank you for playing Minesweeper!\n\n");

            //Print out title to end

            //Title
            System.out.println("___  ____                                                   \n|  \\/  (_)                                                  \n| .  . |_ _ __   ___  _____      _____  ___ _ __   ___ _ __ \n| |\\/| | | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n| |  | | | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |   \n\\_|  |_/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|   \n                                           | |              \n                                           |_|              ");

            //exit
            System.exit(0);

          }
          
        }
        else //Invalid input
        {
          System.out.println("\nInvalid input. Please try again.\n");
          Thread.sleep(750); // Wait for 3/4 seconds
        }

      }

    //Return the username input so that it can be used later to find the index of where to update the highscores
    return usernameInput;

  } // end login

  /**
  * Plays the game using the limited time system.
  * Programmed by: Paanini Kartik.
  * Overloaded method for Paanini Kartik #1.
  * @param timer Timer object that schedules the break of the game.
  * @param innerBoard passes in InnerBoard object to be worked with (contains all the numbers and mines).
  * @param outerBoard passes in OuterBoard object to be worked with (contains all the displayed squares).
  */

  public static void playGame(Timer timer, InnerBoard innerBoard, OuterBoard outerBoard) throws InterruptedException
  {

    // Set isTimerDone to false every time this gamemode happens

    isTimerDone = false;
    
    // Extend TimerTask to incorporate specific functionality

    class CustomTimerTask extends TimerTask // Subclass describing specific task to be executed
    {

      @Override // Annotation to indicate to compiler that run method of TimerTask is getting overriden
      public void run() // Overriding method
      {
        isTimerDone = true; // Indicate that the timer is done
      }

    }

    // Declare a CustomTimerTask object

    CustomTimerTask task = new CustomTimerTask();
    
    // Ask user to choose the delay

    boolean inputFlag = false; // Flag to check if input is valid
    long delay = 1; // Holds the amount of time the Timer object waits for when scheduling a task (initialize to 1 second)

    do // Loop for asking user to input a delay
    {
      
      inputFlag = false; // Reset flag
      
      try
      {
        System.out.println("Please enter the maximum time you would like to play for (in seconds): ");
        delay = keyboard.nextLong()*1000; // Convert to milliseconds
        keyboard.nextLine(); // Consume newline character
      }
      catch (Exception e)
      {
        System.out.println("\nInvalid input. Please try again.");
        keyboard.nextLine(); // Consume newline character
        inputFlag = true;
      }

      if (delay < 0)
      {
        System.out.println("\nPlease enter a positive time.\n");
      }
      
    } while (inputFlag || (delay < 0)); // Loop until input is valid

    // Start game

    // Variables
    
    int rowRevealedTile = -1, colRevealedTile = -1, row = -1, col = -1; // First two variables are for selected squares to reveal (initialize with values of -1 so that it does not pass loops to check for valid inputs)
    String menuSelection;
    boolean isExit = false;

    Thread.sleep(1000); // Wait for 1 second
    System.out.print(CLEAR); // Clear screen
    System.out.flush();
    
    // Print board to user

    Thread.sleep(1000); // Wait for 1 second
    System.out.print(CLEAR); // Clear screen
    System.out.flush();
    
    printBoard(outerBoard);

    // Code for the first click
    
    do // Loops until the user selects a valid square
    {
      
      try
      {

        // Ask for rows and cols

        System.out.print("Please enter the row of the coordinate you would like to click on: ");
        rowRevealedTile = keyboard.nextInt();
        keyboard.nextLine(); // Clear buffer

        System.out.print("Please enter the column of the coordinate you would like to click on: ");
        colRevealedTile = keyboard.nextInt();
        keyboard.nextLine(); // Clear buffer

      }
      catch (Exception e)
      {
        System.out.println("\nInvalid input. Please try again.\n");
        keyboard.nextLine(); // Clear buffer
        continue;
      }

      if (rowRevealedTile < 0 || rowRevealedTile >= outerBoard.getBoard().length || colRevealedTile < 0 || colRevealedTile >= outerBoard.getBoard().length) // Check if the input is out of bounds and print out if it is
      {
        System.out.println("\nInvalid input. Please try again.\n");
      }
      
    } while (rowRevealedTile < 0 || rowRevealedTile >= outerBoard.getBoard().length || colRevealedTile < 0 || colRevealedTile >= outerBoard.getBoard().length); // Check if the input is valid
    

    // Start timer after first click, set starting time

    timer.schedule(task, delay);
    double startingTime = (System.currentTimeMillis() / 1000); // Get starting time to display elapsed time

    // Reveal the tile on the outer board
    
    outerBoard.revealTile(rowRevealedTile,colRevealedTile,innerBoard,true);

    Thread.sleep(1000); // Wait for 1 second
    System.out.print(CLEAR); // Clear screen
    System.out.flush();

    // Print out board

    printBoard(outerBoard);

    // While loop for the subsequent clicks
    
    while(!outerBoard.gameStatus(innerBoard) && !outerBoard.gameStatus(rowRevealedTile,colRevealedTile,innerBoard) && !isTimerDone)
    {
      
      // Set isExit to false each time
      
      isExit = false;

      // Give the user the options to reveal a tile, or flag/unflag a tile
      
      System.out.println("Would you like to:\n1)Reveal a Tile\n2)Flag a Tile\n3)Unflag a Tile\n4)Exit");
      System.out.print("Please enter your selection (1, 2, 3, 4): ");
      menuSelection = keyboard.nextLine();

      if(menuSelection.equals("1"))
      {

        // Code for all later clicks
        
        do // Loops until the user selects a valid square
        {
          
          try
          {

            // Ask rows and cols
            
            System.out.print("Please enter the row of the coordinate you would like to click on: ");
            rowRevealedTile = keyboard.nextInt();
            keyboard.nextLine(); // Clear buffer

            System.out.print("Please enter the column of the coordinate you would like to click on: ");
            colRevealedTile = keyboard.nextInt();
            keyboard.nextLine(); // Clear buffer

          }
          catch (Exception e)
          {
            System.out.println("\nInvalid input. Please try again.\n");
            keyboard.nextLine(); // Clear buffer
            continue;
          }

          if (rowRevealedTile < 0 || rowRevealedTile >= outerBoard.getBoard().length || colRevealedTile < 0 || colRevealedTile >= outerBoard.getBoard().length) // Check if the input is out of bounds and print out if it is
          {
            System.out.println("\nInvalid input. Please try again.\n");
          }
          
        } while (rowRevealedTile < 0 || rowRevealedTile >= outerBoard.getBoard().length || colRevealedTile < 0 || colRevealedTile >= outerBoard.getBoard().length); // Check if the input is valid

        // Reveal the tile on the outer board without passing boolean
        
        outerBoard.revealTile(rowRevealedTile,colRevealedTile,innerBoard);

      }
      else if(menuSelection.equals("2"))
      {

        // Select tile to flag
        
        do // Loops until the user selects a valid square
        {
          
          try
          {

            // Ask rows and cols
            
            System.out.print("Please enter the row of the coordinate you would like to flag: ");
            row = keyboard.nextInt();
            keyboard.nextLine(); // Clear buffer

            System.out.print("Please enter the column of the coordinate you would like to flag: ");
            col = keyboard.nextInt();
            keyboard.nextLine(); // Clear buffer

          }
          catch (Exception e)
          {
            System.out.println("\nInvalid input. Please try again.\n");
            keyboard.nextLine(); // Clear buffer
            continue;
          }

          if (row < 0 || row >= outerBoard.getBoard().length || col < 0 || col >= outerBoard.getBoard().length) // Check if the input is out of bounds and print out if it is
          {
            System.out.println("\nInvalid input. Please try again.\n");
          }
          
        } while (row < 0 || row >= outerBoard.getBoard().length || col < 0 || col >= outerBoard.getBoard().length); // Check if the input is valid

        // Flag the tile on the outer board
        
        outerBoard.addFlag(row,col);

      }
      else if(menuSelection.equals("3"))
      {

        // Select tile to unflag
        
        do // Loops until the user selects a valid square
        {
          
          try
          {

            // Ask rows and cols
            
            System.out.print("Please enter the row of the coordinate you would like to unflag: ");
            row = keyboard.nextInt();
            keyboard.nextLine(); // Clear buffer

            System.out.print("Please enter the column of the coordinate you would like to unflag: ");
            col = keyboard.nextInt();
            keyboard.nextLine(); // Clear buffer

          }
          catch (Exception e)
          {
            System.out.println("\nInvalid input. Please try again.\n");
            keyboard.nextLine();
            continue;
          }

          if (row < 0 || row >= outerBoard.getBoard().length || col < 0 || col >= outerBoard.getBoard().length) // Check if the input is out of bounds and print out if it is
          {
            System.out.println("\nInvalid input. Please try again.\n");
          }
          
        } while (row < 0 || row >= outerBoard.getBoard().length || col < 0 || col >= outerBoard.getBoard().length); // Check if the input is valid

        // Unflag the tile on the outer board
        outerBoard.removeFlag(row,col);

      }
      else if(menuSelection.equals("4"))
      {
        // Exit the game
        
        isExit = true;
        break; // Break out of loop
      }
      else // Invalid input
      {
        System.out.println("\nInvalid input. Please try again.");
      }

      Thread.sleep(1000); // Wait for 1 second
      System.out.println(CLEAR); // Clear screen
      System.out.flush();
      
      // Print board to user
      
      printBoard(outerBoard);

      // Print elapsed time

      System.out.println("Elapsed Time: " + (int) ((System.currentTimeMillis()/1000) - startingTime) + " seconds"); // Convert to int for display
      
    }

    // Print whether user won or not, if the user simply wanted to exit, do not print anything

    if(isExit)
    {
      // Do nothing, as this is game exit condition
    }
    
    // Prioritize this case over win case, as the win condition can be triggered by clicking the mine when there are two tiles left to reveal
    
    else if (outerBoard.gameStatus(rowRevealedTile,colRevealedTile,innerBoard)) // Loss condition due to mines
    {
      
      System.out.println("you lose... :( You clicked a mine.");

      Thread.sleep(2000); // Wait for 2 seconds
      System.out.println(CLEAR); //Clear screen
      System.out.flush();
      
      // Tell user true placement of the mines
      
      System.out.println("Here was the true placement of the mines: \n");
      printBoard(innerBoard);
      
      
    }
    else if (outerBoard.gameStatus(innerBoard))  // Win condition
    {
      System.out.println("YOU WIN!!! :) All the tiles were revealed!");
    }
    else // Loss condition due to timer
    {
      
      System.out.println("you ran out of time... :(");

      Thread.sleep(2000); // Wait for 2 seconds
      System.out.println(CLEAR); // Clear screen
      System.out.flush();
      
      // Tell user true placement of the mines

      System.out.println("Here was the true placement of the mines: \n");
      printBoard(innerBoard);
      
    }

    System.out.print("Press enter to continue.");
    keyboard.nextLine();
    
  }

  /**
   * Plays the game using the unlimited time system.
   * Programmed by: Emmett Burns. (Void method 2, Parameter Method 1)
   * @param userName passes in current username to add to high scores
   * @param innerBoard passes in InnerBoard object to be worked with (contains all the numbers and mines).
   * @param outerBoard passes in OuterBoard object to be worked with (contains all the displayed squares).
   */
  public static void playGame(String userName, InnerBoard innerBoard, OuterBoard outerBoard) throws InterruptedException
  {
    // Variables
    int rowRevealedTile = -1, colRevealedTile = -1, row = -1, col = -1; //First two variables are for selected squares to reveal
    String menuSelection;
    boolean isExit = false;
    long startingTime, endingTime; //Calculate amount of time taken

    //Wait 1 sec and clear screen
    Thread.sleep(1000); 
    System.out.println(CLEAR);
    System.out.flush();
    
    //Print board to user
    printBoard(outerBoard);

    //Code for the first click
    do //Loops until the user selects a valid square
    {
      try
      {
        System.out.print("Please enter the row of the coordinate you would like to click on: ");
        rowRevealedTile = keyboard.nextInt();

        keyboard.nextLine(); //Clear buffer

        System.out.print("Please enter the column of the coordinate you would like to click on: ");
        colRevealedTile = keyboard.nextInt();

        keyboard.nextLine(); //Clear buffer
      }
      catch (Exception e)
      {
        System.out.println("\nInvalid input. Please try again.\n");

        keyboard.nextLine(); //Clear buffer

        continue;
      }

      //Check if the input is out of bounds and print out if it is
      if (rowRevealedTile < 0 || rowRevealedTile >= outerBoard.getBoard().length || colRevealedTile < 0 || colRevealedTile >= outerBoard.getBoard().length)
      {
        System.out.println("\nInvalid input. Please try again.\n");
      }

    } while (rowRevealedTile < 0 || rowRevealedTile >= outerBoard.getBoard().length || colRevealedTile < 0 || colRevealedTile >= outerBoard.getBoard().length); //Check if the input is valid
    
    //Select starting time
    startingTime = System.currentTimeMillis();
    
    //Reveal the tile on the outer board
    outerBoard.revealTile(rowRevealedTile, colRevealedTile, innerBoard, true);

    //Wait 1 sec and clear screen
    Thread.sleep(1000); 
    System.out.println(CLEAR); 
    System.out.flush();
    
    //Print the board
    printBoard(outerBoard);

    //While loop for the subsequent clicks
    while(!outerBoard.gameStatus(innerBoard) && !outerBoard.gameStatus(rowRevealedTile,colRevealedTile,innerBoard))
    {
      
      //Set isExit to false each time
      isExit = false;

      //Give the user the options to reveal a tile, or flag/unflag a tile
      System.out.println("Would you like to:\n1)Reveal a Tile\n2)Flag a Tile\n3)Unflag a Tile\n4)Exit");
      System.out.println("Please enter your selection (1, 2, 3, 4): ");
      menuSelection = keyboard.nextLine();

      if(menuSelection.equals("1"))
      {
        
        //Code for all later clicks
        do //Loops until the user selects a valid square
        {
          try
          {
            System.out.print("Please enter the row of the coordinate you would like to click on: ");
            rowRevealedTile = keyboard.nextInt();
            
            keyboard.nextLine(); //Clear buffer

            System.out.print("Please enter the column of the coordinate you would like to click on: ");
            colRevealedTile = keyboard.nextInt();
            
            keyboard.nextLine(); //Clear buffer
          }
          catch (Exception e)
          {
            System.out.println("\nInvalid input. Please try again.\n");
            
            keyboard.nextLine(); //Clear buffer
            
            continue;
          }

          //Check if the input is out of bounds and print out if it is
          if (rowRevealedTile < 0 || rowRevealedTile >= outerBoard.getBoard().length || colRevealedTile < 0 || colRevealedTile >= outerBoard.getBoard().length)
          {
            System.out.println("\nInvalid input. Please try again.\n");
          }
          
        } while (rowRevealedTile < 0 || rowRevealedTile >= outerBoard.getBoard().length || colRevealedTile < 0 || colRevealedTile >= outerBoard.getBoard().length); //Check if the input is valid
        
        //Reveal the tile on the outer board without passing boolean
        outerBoard.revealTile(rowRevealedTile,colRevealedTile,innerBoard);

      }
      else if(menuSelection.equals("2"))
      {

        //Select tile to flag
        do //Loops until the user selects a valid square
        {
          try
          {
            System.out.print("Please enter the row of the coordinate you would like to flag: ");
            row = keyboard.nextInt();
            
            keyboard.nextLine(); //Clear buffer

            System.out.print("Please enter the column of the coordinate you would like to flag: ");
            col = keyboard.nextInt();
            
            keyboard.nextLine(); //Clear buffer
          }
          catch (Exception e)
          {
            System.out.println("\nInvalid input. Please try again.\n");
            
            keyboard.nextLine(); //Clear buffer
            continue;
          }

          //Check if the input is out of bounds and print out if it is
          if (row < 0 || row >= outerBoard.getBoard().length || col < 0 || col >= outerBoard.getBoard().length) 
          {
            System.out.println("\nInvalid input. Please try again.\n");
          }
        } while (row < 0 || row >= outerBoard.getBoard().length || col < 0 || col >= outerBoard.getBoard().length); //Check if the input is valid

        //Flag the tile on the outer board
        outerBoard.addFlag(row,col);
        
      }
      else if(menuSelection.equals("3"))
      {

        do //Loops until the user selects a valid square
        {
          try
          {
            System.out.print("Please enter the row of the coordinate you would like to unflag: ");
            row = keyboard.nextInt();
            
            keyboard.nextLine(); //Clear buffer

            System.out.print("Please enter the column of the coordinate you would like to unflag: ");
            col = keyboard.nextInt();
            
            keyboard.nextLine(); //Clear buffer
          }
          catch (Exception e)
          {
            System.out.println("\nInvalid input. Please try again.\n");
            
            keyboard.nextLine(); //Clear buffer
            continue;
          }

          //Check if the input is out of bounds and print out if it is
          if (row < 0 || row >= outerBoard.getBoard().length || col < 0 || col >= outerBoard.getBoard().length) 
          {
            System.out.println("\nInvalid input. Please try again.\n");
          }
        } while (row < 0 || row >= outerBoard.getBoard().length || col < 0 || col >= outerBoard.getBoard().length); //Check if the input is valid

        //Unflag the tile on the outer board
        outerBoard.removeFlag(row,col);
        
      }
      else if(menuSelection.equals("4"))
      {
        //Exit the game
        isExit = true;
        break;
      }
      else //Invalid input
      {
        System.out.println("\nInvalid input. Please try again.\n");
      }

      //Wait 1 sec and clear screen
      Thread.sleep(1000); 
      System.out.println(CLEAR);
      System.out.flush();
      
      //Print board to user again
      printBoard(outerBoard);
    }

    //Select ending time
    endingTime = System.currentTimeMillis();

    //Print whether user won or not, if the user  wanted to exit do nothing
    if(isExit)
    {
      //Do nothing, as this is game exit condition
    }
    else if (outerBoard.gameStatus(rowRevealedTile, colRevealedTile, innerBoard)) //Prioritize this case over win case since the win condition can be triggered by clicking the mine when there are two tiles left to reveal
    {
      //You lose
      System.out.println("you lose... :("); 

      //Wait 1 sec and clear screen
      Thread.sleep(2000); 
      System.out.println(CLEAR); 
      System.out.flush();
      
      //Tell user where mines really are
      System.out.println("Here was the true placement of the mines: \n");
      printBoard(innerBoard);
      
    }
    else
    {
      //You win
      System.out.println("YOU WIN!!! :)");

      //Calculate time it took to beat the game
      long totalTime = (endingTime - startingTime)/1000; //Allow integer division to occur

      //Print time
      System.out.println("Your final time: " + totalTime + " seconds");

      //Add score to high scores
      try
      {
        addToHighScores(totalTime, userName);
      }
      catch (IOException e)
      {
        // Catch thrown exception
        System.out.println("Error: IOException");
        e.printStackTrace();
      }

    }

    //Press enter to continue
    System.out.print("Press enter to continue.");
    keyboard.nextLine();
    
    
  } // End playGame

  /** 
   * Prints the OuterBoard board with proper formatting
   * Programmed by: Emmett Burns. (Parameter Method 2)
   * @param outerBoard passes in the OuterBoard object to be worked with
   */
  public static void printBoard(OuterBoard outerBoard)
  {
    //extra tab for formatting
    System.out.print("\t");

    //Loop to print the top coordinates
    for (int i = 0; i < outerBoard.getBoard().length; i++)
    {
      System.out.print(i + "\t");
    }

    //new lines for output format
    System.out.println();
    System.out.println();

    //Loop to print the board with the side coordinates
    for (int row = 0; row < outerBoard.getBoard().length; row++)
    {
      System.out.print(row + "\t"); //side coordinates
      for (int col = 0; col < outerBoard.getBoard()[0].length; col++)
        {
          System.out.print(outerBoard.getBoard()[row][col] + "\t");
        }
      //Go to the next line
      System.out.println();
      System.out.println();
    }
  } // end printBoard

  /**
  * Prints the InnerBoard board with proper formatting
  * Overloaded method Paanini Kartik #3
  * Programmed by: Paanini Kartik
  * @param innerBoard passes in InnerBoard object to be worked with (contains all the numbers and mines).
  */
  public static void printBoard(InnerBoard innerBoard)
  {
    // Loop to print the top coordinates
    
    System.out.print("\t"); // Extra tab for formatting
    for (int i = 0; i < innerBoard.getBoard().length; i++)
    {
      System.out.print(i + "\t");
    }

    // New lines for output format
    
    System.out.println();
    System.out.println();

    // Loop to print the board with the side coordinates
    for (int row = 0; row < innerBoard.getBoard().length; row++)
    {
      System.out.print(row + "\t"); // Print the row coordinates on the side
      for (int col = 0; col < innerBoard.getBoard()[0].length; col++)
        {
          System.out.print(innerBoard.getBoard()[row][col] + "\t"); // Print individual cell values
        }
      
      // Go to the next line
      System.out.println("\n");
    }
  } // end printBoard
  
  
  /**
   * Method to display the instructions to the user. 
   * Programmed by: Emmett Burns.
   */ 
  public static void displayInstructions()
  {
    
    System.out.print("\033[H\033[2J\n"); //Clear screen
    System.out.flush();

    //Display instructions
    //Beginning & Objective
    System.out.println("1) Beginning & Objective");
    System.out.println("Upon loading the board, a number of mines, determined by the dimensions of the board, will be randomly generated throughout the board. These mines are represented by the letter \"M\" when revealed. The goal of the game is to reveal all the tiles without clicking on any mines. The numbers that are on revealed tiles represent the number of mines that are immediately touching that tile (either directly above/below, on either side, or on its diagonals).");

    //First click
    System.out.println("\n2) First Click");
    System.out.println("On the first click, you enter the coordinates of a tile to reveal. That tile will then be revealed along with any tiles immediately touching it. It is not possible to reveal a mine on the first click.");

    //Subsequent clicks
    System.out.println("\n3) Subsequent Clicks");
    System.out.println("On subsequent clicks, you can choose to reveal a tile, flag a tile, or unflag a tile. Revealing tiles will show the number of mines that the revealed tile touches. If the tile is a mine, then you lose the game. Flagging a tile is simply a way to visualy help you keep track of where mines are. You can unflag flagged tiles as needed.");

    //Limited time
    System.out.println("\n4) Limited Time Gamemode");
    System.out.println("In the limited time gamemode, you may choose an amount of time to beat the game in. If you do not beat the game in the allotted time, or you hit a mine, you lose.");

    //unlimited time
    System.out.println("\n5) Unlimited Time Gamemode");
    System.out.println("In the unlimited time gamemode, you have as much time as you want to beat the game, but still lose if you hit a mine. The unlimited time gamemode is still timed with a timer, but this is only used to keep track of users highest score.");

    //Prompt user to continue
    System.out.print("\nPress enter to continue.");
    keyboard.nextLine();
    

  } //End displayInstructions

  /**
  * Method to add score to high scores.
  * @param totalTime passes in the total time taken to complete the game.
  * @param userName passes in the username of the user.
  * Programmed by: Paanini Kartik.
  */
  
  public static void addToHighScores(long totalTime, String userName) throws IOException
  {

    // Get list of userNames from highScores.txt and list of high scores from highScores.txt

    File file = new File("usernames.txt");
    Scanner inputFile = new Scanner(file);

    ArrayList<String> userNames = new ArrayList<>();

    while (inputFile.hasNext())
    {
      userNames.add(inputFile.nextLine());
    }

    inputFile.close(); // Close reading file to change to other file

    File file2 = new File("highScores.txt");
    inputFile = new Scanner(file2);

    ArrayList<String> highScores = new ArrayList<>();

    while (inputFile.hasNext())
    {
      highScores.add(inputFile.nextLine());
    }

    inputFile.close(); // Close reading file

    // Find index of the current userName within userNames

    int index = userNames.indexOf(userName);

    // Update highScores.txt with new score

    PrintWriter outputFile;
    
    outputFile = new PrintWriter("highScores.txt");
    

    for (int i = 0; i < userNames.size(); i++) // Loop for all usernames
    {
      
      if (i == index) // Condition for when the userName iterated is the same as the current userName
      {
        
        if (totalTime < Long.parseLong(highScores.get(i)) || (Long.parseLong(highScores.get(i)) == 0)) // Check if this is a high score or this is the first time a user has played -- high score is set to 0 by default
        {
          outputFile.println(totalTime); // Replace with new time
        }
        else
        {
          // Do nothing, as this was not a faster time
        }
        
      }
      else
      {
        outputFile.println(highScores.get(i)); // Keep it the same
      }
      
    }

    outputFile.close(); // Close writing file
    
  } //End addToHighScores
  
  /**
  * Method to display the high scores to the user.
  * Programmed by: Paanini Kartik.
  */
  
  public static void displayHighScores() throws IOException
  {

    // Read relevant information from files

    File file = new File("usernames.txt");
    Scanner inputFile = new Scanner(file);

    ArrayList<String> userNames = new ArrayList<>();

    while (inputFile.hasNext())
    {
      userNames.add(inputFile.nextLine());
    }

    inputFile.close(); // Close reading file to change to other file

    File file2 = new File("highScores.txt");
    inputFile = new Scanner(file2);

    ArrayList<Long> highScores = new ArrayList<>();

    while (inputFile.hasNext())
    {
      highScores.add(Long.parseLong(inputFile.nextLine()));
    }

    inputFile.close();
    
    // Display high scores

    System.out.println("High Scores:\n");

    for (int i = 0; i < userNames.size(); i++) // Loop for usernames
    {
      
      if (highScores.get(i) == 0) // Check for empty high score
      {
        System.out.println(userNames.get(i) + ": Has not sucessfully played yet");
      }
      else // Print valid high score
      {
        System.out.println(userNames.get(i) + ": " + highScores.get(i) + " seconds"); // Have it print out high scores in order of lowest to highest
      }
      
    }
    
  } // End displayHighScores
  
}