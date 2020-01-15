package br.com.virtualab.app.entity.menu;

import br.com.virtualab.app.entity.usuario.Usuario;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "menuItem_id"}))
public class MenuFavorito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Transient
    private final int menuItem_IdSize = 50;
    @Column(length = menuItem_IdSize, nullable = false)
    private String menuItem_id;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((usuario.getId() != null) ? usuario.getId().hashCode() : 0);
        result = prime * result + ((menuItem_id != null) ? menuItem_id.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MenuFavorito other = (MenuFavorito) obj;
        if ((usuario.getId() == null) ? (other.usuario.getId() != null) : !usuario.getId().equals(other.usuario.getId())) {
            return false;
        }
        if ((menuItem_id == null) ? (other.menuItem_id != null) : !menuItem_id.equals(other.menuItem_id)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMenuItem_id() {
        return menuItem_id;
    }

    public void setMenuItem_id(String menuItem_id) {
        this.menuItem_id = menuItem_id;
    }

}
