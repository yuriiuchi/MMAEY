package com.totvs.tj.tcc.domain.conta;

import org.javamoney.moneta.Money;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class Empresa {
    private EmpresaId id;
    private String CPNJ;
    private int quantidadeFuncionarios;
    private Money valorDeMercado;
    private ResponsavelId responsavel;
    
    public int getQuantidadeFuncionarios() {
        return quantidadeFuncionarios;
    }
    public Money getValorDeMercado() {
        return valorDeMercado;
    }
       
}
