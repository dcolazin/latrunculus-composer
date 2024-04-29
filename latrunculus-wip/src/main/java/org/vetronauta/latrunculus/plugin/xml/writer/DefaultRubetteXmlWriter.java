package org.vetronauta.latrunculus.plugin.xml.writer;

import edu.uci.ics.jung.graph.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.rubato.rubettes.alteration.AlterationRubette;
import org.rubato.rubettes.alteration.JAlterationDimensionsTable;
import org.rubato.rubettes.bigbang.BigBangRubette;
import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.graph.BigBangOperationGraph;
import org.rubato.rubettes.bigbang.model.graph.CompositionState;
import org.rubato.rubettes.bigbang.model.operations.AbstractOperation;
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
import org.rubato.rubettes.score.MidiFileInRubette;
import org.rubato.rubettes.score.MidiFileOutRubette;
import org.rubato.rubettes.score.ScaleRubette;
import org.rubato.rubettes.score.ScorePlayRubette;
import org.rubato.rubettes.score.ScoreToCsoundRubette;
import org.rubato.rubettes.select2d.Select2DPanel;
import org.rubato.rubettes.select2d.Select2DRubette;
import org.rubato.rubettes.wallpaper.JWallpaperDimensionsTable;
import org.rubato.rubettes.wallpaper.WallpaperRubette;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.SimpleAbstractRubette;
import org.vetronauta.latrunculus.plugin.impl.AddressEvalPlugin;
import org.vetronauta.latrunculus.server.xml.XMLConstants;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.LatrunculusXmlWriter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.CLASSNAME_ATTR;
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
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.GRAPH_TAG;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.HEAD_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.INFO;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.INPUTS;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.INT;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.INTERVAL;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.LABEL;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.LABELS;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.LOGICAL_POSITION_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.LONG;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.MAP_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.MORPHISM;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.NAME;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.NUMBER_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.NUMBER_OF_NOTES;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.NUMBER_OF_STATES_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.OPERATION;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.OPERATION_TAG;
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
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.TAIL_ATTR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.TEMPO_FACTOR;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.TIMESCALE;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.VOICES;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.XML;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FALSE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NAME_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TRUE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUE_ATTR;

public class DefaultRubetteXmlWriter implements LatrunculusXmlWriter<Rubette> {

    @Override
    public void toXML(Rubette object, XMLWriter writer) {
        if (object == null) {
            return;
        }
        if (object instanceof AddressEvalRubette) {
            write(((AddressEvalRubette) object).getPlugin(), writer);
            return;
        }
        if (object instanceof AlterationRubette) {
            write((AlterationRubette) object, writer);
            return;
        }
        if (object instanceof BigBangRubette) {
            write((BigBangRubette) object, writer);
            return;
        }
        if (object instanceof BooleanRubette) {
            write((BooleanRubette) object, writer);
            return;
        }
        if (object instanceof ConstructorRubette) {
            write((ConstructorRubette) object, writer);
            return;
        }
        if (object instanceof DisplayRubette) {
            write((DisplayRubette) object, writer);
            return;
        }
        if (object instanceof LatchRubette) {
            write((LatchRubette) object, writer);
            return;
        }
        if (object instanceof ListRubette) {
            write((ListRubette) object, writer);
            return;
        }
        if (object instanceof MacroInputRubette) {
            write((MacroInputRubette) object, writer);
            return;
        }
        if (object instanceof MacroOutputRubette) {
            write((MacroOutputRubette) object, writer);
            return;
        }
        if (object instanceof MacroRubette) {
            write((MacroRubette) object, writer);
            return;
        }
        if (object instanceof MidiFileInRubette) {
            write((MidiFileInRubette) object, writer);
            return;
        }
        if (object instanceof MidiFileOutRubette) {
            write((MidiFileOutRubette) object, writer);
            return;
        }
        if (object instanceof ModuleMapRubette) {
            write((ModuleMapRubette) object, writer);
            return;
        }
        if (object instanceof MuxRubette) {
            write((MuxRubette) object, writer);
            return;
        }
        if (object instanceof RealArithRubette) {
            write((RealArithRubette) object, writer);
            return;
        }
        if (object instanceof ReformRubette) {
            write((ReformRubette) object, writer);
            return;
        }
        if (object instanceof RegisterRubette) {
            write((RegisterRubette) object, writer);
            return;
        }
        if (object instanceof ScaleRubette) {
            write((ScaleRubette) object, writer);
            return;
        }
        if (object instanceof SchemeRubette) {
            write((SchemeRubette) object, writer);
            return;
        }
        if (object instanceof ScorePlayRubette) {
            write((ScorePlayRubette) object, writer);
            return;
        }
        if (object instanceof ScoreToCsoundRubette) {
            write((ScoreToCsoundRubette) object, writer);
            return;
        }
        if (object instanceof Select2DRubette) {
            write((Select2DRubette) object, writer);
            return;
        }
        if (object instanceof SelectFormRubette) {
            write((SelectFormRubette) object, writer);
            return;
        }
        if (object instanceof SetRubette) {
            write((SetRubette) object, writer);
            return;
        }
        if (object instanceof SimpleRubette) {
            write((SimpleRubette) object, writer);
            return;
        }
        if (object instanceof SourceRubette) {
            write((SourceRubette) object, writer);
            return;
        }
        if (object instanceof SplitRubette) {
            write((SplitRubette) object, writer);
            return;
        }
        if (object instanceof StatRubette) {
            write((StatRubette) object, writer);
            return;
        }
        if (object instanceof WallpaperRubette) {
            write((WallpaperRubette) object, writer);
            return;
        }
        if (object instanceof SimpleAbstractRubette) {
            write((SimpleAbstractRubette) object, writer);
        }
    }

    private void write(AddressEvalPlugin plugin, XMLWriter writer) {
        writer.empty(EVALTYPE, VALUE_ATTR, plugin.getEvalType());
        switch (plugin.getEvalType()) {
            case INPUT:
                if (plugin.getOutputForm() != null) {
                    writer.writeFormRef(plugin.getOutputForm());
                }
            case CHANGE:
                writer.writeModuleMorphism(plugin.getMorphism());
                return;
            case LIST:
                if (plugin.getOutputForm() != null) {
                    writer.writeFormRef(plugin.getOutputForm());
                    writer.writeModule(plugin.getModule());
                    plugin.getElements().forEach(writer::writeModuleElement);
                }
                return;
            case ELEMENT:
                writer.writeModuleElement(plugin.getModuleElement());
        }
    }

    private void write(AlterationRubette rubette, XMLWriter writer) {
        if (rubette.getInputForm() == null) {
            return;
        }
        writer.writeFormRef(rubette.getInputForm());
        if (rubette.isGlobal()) {
            writer.empty(GLOBAL, START, rubette.getGlobalStartDegree(), END, rubette.getGlobalEndDegree());
        }

        JAlterationDimensionsTable table = rubette.getDimensionsTable();
        for (int i = 0; i < table.dimensionCount(); i++) {
            writer.openBlock(DIMENSION, START_DEGREE, table.getStartPercentage(i), END_DEGREE, table.getEndPercentage(i));
            writer.writeFormRef(table.getForm(i));
            writer.writeFormRef(table.getRelativeForm(i));
            writer.closeBlock();
        }
    }

    private void write(BigBangRubette rubette, XMLWriter writer) {
        BigBangModel model = rubette.getBigBangModel();
        writer.writeFormRef(model.getDenotatorManager().getForm());
        BigBangOperationGraph graph = model.getTransformationGraph();
        writer.openBlock(GRAPH_TAG, NUMBER_OF_STATES_ATTR, graph.getCompositionStateSize());
        for (AbstractOperation currentOperation : graph.getAllOperationsInAddedOrder()) {
            Pair<CompositionState> currentEndpoints = graph.getEndpoints(currentOperation);
            int currentHead = currentEndpoints.getFirst().getIndex();
            int currentTail = currentEndpoints.getSecond().getIndex();
            writer.openBlock(OPERATION_TAG, CLASSNAME_ATTR, currentOperation.getClass().getName(),
                HEAD_ATTR, currentHead, TAIL_ATTR, currentTail,
                LOGICAL_POSITION_ATTR, graph.getAllOperationsInLogicalOrder().indexOf(currentOperation));
            currentOperation.toXML(writer);
            writer.closeBlock();
        }
        writer.closeBlock();
    }

    private void write(BooleanRubette rubette, XMLWriter writer) {
        writer.empty(INPUTS, NUMBER_ATTR, rubette.getInCount());
        writer.empty(EXPRESSION, EXPR_ATTR, rubette.getExpression());
    }

    private void write(ConstructorRubette rubette, XMLWriter writer) {
        if (rubette.getForm() != null) {
            writer.writeFormRef(rubette.getForm());
        }
    }

    private void write(DisplayRubette rubette, XMLWriter writer) {
        writer.empty(XML, VALUE_ATTR, rubette.isXML() ? TRUE_VALUE : FALSE_VALUE);
    }

    private void write(LatchRubette rubette, XMLWriter writer) {
        writer.empty(OUTPUTS, NUMBER_ATTR, rubette.getOutCount());
    }

    private void write(ListRubette rubette, XMLWriter writer) {
        writer.empty(OPERATION, OP_ATTR, rubette.getOp());
        writer.empty(INPUTS, NUMBER_ATTR, rubette.getInCount());
    }

    private void write(MacroInputRubette rubette, XMLWriter writer) {
        writer.openBlock(LABELS, COUNT_ATTR, rubette.getOutCount());
        for (int i = 0; i < rubette.getOutCount(); i++) {
            writer.empty(LABEL, NAME_ATTR, Optional.ofNullable(rubette.getLabel(i)).map(String::trim).orElse(StringUtils.EMPTY));
        }
        writer.closeBlock();
    }

    private void write(MacroOutputRubette rubette, XMLWriter writer) {
        writer.openBlock(LABELS, COUNT_ATTR, rubette.getInCount());
        for (int i = 0; i < rubette.getInCount(); i++) {
            writer.empty(LABEL, NAME_ATTR, Optional.ofNullable(rubette.getLabel(i)).map(String::trim).orElse(StringUtils.EMPTY));
        }
        writer.closeBlock();
    }

    private void write(MacroRubette rubette, XMLWriter writer) {
        if (rubette.getInfo().length() > 0) {
            writer.openBlock(INFO);
            writer.writeTextNode("\n" + rubette.getInfo() + "\n");
            writer.closeBlock();
        }
        else {
            writer.empty(INFO);
        }
        if (rubette.getShortDescription().length() > 0) {
            writer.openBlock(SHORT);
            writer.writeTextNode("\n" + rubette.getShortDescription() + "\n");
            writer.closeBlock();
        } else {
            writer.empty(SHORT);
        }
        if (rubette.getLongDescription().length() > 0) {
            writer.openBlock(LONG);
            writer.writeTextNode("\n" + rubette.getLongDescription() + "\n");
            writer.closeBlock();
        } else {
            writer.empty(LONG);
        }
        writer.writeNetworkModel(rubette.getNetworkModel());
    }

    private void write(MidiFileInRubette rubette, XMLWriter writer) {
        writer.empty(FILE, NAME_ATTR, writer.toRelativePath(rubette.getMidiFileName()));
        writer.empty(TEMPO_FACTOR, VALUE_ATTR, rubette.getTempoFactor());
        writer.empty(STORE_DENOTATOR, VALUE_ATTR, rubette.isStoreDenotator() ? TRUE_VALUE : FALSE_VALUE);
        if (rubette.isStoreDenotator() && rubette.getScore() != null) {
            writer.writeDenotator(rubette.getScore());
        }
    }

    private void write(MidiFileOutRubette rubette, XMLWriter writer) {
        writer.empty("File", "name", writer.toRelativePath(rubette.getMidiFileName()));
    }

    private void write(ModuleMapRubette rubette, XMLWriter writer) {
        if (rubette.getInputForm() != null) {
            writer.writeFormRef(rubette.getInputForm());
            if (rubette.getPath() != null) {
                StringJoiner joiner = new StringJoiner(",");
                Arrays.stream(rubette.getPath()).mapToObj(Integer::toString).forEach(joiner::add);
                writer.empty(PATH, VALUE_ATTR, joiner.toString());
                if (rubette.getMorphism() != null) {
                    writer.writeModuleMorphism(rubette.getMorphism());
                }
            }
        }
    }

    private void write(MuxRubette rubette, XMLWriter writer) {
        writer.empty(INPUTS, NUMBER_ATTR, rubette.getInCount());
    }

    private void write(RealArithRubette rubette, XMLWriter writer) {
        writer.empty(INPUTS, NUMBER_ATTR, rubette.getInCount());
        writer.empty(EXPRESSION, RES_ATTR, rubette.isResultReal() ? 1 : 0, EXPR_ATTR, rubette.getExpressionString());
    }

    private void write(ReformRubette rubette, XMLWriter writer) {
        if (rubette.getOutputForm() != null) {
            writer.writeFormRef(rubette.getOutputForm());
        }
    }

    private void write(RegisterRubette rubette, XMLWriter writer) {
        writer.empty(NAME, VALUE_ATTR, Optional.ofNullable(rubette.getDenotatorName()).orElse(StringUtils.EMPTY));
    }

    private void write(ScaleRubette rubette, XMLWriter writer) {
        writer.openBlock(SCALE,
            NUMBER_OF_NOTES, rubette.getNumberOfNotes(),
            ROOT, rubette.getRoot(),
            PRESET, rubette.getPreset());
        if (rubette.getPreset() == 0) {
            for (double interval : rubette.getIntervals()) {
                writer.empty(INTERVAL, SEMITONES, interval);
            }
        }
        writer.closeBlock();
    }

    private void write(SchemeRubette rubette, XMLWriter writer) {
        writer.empty(INPUTS, NUMBER_ATTR, rubette.getInCount());
        writer.empty(OUTPUTS, NUMBER_ATTR, rubette.getOutCount());
        writer.openBlock(CODE);
        writer.writeTextNode(rubette.getSchemeCode().trim()+"\n");
        writer.closeBlock();
    }

    private void write(ScorePlayRubette rubette, XMLWriter writer) {
        StringJoiner joiner = new StringJoiner(",");
        Arrays.stream(rubette.getVoices()).mapToObj(Integer::toString).forEach(joiner::add);
        writer.empty(VOICES, MAP_ATTR, joiner.toString());
    }

    private void write(ScoreToCsoundRubette rubette, XMLWriter writer) {
        writer.empty(FILE, XMLConstants.NAME_ATTR, writer.toRelativePath(rubette.getScoFileName()));
        writer.empty(TIMESCALE, XMLConstants.VALUE_ATTR, rubette.getTimeScale());
        writer.empty(FREQ, XMLConstants.VALUE_ATTR, rubette.getA4freq());
        writer.openBlock(PROLOG);
        writer.writeTextNode(rubette.getProlog());
        writer.closeBlock();
    }

    private void write(Select2DRubette rubette, XMLWriter writer) {
        if (rubette.getForm() != null) {
            writer.writeFormRef(rubette.getForm());
            for (Select2DPanel panel : rubette.getSelections()) {
                writer.openBlock(SELECTION_PANEL);
                panel.toXML(writer);
                writer.closeBlock();
            }
        }
    }

    private void write(SelectFormRubette rubette, XMLWriter writer) {
        if (rubette.getOutputForm() != null) {
            writer.writeFormRef(rubette.getOutputForm());
        }
    }

    private void write(SetRubette rubette, XMLWriter writer) {
        writer.empty(OPERATION, OP_ATTR, rubette.getOp());
        writer.empty(INPUTS, NUMBER_ATTR, rubette.getInCount());
    }

    private void write(SimpleRubette rubette, XMLWriter writer) {
        if (rubette.getDenotator() != null) {
            writer.writeDenotator(rubette.getDenotator());
        }
    }

    private void write(SourceRubette rubette, XMLWriter writer) {
        writer.empty(REFRESHABLE, VALUE_ATTR, rubette.isRefreshable() ? TRUE_VALUE : FALSE_VALUE);
        if (rubette.getDenotator() != null) {
            writer.writeDenotator(rubette.getDenotator());
        }
    }

    private void write(SplitRubette rubette, XMLWriter writer) {
        if (rubette.getForm() != null) {
            writer.writeFormRef(rubette.getForm());
            if (rubette.getSelectedForms() != null) {
                for (int selectedForm : rubette.getSelectedForms()) {
                    writer.empty(SELECTED, VALUE_ATTR, selectedForm);
                }
            }
        }
    }

    private void write(StatRubette rubette, XMLWriter writer) {
        writer.empty(OPERATION, OP_ATTR, rubette.getOp());
        if (rubette.getForm() != null) {
            writer.writeFormRef(rubette.getForm());
        }
    }

    private void write(WallpaperRubette rubette, XMLWriter writer) {
        if (rubette.getInputForm() != null) {
            writer.writeFormRef(rubette.getInputForm());
            JWallpaperDimensionsTable morphismTable = rubette.getMorphismsTable();
            for (int i = 0; i < morphismTable.getMorphismCount(); i++) {
                ModuleMorphism currentMorphism = morphismTable.getMorphism(i);
                writer.openBlock(MORPHISM,
                    R_FROM, morphismTable.getRangeFrom(currentMorphism),
                    R_TO, morphismTable.getRangeTo(currentMorphism));
                writer.writeModuleMorphism(currentMorphism);
                List<List<Integer>> currentElementPaths = morphismTable.getCoordinates(currentMorphism);
                for (List<Integer> currentElementPath : currentElementPaths) {
                    writer.openBlock(ELEMENT_PATH);
                    for (Integer integer : currentElementPath) {
                        writer.empty(INT, VALUE_ATTR, integer);
                    }
                    writer.closeBlock();
                }
                writer.closeBlock();
            }
        }
    }

    private void write(SimpleAbstractRubette rubette, XMLWriter writer) {
        writer.writeRubetteProperties(rubette.getRubetteProperties());
    }

}
