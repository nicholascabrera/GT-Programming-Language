package com.gt.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.gt.adjacencies.AdjacencyList;
import com.gt.adjacencies.Branch;
import com.gt.adjacencies.ClosedBracket;
import com.gt.adjacencies.Else;
import com.gt.adjacencies.If;
import com.gt.adjacencies.Input;
import com.gt.adjacencies.Label;
import com.gt.adjacencies.Logic;
import com.gt.adjacencies.Math;
import com.gt.adjacencies.New;
import com.gt.adjacencies.OpenBracket;
import com.gt.adjacencies.Output;
import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;
import com.gt.regex.RegexPair;
import com.gt.regex.RegexType;

public class Compiler {
    private final RegexPair[] KEYWORD_REGEX = 
        {
            new RegexPair(" {0,}in {0,}\\[ {0,}[A-Na-n] {0,}\\] {0,}", RegexType.IN),
            new RegexPair(" {0,}in {0,}\\[ {0,}[A-Na-n] {0,}, {0,}[0-9]+ {0,}\\] {0,}", RegexType.WEIGHTED_IN),
            new RegexPair(" {0,}out {0,}\\[ {0,}[A-Na-n] {0,}\\] {0,}", RegexType.OUT),
            new RegexPair(" {0,}out {0,}\\[ {0,}[A-Na-n] {0,}, {0,}[0-9]+ {0,}\\] {0,}", RegexType.WEIGHTED_OUT),
            new RegexPair(" {0,}new {0,}\\[[A-Za-z0-9]+ ?= ?[A-Z a-z0-9]+\\] {0,}", RegexType.NEW),
            new RegexPair(" {0,}new {0,}\\[[A-Z a-z0-9]+ {0,}= {0,}\\{[A-Z a-z0-9]+( {0,}, {0,}[A-Z a-z0-9]+){0,}\\} {0,}\\] {0,}", RegexType.NEW_ARRAY),
            new RegexPair(" {0,}math {0,}\\[ {0,}[A-Z a-z0-9]+ {0,}= {0,}[0-9 A-Za-z()+\\-*\\/^]+\\] {0,}", RegexType.MATH),
            new RegexPair(" {0,}logic {0,}\\[ {0,}[A-Z a-z0-9]+ {0,}= {0,}[A-Z a-z0-9<>=&|!()]+ {0,}\\] {0,}", RegexType.LOGIC_STOW),
            new RegexPair(" {0,}logic {0,}\\[ {0,}[A-Z 0-9a-z<>=&|!()]+ {0,}\\] {0,}", RegexType.LOGIC),
            new RegexPair(" {0,}if {0,}\\[ {0,}\\] {0,}", RegexType.IF),
            new RegexPair(" {0,}[{] {0,}", RegexType.OPEN_BRACKET),
            new RegexPair(" {0,}", RegexType.EMPTY),
            new RegexPair(" {0,}[}] {0,}", RegexType.CLOSED_BRACKET),
            new RegexPair(" {0,}branch {0,}\\[ {0,}[A-Za-z]+ {0,}\\] {0,}", RegexType.BRANCH),
            new RegexPair(" {0,}for {0,}\\[ {0,}[A-Za-z0-9]+ {0,}in {0,}-?[A-Za-z0-9]+ {0,}\\] {0,}", RegexType.FOR),
            new RegexPair(" {0,}for {0,}\\[ {0,}[A-Za-z0-9]+ {0,}in {0,}-?[A-Za-z0-9]+ {0,}-> {0,}-?[A-Za-z0-9]+ {0,}\\] {0,}", RegexType.FOR_ARROW)
        };
    
    private Stack<String> read;
    private BufferedReader reader;
    private AdjacencyList adjacencyList;
    private static HashMap<String, NodeVector> labelMap;

    public Compiler(String filename){
        // from any given string, the goal is to convert it into a series of tokens, or objects.
        // given the "in", "out", and "new" functions, i have to determine what the parameters for each might be.
        // seeing as there can usually be function calls as a parameter of another function, given it returns the correct thing
        // i must devise a way to encapsulate parameters correctly.

        // System.out.println( //compiler reads until it reaches an open bracket, at which point the substring it just read is placed into the stack, including the bracket
        //     new AdjacencyList(
        //         new NodeVector(
        //             (   //i need a rule which states that if there is no function before the paranethesis in the stack, then it does nothing.
        //                 NodeAttribute.A)    //when the compiler reads a closed bracket, it pops from the stack and forms a token or object based on the command contained in the brackets.
        //                                     //this pop would result in nothing happening
        //                     , NodeAttribute.B)  //this pop would result in a new NodeVector being made. I need a rule which states that only one function may be contained
        //                                         //within the popped string
        //                         )   //this pop would create a new AdjacencyList
        //                             );  //this pop would print the output to the command line.

        this.read = new Stack<>();
        this.adjacencyList = new AdjacencyList(filename);
        labelMap = new HashMap<String,NodeVector>();
        try {
            this.reader = new BufferedReader(new FileReader("C:\\Users\\n" + 
                    "cabr\\OneDrive\\Documents\\Programming\\Java\\GT\\gtrunner\\src\\main\\java\\com\\gt\\built in functions\\" + filename + ".gt"));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean compile(){
        int lineNumber = 1;

        try {
            //read the first line
            String line = reader.readLine();
            Stack<NodeVector> logicStack = new Stack<>();
            int keywordNum = 1;

            //read each line
            lineloop:
            while(line != null){
                /**
                 * within each line, run through each character until an open bracket is found.
                 * add the substring from where the read began to just after the open bracket.
                 * if a closing bracket is found, pop from the stack and combine what is popped
                 *      with the closing bracket. If there is nothing before the open bracket,
                 *      nothing occurs. If there is a valid line of code when the two things are
                 *      combined, tokenize it and add it to the adjacency list. repeat until the
                 *      line is complete.
                 * 
                 * 
                 */
                boolean isMatch = false;
                int start = 0;

                outerloop:
                for(int i = 0; i < line.length(); i++){     //read through each character for the brackets
                    if(Compiler.inputValidation(Pattern.compile(" {0,}\\/\\/"), line.substring(start, i))) {
                        //read the next line
                        line = reader.readLine();
                        lineNumber += 1;
                        continue lineloop;
                    } else if(line.charAt(i) == '['){
                        this.read.push(line.substring(start, i+1));
                        start = i+1;
                    } else if(line.charAt(i) == ']' && !read.isEmpty()){
                        String command = this.read.pop();
                        command = command.concat(line.substring(start, i+1));

                        for(RegexPair pair : KEYWORD_REGEX){
                            String pattern = pair.getRegexString();
                            Pattern commandPattern = Pattern.compile(pattern);

                            if(Compiler.inputValidation(commandPattern, command)){
                                isMatch = true;
                                switch(pair.getRegexType()){
                                    case IN:
                                        this.adjacencyList.addNodeVector(new Input(NodeAttribute.getNodeAttribute(line.substring(start, i)), keywordNum).getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case NEW:
                                        String[] var = line.substring(start, i).split(" {0,}= {0,}");
                                        this.adjacencyList.addNodeVector(new New(var[0], var[1], keywordNum).getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case NEW_ARRAY:
                                        var = line.substring(start, i).split(" {0,}= {0,}");
                                        var[1] = var[1].replaceAll("\\{", "");
                                        var[1] = var[1].replaceAll("\\}", "");
                                        String[] elements = var[1].split(" {0,}, {0,}");
                                        this.adjacencyList.addNodeVector(new New(var[0], elements, keywordNum).getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case OUT:
                                        this.adjacencyList.addNodeVector(new Output(NodeAttribute.getNodeAttribute(line.substring(start, i)), keywordNum).getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case WEIGHTED_IN:
                                        this.adjacencyList.addNodeVector(new Input(NodeAttribute.getNodeAttribute(line.substring(start, start+1)), keywordNum, line.substring(start+3, i)).getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case WEIGHTED_OUT:
                                        this.adjacencyList.addNodeVector(new Output(NodeAttribute.getNodeAttribute(line.substring(start, start+1)), keywordNum, line.substring(start+3, i)).getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case MATH:
                                        var = line.substring(start, i).split(" {0,}= {0,}");
                                        this.adjacencyList.addNodeVector(new Math(var[0], var[1], keywordNum).getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case LOGIC_STOW:
                                        var = line.substring(start, i).split(" {0,}= {0,}");
                                        this.adjacencyList.addNodeVector(new Logic(var[0], var[1], keywordNum).getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case LOGIC:
                                        /**
                                         * unfortunately, there is overlap between the logic and logic_stow regex formulas. When an adjacency satisfies logic stow, it will also satisfy logic.
                                         *  to avoid getting repeat adjacencies in the adjacency file, checking to see if a logic also satisfies a logic_stow is necessary.
                                         */
                                        if(!Compiler.inputValidation(Pattern.compile(" {0,}logic {0,}\\[ {0,}[A-Z a-z]+ {0,}= {0,}[A-Z a-z0-9s<>=&|!()]+ {0,}\\] {0,}"), command)){
                                            this.adjacencyList.addNodeVector(new Logic(line.substring(start, i), keywordNum).getVector());
                                            start = i+1;
                                            keywordNum+=1;
                                            continue outerloop;
                                        }
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case IF:
                                        If ifObject = new If(keywordNum);
                                        this.adjacencyList.addNodeVector(ifObject.getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case FOR:
                                        
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case FOR_ARROW:
                                        
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    case BRANCH:
                                        this.adjacencyList.addNodeVector(new Branch(line.substring(start, i), keywordNum).getVector());
                                        start = i+1;
                                        keywordNum+=1;
                                        continue outerloop;
                                    default:
                                        /** This is where empty space and open brackets will be parsed out, in other words, do nothing here. */
                                        break;
                                }
                            }
                        }
                        start = i+1;
                    } else if (Compiler.inputValidation(Pattern.compile(" {0,}[{] {0,}"), line.substring(start, i+1))) {
                        isMatch = true;
                        NodeVector obNodeVector = new OpenBracket(keywordNum).getVector();
                        this.adjacencyList.addNodeVector(obNodeVector);
                        logicStack.add(obNodeVector);
                        keywordNum += 1;
                        start = i+1;
                    } else if (Compiler.inputValidation(Pattern.compile(" {0,}[}] {0,}"), line.substring(start, i+1))) {
                        isMatch = true;
                        String reference = Integer.toString(logicStack.pop().getOrder());
                        this.adjacencyList.addNodeVector(new ClosedBracket(keywordNum, reference).getVector());
                        keywordNum += 1;
                        start = i+1;
                    } else if (Compiler.inputValidation(Pattern.compile(" {0,}else {0,}"), line.substring(start, i+1))) {
                        isMatch = true;
                        this.adjacencyList.addNodeVector(new Else(keywordNum).getVector());
                        keywordNum += 1;
                        start = i+1;
                    } else if (Compiler.inputValidation(Pattern.compile(" {0,}[A-Za-z]+: {0,}"), line.substring(start, i+1))) {
                        isMatch = true;
                        this.adjacencyList.addNodeVector(new Label(line.substring(start, i), keywordNum).getVector());
                        labelMap.put(line.substring(start, i), this.adjacencyList.get(this.adjacencyList.size()-1));
                        keywordNum += 1;
                        start = i+1;
                    }
                }

                if (Compiler.inputValidation(Pattern.compile(" {0,}"), line)){
                    isMatch = true;
                }

                if(!isMatch){
                    System.out.println("Syntax error on line " + lineNumber + ".");
                    System.exit(0);
                    return false;
                }

                //read the next line
                line = reader.readLine();
                lineNumber += 1;
            }

            adjacencyList.close();
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + "\nline: " + lineNumber, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return true;
    }

    public AdjacencyList getAdjacencyList() {
        return adjacencyList;
    }

    public static boolean inputValidation(Pattern pattern, String input){
        if(pattern.matcher(input).matches()){
            return true;
        }

        return false;
    }

    public static HashMap<String, NodeVector> getLabelMap() {
        return labelMap;
    }

    public static boolean inputFinder(Pattern pattern, String input){
        if(pattern.matcher(input).find()){
            return true;
        }

        return false;
    }
}