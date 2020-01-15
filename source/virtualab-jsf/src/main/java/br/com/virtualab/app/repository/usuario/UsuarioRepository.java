package br.com.virtualab.app.repository.usuario;

import java.util.List;

import br.com.virtualab.app.entity.usuario.Perfil;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.app.entity.usuario.UsuarioPerfil;
import javax.persistence.EntityManager;

public interface UsuarioRepository {

    void save(EntityManager em, Usuario usuario, List<UsuarioPerfil> usuarioPerfilList);

    void saveAlterarSenha(EntityManager em, Usuario usuario);

    Usuario buscarUsuarioPorId(EntityManager em, Long id);

    List<Usuario> buscarListaUsuarios(EntityManager em, String name, String username, Boolean ativo);

    List<Perfil> buscarListaPerfil(EntityManager em);

    Perfil buscarPerfilPorId(EntityManager em, Long id);

    List<UsuarioPerfil> buscarListaUsuarioPerfil(EntityManager em, Usuario usuario);

    Usuario buscarUsuarioPorUserName(EntityManager em, String username);
    
    long buscarTotalUsuariosAtivos(EntityManager em);

}
