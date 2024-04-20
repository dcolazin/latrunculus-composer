package org.vetronauta.latrunculus.server.display;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.DenotatorReference;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.map.IndexMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.ListMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.ModuleMorphismMap;

import java.io.PrintStream;
import java.util.LinkedList;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DenotatorDisplay {

    public static void display(Denotator denotator) {
        display(denotator, System.out);
    }

    public static void display(Denotator denotator, PrintStream out) {
        display(denotator, out, new LinkedList<>(), 0);
    }

    //TODO common logic
    private static void display(Denotator denotator, PrintStream out, LinkedList<Denotator> recursionCheckStack, int indent) {
        if (denotator instanceof ColimitDenotator) {
            displayColimit(denotator, out, recursionCheckStack, indent);
            return;
        }
        if (denotator instanceof LimitDenotator) {
            displayLimit(denotator, out, recursionCheckStack, indent);
            return;
        }
        if (denotator instanceof ListDenotator) {
            displayList(denotator, out, recursionCheckStack, indent);
            return;
        }
        if (denotator instanceof PowerDenotator) {
            displayPower(denotator, out, recursionCheckStack, indent);
            return;
        }
        if (denotator instanceof SimpleDenotator) {
            displaySimple(denotator, out, recursionCheckStack, indent);
            return;
        }
        if (denotator instanceof DenotatorReference) {
            throw new Error("Fatal error: DenotatorReference.display should never be called");
        }
    }

    private static void displayColimit(Denotator denotator, PrintStream out, LinkedList<Denotator> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \"" + denotator.getNameString() + "\"");
        out.print("; Form: \"" + denotator.getForm().getNameString() + "\"");
        out.print("; Type: " + denotator.getForm().getType());
        out.println("; Address: " + denotator.getAddress());

        indent += 4;

        if (recursionCheck(denotator, recursionCheckStack)) {
            indent(out, indent);
            out.println("...");
        } else {
            IndexMorphismMap map = (IndexMorphismMap) denotator.getCoordinate().getMap();
            recursionCheckStack.addFirst(denotator);
            display(map.getFactor(), out, recursionCheckStack, indent);
            recursionCheckStack.removeFirst();
        }
    }

    private static void displayLimit(Denotator denotator, PrintStream out, LinkedList<Denotator> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \"" + denotator.getNameString() + "\"");
        out.print("; Form: \"" + denotator.getForm().getNameString() + "\"");
        out.print("; Type: " + denotator.getForm().getType());
        out.println("; Address: " + denotator.getAddress());

        indent += 4;

        if (recursionCheck(denotator, recursionCheckStack)) {
            indent(out, indent);
            out.println("...");
        } else {
            ListMorphismMap map = (ListMorphismMap) denotator.getCoordinate().getMap();
            recursionCheckStack.addFirst(denotator);
            for (int i = 0; i < map.getFactorCount(); i++) {
                display(map.getFactor(i), out, recursionCheckStack, indent);
            }
            recursionCheckStack.removeFirst();
        }
    }

    private static void displayList(Denotator denotator, PrintStream out, LinkedList<Denotator> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \"" + denotator.getNameString() + "\"");
        out.print("; Form: \"" + denotator.getForm().getNameString() + "\"");
        out.print("; Type: "+ denotator.getForm().getType());
        out.println("; Address: " + denotator.getAddress());

        if (recursionCheck(denotator, recursionCheckStack)) {
            indent(out, indent+4);
            out.println("...");
            return;
        }

        recursionCheckStack.addFirst(denotator);
        for (Denotator d : denotator) {
            display(d, out, recursionCheckStack, indent+4);
        }
        recursionCheckStack.removeFirst();
    }

    private static void displayPower(Denotator denotator, PrintStream out, LinkedList<Denotator> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \"" + denotator.getNameString() + "\"");
        out.print("; Form: \"" + denotator.getForm().getNameString() + "\"");
        out.print("; Type: " + denotator.getForm().getType());
        out.println("; Address: " + denotator.getAddress());

        indent += 4;

        if (recursionCheck(denotator, recursionCheckStack)) {
            indent(out, indent);
            out.println("...");
        } else {
            recursionCheckStack.addFirst(denotator);
            for (Denotator d : denotator) {
                display(d, out, recursionCheckStack, indent);
            }
            recursionCheckStack.removeFirst();
        }
    }

    private static void displaySimple(Denotator denotator, PrintStream out, LinkedList<Denotator> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \"" + denotator.getNameString() + "\"");
        out.print("; Form: \"" + denotator.getForm().getNameString() + "\"");
        out.print("; Type: " + denotator.getForm().getType());
        out.println("; Address: " + denotator.getAddress());

        indent += 4;

        if (recursionCheck(denotator, recursionCheckStack)) {
            indent(out, indent);
            out.println("...");
        } else {
            indent(out, indent);
            ModuleMorphismMap moduleMorphismMap = ((SimpleDenotator) denotator).getModuleMorphismMap();
            if (moduleMorphismMap.isConstant()) {
                out.println(moduleMorphismMap.getElement());
            }
            else {
                out.println(moduleMorphismMap.getMorphism());
            }
        }
    }

    /**
     * Print out a number of white spaces to a stream.
     *
     * @param out the stream to print to
     * @param n the number of spaces to print
     */
    private static void indent(PrintStream out, int n) {
        for (int i = 0; i < n; i++) {
            out.print(" ");
        }
    }

    /**
     * Returns true if this denotator is in the recursion stack.
     */
    private static boolean recursionCheck(Denotator toCheck, LinkedList<Denotator> recursionCheckStack) {
        for (Denotator d : recursionCheckStack) {
            if (d == toCheck) {
                return true;
            }
        }
        return false;
    }

}
