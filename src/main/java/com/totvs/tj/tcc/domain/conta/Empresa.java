package com.totvs.tj.tcc.domain.conta;

import org.javamoney.moneta.Money;

import br.com.caelum.stella.validation.CNPJValidator;
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
    
    private ContaId contaId;
    
    public void validarCNPJ(String cnpj) {
       CNPJValidator cnpjValidator = new CNPJValidator();
       cnpjValidator.assertValid(cnpj);
    }
    public int getQuantidadeFuncionarios() {
        return quantidadeFuncionarios;
    }
    public Money getValorDeMercado() {
        return valorDeMercado;
    }
    
    public static Empresa empty() {
    	return Empresa.builder()
                .id(null)
                .CPNJ("")
                .valorDeMercado(Money.of(0, "BRL"))
                .quantidadeFuncionarios(-1)
                .build();
    }
       
}
