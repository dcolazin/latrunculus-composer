package org.rubato.rubettes.bigbang;

import org.rubato.rubettes.bigbang.controller.BigBangController;
import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.view.model.BigBangSwingView;
import org.rubato.rubettes.bigbang.view.model.MTBigBangView;
import org.rubato.rubettes.util.CoolFormRegistrant;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import javax.swing.*;

public class BigBangRubette extends AbstractRubette {
	
	public static final boolean IS_MULTITOUCH = false;
	public static final String STANDARD_FORM_NAME = "FMSet";

	private BigBangModel model;
	private BigBangSwingView view;
	private BigBangController controller;
	
	/**
	 * Creates a basic BigBangRubette.
	 */
	public BigBangRubette() {
		this(null);
	}
	
	public BigBangRubette(BigBangModel model) {
		new CoolFormRegistrant().registerAllTheCoolStuff();
		this.setInCount(1);
        this.setOutCount(1);
        this.controller = new BigBangController();
        if (BigBangRubette.IS_MULTITOUCH) {
        	this.view = new MTBigBangView(this.controller);
        } else {
        	this.view = new BigBangSwingView(this.controller);
        }
        if (model != null) {
        	this.model = model;
        } else {
        	this.model = new BigBangModel();
        }
        this.model.setController(this.controller);
        this.model.setMultiTouch(false);
	}

	public BigBangModel getBigBangModel() {
		return model;
	}

	@Override
	public Rubette duplicate() {
		return new BigBangRubette(this.model.clone());
	}

	@Override
	public String getName() {
		return "BigBang";
	}
	
	/**
     * Returns the fact that BigBangRubette belongs to the core rubettes
     */
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

	@Override
	public void run(RunInfo runInfo) {
		//TODO: SHOULD REALLY NOT HAVE TO BE HERE. PUT BACK TO REPOSITORY
		new CoolFormRegistrant().registerAllTheCoolStuff();
		Denotator output = this.model.getComposition();
		if (this.model.isInputActive()) {
			this.verifyAndSetInput();
		}
		this.setOutput(0, output);
	}
	
	private void verifyAndSetInput() {
		Denotator input = this.getInput(0);
		if (input != null) {
			this.model.setOrAddComposition(input);
		} else {
			this.addError("Input denotator is null.");
		}
	}
	
	public boolean hasView() {
		return true;
	}
	
	public JComponent getView() {
		return this.view.getPanel();
	}

}
