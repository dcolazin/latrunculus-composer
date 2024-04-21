package org.rubato.rubettes.texture;

import org.vetronauta.latrunculus.client.properties.DoubleProperty;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.plugin.base.SimpleAbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;
import org.rubato.rubettes.util.CoolFormRegistrant;
import org.rubato.rubettes.util.ObjectGenerator;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;

import java.util.Map;
import java.util.TreeMap;

/**
 * Creates a Texture based on the rate of occurrence of each pitch in a given Score
 * @author Florian Thalmann, April 14 2014
 */
public class TexturalizeRubette extends SimpleAbstractRubette {

	public TexturalizeRubette() {
		this.setInCount(1);
		this.setOutCount(1);
		this.putProperty(new DoubleProperty("rateFactor", "Rate factor", .1, .001, 1000));
	}
	
	@Override
	public void run(RunInfo runInfo) {
		PowerDenotator score = (PowerDenotator)this.getInput(0);
		Map<Double,Integer> occurrences = new TreeMap<>();
		Map<Double,Double> avgLoudnesses = new TreeMap<>();
		Map<Double,Double> avgDurations = new TreeMap<>();
		double maxOnset = 0;
		double maxDuration = 0;
		double rateFactor = ((DoubleProperty)this.getProperty("rateFactor")).getDouble();
		
		try {
			for (Denotator currentNote : score.getFactors()) {
				double currentOnset = ((Real)currentNote.getElement(new int[]{0,0})).getValue();
				double currentPitch = ((Rational)currentNote.getElement(new int[]{1,0})).doubleValue();
				double currentLoudness = ((ZInteger)currentNote.getElement(new int[]{2,0})).doubleValue();
				double currentDuration = ((Real)currentNote.getElement(new int[]{3,0})).getValue();
				
				maxOnset = Math.max(maxOnset, currentOnset);
				maxDuration = Math.max(maxDuration, currentDuration);
				
				if (occurrences.get(currentPitch) != null) {
					int previousOccurrences = occurrences.get(currentPitch);
					occurrences.put(currentPitch, previousOccurrences+1);
					double newAvgLoudness = ((avgLoudnesses.get(currentPitch)*previousOccurrences)+currentLoudness)
												/(previousOccurrences+1);
					avgLoudnesses.put(currentPitch, newAvgLoudness);
					double newAvgDuration = ((avgDurations.get(currentPitch)*previousOccurrences)+currentDuration)
												/(previousOccurrences+1);
					avgDurations.put(currentPitch, newAvgDuration);
				} else {
					occurrences.put(currentPitch, 1);
					avgLoudnesses.put(currentPitch, currentLoudness);
					avgDurations.put(currentPitch, currentDuration);
				}
			}
		
			ObjectGenerator generator = new ObjectGenerator();
			PowerDenotator texture = (PowerDenotator)generator.createStandardDenotator(CoolFormRegistrant.TEXTURE_FORM, new double[]{});
			
			for (double currentPitch : occurrences.keySet()) {
				double rate = (maxOnset+maxDuration)/occurrences.get(currentPitch)*rateFactor;
				Denotator currentRepeatedNote = generator.createStandardDenotator(CoolFormRegistrant.REPEATED_NOTE_FORM,
						new double[]{currentPitch, avgLoudnesses.get(currentPitch), rate, avgDurations.get(currentPitch)});
				texture.appendFactor(currentRepeatedNote);
			}
			
			this.setOutput(0, texture);
		} catch (LatrunculusCheckedException e) {
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public String getName() {
		return "Texturalize";
	}
	
	@Override
	public String getGroup() {
        return "Other";
    }
    
	@Override
    public String getShortDescription() {
        return "Outputs a Texture based on a Score";
    }

	@Override
    public String getLongDescription() {
        return "The Texturalize Rubette generates a Texture based on the rate of occurrence"+
               " of each pitch in a given Score";
    }
    
	@Override
    public String getInTip(int i) {
        return "Input Score denotator";
    }

	@Override
    public String getOutTip(int i) {
        return "Output Texture denotator";
    }

}
