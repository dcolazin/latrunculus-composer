package org.vetronauta.latrunculus.client.plugin.properties;

import org.rubato.composer.preferences.UserPreferences;
import org.vetronauta.latrunculus.plugin.properties.IntegerProperty;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntegerClientProperty extends ClientPluginProperty<Integer> implements ActionListener, CaretListener {

    private JTextField textField;
    private Color bgColor;

    public IntegerClientProperty(IntegerProperty pluginProperty) {
        super(pluginProperty);
    }

    private IntegerClientProperty(PluginProperty<Integer> pluginProperty) {
        super(pluginProperty);
    }

    protected IntegerClientProperty(String key, String name, Integer value) {
        super(new IntegerProperty(key, name, value));
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
            int i = Integer.parseInt(s);
            if (pluginProperty.isAcceptable(i)) {
                pluginProperty.setTmpValue(i);
                return;
            }
        }
        catch (NumberFormatException e) { /* do nothing */ }
        textField.setBackground(UserPreferences.getUserPreferences().getEntryErrorColor());
    }

    @Override
    public ClientPluginProperty<Integer> deepCopy() {
        return new IntegerClientProperty(pluginProperty);
    }

    @Override
    public JComponent getJComponent() {
        textField = new JTextField();
        textField.setText(Integer.toString(pluginProperty.getValue()));
        textField.addCaretListener(this);
        textField.addActionListener(this);
        bgColor = textField.getBackground();
        return textField;
    }

    @Override
    public void revert() {
        pluginProperty.revert(i -> textField.setText(Integer.toString(i)));
    }

}
