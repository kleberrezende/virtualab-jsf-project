package br.com.virtualab.app.controller.converter;

import br.com.virtualab.app.entity.Abstract.AbstractEntity;
import br.com.virtualab.app.entity.Abstract.AbstractEntityId;
import br.com.virtualab.app.entity.Abstract.AbstractEntityIdGV;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class AbstractEntityConverterJsf implements Converter {

    public Object getAsObject(FacesContext ctx, UIComponent component,
            String value) {
        if (value != null) {
            return this.getAttributesFrom(component).get(value);
        }
        return null;
    }

    public String getAsString(FacesContext ctx, UIComponent component,
            Object value) {
        if (value != null && !"".equals(value)) {
            if (value instanceof AbstractEntityId) {
                return verificaAbstractEntityId(component, value);
            }
            if (value instanceof AbstractEntityIdGV) {
                return verificaAbstractEntityIdGV(component, value);
            }
            if (value instanceof AbstractEntity) {
                return verificaAbstractEntity(component, value);
            }
        }
        return "";
    }

    private String verificaAbstractEntityId(UIComponent component, Object value) {
        AbstractEntityId entity = (AbstractEntityId) value;
        if (entity.getId() != null) {
            this.addAttribute(component, entity);

            if (entity.getId() != null) {
                return String.valueOf(entity.getId());
            }
            return (String) value;
        }
        return "";
    }

    private String verificaAbstractEntityIdGV(UIComponent component, Object value) {
        AbstractEntityIdGV entity = (AbstractEntityIdGV) value;
        if (entity.getId() != null) {
            this.addAttribute(component, entity);

            if (entity.getId() != null) {
                return String.valueOf(entity.getId());
            }
            return (String) value;
        }
        return "";
    }

    private String verificaAbstractEntity(UIComponent component, Object value) {
        AbstractEntity entity = (AbstractEntity) value;
        if (entity.getId() != null) {
            this.addAttribute(component, entity);

            if (entity.getId() != null) {
                return String.valueOf(entity.getId());
            }
            return (String) value;
        }
        return "";
    }

    private void addAttribute(UIComponent component, AbstractEntityId o) {
        this.getAttributesFrom(component).put(o.getId().toString(), o);
    }

    private void addAttribute(UIComponent component, AbstractEntityIdGV o) {
        this.getAttributesFrom(component).put(o.getId().toString(), o);
    }

    private void addAttribute(UIComponent component, AbstractEntity o) {
        this.getAttributesFrom(component).put(o.getId().toString(), o);
    }

    private Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }

}
