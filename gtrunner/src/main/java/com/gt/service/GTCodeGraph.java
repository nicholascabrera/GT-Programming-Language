package com.gt.service;

import java.util.ArrayList;

import com.gt.adjacencies.ClosedBracket;
import com.gt.adjacencies.Comment;
import com.gt.adjacencies.Else;
import com.gt.adjacencies.If;
import com.gt.adjacencies.Input;
import com.gt.adjacencies.Logic;
import com.gt.adjacencies.Math;
import com.gt.adjacencies.New;
import com.gt.adjacencies.OpenBracket;
import com.gt.adjacencies.Output;

public class GTCodeGraph {
    private ArrayList<Input> inputList;
    private ArrayList<Output> outputList;
    private ArrayList<New> newList;
    private ArrayList<Math> mathList;
    private ArrayList<Logic> logicList;
    private ArrayList<If> ifList;
    private ArrayList<Else> elseList;
    private ArrayList<OpenBracket> obList;
    private ArrayList<ClosedBracket> cbList;
    private ArrayList<Comment> commentList;

    public void addInput(Input in){
        this.inputList.add(in);
    }
    public void addOutput(Output out){
        this.outputList.add(out);
    }

    public ArrayList<ClosedBracket> getCbList() {
        return cbList;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public ArrayList<Else> getElseList() {
        return elseList;
    }

    public ArrayList<If> getIfList() {
        return ifList;
    }

    public ArrayList<Input> getInputList() {
        return inputList;
    }

    public ArrayList<Logic> getLogicList() {
        return logicList;
    }

    public ArrayList<Math> getMathList() {
        return mathList;
    }

    public ArrayList<New> getNewList() {
        return newList;
    }

    public ArrayList<OpenBracket> getObList() {
        return obList;
    }

    public ArrayList<Output> getOutputList() {
        return outputList;
    }
}
