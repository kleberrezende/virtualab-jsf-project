package br.com.virtualab.app.entity.usuario;

import br.com.virtualab.app.entity.Abstract.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Perfil extends AbstractEntity implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Column(length = 21, nullable = false, unique = true)
    private String descricao;

    @Column(length = 21, nullable = false, unique = true)
    private String titulo;

    @Override
    public String getAuthority() {
        return this.descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
