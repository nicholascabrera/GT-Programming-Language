package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Math {
    private NodeVector vector;
    private String expression;

    public Math(String name, String expression, int order){
        this.vector = new NodeVector(NodeAttribute.M, name, order, expression);
    }

    public String getExpression() {
        return expression;
    }

    public NodeVector getVector() {
        return vector;
    }
}
