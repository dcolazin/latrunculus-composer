package org.vetronauta.latrunculus.server.display;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.repository.Repository;

import java.io.PrintStream;
import java.util.TreeSet;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepositoryDisplay {

    public static void display(Repository repository) {
        display(repository, System.out);
    }

    public static void display(Repository repository, PrintStream out) {
        int width = 70;
        out.print("┍"); repeat(out, "━", width-2); out.println("┑");
        out.print("│Forms"); repeat(out, " ", width-7); out.println("│");
        out.print("├"); repeat(out, "─", width-2); out.println("┤");
        TreeSet<Form> formSet = new TreeSet<>(repository.getForms());
        for (Form f : formSet) {
            String s = f.getName().getNameEntry()+": "+f;
            out.print("│"+s); repeat(out, " ", width-s.length()-2); out.println("│");
        }
        out.print("├"); repeat(out, "─", width-2); out.println("┤");
        out.print("│Denotators"); repeat(out, " ", width-12); out.println("│");
        out.print("├"); repeat(out, "─", width-2); out.println("┤");
        TreeSet<Denotator> denoSet = new TreeSet<>(repository.getDenotators());
        for (Denotator d : denoSet) {
            String s = d.getName().getNameEntry()+": "+d.getForm().getName().getNameEntry();
            out.print("│"+s); repeat(out, " ", width-s.length()-2); out.println("│");
        }
        out.print("├"); repeat(out, "─", width-2); out.println("┤");
        out.print("│Modules"); repeat(out, " ", width-9); out.println("│");
        out.print("├"); repeat(out, "─", width-2); out.println("┤");
        TreeSet<String> moduleSet = new TreeSet<>(repository.getModuleNames());
        for (String moduleName : moduleSet) {
            String s = moduleName+": "+repository.getModule(moduleName);
            out.print("│"+s); repeat(out, " ", width-s.length()-2); out.println("│");
        }
        out.print("├"); repeat(out, "─", width-2); out.println("┤");
        out.print("│Module Elements"); repeat(out, " ", width-17); out.println("│");
        out.print("├"); repeat(out, "─", width-2); out.println("┤");
        TreeSet<String> moduleElementSet = new TreeSet<>(repository.getModuleElementNames());
        for (String moduleElementName : moduleElementSet) {
            String s = moduleElementName+": "+repository.getModuleElement(moduleElementName);
            out.print("│"+s); repeat(out, " ", width-s.length()-2); out.println("│");
        }
        out.print("├"); repeat(out, "─", width-2); out.println("┤");
        out.print("│Module Morphisms"); repeat(out, " ", width-18); out.println("│");
        out.print("├"); repeat(out, "─", width-2); out.println("┤");
        TreeSet<String> moduleMorphismSet = new TreeSet<>(repository.getModuleMorphismNames());
        for (String moduleMorphismName : moduleMorphismSet) {
            String s = moduleMorphismName+": "+repository.getModuleMorphism(moduleMorphismName);
            out.print("│"+s); repeat(out, " ", width-s.length()-2); out.println("│");
        }
        out.print("┕");
        repeat(out, "━", width-2);
        out.print("┙");
        out.println();
    }

    /**
     * Print out the string <code>c</code> repeated <code>n</code> times.
     */
    private static void repeat(PrintStream out, String c, int n) {
        for (int i = 0; i < n; i++) {
            out.print(c);
        }
    }


}
