import java.util.*;

/**
 * Created by menna on 10/23/16.
 */

public class Tree {

    private static Node root;

    public Tree(int num, int count, char s, Node p, String bcode) { // construct tree with p null!!
        root = new Node(num, count, s, p, bcode);
    }

    public Node add(char c) {
        // BFS to find NYT node
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        Boolean found = false;
        Node t = new Node();
        while (!q.isEmpty()) {
            t = q.remove();
            if (t.symbol == '!') { // if its nyt
                found = true;
                break;
            }
            else {
                if (t.right != null)
                    q.add(t.right);
                if (t.left != null)
                    q.add(t.left);
            }
        }
        if (t != null && found) {
            t.symbol = '?'; // not new nyt anymore
            t.counter = 1;
            Node newNYT = new Node(t.nodeNum - 2, 0, '!', t, t.binarycode + '0'); // old NYT count = 1
            t.setLeft(newNYT);
            Node newNode = new Node(t.nodeNum - 1, 1, c, t, t.binarycode + '1'); // symbol count = 1
            t.setRight(newNode);
            return t;
        }
        else {
            return null;
        }
    }

    public Node swapWith(Node x) {
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        Node t = new Node();
        while (!q.isEmpty()) {
            t = q.remove();
            if (t.swapConditions(x)) {
                return t;
            } else {
                if (t.right != null)
                    q.add(t.right);
                if (t.left != null)
                    q.add(t.left);
            }
        }
        return null;
    }

    public Node swap(Node x, Node y) { // :'((((((((((((( '
        Boolean xisleft = x.isLeft();
        Boolean yisleft = y.isLeft();
        Node parentofx = x.parent;
        Node parentofy = y.parent;
        if (xisleft) {
            parentofx.setLeft(y);
            x.parent = y.parent;
            if (yisleft) {
                parentofy.setLeft(x);
                y.parent = parentofx;
            }
            else {
                parentofy.setRight(x);
                y.parent = parentofx;
            }
        }
        else {
            parentofx.setRight(y);
            x.parent = y.parent;
            if (yisleft) {
                parentofy.setLeft(x);
                y.parent = parentofx;
            }
            else {
                parentofy.setRight(x);
                y.parent = parentofx;
            }        }

        fixTree();
        return x;
    }

    public void updateTree(Character symbol, Boolean first) {
        if (first) {
            Node parent = add(symbol); // splits, sets counters
            while (!parent.isRoot()) {
                //            System.out.println("first occurrence and iterating");
                parent = parent.parent;
                Node toSwap = swapWith(parent); // anything to swap with parent?
                if (toSwap != null) { // YES!
                    parent = swap(parent, toSwap);
                }
                parent.counter++;
            }
        }
        else {
            Node node = findNode(symbol);
            while (!node.isRoot()) {
                //          System.out.println("not first occurrence + iterating");
                Node toSwap = swapWith(node);
                if (toSwap != null) {
                    node = swap(node, toSwap);
                }
                node.counter++;
                node = node.parent;
                if (node.isRoot()) {
                    node.counter++;
                }
            }
        }
    }

    public void printTree() {
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        Node t = new Node();
        while (!q.isEmpty()) {
            t = q.remove();
            System.out.println(t.symbol + " " + t.binarycode + " " + t.counter + " " + t.nodeNum + " " + t + "parent is: " + t.parent);
            if (t.right != null)
                q.add(t.right);
            if (t.left != null)
                q.add(t.left);
        }
    }

    public void fixTree() {
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        int num = 100;
        Node n; n = q.peek();
        n.nodeNum = (num--);
        n.binarycode = "";
        while (!q.isEmpty()) {
            n = q.remove();
            if (n.right != null) {
                n.right.binarycode = n.binarycode + "1";
                n.right.nodeNum = num--;
                q.add(n.right);
            }
            if (n.left != null) {
                n.left.binarycode = n.binarycode + "0";
                n.left.nodeNum = num--;
                q.add(n.left);
            }
        }
    }


    public Node findNode(char c) {
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        Node t;
        while (!q.isEmpty()) {
            t = q.remove();
            if (t.symbol == c) {
                return t;
            }
            else {
                if (t.right != null)
                    q.add(t.right);
                if (t.left != null)
                    q.add(t.left);
            }
        }
        return null;
    }
    public Node findNYT() {
        // BFS to find NYT node
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        Node t = new Node();
        while (!q.isEmpty()) {
            t = q.remove();
            if (t.symbol == '!') {// if its nyt
                return t;
            }
            else {
                if (t.right != null)
                    q.add(t.right);
                if (t.left != null)
                    q.add(t.left);
            }
        }
        return null;
    }

    public Node findSymbol(String code) {
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        Node t;
        int counter = 0;
        while (!q.isEmpty()) {
            t = q.remove();
            if (t.binarycode.equals(code)) {
                return t;
            }
            else {
                if (code.charAt(counter) == '1') {
                    if (t.right != null)
                        q.add(t.right);
                    counter++;
                }
                else if (code.charAt(counter) == '0') {
                    if (t.left != null)
                        q.add(t.left);
                    counter++;
                }
            }
        }
        return null;
    }


}

