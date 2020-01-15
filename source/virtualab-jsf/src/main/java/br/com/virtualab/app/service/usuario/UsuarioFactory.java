package br.com.virtualab.app.service.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.virtualab.app.entity.usuario.Perfil;
import br.com.virtualab.app.entity.usuario.RoleType;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.app.repository.usuario.UsuarioRepository;
import br.com.virtualab.app.service.Abstract.AbstractService;
import javax.persistence.EntityManager;
import org.apache.commons.codec.digest.DigestUtils;

public class UsuarioFactory extends AbstractService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario buscarUsuarioPorId(EntityManager em, Long id) {
        return usuarioRepository.buscarUsuarioPorId(em, id);
    }

    public List<Usuario> buscarListaUsuarios(EntityManager em, String name, String username, Boolean ativo) {
        return usuarioRepository.buscarListaUsuarios(em, name, username, ativo);
    }

    public List<Perfil> buscarListaPerfil(EntityManager em) {
        return usuarioRepository.buscarListaPerfil(em);
    }

    public Perfil buscarPerfilPorId(EntityManager em, Long id) {
        return usuarioRepository.buscarPerfilPorId(em, id);
    }

    public Usuario buscarUsuarioPorUserName(EntityManager em, String username) {
        return usuarioRepository.buscarUsuarioPorUserName(em, username);
    }

    /**
     * Verifica se o usuário é um usuário supervisor
     *
     * @param em:EntityManager
     * @param usuario:Usuario
     * @return Mensagem de erro ou caso seja um SUser o id do usuário Ex: id=10
     */
    public String validarSUser(EntityManager em, Usuario usuario) {
        Usuario usuarioR = buscarUsuarioPorUserName(em, usuario.getUsername());
        if (usuarioR != null && DigestUtils.sha512Hex(usuario.getPassword()).equals(usuarioR.getPassword())) {
            if (usuarioR.isEnabled()) {
                if (usuarioR.getPerfilList() != null && isRoleInPefilList(RoleType.ROLE_SUSER, usuarioR.getPerfilList())) {
                    return "id=" + usuarioR.getId().toString();
                } else {
                    return "Este Usuário não tem perfil de Super Usuário.";
                }
            } else {
                return "Este Usuário está inativo.";
            }
        } else {
            return "Usuário ou Senha incorreto(s).";
        }
    }

    public boolean isRoleInPefilList(RoleType roleType, List<Perfil> perfilList) {
        for (Perfil perfil : perfilList) {
            if (perfil.getDescricao().equals(roleType.name())) {
                return true;
            }
        }
        return false;
    }

}
