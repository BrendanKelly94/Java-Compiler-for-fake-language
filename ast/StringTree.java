/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author brend_000
 */
import lexer.Symbol;
import lexer.Token;
import visitor.*;

public class StringTree extends AST {

    private Symbol symbol;

    /**
     * @param tok is the Token containing the String representation of the
     * integer literal; we keep the String rather than converting to an integer
     * value so we don't introduce any machine dependencies with respect to
     * integer representations
     */
    public StringTree(Token tok) {
        this.symbol = tok.getSymbol();
    }

    public Object accept(ASTVisitor v) {
        return v.visitIntTree(this);
    }

    public Symbol getSymbol() {
        return symbol;
    }

}
