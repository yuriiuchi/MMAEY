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
    private Conta contaCredito;
    private Conta contaDebito;
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
    
    public MovimentacaoFinanceira(Money valor, TipoMovimentacaoFinanceira tipo, Conta contaCredito, Conta contaDebito) {
		this.contaCredito = contaCredito;
		this.contaDebito = contaDebito;
		this.valor = valor;
		this.tipo = tipo;
		this.status = StatusMovimentacaoFinanceira.aguardandoAprovacao;   
	}
	
    public static MovimentacaoFinanceira saque(Money valor, Conta conta) {    	    	
    	MovimentacaoFinanceira movimentacaoFinaceira = new MovimentacaoFinanceira(valor, 
    			TipoMovimentacaoFinanceira.saque, Conta.empty(), conta);
   	
    	return movimentacaoFinaceira;
    }
    
    public static MovimentacaoFinanceira deposito(Money valor, Conta conta) {    	
    	MovimentacaoFinanceira movimentacaoFinaceira = new MovimentacaoFinanceira(valor, 
    			TipoMovimentacaoFinanceira.deposito, conta, Conta.empty());
      	
    	return movimentacaoFinaceira;
    }
    
    public static MovimentacaoFinanceira transferencia(Money valor, Conta contaDebito, Conta contaCredito) {
    	MovimentacaoFinanceira movimentacaoFinaceira = new MovimentacaoFinanceira(valor, 
    			TipoMovimentacaoFinanceira.deposito, contaCredito, contaDebito);
    	
    	return movimentacaoFinaceira;
    }
    
    public void realizar() {
    	
    	if (this.contaDebito.debitarSaldo(this)){
    		this.status =  StatusMovimentacaoFinanceira.finalizada;
    	
    	
    		this.contaCredito.creditarSaldo(this);
    	}
    	
    }

}