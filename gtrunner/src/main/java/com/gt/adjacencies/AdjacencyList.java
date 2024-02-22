package com.gt.adjacencies;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

import java.util.ArrayList;

import com.gt.node.NodeVector;

public class AdjacencyList extends ArrayList<NodeVector>{
    private String filename;

    public AdjacencyList(String filename){
        try {
            this.filename = "C:\\Users\\ncabr\\OneDrive\\Documents\\Programming\\Java\\GT\\gtrunner\\gtarget\\" + filename + ".aja";
            File myObj = new File(this.filename);
            //use Files.mismatch
            
            if (myObj.createNewFile()) {

            } else {
                myObj.delete();
                File myNewObj = new File(this.filename);
                myNewObj.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void addNodeVector(NodeVector vector){
        add(vector);
    }

    public String getFilename() {
        return filename;
    }

    public void close(){
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String getList(){
        return toString();
    }

    public ArrayList<NodeVector> getArrayList(){
        return this;
    }

    public void displayList(){
        System.out.println(toString());
    }
}
