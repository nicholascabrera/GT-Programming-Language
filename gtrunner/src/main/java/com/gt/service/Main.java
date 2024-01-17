package com.gt.service;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run(){
        Compiler compiler = new Compiler("code");
        compiler.compile();

        new Interpreter().run(compiler.getAdjacencyList());
    }

    public void timedRun(){

        long startTime = System.currentTimeMillis();
        // compile the program
        Compiler compiler = new Compiler("code");
        compiler.compile();
        long endTime = System.currentTimeMillis();

        System.out.println("Compile Time: " + (endTime-startTime) + "ms");


        startTime = System.currentTimeMillis();
        //actually run the program
        new Interpreter().run(compiler.getAdjacencyList().getFilename());
        endTime = System.currentTimeMillis();

        System.out.println("Text File Interpreter Time: " + (endTime-startTime) + "ms");

        startTime = System.currentTimeMillis();
        //actually run the program
        new Interpreter().run(compiler.getAdjacencyList());
        endTime = System.currentTimeMillis();

        System.out.println("Adjacency List Interpreter Time: " + (endTime-startTime) + "ms");
    }
}
