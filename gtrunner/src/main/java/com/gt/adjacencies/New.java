package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class New {
    private NodeVector vector;

    public New(String name, Object value, int order){
        this.vector = new NodeVector(NodeAttribute.I, name, order, (String)value);
    }

    public New(String name, Object[] value, int order){
        this.vector = new NodeVector(NodeAttribute.I, name, order, (String[])value);
    }

    public NodeVector getVector() {
        return vector;
    }
}
