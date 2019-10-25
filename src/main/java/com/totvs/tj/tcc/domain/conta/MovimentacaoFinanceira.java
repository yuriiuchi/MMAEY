package com.totvs.tj.tcc.domain.conta;

import org.javamoney.moneta.Money;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
//@AllArgsConstructor(access = PRIVATE)
public class MovimentacaoFinanceira {
    private MovimentacaoFinanceiraId id;
    private ContaId contaCredito;
    private ContaId contaDebito;
    private Money valor;
    private GerenteId gerente;
    private StatusMovimentacaoFinanceira status;
    private TipoMovimentacaoFinanceira tipo;


public void Aprovar(GerenteId gerente) {
    if (!this.status.equals(StatusMovimentacaoFinanceira.realizado)) {
     this.status = StatusMovimentacaoFinanceira.aprovado;
  }
}

public void Reprovar(GerenteId gerente) {
    if (!this.status.equals(StatusMovimentacaoFinanceira.realizado)) {
       this.status = StatusMovimentacaoFinanceira.recusado;
    }  
  }
}

enum StatusMovimentacaoFinanceira{
    aguardandoAprovacao, recusado, aprovado, realizado;
}


