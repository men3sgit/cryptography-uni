package com.menes.cryptography.gui.custom;

import javax.swing.*;
import javax.swing.text.*;

public class CharacterLimitTextField extends JTextField {
    private int characterLimit;
    private PlainDocument doc;

    public CharacterLimitTextField(int characterLimit) {
        this.characterLimit = characterLimit;
        this.doc = (PlainDocument) getDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(characterLimit));
    }

    public CharacterLimitTextField() {
        this(0);
    }

    private class DocumentSizeFilter extends DocumentFilter {
        private int maxCharacters;

        public DocumentSizeFilter(int maxChars) {
            maxCharacters = maxChars;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (fb.getDocument().getLength() + string.length() <= maxCharacters) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            int newLength = fb.getDocument().getLength() - length + text.length();
            if (newLength <= maxCharacters) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    public void setCharacterLimit(int n) {
        this.characterLimit = n;
        doc.setDocumentFilter(new DocumentSizeFilter(characterLimit));
    }
    public int getCharacterLimit(){
        return characterLimit;
    }
}
