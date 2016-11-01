import java.util.*;
/**
 * Created by menna on 10/23/16.
 */

public class AdaptiveHuffman {

    public static void main(String [] args) {
        String input;
        Scanner cin = new Scanner(System.in);
        input = cin.nextLine();
        String c = Integer.toBinaryString(input.getBytes()[0]);
       // System.out.println(Integer.toBinaryString(input.getBytes()[0]));
        String result = Compress(input);
        System.out.println(result);
        String res = deCompress(result);
        System.out.println(res);
        System.out.println(Integer.toBinaryString('!'));
    }

    public static String Compress(String original) {
        String result = "";
        HashMap<Character, String> Codes = new HashMap<>();
        String code = (Integer.toBinaryString(original.getBytes()[0]));
        result += code;
        Codes.put(original.charAt(0), code); // update codes for nodes

        Tree tree = new Tree(100, 0, '!', null, "");

        tree.updateTree(original.charAt(0), true); // first occurrence bc first char obviously

        for (int i = 1; i < original.length(); i++) { // read symbol

            String value = Codes.get(original.charAt(i));
            //System.out.println("done with: " + original.substring(0, i)); // debugging
            if (value == null) { // first occurrence??
                Node nyt = tree.findNYT(); // find NYT
                //result += " ";
                result += nyt.binarycode; // result += nyt code
                //result += " ";
                code = (Integer.toBinaryString(original.getBytes()[i]));
                result += code;
                tree.updateTree(original.charAt(i), true);
                Codes.put(original.charAt(i), code); // add code in hashmap (for later ref - not first occurrence)
            }
            else { // not first occurrence
                String symbol = tree.findNode(original.charAt(i)).binarycode;
                //result += " ";
                result += symbol;
                tree.updateTree(original.charAt(i), false);
            }

        }
//        System.out.println("------------------------");
//        System.out.println("FINAL TREE: :)");
//        tree.printTree();
//        System.out.println(result);

        return result;
    }

    public static String deCompress(String code) {
        String result = "";
        String firstCharCode = code.substring(0, 7);

        Character firstChar = (char)Byte.parseByte(firstCharCode, 2);
        result += firstChar;
        Tree tree = new Tree(100, 0, '!', null, "");

        tree.updateTree(firstChar, true);
        int start = 7; int finish = 8;
        String searchFor;
        while(finish <= code.length()) {
           // System.out.println("start: " + start + " finish: " + finish);
            searchFor = code.substring(start, finish);
            Node node = tree.findSymbol(searchFor);
            //System.out.println("searching for: " + searchFor);
            if (node == null || node.symbol == '?') { // found no node continue
                finish++;
            }
            else if (node.symbol == '!') { // NYT
                String newCharCode = code.substring(finish, finish + 7);
              //  System.out.println("new char code:" + newCharCode);
                Character newChar = (char)Byte.parseByte(newCharCode, 2);
                result += newChar;
              //  System.out.println(newChar);
                tree.updateTree(newChar, true);
                start = finish + 7;
                finish = start + 1;
            }
            else {
                //System.out.println("yay found " + node.symbol);
                result += node.symbol;
                tree.updateTree(node.symbol, false);
                start = finish; finish++;
            }
        }
        return result;
    }

}
