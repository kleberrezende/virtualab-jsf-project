package br.com.virtualab.app.controller.cadastrousuario;

import br.com.virtualab.app.controller.Abstract.AbstractController;
import br.com.virtualab.app.controller.converter.PerfilConverterJsf;
import br.com.virtualab.app.controller.principal.AbstractPrincipalController;
import br.com.virtualab.app.entity.usuario.Perfil;
import br.com.virtualab.app.entity.usuario.RoleType;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.app.service.principal.PrincipalFactory;
import br.com.virtualab.app.service.usuario.UsuarioBuilder;
import br.com.virtualab.app.service.usuario.UsuarioFactory;
import br.com.virtualab.utils.VirtualabUtils;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ActionEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;

public class CadastroUsuarioController extends AbstractController {

    /*inicio variaveis padrão sistema*/
    private static final long serialVersionUID = 1L;
    private boolean inicializado = false;
    private boolean acessoPermitidoTela = false;
    @Autowired
    private PrincipalFactory principalFactory;
    /*fim variaveis padrão sistema*/

    private Usuario usuario;
    private Usuario usuarioSize;
    private boolean localizarRendered = false;
    private List<Usuario> usuarioListLocalizar = new ArrayList<Usuario>();
    private Usuario usuarioLocalizar = new Usuario();
    private DualListModel<Perfil> perfilDualList = new DualListModel<Perfil>();
    private List<Perfil> perfilList = new ArrayList<Perfil>();
    private boolean alterarSenha = true;

    @Autowired
    private UsuarioFactory usuarioFactory;

    @Autowired
    private UsuarioBuilder usuarioBuilder;

    @Autowired
    private AbstractPrincipalController abstractPrincipalController;

    /*inicio padrão sistema*/
    public void inicializar() {
        if (inicializado == false) {
            novoObj();
            novoObjConsulta();
            novoObjSize();
            localizarRendered = false;
            acessoPermitidoTela = principalFactory.isPermitirAcessoUsuario(getPermissaoPerfil());
            inicializado = true;
        }
    }

    public String getPermissaoPerfil() {
        return RoleType.ROLE_ADMIN.name();
    }

    public boolean isAcessoPermitidoTela() {
        return acessoPermitidoTela;
    }

    public void novoObj() {
        usuario = new Usuario();
        alterarSenha = true;
        perfilList = usuarioFactory.buscarListaPerfil(abstractPrincipalController.getEntityManager());

        PerfilConverterJsf.perfilList = perfilList;
        List<Perfil> perfilSource = new ArrayList<Perfil>();
        perfilSource.addAll(perfilList);
        List<Perfil> perfilTarget = new ArrayList<Perfil>();
        perfilDualList = new DualListModel<Perfil>(perfilSource, perfilTarget);
    }

    public void novoObjConsulta() {
        usuarioListLocalizar = new ArrayList<Usuario>();
        usuarioLocalizar = new Usuario();
    }

    private void novoObjSize() {
        usuarioSize = new Usuario();
    }

    public void carregarObj(Usuario usuario) {
        List<Perfil> perfilSource = new ArrayList<Perfil>();
        List<Perfil> perfilTarget = new ArrayList<Perfil>();
        perfilDualList = new DualListModel<Perfil>(perfilSource, perfilTarget);

        perfilTarget.addAll(usuario.getPerfilList());
        for (Perfil perfil : perfilList) {
            if (!perfilTarget.contains(perfil)) {
                perfilSource.add(perfil);
            }
        }

        this.usuario = usuario;
        alterarSenha = false;
    }
    /*fim padrão sistema*/

    public void salvar() {
        try {
            usuarioBuilder.build(abstractPrincipalController.getEntityManager(), usuario,
                    perfilDualList.getTarget(), -1, alterarSenha);
            Long id = usuario.getId();
            novoObj();
            carregarObj(usuarioFactory.buscarUsuarioPorId(abstractPrincipalController.getEntityManager(), id));
            setMessageGrowl("Usuário salvo com sucesso!");
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    public void limpar() {
        novoObj();
    }

    public void iniciarConsulta() {
        novoObjConsulta();
        localizarRendered = true;
    }

    public void localizar() {
        if (localizarRendered) {
            if (!VirtualabUtils.stringIsEmpty(usuarioLocalizar.getName())
                    || !VirtualabUtils.stringIsEmpty(usuarioLocalizar.getUsername())) {
                usuarioListLocalizar = usuarioFactory.buscarListaUsuarios(abstractPrincipalController.getEntityManager(), usuarioLocalizar.getName(), usuarioLocalizar.getUsername(), null);
            } else {
                setMessageGrowl("Por favor refine mais sua busca.");
            }
        }
    }

    public void editar(ActionEvent event) {
        Long id = ((Usuario) event.getComponent().getAttributes().get("obj")).getId();
        carregarObj(usuarioFactory.buscarUsuarioPorId(abstractPrincipalController.getEntityManager(), id));
    }

    public List<Usuario> getLocalizarDataSet() {
        return usuarioListLocalizar;
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

    public boolean isLocalizarRendered() {
        return localizarRendered;
    }

    public void setLocalizarRendered(boolean searchRendered) {
        this.localizarRendered = searchRendered;
    }

    public List<Usuario> getUsuarioListLocalizar() {
        return usuarioListLocalizar;
    }

    public void setUsuarioListLocalizar(List<Usuario> usuarioListLocalizar) {
        this.usuarioListLocalizar = usuarioListLocalizar;
    }

    public Usuario getUsuarioLocalizar() {
        return usuarioLocalizar;
    }

    public void setUsuarioLocalizar(Usuario usuarioLocalizar) {
        this.usuarioLocalizar = usuarioLocalizar;
    }

    public DualListModel<Perfil> getPerfilDualList() {
        return perfilDualList;
    }

    public void setPerfilDualList(DualListModel<Perfil> perfilDualList) {
        this.perfilDualList = perfilDualList;
    }

    public List<Perfil> getPerfilList() {
        return perfilList;
    }

    public void setPerfilList(List<Perfil> perfilList) {
        this.perfilList = perfilList;
    }

    public boolean isAlterarSenha() {
        return alterarSenha;
    }

    public void setAlterarSenha(boolean alterarSenha) {
        this.alterarSenha = alterarSenha;
    }

}
