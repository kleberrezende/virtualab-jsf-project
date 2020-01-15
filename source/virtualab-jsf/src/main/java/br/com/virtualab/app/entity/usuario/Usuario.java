package br.com.virtualab.app.entity.usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.virtualab.app.entity.Abstract.AbstractEntity;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Usuario extends AbstractEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Transient
    private final int usernameSize = 21;
    @Column(length = usernameSize, nullable = false)
    private String username;

    @Transient
    private final int nameSize = 40;
    @Column(length = nameSize, nullable = false)
    private String name;

    @Transient
    private final int passwordSize = 15;
    @Column(length = 130, nullable = false)
    private String password;

    @Transient
    private String confirmarPassword;

    @Basic
    private boolean enabled = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UsuarioPerfil", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "perfil_id"))
    private List<Perfil> perfilList = new ArrayList<Perfil>();

    @Transient
    private final int emailSize = 50;
    @Column(length = emailSize, nullable = false)
    private String email;

    @Column(length = 15, nullable = true)
    private String codigoRenovarSenha;

    @Column(length = 130, nullable = true)
    private String passwordRenovar;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username != null) ? username.hashCode() : 0);
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
        Usuario other = (Usuario) obj;
        if ((username == null) ? (other.username != null) : !username.equals(other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("#%d %s", getId(), getUsername());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Perfil> getPerfilList() {
        return perfilList;
    }

    public void setPerfilList(List<Perfil> perfilList) {
        this.perfilList = perfilList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigoRenovarSenha() {
        return codigoRenovarSenha;
    }

    public void setCodigoRenovarSenha(String codigoRenovarSenha) {
        this.codigoRenovarSenha = codigoRenovarSenha;
    }

    public String getPasswordRenovar() {
        return passwordRenovar;
    }

    public void setPasswordRenovar(String passwordRenovar) {
        this.passwordRenovar = passwordRenovar;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        for (Perfil perfil : getPerfilList()) {
            list.add(new GrantedAuthorityImpl(perfil.getAuthority()));
        }
        return list;
    }

    public int getUsernameSize() {
        return usernameSize;
    }

    public int getNameSize() {
        return nameSize;
    }

    public int getPasswordSize() {
        return passwordSize;
    }

    public int getEmailSize() {
        return emailSize;
    }

}
