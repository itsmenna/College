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
            Node newNYT = new Node(t.nodeNum - 2, 0, '!', t, t.binarycode + '0'); // old NYT count = 1 SAH
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
            //stem.out.println("looking for node to swap with");
            t = q.remove();
            if (t.swapConditions(x)) {
                // System.out.println("found node to swap w/" + t.symbol);
                return t;
            } else {
                if (t.right != null)
                    q.add(t.right);
                if (t.left != null)
                    q.add(t.left);
            }
        }
        //System.out.println("found no node");
        // return x.parent;
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






//        System.out.println("Y is " + y.symbol);
//        System.out.println("X is " + x.symbol);
//        Boolean LeftY = false;
//        if (y.isLeft()) LeftY = true;
//        System.out.println("SHOULD BE FALSE" + LeftY);
//        Node parentofX = x.parent;
//        Node parentofY = y.parent;
//        Node tempx = x;
//        Node tempy = y;
//        Node tempY = new Node();
//        tempY = y;
//        System.out.println("should be true :/ ");
//        System.out.println(tempx.equals(x));
//        System.out.println(x.left.binarycode + " " + x.right.binarycode); // y???
//        System.out.println("BELOW SHOULD BE: 00 AND 01");
//        System.out.println(tempx.left.binarycode + " " + tempx.right.binarycode);
//        System.out.println(y.symbol + " " + y.binarycode); // Y IS A AND X IS ?
//        //   printTree();
//        Node toReturn = new Node();
//        if (x.isLeft()) {
//          //  System.out.println("before: " + parentofX.left + " " + parentofX.left.symbol);
//            parentofX.setLeft(y);
//            //System.out.println("after: " + parentofX.left + " " + parentofX.left.symbol);
//            //System.out.println("after: " + x + " " + x.symbol);
//            //   toReturn = parentofX.left;
//            //System.out.println("before "+y.parent);
//
//            y.parent = parentofX;
//            //System.out.println("after " +y.parent);
//            //y.parent.right = x;
//            //x.parent = y.parent; ghalat
//            //System.out.println(tempY + " " + y + " " + tempy);
//            x.parent = parentofY;
//            if (LeftY) {
//            //    parentofX
//            }
//            else {
//                parentofX.setRight(y);
//            }
//            //parentofY.left=x;
//        }
//        else if (x.isRight()) {
//            parentofX.setRight(y);
//            y.parent = parentofX;
//            // toReturn = parentofX.right;
//        }
////        if (tempy.isLeft()) {
////            System.out.println("TEST ERROR DSFAKSDF;A");
////            System.out.println(tempy.parent + " " + tempy.left + " " + tempy.right);
////            System.out.println(tempy.symbol + " " + tempy.binarycode + " " + tempy.isLeft() + " " + tempy.isRight());
////            parentofY.setLeft(x);
////            x.parent = parentofY;
////        }
////        else if (tempy.isRight()) {
////            System.out.println("WHAT THE HELL???"); /// how on earth is tempy both left and right mesh fahma -_-
////            //System.out.println(y + " " + y.symbol + " " + y.nodeNum);
////            parentofY.setRight(x);
////            x.parent = parentofY;
////        }

        //System.out.println("Press Any Key To Continue...");

//     new java.util.Scanner(System.in).nextLine();

        fixTree();
        // return toReturn;
        // return y;
        //System.out.println(x.binarycode + "????????????? " + x.symbol + x.isLeft() + " " + x.isRight());
        //System.out.println(y.binarycode + "!!!!!!!" + y.symbol + y.isLeft() + " " + y.isRight());
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
//                System.out.println(node); // parent is the node itself ://// thres probably a prob w/ the swapping
//                System.out.println(node.parent);
                node = node.parent;
                //System.out.println(node);
                if (node.isRoot()) {
                    node.counter++;
                }
            }
        }
    }

    public void printTree() {
        //System.out.println("THIS IS THE TREE: ");
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        Node t = new Node();
        while (!q.isEmpty()) {
            //  System.out.println("printing tree");
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
            //System.out.println("FIXING TREE");
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
            // System.out.println("looking for this node: " + c);
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
            // System.out.println("looking for the NYT...");
            t = q.remove();
            if (t.symbol == '!') {// if its nyt
                //   System.out.println("found NYT!");
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

