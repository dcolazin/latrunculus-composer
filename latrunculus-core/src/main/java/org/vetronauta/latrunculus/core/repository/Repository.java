/*
 * Copyright (C) 2002 Gérard Milmeister
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.vetronauta.latrunculus.core.repository;

import org.vetronauta.latrunculus.core.logeo.FormFactory;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.module.generic.PolynomialRing;
import org.vetronauta.latrunculus.core.math.module.generic.ProductRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.NameEntry;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.form.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.FormReference;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.NameForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.core.scheme.Evaluator;
import org.vetronauta.latrunculus.core.scheme.Parser;
import org.vetronauta.latrunculus.core.scheme.expression.Env;
import org.vetronauta.latrunculus.core.scheme.expression.SExpr;

import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.stream.Collectors;

import static org.vetronauta.latrunculus.core.logeo.DenoFactory.makeDenotator;

/**
 * A repository of forms and denotators, retrievable by their names.
 * There is a global repository, that should contain all forms
 * and named denotators used troughout the Latrunculus system.
 * 
 * @author Gérard Milmeister
 */
public class Repository extends Observable implements Dictionary { //TODO this stuff should be in server package?

    /**
     * Registers a form with repository.
     * Performs a rollback, if a different form already exists with the same name.
     * @return the form in the repository 
     */
    public synchronized Form register(Form form) {
        Form f = register(form, false);
        if (f == null) {
            rollback();
        } else {
            setChanged();
            notifyObservers();
            reset();
        }
        return f;
    }
    
    
    /**
     * Registers a form as builtin with repository.
     * @return the form in the repository 
     */
    public synchronized Form registerBuiltin(Form form) {
        return form._register(this, true);
    }
    
    
    /**
     * Returns true iff the given form is a builtin.
     */
    public boolean isBuiltin(Form form) {
        return builtinForms.containsKey(form);
    }
        

    /**
     * Registers a denotator with repository.
     * @param d register d only if it has a non-null name
     * @return the registered denotator
     */
    public synchronized Denotator register(Denotator d) {
        Denotator deno = register(d, false);
        reset();
        setChanged();
        notifyObservers();
        return deno;
    }
    
    
    /**
     * Registers a denotator as builtin with repository.
     * @return the denotator in the repository 
     */
    public synchronized Denotator registerBuiltin(Denotator denotator) {
        return register(denotator, true);
    }
    
    
    /**
     * Returns true iff the given denotator is a builtin.
     */
    public boolean isBuiltin(Denotator denotator) {
        return builtinDenotators.containsKey(denotator);
    }
        

    /**
     * Registers a denotator with repository.
     * Adds the registered denotator to the temporaries.
     * @return the registered denotator
     */
    private Denotator register(Denotator denotator, boolean builtin) {
        if (denotator.getName() != null) {
            NameEntry nameEntry = denotator.getName().getNameEntry();
            denotators.put(nameEntry, new DenotatorItem(denotator, builtin));
            tmpDenos.add(denotator);
            if (builtin) {
                builtinDenotators.put(denotator, denotator);
            }
        }
        return denotator;        
    }

    
    /**
     * Registers the objects in the LoadableDictionary.
     * @return true if successful
     */
    public synchronized boolean registerLoadableDictionary(LoadableDictionary dictionary) {
        dictionary.read();
        if (dictionary.hasError()) {
            return false;
        }
        return register(dictionary.getForms(), dictionary.getDenotators());
    }

    /**
     * Registers a collection of forms and a collection of denotators.
     * Performs a rollback, if the registration fails
     * @param formSet a collection of forms
     * @param denotatorSet a collection of denotators
     * @return true if the registration succeeds
     */
    public synchronized boolean register(Collection<Form> formSet, Collection<Denotator> denotatorSet) {
        for (Form f : formSet) {
            Form form = f._register(this, false);
            if (form == null) {
                // registration failed
                rollback();
                reset();
                return false;
            }                    
        }
        
        for (Denotator deno : denotatorSet) {
            Denotator d = register(deno, false);
            if (d == null) {
                // registration failed
                rollback();
                reset();
                return false;
            }
        }

        // registration successful
        reset();
        return true;
    }


    /**
     * Registers a form.
     * Does <code>not</code> perform a rollback, if the registration fails
     */
    public Form register(Form form, boolean builtin) {
        if (form instanceof FormReference) {
            // do not register a form reference
            return form;
        }
        NameEntry name = form.getName().getNameEntry();
        FormItem item = forms.get(name);
        if (item == null) {
            // form not yet in repository
            if (form.getType() != FormDenotatorTypeEnum.SIMPLE) {
                FormDiagram diagram = (FormDiagram)form.getIdentifier().getCodomainDiagram();
                if (!diagram.registerForms(this, builtin)) {
                    // registration failed
                    return null;
                }
            }
            forms.put(name, new FormItem(form, builtin));
            tmpForms.add(form);
            if (builtin) {
                builtinForms.put(form, form);
            }
            return form;
        }
        else if (item.getForm() == form) {
            // (pointer)-identical form already in repository
            return item.getForm();
        }
        else if (!item.getForm().fullEquals(form))  {
            // different form with same name in repository --> registration failed
            return null;
        }
        else {
            // same form already registered in repository 
            return item.getForm();
        }        
    }


    /**
     * Returns the form with the given name if it exists, otherwise null.
     */
    public synchronized Form getForm(NameDenotator name) {
        FormItem item = forms.get(name.getNameEntry());
        return (item == null)?null:item.getForm();
    }
    

    /**
     * Returns the form with the given name if it exists, otherwise null.
     */
    public synchronized Form getForm(String name) {
        NameEntry nameEntry = NameEntry.lookup(name);        
        FormItem item = forms.get(nameEntry);
        return (item == null)?null:item.getForm();
    }
    
    
    /**
     * Returns the form with the given name if it exists, otherwise null.
     */
    public synchronized Form getForm(NameEntry name) {
        NameEntry nameEntry = NameEntry.lookup(name);        
        FormItem item = forms.get(nameEntry);
        return (item == null)?null:item.getForm();
    }

    
    public synchronized List<Form> getForms() {
        LinkedList<Form> res = new LinkedList<>();
        for (FormItem item : forms.values()) {
            res.add(item.getForm());
        }
        return res;
    }

    public synchronized List<Form> getCustomForms() {
        return forms.values().stream()
            .filter(formItem -> !formItem.isBuiltin())
            .map(FormItem::getForm)
            .collect(Collectors.toList());
    }


    /**
     * Returns a power form with the given base form.
     * The new form is given the name "_Power(baseFormName)".
     */
    public PowerForm autogenPowerForm(Form baseForm) {
        PowerForm powerForm = autogenPowerForms.get(baseForm);
        if (powerForm == null) {
            String name = "_Power("+baseForm.getNameString()+")";
            powerForm = FormFactory.makePowerForm(name, baseForm);
            autogenPowerForms.put(baseForm, powerForm);
            register(powerForm);
        }
        return powerForm;
    }
    
    
    /**
     * Returns a list form with the given base form.
     * The new form is given the name "_List(baseFormName)".
     */
    public ListForm autogenListForm(Form baseForm) {
        ListForm listForm = autogenListForms.get(baseForm);
        if (listForm == null) {
            String name = "_List("+baseForm.getNameString()+")";
            listForm = FormFactory.makeListForm(name, baseForm);
            autogenListForms.put(baseForm, listForm);
            register(listForm);
        }
        return listForm;
    }
    
    
    /**
     * Returns a limit form with the given factors.
     * The new form is given the name "_Limit(factor0Name,factor1Name,...)".
     */
    public LimitForm autogenLimitForm(List<Form> factors) {
        LimitForm limitForm = autogenLimitForms.get(factors);
        if (limitForm == null) {
            StringBuilder buf = new StringBuilder();
            buf.append("_Limit(");
            buf.append(factors.get(0));
            for (int i = 0; i < factors.size(); i++) {
                buf.append(",");
                buf.append(factors.get(i));
            }
            buf.append(")");
            limitForm = FormFactory.makeLimitForm(buf.toString(), factors);
            autogenLimitForms.put(factors, limitForm);
            register(limitForm);
        }
        return limitForm;
    }
    
    
    /**
     * Returns a colimit form with the given factors.
     * The new form is given the name "_Colimit(factor0Name,factor1Name,...)".
     */
    public ColimitForm autogenColimitForm(List<Form> factors) {
        ColimitForm colimitForm = autogenColimitForms.get(factors);
        if (colimitForm == null) {
            StringBuilder buf = new StringBuilder();
            buf.append("_Colimit(");
            buf.append(factors.get(0));
            for (int i = 0; i < factors.size(); i++) {
                buf.append(",");
                buf.append(factors.get(i));
            }
            buf.append(")");
            colimitForm = FormFactory.makeColimitForm(buf.toString(), factors);
            autogenColimitForms.put(factors, colimitForm);
            register(colimitForm);
        }
        return colimitForm;
    }
    
    
    /**
     * Returns a simple form with the given module.
     * The new form is given the name "_Simple(moduleName)".
     */
    public SimpleForm autogenSimpleForm(Module module) {
        SimpleForm simpleForm = autogenSimpleForms.get(module);
        if (simpleForm == null) {
            String name = "_Simple("+module.toString()+")";
            simpleForm = FormFactory.makeModuleForm(name, module);
            autogenSimpleForms.put(module, simpleForm);
            register(simpleForm);
        }
        return simpleForm;
    }
    
    
    /**
     * Removes the form with given name in the repository.
     */
    public synchronized void removeForm(NameDenotator name) {
        forms.remove(name.getNameEntry());
        setChanged();
        notifyObservers();
    }
    
    
    /**
     * Removes the given form from the repository.
     * @param form the form to be removed
     */
    public void removeForm(Form form) {
        removeForm(form.getName());
    }


    /**
     * Removes the forms in the list from the repository.
     */
    private void removeForms(Collection<Form> formSet) {
        for (Form f : formSet) {
            removeForm(f);
        }
    }


    /**
     * Remove the denotator with the given name from the repository.
     * @param name the name for the denotator to be removed
     */
    public synchronized void removeDenotator(NameDenotator name) {
        denotators.remove(name.getNameEntry());
        setChanged();
        notifyObservers();
    }


    /**
     * Removes the given denotator from the repository.
     * @param d the denotator to be removed
     */
    public void removeDenotator(Denotator d) {
        removeDenotator(d.getName());
    }


    /**
     * Removes the denotators in the list from the repository.
     */
    private void removeDenotators(Collection<Denotator> denos) {
        for (Denotator d : denos) {
            removeDenotator(d);
        }
    }
    

    /**
     * Removes all temporary forms and denotators from the repository.
     */
    public void rollback() {
        removeForms(tmpForms);
        removeDenotators(tmpDenos);
        reset();
    }
    
    
    /**
     * Returns the system Scheme environment.
     */
    public Env getSchemeEnvironment() {
        return env;
    }
    
    
    /**
     * Returns the Scheme code.
     */
    public String getSchemeCode() {
        return code;
    }
    
    
    /**
     * Sets the Scheme code.
     * 
     * @return error as a string, and null if no error occurred
     */
    public String setSchemeCode(String s) {
        Parser parser = new Parser();
        List<SExpr> sexprList = parser.parse(s);
        if (parser.hasError()) {
            return parser.getError();
        }
        Evaluator evaluator = new Evaluator(env);
        evaluator.eval(sexprList);
        setChanged();
        notifyObservers();
        if (evaluator.hasErrors()) {
            return evaluator.getErrors().get(0);
        }
        code = s;
        return null;
    }
    

    /**
     * Clears the collections of temporary forms and denotators.
     */
    private void reset() {
        tmpForms.clear();
        tmpDenos.clear();
    }
    
    
    /**
     * Removes all content from the repository. 
     */
    public void clear() {
        reset();
        
        forms      = new HashMap<>(256);
        denotators = new HashMap<>(1024);
        modules    = new HashMap<>(256);
        
        moduleElements  = new HashMap<>(1024);
        moduleMorphisms = new HashMap<>(1024);
        
        builtinForms      = new IdentityHashMap<>(256);
        builtinDenotators = new IdentityHashMap<>(256);
        
        autogenPowerForms   = new HashMap<>(256);
        autogenListForms    = new HashMap<>(256);
        autogenLimitForms   = new HashMap<>(256);
        autogenColimitForms = new HashMap<>(256);
        autogenSimpleForms  = new HashMap<>(256);
        
        env = Env.makeGlobalEnvironment();
        code = "";
        
        setChanged();
        notifyObservers();
    }

    
    /**
     * Returns denotator with the given name if it exists, otherwise null.
     * @param name the name of the denotator to be returned
     */    
    public synchronized Denotator getDenotator(NameDenotator name) {
        DenotatorItem item = denotators.get(name.getNameEntry());
        return (item == null)?null:item.getDenotator();
    }

    
    /**
     * Returns denotator with the given name if it exists, otherwise null.
     * @param name the name of the denotator to be returned
     */    
    public synchronized Denotator getDenotator(NameEntry name) {
        NameEntry nameEntry = NameEntry.lookup(name);
        DenotatorItem item = denotators.get(nameEntry);
        return (item == null)?null:item.getDenotator();
    }

    
    /**
     * Returns denotator with the given name if it exists, otherwise null.
     * @param name the name of the denotator to be returned
     */    
    public synchronized Denotator getDenotator(String name) {
        NameEntry nameEntry = NameEntry.lookup(name);
        DenotatorItem item = denotators.get(nameEntry);
        return (item == null)?null:item.getDenotator();
    }


    public synchronized List<Denotator> getDenotators() {
        LinkedList<Denotator> res = new LinkedList<Denotator>();
        for (DenotatorItem item : denotators.values()) {
            res.add(item.getDenotator());
        }
        return res;
    }

    public synchronized List<Denotator> getCustomDenotators() {
        return denotators.values().stream()
            .filter(denotatorItem -> !denotatorItem.isBuiltin())
            .map(DenotatorItem::getDenotator)
            .collect(Collectors.toList());
    }

    
    public void setNamespace(NameEntry ns) {
        namespace = ns;        
    }
    
    
    public NameEntry getCurrentNamespace() {
        return namespace;
    }
    
    
    public void registerBuiltinModule(String name, Module module) {
        modules.put(name, new ModuleItem(name, module, true));
    }
    
    
    public void registerModule(String name, Module module) {
        modules.put(name, new ModuleItem(name, module, false));
        setChanged();
        notifyObservers();
    }
    
    
    public Module getModule(String name) {
        ModuleItem item = modules.get(name);
        return (item == null)?null:item.getModule();
    }

    public boolean isBuiltinModule(String name) {
        ModuleItem item = modules.get(name);
        return isNullOrBuiltin(item);
    }
    
    
    public List<String> getModuleNames() {
        LinkedList<String> list = new LinkedList<String>();
        list.addAll(modules.keySet());
        return list;
    }
    
    
    public void registerModuleElement(String name, ModuleElement element) {
        moduleElements.put(name, new ModuleElementItem(name, element, false));
        setChanged();
        notifyObservers();
    }
    
    
    public ModuleElement getModuleElement(String name) {
        ModuleElementItem item = moduleElements.get(name);
        return (item == null)?null:item.getModuleElement();
    }

    public boolean isBuiltinModuleElement(String name) {
        ModuleElementItem item = moduleElements.get(name);
        return isNullOrBuiltin(item);
    }
    
    
    public List<String> getModuleElementNames() {
        LinkedList<String> list = new LinkedList<>();
        list.addAll(moduleElements.keySet());
        return list;
    }
    
    
    public void registerModuleMorphism(String name, ModuleMorphism morphism) {
        moduleMorphisms.put(name, new ModuleMorphismItem(name, morphism, false));
        setChanged();
        notifyObservers();
    }
    
    
    public ModuleMorphism getModuleMorphism(String name) {
        ModuleMorphismItem item = moduleMorphisms.get(name);
        return (item == null)?null:item.getModuleMorphism();
    }

    public boolean isBuiltinModuleMorphism(String name) {
        ModuleMorphismItem item = moduleMorphisms.get(name);
        return isNullOrBuiltin(item);
    }
    
    
    public List<String> getModuleMorphismNames() {
        LinkedList<String> list = new LinkedList<>();
        list.addAll(moduleMorphisms.keySet());
        return list;
    }
    
    
    public List<String> getModuleMorphismNames(Module domain, Module codomain) {
        LinkedList<String> list = new LinkedList<>();
        for (Entry<String,ModuleMorphismItem> entry : moduleMorphisms.entrySet()) {
            ModuleMorphism m = entry.getValue().getModuleMorphism();
            if (domain == null || domain.equals(m.getDomain())) {
                if (codomain == null || codomain.equals(m.getCodomain())) {
                    list.add(entry.getKey());
                }
            }
        }
        return list;
    }

    /**
     * Returns the global system-wide repository.
     */    
    public static Repository systemRepository() {
        if (globalRepository == null) {
            globalRepository = makeGlobalRepository();
        }
        return globalRepository;
    }


    /**
     * Creates an empty repository.
     */
    private Repository() {
        clear();
    }
    

    /**
     * Creates the system repository with builtin forms and denotators.
     */
    private static synchronized Repository makeGlobalRepository() {
        Repository rep = new Repository();
        rep.initGlobalRepository();
        return rep;
    }

    private static boolean isNullOrBuiltin(Item item) {
        return item == null || item.builtin;
    }
    
    
    private class Item {
        public Item(boolean builtin) {
            this.builtin = builtin;
        }
        public final boolean isBuiltin() {
            return builtin;
        }
        private boolean builtin;
    }
    
    
    private final class FormItem 
            extends Item
            implements Comparable<FormItem> {
        public FormItem(Form form, boolean builtin) {
            super(builtin);
            this.form = form;
        }
        public Form getForm() {
            return form;
        }
        public int compareTo(FormItem o) {
            return form.getNameString().compareTo(o.form.getNameString());
        }
        private Form form;
    }

    
    private final class DenotatorItem 
            extends Item
            implements Comparable<DenotatorItem> {
        public DenotatorItem(Denotator denotator, boolean builtin) {
            super(builtin);
            this.denotator = denotator;
        }
        public Denotator getDenotator() {
            return denotator;
        }
        public int compareTo(DenotatorItem o) {
            return denotator.getNameString().compareTo(o.denotator.getNameString());
        }
        private Denotator denotator;
    }

    
    private final class ModuleItem
            extends Item
            implements Comparable<ModuleItem> {
        public ModuleItem(String name, Module module, boolean builtin) {
            super(builtin);
            this.name = name;
            this.module = module;
        }
        public Module getModule() {
            return module;
        }
        public String getName() {
            return name;
        }
        public int compareTo(ModuleItem o) {
            return name.compareTo(o.name);
        }
        private String name;
        private Module module;
    }
    
    
    private final class ModuleElementItem
            extends Item
            implements Comparable<ModuleElementItem> {
        public ModuleElementItem(String name, ModuleElement element, boolean builtin) {
            super(builtin);
            this.name = name;
            this.element = element;
        }
        public ModuleElement getModuleElement() {
            return element;
        }
        public String getName() {
            return name;
        }
        public int compareTo(ModuleElementItem o) {
            return name.compareTo(o.name);
        }
        protected String        name;
        private   ModuleElement element;
    }
    
    
    private final class ModuleMorphismItem
            extends Item
            implements Comparable<ModuleMorphismItem> {
        public ModuleMorphismItem(String name, ModuleMorphism morphism, boolean builtin) {
            super(builtin);
            this.name = name;
            this.morphism = morphism;
        }
        public ModuleMorphism getModuleMorphism() {
            return morphism;
        }
        public int compareTo(ModuleMorphismItem o) {
            return morphism.compareTo(o.morphism);
        }
        public String getName() {
            return name;
        }
        public int compareTo(ModuleElementItem o) {
            return name.compareTo(o.name);
        }
        private String         name;
        private ModuleMorphism morphism;
    }
    
    
    public void initGlobalRepository() {
        clear();
        
        // Register simple types
        SimpleForm integerForm = FormFactory.makeZModuleForm("Integer"); 
        registerBuiltin(integerForm);
        SimpleForm realForm = FormFactory.makeRModuleForm("Real");
        registerBuiltin(realForm);
        SimpleForm rationalForm = FormFactory.makeQModuleForm("Rational");
        registerBuiltin(rationalForm);
        SimpleForm complexForm = FormFactory.makeCModuleForm("Complex");
        registerBuiltin(complexForm);
        SimpleForm stringForm = FormFactory.makeZStringModuleForm("String");
        registerBuiltin(stringForm);
        
        // Name form is handled separately
        registerNameForm(NameForm.getNameForm());
        
        // 2D and 3D vectors
        VectorModule<Real> m2d = new VectorModule<>(RRing.ring, 2);
        registerBuiltin(FormFactory.makeModuleForm("Vector2D", m2d));
        VectorModule<Real> m3d = new VectorModule<>(RRing.ring, 3);
        registerBuiltin(FormFactory.makeModuleForm("Vector3D", m3d));
        VectorModule<Real> m4d = new VectorModule<>(RRing.ring, 4);
        registerBuiltin(FormFactory.makeModuleForm("Vector4D", m4d));
        VectorModule<Real> m5d = new VectorModule<>(RRing.ring, 5);
        registerBuiltin(FormFactory.makeModuleForm("Vector5D", m5d));
        VectorModule<Real> m6d = new VectorModule<>(RRing.ring, 6);
        registerBuiltin(FormFactory.makeModuleForm("Vector6D", m6d));
        VectorModule<Real> m7d = new VectorModule<>(RRing.ring, 7);
        registerBuiltin(FormFactory.makeModuleForm("Vector7D", m7d));
        VectorModule<Real> m8d = new VectorModule<>(RRing.ring, 8);
        registerBuiltin(FormFactory.makeModuleForm("Vector8D", m8d));
        VectorModule<Real> m9d = new VectorModule<>(RRing.ring, 9);
        registerBuiltin(FormFactory.makeModuleForm("Vector9D", m9d));
        VectorModule<Real> m10d = new VectorModule<>(RRing.ring, 10);
        registerBuiltin(FormFactory.makeModuleForm("Vector10D", m10d));
        VectorModule<Real> m11d = new VectorModule<>(RRing.ring, 11);
        registerBuiltin(FormFactory.makeModuleForm("Vector11D", m11d));
        VectorModule<Real> m12d = new VectorModule<>(RRing.ring, 12);
        registerBuiltin(FormFactory.makeModuleForm("Vector12D", m12d));
        
        // Polynomials
        PolynomialRing rpol = PolynomialRing.make(RRing.ring, "X");
        registerBuiltin(FormFactory.makeModuleForm("Real[X]", rpol));
        PolynomialRing zpol = PolynomialRing.make(ZRing.ring, "X");
        registerBuiltin(FormFactory.makeModuleForm("Integer[X]", zpol));
        PolynomialRing qpol = PolynomialRing.make(QRing.ring, "X");
        registerBuiltin(FormFactory.makeModuleForm("Rational[X]", qpol));
        PolynomialRing cpol = PolynomialRing.make(CRing.ring, "X");
        registerBuiltin(FormFactory.makeModuleForm("Complex[X]", cpol));
        
        // Tuples
        ProductRing realPair = ProductRing.make(RRing.ring, RRing.ring);
        registerBuiltin(FormFactory.makeModuleForm("RealPair", realPair));
        ProductRing realTriple = ProductRing.make(RRing.ring, RRing.ring, RRing.ring);
        registerBuiltin(FormFactory.makeModuleForm("RealTriple", realTriple));
        
        // Register booleans
        SimpleForm boolForm = FormFactory.makeZnModuleForm("Boolean", 2);
        registerBuiltin(boolForm);
        registerBuiltin(makeDenotator("False", boolForm, 0, 2));
        registerBuiltin(makeDenotator("True", boolForm, 1, 2));
        
        // Several non simple forms
        LimitForm boolPairForm = FormFactory.makeLimitForm("BooleanPair", boolForm, boolForm);
        registerBuiltin(boolPairForm);
        ListForm boolListForm = FormFactory.makeListForm("BooleanList", boolForm);
        registerBuiltin(boolListForm);
        
        ColimitForm intOrRealForm = FormFactory.makeColimitForm("IntegerOrReal", integerForm, realForm);
        registerBuiltin(intOrRealForm);
        
        // Sets
        PowerForm integerSetForm = FormFactory.makePowerForm("IntegerSet", integerForm);
        registerBuiltin(integerSetForm);
        PowerForm realSetForm = FormFactory.makePowerForm("RealSet", realForm);
        registerBuiltin(realSetForm);
        
        // Note and Score
        SimpleForm onsetForm = FormFactory.makeRModuleForm("Onset");
        registerBuiltin(onsetForm);
        SimpleForm pitchForm = FormFactory.makeQModuleForm("Pitch");
        registerBuiltin(pitchForm);
        SimpleForm loudnessForm = FormFactory.makeZModuleForm("Loudness");
        registerBuiltin(loudnessForm);
        SimpleForm durationForm = FormFactory.makeRModuleForm("Duration");
        registerBuiltin(durationForm);
        SimpleForm voiceForm = FormFactory.makeZModuleForm("Voice");
        registerBuiltin(voiceForm);
        
        List<Form> noteFormList = new LinkedList<>();
        List<String> noteFormLabelList = new LinkedList<>();
        noteFormList.add(onsetForm);
        noteFormLabelList.add("onset");
        noteFormList.add(pitchForm);
        noteFormLabelList.add("pitch");
        noteFormList.add(loudnessForm);
        noteFormLabelList.add("loudness");
        noteFormList.add(durationForm);
        noteFormLabelList.add("duration");
        noteFormList.add(voiceForm);
        noteFormLabelList.add("voice");
        LimitForm noteForm = FormFactory.makeLimitForm("Note", noteFormList);
        noteForm.setLabels(noteFormLabelList);
        registerBuiltin(noteForm);
        
        PowerForm scoreForm = FormFactory.makePowerForm("Score", noteForm);
        registerBuiltin(scoreForm);
        
        // Karim's registration of the MacroScore forms
        FormReference formRef = new FormReference("MacroScore", FormDenotatorTypeEnum.POWER);
	
		LimitForm nodeForm = FormFactory.makeLimitForm("Knot", noteForm, formRef);		
		registerBuiltin(nodeForm);
		PowerForm macroScoreForm = FormFactory.makePowerForm("MacroScore", nodeForm);			
		registerBuiltin(macroScoreForm);

		macroScoreForm.resolveReferences(this);
        
        // register modules
        registerBuiltinModule("Integers", ZRing.ring);
        registerBuiltinModule("Reals", RRing.ring);
        registerBuiltinModule("Rationals", QRing.ring);
        registerBuiltinModule("Complexes", CRing.ring);

        registerBuiltinModule("Integers modulo 2", ZnRing.make(2));
        registerBuiltinModule("Integers modulo 12", ZnRing.make(12));
        
        registerBuiltinModule("Pairs of integers", new VectorModule<>(ZRing.ring, 2));
        registerBuiltinModule("Pairs of rationals", new VectorModule<>(QRing.ring, 2));
        registerBuiltinModule("Pairs of reals", new VectorModule<>(RRing.ring, 2));
        registerBuiltinModule("Pairs of complexes", new VectorModule<>(CRing.ring, 2));

        registerBuiltinModule("Triples of integers", new VectorModule<>(ZRing.ring, 3));
        registerBuiltinModule("Triples of rationals", new VectorModule<>(QRing.ring, 3));
        registerBuiltinModule("Triples of reals", new VectorModule<>(RRing.ring, 3));
        registerBuiltinModule("Triples of complexes", new VectorModule<>(CRing.ring, 3));
        
        // Scheme environment and code
        env = Env.makeGlobalEnvironment();
        code = "";
   }


    private void registerNameForm(NameForm form) {
        NameEntry name = form.getName().getNameEntry();
        forms.put(name, new FormItem(form, true));
        builtinForms.put(form, form);
    }


    // Dictionary for forms
    private Map<NameEntry,FormItem> forms;
    
    // Dictionary for denotators
    private Map<NameEntry,DenotatorItem> denotators;
    
    // Dictionary for modules
    private Map<String,ModuleItem> modules;

    // Dictionary for module elements
    private Map<String,ModuleElementItem> moduleElements;

    // Dictionary for module morphisms
    private Map<String,ModuleMorphismItem> moduleMorphisms;

    // The current namespace for lookup
    private NameEntry namespace;
    
    private IdentityHashMap<Form,Form> builtinForms;
    private IdentityHashMap<Denotator,Denotator> builtinDenotators;
        
    // caching tables for autogenerated forms
    private HashMap<Form,PowerForm>              autogenPowerForms;
    private HashMap<Form,ListForm>               autogenListForms;
    private HashMap<List<Form>,LimitForm>   autogenLimitForms;
    private HashMap<List<Form>,ColimitForm> autogenColimitForms;
    private HashMap<Module,SimpleForm>           autogenSimpleForms;
    
    // Containers for temporary forms and denotators
    private LinkedList<Form> tmpForms = new LinkedList<>();
    private LinkedList<Denotator> tmpDenos = new LinkedList<>();
    
    // Scheme environment and code
    private Env env;
    private String code;
    
    // The unique global repository
    private static Repository globalRepository = null;
}
