package dk.aau.cs.d703e20.ast.expressions;

import dk.aau.cs.d703e20.ast.ASTNode;
import dk.aau.cs.d703e20.ast.CodePosition;
import dk.aau.cs.d703e20.ast.Enums;
import dk.aau.cs.d703e20.ast.statements.FunctionCallNode;

public class ArithExpressionNode implements ASTNode {
    // arithExpr arithOp arithExpr
    private ArithExpressionNode arithExpressionNode1;
    private ArithExpressionNode arithExpressionNode2;
    private Enums.ArithOperator arithExpressionOperator;

    // functionCall
    private FunctionCallNode functionCallNode;
    // NOT? (arithExpr)
    private Enums.BoolOperator optionalNot;

    // numLiteral
    private Double number;

    private String variableName;

    private SubscriptNode subscriptNode;

    private CodePosition codePosition;

    // arithExpr arithOp arithExpr
    public ArithExpressionNode(ArithExpressionNode arithExpressionNode1, ArithExpressionNode arithExpressionNode2, Enums.ArithOperator arithExpressionOperator) {
        this.arithExpressionNode1 = arithExpressionNode1;
        this.arithExpressionNode2 = arithExpressionNode2;
        this.arithExpressionOperator = arithExpressionOperator;
    }

    // numLiteral
    public ArithExpressionNode(Double number) {
        this.number = number;
    }

    public ArithExpressionNode(ArithExpressionNode arithExpressionNode1) {
        this.arithExpressionNode1 = arithExpressionNode1;
    }

    public ArithExpressionNode(String variableName) {
        this.variableName = variableName;
    }

    // functionCall
    public ArithExpressionNode(FunctionCallNode functionCallNode) {
        this.functionCallNode = functionCallNode;
    }

    // NOT? (arithExpr)
    public ArithExpressionNode(ArithExpressionNode arithExpressionNode1, Enums.BoolOperator optionalNot) {
        this.arithExpressionNode1 = arithExpressionNode1;
        this.optionalNot = optionalNot;
    }

    public ArithExpressionNode(SubscriptNode subscriptNode) {
        this.subscriptNode = subscriptNode;
    }

    public ArithExpressionNode getArithExpressionNode1() {
        return arithExpressionNode1;
    }

    public ArithExpressionNode getArithExpressionNode2() {
        return arithExpressionNode2;
    }

    public Enums.ArithOperator getArithExpressionOperator() {
        return arithExpressionOperator;
    }

    public Double getNumber() {
        return number;
    }

    public String getVariableName() {
        return variableName;
    }

    public FunctionCallNode getFunctionCallNode() {
        return functionCallNode;
    }

    public Enums.BoolOperator getOptionalNot() {
        return optionalNot;
    }

    public SubscriptNode getSubscriptNode() {
        return subscriptNode;
    }

    @Override
    public String prettyPrint(int indentation) {
        return "ARITH EXPRESSION";
    }

    @Override
    public void setCodePosition(CodePosition codePosition) {
        this.codePosition = codePosition;
    }

    @Override
    public CodePosition getCodePosition() {
        return codePosition;
    }
}
