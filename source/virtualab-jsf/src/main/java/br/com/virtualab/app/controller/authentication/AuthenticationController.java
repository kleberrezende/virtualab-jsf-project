package br.com.virtualab.app.controller.authentication;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import br.com.virtualab.app.controller.Abstract.AbstractController;
import br.com.virtualab.app.controller.principal.AbstractPrincipalController;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationController extends AbstractController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private AbstractPrincipalController abstractPrincipalController;

    public String exit() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            final ServletRequest request = (ServletRequest) context.getRequest();
            final ServletResponse response = (ServletResponse) context.getResponse();
            request.getRequestDispatcher("/j_spring_security_logout").forward(request, response);
            FacesContext.getCurrentInstance().responseComplete();

            if (abstractPrincipalController.getEntityManager() != null
                    && abstractPrincipalController.getEntityManager().getEntityManagerFactory().isOpen()) {
                abstractPrincipalController.getEntityManager().getEntityManagerFactory().close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMessageGrowl("Não foi possível fazer o logoff.\n" + e.getMessage());
        }
        return "login";
    }

    public void access() {
        try {
            if (!abstractPrincipalController.isInicializado()) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                final ServletRequest request = (ServletRequest) context.getRequest();
                final ServletResponse response = (ServletResponse) context.getResponse();
                request.getRequestDispatcher("/j_spring_security_check").forward(request, response);
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                setMessageGrowl("Sistema já iniciado. Não será possível fazer login novamente.");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setMessageGrowl(e.getMessage());
        }
    }

    public void keepAlive() {

    }

}
