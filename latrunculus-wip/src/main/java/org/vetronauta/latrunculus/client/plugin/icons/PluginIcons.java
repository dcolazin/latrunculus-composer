package org.vetronauta.latrunculus.client.plugin.icons;

import org.rubato.rubettes.builtin.BooleanRubette;
import org.rubato.rubettes.builtin.ConstructorRubette;
import org.rubato.rubettes.builtin.DisplayRubette;
import org.rubato.rubettes.builtin.LatchRubette;
import org.rubato.rubettes.builtin.MacroInputRubette;
import org.rubato.rubettes.builtin.MacroOutputRubette;
import org.rubato.rubettes.builtin.MacroRubette;
import org.rubato.rubettes.builtin.MuxRubette;
import org.rubato.rubettes.builtin.RealArithRubette;
import org.rubato.rubettes.builtin.ReformRubette;
import org.rubato.rubettes.builtin.RegisterRubette;
import org.rubato.rubettes.builtin.SchemeRubette;
import org.rubato.rubettes.builtin.SetRubette;
import org.rubato.rubettes.builtin.SimpleRubette;
import org.rubato.rubettes.builtin.SourceRubette;
import org.rubato.rubettes.builtin.SplitRubette;
import org.rubato.rubettes.builtin.StatRubette;
import org.rubato.rubettes.builtin.address.AddressEvalRubette;
import org.rubato.rubettes.image.ImageFileInRubette;
import org.rubato.rubettes.image.ImageFileOutRubette;
import org.rubato.rubettes.score.LilyPondOutRubette;
import org.rubato.rubettes.score.MelodyRubette;
import org.rubato.rubettes.score.MidiFileInRubette;
import org.rubato.rubettes.score.MidiFileOutRubette;
import org.rubato.rubettes.score.QuantizeRubette;
import org.rubato.rubettes.score.RhythmizeRubette;
import org.rubato.rubettes.score.ScaleRubette;
import org.rubato.rubettes.score.ScorePlayRubette;
import org.rubato.rubettes.score.ScoreToCsoundRubette;
import org.rubato.rubettes.select2d.Select2DRubette;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PluginIcons {

    private static final Map<Class<?>, ImageIcon> ICONS;

    static {
        ICONS = new HashMap<>();
        put(BooleanRubette.class, "/images/rubettes/builtin/logicicon.png");
        put(ConstructorRubette.class, "/images/rubettes/builtin/constricon.png");
        put(DisplayRubette.class, "/images/rubettes/builtin/displayicon.png");
        put(LatchRubette.class, "/images/rubettes/builtin/latchicon.png");
        put(MacroInputRubette.class, "/images/rubettes/builtin/inputicon.png");
        put(MacroOutputRubette.class, "/images/rubettes/builtin/outputicon.png");
        put(MacroRubette.class, "/images/rubettes/builtin/neticon.png");
        put(MuxRubette.class, "/images/rubettes/builtin/muxicon.png");
        put(RealArithRubette.class, "/images/rubettes/builtin/arithicon.png");
        put(ReformRubette.class, "/images/rubettes/builtin/reformicon.png");
        put(RegisterRubette.class, "/images/rubettes/builtin/registericon.png");
        put(SchemeRubette.class, "/images/rubettes/builtin/schemeicon.png");
        put(SetRubette.class, "/images/rubettes/builtin/seticon.png");
        put(SimpleRubette.class, "/images/rubettes/builtin/simpleicon.png");
        put(SourceRubette.class, "/images/rubettes/builtin/sourceicon.png");
        put(SplitRubette.class, "/images/rubettes/builtin/spliticon.png");
        put(StatRubette.class, "/images/rubettes/builtin/staticon.png");
        put(AddressEvalRubette.class, "/images/rubettes/builtin/address/addressicon.png");
        put(ImageFileInRubette.class, "/images/rubettes/image/imagefileinicon.png");
        put(ImageFileOutRubette.class, "/images/rubettes/image/imagefileouticon.png");
        put(LilyPondOutRubette.class, "/images/rubettes/score/lilypondouticon.png");
        put(MelodyRubette.class, "/images/rubettes/score/melodyicon.png");
        put(MidiFileInRubette.class, "/images/composer/icons/midiin.png");
        put(MidiFileOutRubette.class, "/images/composer/icons/midiout.png");
        put(QuantizeRubette.class, "/images/rubettes/score/quantizeicon.png");
        put(RhythmizeRubette.class, "/images/rubettes/score/rhythmizeicon.png");
        put(ScaleRubette.class, "/images/rubettes/score/scaleicon.png");
        put(ScorePlayRubette.class, "/images/rubettes/score/scoreplayicon.png");
        put(ScoreToCsoundRubette.class, "/images/rubettes/score/csound.png");
        put(Select2DRubette.class, "/images/rubettes/select2d/select2dicon.png");
    }

    public static ImageIcon get(Object o) {
        return ICONS.get(o.getClass());
    }

    private static void put(Class<?> cls, String name) {
        ICONS.put(cls, Icons.loadIcon(cls, name));
    }

}
