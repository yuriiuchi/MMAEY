package com.totvs.tj.tcc.domain.conta;

import org.javamoney.moneta.Money;

import br.com.caelum.stella.validation.CNPJValidator;
import lombok.Builder;
import lombok.ToString;

@Builder(builderClassName = "Builder",buildMethodName = "build")
@ToString
public class Empresa {
    private EmpresaId id;
    private String CPNJ;
    private int quantidadeFuncionarios;
    private Money valorDeMercado;
    private ResponsavelId responsavel;
    private ContaId contaId;
    
//    public void validarCNPJ(String cnpj) {
//       CNPJValidator cnpjValidator = new CNPJValidator();
//       cnpjValidator.assertValid(cnpj);
//    }
    public int getQuantidadeFuncionarios() {
        return quantidadeFuncionarios;
    }
    public Money getValorDeMercado() {
        return valorDeMercado;
    }
    
    public static Empresa empty() {
    	return Empresa.builder()
                .id(null)
                .CPNJ("52.211.237/0001-15")
                .valorDeMercado(Money.of(0, "BRL"))
                .quantidadeFuncionarios(-1)
                .build();
    }
    
    public static class Builder {
        private String CPNJ;
        
        public Empresa build() {
            if (CPNJInvalido(this.CPNJ)) {
//                throw new IllegalArgumentException("Invalid CNPJ.");
            }
            return new Empresa(id, CPNJ, quantidadeFuncionarios, valorDeMercado, responsavel, contaId);
        }
        
        public boolean CPNJInvalido(String CNPJ) {
            CNPJValidator cnpjValidator = new CNPJValidator();
            try{ 
                cnpjValidator.assertValid(CNPJ); 
                return false; 
            }catch(Exception e){ 
                e.printStackTrace(); 
                return false; 
            } 
        }
    }
       
}
