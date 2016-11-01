/**
 * Created by menna on 10/23/16.
 */
public class Node {
    int nodeNum;
    int counter;
    char symbol;
    Node parent;
    String binarycode;
    Node right;
    Node left;

    //methods:
    // swap (exchange nodeNum only)

    public Node(int num, int count, char s, Node p, String bcode) {
        this.nodeNum = num;
        this.counter = count;
        this.symbol = s;
        this.parent = p;
        this.binarycode = bcode;
        this.right = null; this.left = null;
    }

    public Node() {
        this.nodeNum = 0;
        this.counter = 0;
        this.symbol = ' ';
        this.parent = null;
        this.right = null;
        this.binarycode = " ";
        this.right = null; this.left = null;
    }


    public Boolean swapConditions(Node x) {
        if (this.nodeNum > x.nodeNum && this.counter <= x.counter && x.parent != this && !this.isRoot() && !x.isRoot())
            return true;
        else return false;
    }


    public void setLeft(Node l) {
        this.left = l;
    }

    public void setRight(Node r) {
        this.right = r;
    }

    public Boolean isRoot() {
        return (this.parent == null);
    }

    public Boolean isLeft() {
        return (this.parent.left == this);
    }

    public Boolean isRight() {
        return (this.parent.right == this);
    }


}


