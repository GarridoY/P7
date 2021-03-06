package dk.aau.cs.d703e20.ast;

import dk.aau.cs.d703e20.errorhandling.InvalidArrayException;
import dk.aau.cs.d703e20.errorhandling.CompilerException;
import dk.aau.cs.d703e20.ast.expressions.*;
import dk.aau.cs.d703e20.ast.statements.*;
import dk.aau.cs.d703e20.ast.structure.*;
import dk.aau.cs.d703e20.parser.OurParser;
import dk.aau.cs.d703e20.parser.OurParserBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ASTBuilder extends OurParserBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(OurParser.ProgramContext ctx) {
        SetupNode setupNode = (SetupNode) visitSetup(ctx.setup());
        LoopNode loopNode = (LoopNode) visitLoop(ctx.loop());

        List<FunctionDeclarationNode> functionDeclarationNodes =
                visitList(FunctionDeclarationNode.class, ctx.functionDecl(), this::visitFunctionDecl);

        ProgramNode programNode = new ProgramNode(setupNode, loopNode, functionDeclarationNodes);
        setCodePos(programNode, ctx);
        return programNode;
    }

    @Override
    public ASTNode visitSetup(OurParser.SetupContext ctx) {
        BlockNode blockNode = (BlockNode) visitBlock(ctx.block());

        SetupNode setupNode = new SetupNode(blockNode);
        setCodePos(setupNode, ctx);
        return setupNode;
    }

    @Override
    public ASTNode visitLoop(OurParser.LoopContext ctx) {
        BlockNode blockNode = (BlockNode) visitBlock(ctx.block());

        LoopNode loopNode = new LoopNode(blockNode);
        setCodePos(loopNode, ctx);
        return loopNode;
    }

    @Override
    public ASTNode visitBlock(OurParser.BlockContext ctx) {
        List<StatementNode> statementNodes = visitList(StatementNode.class, ctx.statement(), this::visitStatement);

        BlockNode blockNode = new BlockNode(statementNodes);
        setCodePos(blockNode, ctx);
        return blockNode;
    }

    @Override
    public ASTNode visitFunctionDecl(OurParser.FunctionDeclContext ctx) {
        BlockNode blockNode = (BlockNode) visitBlock(ctx.block());

        List<FunctionParameterNode> functionParameterNodes =
                visitList(FunctionParameterNode.class, ctx.functionParam(), this::visitFunctionParam);

        FunctionDeclarationNode functionDeclarationNode =
                new FunctionDeclarationNode(
                        getDataType(ctx.dataType()),
                        ctx.functionName().getText(),
                        blockNode,
                        functionParameterNodes);

        setCodePos(functionDeclarationNode, ctx);
        return functionDeclarationNode;
    }

    @Override
    public ASTNode visitFunctionParam(OurParser.FunctionParamContext ctx) {
        FunctionParameterNode functionParameterNode =
                new FunctionParameterNode(getDataType(ctx.dataType()), ctx.variableName().getText());

        setCodePos(functionParameterNode, ctx);
        return functionParameterNode;
    }

    @Override
    public ASTNode visitStatement(OurParser.StatementContext ctx) {
        if (ctx.variableDecl() != null)
            return visitVariableDecl(ctx.variableDecl());
        else if (ctx.assignment() != null)
            return visitAssignment(ctx.assignment());
        else if (ctx.pinDecl() != null)
            return visitPinDecl(ctx.pinDecl());
        else if (ctx.functionCall() != null)
            return visitFunctionCall(ctx.functionCall());
        else if (ctx.ifElseStatement() != null)
            return visitIfElseStatement(ctx.ifElseStatement());
        else if (ctx.iterativeStatement() != null)
            return visitIterativeStatement(ctx.iterativeStatement());
        else if (ctx.atStatement() != null)
            return visitAtStatement(ctx.atStatement());
        else if (ctx.boundStatement() != null)
            return visitBoundStatement(ctx.boundStatement());
        else if (ctx.returnStatement() != null)
            return visitReturnStatement(ctx.returnStatement());
        else
            throw new CompilerException("Invalid statement", getCodePosition(ctx));
    }

    @Override
    public ASTNode visitFunctionCall(OurParser.FunctionCallContext ctx) {
        List<FunctionArgNode> functionArgNodes =
                visitList(FunctionArgNode.class, ctx.functionArg(), this::visitFunctionArg);

        FunctionCallNode functionCallNode = new FunctionCallNode(ctx.functionName().getText(), functionArgNodes);

        setCodePos(functionCallNode, ctx);
        return functionCallNode;
    }

    @Override
    public ASTNode visitFunctionArg(OurParser.FunctionArgContext ctx) {
        FunctionArgNode functionArgNode;

        if (ctx.arithExpr() != null)
            functionArgNode = new FunctionArgNode((ArithExpressionNode) visitArithExpr(ctx.arithExpr()));
        else if (ctx.boolExpr() != null)
            functionArgNode = new FunctionArgNode((BoolExpressionNode) visitBoolExpr(ctx.boolExpr()));
        else
            throw new CompilerException("Invalid Function Argument", getCodePosition(ctx));

        setCodePos(functionArgNode, ctx);
        return functionArgNode;
    }

    @Override
    public ASTNode visitIfElseStatement(OurParser.IfElseStatementContext ctx) {
        IfStatementNode ifStatementNode = (IfStatementNode) visitIfStatement(ctx.ifStatement());

        // Create empty list then if ctx size > 0, fill it using visitList
        List<ElseIfStatementNode> elseIfStatementNode = new ArrayList<ElseIfStatementNode>();

        // ElseIf nodes
        elseIfStatementNode = visitList(ElseIfStatementNode.class, ctx.elseIfStatement(), this::visitElseIfStatement);

        // Else node
        ElseStatementNode elseStatementNode = null;
        if (ctx.elseStatement() != null) {
            elseStatementNode = (ElseStatementNode) visitElseStatement(ctx.elseStatement());
        }

        IfElseStatementNode ifElseStatementNode =
                new IfElseStatementNode(ifStatementNode, elseIfStatementNode, elseStatementNode);

        setCodePos(ifStatementNode, ctx);
        return ifElseStatementNode;
    }

    @Override
    public ASTNode visitIfStatement(OurParser.IfStatementContext ctx) {
        ConditionalExpressionNode conditionalExpressionNode =
                (ConditionalExpressionNode) visitConditionalExpression(ctx.conditionalExpression());

        BlockNode blockNode = (BlockNode) visitBlock(ctx.block());

        IfStatementNode ifStatementNode = new IfStatementNode(conditionalExpressionNode, blockNode);
        setCodePos(ifStatementNode, ctx);
        return ifStatementNode;
    }

    @Override
    public ASTNode visitElseIfStatement(OurParser.ElseIfStatementContext ctx) {
        ConditionalExpressionNode conditionalExpressionNode =
                (ConditionalExpressionNode) visitConditionalExpression(ctx.conditionalExpression());

        BlockNode blockNode = (BlockNode) visitBlock(ctx.block());

        ElseIfStatementNode elseIfStatementNode = new ElseIfStatementNode(conditionalExpressionNode, blockNode);
        setCodePos(elseIfStatementNode, ctx);
        return elseIfStatementNode;
    }

    @Override
    public ASTNode visitElseStatement(OurParser.ElseStatementContext ctx) {
        BlockNode blockNode = (BlockNode) visitBlock(ctx.block());

        ElseStatementNode elseStatementNode = new ElseStatementNode(blockNode);
        setCodePos(elseStatementNode, ctx);
        return elseStatementNode;
    }

    @Override
    public ASTNode visitConditionalExpression(OurParser.ConditionalExpressionContext ctx) {
        ConditionalExpressionNode conditionalExpressionNode;

        if (ctx.boolExpr() != null) {
            BoolExpressionNode boolExpressionNode = (BoolExpressionNode) visitBoolExpr(ctx.boolExpr());
            conditionalExpressionNode = new ConditionalExpressionNode(boolExpressionNode);
        }
        else if (ctx.variableName() != null) {
            conditionalExpressionNode =
                    new ConditionalExpressionNode(ctx.variableName().getText(), ctx.NOT() != null);
        }
        else if (ctx.functionCall() != null) {
            FunctionCallNode functionCallNode = (FunctionCallNode) visitFunctionCall(ctx.functionCall());
            conditionalExpressionNode = new ConditionalExpressionNode(functionCallNode);
        }
        else if (ctx.SUBSCRIPT() != null) {
            SubscriptNode subscriptNode = new SubscriptNode(ctx.SUBSCRIPT().getText());
            conditionalExpressionNode = new ConditionalExpressionNode(subscriptNode);
        }
        else
            throw new CompilerException("Invalid conditional expression", getCodePosition(ctx));

        setCodePos(conditionalExpressionNode, ctx);
        return conditionalExpressionNode;
    }

    @Override
    public ASTNode visitIterativeStatement(OurParser.IterativeStatementContext ctx) {
        if (ctx.forStatement() != null)
            return visitForStatement(ctx.forStatement());
        else if (ctx.whileStatement() != null)
            return visitWhileStatement(ctx.whileStatement());
        else
            throw new CompilerException("Invalid Iterative Statement", getCodePosition(ctx));
    }

    @Override
    public ASTNode visitForStatement(OurParser.ForStatementContext ctx) {
        ArithExpressionNode arithExpressionNode1 = (ArithExpressionNode) visitArithExpr(ctx.arithExpr(0));
        ArithExpressionNode arithExpressionNode2 = (ArithExpressionNode) visitArithExpr(ctx.arithExpr(1));
        BlockNode blockNode = (BlockNode) visitBlock(ctx.block());

        ForStatementNode forStatementNode = new ForStatementNode(arithExpressionNode1, arithExpressionNode2, blockNode);
        setCodePos(forStatementNode, ctx);

        return forStatementNode;
    }

    @Override
    public ASTNode visitArithExpr(OurParser.ArithExprContext ctx) {
        ArithExpressionNode arithExpressionNode = null;

        if (ctx.arithExpr().size() > 0) {
            if (ctx.arithExpr().size() > 1) {
                // arithExpr arithOp arithExpr
                ArithExpressionNode node1 = (ArithExpressionNode) visitArithExpr(ctx.arithExpr(0));
                ArithExpressionNode node2 = (ArithExpressionNode) visitArithExpr(ctx.arithExpr(1));
                arithExpressionNode = new ArithExpressionNode(node1, node2, getArithOperator(ctx.arithOp()));
            }
            else {
                // NOT? (arithExpr)
                ArithExpressionNode node = (ArithExpressionNode) visitArithExpr(ctx.arithExpr(0));
                arithExpressionNode = new ArithExpressionNode(node, ctx.NOT() != null);
            }
        }
        else if (ctx.functionCall() != null) {
            FunctionCallNode funcNode = (FunctionCallNode) visitFunctionCall(ctx.functionCall());
            arithExpressionNode = new ArithExpressionNode(funcNode);
        }
        else if (ctx.numLiteral() != null) {
            arithExpressionNode = new ArithExpressionNode(ctx.numLiteral().getText(), true);
        }
        else if (ctx.variableName() != null) {
            arithExpressionNode = new ArithExpressionNode(ctx.variableName().getText(), false);
        }
        else if (ctx.SUBSCRIPT() != null) {
            arithExpressionNode = new ArithExpressionNode(new SubscriptNode(ctx.SUBSCRIPT().getText()));
        }
        else {
            throw new CompilerException("Invalid Expression", getCodePosition(ctx));
        }

        setCodePos(arithExpressionNode, ctx);
        return arithExpressionNode;
    }

    @Override
    public ASTNode visitBoolExpr(OurParser.BoolExprContext ctx) {
        BoolExpressionNode boolExpressionNode;

        // boolExprOperand (boolOp boolExprOperand)+
        if (ctx.boolOp().size() > 0) {
            List<BoolExprOperandNode> boolExprOperandNodes =
                    visitList(BoolExprOperandNode.class, ctx.boolExprOperand(), this::visitBoolExprOperand);

            List<Enums.BoolOperator> boolOperators = new ArrayList<>();
            for (OurParser.BoolOpContext boolOpContext : ctx.boolOp())
                boolOperators.add(getBoolOperator(boolOpContext));

            boolExpressionNode = new BoolExpressionNode(boolExprOperandNodes, boolOperators);
            setCodePos(boolExpressionNode, ctx);
            return boolExpressionNode;
        }
        // BOOL_LITERAL
        else if (ctx.BOOL_LITERAL() != null) {
            boolExpressionNode = new BoolExpressionNode(ctx.BOOL_LITERAL().getText());
        }
        // NOT? LEFT_PAREN boolExpr RIGHT_PAREN;
        else if (ctx.boolExpr() != null) {
                BoolExpressionNode nestedBoolExpressionNode = (BoolExpressionNode) visitBoolExpr(ctx.boolExpr());
                boolExpressionNode = new BoolExpressionNode(ctx.NOT() != null, nestedBoolExpressionNode);
        }
        else
            throw new CompilerException("Invalid Bool Expression", getCodePosition(ctx));

        setCodePos(boolExpressionNode, ctx);
        return boolExpressionNode;
}

    @Override
    public ASTNode visitBoolExprOperand(OurParser.BoolExprOperandContext ctx) {
        BoolExprOperandNode boolExprOperandNode;

        if (ctx.boolExpr() != null)
            boolExprOperandNode = new BoolExprOperandNode((BoolExpressionNode) visitBoolExpr(ctx.boolExpr()));
        else if (ctx.arithExpr() != null)
            boolExprOperandNode = new BoolExprOperandNode((ArithExpressionNode) visitArithExpr(ctx.arithExpr()));
        else if (ctx.BOOL_LITERAL() != null)
            boolExprOperandNode = new BoolExprOperandNode(ctx.BOOL_LITERAL().getText());
        else
            throw new CompilerException("Invalid Operand", getCodePosition(ctx));

        setCodePos(boolExprOperandNode, ctx);
        return boolExprOperandNode;
    }

    @Override
    public ASTNode visitVariableDecl(OurParser.VariableDeclContext ctx) {
        VariableDeclarationNode variableDeclarationNode;

        // In case of array declaration, we keep track of the allocated size
        Integer allocatedArraySize = 0;
        switch (getDataType(ctx.dataType())) {
            case DOUBLE_ARRAY:
            case INT_ARRAY:
            case BOOL_ARRAY:
                // Get the allocated size from the [] in the dataType
                allocatedArraySize = getSizeFromArrayDataType(ctx.dataType());
                if (allocatedArraySize != null && allocatedArraySize <= 0) {
                    // if there is a size, and it's <= 0, throw an exception
                    // and if it is null, it won't be used later on
                    throw new InvalidArrayException(
                            ctx.variableName().getText(), allocatedArraySize, getCodePosition(ctx));
                }
                break;
        }

        if (ctx.assignment() != null) {
            variableDeclarationNode =
                    new VariableDeclarationNode(
                            getDataType(ctx.dataType()),
                            (AssignmentNode) visitAssignment(ctx.assignment()));
        }
        else if (ctx.assignArray() != null) {
            // if there is an allocated array size
            if (allocatedArraySize != null) {
                variableDeclarationNode =
                        new VariableDeclarationNode(
                                getDataType(ctx.dataType()),
                                allocatedArraySize,
                                (AssignArrayNode) visitAssignArray(ctx.assignArray()));
            }
            // if there is no allocated array size
            else {
                variableDeclarationNode =
                        new VariableDeclarationNode(
                                getDataType(ctx.dataType()),
                                (AssignArrayNode) visitAssignArray(ctx.assignArray()));
            }
        }
        else if (ctx.variableName() != null) {
            // if there is an allocated array size
            if (allocatedArraySize != null) {
                variableDeclarationNode =
                        new VariableDeclarationNode(
                                getDataType(ctx.dataType()),
                                allocatedArraySize,
                                ctx.variableName().getText());
            }
            // if there is no allocated array size
            else {
                variableDeclarationNode =
                        new VariableDeclarationNode(getDataType(ctx.dataType()), ctx.variableName().getText());
            }
        }
        else
            throw new CompilerException("Invalid Variable Declaration Statement", getCodePosition(ctx));

        setCodePos(variableDeclarationNode, ctx);
        return variableDeclarationNode;
    }

    @Override
    public ASTNode visitAssignArray(OurParser.AssignArrayContext ctx) {
        List<ArrayParamNode> arrayParamNodes = visitList(ArrayParamNode.class, ctx.arrayParam(), this::visitArrayParam);

        AssignArrayNode assignArrayNode = new AssignArrayNode(ctx.variableName().getText(), arrayParamNodes);
        setCodePos(assignArrayNode, ctx);
        return assignArrayNode;
    }

    @Override
    public ASTNode visitArrayParam(OurParser.ArrayParamContext ctx) {
        ArrayParamNode arrayParamNode;

        if (ctx.arithExpr() != null && !ctx.arithExpr().isEmpty()) {
            arrayParamNode = new ArrayParamNode((ArithExpressionNode) visitArithExpr(ctx.arithExpr()));
        } else if (!ctx.literal().isEmpty()) {
            arrayParamNode = new ArrayParamNode(ctx.literal().getText());
        }
        else {
            throw new InvalidArrayException(getCodePosition(ctx));
        }

        setCodePos(arrayParamNode, ctx);
        return arrayParamNode;
    }

    @Override
    public ASTNode visitAssignment(OurParser.AssignmentContext ctx) {
        AssignmentNode assignmentNode;

        if (ctx.literal() != null) {
            // Check the kind of literal
            if (ctx.literal().BOOL_LITERAL() != null)
                assignmentNode = new AssignmentNode(ctx.variableName().getText(), ctx.literal().getText());
            else
                // if String_Literal then remove "" from string value
                assignmentNode = new AssignmentNode(ctx.variableName().getText(), getStringLiteral(ctx.literal()));
        }
        else if (ctx.arithExpr() != null) {
            ArithExpressionNode arithExpressionNode = (ArithExpressionNode) visitArithExpr(ctx.arithExpr());
            assignmentNode = new AssignmentNode(ctx.variableName().getText(), arithExpressionNode);
        }
        else
            throw new CompilerException("Invalid Assignment Statement", getCodePosition(ctx));

        setCodePos(assignmentNode, ctx);
        return assignmentNode;
    }

    @Override
    public ASTNode visitReturnStatement(OurParser.ReturnStatementContext ctx) {
        ReturnStatementNode returnStatementNode = new ReturnStatementNode(ctx.variableName().getText());
        setCodePos(returnStatementNode, ctx);
        return returnStatementNode;
    }

    @Override
    public ASTNode visitAtStatement(OurParser.AtStatementContext ctx) {
        AtParamsNode atParamsNode = (AtParamsNode) visitAtParams(ctx.atParams());
        BlockNode blockNode = (BlockNode) visitBlock(ctx.block());

        AtStatementNode atStatementNode = new AtStatementNode(atParamsNode, blockNode);
        setCodePos(atStatementNode, ctx);
        return atStatementNode;
    }

    @Override
    public ASTNode visitAtParams(OurParser.AtParamsContext ctx) {
        AtParamsNode atParamsNode = new AtParamsNode((BoolExpressionNode) visitBoolExpr(ctx.boolExpr()));
        setCodePos(atParamsNode, ctx);
        return atParamsNode;
    }

    @Override
    public ASTNode visitBoundStatement(OurParser.BoundStatementContext ctx) {
        BoundStatementNode boundStatementNode;
        // Non optional
        AtParamsNode atParamsNode = (AtParamsNode) visitAtParams(ctx.atParams());
        // ctx block structure follow: 0=body, 1=catch, 2=final, if they all exist
        BlockNode body = (BlockNode) visitBlock(ctx.block(0));

        // Optional literal
        if (ctx.BOOL_LITERAL() != null) {
            // arg literal block blockNode
            if (ctx.block().size() == 2) {
                // blockNode is finalBlock
                if (ctx.FINAL() != null) {
                    boundStatementNode =
                            new BoundStatementNode(
                                    atParamsNode,
                                    ctx.BOOL_LITERAL().getText(),
                                    body,
                                    (BlockNode) visitBlock(ctx.block(1)),
                                    false);
                }
                // blockNode is Catch
                else {
                    boundStatementNode =
                            new BoundStatementNode(
                                    atParamsNode,
                                    ctx.BOOL_LITERAL().getText(),
                                    body,
                                    (BlockNode) visitBlock(ctx.block(1)),
                                    true);
                }
            }
            // arg literal block catch final
            else if (ctx.block().size() == 3) {
                boundStatementNode =
                        new BoundStatementNode(
                                atParamsNode,
                                ctx.BOOL_LITERAL().getText(),
                                body,
                                (BlockNode) visitBlock(ctx.block(1)),
                                (BlockNode) visitBlock(ctx.block(2)));
            }
            // arg literal block
            else boundStatementNode = new BoundStatementNode(atParamsNode, ctx.BOOL_LITERAL().getText(), body);
        }
        // without optional literal
        else {
            // arg block blockNode
            if (ctx.block().size() == 2) {
                // blockNode is finalBlock
                if (ctx.FINAL() != null) {
                    boundStatementNode =
                            new BoundStatementNode(
                                    atParamsNode,
                                    body,
                                    (BlockNode) visitBlock(ctx.block(1)),
                                    false);
                }
                // blockNode is Catch
                else {
                    boundStatementNode =
                            new BoundStatementNode(
                                    atParamsNode,
                                    body,
                                    (BlockNode) visitBlock(ctx.block(1)),
                                    true);
                }
            }
            // arg block catch final
            else if (ctx.block().size() == 3) {
                boundStatementNode =
                        new BoundStatementNode(
                                atParamsNode,
                                body,
                                (BlockNode) visitBlock(ctx.block(1)),
                                (BlockNode) visitBlock(ctx.block(2)));
            }
            // arg block
            else boundStatementNode = new BoundStatementNode(atParamsNode, body);
        }

        setCodePos(boundStatementNode, ctx);
        return boundStatementNode;
    }

    @Override
    public ASTNode visitPinDecl(OurParser.PinDeclContext ctx) {
        PinDeclarationNode pinDeclarationNode;
        Enums.PinType pinType = getPinType(ctx.pinType());

        if (ctx.DIGIT() != null) {
            pinDeclarationNode =
                    new PinDeclarationNode(pinType, ctx.variableName().getText(), ctx.DIGIT().getText());
        }
        else if (ctx.ANALOGPIN() != null) {
            pinDeclarationNode =
                    new PinDeclarationNode(pinType, ctx.variableName().getText(), ctx.ANALOGPIN().getText());
        }
        else if (ctx.LED_BUILTIN() != null) {
            pinDeclarationNode =
                    new PinDeclarationNode(pinType, ctx.variableName().getText(), ctx.LED_BUILTIN().getText());
        }
        else
            throw new CompilerException("Invalid pin declaration", getCodePosition(ctx));

        setCodePos(pinDeclarationNode, ctx);
        return pinDeclarationNode;
    }

    @Override
    public ASTNode visitWhileStatement(OurParser.WhileStatementContext ctx) {
        BoolExpressionNode boolExpressionNode = (BoolExpressionNode) visitBoolExpr(ctx.boolExpr());
        BlockNode blockNode = (BlockNode) visitBlock(ctx.block());

        WhileStatementNode whileStatementNode = new WhileStatementNode(boolExpressionNode, blockNode);
        setCodePos(whileStatementNode, ctx);
        return whileStatementNode;
    }

    private Enums.PinType getPinType(OurParser.PinTypeContext ctx) {
        Enums.PinType pinType;

        if (ctx.IPIN() != null)
            pinType = Enums.PinType.IPIN;
        else if (ctx.OPIN() != null)
            pinType = Enums.PinType.OPIN;
        else if (ctx.IPPIN() != null)
            pinType = Enums.PinType.IPPIN;
        else
            throw new CompilerException("Invalid pin type", getCodePosition(ctx));

        return pinType;
    }

    private Enums.DataType getDataType(OurParser.DataTypeContext ctx) {
        Enums.DataType dataType;

        if (ctx.INT() != null)
            dataType = Enums.DataType.INT;
        else if (ctx.BOOLEAN() != null)
            dataType = Enums.DataType.BOOL;
        else if (ctx.CLOCK() != null)
            dataType = Enums.DataType.CLOCK;
        else if (ctx.STRING() != null)
            dataType = Enums.DataType.STRING;
        else if (ctx.DOUBLE() != null)
            dataType = Enums.DataType.DOUBLE;
        else if (ctx.DOUBLE_ARRAY() != null)
            dataType = Enums.DataType.DOUBLE_ARRAY;
        else if (ctx.BOOLEAN_ARRAY() != null)
            dataType = Enums.DataType.BOOL_ARRAY;
        else if (ctx.INT_ARRAY() != null)
            dataType = Enums.DataType.INT_ARRAY;
        else if (ctx.VOID() != null)
            dataType = Enums.DataType.VOID;
        else
            throw new CompilerException("DataType is unknown", getCodePosition(ctx));

        return dataType;
    }

    private Enums.ArithOperator getArithOperator(OurParser.ArithOpContext ctx) {
        Enums.ArithOperator operator;

        if (ctx.ADD() != null)
            operator = Enums.ArithOperator.ADD;
        else if (ctx.SUB() != null)
            operator = Enums.ArithOperator.SUB;
        else if (ctx.MOD() != null)
            operator = Enums.ArithOperator.MOD;
        else if (ctx.DIV() != null)
            operator = Enums.ArithOperator.DIV;
        else if (ctx.MUL() != null)
            operator = Enums.ArithOperator.MUL;
        else
            throw new CompilerException("Operator is unknown", getCodePosition(ctx));

        return operator;
    }

    private Enums.BoolOperator getBoolOperator(OurParser.BoolOpContext ctx) {
        Enums.BoolOperator operator;

        if (ctx.EQUAL() != null)
            operator = Enums.BoolOperator.EQUAL;
        else if (ctx.NOT_EQUAL() != null)
            operator = Enums.BoolOperator.NOT_EQUAL;
        else if (ctx.GREATER_THAN() != null)
            operator = Enums.BoolOperator.GREATER_THAN;
        else if (ctx.GREATER_OR_EQUAL() != null)
            operator = Enums.BoolOperator.GREATER_OR_EQUAL;
        else if (ctx.LESS_THAN() != null)
            operator = Enums.BoolOperator.LESS_THAN;
        else if (ctx.LESS_OR_EQUAL() != null)
            operator = Enums.BoolOperator.LESS_OR_EQUAL;
        else if (ctx.AND() != null)
            operator = Enums.BoolOperator.AND;
        else if (ctx.OR() != null)
            operator = Enums.BoolOperator.OR;
        else
            throw new CompilerException("Operator is unknown", getCodePosition(ctx));

        return operator;
    }

    private Integer getSizeFromArrayDataType(OurParser.DataTypeContext ctx) {
        Pattern pattern = Pattern.compile(".+?\\[(.+?)\\]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ctx.getText());
        if (matcher.matches())
            return Integer.parseInt(matcher.group(1));
        else
            return null;
    }

    private String getStringLiteral(OurParser.LiteralContext ctx) {
        if (ctx.STRING_LITERAL() != null) {
            // Remove "" from both ends of the string
            return ctx.STRING_LITERAL().getText().subSequence(1, ctx.STRING_LITERAL().getText().length()-1).toString();
        } else
            throw new CompilerException("Bool is not String");
    }

    // Creates a list of T (ASTNodes), then visits all contexts in S using func
    // All the results from visiting are added to the list which gets returned.
    // Usage example:
    // List<StatementNode> statementNodes = visitList(StatementNode.class, ctx.statement(), this::visitStatement);
    private <T, S> List<T> visitList(Class<T> tClass, List<S> ctxs, Function<S, ASTNode> func) {
        List<T> nodes = new ArrayList<>();
        if (ctxs != null) {
            for (S ctx : ctxs) {
                nodes.add(tClass.cast(func.apply(ctx)));
            }
        }
        return nodes;
    }

    private List<String> visitLiterals(List<OurParser.LiteralContext> ctxs) {
        List<String> nodes = new ArrayList<>();
        for (OurParser.LiteralContext ctx : ctxs) {
            nodes.add(ctx.getText());
        }
        return nodes;
    }

    // Set codePosition of a Node using ctx
    private void setCodePos(ASTNode node, ParserRuleContext ctx) {
        node.setCodePosition(getCodePosition(ctx));
    }

    private CodePosition getCodePosition(ParserRuleContext ctx) {
        return new CodePosition(ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }
}
