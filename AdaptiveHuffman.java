import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;


/**
 * Created by menna on 10/23/16.
 */

public class AdaptiveHuffman extends Application {

    public static void main(String [] args) {
        launch(args);
    }

    public static File Compress(String path) {
        String original = "";
        try {
            original = new Scanner(new File(path)).useDelimiter("\\Z").next(); // convert everything in file into one string -> content
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String result = "";
        HashMap<Character, String> Codes = new HashMap<>();
        String code = (Integer.toBinaryString(original.getBytes()[0]));
        result += code;
        Codes.put(original.charAt(0), code); // update codes for nodes

        Tree tree = new Tree(100, 0, '!', null, "");

        tree.updateTree(original.charAt(0), true); // first occurrence bc first char obviously

        for (int i = 1; i < original.length(); i++) { // read symbol

            String value = Codes.get(original.charAt(i));
            if (value == null) { // first occurrence??
                Node nyt = tree.findNYT(); // find NYT
                result += nyt.binarycode; // result += nyt code
                code = (Integer.toBinaryString(original.getBytes()[i]));
                result += code;
                tree.updateTree(original.charAt(i), true);
                Codes.put(original.charAt(i), code); // add code in hashmap (for later ref - not first occurrence)
            }
            else { // not first occurrence
                String symbol = tree.findNode(original.charAt(i)).binarycode;
                result += symbol;
                tree.updateTree(original.charAt(i), false);
            }

        }

        File originFile = new File(path);
        String dir = originFile.getParent();
        File compressed = new File(dir + "\\compressed.txt"); // change to / on linux

        try(PrintWriter out = new PrintWriter(dir + "\\compressed.txt")) { // make a new compressed file in same working dir
            out.println(result); // write tags
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return compressed;
        //return result;
    }

    public static File deCompress(String path) {
        String code = "";
        try {
            code = new Scanner(new File(path)).useDelimiter("\\Z").next(); // convert everything in file into one string -> content
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String result = "";
        String firstCharCode = code.substring(0, 7); // bc 7 bits
        // wont work with other non alpha chars bc not 7 bits?

        Character firstChar = (char)Byte.parseByte(firstCharCode, 2);
        result += firstChar;
        Tree tree = new Tree(100, 0, '!', null, "");

        tree.updateTree(firstChar, true);
        int start = 7; int finish = 8;
        String searchFor;
        while(finish <= code.length()) {
            searchFor = code.substring(start, finish);
            Node node = tree.findSymbol(searchFor);
            if (node == null || node.symbol == '?') { // found no node continue
                finish++;
            }
            else if (node.symbol == '!') { // NYT
                String newCharCode = code.substring(finish, finish + 7);
                Character newChar = (char)Byte.parseByte(newCharCode, 2);
                result += newChar;
                tree.updateTree(newChar, true);
                start = finish + 7;
                finish = start + 1;
            }
            else {
                result += node.symbol;
                tree.updateTree(node.symbol, false);
                start = finish; finish++;
            }
        }

        File compressedfile = new File(path);
        String dir = compressedfile.getParent();
        File decompressed = new File(dir + "\\decompressed.txt");

        try(PrintWriter out = new PrintWriter(dir + "\\decompressed.txt")) { // make a decompressed file in same working dir
            out.println(result);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return decompressed;
        //return result;
    }

    private Desktop desktop = Desktop.getDesktop(); // opening files like this doesnt work on linux
    private void openFile(File file) {
        try {
            desktop.open(file); // use processbuilder for linux
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    Button browse, compress, decompress;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("LZ77 Compressor/Decompressor Tool");
        Label home = new Label("Choose file to compress/decompress:");
        TextField fillWithPath = new TextField();
        fillWithPath.setMaxWidth(200);
        browse = new Button("Browse");
        FileChooser choose = new FileChooser();
        browse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser.ExtensionFilter extFilter =
                        new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt"); // only choose text files
                choose.getExtensionFilters().add(extFilter);

                // show open file dialog
                File file = choose.showOpenDialog(primaryStage);
                fillWithPath.setText(file.getAbsolutePath()); // extension filter needed here

            }
        });
        VBox layout1  = new VBox(20);
        layout1.setAlignment(Pos.CENTER);
        // StackPane layout1 = new StackPane();
        compress = new Button("Compress");
        decompress = new Button("Decompress");

        compress.setOnAction(e -> {
            if (fillWithPath.getCharacters().toString().isEmpty()) { // FILE NOT CHOSEN
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a file.");
                alert.show();
            }
            else if (!new File(fillWithPath.getCharacters().toString()).isFile()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a valid path");
                alert.show();
            }
            else {
                File o = Compress(fillWithPath.getText());
                openFile(o);
            }


        });
        decompress.setOnAction(e -> {
            if(fillWithPath.getCharacters().toString().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a file.");
                alert.show();
            }
            else if (!new File(fillWithPath.getCharacters().toString()).isFile()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a valid path");
                alert.show();
            }
            else {
                File d = deCompress(fillWithPath.getText());
                openFile(d);
            }
        });


        layout1.getChildren().addAll(home, fillWithPath, browse, compress, decompress);
        Scene scene = new Scene(layout1, 300, 250);
        layout1.setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, CornerRadii.EMPTY, Insets.EMPTY))); //THIS IS SO FUN OMG - LOVE THE COLOR NAMES
        compress.setFont(Font.font("Lucida Grande")); // just for fun :) عشان انا فاضية
        decompress.setFont(Font.font("Lucida Grande"));
        browse.setFont(Font.font("Lucida Grande"));
        fillWithPath.setFont(Font.font("Lucida Grande"));
        home.setFont(Font.font("Lucida Grande"));
        primaryStage.setScene(scene);
        primaryStage.show();


    }

}



