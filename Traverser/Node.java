import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by tanaysinghal on 11/4/15.
 */
public class Node {

    int y, x;
    String coords;
    Node parentNode;

    public Node(int myY, int myX, Node parent) {
        this.y = myY;
        this.x = myX;
        this.coords = myY+","+myX;
        parentNode = parent;
    }

    public Queue<Node> getChildren() {
        Node n = this;
        Queue<Node> children = new LinkedList<Node>();

        //Right
        children.add(new Node(n.y, n.x + 1, n));
        //Left
        children.add(new Node(n.y, n.x - 1, n));
        //Up
        children.add(new Node(n.y - 1, n.x, n));
        //Down
        children.add(new Node(n.y + 1, n.x, n));

        return children;
    }

    public void getSolutionCoords() {
        Traverser.path.add(coords);
        if(parentNode != null) {
            parentNode.getSolutionCoords();
        }

    }

    public boolean reachedGoal() {
        return x == Traverser.goalX && y == Traverser.goalY;
    }

    public boolean isLegal() {
        if(y >= 0 && y < Traverser.mazeHeight) {
            if(x >= 0 && x < Traverser.mazeWidth) {
                return Traverser.charArray[y][x] == Traverser.emptyBlock;
            }
        }
        return false;
    }
}
