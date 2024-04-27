package org.vetronauta.latrunculus.client.plugin.properties;

import org.rubato.composer.preferences.UserPreferences;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoubleClientProperty extends ClientPluginProperty<Double> implements ActionListener, CaretListener {

    private JTextField textField;
    private Color bgColor;

    protected DoubleClientProperty(PluginProperty<Double> pluginProperty) {
        super(pluginProperty);
    }

    protected DoubleClientProperty(String key, String name, Double value) {
        super(key, name, value);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        update();
    }

    @Override
    public void caretUpdate(CaretEvent caretEvent) {
        update();
    }

    private void update() {
        textField.setBackground(bgColor);
        String s = textField.getText();
        try {
            double d = Double.parseDouble(s);
            if (pluginProperty.isAcceptable(d)) {
                pluginProperty.setTmpValue(d);
                return;
            }
        }
        catch (NumberFormatException e) { /* do nothing */ }
        textField.setBackground(UserPreferences.getUserPreferences().getEntryErrorColor());
    }

    @Override
    public ClientPluginProperty<Double> deepCopy() {
        return new DoubleClientProperty(this.pluginProperty);
    }

    @Override
    public JComponent getJComponent() {
        textField = new JTextField();
        textField.setText(Double.toString(pluginProperty.getValue()));
        textField.addCaretListener(this);
        textField.addActionListener(this);
        bgColor = textField.getBackground();
        return textField;
    }

    @Override
    public void revert() {
        pluginProperty.revert(d -> textField.setText(Double.toString(d)));
    }
}
