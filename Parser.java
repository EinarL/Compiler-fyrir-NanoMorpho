//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 6 "morpho.y"
	import java.util.*;
	import java.io.*;
//#line 20 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short NAME=2;
public final static short LITERAL=4;
public final static short VAR=5;
public final static short RETURN=9;
public final static short OP1=10;
public final static short OP2=11;
public final static short OP3=12;
public final static short OP4=13;
public final static short OP5=14;
public final static short OP6=15;
public final static short OP7=16;
public final static short IF=1;
public final static short ELSIF=7;
public final static short ELSE=8;
public final static short WHILE=6;
public final static short OR=257;
public final static short AND=258;
public final static short NOT=259;
public final static short UNOP=260;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    2,    2,   18,    3,    1,    4,    4,    5,    5,
   15,   15,   16,   16,   13,   13,   14,   14,   14,   14,
   14,   11,   12,   12,   12,   17,    6,    6,    7,    7,
    8,    8,   10,   10,   10,   10,   10,    9,    9,    9,
    9,    9,    9,    9,
};
final static short yylen[] = {                            2,
    1,    2,    1,    0,   10,    0,    1,    0,    3,    1,
    5,    0,    3,    0,    2,    1,    5,    1,    1,    3,
    4,    8,    8,    4,    0,    7,    1,    0,    3,    1,
    3,    1,    1,    1,    3,    4,    2,    1,    1,    1,
    1,    1,    1,    1,
};
final static short yydefred[] = {                         4,
    0,    0,    3,    0,    2,    0,    0,   10,    6,    0,
    0,    0,    0,    9,   12,    0,    0,    0,    0,    0,
    0,   18,    0,   16,   19,    0,    0,   14,    0,   34,
    0,    0,    0,   32,    0,    0,    5,   15,    0,    0,
    0,    0,    0,    0,    0,    0,   38,   39,   40,   41,
   42,   43,   44,   20,    0,    0,    0,   21,    0,    0,
    0,   11,    0,   35,   31,    0,    0,   17,    0,   13,
   36,    0,    0,    0,    0,    0,   26,    0,    0,   22,
    0,    0,    0,    0,    0,   24,    0,    0,    0,   23,
};
final static short yydgoto[] = {                          1,
   11,    2,    3,    9,   10,   40,   41,   42,   55,   34,
   22,   80,   23,   24,   16,   43,   25,    4,
};
final static short yysindex[] = {                         0,
    0,    0,    0,    5,    0,   -1,   41,    0,    0,    7,
   20,   62,  -60,    0,    0,  117,  -17,   65,   -2,   28,
   29,    0,    2,    0,    0,   -2,   -2,    0,   46,    0,
   -2,   -2,   61,    0,   -2,   -2,    0,    0,   82,   66,
   43,  152,    6,   -2,  152,   68,    0,    0,    0,    0,
    0,    0,    0,    0,   -2,  101,  120,    0,   47,   -2,
  100,    0,   69,    0,    0,    1,   14,    0,  152,    0,
    0,   99,   99,    4,   13,   40,    0,   81,   16,    0,
   -2,   99,  142,   15,   21,    0,   99,   24,   40,    0,
};
final static short yyrindex[] = {                         0,
    0,   18,    0,    0,    0,    0,   84,    0,    0,   87,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  102,    0,   44,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  104,  -32,    0,  102,  -10,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   -4,    0,
    0,    0,    0,    0,    0,   26,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   26,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  144,    0,    0,  103,    0,   10,    0,   93,
    0,   70,   17,  -22,    0,    0,    0,    0,
};
final static int YYTABLESIZE=257;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         29,
   38,   30,   20,   17,   20,   17,    6,   21,   30,   21,
   19,   30,   19,   20,   17,   20,   17,    1,   21,    4,
   21,   19,   27,   19,   20,   17,   25,   25,   33,   21,
   37,   25,   19,   37,   25,   39,   29,   32,    7,   29,
   45,   46,    8,   26,   56,   57,   78,   79,   37,   61,
   12,   38,   38,   33,   33,   33,   33,   33,   33,   33,
   13,   38,   15,   14,   62,   38,   28,   35,   36,   69,
   47,   48,   49,   50,   51,   52,   53,   47,   48,   49,
   50,   51,   52,   53,   33,   44,   60,   33,   74,   75,
   83,   47,   48,   49,   50,   51,   52,   53,   84,   20,
   17,   70,   33,   88,   21,   68,   59,   19,   64,   71,
   47,   48,   49,   50,   51,   52,   53,   20,   17,   54,
   81,   18,   21,   72,    8,   19,   37,    7,   76,   47,
   48,   49,   50,   51,   52,   53,   73,   77,   82,   86,
   58,   66,   28,   87,   27,    5,   63,   65,   89,    0,
   25,   47,   48,   49,   50,   51,   52,   53,   90,    0,
   67,   47,   48,   49,   50,   51,   52,   53,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   85,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   31,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          2,
   23,    4,    1,    2,    1,    2,    2,    6,   41,    6,
    9,   44,    9,    1,    2,    1,    2,    0,    6,    2,
    6,    9,   40,    9,    1,    2,    1,    2,   19,    6,
   41,    6,    9,   44,    9,   26,   41,   40,   40,   44,
   31,   32,    2,   61,   35,   36,    7,    8,   59,   44,
   44,   74,   75,   10,   11,   12,   13,   14,   15,   16,
   41,   84,  123,    2,   59,   88,    2,   40,   40,   60,
   10,   11,   12,   13,   14,   15,   16,   10,   11,   12,
   13,   14,   15,   16,   41,   40,   44,   44,   72,   73,
   81,   10,   11,   12,   13,   14,   15,   16,   82,    1,
    2,    2,   59,   87,    6,   59,   41,    9,   41,   41,
   10,   11,   12,   13,   14,   15,   16,    1,    2,   59,
   40,    5,    6,  123,   41,    9,  125,   41,  125,   10,
   11,   12,   13,   14,   15,   16,  123,  125,  123,  125,
   59,   41,   41,  123,   41,    2,   44,   55,  125,   -1,
  125,   10,   11,   12,   13,   14,   15,   16,   89,   -1,
   41,   10,   11,   12,   13,   14,   15,   16,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   41,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  259,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=260;
final static String yyname[] = {
"end-of-file","IF","NAME",null,"LITERAL","VAR","WHILE","ELSIF","ELSE","RETURN",
"OP1","OP2","OP3","OP4","OP5","OP6","OP7",null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'('","')'",null,null,"','",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,"';'",null,"'='",null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,"OR","AND","NOT",
"UNOP",
};
final static String yyrule[] = {
"$accept : start",
"start : program",
"program : program function",
"program : function",
"$$1 :",
"function : $$1 NAME '(' optfuncparams varcount ')' '{' optdecls body '}'",
"varcount :",
"optfuncparams : funcparams",
"optfuncparams :",
"funcparams : funcparams ',' NAME",
"funcparams : NAME",
"optdecls : optdecls VAR NAME moredecls ';'",
"optdecls :",
"moredecls : moredecls ',' NAME",
"moredecls :",
"body : body exprs",
"body : exprs",
"exprs : NAME '(' optargs ')' ';'",
"exprs : ifexpr",
"exprs : whileexpr",
"exprs : RETURN expr ';'",
"exprs : NAME '=' expr ';'",
"ifexpr : IF '(' expr ')' '{' body '}' optelsif",
"optelsif : ELSIF '(' expr ')' '{' body '}' optelsif",
"optelsif : ELSE '{' body '}'",
"optelsif :",
"whileexpr : WHILE '(' expr ')' '{' body '}'",
"optargs : args",
"optargs :",
"args : args ',' expr",
"args : expr",
"expr : expr op factor",
"expr : factor",
"factor : NAME",
"factor : LITERAL",
"factor : '(' expr ')'",
"factor : NAME '(' optargs ')'",
"factor : NOT expr",
"op : OP1",
"op : OP2",
"op : OP3",
"op : OP4",
"op : OP5",
"op : OP6",
"op : OP7",
};

//#line 144 "morpho.y"


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
//#line 367 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 46 "morpho.y"
{ intermediate = ((Vector<Object[]>)val_peek(0).obj).toArray(); }
break;
case 2:
//#line 50 "morpho.y"
{ ((Vector<Object[]>)val_peek(1).obj).add((Object[])val_peek(0).obj); yyval.obj = val_peek(1).obj; }
break;
case 3:
//#line 51 "morpho.y"
{ yyval.obj = new Vector<Object[]>(); ((Vector<Object[]>)yyval.obj).add((Object[])val_peek(0).obj); }
break;
case 4:
//#line 55 "morpho.y"
{ variables = new HashMap<String, Integer>(); varCount = 0; }
break;
case 5:
//#line 57 "morpho.y"
{ yyval.obj = new Object[]{val_peek(8).obj.toString(), val_peek(5).ival, varCount-val_peek(5).ival, ((Vector<Object>)val_peek(1).obj).toArray() }; }
break;
case 6:
//#line 61 "morpho.y"
{ yyval.ival = varCount; }
break;
case 7:
//#line 64 "morpho.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 9:
//#line 69 "morpho.y"
{ addVar(val_peek(0).obj.toString()); }
break;
case 10:
//#line 70 "morpho.y"
{ addVar(val_peek(0).obj.toString()); }
break;
case 11:
//#line 74 "morpho.y"
{ addVar(val_peek(2).obj.toString()); }
break;
case 13:
//#line 79 "morpho.y"
{ addVar(val_peek(0).obj.toString()); }
break;
case 15:
//#line 84 "morpho.y"
{ ((Vector<Object>)val_peek(1).obj).add(new Object[]{(Object)val_peek(0).obj}); yyval.obj = val_peek(1).obj; }
break;
case 16:
//#line 85 "morpho.y"
{ yyval.obj = new Vector<Object>(); ((Vector<Object>)yyval.obj).add(new Object[]{(Object)val_peek(0).obj}); }
break;
case 17:
//#line 89 "morpho.y"
{ yyval.obj = new Object[]{"call", val_peek(4).obj.toString(), new Object[]{((Vector<Object>)val_peek(2).obj).toArray()}}; }
break;
case 18:
//#line 90 "morpho.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 19:
//#line 91 "morpho.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 20:
//#line 92 "morpho.y"
{ yyval.obj = new Object[]{"return", ((Vector<Object>)val_peek(1).obj).toArray()}; }
break;
case 21:
//#line 93 "morpho.y"
{ yyval.obj = new Object[]{"assign", varPos(val_peek(3).obj.toString()), ((Vector<Object>)val_peek(1).obj).toArray()}; }
break;
case 22:
//#line 97 "morpho.y"
{ yyval.obj = new Object[] { "if", ((Vector<Object>)val_peek(5).obj).toArray(), ((Vector<Object>)val_peek(2).obj).toArray(), val_peek(0).obj }; }
break;
case 23:
//#line 101 "morpho.y"
{ yyval.obj = new Object[] {"if", ((Vector<Object>)val_peek(5).obj).toArray(), ((Vector<Object>)val_peek(2).obj).toArray(), val_peek(0).obj }; }
break;
case 24:
//#line 102 "morpho.y"
{ yyval.obj = new Object[] {"else", ((Vector<Object>)val_peek(1).obj).toArray() }; }
break;
case 25:
//#line 103 "morpho.y"
{ yyval.obj = null; }
break;
case 26:
//#line 107 "morpho.y"
{ yyval.obj = new Object[]{"while", ((Vector<Object>)val_peek(4).obj).toArray(), ((Vector<Object>)val_peek(1).obj).toArray() }; }
break;
case 27:
//#line 111 "morpho.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 28:
//#line 112 "morpho.y"
{ yyval.obj = new Vector<Object>(); }
break;
case 29:
//#line 116 "morpho.y"
{ ((Vector<Object>)val_peek(2).obj).add(((Vector<Object>)val_peek(0).obj).toArray()); yyval.obj = val_peek(2).obj; }
break;
case 30:
//#line 117 "morpho.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 31:
//#line 121 "morpho.y"
{ ((Vector<Object>)val_peek(2).obj).add(val_peek(1).obj.toString()); ((Vector<Object>)val_peek(2).obj).add((Object)val_peek(0).obj); yyval.obj = val_peek(2).obj; }
break;
case 32:
//#line 122 "morpho.y"
{ yyval.obj = new Vector<Object>(); ((Vector<Object>)yyval.obj).add((Object)val_peek(0).obj); }
break;
case 33:
//#line 126 "morpho.y"
{ yyval.obj = new Object[]{"name", varPos(val_peek(0).obj.toString())}; }
break;
case 34:
//#line 127 "morpho.y"
{ yyval.obj = new Object[]{"literal", val_peek(0).obj}; }
break;
case 35:
//#line 128 "morpho.y"
{ yyval.obj = new Object[]{"prior", ((Vector<Object>)val_peek(1).obj).toArray()}; }
break;
case 36:
//#line 129 "morpho.y"
{ yyval.obj = new Object[]{"call", val_peek(3).obj.toString(), new Object[]{((Vector<Object>)val_peek(1).obj).toArray()}}; }
break;
case 37:
//#line 130 "morpho.y"
{ yyval.obj = new Object[]{"not", val_peek(0).obj }; }
break;
case 38:
//#line 134 "morpho.y"
{ yyval.obj = val_peek(0).obj.toString(); }
break;
case 39:
//#line 135 "morpho.y"
{ yyval.obj = val_peek(0).obj.toString(); }
break;
case 40:
//#line 136 "morpho.y"
{ yyval.obj = val_peek(0).obj.toString(); }
break;
case 41:
//#line 137 "morpho.y"
{ yyval.obj = val_peek(0).obj.toString(); }
break;
case 42:
//#line 138 "morpho.y"
{ yyval.obj = val_peek(0).obj.toString(); }
break;
case 43:
//#line 139 "morpho.y"
{ yyval.obj = val_peek(0).obj.toString(); }
break;
case 44:
//#line 140 "morpho.y"
{ yyval.obj = val_peek(0).obj.toString(); }
break;
//#line 680 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
