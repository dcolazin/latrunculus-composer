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

package org.rubato.rubettes.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.vetronauta.latrunculus.client.plugin.properties.BooleanClientProperty;
import org.vetronauta.latrunculus.client.plugin.properties.FileClientProperty;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.plugin.base.RunInfo;
import org.vetronauta.latrunculus.client.plugin.icons.Icons;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.rubato.rubettes.util.ObjectGenerator;
import org.vetronauta.latrunculus.plugin.base.SimpleAbstractRubette;

/**
 * Imports any GIF, PNG, JPEG, BMP, or WBMP image file and converts it to an image denotator 
 * 
 * @author Florian Thalmann
 */
public class ImageFileInRubette extends SimpleAbstractRubette {
	
	private static Form PIXEL_FORM = Repository.systemRepository().getForm("Pixel");
	private static PowerForm IMAGE_FORM = (PowerForm)Repository.systemRepository().getForm("Image");
	private static Form VS_PIXEL_FORM = Repository.systemRepository().getForm("VariableSizePixel");
	private static PowerForm VS_IMAGE_FORM = (PowerForm) Repository.systemRepository().getForm("VSPixelImage");
	
	private File imageFile;
	private boolean variableSizePixels;
	private final String imageFileKey = "imageFile";
	private final String variableSizePixelsKey = "variableSizePixels";
	private ObjectGenerator objectGenerator;

	/**
	 * Creates a basic ImageFileInRubette.
	 */
	public ImageFileInRubette() {
        this.setInCount(0);
        this.setOutCount(1);
        this.objectGenerator = new ObjectGenerator();
        String[] allowedExtensions = new String[]{".gif", ".png", ".jpg", ".bmp"};
        this.putProperty(new FileClientProperty(this.imageFileKey, "Image file", allowedExtensions, false));
        this.putProperty(new BooleanClientProperty(this.variableSizePixelsKey, "Variable size pixels", false));
    }

    public void run(RunInfo runInfo) {
    	if (this.imageFile == null) {
            this.addError("No file has been set.");
    	} else {
    		this.setOutput(0, this.getConvertedImage(runInfo));
    	}
    }
    
    private Denotator getConvertedImage(RunInfo runInfo) {
    	BufferedImage image = this.readImageFile();
    	List<Denotator> pixels = new ArrayList<Denotator>();
    	for (int x = 0; x < image.getWidth(); x++) {
    		for (int y = 0; y < image.getHeight(); y++) {
    			int rgb = image.getRGB(x, y);
    			if (rgb != 0) {
    				Color currentColor = new Color(image.getRGB(x, y), true);
    				int red = currentColor.getRed();
    				int green = currentColor.getGreen();
    				int blue = currentColor.getBlue();
    				int alpha = currentColor.getAlpha();
    				if (this.variableSizePixels) {
    					pixels.add(this.objectGenerator.createStandardDenotator(ImageFileInRubette.VS_PIXEL_FORM, x, image.getHeight()-1-y, 1, 1, red, green, blue, alpha));
    				} else {
    					pixels.add(this.objectGenerator.createStandardDenotator(ImageFileInRubette.PIXEL_FORM, x, image.getHeight()-1-y, red, green, blue, alpha));
    				}
    			}
    			if (runInfo.stopped()) {
    				return null;
    			}
    		}
    	}
    	try {
    		if (this.variableSizePixels) {
    			return new PowerDenotator(NameDenotator.make(""), ImageFileInRubette.VS_IMAGE_FORM, pixels);
    		}
    		return new PowerDenotator(NameDenotator.make(""), ImageFileInRubette.IMAGE_FORM, pixels);
    	} catch (LatrunculusCheckedException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    private BufferedImage readImageFile() {
    	try {
    	    return ImageIO.read(this.imageFile);
    	} catch (IOException e) {
    		addError("File %%1 could not be read.", this.imageFile.getName());
    	}
    	return null;
    }
	
	public JComponent getProperties() {
		return super.getProperties();
	}
	
	//has to be overriden so that the info sets!!
	@Override
	public boolean applyProperties() {
        super.applyProperties();
        this.imageFile = ((FileClientProperty)this.getProperty(this.imageFileKey)).getValue();
        this.variableSizePixels = ((BooleanClientProperty)this.getProperty(this.variableSizePixelsKey)).getValue();
        return true;
    }
	
	public boolean hasInfo() {
        return true;
    }
	
	public String getInfo() {
        if (this.imageFile != null) {
        	return this.imageFile.getName();
        }
        return "File not set";
    }    
    
    public String getGroup() {
        return "Image";
    }

    public String getName() {
        return "ImageFileIn";
    }
    
    public String getShortDescription() {
        return "Outputs an image denotator";
    }

    public String getLongDescription() {
        return "The ImageFileIn rubette reads an image file and outputs it as an image denotator";
    }

    public String getOutTip(int i) {
        return "Output image denotator";
    }
    
}
