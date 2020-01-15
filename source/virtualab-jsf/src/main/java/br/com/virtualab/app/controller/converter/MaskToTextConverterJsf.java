package br.com.virtualab.app.controller.converter;

import br.com.virtualab.utils.VirtualabUtils;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class MaskToTextConverterJsf implements Converter {

    public Object getAsObject(FacesContext ctx, UIComponent component,
            String value) {

        if (value != null) {
            if (value.isEmpty() == false) {
                value = VirtualabUtils.apenasTexto(value);
            }
        }

        return value;
    }

    public String getAsString(FacesContext ctx, UIComponent component,
            Object value) {
        return value.toString();
    }

}
