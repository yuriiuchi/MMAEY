package com.totvs.tj.tcc.domain.conta;

import java.time.LocalDate;

import org.javamoney.moneta.Money;

import lombok.ToString;

@ToString
public class ExtratoConta {
    private Money valor;
    private LocalDate data;

    private ExtratoConta(Money valor, LocalDate data) {
        this.valor = valor;
        this.data = data;
    }

    public static ExtratoConta from (Money valor) {
        return new ExtratoConta(valor, LocalDate.now());
    }
    
}
