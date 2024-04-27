package org.vetronauta.latrunculus.client.plugin.properties;

import org.rubato.composer.preferences.UserPreferences;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StringClientProperty extends ClientPluginProperty<String> implements ActionListener, CaretListener {

    private JTextField textField = null;
    private Color bgColor = null;
    private static final UserPreferences prefs = UserPreferences.getUserPreferences();

    public StringClientProperty(PluginProperty<String> pluginProperty) {
        super(pluginProperty);
    }

    protected StringClientProperty(String key, String name, String value) {
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
        if (pluginProperty.isAcceptable(s)) {
            pluginProperty.setTmpValue(s);
            return;
        }
        textField.setBackground(prefs.getEntryErrorColor());
    }

    @Override
    public ClientPluginProperty<String> deepCopy() {
        return null;
    }

    @Override
    public JComponent getJComponent() {
        textField = new JTextField();
        textField.setText(pluginProperty.getValue());
        textField.addCaretListener(this);
        textField.addActionListener(this);
        bgColor = textField.getBackground();
        return textField;
    }

    @Override
    public void revert() {
        pluginProperty.revert(textField::setText);
    }

}
