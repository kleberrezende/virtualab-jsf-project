package br.com.virtualab.app.controller.principal;

import java.util.Date;
import br.com.virtualab.app.controller.Abstract.AbstractController;
import br.com.virtualab.app.entity.menu.MenuFavorito;
import br.com.virtualab.app.entity.menu.MenuGrupo;
import br.com.virtualab.app.entity.menu.MenuItem;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.app.service.menu.AbstractMenuBuilder;
import br.com.virtualab.app.service.menu.AbstractMenuFactory;
import br.com.virtualab.app.service.principal.PrincipalFactory;
import br.com.virtualab.utils.VirtualabUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractPrincipalController extends AbstractController {

    private static final long serialVersionUID = 1L;

    private boolean inicializado = false;
    protected boolean habilitarMenu = false;
    private String temaSistema = "aristo";
    protected EntityManager entityManager;

    @Autowired
    protected PrincipalFactory principalFactory;

    @PostConstruct
    public void iniciar() {
        logger.debug("Classe principal carregada...");
    }

    public Date getDataHoraServer() {
        return new Date();
    }

    public boolean isItemMenuVisivel(String perfil) {
        return principalFactory.isPermitirAcessoUsuario(perfil);
    }

    public String getTemaSistema() {
        if (temaSistema == null) {
            temaSistema = "aristo";
        }
        return temaSistema;
    }

    public void setTemaSistema(String temaSistema) {
        this.temaSistema = temaSistema;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void inicializar() {
        try {
            if (inicializado == false) {
                inicializado = true;
                habilitarMenu = true;
                logger.debug("Classe principal inicializar...");
                carregarInicializar();
            }
        } catch (Exception e) {
            inicializado = false;
            habilitarMenu = false;
            tratarErro(e);
        }
    }

    public void carregarInicializar() {
        //dar override nesse método na classe PrincipalController 
        //e fazer as ações que quiser iniciar
    }

    public Usuario getUsuarioLogin() {
        return super.getUsuarioLogado();
    }

    public boolean isInicializado() {
        return inicializado;
    }

    public boolean isHabilitarMenu() {
        return habilitarMenu;
    }

    //******************************************************
    //Menu sistema
    @Autowired
    private AbstractMenuBuilder abstractMenuBuilder;

    @Autowired
    private AbstractMenuFactory abstractMenuFactory;

    private List<MenuGrupo> menuGrupos;
    private String filtroMenu;
    private MenuItem itemMenuSelecionado;
    private List<MenuFavorito> menuFavoritos;

    public List<MenuGrupo> getMenuGrupos() {
        return menuGrupos;
    }

    public void setMenuGrupos(List<MenuGrupo> menuGrupos) {
        this.menuGrupos = menuGrupos;
    }

    public String getFiltroMenu() {
        return filtroMenu;
    }

    public void setFiltroMenu(String filtroMenu) {
        this.filtroMenu = filtroMenu;
    }

    public MenuItem getItemMenuSelecionado() {
        return itemMenuSelecionado;
    }

    public void setItemMenuSelecionado(MenuItem itemMenuSelecionado) {
        this.itemMenuSelecionado = itemMenuSelecionado;
    }

    public List<MenuFavorito> getMenuFavoritos() {
        return menuFavoritos;
    }

    public void setMenuFavoritos(List<MenuFavorito> menuFavoritos) {
        this.menuFavoritos = menuFavoritos;
    }

    public void filtrarMenu() {
        if (filtroMenu == null || filtroMenu.length() == 0) {
            for (MenuGrupo menuGrupo : menuGrupos) {
                menuGrupo.setGrupoVisivel(true);
                menuGrupo.setTodosItensVisivel();
            }
        } else {
            String filtro = VirtualabUtils.removerAcentos(filtroMenu.toLowerCase());
            for (MenuGrupo menuGrupo : menuGrupos) {
                menuGrupo.setGrupoVisivel(false);
                if (menuGrupo.getTags().toLowerCase().contains(filtro)) {
                    menuGrupo.setGrupoVisivel(true);
                    menuGrupo.setTodosItensVisivel();
                } else {
                    for (MenuItem menuItem : menuGrupo.getMenuItens()) {
                        if (menuItem.getTags().toLowerCase().contains(filtro)) {
                            menuGrupo.setGrupoVisivel(true);
                            menuItem.setItemVisivel(true);
                        } else {
                            menuItem.setItemVisivel(false);
                        }
                    }
                }
            }

            //favoritos
            if (menuGrupos.get(0).getTags().toLowerCase().contains(filtro)) {
                menuGrupos.get(0).setGrupoVisivel(true);
                menuGrupos.get(0).setTodosItensVisivel();
            }
        }
    }

    public void favoritoAddOrRemove() {
        MenuFavorito menuFavorito = null;
        for (MenuFavorito menuFavoritoObj : menuFavoritos) {
            if (menuFavoritoObj.getMenuItem_id().equals(itemMenuSelecionado.getId())) {
                menuFavorito = menuFavoritoObj;
                break;
            }
        }
        buildMenuFavorito(menuFavorito, itemMenuSelecionado);
        inicializarFavoritos();

        if (itemMenuSelecionado.isFavorito()) {
            setMessageGrowl("Tela adicionada aos favoritos!");
        } else {
            setMessageGrowl("Tela removida dos favoritos!");
        }
    }

    private void buildMenuFavorito(MenuFavorito menuFavorito, MenuItem itemMenuSelecionado) {
        abstractMenuBuilder.build(entityManager, menuFavorito, itemMenuSelecionado, getUsuarioLogado());
    }

    private void carregarMenuFavorito() {
        menuFavoritos = abstractMenuFactory.buscarMenuFavoritoPorUsuario(entityManager, getUsuarioLogado().getId());
    }

    public void inicializarFavoritos() {
        carregarMenuFavorito();
        menuGrupos.get(0).setMenuItens(new ArrayList<MenuItem>());

        for (MenuGrupo menuGrupo : menuGrupos) {
            menuGrupo.setTodosItensNaoFavorito();
            for (MenuItem menuItem : menuGrupo.getMenuItens()) {
                for (MenuFavorito menuFavorito : menuFavoritos) {
                    if (menuFavorito.getMenuItem_id().equals(menuItem.getId())) {
                        addMenuItemFavoritos(menuItem);
                    }
                }
            }
        }
    }

    private void addMenuItemFavoritos(MenuItem menuItem) {
        menuItem.setFavorito(true);
        menuGrupos.get(0).getMenuItens().add(menuItem);
    }
    //Fim menu sistema
    //******************************************************

}
