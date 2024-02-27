package com.gt.service;

import com.gt.visualization.Grapher;

public class Main {
    public static void main(String[] args) {
        new Main().timedRun();
    }

    public void run(){
        Compiler compiler = new Compiler("factorial");
        compiler.compile();

        Grapher grapher = new Grapher(compiler.getAdjacencyList()); // this is occuring in a different thread.
        grapher.execute();

        new Interpreter().run(compiler.getAdjacencyList());
        
        while (!grapher.isDone()){  //this is so that we give the Grapher enough time to run, since the interpreter is so fast.
            // do nothing but run
        }
    }

    public void timedRun(){

        long startTime = System.currentTimeMillis();
        // compile the program
        Compiler compiler = new Compiler("factorial");
        compiler.compile();
        long endTime = System.currentTimeMillis();

        System.out.println("Compile Time: " + (endTime-startTime) + "ms");


        // startTime = System.currentTimeMillis();
        // //actually run the program
        // new Interpreter().run(compiler.getAdjacencyList().getFilename());
        // endTime = System.currentTimeMillis();

        // System.out.println("Text File Interpreter Time: " + (endTime-startTime) + "ms");

        Grapher grapher = new Grapher(compiler.getAdjacencyList()); // this is occuring in a different thread.
        grapher.execute();

        startTime = System.currentTimeMillis();
        //actually run the program
        new Interpreter().run(compiler.getAdjacencyList());
        endTime = System.currentTimeMillis();

        System.out.println("Adjacency List Interpreter Time: " + (endTime-startTime) + "ms");

        while (!grapher.isDone()){  //this is so that we give the Grapher enough time to run, since the interpreter is so fast.
            // do nothing but run
        }
    }
}
