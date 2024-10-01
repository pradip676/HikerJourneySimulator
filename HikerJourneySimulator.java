
package HikerJourneySimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * This program guides the hiker through the forest to the safety.
   Hiker starts at the Southern edge of the forest (Bottom of the map) until they
   safely reach the Northward to the top of the map.
    
 * @author Pradip Sapkota (3741235)
 */


public class HikerJourneySimulator {

    public static void main(String[] args) throws FileNotFoundException {
        
        //The constants contents of the map 
        final int CLEAR = 0;
        final int ROCKS = 1;
        final int WATER = 2;
        final int VOLCANO = 3;
        
        int rows,columns;      //store the dimentions of the map
        String forestName;     //store the forest name 
        int totalMoves = 1;     //to keep track of how many moves were required for safe trip and starts counting from 1st step.
        
        Scanner keyboard = new Scanner(System.in);
        
        System.out.println("...Greetings from the Forest Hiker Simulator...");
        System.out.println("We will provide the best way possible to travel through forest safely.");
        System.out.println("");
        
        // Prompt the user to identify the map definition file
        System.out.print("Enter the file name containing map definition: ");
        String fileName= keyboard.nextLine();
        
        //open and read the file using File and Scanner object
        File file = new File(fileName);
        Scanner inputFile = new Scanner(file);
        
        String firstLine= inputFile.nextLine();          //read the first line of a file 
        String [] headerTokens= firstLine.split(",");     //removing delimeter using tokenizing 
        forestName = headerTokens[0];
        rows = Integer.parseInt(headerTokens[1].trim());         // total number of rows given in input file 
        columns = Integer.parseInt(headerTokens[2].trim());     //total number of columns given in input file
        
        //store the map information from the data file to two Dimensional array
        int [][] map = new int[rows][columns];
        
        //read rest of the lines of file to fill the map array
        for(int i= 0;i<rows && inputFile.hasNextLine();i++) {
            String lineOffile = inputFile.nextLine();
            String [] tokens = lineOffile.split(","); 
            
            for(int j=0;j<columns;j++){
                map [i][j] = Integer.parseInt(tokens[j]);
            }
        }
        inputFile.close();     //closing the file after reading 
        
        System.out.println("The map of " + forestName + " forest: ");
        System.out.println("");    // leave the blank line         
        displayForestMap(map);     //call method which displays the content of the map 

        
        // create random object to use random starting column on bottom of the map 
        Random random = new Random();
        int startingCol = random.nextInt(columns);      //starting columns is randomly selected
        int startingRow = rows - 1;                     //starting row is southernmost row
        
        //validate the randomly selected columns till it has no obstacles
        while (map[startingRow][startingCol] != CLEAR) {
            startingCol = random.nextInt(columns);
        }
        
        System.out.println("");
        System.out.println("Here's a detailed log of activity necessary for hiker to travel through forest safely.");
        System.out.println("");
        
        //displaying the starting position and entering the forest 
        System.out.printf("Attempting to start at %c-%d\n",'A' + startingRow, startingCol + 1);
        System.out.printf("%2sMap square %c-%d is empty.\n","", 'A' + startingRow, startingCol + 1);
        System.out.printf("Entering the %s forest at map square %c-%d\n", forestName, 'A' + startingRow, startingCol + 1);
        
        // loop that makes the hiker nevigate through the forest
        while (startingRow > 0) {
            int currentRow = startingRow - 1;    //next row to move from last row
            
            // first check the next north square is clear or not 
            if (map[currentRow][startingCol] == 0) {
               System.out.printf("Moving North to map square %c-%d\n", 'A' + currentRow, startingCol + 1);
               startingRow = currentRow;         //move north if obstacle free
            } else {
                //help to distimguish the currect obstacles for north direction
                String obstacleTypeNorth="";
                switch (map[currentRow][startingCol]) {
                    case 1:
                        obstacleTypeNorth = "rocks";
                        System.out.printf("%2sUnable to move North, map square %c-%d is blocked by %s.\n","", 'A' + currentRow, startingCol+1,obstacleTypeNorth);
                        break;
                    case 2:
                        obstacleTypeNorth= "water";
                        System.out.printf("%2sMap square %c-%d is blocked by %s.\n","", 'A' + currentRow, startingCol + 1, obstacleTypeNorth);
                        break;
                    case 3:
                        obstacleTypeNorth = "volcano";
                        System.out.printf("%2sMap square %c-%d is blocked by %s.\n","", 'A' + currentRow, startingCol + 1, obstacleTypeNorth);
                        break;
            }

            // boolean variable to track if move is successful or not(successful = no obstacle)
            boolean successMove = false; 
            
            // Variable to store the type of obstacle encountered for west direction movement.
             String obstacleTypeWest = "";

             // Moving west if possible.
              if (startingCol > 0) {
                int obstacleWest = map[startingRow][startingCol - 1];
                // Determine the type of obstacle for the West direction 
                  if (obstacleWest == 1) {
                       obstacleTypeWest = "rocks";
                    } else if (obstacleWest == 2) {
                        obstacleTypeWest = "water";
                    } else if (obstacleWest == 3) {
                        obstacleTypeWest = "volcano";
                    }
               if (obstacleWest == 0) {
                   startingCol--;    // moving west if the obstacle is clear in west
                   System.out.printf("Moving West to map square %c-%d\n", 'A' + startingRow, startingCol + 1);
                   successMove = true;    //update the flag variable for successful move 
                } else if(obstacleWest==1){
                    
                   //display if rock is stopping the hiker to move forward
                    System.out.printf("%2sUnable to move West to map square %c-%d.\n %1sThe path West is blocked by %s.\n","", 'A' + startingRow, startingCol,"",obstacleTypeWest);
                }
                //if the obstacles are other than rocks 
                else if (obstacleWest==2 || obstacleWest == 3){ 
                      System.out.printf("%2sMap square %c-%d is blocked by %s.\n","", 'A' + startingRow, startingCol, obstacleTypeWest); 
                     }    
            }
             
            //variable to store the type of obstacle encountered for east direction movement.
            String obstacleTypeEast ="";
            //moving east if possible
            if (!successMove && startingCol < columns - 1) {
                
                // check the obstacles in the east square
               int obstacleEast = map[startingRow][startingCol + 1];
                // Determine the type of obstacle for the West direction immediately before reporting.
                  if (obstacleEast == 1) {
                       obstacleTypeEast = "rocks";
                    } else if (obstacleEast == 2) {
                        obstacleTypeEast = "water";
                    } else if (obstacleEast == 3) {
                        obstacleTypeEast = "volcano";
                    }         
               
               if (obstacleEast == 0) {
                   startingCol++;  //moving east if the obstacle is clear in east
                   System.out.printf("Moving East to map square %c-%d\n", 'A' + startingRow, startingCol + 1);
                   successMove = true;      //successful move has been made 
                } else if(obstacleEast==1){
                    
                   //display if rock is stopping the hiker to move forward.
                   System.out.printf("%2sUnable to move East to map square %c-%d.\n %1sThe path East is blocked by %s.\n","", 'A' + startingRow, startingCol + 2,"",obstacleTypeEast);
                   
                   //if the obstacles are other than rocks 
                } else if(obstacleEast==2 || obstacleEast ==3){    
                    System.out.printf("%2sMap square %c-%d is blocked by %s.\n","", 'A' + startingRow, startingCol + 2, obstacleTypeEast);
                }
            }
            
            // if three paths i.e north,east and west are blocked, check if moving south can give direction to hiker
            if (!successMove) {
                    // Check if there's a way to move South
                    if (startingRow < rows - 1 && map[startingRow + 1][startingCol] == CLEAR) {
                        startingRow++;   //moving south which is obstacle free
                        System.out.printf("Moving South to map square %c-%d\n", 'A' + startingRow, startingCol + 1);
                        successMove = true;   // indicate the successful move
                    }
                }

            //if all paths are blocked by obstacles, display safe trip can not be completed
            if (!successMove) {
            System.out.println("A safe trip cannot be completed this way. You need to change the directions.");
            return;
            }
        }
        totalMoves++;    //increament total moves each time hiker takes a successful move.
    }
        //exit the forest with the exit square and display the total number of successful moves
        System.out.printf("Exiting the %s forest at map square %c-%d\n", forestName, 'A' + startingRow, startingCol + 1);
        System.out.printf("Safe trip completed in %d moves.\n", totalMoves);

    }
    
    /**
     * Thus static method displays the forest map in box format.
     * @param map tow-D array of the map
     */
    private static void displayForestMap(int[][] map) {
        
    for (int row = 0; row < map.length; row++) {
        // get the current row's border based on cols count 
        String currentRowBorder = "+";
        for (int col = 0; col < map[row].length; col++) {
            currentRowBorder += "---+";
        }
        System.out.println(currentRowBorder); // print the top border of the current row

        // print the row's content.
        System.out.print("|");
        for (int col = 0; col < map[row].length; col++) {
            System.out.printf(" %d |", map[row][col]);
        }
        System.out.println();     // finish the current row.
    }
    // print the bottom border for the last row based on its column count
    String lastBorder = "+";
    for (int col = 0; col < map[map.length - 1].length; col++) {
        lastBorder += "---+";
    }
    System.out.println(lastBorder);
    }
}
