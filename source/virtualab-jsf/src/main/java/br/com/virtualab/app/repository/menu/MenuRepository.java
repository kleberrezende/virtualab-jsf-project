package br.com.virtualab.app.repository.menu;

import br.com.virtualab.app.entity.menu.MenuFavorito;
import java.util.List;
import javax.persistence.EntityManager;

public interface MenuRepository {

    void save(EntityManager em, MenuFavorito menuFavorito);

    List<MenuFavorito> buscarMenuFavoritoPorUsuario(EntityManager em, Long id);

}
