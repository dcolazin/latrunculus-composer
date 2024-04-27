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

package org.vetronauta.latrunculus.client.plugin.properties;

import org.rubato.composer.components.JSelectFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileClientProperty extends ClientPluginProperty<File> implements ActionListener {

	private final String[] allowedExtensions;
	private JSelectFile fileSelector;
	private boolean saving;

    public FileClientProperty(String key, String name, String[] allowedExtensions, boolean saving) {
        super(key, name, null);
        this.allowedExtensions = allowedExtensions;
        this.saving = saving;
    }
    
    public FileClientProperty(FileClientProperty property) {
        super(property.pluginProperty);
        this.allowedExtensions = property.allowedExtensions;
    }

    public FileClientProperty(String key, String name, File value) {
        super(key, name, value);
        allowedExtensions = null;
    }

    @Override
    public JComponent getJComponent() {
        this.fileSelector = new JSelectFile(this.allowedExtensions, this.saving);
        this.fileSelector.disableBorder();
        this.fileSelector.addActionListener(this);
        this.fileSelector.setFile(pluginProperty.getValue());
        return this.fileSelector;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pluginProperty.setTmpValue(fileSelector.getFile());
    }

    @Override
    public void revert() {
        pluginProperty.revert(fileSelector::setFile);
    }
    
    @Override
    public FileClientProperty deepCopy() {
        return new FileClientProperty(this);
    }

}
