/*
 * Copyright (C) 2005 Gérard Milmeister
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

package org.rubato.rubettes.builtin;

import static org.rubato.logeo.DenoFactory.makeDenotator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.rubato.base.*;
import org.rubato.composer.RunInfo;
import org.rubato.composer.components.JConnectorSliders;
import org.rubato.composer.icons.Icons;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.w3c.dom.Element;


public class RealArithRubette extends AbstractRubette {    
    
    public RealArithRubette() {
        setInCount(1);
        setOutCount(1);
        values = new double[getInCount()];
        for (int i = 0; i < values.length; i++) {
            values[i] = 0.0;
        }
    }

    
    public void run(RunInfo runInfo) {
        if (vm == null) {
            addError(Messages.getString("RealArithRubette.novalidexpression")); 
            return;
        }
        SimpleForm resForm = realForm;
        for (int i = getInCount()-1; i >= 0; i--) {
            Denotator d = getInput(i);
            if (d == null) {
                values[i] = 0.0;
            }
            else if (d.getForm() instanceof SimpleForm) {
                SimpleForm f = (SimpleForm)d.getForm();
                if (f.getModule() == RRing.ring) {
                    values[i] = ((SimpleDenotator)d).getReal();
                    resForm = f;
                }
                else if (f.getModule() == ZRing.ring) {
                    values[i] = ((SimpleDenotator)d).getInteger();                    
                }
                else if (f.getModule() == QRing.ring) {
                    values[i] = ((SimpleDenotator)d).getRational().doubleValue();                    
                }
                else if (f.getModule() instanceof ZnRing) {
                    values[i] = ((SimpleDenotator)d).getModInteger();                    
                }
                else {
                    addError(Messages.getString("RealArithRubette.inputnotreal"), i); 
                }
            }
            else {
                addError(Messages.getString("RealArithRubette.inputnotreal"), i); 
            }
        }
        if (!hasErrors()) {
            vm.eval(values);
            if (vm.hasError()) {
                addError(vm.getError());
                return;
            }
            if (isResultReal) {
                setOutput(0, makeDenotator(resForm, vm.getRealResult()));
            }
            else {
                setOutput(0, vm.getBooleanResult()?trueDeno:falseDeno);                
            }
        }
    }

    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }


    public Rubette duplicate() {
        RealArithRubette rubette = new RealArithRubette();
        rubette.setInCount(getInCount());
        rubette.setOutCount(getOutCount());
        rubette.expressionString = expressionString;
        rubette.isResultReal = isResultReal;
        rubette.compile(expressionString, getInCount());
        rubette.values = new double[getInCount()];
        return rubette;
    }
    
    
    public String getName() {
        return "RealArith"; 
    }

    
    public boolean hasProperties() {
        return true;
    }
    

    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());
            
            Box box = new Box(BoxLayout.Y_AXIS);
            
            inSlider = new JConnectorSliders(true, false);
            inSlider.setInLimits(1, 8);
            inSlider.setInValue(getInCount());
            box.add(inSlider);
            
            JPanel resultPanel = new JPanel();
            resultPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("RealArithRubette.resulttype"))); 
            ButtonGroup group = new ButtonGroup();
            final JRadioButton realButton = new JRadioButton(Messages.getString("RealArithRubette.real")); 
            group.add(realButton);
            resultPanel.add(realButton);
            JRadioButton booleanButton = new JRadioButton(Messages.getString("RealArithRubette.boolean")); 
            resultPanel.add(booleanButton);
            group.add(booleanButton);
            realButton.setSelected(isResultReal);
            booleanButton.setSelected(!isResultReal);
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    isResultReal = realButton.isSelected();
                } 
            };
            realButton.addActionListener(listener);
            booleanButton.addActionListener(listener);
            box.add(resultPanel);
            
            properties.add(box, BorderLayout.NORTH);
            
            exprTextArea = new JTextArea(5, 20);
            exprTextArea.setText(expressionString);
            JScrollPane scrollPane = new JScrollPane(exprTextArea);
            properties.add(scrollPane, BorderLayout.CENTER);
            
            infoLabel = new JLabel(" "); 
            infoLabel.setForeground(Color.RED);
            infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            properties.add(infoLabel, BorderLayout.SOUTH);
        }
        return properties;
    }
    
    
    public boolean applyProperties() {
        infoLabel.setText(" "); 
        int newInCount = inSlider.getInValue();
        String expr = exprTextArea.getText().trim();
        if (expr.length() == 0) {
            infoLabel.setText(Messages.getString("RealArithRubette.noexpression")); 
            return false;
        }
        boolean ok = compile(expr, newInCount);
        if (!ok) {
            infoLabel.setText(compiler.getError());
            return false;
        }
        else {
            setInCount(newInCount);
            expressionString = expr;
            values = new double[getInCount()];
            return true;
        }
    }
    
    
    public void revertProperties() {
        infoLabel.setText(" "); 
        inSlider.setInValue(getInCount());
        exprTextArea.setText(expressionString);
        compile(expressionString, getInCount());
    }
    

    public String getShortDescription() {
        return "Evaluates an expression of real arithmetic";
    }

    
    public String getLongDescription() {
        return "The RealArith Rubette evaluates an expression "+
               "of real arithmetic on its inputs.";
    }
    
    
    public String getInTip(int i) {
        return TextUtils.replaceStrings(Messages.getString("RealArithRubette.intip"), i); 
    }

    
    public String getOutTip(int i) {
        return Messages.getString("RealArithRubette.outtip"); 
    }
    

    public ImageIcon getIcon() {
        return icon;
    }
    
    
    private static final String INPUTS      = "Inputs"; 
    private static final String EXPRESSION  = "Expression"; 
    private static final String NUMBER_ATTR = "number"; 
    private static final String RES_ATTR    = "res"; 
    private static final String EXPR_ATTR   = "expr"; 
    
    
    public void toXML(XMLWriter writer) {
        writer.empty(INPUTS, NUMBER_ATTR, getInCount());
        writer.empty(EXPRESSION,
                     RES_ATTR, isResultReal?1:0,
                     EXPR_ATTR, expressionString);
    }

    
    public Rubette fromXML(XMLReader reader, Element element) {
        RealArithRubette rubette = new RealArithRubette();
        Element child = XMLReader.getChild(element, INPUTS);
        if (child != null) {
            int n = XMLReader.getIntAttribute(child, NUMBER_ATTR, 1);
            rubette.setInCount(n);
            child = XMLReader.getNextSibling(child, EXPRESSION);
            if (child != null) {
                rubette.expressionString = XMLReader.getStringAttribute(child, EXPR_ATTR);
                rubette.isResultReal = (XMLReader.getIntAttribute(child, RES_ATTR, 1) == 1);
                rubette.compile(rubette.expressionString, n);
            }
        }
        return rubette;
    }

    
    private boolean compile(String expr, int nrArgs) {
        if (compiler == null) {
            compiler = new ArithCompiler();
        }
        vm = null;
        compiler.setExpression(expr, isResultReal);
        if (compiler.parse(nrArgs)) {
            vm = compiler.getVM();
            return true;
        }
        return false;
    }
    
    
    private JPanel            properties = null;
    private JTextArea         exprTextArea = null;
    private JLabel            infoLabel = null;
    private String            expressionString = ""; 
    private double[]          values = null;
    private JConnectorSliders inSlider = null;
    private ArithCompiler     compiler = null;
    private ArithVM           vm = null;
    protected boolean         isResultReal = true;      
    
    private static final SimpleForm realForm; 
    private static final Denotator  trueDeno; 
    private static final Denotator  falseDeno;
    private static final ImageIcon  icon;
    
    static {
        icon = Icons.loadIcon(RealArithRubette.class, "/images/rubettes/builtin/arithicon.png"); 
        Repository rep = Repository.systemRepository();
        realForm = (SimpleForm)rep.getForm("SReal"); 
        trueDeno = rep.getDenotator("True"); 
        falseDeno = rep.getDenotator("False");         
    }
}
