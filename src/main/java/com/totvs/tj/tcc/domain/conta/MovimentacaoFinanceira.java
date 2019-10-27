package com.totvs.tj.tcc.domain.conta;

import org.javamoney.moneta.Money;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MovimentacaoFinanceira {
    private MovimentacaoFinanceiraId id;
    private ContaId contaCredito;
    private ContaId contaDebito;
    private Money valor;
    private GerenteId gerente;
    @Builder.Default
    private StatusMovimentacaoFinanceira status = StatusMovimentacaoFinanceira.iniciada;
    private TipoMovimentacaoFinanceira tipo;

    public void Aprovar(GerenteId gerente) {
        this.status = StatusMovimentacaoFinanceira.aprovada;
        this.gerente = gerente;
    }

    public void Reprovar(GerenteId gerente) {
        this.status = StatusMovimentacaoFinanceira.recusada;
        this.gerente = gerente;
    }

    public void recusar() {
        this.status = StatusMovimentacaoFinanceira.recusada;
    }

    public void aguardarAprovacao() {
        this.status = StatusMovimentacaoFinanceira.aguardandoAprovacao;
    }

    public void finalizar() {
        this.status = StatusMovimentacaoFinanceira.finalizada;
    }

}