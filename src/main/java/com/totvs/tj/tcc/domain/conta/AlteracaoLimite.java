package com.totvs.tj.tcc.domain.conta;

import java.time.LocalDate;

import org.javamoney.moneta.Money;

public class AlteracaoLimite<List> {
    private Money valor;
    private LocalDate dataOperacao;
    
    private AlteracaoLimite() {}
}
