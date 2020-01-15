package br.com.virtualab.app.service.menu;

import br.com.virtualab.app.entity.menu.MenuFavorito;
import br.com.virtualab.app.entity.menu.MenuGrupo;
import br.com.virtualab.app.entity.menu.MenuItem;
import br.com.virtualab.app.entity.usuario.RoleType;
import br.com.virtualab.app.repository.menu.MenuRepository;
import br.com.virtualab.app.service.Abstract.AbstractService;
import br.com.virtualab.app.service.principal.PrincipalFactory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractMenuFactory extends AbstractService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PrincipalFactory principalFactory;

    @Autowired
    private MenuRepository menuRepository;

    public List<MenuGrupo> inicializarMenu() {
        logger.debug("Inicializar Menu ...");
        List<MenuGrupo> menuGrupos = new ArrayList<MenuGrupo>();
        return menuGrupos;
    }

    public void addMenuGrupo(List<MenuGrupo> menuGrupos, List<MenuItem> menuItens, String grupo, String tags, boolean grupoVisivel) {
        menuGrupos.add(new MenuGrupo(grupo, tags, grupoVisivel, menuItens));
    }

    public void addMenuItem(List<MenuItem> menuItens, String id, String displayLabel, String iconeMenu, String link, String tags, boolean itemVisivel, RoleType roleType) {
        if (isItemPermissao(roleType)) {
            menuItens.add(new MenuItem(id, displayLabel, iconeMenu, link, tags, itemVisivel, isItemPermissao(roleType)));
        }
    }

    public void addMenuItem(List<MenuItem> menuItens, String id, String displayLabel, String iconeMenu, String link, String tags, boolean itemVisivel, boolean itemPermissao) {
        if (itemPermissao) {
            menuItens.add(new MenuItem(id, displayLabel, iconeMenu, link, tags, itemVisivel, itemPermissao));
        }
    }

    public boolean isItemPermissao(RoleType roleType) {
        return principalFactory.isPermitirAcessoUsuario(roleType.name());
    }

    public List<MenuFavorito> buscarMenuFavoritoPorUsuario(EntityManager em, Long id) {
        return menuRepository.buscarMenuFavoritoPorUsuario(em, id);
    }

}
