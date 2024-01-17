package com.gt.evaluators;

import java.util.Iterator;
import java.util.regex.Pattern;

import com.fathzer.soft.javaluator.*;
import com.gt.service.Compiler;

public class LogicEvaluator extends AbstractEvaluator<Object> {
    /** The less than unary operator. */
    public static final Operator LESS_THAN = new Operator("<", 2, Operator.Associativity.LEFT, 9);
    /** The less than or equal to unary operator. */
    public static final Operator LESS_EQUAL = new Operator("<=", 2, Operator.Associativity.LEFT, 8);
    /** The greater than unary operator. */
    public static final Operator GREATER_THAN = new Operator(">", 2, Operator.Associativity.LEFT, 7);
    /** The greater than or equal to unary operator. */
    public static final Operator GREATER_EQUAL = new Operator(">=", 2, Operator.Associativity.LEFT, 6);
    /** The equal to unary operator. */
    public static final Operator EQUAL_TO = new Operator("==", 2, Operator.Associativity.LEFT, 5);
    /** The not equal to unary operator. */
    public static final Operator NOT_EQUAL = new Operator("!=", 2, Operator.Associativity.LEFT, 4);
    /** The negate unary operator. */
    public final static Operator NEGATE = new Operator("!", 1, Operator.Associativity.RIGHT, 3);
    /** The logical AND operator. */
    private static final Operator AND = new Operator("&&", 2, Operator.Associativity.LEFT, 2);
    /** The logical OR operator. */
    public final static Operator OR = new Operator("||", 2, Operator.Associativity.LEFT, 1);

    private static final Parameters PARAMETERS;

    static {
        // Create the evaluator's parameters
        PARAMETERS = new Parameters();
        // Add the supported operators
        PARAMETERS.add(LESS_THAN);
        PARAMETERS.add(LESS_EQUAL);
        PARAMETERS.add(GREATER_THAN);
        PARAMETERS.add(GREATER_EQUAL);
        PARAMETERS.add(EQUAL_TO);
        PARAMETERS.add(NOT_EQUAL);
        PARAMETERS.add(AND);
        PARAMETERS.add(OR);
        PARAMETERS.add(NEGATE);
		PARAMETERS.addFunctionBracket(BracketPair.PARENTHESES);
		PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);
    }

    public LogicEvaluator() {
        super(PARAMETERS);
    }

    @Override
    protected Object toValue(String literal, Object evaluationContext) {
        Pattern booleanPattern = Pattern.compile("true|false");
        Pattern numberPattern = Pattern.compile("[0-9.]+");
        if(Compiler.inputValidation(booleanPattern, literal)){
            return Boolean.parseBoolean(literal);
        } else if (Compiler.inputValidation(numberPattern, literal)){
            return Double.parseDouble(literal);
        } else {
            try {
                throw new Exception("incompatible type");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }   

        return 0;
    }

    @Override
    protected Boolean evaluate(Operator operator, Iterator<Object> operands, Object evaluationContext) {
        if (operator == LESS_THAN){
            double o1 = (Double)operands.next();
            double o2 = (Double)operands.next();
            return o1 < o2;
        } else if (operator == LESS_EQUAL){
            double o1 = (Double)operands.next();
            double o2 = (Double)operands.next();
            return o1 <= o2;
        } else if (operator == GREATER_THAN){
            double o1 = (Double)operands.next();
            double o2 = (Double)operands.next();
            return o1 > o2;
        } else if (operator == GREATER_EQUAL){
            double o1 = (Double)operands.next();
            double o2 = (Double)operands.next();
            return o1 >= o2;
        } else if (operator == EQUAL_TO){
            double o1 = (Double)operands.next();
            double o2 = (Double)operands.next();
            return o1 == o2;
        } else if (operator == NOT_EQUAL){
            double o1 = (Double)operands.next();
            double o2 = (Double)operands.next();
            return o1 != o2;
        } else if (operator == NEGATE) {
            return !(Boolean)operands.next();
        } else if (operator == OR) {
            Boolean o1 = (Boolean)operands.next();
            Boolean o2 = (Boolean)operands.next();
            return o1 || o2;
        } else if (operator == AND) {
            Boolean o1 = (Boolean)operands.next();
            Boolean o2 = (Boolean)operands.next();
            return o1 && o2;
        } else {
            return (Boolean) super.evaluate(operator, operands, evaluationContext);
        }
    }
}