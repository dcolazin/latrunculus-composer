package org.vetronauta.latrunculus.client.plugin.properties;

import org.rubato.composer.preferences.UserPreferences;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;
import org.vetronauta.latrunculus.plugin.properties.RationalProperty;
import org.vetronauta.latrunculus.server.parse.ArithmeticParsingUtils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RationalClientProperty extends ClientPluginProperty<Rational> implements ActionListener, CaretListener {

    private JTextField textField;
    private Color bgColor;

    public RationalClientProperty(RationalProperty pluginProperty) {
        super(pluginProperty);
    }

    private RationalClientProperty(PluginProperty<Rational> pluginProperty) {
        super(pluginProperty);
    }

    public RationalClientProperty(String key, String name, Rational value) {
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
            Rational d = ArithmeticParsingUtils.parseRational(s);
            if (pluginProperty.isAcceptable(d)) {
                pluginProperty.setTmpValue(d);
                return;
            }
        }
        catch (NumberFormatException e) {}
        textField.setBackground(UserPreferences.getUserPreferences().getEntryErrorColor());
    }

    @Override
    public ClientPluginProperty<Rational> deepCopy() {
        return new RationalClientProperty(pluginProperty);
    }

    @Override
    public JComponent getJComponent() {
        textField = new JTextField();
        textField.setText(pluginProperty.getValue().toString());
        textField.addCaretListener(this);
        textField.addActionListener(this);
        bgColor = textField.getBackground();
        return textField;
    }

    @Override
    public void revert() {
        pluginProperty.revert(r -> textField.setText(r.toString()));
    }

}
