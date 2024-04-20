package org.rubato.rubettes.bigbang.view.subview.toolbars;

import org.rubato.rubettes.bigbang.view.controller.ViewController;
import org.rubato.rubettes.bigbang.view.controller.mode.ShapingModeAdapter;

public class MultiTouchModeButtons extends BasicModeButtons {

	public MultiTouchModeButtons(ViewController viewController) {
		super(viewController);
	}

	@Override
	protected void initModeButtons() {
		/*this.add(new JDisplayModeButton(this.viewController, "Nav", new MTNavigationModeAdapter(this.viewController)));
		this.drawingModeButton = new JDisplayModeButton(this.viewController, "Dra", new MTDrawingModeAdapter(this.viewController));
		this.add(this.drawingModeButton);
		this.add(new JDisplayModeButton(this.viewController, "Gen", new MTGeneralModeAdapter(this.viewController)));
		this.add(new JDisplayModeButton(this.viewController, "Sma", new MTSmartModeAdapter(this.viewController)));
		this.add(new JDisplayModeButton(this.viewController, "Ref", new MTReflectionModeAdapter(this.viewController)));
		*/this.add(new JDisplayModeButton(this.viewController, "Sha", new ShapingModeAdapter(this.viewController)));
	}

}
