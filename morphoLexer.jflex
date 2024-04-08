/**
	JFlex lesgreinir fyrir NanoMorpho.

	Þennan lesgreini má þýða með skipununum
		jflex morphoLexer.jflex
		javac NanoMorphoLexer.java
	
	og keyrt lesgreininn með
		java NanoMorphoLexer input
	
 */
%%

%public
%class NanoMorphoLexer
%line
%column
%unicode

%{

public int getLine() { return yyline + 1; }
public int getColumn() { return yycolumn + 1; }

%}

%{

static public final int IF = 1;
static public final int NAME = 2;
static public final int LITERAL = 4;
static public final int VAR = 5;
static public final int WHILE = 6;
static public final int ELIF = 7;
static public final int ELSE = 8;
static public final int RETURN = 9;
static public final int OP1 = 10;
static public final int OP2 = 11;
static public final int OP3 = 12;
static public final int OP4 = 13;
static public final int OP5 = 14;
static public final int OP6 = 15;
static public final int OP7 = 16;

static public final int ERR = -1;
static public final int EOF = -2;
static private Yytoken next_token;
static private NanoMorphoLexer lexer;

static private void advance(){
	try{
		next_token = lexer.yylex();
	}
	catch (Exception e){
		throw new Error(e);
	}
	if( next_token == null ){
		next_token = new Yytoken(NanoMorphoLexer.EOF,"EOF");
	}
}

static public void main( String args[] ){
	try {
		lexer = new NanoMorphoLexer(new java.io.FileReader(args[0]));
		java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.FileWriter("lexemes"));
		
		advance();
		
		while( next_token.number != NanoMorphoLexer.EOF) {
			out.write(next_token.number+": "+next_token);
			out.newLine();
			advance();
		}
		out.close();
		
	} catch(Exception e) {
		e.printStackTrace();
	}
}

%}

_DIGIT=[0-9]
_FLOAT={_DIGIT}+\.{_DIGIT}+([eE][+-]?{_DIGIT}+)?
_INT={_DIGIT}+
_STRING=\"([^\"\\]|\\b|\\t|\\n|\\f|\\r|\\\"|\\\'|\\\\|(\\[0-3][0-7][0-7])|\\[0-7][0-7]|\\[0-7])*\"
_CHAR=\'([^\'\\]|\\b|\\t|\\n|\\f|\\r|\\\"|\\\'|\\\\|(\\[0-3][0-7][0-7])|(\\[0-7][0-7])|(\\[0-7]))\'
_DELIM=[()\{\},;=]
_OPNAME=[\+\-*/!%&=><\:\^\~&|?]+
_NAME=[a-zA-Z_$][a-zA-Z{_DIGIT}_$]*
%%

{_DELIM} {
	return new Yytoken(yycharat(0));
}

{_STRING} | {_FLOAT} | {_CHAR} | {_INT} | null | true | false {
	return new Yytoken(LITERAL, yytext());
}

{_OPNAME} {
    switch( yycharat(0) )
    {
    case '?':
    case '~':
    case '^':
        return new Yytoken(OP1, yytext());
    case ':':
        return new Yytoken(OP2, yytext());
    case '|':
        return new Yytoken(OP3, yytext());
    case '&':
        return new Yytoken(OP4, yytext());
    case '<':
    case '>':
    case '=':
    case '!':
        return new Yytoken(OP5, yytext());
    case '+':
    case '-':
        return new Yytoken(OP6, yytext());
    case '*':
    case '/':
    case '%':
        return new Yytoken(OP7, yytext());
    }
    throw new Error("This can't happen");
}

"if" {
	return new Yytoken(IF, yytext()); 
}

"elsif" {
	return new Yytoken(ELIF, yytext()); 
}

"else" {
	return new Yytoken(ELSE, yytext()); 
}

"while" {
	return new Yytoken(WHILE, yytext()); 
}

"var" {
	return new Yytoken(VAR, yytext());
}

"return" {
	return new Yytoken(RETURN, yytext()); 
}

{_NAME} {
	return new Yytoken(NAME,yytext());
}

";;;".*$ {
}

[ \t\r\n\f] {
}

. {
	return new Yytoken(ERR, "Error");
}