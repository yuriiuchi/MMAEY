package com.totvs.tj.tcc.domain.conta;

import java.time.LocalDate;

import org.javamoney.moneta.Money;

public class AlteracaoLimite {
    private Money valor;
    private LocalDate data;
    
    private AlteracaoLimite(Money valor, LocalDate data) {
        this.valor = valor;
        this.data = data;
    }

    public static AlteracaoLimite from (Money valor) {
        return new AlteracaoLimite(valor, LocalDate.now());
    }
    
    
}
