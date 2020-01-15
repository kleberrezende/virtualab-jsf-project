package br.com.virtualab.app.controller.authentication;

import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.springframework.security.web.WebAttributes;
import br.com.virtualab.app.controller.Abstract.AbstractController;

public class LoginErrorPhaseListener extends AbstractController implements PhaseListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void afterPhase(PhaseEvent event) {

    }

    @Override
    public void beforePhase(PhaseEvent event) {
        final Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Exception dadosIncorretosException = (Exception) sessionMap.get(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (dadosIncorretosException != null) {
            sessionMap.put(WebAttributes.AUTHENTICATION_EXCEPTION, null);
            if (dadosIncorretosException.getMessage().toLowerCase().equals("bad credentials")) {
                setMessageGrowl(FacesMessage.SEVERITY_ERROR, "Usuário ou senha incorreto(s).");
            } else {
                setMessageGrowl(FacesMessage.SEVERITY_ERROR, dadosIncorretosException.getMessage());
            }
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}
