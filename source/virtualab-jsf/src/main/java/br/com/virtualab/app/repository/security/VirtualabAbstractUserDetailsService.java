package br.com.virtualab.app.repository.security;

import java.io.Serializable;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import br.com.virtualab.app.entity.usuario.QUsuario;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.app.repository.Abstract.AbstractRepository;
import br.com.virtualab.utils.VirtualabUtils;
import com.mysema.query.jpa.impl.JPAQuery;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import org.apache.commons.codec.digest.DigestUtils;

public class VirtualabAbstractUserDetailsService extends AbstractRepository implements UserDetailsService, Serializable {

    private static final long serialVersionUID = 1L;
    private EntityManager entityManagerLogin = null;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        logger.debug("busca usuário");
        if (username == null) {
            throw new UsernameNotFoundException("Usuário não pode estar vazio.");
        }
        Usuario usuario = getUser(validarUsername(username));
        trocarSenha(usuario, username);
        return usuario;
    }

    private String validarUsername(String username) {
        if (username.startsWith("#p#")) {
            username = username.replace("#p#", "");
        }
        return username;
    }

    @Transactional(readOnly = true)
    public Usuario getUser(String username) {
        logger.debug("Validando username.");
        verificarTrocaSenha(username);
        JPAQuery query = new JPAQuery(getEntityManager());
        QUsuario qUsuario = QUsuario.usuario;
        query.from(qUsuario).where(qUsuario.username.eq(username));
        Usuario usuario = query.uniqueResult(qUsuario);
        validarUsuario(usuario);
        return usuario;
    }

    public void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            lancarExcessao("Bad credentials");
        } else {
            if (!usuario.isEnabled()) {
                lancarExcessao(String.format("Usuário %s está inativo.", usuario.getUsername()));
            } else if (usuario.getPerfilList().isEmpty()) {
                lancarExcessao(String.format("Usuário %s sem perfil de acesso.", usuario.getUsername()));
            }
        }
    }

    @Transactional(readOnly = false)
    public void verificarTrocaSenha(String username) {
        if (username.startsWith("#")) {
            Usuario usuario = getUserCodigoRenovarSenha(username);
            if (usuario != null) {
                usuario.setCodigoRenovarSenha(null);
                usuario.setPassword(usuario.getPasswordRenovar());
                saveUsuario(usuario);
                lancarExcessao("Nova senha ativada. Entre com seu usuário e com sua nova senha.");
            } else {
                lancarExcessao("Código de ativação de nova senha incorreto.");
            }
        }
    }

    @Transactional(readOnly = true)
    private Usuario getUserCodigoRenovarSenha(String username) {
        JPAQuery query;
        if (entityManagerLogin != null) {
            query = new JPAQuery(getEntityManagerLogin());
        } else {
            query = new JPAQuery(getEntityManager());
        }
        QUsuario qUsuario = QUsuario.usuario;
        query.from(qUsuario).where(qUsuario.codigoRenovarSenha.eq(username));
        Usuario usuario = query.uniqueResult(qUsuario);
        return usuario;
    }

    @Transactional(readOnly = false)
    private void trocarSenha(Usuario usuario, String username) {
        if (username.startsWith("#p#")) {
            if (VirtualabUtils.stringIsEmpty(usuario.getEmail())) {
                lancarExcessao("E-mail não cadastrado para este usuário.");
            }
            usuario.setCodigoRenovarSenha("#" + VirtualabUtils.getAlfaNumericoAleatorio(10));
            usuario.setConfirmarPassword(VirtualabUtils.getAlfaNumericoAleatorio(10));
            usuario.setPasswordRenovar(DigestUtils.sha512Hex(usuario.getConfirmarPassword()));
            saveUsuario(usuario);
            enviarEmail(usuario);
        }
    }

    @Transactional(readOnly = false)
    private void saveUsuario(Usuario usuario) {
        try {
            if (entityManagerLogin != null) {
                entityManagerLogin.getTransaction().begin();
                entityManagerLogin.merge(usuario);
                entityManagerLogin.getTransaction().commit();
            } else {
                getEntityManager().merge(usuario);
            }
        } catch (Exception e) {
            if (entityManagerLogin != null) {
                if (entityManagerLogin.getTransaction().isActive()) {
                    entityManagerLogin.getTransaction().rollback();
                }
            }
            tratarErro(e);
        }
    }

    private void enviarEmail(Usuario usuario) {
        try {
            ArrayList<String> paraList = new ArrayList<String>();
            paraList.add(usuario.getEmail());
            VirtualabUtils.sendEmailDeVirtualab(paraList, "Trocar senha sistema.",
                    String.format("Olá %s,\n"
                            + "Você precisa ativar a sua nova senha.\n"
                            + "Para ativar coloque seu código de ativação no campo usuário e a nova senha no campo senha.\n"
                            + "Após feito isso entre com seu usuário normalmente usando seu usuário e a nova senha.\n\n"
                            + "Seu código de ativação é:  %s\n"
                            + "Sua nova senha é:  %s\n\n"
                            + "Virtua Lab Sistemas",
                            usuario.getName(), usuario.getCodigoRenovarSenha(), usuario.getConfirmarPassword()));
        } catch (Exception e) {
            tratarErro(e);
        }
        lancarExcessao(String.format("Confira seu e-mail. '%s'", usuario.getEmail()));
    }

    public void lancarExcessao(String msg) {
        if (entityManagerLogin != null) {
            if (entityManagerLogin.getEntityManagerFactory().isOpen()) {
                entityManagerLogin.getEntityManagerFactory().close();
            }
            if (entityManagerLogin.isOpen()) {
                entityManagerLogin.close();
            }
        }
        throw new BadCredentialsException(msg);
    }

    public EntityManager getEntityManagerLogin() {
        return entityManagerLogin;
    }

    public void setEntityManagerLogin(EntityManager entityManagerLogin) {
        this.entityManagerLogin = entityManagerLogin;
    }

}
