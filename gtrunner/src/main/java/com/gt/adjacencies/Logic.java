package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Logic {
    private NodeVector vector;
    
    public Logic(String name, String expression, int order){
        this.vector = new NodeVector(NodeAttribute.N, name, order, expression);
    }

    public Logic(String expression, int order){
        this.vector = new NodeVector(NodeAttribute.N, NodeAttribute.D, order, expression);
    }

    public NodeVector getVector() {
        return vector;
    }
}
