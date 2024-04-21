/*
 * Copyright (C) 2013 Florian Thalmann
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

package org.vetronauta.latrunculus.client.properties;

import org.rubato.composer.components.JSelectFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileProperty extends RubetteProperty implements ActionListener {
	
	private File value;
	private File tmpValue;
	private String[] allowedExtensions;
	private JSelectFile fileSelector;
	private boolean saving;
	
    
    public FileProperty(String key, String name, String[] allowedExtensions, boolean saving) {
        super(key, name);
        this.allowedExtensions = allowedExtensions;
        this.saving = saving;
    }
    
    
    public FileProperty(FileProperty property) {
        super(property);
        this.allowedExtensions = property.allowedExtensions;
        this.value = property.value;
    }
    
    
    public Object getValue() {
        return value;
    }
    
    
    public void setValue(Object value) {
        if (value instanceof File) {
            this.setFile((File)value);
        }
    }
    
    
    public File getFile() {
        return value; 
    }
    
    
    public void setFile(File value) {
        this.value = value;
        this.tmpValue = value;
    }
    
    
    public JComponent getJComponent() {
        this.fileSelector = new JSelectFile(this.allowedExtensions, this.saving);
        this.fileSelector.disableBorder();
        this.fileSelector.addActionListener(this);
        this.fileSelector.setFile(this.value);
        return this.fileSelector;
    }

    
    public void actionPerformed(ActionEvent e) {
        this.tmpValue = this.fileSelector.getFile();
    }
    
    
    public void apply() {
        this.setFile(this.tmpValue);
    }
    
    
    public void revert() {
        this.tmpValue = value;
        this.fileSelector.setFile(this.value);
    }
    
    @Override
    public FileProperty deepCopy() {
        return new FileProperty(this);
    }

    public String toString() {
        return "FileProperty["+getOrder()+","+getKey()+","+getName()+","+value+"]";
    }

}
