package br.com.virtualab.app.entity.menu;

import java.io.Serializable;

public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String displayLabel;

    private String iconeMenu;

    private String link;

    private String tags;
    
    private boolean itemVisivel = false;

    private boolean itemPermissao = false;
    
    private boolean favorito = false;
    
    public MenuItem() {

    }

    public MenuItem(String id, String displayLabel, String iconeMenu, String link, String tags, boolean itemVisivel, boolean itemPermissao) {
        this.id = id;
        this.displayLabel = displayLabel;
        this.iconeMenu = iconeMenu;
        this.link = link;
        this.tags = tags;
        this.itemVisivel = itemVisivel;
        this.itemPermissao = itemPermissao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getIconeMenu() {
        return iconeMenu;
    }

    public void setIconeMenu(String iconeMenu) {
        this.iconeMenu = iconeMenu;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isItemVisivel() {
        return itemVisivel;
    }

    public void setItemVisivel(boolean itemVisivel) {
        this.itemVisivel = itemVisivel;
    }

    public boolean isItemPermissao() {
        return itemPermissao;
    }

    public void setItemPermissao(boolean itemPermissao) {
        this.itemPermissao = itemPermissao;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

}
