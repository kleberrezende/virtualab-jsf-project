package br.com.virtualab.app.repository.usuario.impl;

import br.com.virtualab.app.entity.Abstract.AbstractEntity;
import br.com.virtualab.app.entity.usuario.Perfil;
import br.com.virtualab.app.entity.usuario.QPerfil;
import br.com.virtualab.app.entity.usuario.QUsuario;
import br.com.virtualab.app.entity.usuario.QUsuarioPerfil;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.app.entity.usuario.UsuarioPerfil;
import br.com.virtualab.app.repository.Abstract.AbstractRepository;
import br.com.virtualab.app.repository.usuario.UsuarioRepository;
import br.com.virtualab.utils.VirtualabUtils;

import com.mysema.query.jpa.impl.JPAQuery;

import java.util.List;
import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

public class UsuarioRepositoryImpl extends AbstractRepository implements UsuarioRepository {

    private static final long serialVersionUID = 1L;

    @Override
    @Transactional(readOnly = false)
    public void save(EntityManager em, Usuario usuario, List<UsuarioPerfil> usuarioPerfilList) {
        try {
            if (em == null) {
                em = getEntityManager();
            }
            setAlteracao((AbstractEntity) usuario);
            if (usuario.getId() == null) {
                em.persist(usuario);
            } else {
                em.merge(usuario);
            }
            if (usuarioPerfilList != null) {
                for (UsuarioPerfil usuarioPerfil : usuarioPerfilList) {
                    if (usuarioPerfil.getId() == null) {
                        em.persist(usuarioPerfil);
                    } else {
                        UsuarioPerfil usuarioPerfilR = em.getReference(UsuarioPerfil.class, usuarioPerfil.getId());
                        em.remove(usuarioPerfilR);
                    }
                }
            }
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void saveAlterarSenha(EntityManager em, Usuario usuario) {
        try {
            if (em == null) {
                em = getEntityManager();
            }
            setAlteracao((AbstractEntity) usuario);
            em.merge(usuario);
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarUsuarioPorId(EntityManager em, Long id) {
        if (em == null) {
            em = getEntityManager();
        } else {
            em.clear();
        }
        JPAQuery query = new JPAQuery(em);
        QUsuario qUsuario = QUsuario.usuario;
        query.from(qUsuario).where(qUsuario.id.eq(id));
        return query.uniqueResult(qUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarListaUsuarios(EntityManager em, String name, String username, Boolean ativo) {
        if (em == null) {
            em = getEntityManager();
        } else {
            em.clear();
        }
        JPAQuery query = new JPAQuery(em);
        QUsuario qUsuario = QUsuario.usuario;
        query.from(qUsuario);
        if (!VirtualabUtils.stringIsEmpty(name)) {
            query.where(qUsuario.name.startsWith(name));
        }
        if (!VirtualabUtils.stringIsEmpty(username)) {
            query.where(qUsuario.username.startsWith(username));
        }
        if (ativo != null) {
            query.where(qUsuario.enabled.eq(ativo));
        }
        query.orderBy(qUsuario.username.asc());
        return query.list(qUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Perfil> buscarListaPerfil(EntityManager em) {
        if (em == null) {
            em = getEntityManager();
        } else {
            em.clear();
        }
        JPAQuery query = new JPAQuery(em);
        QPerfil qPerfil = QPerfil.perfil;
        query.from(qPerfil);
        return query.list(qPerfil);
    }

    @Override
    @Transactional(readOnly = true)
    public Perfil buscarPerfilPorId(EntityManager em, Long id) {
        if (em == null) {
            em = getEntityManager();
        } else {
            em.clear();
        }
        JPAQuery query = new JPAQuery(em);
        QPerfil qPerfil = QPerfil.perfil;
        query.from(qPerfil).where(qPerfil.id.eq(id));
        return query.uniqueResult(qPerfil);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioPerfil> buscarListaUsuarioPerfil(EntityManager em, Usuario usuario) {
        if (em == null) {
            em = getEntityManager();
        } else {
            em.clear();
        }
        JPAQuery query = new JPAQuery(em);
        QUsuarioPerfil qUsuarioPerfil = QUsuarioPerfil.usuarioPerfil;
        query.from(qUsuarioPerfil).where(qUsuarioPerfil.usuario.eq(usuario));
        return query.list(qUsuarioPerfil);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarUsuarioPorUserName(EntityManager em, String username) {
        if (em == null) {
            em = getEntityManager();
        } else {
            em.clear();
        }
        JPAQuery query = new JPAQuery(em);
        QUsuario qUsuario = QUsuario.usuario;
        query.from(qUsuario).where(qUsuario.username.eq(username));
        return query.uniqueResult(qUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public long buscarTotalUsuariosAtivos(EntityManager em) {
        if (em == null) {
            em = getEntityManager();
        } else {
            em.clear();
        }
        JPAQuery query = new JPAQuery(em);
        QUsuario qUsuario = QUsuario.usuario;
        query.from(qUsuario).where(qUsuario.enabled.eq(true));
        return query.singleResult(qUsuario.id.count());
    }

}
