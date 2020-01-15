package br.com.virtualab.app.controller.converter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import br.com.virtualab.app.entity.usuario.Perfil;

public class PerfilConverterJsf implements Converter {

    public static List<Perfil> perfilList = new ArrayList<Perfil>();

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                Long id = Long.parseLong(submittedValue);

                for (Perfil perfil : perfilList) {
                    if (perfil.getId() == id) {
                        return perfil;
                    }
                }

            } catch (NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
            }
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((Perfil) value).getId());
        }
    }

}
