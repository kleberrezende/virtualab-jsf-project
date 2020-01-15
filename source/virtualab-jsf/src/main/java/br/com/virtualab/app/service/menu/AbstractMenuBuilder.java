package br.com.virtualab.app.service.menu;

import br.com.virtualab.app.entity.menu.MenuFavorito;
import br.com.virtualab.app.entity.menu.MenuItem;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.app.repository.menu.MenuRepository;
import br.com.virtualab.app.service.Abstract.AbstractService;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractMenuBuilder extends AbstractService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private MenuRepository menuRepository;

    public void build(EntityManager em, MenuFavorito menuFavorito, MenuItem menuItem, Usuario usuario) {
        try {
            if (menuFavorito == null) {
                menuFavorito = new MenuFavorito();
                menuFavorito.setUsuario(usuario);
                menuFavorito.setMenuItem_id(menuItem.getId());
            }
            if (em != null) {
                em.getTransaction().begin();
            }
            menuRepository.save(em, menuFavorito);
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

}
