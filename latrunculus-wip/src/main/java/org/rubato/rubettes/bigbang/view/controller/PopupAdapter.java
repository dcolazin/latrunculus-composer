package org.rubato.rubettes.bigbang.view.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.rubato.rubettes.bigbang.view.subview.JBigBangDisplay;
import org.rubato.rubettes.bigbang.view.subview.JBigBangPopupMenu;
import org.rubato.rubettes.util.Point2D;

public class PopupAdapter extends MouseAdapter {
	
	private JBigBangPopupMenu popup;
	
	public PopupAdapter(JBigBangPopupMenu popup) {
		this.popup = popup;
	}

	public void mousePressed(MouseEvent event) {
        this.maybeShowPopup(event);
    }

    public void mouseReleased(MouseEvent event) {
        this.maybeShowPopup(event);
    }

    private void maybeShowPopup(MouseEvent event) {
    	Point2D location = new Point2D(event.getPoint().x, event.getPoint().y);
        if (event.isPopupTrigger()) {
        	this.popup.setNoteMode(((JBigBangDisplay)event.getSource()).getContents().getDisplayObjects().hasSelectedObjectAt(location));
            this.popup.show(event.getComponent(), event.getX(), event.getY());
        }
    }
	
}
