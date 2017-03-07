/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visitor;

import ast.AST;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lowell Milliken
 */
public class LocateVisitor extends ASTVisitor {

    private int[] nCount = new int[100];
    private int depth = 0;
    private int maxDepth = 0;
    boolean shift = false;
    int shiftAmt;

    public LocateVisitor(int inpDepth) {
        maxDepth = inpDepth;
        nCount = new int[maxDepth + 1];
        for (int i = 0; i < maxDepth; i++) {
            nCount[i] = 0;
        }
    }

    private void locate(AST t) {
        /* nCount[depth]++;

         if (depth > maxDepth) {
         maxDepth = depth;
         }

         depth++;
         visitKids(t);
         depth--;
         */

        int numKid = t.kidCount();
        if (shift == false) {
            int avgX = 0;
            if (numKid > 0) {

                depth++;
                visitKids(t);
                if (numKid > 1) {
                    for (int i = 1; i <= numKid; i++) {

                        if (i == 1 || i == numKid) {
                            avgX += t.getKid(i).getXCoor();
                        }
                    }
                    avgX /= 2;
                } else {
                    avgX = nCount[depth];
                }
                depth--;
                t.setXCoor(avgX);

                if (nCount[depth] >= avgX) {
                    shift = true;
                    shiftAmt = nCount[depth] - 2;
                    depth++;
                    visitKids(t);
                    depth--;
                    shift = false;
                    avgX += shiftAmt;
                    t.setXCoor(avgX);
                }

                nCount[depth] = avgX;
            } else {
                //  if (nCount[depth] > 0) {
                t.setXCoor(nCount[depth] + 2);
                nCount[depth] = t.getXCoor();
              //  } else {

                //  t.setXCoor(nCount[depth]++);
                //  nCount[depth] += t.getXCoor();
                // }
            }
        } else {
            if (t.kidCount() > 0) {
                depth++;
                visitKids(t);
                depth--;
            }
            t.setXCoor(t.getXCoor() + shiftAmt);
        }
    }

    public int[] getCount() {
        int[] count = new int[maxDepth + 1];

        for (int i = 0; i <= maxDepth; i++) {
            count[i] = nCount[i];
        }

        return count;
    }

    public void printCount() {
        for (int i = 0; i <= maxDepth; i++) {
            System.out.println("Depth: " + i + " Nodes: " + nCount[i]);
        }
    }

    public Object visitProgramTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitBlockTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitFunctionDeclTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitCallTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitDeclTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitIntTypeTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitBoolTypeTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitFormalsTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitActualArgsTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitIfTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitWhileTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitReturnTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitAssignTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitIntTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitIdTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitRelOpTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitAddOpTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitMultOpTree(AST t) {
        locate(t);
        return null;
    }

    //new methods here
    public Object visitFloatTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitFloatTypeTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitCharTypeTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitStringTypeTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitRepeatTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitCharTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitStringTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitDoWhileTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitForTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitPowerOpTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitNegOpTree(AST t) {
        locate(t);
        return null;
    }

    public Object visitNotOpTree(AST t) {
        locate(t);
        return null;
    }

}
