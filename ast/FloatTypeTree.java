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
import visitor.*;

public class FloatTypeTree extends AST {

    public FloatTypeTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitFloatTypeTree(this);
    }

}
