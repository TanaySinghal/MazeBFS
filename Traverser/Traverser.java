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


public class Traverser {
    static final char emptyBlock = ' ';
    final char startBlock = 'S';
    final char goalBlock = 'G';
    final char pathMarking = '0';
    final String[] mazeFiles = {"maze1.txt", "maze2.txt", "maze3.txt"};
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
        File folder = new File("../Maze/");
        File[] files = folder.listFiles();
        for(File file : files) {
            String fileName = file.getName();
            if(fileName.contains(".txt")) {
                charArray = buildArrayFromTextFile(file);
                System.out.println(fileName + ":");
                printBoard();
                
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
        
        //starting on right side but cr, mr, cl, ml because solution is printed backwards..
        Node initialNode = new Node(startY, startX, null);
        list.add(initialNode);
        
        while (!list.isEmpty()) {
            //Set this testNode equal to the first (FIFO) element and remove it
            Node testNode = list.remove();
            exploredCoords.add(testNode.coords);
            
            for (Node childState : testNode.getChildren()) {
                if(!exploredCoords.contains(childState.coords) && !list.contains(childState)) {
                    if (childState.reachedGoal()) {
                        return childState;
                    }
                    if(childState.isLegal()) {
                        list.add(childState);
                    }
                }
            }
        }
        return null;
    }
}