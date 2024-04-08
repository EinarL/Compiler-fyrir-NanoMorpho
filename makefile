all: Compiler.class NanoMorphoLexer.class NanoMorphoParser.class
Compiler.class NanoMorphoLexer.class NanoMorphoParser.class: Compiler.java NanoMorphoLexer.java NanoMorphoParser.java
	javac.exe Compiler.java NanoMorphoLexer.java NanoMorphoParser.java
NanoMorphoLexer.java: morpho.jflex
	java.exe -jar jflex-full-1.9.1.jar morpho.jflex
NanoMorphoParser.java: morpho.y
	bison -o NanoMorphoParser.java morpho.y
test: test.nm all
	java.exe Compiler test.nm > test.masm
	java.exe -jar morpho.jar -c --encoding utf8 test.masm
	java.exe -jar morpho.jar test
clean:
	rm -f *.class *~ NanoMorphoLexer.java NanoMorphoParser.java *.masm *.mexe