/*
 * Copyright (C) 2004 Gérard Milmeister
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

package org.vetronauta.latrunculus.server.midi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.FactorDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MidiChange implements Comparable<MidiChange> {

    //TODO enum
    static final int NOTE_ON        = 0;
    static final int NOTE_OFF       = 1;
    static final int PROGRAM_CHANGE = 2;
    static final int TEMPO_CHANGE   = 3;
    static final int TIME_CHANGE    = 4;
    static final int CONTROL_CHANGE = 5;

    private int onset    = 0;
    private int pitch    = 0;
    private int loudness = 0;
    private int duration = 0;
    private int channel  = 0;
    private int track    = 0;
    private int type;
    
    public MidiChange(FactorDenotator note, int resolution) {
        double   o = ((SimpleDenotator)note.getFactor(0)).getReal();
        Rational p = ((SimpleDenotator)note.getFactor(1)).getRational();
        int      l = ((SimpleDenotator)note.getFactor(2)).getInteger();
        double   d = ((SimpleDenotator)note.getFactor(3)).getReal();
        int      v = ((SimpleDenotator)note.getFactor(4)).getInteger();

        onset    = (int)Math.round(o*resolution);
        pitch    = Math.max(Math.min(p.intValue(), 127), 0);
        loudness = Math.max(Math.min(l, 127), 0);
        duration = (int)Math.round(d*resolution);
        track    = 0;
        channel  = v % 16; 

        type = NOTE_ON;
    }
       

    public MidiChange(MidiChange note) {
        onset    = note.onset;
        pitch    = note.pitch;
        loudness = note.loudness;
        duration = note.duration;
        track    = note.track;
        channel  = note.channel;
        type     = note.type;
    }
    

    public MidiChange getNoteOff() {
        MidiChange newNote = new MidiChange(this);
        newNote.type  = NOTE_OFF;
        newNote.onset = onset+duration;
        return newNote;
    }


        public static MidiChange getPedalOn(FactorDenotator pedal, int resolution) {
        MidiChange newChange = new MidiChange();
        StringMap<ZInteger> v = (StringMap<ZInteger>) ((SimpleDenotator)pedal.getFactor(0)).getElement();
        double  o = ((SimpleDenotator)pedal.getFactor(1)).getReal();
        double  d = ((SimpleDenotator)pedal.getFactor(2)).getReal();

        newChange.onset = (int)Math.round(o*resolution);
        newChange.duration = (int)Math.round(d*resolution);
        newChange.track = v.getFactor("Track").intValue() - 1;
        newChange.channel = v.getFactor("Channel").intValue() - 1;
        newChange.loudness = 127; 
        newChange.pitch = 0x40;
        newChange.type = CONTROL_CHANGE;         

        return newChange;
    }
    
    
    public MidiChange getPedalOff() {
        MidiChange newNote = new MidiChange(this);
        newNote.onset = onset+duration;
        newNote.loudness = 0;
        return newNote;        
    }

    
    public void shiftOnset(int shift) {
        onset += shift;
    }
    
    
    public int getOnset() {
        return onset;
    }
    
    
    public int getPitch() {
        return pitch;
    }
    
    
    public int getLoudness() {
        return loudness;
    }
    
    
    public int getDuration() {
        return duration;
    }


    public int getType() {
        return type;
    }


    public int getChannel() {
        return channel;
    }


    public void setChannel(int c) {
        channel = c;
    }


    public int getTrack() {
        return track;
    }


    public void setTrack(int t) {
        track = t;
    }

    @Override
    public int compareTo(MidiChange note) {
        int c = onset-note.onset;
        if (c < 0) {
            return -1;
        }
        else if (c > 0) {
            return 1;
        }
        else if (type == CONTROL_CHANGE) {
            return -1;
        }
        else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(40);
        buf.append("MidiChange[");
        buf.append("onset=");
        buf.append(onset);
        buf.append(",pitch=");
        buf.append(pitch);
        buf.append(",loudness=");
        buf.append(loudness);
        buf.append(",duration=");
        buf.append(duration);
        buf.append(",channel=");
        buf.append(channel);
        buf.append(",track=");
        buf.append(track);
        buf.append(",type=");
        buf.append(type);
        buf.append("]");
        return buf.toString();
    }

}