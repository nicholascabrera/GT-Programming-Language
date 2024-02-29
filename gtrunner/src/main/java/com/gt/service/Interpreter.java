package com.gt.service;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;
import com.gt.adjacencies.AdjacencyList;
import com.gt.evaluators.LogicEvaluator;
import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Interpreter {

    public Interpreter(){}

    /**
     * The run function takes in a file name and checks to see if it is formatted correctly for an
     *  adjacency file. If it is, it runs through each adjacency and builds it corresponding keyword
     *  or function. The utility of having it read from a file rather than simply passing an object is
     *  the ability to run any program at any time. Objects only exist while the program is running, but
     *  a file containing all of the adjacencies can be built and then saved forever. This way,
     *  a different computer can run my code, simply by sending a text file.
     * @param filename
     * @return
     */
    public boolean run(String filename){
        Pattern adjacencyFilePattern = Pattern.compile("\\[[A-Za-z0-9]{3,}(:[A-Za-z+<>.,\\[\\]!=\\-*\\/^()0-9 ]+)?(, [A-Za-z0-9]{3,}(:[A-Za-z+<>!=\\[\\].\\-*\\/^,()0-9 ]+)?){0,}\\]");
        int X = 0, Y = 0, Z = 0;
        boolean temp = false;
        boolean skipping = false;
        StaticVariableSet<Object> userVariables = new StaticVariableSet<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            reader.close();
            if(Compiler.inputValidation(adjacencyFilePattern, line)){
                //get rid of the brackets in the way, as they are no longer necessary.
                line = line.replaceAll("\\[", "");
                line = line.replaceAll("\\]", "");
                //separate each command into a list
                ArrayList<String> adjacencies = new ArrayList<String>(Arrays.asList(line.split(", ")));
                //run through each command/keyword
                for(int i = 0; i < adjacencies.size(); i++){
                    String keyword = adjacencies.get(i);
                    /** if we are skipping lines and the current keyword is a "}", stop skipping and continue to the next line. */
                    if (skipping && this.isOpenBracket(keyword)) {
                        this.concatTruth(adjacencies, i, temp);
                        continue;
                    } else if(skipping && this.isClosedBracket(keyword)){
                        skipping = false;
                        continue;
                    } else if (skipping && !this.isClosedBracket(keyword)){
                        /** if we are skipping lines and the current keyword is NOT a "}", continue skipping. */
                        continue;
                    } else {
                        /**if we are NOT skipping lines, then interpret this line of code(ie, do nothing.) */
                    }
                    String endNodeString;
                    //the only command that has an E at the start is "in"
                    if(keyword.charAt(0) == 'E' && this.isEndNode(keyword)){
                        endNodeString = NodeAttribute.getNodeString(NodeAttribute.getNodeAttribute(Character.toString(keyword.charAt(2))));
                        if(!keyword.contains(":")){
                            System.out.printf("You take 1 %s damage!", endNodeString);
                            X += 1;
                        } else {
                            System.out.printf("You take %s %s damage!", NodeAttribute.getNodeDamage(NodeAttribute.getNodeAttribute(Character.toString(keyword.charAt(2))), Integer.parseInt(keyword.substring(keyword.indexOf(":") + 1))), endNodeString);
                            X += Integer.parseInt(keyword.substring(4));
                        }
                        System.out.println();
                        continue;
                    }
                    //two commands have I at the start, "new" and "out"
                    else if(keyword.charAt(0) == 'I'){
                        //out should only utilize nodes.
                        if(this.isEndNode(keyword)){
                            endNodeString = NodeAttribute.getNodeString(NodeAttribute.getNodeAttribute(Character.toString(keyword.charAt(2))));
                            if(!keyword.contains(":")){
                                System.out.printf("You deal %d %s damage!", X+Y+Z, endNodeString);
                                X = 0;
                                Y = 0;
                                Z = 0;
                            } else {
                                int dmg = Integer.parseInt(keyword.substring(keyword.indexOf(":") + 1));
                                if(dmg <= X+Y+Z){
                                    System.out.printf("You deal %d %s damage!", dmg, endNodeString);
                                } else {
                                    System.out.println("Not enough input for your output.");
                                    continue;
                                }
                            }
                            System.out.println();
                            continue;
                        }
                        //new has to do with variable names and values.
                        else {
                            keyword = keyword.replaceAll("[A-N][0-9]+", "");
                            String[] newUserVariable = keyword.split(":");
                            userVariables = this.setVariable(userVariables, newUserVariable);
                            // System.out.println(newUserVariable[0] + " = " + userVariables.get(newUserVariable[0]));  //helps with seeing if everything is correct if i print
                            continue;
                        }
                    }
                    else if (keyword.charAt(0) == 'M') {
                        keyword = keyword.replaceAll("[A-N][0-9]+", "");
                        String[] newExpression = keyword.split(":");    //[0] is where the result is stored, [1] is the expression itself.
                        DoubleEvaluator evaluator = new DoubleEvaluator();
                        Double result = evaluator.evaluate(newExpression[1], userVariables);
                        userVariables.set(newExpression[0], result);
                        // System.out.println(newExpression[0] + " = " + newExpression[1] + " = " + result);  //helps with seeing if everything is correct if i print
                        continue;
                    }
                    else if (keyword.charAt(0) == 'N') {
                        /** This line is basically asking if there is a node name there or a node attribute. */
                        if(this.isEndNode(keyword)){   //if there is an attribute...
                            keyword = keyword.replaceAll("[A-N][0-9]+", "");
                            if(keyword.charAt(0) == 'D'){
                                keyword = keyword.substring(keyword.indexOf(':')+1);
                                LogicEvaluator evaluator = new LogicEvaluator();
                                temp = (boolean) evaluator.evaluate(keyword, userVariables);
                                // System.out.println(keyword + ": " + temp);  //helps with seeing if everything is correct if i print
                            } else if (keyword.charAt(0) == 'K'){
                                /**
                                 * inside every if statement there must be a logical statement. in the adjacency list its right before it,
                                 * which will change the value of the temp bool according to the logic. the "if" adjacency will then
                                 * do something if that logical statement is true, or nothing if its false.
                                 *      something: run the code within the open and closed brackets.
                                 *      nothing: skip the adjacencies until a closed bracket is reached.
                                 */
                                skipping = !temp;
                            } else if(keyword.charAt(0) == 'L'){
                                String closedBracket = adjacencies.get(i-1);
                                int referenceIndex = Integer.parseInt(closedBracket.substring(closedBracket.indexOf(":")+1));
                                String referencedBracket = adjacencies.get(referenceIndex-1);
                                Boolean referenceBool = Boolean.parseBoolean(referencedBracket.substring(referencedBracket.indexOf(":")+1));

                                skipping = referenceBool;
                            } else if(keyword.charAt(0) == 'I'){    /** Open bracket, we must record the truth value for later. */
                                this.concatTruth(adjacencies, i, temp);
                            }
                        } else {
                            /** Separate the Start Node and order from the end node and expression */
                            keyword = keyword.replaceAll("[A-N][0-9]+", "");
                            String[] newExpression = keyword.split(":");    //[0] is where the result is stored, [1] is the expression itself.
                            LogicEvaluator evaluator = new LogicEvaluator();
                            userVariables.set(newExpression[0], (boolean) evaluator.evaluate(newExpression[1], userVariables));
                            // System.out.println(newExpression[1] + ": " + userVariables.get(newExpression[0]));  //helps with seeing if everything is correct if i print
                        }
                    }
                    else {
                        
                    }
                }
            } else {
                throw new Exception("File not formatted correctly!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean run(AdjacencyList adjacencyList){
        int X = 0, Y = 0, Z = 0;
        boolean temp = false;
        boolean skipping = false;
        StaticVariableSet<Object> userVariables = new StaticVariableSet<>();
        StaticVariableSet<Object[]> userArrays = new StaticVariableSet<>();

        try {
            ArrayList<NodeVector> adjacencies = adjacencyList.getArrayList();
            // run through each command/keyword
            for (int i = 0; i < adjacencies.size(); i++) {
                NodeVector keyword = adjacencies.get(i);
                NodeAttribute startNode = keyword.getStartNode();
                /**
                 * if we are skipping lines and the current keyword is a "}", stop skipping and
                 * continue to the next line.
                 */
                if (skipping && this.isOpenBracket(keyword.toString())) {
                    this.concatTruthNode(adjacencies, i, temp);
                    continue;
                } else if (skipping && this.isClosedBracket(keyword.toString())) {
                    skipping = false;
                    continue;
                } else if (skipping && !this.isClosedBracket(keyword.toString())) {
                    /**
                     * if we are skipping lines and the current keyword is NOT a "}", continue
                     * skipping.
                     */
                    continue;
                } else {
                    /**
                     * if we are NOT skipping lines, then interpret this line of code(ie, do
                     * nothing.)
                     */
                }
                String endNodeString;
                // the only command that has an E at the start is "in"
                if (startNode == NodeAttribute.E) {
                    endNodeString = NodeAttribute.getNodeString(keyword.getEndNode());
                    if (keyword.getWeight().equals("")) {
                        System.out.printf("You take 1 %s damage!", endNodeString);
                        X += 1;
                    } else {
                        System.out.printf("You take %s %s damage!", NodeAttribute.getNodeDamage(keyword.getEndNode(), Integer.parseInt(keyword.getWeight())), endNodeString);
                        X += Integer.parseInt(keyword.getWeight());
                    }
                    System.out.println();
                    continue;
                }
                // two commands have I at the start, "new" and "out"
                else if (startNode == NodeAttribute.I) {
                    // out should only utilize nodes.
                    if (this.isEndNode(keyword.toString())) {
                        endNodeString = NodeAttribute.getNodeString(keyword.getEndNode());
                        if (keyword.getWeight().equals("")) {
                            System.out.printf("You deal %d %s damage!", X + Y + Z, endNodeString);
                            X = 0;
                            Y = 0;
                            Z = 0;
                        } else {
                            int dmg = Integer.parseInt(keyword.getWeight());
                            if (dmg <= X + Y + Z) {
                                System.out.printf("You deal %d %s damage!", dmg, endNodeString);
                            } else {
                                System.out.println("Not enough input for your output.");
                                continue;
                            }
                        }
                        System.out.println();
                        continue;
                    }
                    // new has to do with variable names and values.
                    else {
                        /** If the new variable is an array, it wont have a weight. */
                        if(keyword.getWeight().equals("")){
                            userArrays = this.setVariable(userArrays, keyword.getNodeName(), keyword.getArrayWeight());
                        } else {
                            String[] newUserVariable = {keyword.getNodeName(), keyword.getWeight()};
                            userVariables = this.setVariable(userVariables, newUserVariable);
                            // System.out.println(newUserVariable[0] + " = " + userVariables.get(newUserVariable[0])); //helps with seeing if everything is
                            // correct if i print
                        }
                        continue;
                    }
                } else if (startNode == NodeAttribute.M) {
                    String[] newExpression = {keyword.getNodeName(), keyword.getWeight()}; // [0] is where the result is stored, [1] is the expression itself.
                    DoubleEvaluator evaluator = new DoubleEvaluator();
                    Double result = evaluator.evaluate(newExpression[1], userVariables);
                    userVariables.set(newExpression[0], result);
                    // System.out.println(newExpression[0] + " = " + newExpression[1] + " = " + result); //helps with seeing if everything is correct if i print
                    continue;
                } else if (startNode == NodeAttribute.N) {
                    /**
                     * This line is basically asking if there is a node name there or a node
                     * attribute.
                     */
                    if (this.isEndNode(keyword.toString())) { // if there is an attribute...
                        if (keyword.getEndNode() == NodeAttribute.D) {
                            LogicEvaluator evaluator = new LogicEvaluator();
                            temp = (boolean) evaluator.evaluate(keyword.getWeight(), userVariables);
                            // System.out.println(keyword + ": " + temp); //helps with seeing if everything
                            // is correct if i print
                        } else if (keyword.getEndNode() == NodeAttribute.K) {
                            /**
                             * inside every if statement there must be a logical statement. in the adjacency
                             * list its right before it,
                             * which will change the value of the temp bool according to the logic. the "if"
                             * adjacency will then
                             * do something if that logical statement is true, or nothing if its false.
                             * something: run the code within the open and closed brackets.
                             * nothing: skip the adjacencies until a closed bracket is reached.
                             */
                            skipping = !temp;
                        } else if (keyword.getEndNode() == NodeAttribute.L) {
                            NodeVector closedBracket = adjacencies.get(i - 1);
                            int referenceIndex = Integer.parseInt(closedBracket.getWeight());
                            NodeVector referencedBracket = adjacencies.get(referenceIndex - 1);
                            Boolean referenceBool = Boolean.parseBoolean(referencedBracket.getWeight());

                            skipping = referenceBool;
                        } else if (keyword.getEndNode() == NodeAttribute.I) {
                            /** Open bracket, we must record the truth value for later. */
                            this.concatTruthNode(adjacencies, i, temp);
                        }
                    } else {
                        String[] newExpression = {keyword.getNodeName(), keyword.getWeight()}; // node name is where the result is stored, the weight is the expression itself.
                        LogicEvaluator evaluator = new LogicEvaluator();
                        userVariables.set(newExpression[0], (boolean) evaluator.evaluate(newExpression[1], userVariables));
                        // System.out.println(newExpression[1] + ": " + userVariables.get(newExpression[0])); //helps with seeing if everything is
                        // correct if i print
                    }
                } else if (startNode == NodeAttribute.D){
                    if(this.isEndNode(keyword.toString())){
                        /* Print statement! */
                        if(keyword.getEndNode() == NodeAttribute.G){
                            if(userVariables.get(keyword.getWeight()) == null && userArrays.get(keyword.getWeight()) == null){
                                System.out.println(keyword.getWeight());
                            } else {
                                String result = (userVariables.get(keyword.getWeight()) == null) ? userArrays.get(keyword.getWeight()).toString() : userVariables.get(keyword.getWeight()).toString();
                                System.out.println(result);
                            }
                        }
                    }
                } else if (startNode == NodeAttribute.J){
                    if(this.isEndNode(keyword.toString())){
                        if (keyword.getEndNode() == NodeAttribute.B){
                            /** This is a branch statement. We need to find the corresponding label. */
                            String correspondingLabel = keyword.getWeight();
                            /** Look at the label map. We need to find the location of the label in the adjacency list. */
                            NodeVector labelVector = Compiler.getLabelMap().get(correspondingLabel);
                            /** We have the label vector. We need to travel to its index in the adjacency list. */
                            i = labelVector.getOrder()-1;
                            continue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
    
    private void concatTruthNode(ArrayList<NodeVector> adjacencies, int i, boolean temp) {
        adjacencies.get(i).setWeight(Boolean.toString(temp));
    }

    /**
     * Uses regex to determine whether or not this tag is a closed bracket
     * @param keyword
     * @return
     */
    private boolean isClosedBracket(String keyword) {
        return Compiler.inputValidation(Pattern.compile(" {0,}N[0-9]+E {0,}:[0-9]+"), keyword);
    }

    /**
     * Uses regex to determine whether or not this tag is an open bracket
     * @param keyword
     * @return
     */
    private boolean isOpenBracket(String keyword) {
        return Compiler.inputValidation(Pattern.compile(" {0,}N[0-9]+I {0,}"), keyword);
    }

    public void concatTruth (ArrayList<String> adjacencies, int index, boolean temp){
        adjacencies.set(index, adjacencies.get(index).concat(":" + temp));
    }

    public StaticVariableSet<Object[]> setVariable(StaticVariableSet<Object[]> array, String arrayName, String[] userArray){
        Object[] objectArray = new Object[userArray.length];

        Pattern numberPattern = Pattern.compile("-?[0-9.]+");
        Pattern boolPattern = Pattern.compile("true|false");
        Pattern stringPattern = Pattern.compile("[A-Za-z 0-9]+");

        for(int i = 0; i < userArray.length; i++){
            String s = userArray[i];
            if(Compiler.inputValidation(numberPattern, s)){
                objectArray[i] = Integer.parseInt(s);
            } else if (Compiler.inputValidation(boolPattern, s)){
                objectArray[i] = Boolean.parseBoolean(s);
            } else if (Compiler.inputValidation(stringPattern, s)){
                objectArray[i] = s;
            } else {
                System.out.println("Error in array \"" + arrayName + "\" at index: " + i);
            }
        }

        array.set(arrayName, objectArray);
        return array;
    }

    public StaticVariableSet<Object> setVariable(StaticVariableSet<Object> variables, String[] newUserVariable){
        Pattern numberPattern = Pattern.compile("-?[0-9.]+");
        Pattern boolPattern = Pattern.compile("true|false");
        Pattern stringPattern = Pattern.compile("[A-Za-z 0-9]+");

        if(Compiler.inputValidation(numberPattern, newUserVariable[1])){
            variables.set(newUserVariable[0], Double.parseDouble(newUserVariable[1]));
        } else if (Compiler.inputValidation(boolPattern, newUserVariable[1])){
            variables.set(newUserVariable[0], Boolean.parseBoolean(newUserVariable[1]));
        } else if (Compiler.inputValidation(stringPattern, newUserVariable[1])){
            variables.set(newUserVariable[0], newUserVariable[1]);
        }

        return variables;
    }

    public boolean isEndNode(String keyword){
        return Compiler.inputValidation(Pattern.compile(" {0,}[A-Z][0-9]+[A-Z] {0,}"), keyword) 
            || Compiler.inputValidation(Pattern.compile(" {0,}[A-Z][0-9]+[A-Z] {0,}"), keyword.substring(0, keyword.indexOf(":")));
    }
}