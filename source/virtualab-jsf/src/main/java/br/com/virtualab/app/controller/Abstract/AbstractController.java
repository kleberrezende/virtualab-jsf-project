package br.com.virtualab.app.controller.Abstract;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.exception.RepositoryException;
import br.com.virtualab.exception.ServiceException;

public abstract class AbstractController implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String USUARIO_SESSION = "USUARIO_SESSION";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected void tratarErro(Exception e) {
        if (e instanceof ServiceException) {
            setMessageGrowl(FacesMessage.SEVERITY_ERROR, "Erro na camada de serviço. " + e.getMessage());
        } else if (e instanceof RepositoryException) {
            setMessageGrowl(FacesMessage.SEVERITY_ERROR, "Erro na camada de repositório. " + e.getMessage());
        } else if (e instanceof IllegalArgumentException) {
            setMessageGrowl(FacesMessage.SEVERITY_WARN, e.getMessage());
        } else {
            setMessageGrowl(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    protected void setMessageGrowl(String message) {
        setMessageComponent(null, message);
    }

    protected void setMessageComponent(String clientId, String message) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(message));
    }

    protected void setMessageGrowl(FacesMessage message) {
        setMessageComponent(null, message);
    }

    protected void setMessageComponent(String clientId, FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(clientId, message);
    }

    protected void setMessageGrowl(Severity severity, String message) {
        setMessageComponent(severity, null, message);
    }

    protected void setMessageComponent(Severity severity, String clientId, String message) {
        String summary = "";
        String detail = "";
        if (clientId == null) {
            summary = message;
        } else {
            detail = message;
        }

        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(severity, summary, detail));
    }

    public void putSession(String key, Object object) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, object);
    }

    public Object getSession(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }

    public void removeSession(String key) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
    }

    public void clearSession() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
    }

    public Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
