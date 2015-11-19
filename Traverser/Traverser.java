/**
 * Created by tanaysinghal on 11/3/15.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

//TODO: empty out path

public class Traverser {
    static final char emptyBlock = ' ';
    final char startBlock = 'S';
    final char goalBlock = 'G';
    final char pathMarking = '0';
    final String filePath = "";
    
    //Do not change below this line
    //-----------------------------
    public static int mazeWidth, mazeHeight;
    public static int startY, startX, goalX, goalY;
    
    public static char[][] charArray;
    
    //List with path coordinates
    public static List<String> path = new ArrayList<String>();
    
    //Text colors
    public static final String AINSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    
    public static void main(String[] args) {
        new Traverser();
    }
    
    Traverser() {
        //Read maze text files
        File folder = new File("../Maze/");
        File[] files = folder.listFiles();
        for(File file : files) {
            String fileName = file.getName();
            if(fileName.contains(".txt")) {
                path = new ArrayList<String>();
                charArray = buildArrayFromTextFile(file);
                System.out.println(fileName + ":");
                printBoard();
                
                //Find solution
                Node solution = getSolution();
                
                if(solution == null) {
                    System.out.println("No solution");
                }
                else {
                    System.out.println("Solution: ");
                    solution.getSolutionCoords();
                    printSolution();
                    //System.out.println(solution.toString());
                }
            }
        }
    }
    
    void printBoard() {
        for(int i = 0; i < mazeHeight; i ++) {
            System.out.println();
            for(int j = 0; j < mazeWidth; j ++) {
                System.out.print(charArray[i][j]);
            }
        }
        System.out.println("\n");
    }
    
    //Print path in red
    void printSolution() {
        for(int i = 0; i < mazeHeight; i ++) {
            System.out.println();
            for(int j = 0; j < mazeWidth; j ++) {
                String currentCoords = i+","+j;
                if(path.contains(currentCoords)) {
                    System.out.print(ANSI_RED + pathMarking + AINSI_BLACK);
                }
                else {
                    System.out.print(charArray[i][j]);
                }
            }
        }
        System.out.println("\n");
    }
    
    //Convert text file to 2D char array
    char[][] buildArrayFromTextFile (File _mazeFile) {
        
        List<String> lines = new ArrayList<String>();
        
        try {
            Scanner sc = new Scanner(_mazeFile);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                //store this line to string [] here
                lines.add(line);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: Maze file not found");
        }
        
        //Calculate maze width and height
        mazeWidth = lines.get(0).length();
        mazeHeight = lines.size();
        
        //Convert to array
        char[][] array = new char[mazeHeight][mazeWidth];
        
        for(int i = 0; i < mazeHeight; i ++) {
            array[i] = lines.get(i).toCharArray();
            
            //Find start and goal coordinates
            for(int j = 0; j < mazeWidth; j ++) {
                if(array[i][j] == goalBlock) {
                    goalY = i;
                    goalX = j;
                }
                else if(array[i][j] == startBlock) {
                    startY = i;
                    startX = j;
                }
            }
        }
        
        return array;
    }
    
    public static Node getSolution() {
        Queue<Node> list = new LinkedList<Node>();
        Queue<String> exploredCoords = new LinkedList<String>();
        
        //Make an initial parent node of our first possible move
        Node initialNode = new Node(startY, startX, null);
        list.add(initialNode);
        
        while (!list.isEmpty()) {
            //Set this testNode equal to the first (FIFO) element and remove it
            Node testNode = list.remove();
            exploredCoords.add(testNode.coords);
            
            //Loop through all the move options
            for (Node childState : testNode.getChildren()) {
                //Check if this move option has been explored before
                if(!exploredCoords.contains(childState.coords) && !list.contains(childState)) {
                    //If we've reached goal, return solution
                    if (childState.reachedGoal()) {
                        testNode = null;
                        return childState;
                    }
                    //Otherwise, if the move option is a valid path (not a wall)
                    if(childState.isLegal()) {
                        //Add move option to list
                        list.add(childState);
                    }
                }
            }
        }
        return null;
    }
}