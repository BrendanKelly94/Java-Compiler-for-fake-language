package compiler;

import ast.*;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import parser.Parser;
//import constrain.Constrainer;
//import codegen.*;
import visitor.*;

/**
 * The Compiler class contains the main program for compiling a source program
 * to bytecodes
 */
public class Compiler {

    /**
     * The Compiler class reads and compiles a source program
     */
    String sourceFile;

    public Compiler(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    void compileProgram() {
        try {
//            System.out.println("---------------TOKENS-------------");
            Parser parser = new Parser(sourceFile);
            AST t = parser.execute();
            System.out.println("---------------AST-------------");
            PrintVisitor pv = new PrintVisitor();
            t.accept(pv);

            // TREE DRAWING PART BEGINS HERE
            LocateVisitor cv = new LocateVisitor(10);
            t.accept(cv);
            DrawVisitor dv = new DrawVisitor(cv.getCount());
            t.accept(dv);
            try {
                File imagefile = new File(sourceFile + ".png");
                ImageIO.write(dv.getImage(), "png", imagefile);
            } catch (Exception e) {
                System.out.println("Error in saving image: " + e.getMessage());
            }

            final JFrame f = new JFrame();
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    f.dispose();
                    System.exit(0);
                }
            });

            int iH = dv.getImage().getHeight();
            int iW = dv.getImage().getWidth();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double sW = screenSize.getWidth();
            double sH = screenSize.getHeight();

            Image sBI;
            float scaleT, scaleH, scaleW;
            int nW, nH;

            if ((iH > sH) || (iW > sW)) {
                scaleH = (float) sH / (float) iH;
                scaleW = (float) sW / (float) iW;

                scaleT = scaleH > scaleW ? scaleW : scaleH;
                nW = (int) (iW * scaleT);
                nH = (int) (iH * scaleT);

                sBI = dv.getImage().getScaledInstance(nW, nH, Image.SCALE_SMOOTH);
            } else {
                sBI = dv.getImage();
                nW = dv.getImage().getWidth();
                nH = dv.getImage().getHeight();
            }

            JLabel imagelabel = new JLabel(new ImageIcon(sBI));
            f.add("Center", imagelabel);
            f.setMinimumSize(new Dimension(nW + 30, nH + 40));
            f.setResizable(false);
            f.pack();
            //f.setSize(new Dimension(dv.getImage().getWidth() + 30, dv.getImage().getHeight() + 40));
            f.setVisible(true);
            // f.setResizable(false);
            f.repaint();

            /*  COMMENT CODE FROM HERE UNTIL THE CATCH CLAUSE WHEN TESTING PARSER */
            /*           Constrainer con = new Constrainer(t,parser);
             con.execute();
             System.out.println("---------------DECORATED AST-------------");
             t.accept(pv);
             /*  COMMENT CODE FROM HERE UNTIL THE CATCH CLAUSE WHEN TESTING CONSTRAINER */
            /*           Codegen generator = new Codegen(t);
             Program program = generator.execute();
             System.out.println("---------------AST AFTER CODEGEN-------------");
             t.accept(pv);
             System.out.println("---------------INTRINSIC TREES-------------");
             System.out.println("---------------READ/WRITE TREES-------------");
             Constrainer.readTree.accept(pv);
             Constrainer.writeTree.accept(pv);
             System.out.println("---------------INT/BOOL TREES-------------");
             Constrainer.intTree.accept(pv);
             Constrainer.boolTree.accept(pv);
             program.printCodes(sourceFile + ".cod");
             // if the source file is "abc" print bytecodes to abc.cod
             */        } catch (Exception e) {
            System.out.println("********exception*******" + e.toString());
        };
    }

    public static void main(String args[]) {

        if (args.length == 0) {
            System.out.println("***Incorrect usage, try: java compiler.Compiler <file>");
            System.exit(1);
        }

        (new Compiler(args[0])).compileProgram();
    }
}
