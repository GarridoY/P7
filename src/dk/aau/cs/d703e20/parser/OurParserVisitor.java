// Generated from C:/Users/htmle/Documents/GitHub/P7/src\OurParser.g4 by ANTLR 4.8
package dk.aau.cs.d703e20.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link OurParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface OurParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link OurParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(OurParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#setup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetup(OurParser.SetupContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoop(OurParser.LoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(OurParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(OurParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#functionParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParam(OurParser.FunctionParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(OurParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#functionArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArg(OurParser.FunctionArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(OurParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(OurParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#ifElseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseStatement(OurParser.IfElseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(OurParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#elseIfStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStatement(OurParser.ElseIfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#elseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStatement(OurParser.ElseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#conditionalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalExpression(OurParser.ConditionalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#atStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtStatement(OurParser.AtStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#atParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtParams(OurParser.AtParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#boundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundStatement(OurParser.BoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#iterativeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterativeStatement(OurParser.IterativeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(OurParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(OurParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#arithExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithExpr(OurParser.ArithExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#boolExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpr(OurParser.BoolExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#boolExprOperand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExprOperand(OurParser.BoolExprOperandContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#pinDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPinDecl(OurParser.PinDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#variableDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDecl(OurParser.VariableDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#assignArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignArray(OurParser.AssignArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#arrayParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayParam(OurParser.ArrayParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(OurParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#variableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableName(OurParser.VariableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#functionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionName(OurParser.FunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#pinType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPinType(OurParser.PinTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(OurParser.DataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(OurParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#numLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumLiteral(OurParser.NumLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#arithOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithOp(OurParser.ArithOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link OurParser#boolOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolOp(OurParser.BoolOpContext ctx);
}