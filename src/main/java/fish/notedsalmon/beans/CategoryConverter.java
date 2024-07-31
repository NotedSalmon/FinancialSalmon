package fish.notedsalmon.beans;

import fish.notedsalmon.entities.Category;
import fish.notedsalmon.services.EditService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "categoryConverter", managed = true)
public class CategoryConverter implements Converter<Category> {

    @Inject
    private EditService editService;

    @Override
    public Category getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Integer id = Integer.parseInt(value);
            return editService.findCategoryById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid category ID format", e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Category category) {
        if (category == null) {
            return "";
        }
        return category.getId().toString();
    }
}