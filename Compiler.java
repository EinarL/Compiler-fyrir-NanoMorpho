import java.io.*;

/**
 * Compiler fyrir NanoMorpho sem notar jflex og BYACCJ.
 * 
 * Þýðist með:
 *      javac Compiler.java
 * 
 * 
 * Til þess að þýða NanoMorpho kóða yfir í .masm skrá:
 *      java Compiler input.nm
 * 
 * Til að búa til .mexe skrá úr .masm skrá:
 *      java -cp ./morpho.jar -jar ./morpho.jar -c input.masm
 * 
 * Til að keyra .mexe skrá:
 *      java -cp ./morpho.jar -jar ./morpho.jar input
 * 
 * Hægt er að nota morpho.sh til að hjálpa að þýða og keyra .nm skrá:
 *      Til að búa til .mexe og .masm skrá:
 *          morpho.sh -c input.nm
 *      Til að keyra .mexe skrá:
 *          morpho.sh input
 */
public class Compiler
{
    private static PrintWriter writer; 

    static public void main( String[] args ) throws Exception
    {
        try
        {

            Parser par = new Parser(args.length > 1);
            Object[] intermediate = par.parse(args[0]);
            //par.printIntermediate(intermediate); // for debugging

            if (intermediate == null) return; // if intermediate is null, then there was an error

            // remove the .nm at the end of the filename
            if(args[0].endsWith(".nm")) args[0] = args[0].substring(0, args[0].length() - 3);

            writer = new PrintWriter(args[0]+".masm", "UTF-8"); 
            generateProgram(args[0], intermediate);
            writer.close();
        }
        catch( Throwable e )
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

// writes a line to the output file
private static void emit(String line) {
    writer.println(line);
}

private static void generateProgram(String name, Object[] funcs) {
    // funcs is a list of functions {function, function, ...}
    emit("\""+name+".mexe\" = main in");
    emit("!{{");
    for( int i=0; i!=funcs.length; i++ ) generateFunction((Object[])funcs[i]);
    emit("}}*BASIS;");
}

private static void generateFunction(Object[] f) {
    // f = {fname, argcount, varcount, {expr0, expr1, ...}}
    String fname = (String)f[0];
    int argcount = (Integer)f[1];
    int varcount = (Integer)f[2];
    emit("#\""+fname+"[f"+argcount+"]\" = ");
    emit("[");

    for(int i = argcount; i < argcount+varcount; i++){ // variable declarations
        emit("(MakeVal null)");
        emit("(Push)");
    }

    Object[] expressions = (Object[])f[3];
    for(Object expr : expressions){
        generateExpr((Object[])expr);
    }

    emit("(Return)");
    emit("];");
}


private static int nextLab = 1;

private static int newLab() {
    return nextLab++;
}

private static void generateExpr(Object[] e) {
    // e will be {expr} or {expr, op, expr, op, expr, ... , op, expr}
    generateSmallExpr((Object[])e[0]);
    
    if(e.length == 1) return;
    for(int i=1; i < e.length; i+=2) {
        // Put our last expr on the stack
        emit("(Push)");
        String op = e[i].toString();
        generateSmallExpr((Object[])e[i+1]);
        emit("(Call #\""+op+"[f"+2+"]\" "+2+")");
    }
}

private static void generateSmallExpr(Object[] e) {
    if (e == null) return;
    // e = {CodeType, ...}
    switch( e[0].toString() ) {
        case "name":
            // e = {"name", varPos}
            emit("(Fetch "+e[1]+")");
            return;
        case "literal":
            // e = {"literal", literal}
            emit("(MakeVal "+e[1]+")");
            return;
        case "assign":
            // e = {"assign", varPos, expr}
            generateExpr((Object[])e[2]);
            emit("(Store "+e[1]+")");
            return;
        case "call":
            // e = {"call", name, args}
            Object[] args = (Object[])e[2];
            int i;
            for(i=0; i < args.length; i++)
                if (i==0)
                    generateExpr((Object[])args[i]);
                else{
                    Object[] arg = (Object[])args[i];
                    generateExprP((Object[])arg[0]);
                }
                    
            emit("(Call #\""+e[1]+"[f"+i+"]\" "+i+")");
            return;
        case "return":
            // e = {"return", expr}
            generateExpr((Object[])e[1]);
            emit("(Return)");
            return;
        case "prior":
            // e = {"prior", priority}
            generateExpr((Object[])e[1]);
            return;
        case "if":
            // e = {"if", cond, then, else}
            int labElse = newLab();
            int labEnd = newLab();
            generateJump((Object[])e[1],0,labElse); // check condition
            generateBody((Object[])e[2]); // 'then' expr if condition was true
            boolean hasElse = e.length > 3; // if the if expression has an else expression
            
            if (hasElse) emit("(Go _"+labEnd+")");
            emit("_"+labElse+":");
            if(hasElse) {
                generateSmallExpr((Object[])e[3]); // 'else' expr if condition was false.
                emit("_"+labEnd+":");
            }
            return;
        case "else":
            generateBody((Object[])e[1]);
            return;
        case "while":
            // e = {"while", cond, body}
            int labStart = newLab();
            int labQuit = newLab();
            emit("_"+labStart+":"); // start of while loop
            generateJump((Object[])e[1], 0, labQuit);
            generateBody((Object[])e[2]);
            emit("(Go _"+labStart+")");
            emit("_"+labQuit+":");
            return;
    }
}

private static void generateBody(Object[] e) {
    // e = {expr, expr, ...}
    for(int i=0; i<e.length; i++) {
        generateExpr((Object[])e[i]);
    }
}


// Notkun: generateJump(e,labTrue,labTrue);
// Fyrir:  e er milliþula fyrir segð, labTrue og
//         labFalse eru heiltölur sem standa fyrir
//         merki eða eru núll.
// Eftir:  Búið er að skrifa lokaþulu fyrir segðina
//         á aðalúttak.  Lokaþulan veldur stökki til
//         merkisins labTrue ef segðina skilar sönnu,
//         annars stökki til labFalse.  Ef annað merkið
//         er núll þá er það jafngilt merki sem er rétt
//         fyrir aftan þulu segðarinnar.
private static void generateJump( Object[] e, int labTrue, int labFalse ) {
    generateExpr(e);
    if( labTrue!=0 ) emit("(GoTrue _"+labTrue+")");
    if( labFalse!=0 ) emit("(GoFalse _"+labFalse+")");
}

// Notkun: generateJumpP(e,labTrue,labFalse);
// Fyrir:  e er milliþula fyrir segð, labTrue og
//         labFalse eru heiltölur sem standa fyrir
//         merki eða eru núll.
// Eftir:  Þetta kall býr til lokaþulu sem er jafngild
//         þulunni sem köllin
//            emit("(Push)");
//            generateJump(e,labTrue,labFalse);
//         framleiða.  Þulan er samt ekki endilega sú
//         sama og þessi köll framleiða því tilgangurinn
//         er að geta framleitt betri þulu.
private static void generateJumpP( Object[] e, int labTrue, int labFalse ) {
    switch( (String)e[0] )
    {
        case "literal":
            String literal = (String)e[1];
            emit("(Push)");
            if( literal.equals("false") || literal.equals("null") )
            {
                if( labFalse!=0 ) emit("(Go _"+labFalse+")");
                return;
            }
            if( labTrue!=0 ) emit("(Go _"+labTrue+")");
            return;
        default:
            generateExprP(e);
            if( labTrue!=0 ) emit("(GoTrue _"+labTrue+")");
            if( labFalse!=0 ) emit("(GoFalse _"+labFalse+")");
    }
}

// Notkun: generateExprP(e);
// Fyrir:  e er milliþula fyrir segð.
// Eftir:  Þetta kall býr til lokaþulu sem er jafngild
//         þulunni sem köllin
//            emit("(Push)");
//            generateExpr(e);
//         framleiða.  Þulan er samt ekki endilega sú
//         sama og þessi köll framleiða því tilgangurinn
//         er að geta framleitt betri þulu.
private static void generateExprP( Object[] e ) {
    switch( (String)e[0] )
    {
        case "name":
            // e = {"name", name}
            emit("(FetchP "+e[1]+")");
            return;
        case "literal":
            // e = {"literal", literal}
            emit("(MakeValP "+(String)e[1]+")");
            return;
        case "if":
            // e = {"if", cond, then, else}
            int labElse = newLab();
            int labEnd = newLab();
            generateJumpP((Object[])e[1],0,labElse);
            generateExpr((Object[])e[2]);
            emit("(Go _"+labEnd+")");
            emit("_"+labElse+":");
            generateExpr((Object[])e[3]);
            emit("_"+labEnd+":");
            return;
        case "call":
            // e = {"call", name, args}
            Object[] args = (Object[])e[2];
            int i;
            for( i=0 ; i!=args.length ; i++ ) generateExprP((Object[])args[i]);
            if( i==0 ) emit("(Push)");
            emit("(Call #\""+e[1]+"[f"+i+"]\" "+i+")");
            return;
        default:
            return;
    }
}
}