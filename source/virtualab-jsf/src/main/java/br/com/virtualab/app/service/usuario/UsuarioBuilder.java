package br.com.virtualab.app.service.usuario;

import br.com.virtualab.app.entity.usuario.Perfil;
import br.com.virtualab.app.entity.usuario.RoleType;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.app.entity.usuario.UsuarioPerfil;
import br.com.virtualab.app.repository.usuario.UsuarioRepository;
import br.com.virtualab.app.service.Abstract.AbstractService;
import br.com.virtualab.utils.VirtualabResourceBundle;
import br.com.virtualab.utils.VirtualabUtils;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class UsuarioBuilder extends AbstractService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void build(EntityManager em, Usuario usuario, List<Perfil> perfilList, long maxUserAtivos, boolean alterarSenha) {
        try {
            usuario.setCodigoRenovarSenha(null);
            usuario.setPasswordRenovar(null);
            validarUsuario(em, usuario, alterarSenha);
            validarUsuarioAdmin(usuario, perfilList);
            validarMaxUserAtivos(em, usuario, maxUserAtivos);

            //******************************************************
            List<UsuarioPerfil> usuarioPerfilListOriginal = new ArrayList<UsuarioPerfil>();
            if (usuario.getId() != null) {
                usuarioPerfilListOriginal = usuarioRepository.buscarListaUsuarioPerfil(em, usuario);
            }
            List<UsuarioPerfil> usuarioPerfilList = compararListas(usuario, usuarioPerfilListOriginal, perfilList);
            //******************************************************

            if (em != null) {
                em.getTransaction().begin();
            }
            usuarioRepository.save(em, usuario, usuarioPerfilList);
            if (em != null) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            tratarErro(e);
        }
    }

    public void buildAlterarSenha(EntityManager em, Usuario usuario) {
        try {
            validarUsuario(em, usuario, true);
            Usuario usuarioR = usuarioRepository.buscarUsuarioPorId(em, usuario.getId());
            usuarioR.setPassword(usuario.getPassword());

            if (em != null) {
                em.getTransaction().begin();
            }
            usuarioRepository.saveAlterarSenha(em, usuarioR);
            if (em != null) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            tratarErro(e);
        }
    }

    private void validarMaxUserAtivos(EntityManager em, Usuario usuario, long maxUserAtivos) {
        if (maxUserAtivos > -1) {
            String msg = "Quantidade maxima de usuários ativos no sistema é de " + maxUserAtivos + ". Para ter mais usuários ativos favor contratar mais permissões.";

            if (VirtualabUtils.longIsEmpty(usuario.getId())) {
                if (usuario.isEnabled() == true) {
                    Assert.isTrue(usuarioRepository.buscarTotalUsuariosAtivos(em) < maxUserAtivos, msg);
                }
            } else {
                Usuario usuarioR = usuarioRepository.buscarUsuarioPorId(em, usuario.getId());
                if (usuarioR.isEnabled() == false && usuario.isEnabled() == true) {
                    Assert.isTrue(usuarioRepository.buscarTotalUsuariosAtivos(em) < maxUserAtivos, msg);
                }
            }
        }
    }

    private void validarUsuario(EntityManager em, Usuario usuario, boolean alterarSenha) {
        Assert.notNull(usuario, VirtualabResourceBundle.getMessage("controller.entidadeInvalida", "field.usuario"));
        Assert.hasText(usuario.getName(), "Campo nome não pode ser vazio.");
        Assert.hasText(usuario.getUsername(), "Campo nome do usuário não pode ser vazio.");
        Assert.isTrue(!validarUsername(usuario.getUsername()), "Campo nome do usuário não pode conter '/()._#&=-'.");
        Assert.hasText(usuario.getEmail(), "Campo e-mail não pode ser vazio.");

        Usuario usuarioR = usuarioRepository.buscarUsuarioPorUserName(em, usuario.getUsername());
        if (usuarioR != null && !usuarioR.getId().equals(usuario.getId())) {
            Assert.isTrue(false, "Já existe um usuário com o mesmo nome de usuario.");
        }
        validarSenha(usuario, alterarSenha);
    }

    private void validarSenha(Usuario usuario, boolean alterarSenha) {
        if (VirtualabUtils.longIsEmpty(usuario.getId()) || alterarSenha == true) {
            Assert.hasText(usuario.getPassword(), "Campo password não pode ser vazio.");
            Assert.isTrue(usuario.getPassword().equals(usuario.getConfirmarPassword()), "Senha não confere.\nA senha deve ser identica ao da caixa confirmar senha.");
            Assert.isTrue(usuario.getPassword().length() >= 4, "Senha muito curta! Senha deve conter 4 ou mais caracteres.");
            usuario.setPassword(DigestUtils.sha512Hex(usuario.getPassword()));
        }
    }

    private void validarUsuarioAdmin(Usuario usuario, List<Perfil> perfilList) {
        if (VirtualabUtils.longIsEmpty(usuario.getId()) == false && usuario.getId() == 1) {
            Assert.isTrue(usuario.getUsername().equals("admin"), "Campo Usuário deve ser admin.");
            Assert.isTrue(usuario.getName().equals("Admin"), "Campo Nome do usuário deve ser Admin.");
            Assert.isTrue(usuario.isEnabled(), "Admin não pode estar inativo! Marque como abilitado para salvar.");

            for (Perfil perfil : perfilList) {
                if (perfil.getDescricao().equals(RoleType.ROLE_ADMIN.name())) {
                    return;
                }
            }
            Assert.isTrue(false, "Administrador sem perfil de administrador.");
        }
    }

    private List<UsuarioPerfil> compararListas(Usuario usuario, List<UsuarioPerfil> usuarioPerfilList, List<Perfil> perfilList) {
        List<UsuarioPerfil> usuarioPerfilListRemove = new ArrayList<UsuarioPerfil>();
        List<UsuarioPerfil> usuarioPerfilListAdd = new ArrayList<UsuarioPerfil>();
        if (perfilList != null) {
            usuarioPerfilListRemove = removerPerfil(usuarioPerfilList, perfilList);
            usuarioPerfilListAdd = criarPerfil(usuario, usuarioPerfilList, perfilList);
        }

        usuarioPerfilList = new ArrayList<UsuarioPerfil>();
        usuarioPerfilList.addAll(usuarioPerfilListRemove);
        usuarioPerfilList.addAll(usuarioPerfilListAdd);
        return usuarioPerfilList;
    }

    private List<UsuarioPerfil> removerPerfil(List<UsuarioPerfil> usuarioPerfilList, List<Perfil> perfilList) {
        List<UsuarioPerfil> usuarioPerfilRemoverList = new ArrayList<UsuarioPerfil>();
        for (UsuarioPerfil usuarioPerfil : usuarioPerfilList) {
            boolean remove = true;
            for (Perfil perfil : perfilList) {
                if (usuarioPerfil.getPerfil().equals(perfil)) {
                    remove = false;
                }
            }
            if (remove) {
                usuarioPerfilRemoverList.add(usuarioPerfil);
            }
        }
        return usuarioPerfilRemoverList;
    }

    private List<UsuarioPerfil> criarPerfil(Usuario usuario, List<UsuarioPerfil> usuarioPerfilList, List<Perfil> perfilList) {
        List<UsuarioPerfil> usuarioPerfilCriarList = new ArrayList<UsuarioPerfil>();
        for (Perfil perfil : perfilList) {
            boolean criar = true;
            for (UsuarioPerfil usuarioPerfil : usuarioPerfilList) {
                if (perfil.equals(usuarioPerfil.getPerfil())) {
                    criar = false;
                }
            }
            if (criar) {
                UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
                usuarioPerfil.setPerfil(perfil);
                usuarioPerfil.setUsuario(usuario);
                usuarioPerfilCriarList.add(usuarioPerfil);
            }
        }
        return usuarioPerfilCriarList;
    }

    private boolean validarUsername(String username) {
        if (username.contains("/")) {
            return true;
        } else if (username.contains("(")) {
            return true;
        } else if (username.contains(")")) {
            return true;
        } else if (username.contains(".")) {
            return true;
        } else if (username.contains("_")) {
            return true;
        } else if (username.contains("#")) {
            return true;
        } else if (username.contains("&")) {
            return true;
        } else if (username.contains("=")) {
            return true;
        } else if (username.contains("-")) {
            return true;
        }
        return false;
    }

}
