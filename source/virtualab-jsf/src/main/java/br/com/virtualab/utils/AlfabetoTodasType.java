package br.com.virtualab.utils;

public enum AlfabetoTodasType {

    TODAS("Todas"),
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G"),
    H("H"),
    I("I"),
    J("J"),
    K("K"),
    L("L"),
    M("M"),
    N("N"),
    O("O"),
    P("P"),
    Q("Q"),
    R("R"),
    S("S"),
    T("T"),
    U("U"),
    V("V"),
    W("W"),
    X("X"),
    Y("Y"),
    Z("Z");

    private final String descricao;

    public String getDescricao() {
        return descricao;
    }

    private AlfabetoTodasType(String descricao) {
        this.descricao = descricao;
    }
    
}
