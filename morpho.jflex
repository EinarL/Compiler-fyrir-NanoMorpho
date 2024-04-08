import java.io.*;

%%

%public
%class NanoMorphoLexer
%unicode
%byaccj
%line
%column

%{

public String getLVal() { return yytext(); }
public void yyerror( String error )
{
	System.err.println("Error:  "+error);
	System.err.println("Lexeme: "+yytext());
	System.err.println("Line:   "+(yyline+1));
	System.err.println("Column: "+(yycolumn+1));
	System.exit(1);
}
public int getLine() { return yyline + 1; }
public int getColumn() { return yycolumn + 1; }



%}

%{

static public final int IF = 1;
static public final int NAME = 2;
static public final int LITERAL = 4;
static public final int VAR = 5;
static public final int WHILE = 6;
static public final int ELSIF = 7;
static public final int ELSE = 8;
static public final int RETURN = 9;
static public final int OPERATOR = 10;
static public final int FUNC = 11;
static public final int OP1 = 12;
static public final int OP2 = 13;
static public final int OP3 = 14;
static public final int OP4 = 15;
static public final int OP5 = 16;
static public final int OP6 = 17;
static public final int OP7 = 18;


%}
  /* Regular definitions */

_DIGIT=[0-9]
_FLOAT={_DIGIT}+\.{_DIGIT}+([eE][+-]?{_DIGIT}+)?
_INT={_DIGIT}+
_STRING=\"([^\"\\]|\\b|\\t|\\n|\\f|\\r|\\\"|\\\'|\\\\|(\\[0-3][0-7][0-7])|\\[0-7][0-7]|\\[0-7])*\"
_CHAR=\'([^\'\\]|\\b|\\t|\\n|\\f|\\r|\\\"|\\\'|\\\\|(\\[0-3][0-7][0-7])|(\\[0-7][0-7])|(\\[0-7]))\'
_DELIM=[(){},;=]
_NAME=(_|[:jletter:])(_|[:jletter:]|{_DIGIT})*
_OPNAME=[\+\-*/!%&=><\:\^\~&|?]+

%%

  /* Scanner rules */

{_DELIM} {
	return yycharat(0);
}

{_STRING} | {_FLOAT} | {_CHAR} | {_INT} | null | true | false {
	return LITERAL;
}

{_OPNAME} {
    switch( yycharat(0) )
    {
    case '?':
    case '~':
    case '^':
        return OP1;
    case ':':
        return OP2;
    case '|':
        return OP3;
    case '&':
        return OP4;
    case '<':
    case '>':
    case '=':
    case '!':
        return OP5;
    case '+':
    case '-':
        return OP6;
    case '*':
    case '/':
    case '%':
        return OP7;
    }
    throw new Error("This can't happen");
}

"if" {
	return IF;
}

"elsif" {
	return ELSIF;
}

"else" {
	return ELSE;
}

"while" {
	return WHILE;
}

"var" {
	return VAR;
}

"return" {
	return RETURN;
}

{_NAME} {
	return new Yyyoken(NAME, yytext());
}

";;;".*$ {
}

[ \t\r\n\f] {
}

. {
    return -1;
}
