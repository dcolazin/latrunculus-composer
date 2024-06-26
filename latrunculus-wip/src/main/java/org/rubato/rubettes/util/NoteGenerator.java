/*
 * Copyright (C) 2006 Florian Thalmann
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

package org.rubato.rubettes.util;

import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for generating Note and Score denotators with ease. The following possibilities
 * are provided for parallel use:
 * - melody generation from a pitch list
 * - iterative melody generation
 * - single note generation
 * 
 * @author Florian Thalmann
 */
public abstract class NoteGenerator {
	
	protected PowerForm soundScoreForm = (PowerForm) Repository.systemRepository().getForm("SoundScore");
	protected PowerForm macroScoreForm = (PowerForm) Repository.systemRepository().getForm("MacroScore");
	protected SimpleForm layerForm = (SimpleForm) Repository.systemRepository().getForm("Layer");
	protected PowerForm scoreForm = (PowerForm) Repository.systemRepository().getForm("Score");
	protected SimpleForm onsetForm = (SimpleForm) Repository.systemRepository().getForm("Onset");
	protected SimpleForm pitchForm = (SimpleForm) Repository.systemRepository().getForm("Pitch");
	protected SimpleForm loudnessForm = (SimpleForm) Repository.systemRepository().getForm("Loudness");
	protected SimpleForm durationForm = (SimpleForm) Repository.systemRepository().getForm("Duration");
	protected SimpleForm voiceForm = (SimpleForm) Repository.systemRepository().getForm("Voice");
	
	protected NameDenotator emptyName = NameDenotator.make("");
	
	private final List<int[]> noteModulePaths;
	
	private List<Denotator> currentMelody;
	private double noteDistance;
	private double currentOnset;
	
	protected NoteGenerator() {
		this.noteModulePaths = new ArrayList<>();
		this.noteModulePaths.add(new int[]{0,0});
		this.noteModulePaths.add(new int[]{1,0});
		this.noteModulePaths.add(new int[]{2,0});
		this.noteModulePaths.add(new int[]{3,0});
		this.noteModulePaths.add(new int[]{4,0});
		this.noteModulePaths.add(new int[]{5,0});
	}
	
	public abstract Denotator convertScore(Denotator input);
	
	public abstract PowerDenotator createEmptyScore();
	
	/**
	 * Converts a hierarchical score to the specific type represented by this class
	 * e.g. MacroScore to SoundScore or vice versa 
	 * @param hierarchicalScore
	 * @return
	 */
	protected PowerDenotator convertToSpecificScore(PowerDenotator hierarchicalScore) {
		PowerDenotator newSpecificScore = this.createEmptyScore();
		try {
			List<Denotator> factors = hierarchicalScore.getFactors();
			for (Denotator factor : factors) {
				LimitDenotator currentNode = (LimitDenotator) factor;
				Denotator currentNote = this.createNoteDenotator(currentNode.getFactor(0));
				PowerDenotator currentSubScore = this.convertToSpecificScore((PowerDenotator) currentNode.getFactor(1));
				newSpecificScore.appendFactor(this.createNodeDenotator(currentNote, currentSubScore));
			}
		} catch (LatrunculusCheckedException e) { e.printStackTrace(); }
		return newSpecificScore;
	}
	
	protected PowerDenotator moveToLayer(PowerDenotator macroScore, int layerIndex) {
		PowerDenotator newMacroScore = this.createEmptyScore();
		try {
			List<Denotator> factors = macroScore.getFactors();
			for (Denotator factor : factors) {
				LimitDenotator currentNode = (LimitDenotator) factor;
				PowerDenotator currentMacroScore = this.moveToLayer((PowerDenotator) currentNode.getFactor(1), layerIndex);
				currentNode.setFactor(1, currentMacroScore);
				newMacroScore.appendFactor(this.copyAndSetLayer(currentNode, layerIndex));
			}
		} catch (LatrunculusCheckedException e) { e.printStackTrace(); }
		return newMacroScore;
	}
	
	/**
	 * Returns a Score denotator containing a note for every specified pitch. The first note
	 * has onset 0.
	 * 
	 * @param noteDistance - the distance between subsequent notes
	 * @param pitches - an array of pitch values
	 */
	public PowerDenotator createSimpleMelody(double noteDistance, Double[] pitches) {
		this.startNewMelody(noteDistance);
		for (Double pitch : pitches) {
			this.addNoteToMelody(pitch);
		}
		return this.createScoreWithMelody();
	}
	
	/**
	 * Returns a Score denotator containing a note for every specified pitch. The first note
	 * has onset 0.
	 * 
	 * @param noteDistance - the distance between subsequent notes
	 * @param pitches - the pitch values
	 */
	public PowerDenotator createSimpleMelody(double noteDistance, double... pitches) {
		this.startNewMelody(noteDistance);
		for (double pitch : pitches) {
			this.addNoteToMelody(pitch);
		}
		return this.createScoreWithMelody();
	}
	
	/**
	 * Starts a new iterative melody with the specified note distance.
	 * 
	 * @param noteDistance - the distance between subsequent notes
	 */
	public void startNewMelody(double noteDistance) {
		this.currentMelody = new ArrayList<>();
		this.noteDistance = noteDistance;
		this.currentOnset = 0;
	}
	
	/**
	 * Adds a note with the specified pitch to the iterative melody.
	 * 
	 * @param pitch - the pitch of the new note
	 */
	public void addNoteToMelody(double pitch) {
		Denotator note = this.createNoteDenotator(this.currentOnset, pitch, 120, 1, 0);
		this.currentMelody.add(note);
		this.currentOnset += this.noteDistance;
	}
	
	/**
	 * Returns the current iteratively generated melody.
	 */
	public PowerDenotator createScoreWithMelody() {
		try {
			return new PowerDenotator(this.emptyName, this.scoreForm, this.currentMelody);
		} catch (LatrunculusCheckedException e) { return null; }
	}
	
	/**
	 * Returns a new Note denotator with the specified parameters.
	 * 
	 * @param values
	 */
	public LimitDenotator createNoteDenotator(double[] values) {
		return this.createNoteDenotator(values[0], values[1], (int)Math.round(values[2]), values[3], (int)Math.round(values[4]));
	}
	
	/**
	 * Returns a new Note denotator with the specified parameters.
	 * 
	 * @param onset
	 * @param pitch
	 * @param loudness
	 * @param duration
	 */
	public LimitDenotator createNoteDenotator(double onset, double pitch, int loudness, double duration, int voice) {
		List<ModuleElement> elements = new ArrayList<>();
		elements.add(new Real((onset)));
		elements.add(new Rational(pitch));
		elements.add(new ZInteger(loudness));
		elements.add(new Real((duration)));
		elements.add(new ZInteger(voice));
		return this.createNoteDenotator(elements);
	}
	
	/**
	 * Create a note denotator with the elements of another (good for conversion between types)
	 */
	private LimitDenotator createNoteDenotator(Denotator noteDenotator) {
		try {
			List<ModuleElement> elements = new ArrayList<>();
			//take the elements from all coordinates from Onset to Voice
			for (int i = 0; i < 6; i++) {
				elements.add(noteDenotator.getElement(new int[]{i, 0}));
			}
			return this.createNoteDenotator(elements);
		} catch (LatrunculusCheckedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private LimitDenotator createNoteDenotator(List<ModuleElement> elements) {
		try {
			List<Denotator> coordinates = new ArrayList<>();
			coordinates.add(this.createSimpleDenotator(this.onsetForm, elements.get(0)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.pitchForm, elements.get(1)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.loudnessForm, elements.get(2)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.durationForm, elements.get(3)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.voiceForm, elements.get(4)));
			
			//this takes a lot of time compared to the other operations
			return this.createSpecificNoteDenotator(coordinates);
		} catch (DomainException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private LimitDenotator createNoteDenotator(List<ModuleElement> elements, PowerDenotator modulators) {
		try {
			List<Denotator> coordinates = new ArrayList<>();
			coordinates.add(this.createSimpleDenotator(this.onsetForm, elements.get(0)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.pitchForm, elements.get(1)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.loudnessForm, elements.get(2)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.durationForm, elements.get(3)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.voiceForm, elements.get(4)));
			coordinates.add(modulators);
			
			//this takes a lot of time compared to the other operations
			return this.createSpecificNoteDenotator(coordinates);
		} catch (DomainException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected abstract LimitDenotator createSpecificNoteDenotator(List<Denotator> coordinates);
	
	public LimitDenotator makeNoteRelative(LimitDenotator note, LimitDenotator referenceNote) {
		if (this.isNode(referenceNote)) {
			referenceNote = (LimitDenotator)referenceNote.getFactor(0);
		}
		if (this.isNode(note)) {
			return this.createRelativeNode(note, referenceNote);
		}
		return this.createRelativeNote(note, referenceNote);
	}
	
	private boolean isNode(LimitDenotator note) {
		return note.getForm().getNameString().equals("SoundNode");
	}
	
	private LimitDenotator createRelativeNode(LimitDenotator node, LimitDenotator referenceNote) {
		LimitDenotator relativeNote = this.createRelativeNote((LimitDenotator)node.getFactor(0), referenceNote);
		return this.createNodeDenotator(relativeNote, node.getFactor(1));
	}
	
	private LimitDenotator createRelativeNote(LimitDenotator note, LimitDenotator referenceNote) {
		List<ModuleElement> newElements = new ArrayList<>();
		PowerDenotator modulators = null;
		try {
			for (int[] currentPath: this.noteModulePaths) {
				newElements.add(note.getElement(currentPath).difference(referenceNote.getElement(currentPath)));
			}
			modulators = (PowerDenotator)note.get(new int[]{6});
		} catch (LatrunculusCheckedException e) { e.printStackTrace(); }
		return this.createNoteDenotator(newElements, modulators);
	}
	
	public LimitDenotator makeNoteAbsolute(LimitDenotator note, LimitDenotator referenceNote) {
		if (this.isNode(referenceNote)) {
			referenceNote = (LimitDenotator)referenceNote.getFactor(0);
		}
		if (this.isNode(note)) {
			return this.createAbsoluteNode(note, referenceNote);
		}
		return this.createAbsoluteNote(note, referenceNote);
	}
	
	private LimitDenotator createAbsoluteNode(LimitDenotator node, LimitDenotator referenceNote) {
		LimitDenotator relativeNote = this.createAbsoluteNote((LimitDenotator)node.getFactor(0), referenceNote);
		return this.createNodeDenotator(relativeNote, node.getFactor(1));
	}
	
	public LimitDenotator createAbsoluteNote(LimitDenotator note, LimitDenotator referenceNote) {
		List<ModuleElement> newElements = new ArrayList<>();
		PowerDenotator modulators = null;
		try {
			for (int[] currentPath: this.noteModulePaths) {
				newElements.add(note.getElement(currentPath).sum(referenceNote.getElement(currentPath)));
			}
			modulators = (PowerDenotator)note.get(new int[]{6});
		} catch (LatrunculusCheckedException e) { e.printStackTrace(); }
		return this.createNoteDenotator(newElements, modulators);
	}
	
	public LimitDenotator createNodeDenotator(double[] noteValues) {
		return this.createNodeDenotator(this.createNoteDenotator(noteValues));
	}
	
	public LimitDenotator createNodeDenotators(Denotator note, Denotator macroScore) {
		return this.createNodeDenotator(note, macroScore);
	}
	
	public LimitDenotator createNodeDenotator(Denotator note) {
		return this.createNodeDenotator(note, this.createEmptyScore());
	}
	
	public LimitDenotator createNodeDenotator(Denotator note, Denotator macroScore) {
		try {
			List<Denotator> coordinates = new ArrayList<>();
			coordinates.add(note);
			coordinates.add(macroScore);
			return this.createSpecificNodeDenotator(coordinates);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected abstract LimitDenotator createSpecificNodeDenotator(List<Denotator> coordinates);
	
	public LimitDenotator copyAndSetLayer(LimitDenotator note, int layerIndex) {
		if (this.isNode(note)) {
			return this.copyNodeAndSetLayer(note, layerIndex);
		}
		return this.copyNoteAndSetLayer(note, layerIndex);
	}
		
	private LimitDenotator copyNodeAndSetLayer(LimitDenotator node, int layerIndex) {
		Denotator note = this.copyNoteAndSetLayer((LimitDenotator)node.getFactor(0).deepCopy(), layerIndex);
		Denotator satellites = node.getFactor(1).deepCopy();
		return this.createNodeDenotator(note, satellites);
	}
	
	private LimitDenotator copyNoteAndSetLayer(LimitDenotator note, int layerIndex) {
		note = note.deepCopy();
		Denotator layer = this.createSimpleDenotator(this.layerForm, new ZInteger(layerIndex));
		try { note.setFactor(2, layer); } catch (LatrunculusCheckedException e) { }
		return note;
	}
	
	public int getLayer(LimitDenotator node) {
		//TODO:NEW: LAYER IN NOTE!!!!!!!
		try {
			return ((ZInteger)node.getElement(new int[]{2,0})).intValue();
		} catch (LatrunculusCheckedException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Modifies the elements of the coordinates of a note denotator with the specified new values.
	 *
	 * @param note - the note, the values of which have to be replaced
	 * @param onset - the new onset
	 * @param loudness - the new loudness
	 * @param duration - the new duration
	 * @throws LatrunculusCheckedException
	 */
	public void modifyNoteDenotator(LimitDenotator note, double onset, int loudness, double duration) throws LatrunculusCheckedException {
		this.modifyNoteDenotator(note, onset, duration);
		Denotator loudnessDenotator = this.createSimpleDenotator(this.loudnessForm, new ZInteger(loudness));
		note.setFactor(2, loudnessDenotator);
	}
	
	/**
	 * Modifies the elements of the coordinates of a note denotator with the specified new values.
	 * 
	 * @param note - the note, the values of which have to be replaced
	 * @param onset - the new onset
	 * @param duration - the new duration
	 * @throws LatrunculusCheckedException
	 */
	public void modifyNoteDenotator(LimitDenotator note, double onset, double duration) throws LatrunculusCheckedException {
		Denotator onsetDenotator = this.createSimpleDenotator(this.onsetForm, new Real((onset)));
		Denotator durationDenotator = this.createSimpleDenotator(this.durationForm, new Real((duration)));
		
		note.setFactor(0, onsetDenotator);
		note.setFactor(3, durationDenotator);
	}
	
	public void modifyNoteDenotator(LimitDenotator note, int[] elementPath, double value) {
		try {
			ModuleElement newElement = note.getElement(elementPath).getModule().cast(new Real((value)));
			SimpleForm form = (SimpleForm)note.getFactor(elementPath[0]).getForm();
			Denotator newCoordinate = this.createSimpleDenotator(form, newElement);
			note.setFactor(elementPath[0], newCoordinate);
		} catch (LatrunculusCheckedException e) {
			e.printStackTrace();
		}
	}
	
	public Double getDoubleValue(Denotator note, int[] elementPath) {
		try {
			return RRing.ring.cast(note.getElement(elementPath)).getValue();
		} catch (LatrunculusCheckedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected void setLayerToVoice(LimitDenotator note) throws LatrunculusCheckedException {
		int[] voicePath = new int[]{4,0};
		ZInteger voiceElement = (ZInteger)note.getElement(voicePath).deepCopy();
		Denotator currentLayer = this.createSimpleDenotator(this.layerForm, voiceElement);
		note.setFactor(5, currentLayer);
	}
	
	/*
	 * Returns a new denotator of the specified form containing the specified element.
	 */
	protected Denotator createSimpleDenotator(SimpleForm form, ModuleElement element) {
		try {
			return new SimpleDenotator(this.emptyName, form, element);
		} catch(DomainException e) { return null; }
	}
	
	/**
	 * Returns the Score form from the system repository.
	 */
	public PowerForm getScoreForm() { return this.scoreForm; }
	
	/**
	 * Returns the Onset form from the system repository.
	 */
	public SimpleForm getOnsetForm() { return this.onsetForm; }
	
	/**
	 * Returns the Pitch form from the system repository.
	 */
	public SimpleForm getPitchForm() { return this.pitchForm; }
	
	/**
	 * Returns the Loudness form from the system repository.
	 */
	public SimpleForm getLoudnessForm() { return this.loudnessForm; }
	
	/**
	 * Returns the Duration form from the system repository.
	 */
	public SimpleForm getDurationForm() { return this.durationForm; }
	
}
