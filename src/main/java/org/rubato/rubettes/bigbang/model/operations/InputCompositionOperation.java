package org.rubato.rubettes.bigbang.model.operations;

import org.rubato.base.RubatoException;
import org.vetronauta.latrunculus.core.math.yoneda.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.PowerDenotator;
import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.OperationPathResults;
import org.vetronauta.latrunculus.server.xml.XMLConstants;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.DefaultDefinitionXmlWriter;
import org.vetronauta.latrunculus.server.xml.writer.DefinitionXmlWriter;
import org.w3c.dom.Element;

public class InputCompositionOperation extends AbstractOperation {

	//TODO rubette writer
	private final DefinitionXmlWriter definitionXmlWriter = new DefaultDefinitionXmlWriter();
	
	private Denotator composition;
	private Denotator modifiedComposition;
	
	protected InputCompositionOperation(BigBangModel model, InputCompositionOperation other) {
		super(model);
		this.init(other.composition);
	}
	
	public InputCompositionOperation(BigBangModel model, Denotator composition) {
		super(model);
		this.init(composition);
	}
	
	public InputCompositionOperation(BigBangModel model, XMLReader reader, Element element) {
		super(model, reader, element);
		this.fromXML(reader, element);
	}
	
	private void init(Denotator composition) {
		this.isAnimatable = true;
		this.isSplittable = false;
		this.minModRatio = 0.0;
		this.maxModRatio = 1.0;
		this.setOrAddComposition(composition);
	}
	
	public void setOrAddComposition(Denotator composition) {
		this.composition = composition;
		this.updateOperation();
	}

	/*
	 * if the toplevel denotator is a powerset or a list, adjust number of factors
	 */
	@Override
	protected void updateOperation() {
		if (this.composition instanceof PowerDenotator) {
			PowerDenotator clone = ((PowerDenotator)this.composition).copy();
			int modifiedNumberOfElements = (int)Math.round(clone.getFactorCount()*this.modificationRatio);
			try {
				clone.replaceFactors(clone.getFactors().subList(0, modifiedNumberOfElements));
			} catch (RubatoException e) { }
			this.modifiedComposition = clone;
		} else if (this.composition instanceof ListDenotator) {
			ListDenotator clone = ((ListDenotator)this.composition).copy();
			int modifiedNumberOfElements = (int)Math.round(clone.getFactorCount()*this.modificationRatio);
			try {
				clone.replaceFactors(clone.getFactors().subList(0, modifiedNumberOfElements));
			} catch (RubatoException e) { }
			this.modifiedComposition = clone; 
		} else {
			this.modifiedComposition = this.composition; 
		}
	}
	
	public OperationPathResults execute() {
		return this.model.getDenotatorManager().setOrAddComposition(this.modifiedComposition);
	}
	
	@Override
	protected String getSpecificPresentationName() {
		if (this.composition != null) {
			return "Input " + this.composition.getForm().getNameString();
		}
		return "Input";
	}
	
	public void toXML(XMLWriter writer) {
		super.toXML(writer);
		definitionXmlWriter.toXML(composition, writer);
	}
	
	private void fromXML(XMLReader reader, Element element) {
		this.setOrAddComposition(reader.parseDenotator(XMLReader.getChild(element, XMLConstants.DENOTATOR)));
	}

}
