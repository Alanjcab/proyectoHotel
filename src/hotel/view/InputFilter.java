package hotel.view;

import javax.swing.text.*;

/*filtro para los campos*/

public class InputFilter extends DocumentFilter {

    private final int maxLength;
    private final String allowedPattern;

    public InputFilter(int maxLength, String allowedPattern) {
        this.maxLength = maxLength;
        this.allowedPattern = allowedPattern;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs)
            throws BadLocationException {
        replace(fb, offset, 0, text, attrs);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null) {
            return;
        }

        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());

        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

        if (newText.length() <= maxLength && newText.matches(allowedPattern)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}