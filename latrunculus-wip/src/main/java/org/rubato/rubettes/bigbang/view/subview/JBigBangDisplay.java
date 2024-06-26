package org.rubato.rubettes.bigbang.view.subview;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.rubato.rubettes.bigbang.controller.BigBangController;
import org.rubato.rubettes.bigbang.view.View;
import org.rubato.rubettes.bigbang.view.controller.ViewController;
import org.rubato.rubettes.bigbang.view.controller.mode.DisplayModeAdapter;
import org.rubato.rubettes.bigbang.view.model.ViewParameters;
import org.rubato.rubettes.bigbang.view.model.tools.DisplayTool;
import org.rubato.rubettes.util.PerformanceCheck;
import org.rubato.rubettes.util.Point;

public class JBigBangDisplay extends JPanel implements View {
	
	public static final int DISPLAY_WIDTH = 1020;
	
	private DisplayContents contents;
	private DisplayModeAdapter modeAdapter;
	private Timer timer;
	
	public JBigBangDisplay(BigBangController bbController, ViewController controller) {
		controller.addView(this);
		this.contents = new DisplayContents();
		JBigBangPopupMenu popup = new JBigBangPopupMenu(bbController, controller);
		this.setComponentPopupMenu(popup);
	}
	
	private void setModeAdapter(DisplayModeAdapter adapter) {
		if (this.modeAdapter != null) {
			this.modeAdapter.removeFrom(this);
		}
		this.modeAdapter = adapter;
		this.modeAdapter.addTo(this);
	}
	
	public DisplayContents getContents() {
		return this.contents;
	}
	
	public void toggleTimedRepaint() {
		if (this.timer != null) {
			this.timer.stop();
			this.timer = null;
			this.repaint();
		} else {
			this.timer = new Timer(250, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			    	repaint();
				}
			});
			this.timer.start();
		}
	}
	
	@Override
	public void update(Graphics g) {
		this.paint(g);
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //this.setOpaque(true);
        //this.setBackground(BLACK);
        this.contents.paint(new AWTPainter(g), this.getWidth(), this.getHeight());
    }
	
	public Dimension getPreferredSize() {
        return new Dimension(JBigBangDisplay.DISPLAY_WIDTH, JBigBangPanel.CENTER_PANEL_HEIGHT);
    }

	public void modelPropertyChange(PropertyChangeEvent event) {
		String propertyName = event.getPropertyName();
		if (propertyName.equals(ViewController.DISPLAY_MODE)) {
			this.setModeAdapter((DisplayModeAdapter)event.getNewValue());
			this.repaint();
		} else if (propertyName.equals(ViewController.FACTS_VIEW_CONTENTS)) {
			this.contents = (DisplayContents)event.getNewValue();
			this.repaint();
		} 
		
		
		
		else if (propertyName.equals(ViewController.VIEW_PARAMETERS)) {
			this.contents.setViewParameters((ViewParameters)event.getNewValue());
			this.repaint();
		} else if (propertyName.equals(ViewController.DISPLAY_OBJECTS)) {
			this.contents.setObjects((DisplayObjects)event.getNewValue());
			//System.out.println(((DisplayObjects)event.getNewValue()).size());
			this.repaint();
			PerformanceCheck.startTask("done");
			//PerformanceCheck.print();
			//System.out.println("\n\n");
			PerformanceCheck.reset();
		} else if (propertyName.equals(ViewController.CENTER_VIEW)) {
			this.contents.centerView();
			this.repaint();
		} else if (propertyName.equals(ViewController.DISPLAY_TOOL)) {
			this.contents.setTool((DisplayTool)event.getNewValue());
			this.repaint();
		} else if (propertyName.equals(ViewController.ZOOM_FACTORS)) {
			this.contents.setZoomFactors((double[])event.getNewValue());
			this.repaint();
		} else if (propertyName.equals(ViewController.DISPLAY_POSITION)) {
			this.contents.setPosition((Point)event.getNewValue());
			this.repaint();
		} else if (propertyName.equals(ViewController.SELECTED_VIEW_PARAMETERS)) {
			this.contents.updateNoteBounds();
			this.repaint();
		} else if (propertyName.equals(ViewController.OBJECT_SELECTION)) {
			this.repaint();
		} else if (propertyName.equals(ViewController.ANCHOR_OBJECT_SELECTION)) {
			this.repaint();
		} else if (propertyName.equals(ViewController.SATELLITES_CONNECTED)) {
			this.contents.setSatellitesConnected((Boolean)event.getNewValue());
			this.repaint();
		} else if (propertyName.equals(ViewController.LAYERS)) {
			this.repaint();
		}
	}

}
