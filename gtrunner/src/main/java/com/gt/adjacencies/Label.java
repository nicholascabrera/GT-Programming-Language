package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Label {
    private NodeVector vector;

    public Label(String labelName, int order){
        this.vector = new NodeVector(NodeAttribute.J, NodeAttribute.F, order, labelName);
    }

    public NodeVector getVector() {
        return vector;
    }
}
