package com.gt.node;

import java.util.Arrays;

public class NodeVector {
    private NodeAttribute startNode, endNode;
    private String weight, nodeName;
    private String[] arrayWeight;
    private int order;

    public NodeVector(NodeAttribute startNode, NodeAttribute endNode, int order){
        this.startNode = startNode;
        this.endNode = endNode;
        this.order = order;
        this.weight = "";
        this.nodeName = "";
        this.arrayWeight = new String[0];
    }

    public NodeVector(NodeAttribute startNode, NodeAttribute endNode, int order, String weight){
        this.startNode = startNode;
        this.endNode = endNode;
        this.order = order;
        this.weight = weight;
        this.nodeName = "";
        this.arrayWeight = new String[0];
    }

    public NodeVector(NodeAttribute startNode, String nodeName, int order, String weight){
        this.startNode = startNode;
        this.nodeName = nodeName;
        this.order = order;
        this.weight = weight;
        this.arrayWeight = new String[0];
    }

    public NodeVector(NodeAttribute startNode, String nodeName, int order, String[] arrayWeight){
        this.startNode = startNode;
        this.nodeName = nodeName;
        this.order = order;
        this.arrayWeight = arrayWeight;
        this.weight = "";
    }

    public NodeAttribute getStartNode() {
        return startNode;
    }

    public NodeAttribute getEndNode() {
        return endNode;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public int getOrder() {
        return order;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String[] getArrayWeight() {
        return arrayWeight;
    }

    @Override
    public String toString() {
        String result = "";
        if(weight.equals("") && nodeName.equals("") && arrayWeight.length == 0){
            result = String.format("%s%d%s", startNode.toString(), order, endNode.toString());
        } else if (weight.equals("") && arrayWeight.length == 0){
            result = String.format("%s%d%s", startNode.toString(), order, nodeName);
        } else if (nodeName.equals("")) {
            result = String.format("%s%d%s:%s", startNode.toString(), order, endNode.toString(), weight);
        } else if (arrayWeight.length == 0){
            result = String.format("%s%d%s:%s", startNode.toString(), order, nodeName, weight);
        } else {
            result = String.format("%s%d%s:%s", startNode.toString(), order, nodeName, Arrays.toString(arrayWeight));
        }
        return result;
    }
}
