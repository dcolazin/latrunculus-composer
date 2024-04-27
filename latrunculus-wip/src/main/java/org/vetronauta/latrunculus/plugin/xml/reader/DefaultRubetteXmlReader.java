package org.vetronauta.latrunculus.plugin.xml.reader;

import org.rubato.composer.RubetteManager;
import org.rubato.composer.network.NetworkMessages;
import org.rubato.composer.network.NetworkModel;
import org.rubato.composer.notes.NoteModel;
import org.rubato.composer.rubette.Link;
import org.rubato.composer.rubette.RubetteModel;
import org.rubato.rubettes.alteration.AlterationRubette;
import org.rubato.rubettes.alteration.JAlterationDimensionsTable;
import org.rubato.rubettes.bigbang.BigBangRubette;
import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.graph.BigBangOperationGraph;
import org.rubato.rubettes.builtin.BooleanRubette;
import org.rubato.rubettes.builtin.ConstructorRubette;
import org.rubato.rubettes.builtin.DisplayRubette;
import org.rubato.rubettes.builtin.LatchRubette;
import org.rubato.rubettes.builtin.ListRubette;
import org.rubato.rubettes.builtin.MacroInputRubette;
import org.rubato.rubettes.builtin.MacroOutputRubette;
import org.rubato.rubettes.builtin.MacroRubette;
import org.rubato.rubettes.builtin.ModuleMapRubette;
import org.rubato.rubettes.builtin.MuxRubette;
import org.rubato.rubettes.builtin.RealArithRubette;
import org.rubato.rubettes.builtin.ReformRubette;
import org.rubato.rubettes.builtin.RegisterRubette;
import org.rubato.rubettes.builtin.SchemeRubette;
import org.rubato.rubettes.builtin.SelectFormRubette;
import org.rubato.rubettes.builtin.SetRubette;
import org.rubato.rubettes.builtin.SimpleRubette;
import org.rubato.rubettes.builtin.SourceRubette;
import org.rubato.rubettes.builtin.SplitRubette;
import org.rubato.rubettes.builtin.StatRubette;
import org.rubato.rubettes.builtin.address.AddressEvalRubette;
import org.rubato.rubettes.builtin.address.AddressMessages;
import org.rubato.rubettes.score.MidiFileInRubette;
import org.rubato.rubettes.score.MidiFileOutRubette;
import org.rubato.rubettes.score.ScaleRubette;
import org.rubato.rubettes.score.ScorePlayRubette;
import org.rubato.rubettes.score.ScoreToCsoundRubette;
import org.rubato.rubettes.select2d.Select2DPanel;
import org.rubato.rubettes.select2d.Select2DRubette;
import org.rubato.rubettes.util.CoolFormRegistrant;
import org.rubato.rubettes.wallpaper.WallpaperRubette;
import org.vetronauta.latrunculus.client.plugin.properties.BooleanClientProperty;
import org.vetronauta.latrunculus.client.plugin.properties.ClientPluginProperty;
import org.vetronauta.latrunculus.client.plugin.properties.ClientPropertiesFactory;
import org.vetronauta.latrunculus.client.plugin.properties.ComplexClientProperty;
import org.vetronauta.latrunculus.client.plugin.properties.DenotatorClientProperty;
import org.vetronauta.latrunculus.client.plugin.properties.StringClientProperty;
import org.vetronauta.latrunculus.plugin.properties.DoubleProperty;
import org.vetronauta.latrunculus.client.plugin.properties.FileClientProperty;
import org.vetronauta.latrunculus.client.plugin.properties.FormClientProperty;
import org.vetronauta.latrunculus.plugin.properties.IntegerProperty;
import org.vetronauta.latrunculus.plugin.properties.PluginProperties;
import org.vetronauta.latrunculus.plugin.properties.RationalProperty;
import org.vetronauta.latrunculus.client.plugin.properties.ClientPluginProperties;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;
import org.vetronauta.latrunculus.plugin.properties.StringProperty;
import org.vetronauta.latrunculus.client.plugin.properties.TextClientProperty;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.SimpleAbstractRubette;
import org.vetronauta.latrunculus.server.parse.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.server.xml.XMLConstants;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.reader.LatrunculusXmlReader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.rubato.rubettes.builtin.ListRubette.LIST_OP_LENGTH;
import static org.rubato.rubettes.builtin.SetRubette.SET_OP_LENGTH;
import static org.rubato.rubettes.builtin.StatRubette.STAT_OP_LENGTH;
import static org.rubato.rubettes.builtin.address.AddressEvalRubette.EVAL_TYPE_CHANGE;
import static org.rubato.rubettes.builtin.address.AddressEvalRubette.EVAL_TYPE_ELEMENT;
import static org.rubato.rubettes.builtin.address.AddressEvalRubette.EVAL_TYPE_INPUT;
import static org.rubato.rubettes.builtin.address.AddressEvalRubette.EVAL_TYPE_LIST;
import static org.rubato.rubettes.builtin.address.AddressEvalRubette.EVAL_TYPE_TYPE_LENGTH;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.CODE;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.COUNT_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.DIMENSION;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.ELEMENT_PATH;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.END;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.END_DEGREE;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.EVALTYPE;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.EXPRESSION;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.EXPR_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.FILE;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.FREQ;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.GLOBAL;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.INFO;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.INPUTS;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.INT;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.INTERVAL;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.LABEL;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.LABELS;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.LONG;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.MAP_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.MORPHISM;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.NAME;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.NUMBER_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.NUMBER_OF_NOTES;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.OPERATION;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.OP_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.OUTPUTS;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.PATH;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.PRESET;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.PROLOG;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.REFRESHABLE;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.RES_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.ROOT;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.R_FROM;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.R_TO;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.SCALE;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.SELECTED;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.SELECTION_PANEL;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.SEMITONES;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.SHORT;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.START;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.START_DEGREE;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.STORE_DENOTATOR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.TEMPO_FACTOR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.TIMESCALE;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.VOICES;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.XML;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.CLASS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.DENOTATOR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.DESTPOS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.DEST_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LINK;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_ELEMENT;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_MORPHISM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NAME_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NETWORK;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NOTE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.RUBETTE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.SERIAL_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.SRCPOS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.SRC_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TRUE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.X_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.Y_ATTR;

public class DefaultRubetteXmlReader implements LatrunculusXmlReader<Rubette> {

    public NetworkModel readNetworkModel(XMLReader reader, Element networkElement) {
        String name = networkElement.getAttribute(NAME_ATTR).trim();
        if (name.length() == 0) {
            reader.setError(NetworkMessages.getString("NetworkModel.missingattr"), NETWORK, NAME_ATTR);
            return null;
        }
        else {
            NetworkModel networkModel = new NetworkModel(name);
            RubetteManager manager = RubetteManager.getManager();
            HashMap<Integer, RubetteModel> rubetteModelMap = new HashMap<Integer,RubetteModel>();

            // read rubettes
            Element child = XMLReader.getChild(networkElement, RUBETTE);
            while (child != null) {
                String rubName = child.getAttribute(NAME_ATTR);
                String rubClass = child.getAttribute(CLASS_ATTR);
                int serial = XMLReader.getIntAttribute(child, SERIAL_ATTR, 0);
                int x = XMLReader.getIntAttribute(child, X_ATTR, 0);
                int y = XMLReader.getIntAttribute(child, Y_ATTR, 0);
                Rubette rubette = manager.getRubetteByClassName(rubClass);
                if (rubette == null) {
                    reader.setError(NetworkMessages.getString("NetworkModel.classnotavailable"), rubClass);
                }
                else {
                    rubette = fromXML(child, rubette.getClass(), reader);
                    if (rubette != null) {
                        RubetteModel rmodel = new RubetteModel(rubette, name);
                        rmodel.setName(rubName);
                        rmodel.setLocation(new Point(x, y));
                        rubetteModelMap.put(serial, rmodel);
                        networkModel.addRubette(rmodel);
                    }
                }
                child = XMLReader.getNextSibling(child, RUBETTE);
            }

            if (reader.hasError()) {
                return null;
            }

            // read links
            child = XMLReader.getChild(networkElement, LINK);
            while (child != null) {
                int src = XMLReader.getIntAttribute(child, SRC_ATTR, 0);
                int srcPos = XMLReader.getIntAttribute(child, SRCPOS_ATTR, 0);
                int dest = XMLReader.getIntAttribute(child, DEST_ATTR, 0);
                int destPos = XMLReader.getIntAttribute(child, DESTPOS_ATTR, 0);
                int type = XMLReader.getIntAttribute(child, TYPE_ATTR, 0);
                RubetteModel srcModel = rubetteModelMap.get(src);
                RubetteModel destModel = rubetteModelMap.get(dest);
                if (srcModel != null && destModel != null) {
                    Link link = new Link(srcModel, srcPos, destModel, destPos);
                    link.setType(type);
                    srcModel.addOutLink(link);
                    destModel.setInLink(link);
                }
                else {
                    reader.setError(NetworkMessages.getString("NetworkModel.cannotlink"),
                        srcModel == null?"unknown":srcModel.getName(), srcPos,
                        destModel== null?"unknown":destModel.getName(), destPos);
                }
                child = XMLReader.getNextSibling(child, LINK);
            }

            // read notes
            child = XMLReader.getChild(networkElement, NOTE);
            while (child != null) {
                NoteModel noteModel = NoteModel.fromXML(reader, child);
                networkModel.addNote(noteModel);
                child = XMLReader.getNextSibling(child, NOTE);
            }

            return networkModel;
        }
    }

    @Override
    public Rubette fromXML(Element element, Class<? extends Rubette> clazz, XMLReader reader) {
        if (MacroRubette.class.isAssignableFrom(clazz)) {
            return readMacroRubette(reader, element);
        }
        if (AddressEvalRubette.class.isAssignableFrom(clazz)) {
            return readAddressEvalRubette(reader, element);
        }
        if (AlterationRubette.class.isAssignableFrom(clazz)) {
            return readAlterationRubette(reader, element);
        }
        if (BigBangRubette.class.isAssignableFrom(clazz)) {
            return readBigBangRubette(reader, element);
        }
        if (BooleanRubette.class.isAssignableFrom(clazz)) {
            return readBooleanRubette(reader, element);
        }
        if (ConstructorRubette.class.isAssignableFrom(clazz)) {
            return readConstructorRubette(reader, element);
        }
        if (DisplayRubette.class.isAssignableFrom(clazz)) {
            return readDisplayRubette(reader, element);
        }
        if (LatchRubette.class.isAssignableFrom(clazz)) {
            return readLatchRubette(reader, element);
        }
        if (ListRubette.class.isAssignableFrom(clazz)) {
            return readListRubette(reader, element);
        }
        if (MacroInputRubette.class.isAssignableFrom(clazz)) {
            return readMacroInputRubette(reader, element);
        }
        if (MacroOutputRubette.class.isAssignableFrom(clazz)) {
            return readMacroOutputRubette(reader, element);
        }
        if (MidiFileInRubette.class.isAssignableFrom(clazz)) {
            return readMidiFileInRubette(reader, element);
        }
        if (MidiFileOutRubette.class.isAssignableFrom(clazz)) {
            return readMidiFileOutRubette(reader, element);
        }
        if (ModuleMapRubette.class.isAssignableFrom(clazz)) {
            return readModuleMapRubette(reader, element);
        }
        if (MuxRubette.class.isAssignableFrom(clazz)) {
            return readMuxRubette(reader, element);
        }
        if (RealArithRubette.class.isAssignableFrom(clazz)) {
            return readRealArithRubette(reader, element);
        }
        if (ReformRubette.class.isAssignableFrom(clazz)) {
            return readReformRubette(reader, element);
        }
        if (RegisterRubette.class.isAssignableFrom(clazz)) {
            return readRegisterRubette(reader, element);
        }
        if (ScaleRubette.class.isAssignableFrom(clazz)) {
            return readScaleRubette(reader, element);
        }
        if (SchemeRubette.class.isAssignableFrom(clazz)) {
            return readSchemeRubette(reader, element);
        }
        if (ScorePlayRubette.class.isAssignableFrom(clazz)) {
            return readScorePlayRubette(reader, element);
        }
        if (ScoreToCsoundRubette.class.isAssignableFrom(clazz)) {
            return readScoreToCsoundRubette(reader, element);
        }
        if (Select2DRubette.class.isAssignableFrom(clazz)) {
            return readSelect2DRubette(reader, element);
        }
        if (SelectFormRubette.class.isAssignableFrom(clazz)) {
            return readSelectFormRubette(reader, element);
        }
        if (SetRubette.class.isAssignableFrom(clazz)) {
            return readSetRubette(reader, element);
        }
        if (SimpleRubette.class.isAssignableFrom(clazz)) {
            return readSimpleRubette(reader, element);
        }
        if (SourceRubette.class.isAssignableFrom(clazz)) {
            return readSourceRubette(reader, element);
        }
        if (SplitRubette.class.isAssignableFrom(clazz)) {
            return readSplitRubette(reader, element);
        }
        if (StatRubette.class.isAssignableFrom(clazz)) {
            return readStatRubette(reader, element);
        }
        if (WallpaperRubette.class.isAssignableFrom(clazz)) {
            return readWallpaperRubette(reader, element);
        }
        if (SimpleAbstractRubette.class.isAssignableFrom(clazz)) {
            return readSimpleAbstractRubette(reader, element, clazz);
        }
        return null;
    }

    private Rubette readMacroRubette(XMLReader reader, Element element) {
        Element infoElement = XMLReader.getChild(element, INFO);
        String info0 = XMLReader.getText(infoElement).trim();
        Element shortElement = XMLReader.getNextSibling(infoElement, SHORT);
        String shortDesc0 = XMLReader.getText(shortElement).trim();
        Element longElement = XMLReader.getNextSibling(shortElement, LONG);
        String longDesc0 = XMLReader.getText(longElement).trim();
        Element networkElement = XMLReader.getNextSibling(longElement, NETWORK);
        NetworkModel model = readNetworkModel(reader, networkElement);
        MacroRubette rubette = new MacroRubette();
        rubette.setNetworkModel(model);
        rubette.setInfo(info0);
        rubette.setShortDescription(shortDesc0);
        rubette.setLongDescription(longDesc0);
        return rubette;
    }

    private Rubette readAddressEvalRubette(XMLReader reader, Element element) {
        int t = 0;
        AddressEvalRubette newRubette = null;

        Element child = XMLReader.getChild(element, EVALTYPE);

        if (child == null) {
            // there must be a type
            reader.setError(AddressMessages.getString("AddressEvalRubette.missingelement"), EVALTYPE);
            return null;
        }

        t = XMLReader.getIntAttribute(child, VALUE_ATTR, 0, EVAL_TYPE_TYPE_LENGTH - 1, 0);

        if (t == EVAL_TYPE_ELEMENT) {
            // type evaluate at element
            child = XMLReader.getNextSibling(child, MODULE_ELEMENT);
            if (child != null) {
                ModuleElement mel = reader.parseModuleElement(child);
                if (mel != null) {
                    newRubette = new AddressEvalRubette(t, mel, mel.getModule());
                }
            }
            else {
                reader.setError(AddressMessages.getString("AddressEvalRubette.missingelement"), MODULE_ELEMENT);
            }
        }
        else if (t == EVAL_TYPE_LIST) {
            // type evaluate at list of elements
            child = XMLReader.getNextSibling(child, FORM);
            if (child == null) {
                // no output form has been given
                // there must be an output form
                reader.setError(AddressMessages.getString("AddressEvalRubette.missingelement"), FORM);
                return null;
            }
            // get output form
            Form oform = reader.parseAndResolveForm(child);
            child = XMLReader.getNextSibling(child, MODULE);
            if (child == null) {
                // no module has been given
                // there must be a module
                reader.setError(AddressMessages.getString("AddressEvalRubette.missingelement"), MODULE);
                return null;
            }
            Module module0 = reader.parseModule(child);
            if (module0 == null) {
                return null;
            }
            LinkedList<ModuleElement> list = new LinkedList<>();
            child = XMLReader.getNextSibling(child, MODULE_ELEMENT);
            while (child != null) {
                ModuleElement e = reader.parseModuleElement(child);
                if (e == null) {
                    return null;
                }
                if (!module0.hasElement(e)) {
                    reader.setError(AddressMessages.getString("AddressEvalRubette.wrongmodule"), e.getModule(), module0);
                    return null;
                }
                list.add(e);
                child = XMLReader.getNextSibling(child, MODULE_ELEMENT);
            }
            newRubette = new AddressEvalRubette(t, oform, list, module0);
        }
        else if (t == EVAL_TYPE_CHANGE) {
            // type change address
            child = XMLReader.getNextSibling(child, MODULE_MORPHISM);
            if (child == null) {
                // no module morphism has been given
                // there must be a module morphism
                reader.setError(AddressMessages.getString("AddressEvalRubette.missingelement"), MODULE_MORPHISM);
                return null;
            }
            ModuleMorphism morphism0 = reader.parseModuleMorphism(child);
            if (morphism0 == null) {
                return null;
            }
            newRubette = new AddressEvalRubette(t, morphism0);
        }
        else if (t == EVAL_TYPE_INPUT) {
            // get output form if any
            Form oform = null;
            child = XMLReader.getNextSibling(child, FORM);
            if (child != null) {
                oform = reader.parseAndResolveForm(child);
            }
            newRubette = new AddressEvalRubette(t, oform);
            newRubette.setInCount(2);
        }
        else {
            newRubette = new AddressEvalRubette(0);
        }

        return newRubette;
    }

    private Rubette readAlterationRubette(XMLReader reader, Element element) {
        AlterationRubette loadedRubette = new AlterationRubette();
        Element child = XMLReader.getChild(element, FORM);
        loadedRubette.setInputForm((PowerForm) reader.parseAndResolveForm(child));

        Element nextChild = XMLReader.getNextSibling(child, GLOBAL);
        if (nextChild != null) {
            loadedRubette.setGlobalStartDegree(XMLReader.getRealAttribute(nextChild, START, 0));
            loadedRubette.setGlobalEndDegree(XMLReader.getRealAttribute(nextChild, END, 1));
            loadedRubette.setGlobal(true);
            child = nextChild;
        }

        loadDimensionTable(loadedRubette.getDimensionsTable(), reader, child);

        return loadedRubette;
    }

    private void loadDimensionTable(JAlterationDimensionsTable dimensionsTable, XMLReader reader, Element element) {
        Element nextSibling = XMLReader.getNextSibling(element, DIMENSION);

        while (nextSibling != null) {
            double startDegree = XMLReader.getRealAttribute(nextSibling, START_DEGREE, 0);
            double endDegree = XMLReader.getRealAttribute(nextSibling, END_DEGREE, 1);

            Element child = XMLReader.getChild(nextSibling, FORM);
            SimpleForm selectedForm = (SimpleForm) reader.parseAndResolveForm(child);

            child = XMLReader.getNextSibling(child, FORM);
            SimpleForm relativeToForm = (SimpleForm) reader.parseAndResolveForm(child);

            dimensionsTable.addDimension(selectedForm, startDegree, endDegree, relativeToForm);
            nextSibling = XMLReader.getNextSibling(nextSibling, DIMENSION);
        }

        dimensionsTable.applyChanges();
    }

    private Rubette readBigBangRubette(XMLReader reader, Element element) {
        new CoolFormRegistrant().registerAllTheCoolStuff();
        BigBangModel loadedModel = readBigBangModel(reader, element);
        return new BigBangRubette(loadedModel);
    }

    private static BigBangModel readBigBangModel(XMLReader reader, Element element) {
        BigBangModel loadedModel = new BigBangModel();
        try {
            Form form = reader.parseAndResolveForm(XMLReader.getChild(element, FORM));
            loadedModel.setForm(form);
            loadedModel.setGraph(BigBangOperationGraph.fromXML(loadedModel, reader, element));
            //initiates bbobjects and all
            loadedModel.updateComposition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadedModel;
    }

    private Rubette readBooleanRubette(XMLReader reader, Element element) {
        BooleanRubette rubette = new BooleanRubette();
        Element child = XMLReader.getChild(element, INPUTS);
        if (child != null) {
            int n = XMLReader.getIntAttribute(child, NUMBER_ATTR, 1);
            rubette.setInCount(n);
            child = XMLReader.getNextSibling(child, EXPRESSION);
            if (child != null) {
                rubette.setExpressionString(XMLReader.getStringAttribute(child, EXPR_ATTR));
                rubette.getExpression().parse(rubette.getExpressionString(), rubette.getInCount());
            }
        }
        return rubette;
    }

    private Rubette readConstructorRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, FORM);
        Form f = reader.parseAndResolveForm(child);
        ConstructorRubette newRubette = new ConstructorRubette();
        newRubette.setForm(f);
        return newRubette;
    }

    private Rubette readDisplayRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, XML);
        boolean xmlValue = false;
        if (child != null) {
            String val = child.getAttribute(VALUE_ATTR);
            xmlValue = val.equals(TRUE_VALUE);
        }
        DisplayRubette newRubette = new DisplayRubette();
        newRubette.setXML(xmlValue);
        return newRubette;
    }

    private Rubette readLatchRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, OUTPUTS);
        if (child != null) {
            int n = XMLReader.getIntAttribute(child, NUMBER_ATTR, 1, 8, 0);
            LatchRubette r = new LatchRubette();
            r.setOutCount(n);
            return r;
        }
        else {
            return null;
        }
    }

    private Rubette readListRubette(XMLReader reader, Element element) {
        // read operation type
        Element child = XMLReader.getChild(element, OPERATION);
        if (child == null) {
            return null;
        }
        int op0 = XMLReader.getIntAttribute(child, OP_ATTR, 0, LIST_OP_LENGTH - 1, 0);

        // read number of inputs
        child = XMLReader.getChild(element, INPUTS);
        if (child == null) {
            return null;
        }
        int n0 = XMLReader.getIntAttribute(child, NUMBER_ATTR, 1, 8, 2);

        ListRubette newRubette = new ListRubette(op0);
        newRubette.setInCount(n0);
        return newRubette;
    }

    private Rubette readMacroInputRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, LABELS);
        int outCount = XMLReader.getIntAttribute(child, COUNT_ATTR, 1, 8, 1);
        String[] lbls = new String[outCount];
        child = XMLReader.getChild(child, LABEL);
        int i = 0;
        while (child != null) {
            lbls[i] = child.getAttribute(NAME_ATTR);
            i++;
            child = XMLReader.getNextSibling(child, LABEL);
        }
        return new MacroInputRubette(outCount, lbls);
    }

    private Rubette readMacroOutputRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, LABELS);
        int inCount = XMLReader.getIntAttribute(child, COUNT_ATTR, 1, 8, 1);
        String[] lbls = new String[inCount];
        child = XMLReader.getChild(child, LABEL);
        int i = 0;
        while (child != null) {
            lbls[i] = child.getAttribute(NAME_ATTR);
            i++;
            child = XMLReader.getNextSibling(child, LABEL);
        }
        return new MacroOutputRubette(inCount, lbls);
    }

    private Rubette readMidiFileInRubette(XMLReader reader, Element element) {
        String fileName = "";
        double f = 1.0;
        Denotator score = null;
        boolean store = false;
        Element child = XMLReader.getChild(element, FILE);
        if (child != null) {
            fileName = XMLReader.getStringAttribute(child, NAME_ATTR);
        }
        child = XMLReader.getNextSibling(child, TEMPO_FACTOR);
        if (child != null) {
            f = XMLReader.getRealAttribute(child, VALUE_ATTR, 1.0);
        }
        child = XMLReader.getNextSibling(child, STORE_DENOTATOR);
        if (child != null) {
            if (TRUE_VALUE.equals(child.getAttribute(VALUE_ATTR))) store = true;
        }
        child = XMLReader.getNextSibling(child, DENOTATOR);
        if (child != null) {
            score = reader.parseDenotator(child);
        }
        MidiFileInRubette newRubette = new MidiFileInRubette();
        newRubette.setMidiFile(reader.toAbsolutePath(fileName));
        newRubette.setTempoFactor(f);
        newRubette.setScore(score);
        newRubette.setStoreDenotator(store);
        return newRubette;
    }

    private Rubette readMidiFileOutRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, "File");
        if (child != null) {
            String fileName = XMLReader.getStringAttribute(child, "name");
            MidiFileOutRubette newRubette = new MidiFileOutRubette();
            newRubette.setMidiFile(reader.toAbsolutePath(fileName));
            return newRubette;
        }
        else {
            return null;
        }
    }

    private Rubette readModuleMapRubette(XMLReader reader, Element element) {
        Form iform = null;
        Element child = XMLReader.getChild(element, FORM);
        if (child == null) {
            // no input form has been given
            return new ModuleMapRubette();
        }

        // get input form
        iform = reader.parseAndResolveForm(child);

        // get path
        child = XMLReader.getNextSibling(child, PATH);
        if (child == null) {
            // no path has been given
            return new ModuleMapRubette(iform);
        }
        String pathString = child.getAttribute(VALUE_ATTR);
        int[] p = parsePath(pathString);
        if (p == null) {
            reader.setError("Path has not the correct format");
            return null;
        }

        // get module morphism
        child = XMLReader.getNextSibling(child, MODULE_MORPHISM);
        if (child == null) {
            // no module morphism has been given
            return new ModuleMapRubette(iform, p);
        }
        ModuleMorphism m = reader.parseModuleMorphism(child);
        if (m == null) {
            return null;
        }

        return new ModuleMapRubette(iform, p, m);
    }

    private static int[] parsePath(String pathString) {
        String p = pathString.trim();
        if (p.length() == 0) {
            return new int[0];
        }
        String[] strings = pathString.trim().split(",");
        int[] path = new int[strings.length];
        for (int i = 0; i < path.length; i++) {
            try {
                path[i] = Integer.parseInt(strings[i]);
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
        return path;
    }

    private Rubette readMuxRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, INPUTS);
        if (child != null) {
            int n = XMLReader.getIntAttribute(child, NUMBER_ATTR, 2, 8, 2);
            MuxRubette r = new MuxRubette();
            r.setInCount(n);
            return r;
        }
        else {
            return null;
        }
    }

    private Rubette readRealArithRubette(XMLReader reader, Element element) {
        RealArithRubette rubette = new RealArithRubette();
        Element child = XMLReader.getChild(element, INPUTS);
        if (child != null) {
            int n = XMLReader.getIntAttribute(child, NUMBER_ATTR, 1);
            rubette.setInCount(n);
            child = XMLReader.getNextSibling(child, EXPRESSION);
            if (child != null) {
                rubette.setExpressionString(XMLReader.getStringAttribute(child, EXPR_ATTR));
                rubette.setResultReal(XMLReader.getIntAttribute(child, RES_ATTR, 1) == 1);
                rubette.compile(rubette.getExpressionString(), n);
            }
        }
        return rubette;
    }

    private Rubette readReformRubette(XMLReader reader, Element element) {
        Form form = null;
        Element child = XMLReader.getChild(element, FORM);
        if (child != null) {
            form = reader.parseAndResolveForm(child);
        }
        return new ReformRubette(form);
    }

    private Rubette readRegisterRubette(XMLReader reader, Element element) {
        RegisterRubette newRubette = new RegisterRubette();
        String name = null;
        Element child = XMLReader.getChild(element, NAME);
        if (child != null) {
            name = child.getAttribute(VALUE_ATTR);
        }
        newRubette.setDenotatorName(name);
        return newRubette;
    }

    private Rubette readScaleRubette(XMLReader reader, Element element) {
        ScaleRubette loadedRubette = new ScaleRubette();
        loadedRubette.getProperties();
        Element child = XMLReader.getChild(element, SCALE);
        int numberOfNotes = XMLReader.getIntAttribute(child, NUMBER_OF_NOTES, 7);
        loadedRubette.setNumberOfNotes(numberOfNotes);
        loadedRubette.setRoot(XMLReader.getRealAttribute(child, ROOT, 60));
        int loadedPreset = XMLReader.getIntAttribute(child, PRESET, 0);
        loadedRubette.setPreset(loadedPreset);
        if (loadedPreset == 0) {
            double[] intervals = new double[numberOfNotes];
            int i = 0;
            Element nextSibling = XMLReader.getChild(child, INTERVAL);
            while (nextSibling != null) {
                intervals[i] = XMLReader.getRealAttribute(nextSibling, SEMITONES, 1);
                nextSibling = XMLReader.getNextSibling(nextSibling, INTERVAL);
                i++;
            }
            loadedRubette.setIntervals(intervals);
        }
        loadedRubette.revertProperties();
        return loadedRubette;
    }

    private Rubette readSchemeRubette(XMLReader reader, Element element) {
        int inCount;
        int outCount;
        String text = "";

        Element child = XMLReader.getChild(element, INPUTS);
        if (child != null) {
            inCount = XMLReader.getIntAttribute(child, NUMBER_ATTR, 1, 8, 0);
        }
        else {
            reader.setError("Expected element <%1>", INPUTS);
            return null;
        }

        child = XMLReader.getNextSibling(child, OUTPUTS);
        if (child != null) {
            outCount = XMLReader.getIntAttribute(child, NUMBER_ATTR, 1, 8, 0);
        }
        else {
            reader.setError("Expected element <%1>", OUTPUTS);
            return null;
        }
        child = XMLReader.getNextSibling(child, CODE);
        if (child != null) {
            text = XMLReader.getText(child).trim();
        }
        else {
            reader.setError("Expected element <%1>", CODE);
            return null;
        }

        SchemeRubette r = new SchemeRubette();
        r.setInCount(inCount);
        r.setOutCount(outCount);
        r.setSchemeCode(text);
        r.setSexprList(r.getParser().parse(r.getSchemeCode()));
        return r;
    }

    private Rubette readScorePlayRubette(XMLReader reader, Element element) {
        ScorePlayRubette rubette = new ScorePlayRubette();
        Element child = XMLReader.getChild(element, VOICES);
        if (child != null) {
            String map = child.getAttribute(MAP_ATTR);
            String[] vcs = map.split(",");
            int[] voices0 = new int[vcs.length];
            for (int i = 0; i < voices0.length; i++) {
                try {
                    voices0[i] = Integer.parseInt(vcs[i]);
                    if (voices0[i] < 0) {
                        voices0[i] = 0;
                    }
                    else if (voices0[i] > 15) {
                        voices0[i] = 15;
                    }
                }
                catch (NumberFormatException e) {
                    voices0[i] = 0;
                }
            }
            rubette.setVoices(voices0);
        }
        return rubette;
    }

    private Rubette readScoreToCsoundRubette(XMLReader reader, Element element) {
        ScoreToCsoundRubette newRubette = new ScoreToCsoundRubette();
        Element child = XMLReader.getChild(element, FILE);
        if (child != null) {
            String fileName = XMLReader.getStringAttribute(child, XMLConstants.NAME_ATTR);
            newRubette.setScoFile(reader.toAbsolutePath(fileName));
        }
        child = XMLReader.getChild(element, TIMESCALE);
        if (child != null) {
            newRubette.setTimeScale(Math.max(XMLReader.getRealAttribute(child, XMLConstants.VALUE_ATTR, 0.5), 0.0));
        }
        child = XMLReader.getChild(element, FREQ);
        if (child != null) {
            newRubette.setA4freq(Math.max(XMLReader.getRealAttribute(child, XMLConstants.VALUE_ATTR, 440.0), 1.0));
        }
        child = XMLReader.getChild(element, PROLOG);
        if (child != null) {
            newRubette.setProlog(XMLReader.getText(child).trim());
        }
        return newRubette;
    }

    private Rubette readSelect2DRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, FORM);
        if (child == null) {
            // no form
            return new Select2DRubette();
        }

        // parse form
        Form f = reader.parseAndResolveForm(child);

        // parse selection panels
        child = XMLReader.getNextSibling(child, SELECTION_PANEL);
        ArrayList<Select2DPanel> selectionPanels = new ArrayList<Select2DPanel>();
        while (child != null) {
            Select2DPanel panel = Select2DPanel.fromXML(reader, child, f);
            if (panel != null) {
                selectionPanels.add(panel);
            }
            child = XMLReader.getNextSibling(child, SELECTION_PANEL);
        }

        Select2DRubette rubette = new Select2DRubette(f, selectionPanels);
        rubette.getProperties();

        return rubette;
    }

    private Rubette readSelectFormRubette(XMLReader reader, Element element) {
        Form form = null;
        Element child = XMLReader.getChild(element, FORM);
        if (child != null) {
            form = reader.parseAndResolveForm(child);
        }
        SelectFormRubette rubette = new SelectFormRubette();
        rubette.setOutputForm(form);
        return rubette;
    }

    private Rubette readSetRubette(XMLReader reader, Element element) {
        // read operation type
        Element child = XMLReader.getChild(element, OPERATION);
        if (child == null) {
            return null;
        }
        int op0 = XMLReader.getIntAttribute(child, OP_ATTR, 0, SET_OP_LENGTH-1, 0);

        // read number of inputs
        child = XMLReader.getChild(element, INPUTS);
        if (child == null) {
            return null;
        }
        int n = XMLReader.getIntAttribute(child, NUMBER_ATTR, 1, 8, 2);

        return new SetRubette(n, op0);
    }

    private Rubette readSimpleRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, DENOTATOR);
        Denotator d = null;
        if (child != null) {
            d = reader.parseDenotator(child);
        }
        SimpleRubette rubette = new SimpleRubette();
        if (d instanceof SimpleDenotator) {
            rubette.setDenotator((SimpleDenotator)d);
        }
        return rubette;
    }

    private Rubette readSourceRubette(XMLReader reader, Element element) {
        Denotator d = null;
        boolean r = false;
        Element child = XMLReader.getChild(element, REFRESHABLE);
        String val = child.getAttribute(VALUE_ATTR);
        if (val.equals(TRUE_VALUE)) {
            r = true;
        }
        child = XMLReader.getNextSibling(child, DENOTATOR);
        if (child != null) {
            d = reader.parseDenotator(child);
        }
        return new SourceRubette(d, r);
    }

    private Rubette readSplitRubette(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, FORM);
        Form f = reader.parseAndResolveForm(child);
        SplitRubette newRubette = new SplitRubette();
        if (f instanceof LimitForm) {
            LimitForm limitForm = (LimitForm)f;
            int formCount = limitForm.getFormCount();
            ArrayList<Integer> selected = new ArrayList<>();
            child = XMLReader.getNextSibling(child, SELECTED);
            while (child != null) {
                int i = XMLReader.getIntAttribute(child, VALUE_ATTR, 0, Integer.MAX_VALUE, 0);
                if (i < formCount && !selected.contains(i)) {
                    selected.add(i);
                }
                child = XMLReader.getNextSibling(child, SELECTED);
            }
            Collections.sort(selected);
            int[] sel = new int[selected.size()];
            for (int i = 0; i < sel.length; i++) {
                sel[i] = selected.get(i);
            }
            newRubette.set(limitForm, sel);
        }
        return newRubette;
    }

    private Rubette readStatRubette(XMLReader reader, Element element) {
        // read operation type
        Element child = XMLReader.getChild(element, OPERATION);
        if (child == null) {
            return null;
        }
        int op0 = XMLReader.getIntAttribute(child, OP_ATTR, 0, STAT_OP_LENGTH-1, 0);

        Form form0 = null;
        child = XMLReader.getNextSibling(child, XMLConstants.FORM);
        if (child != null) {
            form0 = reader.parseAndResolveForm(child);
        }

        return new StatRubette(form0, op0);
    }

    private Rubette readWallpaperRubette(XMLReader reader, Element element) {
        WallpaperRubette loadedRubette = new WallpaperRubette();
        loadedRubette.init();
        Element child = XMLReader.getChild(element, FORM);
        loadedRubette.setInputForm((PowerForm)reader.parseAndResolveForm(child));

        child = XMLReader.getNextSibling(child, MORPHISM);
        while (child != null) {
            int currentFrom = XMLReader.getIntAttribute(child, R_FROM, 0);
            int currentTo = XMLReader.getIntAttribute(child, R_TO, 1);
            Element grandChild = XMLReader.getChild(child, MODULE_MORPHISM);
            ModuleMorphism currentMorphism = reader.parseModuleMorphism(grandChild);
            java.util.List<java.util.List<Integer>> currentElementPaths = new ArrayList<java.util.List<Integer>>();
            grandChild = XMLReader.getNextSibling(grandChild, ELEMENT_PATH);
            while (grandChild != null) {
                List<Integer> currentElementPath = new ArrayList<Integer>();
                Element greatGrandChild = XMLReader.getChild(grandChild, INT);
                while (greatGrandChild != null) {
                    int currentInt = XMLReader.getIntAttribute(greatGrandChild, VALUE_ATTR, 0);
                    currentElementPath.add(currentInt);
                    greatGrandChild = XMLReader.getNextSibling(greatGrandChild, INT);
                }
                currentElementPaths.add(currentElementPath);
                grandChild = XMLReader.getNextSibling(grandChild, ELEMENT_PATH);
            }
            loadedRubette.addMorphism(currentMorphism, currentFrom, currentTo, currentElementPaths);
            child = XMLReader.getNextSibling(child, MORPHISM);
        }
        return loadedRubette;
    }

    private Rubette readSimpleAbstractRubette(XMLReader reader, Element element, Class<? extends Rubette> clazz) {
        PluginProperties newProp = readRubetteProperties(reader, element);
        SimpleAbstractRubette rubette;
        try {
            rubette = (SimpleAbstractRubette) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            reader.setError(e);
            return null;
        }
        rubette.setProperties(ClientPropertiesFactory.build(newProp));
        //had to add this, Gerard's code was buggy
        rubette.applyProperties();
        return rubette;
    }

    private PluginProperties readRubetteProperties(XMLReader reader, Element element) {
        PluginProperties newProp = new PluginProperties();
        Node node = element.getFirstChild();
        while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element)node;
                String key = e.getTagName();
                PluginProperty<?> property = newProp.get(key);
                if (property != null) { //TODO this cannot work, property is always null...
                    property = readRubetteProperty(reader, e, property.getClass(), key);
                    newProp.put(property);
                }
            }
            node = node.getNextSibling();
        }
        return newProp;
    }

    private PluginProperty<?> readRubetteProperty(XMLReader reader, Element element, Class<? extends PluginProperty> clazz, String key) {
        if (BooleanClientProperty.class.isAssignableFrom(clazz)) {
            return readBooleanProperty(reader, element, key);
        }
        if (ComplexClientProperty.class.isAssignableFrom(clazz)) {
            return readComplexProperty(reader, element, key);
        }
        if (DenotatorClientProperty.class.isAssignableFrom(clazz)) {
            return readDenotatorProperty(reader, element, key);
        }
        if (DoubleProperty.class.isAssignableFrom(clazz)) {
            return readDoubleProperty(reader, element, key);
        }
        if (FileClientProperty.class.isAssignableFrom(clazz)) {
            return readFileProperty(reader, element, key);
        }
        if (FormClientProperty.class.isAssignableFrom(clazz)) {
            return readFormProperty(reader, element, key);
        }
        if (IntegerProperty.class.isAssignableFrom(clazz)) {
            return readIntegerProperty(reader, element, key);
        }
        if (RationalProperty.class.isAssignableFrom(clazz)) {
            return readRationalProperty(reader, element, key);
        }
        if (StringProperty.class.isAssignableFrom(clazz)) {
            return readStringProperty(reader, element, key);
        }
        if (TextClientProperty.class.isAssignableFrom(clazz)) {
            return readTextProperty(reader, element, key);
        }
        return null;
    }

    private PluginProperty<Boolean> readBooleanProperty(XMLReader reader, Element element, String key) {
        boolean value = XMLReader.getStringAttribute(element, VALUE_ATTR).equals(TRUE_VALUE);
        return new PluginProperty<>(key, key, value);
    }

    private PluginProperty<Complex> readComplexProperty(XMLReader reader, Element element, String key) {
        Complex complex;
        try {
            complex = ArithmeticParsingUtils.parseComplex(XMLReader.getStringAttribute(element, VALUE_ATTR));
        } catch (NumberFormatException e) {
            complex = new Complex();
        }
        return new PluginProperty<>(key, key, complex);
    }

    private PluginProperty<Denotator> readDenotatorProperty(XMLReader reader, Element element, String key) {
        Element child = XMLReader.getChild(element, "Denotator");
        Denotator denotator = null;
        if (child != null) {
            denotator = reader.parseDenotator(child);
        }
        return new PluginProperty<>(key, key, denotator);
    }

    private DoubleProperty readDoubleProperty(XMLReader reader, Element element, String key) {
        return new DoubleProperty(key, key, XMLReader.getRealAttribute(element, VALUE_ATTR, 0));
    }

    private PluginProperty<File> readFileProperty(XMLReader reader, Element element, String key) {
        return new PluginProperty<>(key, key, new File(reader.toAbsolutePath(XMLReader.getStringAttribute(element, VALUE_ATTR))));
    }

    private PluginProperty<Form> readFormProperty(XMLReader reader, Element element, String key) {
        Element child = XMLReader.getChild(element, "Form");
        Form form = child != null ? reader.parseAndResolveForm(child) : null;
        return new PluginProperty<>(key, key, form);
    }

    private IntegerProperty readIntegerProperty(XMLReader reader, Element element, String key) {
        return new IntegerProperty(key, key, XMLReader.getIntAttribute(element, VALUE_ATTR, Integer.MIN_VALUE, Integer.MAX_VALUE, 0));
    }

    private RationalProperty readRationalProperty(XMLReader reader, Element element, String key) {
        String s = XMLReader.getStringAttribute(element, VALUE_ATTR);
        Rational rational;
        try {
           rational = ArithmeticParsingUtils.parseRational(s);
        } catch (NumberFormatException e) {
            rational = new Rational(0);
        }
        return new RationalProperty(key, key, rational);
    }

    private StringProperty readStringProperty(XMLReader reader, Element element, String key) {
        return new StringProperty(key, key, XMLReader.getStringAttribute(element, VALUE_ATTR));
    }

    private PluginProperty<String> readTextProperty(XMLReader reader, Element element, String key) {
        return new PluginProperty<>(key, key, XMLReader.getText(element).trim());
    }

}
