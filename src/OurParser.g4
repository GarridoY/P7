parser grammar OurParser;
options { tokenVocab=OurLexer; }

// Program rule, has to consist of a main rule, can be followed by function declarations.
// variableName is used to catch outside of the program
program
    : setup loop (functionDecl | variableName)*;

// main start keyword followed by a block
loop
    : LOOP block;

setup
    : SETUP block;

// Encapsulation of code by brackets
block
    : LEFT_BRACKET (statement)* RIGHT_BRACKET;

// FUNCTIONS
// Function declaration, optional argument followed by more optional arguments prefixed by comma
functionDecl
    : (VOID | dataType) functionName LEFT_PAREN functionParam? RIGHT_PAREN block;

// Function parameters
functionParam
    : dataType variableName ( COMMA dataType variableName)*;

// Call function given optional arguments (expr)
functionCall
    : functionName LEFT_PAREN functionArgs? RIGHT_PAREN;

// Function arguments for calling function(s)
functionArgs
    : (arithExpr | boolExpr) ( COMMA (arithExpr | boolExpr))*;

// Statements available in main
statement
    : variableDecl SEMICOLON
    | assignment SEMICOLON
    | pinDecl SEMICOLON
    | functionCall SEMICOLON
    | ifElseStatement //conditionalStatement
    | iterativeStatement
    | atStatement
    | boundStatement
    | returnStatement SEMICOLON;

returnStatement
    : RETURN variableName;

// CONDITIONAL
// any IF statement require blocks
ifElseStatement
    : ifStatement elseIfStatement* elseStatement?;

ifStatement: IF LEFT_PAREN conditionalExpression RIGHT_PAREN block;
elseIfStatement: ELSE_IF LEFT_PAREN conditionalExpression RIGHT_PAREN block;
elseStatement: ELSE block;

conditionalExpression: boolExpr | NOT? variableName | functionCall | SUBSCRIPT;

// at statement for clock and timing purposes
atStatement
    : AT LEFT_PAREN atParams RIGHT_PAREN block (FINAL block)?;

atParams
    : variableName boolOp arithExpr (COMMA variableName boolOp arithExpr (COMMA BOOL_LITERAL)?)?; // at (x, y, z) y z optional, only z if y

boundStatement
    : BOUND LEFT_PAREN variableName boolOp arithExpr (COMMA BOOL_LITERAL)? RIGHT_PAREN block (FINAL block)?; // bound (y, z) z optional

// ITERATIVE
iterativeStatement
    : forStatement
    | whileStatement;

// for (* to *) {}
forStatement
    : FOR LEFT_PAREN arithExpr TO arithExpr RIGHT_PAREN block;

whileStatement
    : WHILE LEFT_PAREN boolExpr RIGHT_PAREN block;

// EXPRESSIONS

arithExpr
    : arithExpr arithOp arithExpr // Precedence handled by target
    | NOT?'('arithExpr')'
    | numLiteral
    | variableName
    | functionCall
    | SUBSCRIPT;

// TODO: typecheck operator for expr (only pure bools can AND, OR)
boolExpr
    : BOOL_LITERAL
    | (arithExpr | BOOL_LITERAL) boolOp (BOOL_LITERAL | arithExpr)
    | NOT? LEFT_PAREN boolExpr RIGHT_PAREN;

pinDecl
    : pinType variableName (DIGIT | ANALOGPIN);

// Declaration of variable, all variables must be initialized
variableDecl
    : dataType variableName
    | dataType assignment
    | dataType assignArray;

assignArray
    : variableName ASSIGN LEFT_BRACKET (arithExpr | literal) (COMMA (arithExpr | literal))* RIGHT_BRACKET;

assignment
    : variableName ASSIGN (arithExpr | literal);

// Names
variableName
    : ID;
functionName
    : ID;

pinType
    : IPIN
    | OPIN;

dataType
    : INT
    | DOUBLE
    | BOOLEAN
    | CLOCK
    | STRING
    | INT_ARRAY
    | DOUBLE_ARRAY
    | BOOLEAN_ARRAY;

literal
    : STRING_LITERAL
    | BOOL_LITERAL;

numLiteral
    : DIGIT
    | DIGIT_NEGATIVE
    | DOUBLE_DIGIT
    | DOUBLE_DIGIT_NEGATIVE;

arithOp
    : ADD
    | SUB
    | MOD
    | DIV
    | MUL;

boolOp
    : EQUAL
    | NOT_EQUAL
    | GREATER_THAN
    | GREATER_OR_EQUAL
    | LESS_THAN
    | LESS_OR_EQUAL;