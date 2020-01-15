package br.com.virtualab.app.entity.menu;

import java.io.Serializable;
import java.util.List;

public class MenuGrupo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String grupo;

    private String tags;

    private boolean grupoVisivel = false;

    private List<MenuItem> menuItens;

    public MenuGrupo() {

    }

    public MenuGrupo(String grupo, String tags, boolean grupoVisivel, List<MenuItem> menuItens) {
        this.grupo = grupo;
        this.tags = tags;
        this.grupoVisivel = grupoVisivel;
        this.menuItens = menuItens;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public List<MenuItem> getMenuItens() {
        return menuItens;
    }

    public void setMenuItens(List<MenuItem> menuItens) {
        this.menuItens = menuItens;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isGrupoVisivel() {
        return grupoVisivel;
    }

    public void setGrupoVisivel(boolean grupoVisivel) {
        this.grupoVisivel = grupoVisivel;
    }

    public void setTodosItensVisivel() {
        for (MenuItem menuItem : getMenuItens()) {
            menuItem.setItemVisivel(true);
        }
    }
    
    public void setTodosItensNaoFavorito() {
        for (MenuItem menuItem : getMenuItens()) {
            menuItem.setFavorito(false);
        }
    }

}
