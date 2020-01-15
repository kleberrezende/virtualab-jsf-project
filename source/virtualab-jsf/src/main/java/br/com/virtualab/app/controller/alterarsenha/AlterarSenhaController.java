package br.com.virtualab.app.controller.alterarsenha;

import br.com.virtualab.app.controller.Abstract.AbstractController;
import br.com.virtualab.app.controller.principal.AbstractPrincipalController;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.app.service.usuario.UsuarioBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class AlterarSenhaController extends AbstractController {

    /*inicio variaveis padrão sistema*/
    private static final long serialVersionUID = 1L;
    private boolean inicializado = false;
    private boolean acessoPermitidoTela = false;
    /*fim variaveis padrão sistema*/

    private Usuario usuario;
    private Usuario usuarioSize;

    @Autowired
    private UsuarioBuilder usuarioBuilder;

    @Autowired
    private AbstractPrincipalController abstractPrincipalController;
    
    /*inicio padrão sistema*/
    public void inicializar() {
        if (inicializado == false) {
            carregarObj();
            novoObjSize();
            acessoPermitidoTela = true;
            inicializado = true;
        }
    }

    public boolean isAcessoPermitidoTela() {
        return acessoPermitidoTela;
    }

    private void novoObjSize() {
        usuarioSize = new Usuario();
    }

    public void carregarObj() {
        this.usuario = getUsuarioLogado();
    }
    /*fim padrão sistema*/

    public void salvar() {
        try {
            usuarioBuilder.buildAlterarSenha(abstractPrincipalController.getEntityManager(), usuario);
            setMessageGrowl("Usuário salvo com sucesso!");
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioSize() {
        return usuarioSize;
    }

    public void setUsuarioSize(Usuario usuarioSize) {
        this.usuarioSize = usuarioSize;
    }

}
