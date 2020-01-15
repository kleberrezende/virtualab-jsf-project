package br.com.virtualab.app.service.principal;

import br.com.virtualab.app.service.Abstract.AbstractService;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class PrincipalFactory extends AbstractService {

    private static final long serialVersionUID = 1L;

    public boolean isPermitirAcessoUsuario(String perfilUsuario) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.isUserInRole(perfilUsuario);
    }

    public void refresh() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
        context.renderResponse();
    }

    public String getContextPath() {
        String contextName = FacesContext.getCurrentInstance().getExternalContext().getContextName();
        String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(contextName);
        contextPath = contextPath.substring(0, contextPath.indexOf("/target/" + contextName));
        contextPath = contextPath + "/target/" + contextName;
        return contextPath;
    }

}
