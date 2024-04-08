/*
	compile with: yacc -J morpho.y
				  javac Parser.java
*/
%{
	import java.util.*;
	import java.io.*;
%}


%token<obj> NAME 2
%token<obj> LITERAL 4
%token<obj> VAR 5
%token<obj> RETURN 9
%token<obj> OP1 10
%token<obj> OP2 11
%token<obj> OP3 12
%token<obj> OP4 13
%token<obj> OP5 14
%token<obj> OP6 15
%token<obj> OP7 16
%token<obj> IF 1
%token<obj> ELSIF 7
%token<obj> ELSE 8
%token<obj> WHILE 6

%right RETURN '='
%right OR
%right AND
%right NOT
%left OP1
%right OP2
%left OP3
%left OP4
%left OP5
%left OP6
%left OP7
%right UNOP

%type<ival> varcount
%type<obj> start program function optfuncparams funcparams optargs args expr op factor ifexpr optelsif body exprs optdecls moredecls whileexpr

%%

start
	:	program		{ intermediate = ((Vector<Object[]>)$1).toArray(); }
	;

program
	:	program function	{ ((Vector<Object[]>)$1).add((Object[])$2); $$ = $1; } 
	|	function			{ $$ = new Vector<Object[]>(); ((Vector<Object[]>)$$).add((Object[])$1); }
	;

function  // function will be = {fname, argcount, varcount, {expr0, expr1, ...}}
	:	{ variables = new HashMap<String, Integer>(); varCount = 0; } 
		NAME '(' optfuncparams varcount ')' '{' optdecls body '}' 	
		{ $$ = new Object[]{$2.toString(), $5, varCount-$5, ((Vector<Object>)$9).toArray() }; }
	;

varcount
	:	{ $$ = varCount; }
	;
optfuncparams
	:	funcparams	{ $$ = $1; }
	|		 		
	;

funcparams
	:	funcparams ',' NAME		{ addVar($3.toString()); }
	|	NAME		 			{ addVar($1.toString()); }
	;

optdecls
	:	optdecls VAR NAME moredecls ';'		{ addVar($3.toString()); }
	|
	;

moredecls
	:	moredecls ',' NAME		{ addVar($3.toString()); }
	|
	;

body
	:	body exprs	{ ((Vector<Object>)$1).add(new Object[]{(Object)$2}); $$ = $1; }
	| 	exprs		{ $$ = new Vector<Object>(); ((Vector<Object>)$$).add(new Object[]{(Object)$1}); }
	;

exprs
	:	NAME '(' optargs ')' ';'	{ $$ = new Object[]{"call", $1.toString(), new Object[]{((Vector<Object>)$3).toArray()}}; } 
	|	ifexpr						{ $$ = $1; }
	|	whileexpr					{ $$ = $1; }
	|	RETURN expr ';'				{ $$ = new Object[]{"return", ((Vector<Object>)$2).toArray()}; }
	|	NAME '=' expr ';'			{ $$ = new Object[]{"assign", varPos($1.toString()), ((Vector<Object>)$3).toArray()}; }
	;

ifexpr
	:	IF '(' expr ')' '{' body '}' optelsif		{ $$ = new Object[] { "if", ((Vector<Object>)$3).toArray(), ((Vector<Object>)$6).toArray(), $8 }; }
	;

optelsif
	:	ELSIF '(' expr ')' '{' body '}' optelsif	{ $$ = new Object[] {"if", ((Vector<Object>)$3).toArray(), ((Vector<Object>)$6).toArray(), $8 }; }
	|	ELSE '{' body '}'							{ $$ = new Object[] {"else", ((Vector<Object>)$3).toArray() }; }
	|												{ $$ = null; }
	;

whileexpr
	:	WHILE '(' expr ')' '{' body '}'			{ $$ = new Object[]{"while", ((Vector<Object>)$3).toArray(), ((Vector<Object>)$6).toArray() }; }
	;

optargs
	:	args	{ $$ = $1; }
	|		 	{ $$ = new Vector<Object>(); }
	;

args
	:	args ',' expr	{ ((Vector<Object>)$1).add(((Vector<Object>)$3).toArray()); $$ = $1; }
	|	expr		 	{ $$ = $1; } 
	;

expr
	:	expr op factor			{ ((Vector<Object>)$1).add($2.toString()); ((Vector<Object>)$1).add((Object)$3); $$ = $1; }
	| 	factor					{ $$ = new Vector<Object>(); ((Vector<Object>)$$).add((Object)$1); }
	;

factor
	:	NAME					{ $$ = new Object[]{"name", varPos($1.toString())}; }
	|	LITERAL					{ $$ = new Object[]{"literal", $1}; }
	|	'(' expr ')'			{ $$ = new Object[]{"prior", ((Vector<Object>)$2).toArray()}; }
	|	NAME '(' optargs ')'	{ $$ = new Object[]{"call", $1.toString(), new Object[]{((Vector<Object>)$3).toArray()}}; } 
	|	NOT expr				{ $$ = new Object[]{"not", $2 }; }
	;

op	
	:	OP1 	{ $$ = $1.toString(); }
	| 	OP2 	{ $$ = $1.toString(); }
	| 	OP3 	{ $$ = $1.toString(); }
	| 	OP4 	{ $$ = $1.toString(); }
	| 	OP5 	{ $$ = $1.toString(); }
	| 	OP6 	{ $$ = $1.toString(); }
	| 	OP7		{ $$ = $1.toString(); }
	;

%%


NanoMorphoLexer lexer;
int varCount;
Object[] intermediate;

// contains all the variables that are defined in the current function
HashMap<String, Integer> variables;

public Object[] getIntermeditate(){
	return intermediate;
}

// fall fyrir debugging, prentar intermediate listann
void printIntermediate(Object[] intermediate){
	int index = 0;
	for (Object obj: intermediate){
		if(obj instanceof Object[]){
			System.out.print("{ ");
			printIntermediate((Object[])obj);
			System.out.print(" }");
		}else{
			System.out.print(obj);
		}
		if(index < intermediate.length-1)System.out.print(", ");
		index++;
	}
}

int varPos(String variableName){
	if (variables.containsKey(variableName)) return variables.get(variableName);
	throw new Error("Error on line " + lexer.getLine() + ": Variable " + variableName + " has not been defined");
}

void addVar(String variableName){
	if (variables.containsKey(variableName)) throw new Error("Error on line " + lexer.getLine() + ": Variable " + variableName + " has already been declared");
	variables.put(variableName, varCount);
	varCount++;
}

void yyerror(String s)
{
 	System.out.println("error on line " + lexer.getLine() + ": " + s);
}

int yylex(){
	Yytoken next_token;
	try {
		next_token = lexer.yylex();
	} catch (IOException e) {
		throw new Error(e);
	}
	yylval = new ParserVal(next_token); // save the Yytoken, so that we can get the function name, etc.
	if(next_token == null) return 0;

	return next_token.number;
}


public Object[] parse(String fileName){
	try{
		lexer = new NanoMorphoLexer(new FileReader(fileName));
		yyparse();
		
	} catch (IOException e) {e.printStackTrace();}
	return intermediate;
}
