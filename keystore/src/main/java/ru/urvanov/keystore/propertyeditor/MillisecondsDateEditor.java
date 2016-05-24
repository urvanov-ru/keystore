package ru.urvanov.keystore.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.util.Date;

public class MillisecondsDateEditor extends PropertyEditorSupport {

    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || "".equals(text))
            setValue(null);
        else {
            Long milliseconds = Long.valueOf(text);
            this.setValue(new Date(milliseconds));
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        if (getValue() == null)
            return "";
        else
            return String.valueOf(((Date) this.getValue()).getTime());
    }
}
