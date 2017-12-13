package UI.Swing;


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

class CharDocumentFilter extends DocumentFilter {

    @Override
    public void replace(FilterBypass fb, int i, int i1, String string, AttributeSet as) throws BadLocationException {
        for (int n = string.length(); n > 0; n--) {
            char c = string.charAt(n - 1);//get a single character of the string
            if (Character.isAlphabetic(c) || c == ' ') {//if its an alphabetic character or white space
                super.replace(fb, i, i1, String.valueOf(c), as);//allow update to take place for the given character
            } else {//it was not an alphabetic character or white space
                System.out.println(c+" is not allowed.");
            }
        }
    }

    @Override
    public void remove(FilterBypass fb, int i, int i1) throws BadLocationException {
        super.remove(fb, i, i1);
    }

    @Override
    public void insertString(FilterBypass fb, int i, String string, AttributeSet as) throws BadLocationException {
        super.insertString(fb, i, string, as);

    }
}
