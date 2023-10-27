/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.server.display;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.form.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.FormReference;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author vetronauta
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormDisplay {

    public static void display(Form form) {
        display(form, System.out);
    }

    public static void display(Form form, PrintStream out) {
        display(form, out, new LinkedList<>(), 0);
    }

    //TODO common logic
    private static void display(Form form, PrintStream out, LinkedList<Form> recursionCheckStack, int indent) {
        if (form instanceof ColimitForm) {
            displayColimit(form, out, recursionCheckStack, indent);
            return;
        }
        if (form instanceof FormReference) {
            displayReference(form, out, indent);
            return;
        }
        if (form instanceof LimitForm) {
            displayLimit(form, out, recursionCheckStack, indent);
            return;
        }
        if (form instanceof ListForm) {
            displayList((ListForm) form, out, recursionCheckStack, indent);
            return;
        }
        if (form instanceof PowerForm) {
            displayPower(form, out, recursionCheckStack, indent);
        }
        if (form instanceof SimpleForm) {
            displaySimple((SimpleForm) form, out, indent);
        }
    }

    private static void displayColimit(Form form, PrintStream out, LinkedList<Form> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \""+ form.getNameString() + "\"");
        out.println("; Type: colimit");

        indent += 4;

        if (recursionCheck(form, recursionCheckStack)) {
            indent(out, indent);
            out.println("...");
        }
        else {
            recursionCheckStack.addFirst(form);
            FormDiagram d = (FormDiagram) form.getIdentifier().getCodomainDiagram();
            for (int i = 0; i < d.getFormCount(); i++) {
                display(d.getForm(i), out, recursionCheckStack, indent);
            }
            recursionCheckStack.removeFirst();
        }
    }

    private static void displayReference(Form form, PrintStream out, int indent) {
        indent(out, indent);
        out.print("Name: \"" + form.getNameString() + "\"");
        out.println("; Type: " + form.getTypeString());
        indent(out, indent+4);
        out.println("Reference");
    }

    private static void displayLimit(Form form, PrintStream out, LinkedList<Form> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \"" + form.getNameString() + "\"");
        out.println("; Type: limit");

        indent += 4;

        if (recursionCheck(form, recursionCheckStack)) {
            indent(out, indent);
            out.println("...");
        } else {
            recursionCheckStack.addFirst(form);
            FormDiagram d = (FormDiagram) form.getIdentifier().getCodomainDiagram();
            for (int i = 0; i < d.getFormCount(); i++) {
                display(d.getForm(i), out, recursionCheckStack, indent);
            }
            recursionCheckStack.removeFirst();
        }
    }

    private static void displayList(ListForm form, PrintStream out, LinkedList<Form> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \"" + form.getNameString() + "\"");
        out.println("; Type: list");

        indent += 4;

        if (recursionCheck(form, recursionCheckStack)) {
            indent(out, indent);
            out.println("...");
        }
        else {
            recursionCheckStack.addFirst(form);
            display(form.getForm(), out, recursionCheckStack, indent);
            recursionCheckStack.removeFirst();
        }
    }

    private static void displayPower(Form form, PrintStream out, LinkedList<Form> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \"" + form.getNameString() + "\"");
        out.println("; Type: power");

        indent += 4;

        if (recursionCheck(form, recursionCheckStack)) {
            indent(out, indent);
            out.println("...");
        } else {
            recursionCheckStack.addFirst(form);
            display(form.getForm(0), out, recursionCheckStack, indent);
            recursionCheckStack.removeFirst();
        }
    }

    private static void displaySimple(SimpleForm form, PrintStream out, int indent) {
        indent(out, indent);
        out.print("Name: \""+ form.getNameString()+"\"");
        out.println("; Type: simple");
        indent(out, indent+4);
        out.println("Module: " + form.getModule());
    }

    private static void indent(PrintStream out, int n) {
        for (int i = 0; i < n; i++) {
            out.print(" ");
        }
    }

    private static boolean recursionCheck(Form form, List<Form> recursionCheckStack) {
        return recursionCheckStack.stream().anyMatch(f -> f == form);
    }

}
