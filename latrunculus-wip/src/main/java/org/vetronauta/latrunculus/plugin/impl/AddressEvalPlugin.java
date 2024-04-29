package org.vetronauta.latrunculus.plugin.impl;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.rubato.rubettes.builtin.address.AddressMessages;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.logeo.DenoFactory;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.FactorDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public final class AddressEvalPlugin extends AbstractRubette {

    private static final String INPUT_NULL_ERROR    = AddressMessages.getString("AddressEvalRubette.inputnullerror");
    private static final String ELEMENTNOTSET_ERROR = AddressMessages.getString("AddressEvalRubette.elementnotset");
    private static final String MODULENOTSET_ERROR  = AddressMessages.getString("AddressEvalRubette.modulenotset");
    private static final String ADDRESSMODULE_ERROR = AddressMessages.getString("AddressEvalRubette.addressmoduleerror");
    private static final String LISTNOTSET_ERROR    = AddressMessages.getString("AddressEvalRubette.listnotset");
    private static final String OUTPUTFORMNOTSET_ERROR = AddressMessages.getString("AddressEvalRubette.outputformnotset");
    private static final String INPUT_WRONG_FORM    = AddressMessages.getString("AddressEvalRubette.inputwrongform");
    private static final String MORPHISMNOTSET_ERROR = AddressMessages.getString("AddressEvalRubette.morphismnotset");
    private static final String ADDRESSMORPHISM_ERROR = AddressMessages.getString("AddressEvalRubette.addressmorphismerror");
    private static final String INPUT2NOTSET_ERROR  = AddressMessages.getString("AddressEvalRubette.secinputnull");
    private static final String INPUT2WRONGTYPE_ERROR = AddressMessages.getString("AddressEvalRubette.secinputwrongtype");

    private EvalType evalType = EvalType.NULL;
    private ModuleElement<?, ?> moduleElement;
    private Module<?, ?> module;
    private List<? extends ModuleElement> elements;
    private Form outputForm;
    private ModuleMorphism<?, ?, ?, ?> morphism;

    public AddressEvalPlugin(EvalType evalType) {
        this.evalType = evalType;
    }

    public AddressEvalPlugin(EvalType evalType, ModuleElement<?, ?> moduleElement, Module<?, ?> module) {
        this(evalType);
        this.moduleElement = moduleElement;
        this.module = module;
    }

    public AddressEvalPlugin(AddressEvalPlugin.EvalType evalType, Form form) {
        this(evalType);
        this.outputForm = form;
    }

    public AddressEvalPlugin(AddressEvalPlugin.EvalType evalType, Form form, List<? extends ModuleElement> elements, Module<?, ?> module) {
        this(evalType, form);
        this.elements = elements;
        this.module = module;
    }

    public AddressEvalPlugin(AddressEvalPlugin.EvalType evalType, ModuleMorphism<?, ?, ?, ?> morphism) {
        this(evalType);
        this.morphism = morphism;
    }

    private AddressEvalPlugin(AddressEvalPlugin other) {
        //TODO deepcopy of the fields
        this.evalType = other.evalType;
        this.moduleElement = other.moduleElement;
        this.module = other.module;
        this.elements = other.elements;
        this.outputForm = other.outputForm;
        this.morphism = other.morphism;
    }

    @Override
    public void run(RunInfo runInfo) {
        Denotator input = getInput(0);
        Denotator res = null;
        if (input == null) {
            addError(INPUT_NULL_ERROR);
        }
        else {
            switch (evalType) {
                case NULL:
                    res = input.atNull();
                    break;
                case ELEMENT:
                    res = runEvalTypeElement(input);
                    break;
                case LIST:
                    res = runEvalTypeList(input);
                    break;
                case CHANGE:
                    res = runEvalTypeChange(input);
                    break;
                case INPUT:
                    res = runEvalTypeInput(input);
            }
        }
        setOutput(0, res);
    }

    /**
     * Evaluates denotator <code>input</code> at the
     * configured module element.
     */
    private Denotator runEvalTypeElement(Denotator input) {
        if (moduleElement == null){
            addError(ELEMENTNOTSET_ERROR);
            return null;
        }
        if (module == null) {
            addError(MODULENOTSET_ERROR);
            return null;
        }
        if (!input.getAddress().equals(module)) {
            addError(ADDRESSMODULE_ERROR, input.getAddress(), module);
            return null;
        }
        try {
            return input.at(moduleElement);
        }
        catch (MappingException e) {
            addError(e);
            return null;
        }
    }

    /**
     * Evaluates denotator <code>input</code> at the configured
     * list of elements. The result is a power or list denotator
     * whose form hat been configured before. The input denotator
     * must have the same form as the base form of the power
     * or list form.
     */
    private Denotator runEvalTypeList(Denotator input) {
        if (elements == null) {
            addError(LISTNOTSET_ERROR);
            return null;
        }
        if (module == null) {
            addError(MODULENOTSET_ERROR);
            return null;
        }
        if (!input.getAddress().equals(module)) {
            addError(ADDRESSMODULE_ERROR, input.getAddress(), module);
            return null;
        }
        if (outputForm == null) {
            addError(OUTPUTFORMNOTSET_ERROR);
            return null;
        }
        if (!input.hasForm(outputForm.getForm(0))) {
            addError(INPUT_WRONG_FORM, input.getForm(), outputForm.getForm(0));
            return null;
        }
        try {
            LinkedList<Denotator> denoList = new LinkedList<>();
            for (ModuleElement<?, ?> e : elements) {
                denoList.add(input.at(e));
            }
            if (outputForm instanceof PowerForm) {
                return new PowerDenotator(null, module.getNullModule(), (PowerForm)outputForm, denoList);
            }
            if (outputForm instanceof ListForm) {
                return new ListDenotator(null, module.getNullModule(), (ListForm)outputForm, denoList);
            }
        } catch (LatrunculusCheckedException e) {
            addError(e);
        }
        return null;
    }

    /**
     * Changes address of the input denotator using the configured
     * address changing morphism.
     */
    private Denotator runEvalTypeChange(Denotator input) {
        if (morphism == null) {
            addError(MORPHISMNOTSET_ERROR);
            return null;
        }
        if (!input.getAddress().equals(morphism.getCodomain())) {
            addError(ADDRESSMORPHISM_ERROR, input.getAddress(), morphism);
            return null;
        }
        return input.changeAddress(morphism);

    }

    /**
     * Evaluates the input denotator at the element(s) in
     * a second input denotator.
     */
    private Denotator runEvalTypeInput(Denotator input) {
        Denotator res = null;
        Denotator input2 = getInput(1);
        if (input2 == null) {
            addError(INPUT2NOTSET_ERROR);
            return null;
        }
        if (outputForm == null) {
            // if no output form is configured
            // 2nd input denotator must be of type simple
            if (!(input2 instanceof SimpleDenotator)) {
                addError(INPUT2WRONGTYPE_ERROR);
                return null;
            }
            SimpleDenotator d = (SimpleDenotator) input2;
            ModuleElement<?, ?> el = d.getElement();
            try {
                return input.at(el);
            }
            catch (MappingException e) {
                addError(INPUT2WRONGTYPE_ERROR);
                return null;
            }
        }
        // if an output form is configured
        // 2nd input denotator may be of type power or list
        // containing denotators of type simple
        if (input2 instanceof PowerDenotator || input2 instanceof ListDenotator) {
            List<Denotator> list = ((FactorDenotator)input2).getFactors();
            if (CollectionUtils.isEmpty(list)) {
                return DenoFactory.makeDenotator(outputForm);
            }
            if (list.get(0) instanceof SimpleDenotator) {
                try {
                    LinkedList<Denotator> reslist = new LinkedList<>();
                    for (Denotator d : list) {
                        reslist.add(input.at(((SimpleDenotator)d).getElement()));
                    }
                    return DenoFactory.makeDenotator(outputForm, reslist);
                }
                catch (MappingException e) {
                    addError(INPUT2WRONGTYPE_ERROR);
                    return null;
                }
            }
            addError(INPUT2WRONGTYPE_ERROR);
            return null;
        }
        if (input2 instanceof SimpleDenotator) {
            Denotator result = input.changeAddress(((SimpleDenotator)input2).getModuleMorphism());
            if (result == null) {
                addError(INPUT2WRONGTYPE_ERROR);
            }
            return result;
        }
        addError(INPUT2WRONGTYPE_ERROR);
        return null;
    }

    @Override
    public String getName() {
        return "AddressEval";
    }

    @Override
    public AddressEvalPlugin duplicate() {
        return new AddressEvalPlugin(this);
    }
    
    public enum EvalType {
        NULL, ELEMENT, LIST, CHANGE, INPUT;

        public static EvalType ofIndex(int i) {
            if (i < 0 || i >= EvalType.values().length) {
                return null;
            }
            return EvalType.values()[i];
        }

    }
    
}
