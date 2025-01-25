package com.mycompany.ajedrez.menuComponents;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class UIUtils {
    public static JTextField crearInput(String placeholder, int maxLength) {
        JTextField input = new JTextField(placeholder);
        input.setFont(new Font("minimalPixel", Font.PLAIN, 48));
        input.setForeground(Color.WHITE);
        input.setBackground(new Color(0, 0, 0, 0));
        input.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        input.setOpaque(false);
        input.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if ((getLength() + str.length()) <= maxLength) {
                    super.insertString(offs, str, a);
                }
            }
        });
        return input;
    }
}