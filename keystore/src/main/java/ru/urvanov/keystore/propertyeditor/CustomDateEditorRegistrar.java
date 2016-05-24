package ru.urvanov.keystore.propertyeditor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

public class CustomDateEditorRegistrar implements PropertyEditorRegistrar {

    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        // registry.registerCustomEditor(Date.class, new CustomDateEditor(new
        // SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz"), true));
    }
}
