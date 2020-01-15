package br.com.virtualab.app.entity.usuario;

public enum RoleType {

    ROLE_ADMIN("Administrador"),
    ROLE_BASICO("Básico"),
    ROLE_SUSER("Super usuário");

    private final String descricao;

    public String getDescricao() {
        return descricao;
    }

    private RoleType(String descricao) {
        this.descricao = descricao;
    }
    
}
