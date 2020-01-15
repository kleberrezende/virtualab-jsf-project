package br.com.virtualab.app.repository.menu.impl;

import br.com.virtualab.app.entity.menu.MenuFavorito;
import br.com.virtualab.app.entity.menu.QMenuFavorito;
import br.com.virtualab.app.repository.Abstract.AbstractRepository;
import br.com.virtualab.app.repository.menu.MenuRepository;
import com.mysema.query.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

public class MenuRepositoryImpl extends AbstractRepository implements MenuRepository {

    private static final long serialVersionUID = 1L;

    @Override
    @Transactional(readOnly = false)
    public void save(EntityManager em, MenuFavorito menuFavorito) {
        try {
            if (em == null) {
                em = getEntityManager();
            }
            if (menuFavorito.getId() == null) {
                em.persist(menuFavorito);
            } else {
                MenuFavorito usuarioPerfilR = em.getReference(MenuFavorito.class, menuFavorito.getId());
                em.remove(usuarioPerfilR);
            }
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuFavorito> buscarMenuFavoritoPorUsuario(EntityManager em, Long id) {
        if (em == null) {
            em = getEntityManager();
        } else {
            em.clear();
        }
        JPAQuery query = new JPAQuery(em);
        QMenuFavorito qMenuFavorito = QMenuFavorito.menuFavorito;
        query.from(qMenuFavorito).where(qMenuFavorito.usuario.id.eq(id));
        return query.list(qMenuFavorito);
    }

}
