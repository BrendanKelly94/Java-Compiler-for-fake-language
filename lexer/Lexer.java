/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.ArrayList;

/**
 *
 * @author brend_000
 */
public class Lexer {

    private boolean atEOF = false;
    private char ch;     // next character to process
    private SourceReader source;
    private ArrayList<String> s = new ArrayList<String>();
    // positions in line of current token
    private int startPosition, endPosition;

    public Lexer(String sourceFile) throws Exception {
        new TokenType();  // init token table
        source = new SourceReader(sourceFile);
        try {
            ch = source.read();
        } catch (Exception e) {
            System.out.println("Error: Could not find first character of file");
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        Token tok;
        for (String arg : args) {
            try {
                Lexer lex = new Lexer(System.getProperty("user.dir") + arg);
                int prevLineNo = 0;
                while (!lex.atEOF) {
                    tok = lex.nextToken();

                    if (prevLineNo != lex.source.getLineno()) {
                        lex.s.add(lex.source.getLineno() + "." + lex.source.getLine());
                    }
                    String p = "L: " + tok.getLeftPosition()
                            + " R: " + tok.getRightPosition() + "  "
                            + TokenType.tokens.get(tok.getKind()) + " ";
                    if (TokenType.tokens.containsKey(tok.getKind())) {
                        p += tok.toString();
                    }
                    prevLineNo = lex.source.getLineno();
                    System.out.println(p + ": " + lex.source.getLineno());
                }

            } catch (Exception e) {
                System.out.print(e.getMessage());
                System.exit(1);
            }
        }

    }

    /**
     * newIdTokens are either ids or reserved words; new id's will be inserted
     * in the symbol table with an indication that they are id's
     *
     * @param id is the String just scanned - it's either an id or reserved word
     * @param startPosition is the column in the source file where the token
     * begins
     * @param endPosition is the column in the source file where the token ends
     * @return the Token; either an id or one for the reserved words
     */
    public Token newIdToken(String id, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(id, Tokens.Identifier));
    }

    /**
     * number tokens are inserted in the symbol table; we don't convert the
     * numeric strings to numbers until we load the bytecodes for interpreting;
     * this ensures that any machine numeric dependencies are deferred until we
     * actually run the program; i.e. the numeric constraints of the hardware
     * used to compile the source program are not used
     *
     * @param number is the int String just scanned
     * @param startPosition is the column in the source file where the int
     * begins
     * @param endPosition is the column in the source file where the int ends
     * @return the int Token
     */
    public Token newNumberToken(String number, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition,
                Symbol.symbol(number, Tokens.INTeger));
    }

    /**
     *
     * @param number
     * @param startPosition
     * @param endPosition
     * @return
     */
    public Token newFloatToken(String number, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(number, Tokens.FLOAT));
    }

    public Token newSciToken(String number, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(number, Tokens.SCIentificN));
    }

    public Token newCharToken(String s, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(s, Tokens.CHAR));
    }

    public Token newStringToken(String str, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(str, Tokens.STRING));
    }

    /**
     * build the token for operators (+ -) or separators (parens, braces) filter
     * out comments which begin with two slashes
     *
     * @param s is the String representing the token
     * @param startPosition is the column in the source file where the token
     * begins
     * @param endPosition is the column in the source file where the token ends
     * @return the Token just found
     */
    public Token makeToken(String s, int startPosition, int endPosition) {
        if (s.equals("//")) {  // filter comment
            try {
                int oldLine = source.getLineno();
                do {
                    ch = source.read();
                } while (oldLine == source.getLineno());
            } catch (Exception e) {
                atEOF = true;
            }
            return nextToken();
        }
        Symbol sym = Symbol.symbol(s, Tokens.BogusToken); // be sure it's a valid token
        if (sym == null) {
            System.out.println("******** illegal character: " + s);
            atEOF = true;
            return nextToken();
        }
        return new Token(startPosition, endPosition, sym);
    }

    /**
     * @return the next Token found in the source file
     */
    public Token nextToken() { // ch is always the next char to process
        if (atEOF) {
            if (source != null) {
                for (int i = 0; i < s.size(); i++) {
                    System.out.println(s.get(i));
                }
                source.close();
                source = null;
            }
            return null;
        }
        try {
            while (Character.isWhitespace(ch)) {  // scan past whitespace
                ch = source.read();
            }
        } catch (Exception e) {
            atEOF = true;
            return nextToken();
        }
        startPosition = source.getPosition();
        endPosition = startPosition - 1;

        if (Character.isJavaIdentifierStart(ch)) {
            // return tokens for ids and reserved words
            String id = "";
            try {
                do {
                    endPosition++;
                    id += ch;
                    ch = source.read();

                } while (Character.isJavaIdentifierPart(ch));
            } catch (Exception e) {
                atEOF = true;
            }
            return newIdToken(id, startPosition, endPosition);
        }
        //floats starting with a decimal
        if (ch == '.') {
            String number = "";
            boolean hasDec = false;
            int decCounter = 0;
            try {
                do {
                    endPosition++;
                    if (ch == '.') {
                        decCounter++;
                        hasDec = true;
                    }
                    number += ch;
                    ch = source.read();
                    if (hasDec == true && ch == '.') {
                        decCounter++;
                    }

                } while ((Character.isDigit(ch) || ch == '.') && decCounter <= 1);
            } catch (Exception e) {
                atEOF = true;
            }
            return newFloatToken(number, startPosition, endPosition);
        }

        if (ch == '\'') {
            String c = "";
            try {
                do {
                    endPosition++;
                    c += ch;
                    ch = source.read();
                } while (!(ch == '\''));
                c += ch;
                ch = source.read();
                return newCharToken(c, startPosition, endPosition);

            } catch (Exception e) {
                atEOF = true;

            }
        }

        if (ch == '"') {
            String str = "";
            try {
                do {
                    endPosition++;
                    str += ch;
                    ch = source.read();
                } while (!(ch == '"'));
                str += ch;
                ch = source.read();
                return newStringToken(str, startPosition, endPosition);

            } catch (Exception e) {
                atEOF = true;

            }
        }

        //ints, floats, and scientific notation
        if (Character.isDigit(ch)) {
            // return number tokens
            String number = "";
            int decCounter = 0;
            int numCounter = 0;
            boolean isStandard = false;
            boolean hasDec = false;
            try {
                do {
                    //increase dec counter and checks whether it can be standard scientificN
                    if (ch == '.') {
                        decCounter++;
                        hasDec = true;
                        if (numCounter == 1) {
                            isStandard = true;
                        }
                    } else {
                        numCounter++;
                    }

                    endPosition++;
                    number += ch;
                    ch = source.read();

                    if (hasDec == true && ch == '.') {
                        decCounter++;
                    }

                } while ((Character.isDigit(ch) || ch == '.') && decCounter <= 1);

                if ((ch == 'E' || ch == 'e') && ((source.peek() == '+' || source.peek() == '-') || Character.isDigit(source.peek())) && isStandard == true) {
                    number += ch;
                    ch = source.read();
                    number += ch;
                    ch = source.read();
                    while (Character.isDigit(ch)) {
                        number += ch;
                        ch = source.read();
                    }
                    return newSciToken(number, startPosition, endPosition);
                }
            } catch (Exception e) {
                atEOF = true;
            }
            if (decCounter > 0) {
                return newFloatToken(number, startPosition, endPosition);
            } else {
                return newNumberToken(number, startPosition, endPosition);
            }
        }

        // At this point the only tokens to check for are one or two
        // characters; we must also check for comments that begin with
        // 2 slashes
        String charOld = "" + ch;
        String op = charOld;
        Symbol sym;
        try {
            endPosition++;
            ch = source.read();
            op += ch;
            // check if valid 2 char operator; if it's not in the symbol
            // table then don't insert it since we really have a one char
            // token
            sym = Symbol.symbol(op, Tokens.BogusToken);
            if (sym == null) {  // it must be a one char token
                return makeToken(charOld, startPosition, endPosition);
            }
            endPosition++;
            ch = source.read();
            return makeToken(op, startPosition, endPosition);
        } catch (Exception e) {
        }
        atEOF = true;
        if (startPosition == endPosition) {
            op = charOld;
        }
        return makeToken(op, startPosition, endPosition);
    }
}
