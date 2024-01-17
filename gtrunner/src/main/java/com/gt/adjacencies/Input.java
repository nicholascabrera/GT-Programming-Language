package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Input {
    private NodeVector vector;

    public Input(NodeAttribute attribute, int order){
        this.vector = new NodeVector(NodeAttribute.E, attribute, order);
    }

    public Input(NodeAttribute attribute, int order, String weight){
        this.vector = new NodeVector(NodeAttribute.E, attribute, order, weight);
    }

    public NodeVector getVector() {
        return this.vector;
    }
}