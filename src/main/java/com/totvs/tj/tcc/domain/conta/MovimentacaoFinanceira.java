package com.totvs.tj.tcc.domain.conta;

import org.javamoney.moneta.Money;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MovimentacaoFinanceira {
    private MovimentacaoFinanceiraId id;
    private Conta contaCredito;
    private Conta contaDebito;
    private Money valor;
    private GerenteId gerente;
    private StatusMovimentacaoFinanceira status;
    private TipoMovimentacaoFinanceira tipo;

    public void aprovar(GerenteId gerente) {
        this.status = StatusMovimentacaoFinanceira.aprovada;
        this.gerente = gerente;
    }

    public void reprovar(GerenteId gerente) {
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
    
    
    //public MovimentacaoFinanceira(MovimentacaoFinanceiraId id, Money valor, TipoMovimentacaoFinanceira tipo, Conta contaCredito, Conta contaDebito) {
    private MovimentacaoFinanceira(MovimentacaoFinanceiraId id, Money valor, TipoMovimentacaoFinanceira tipo, Conta conta) {
		this.id = id;	
		this.contaCredito = (tipo == TipoMovimentacaoFinanceira.deposito ? conta: Conta.empty());
		this.contaDebito  = (tipo == TipoMovimentacaoFinanceira.saque ? conta: Conta.empty());
		this.valor = valor;
		this.tipo = tipo;
		this.status = StatusMovimentacaoFinanceira.aguardandoAprovacao;   
	}
    
    private MovimentacaoFinanceira(MovimentacaoFinanceiraId id, Money valor, Conta contaCredito, Conta contaDebito) {
		this.id = id;
        this.contaCredito = contaCredito;
		this.contaDebito = contaDebito;
		this.valor = valor;
		this.tipo = TipoMovimentacaoFinanceira.transferencia;
		this.status = StatusMovimentacaoFinanceira.aguardandoAprovacao;   
	}
	
    public static MovimentacaoFinanceira saque(MovimentacaoFinanceiraId id, Money valor, Conta conta) {    	    	
    	MovimentacaoFinanceira movimentacaoFinaceira = new MovimentacaoFinanceira(id, valor, 
    			TipoMovimentacaoFinanceira.saque, conta);
   	
    	return movimentacaoFinaceira;
    }
    
    public static MovimentacaoFinanceira deposito(MovimentacaoFinanceiraId id, Money valor, Conta conta) {    	
    	MovimentacaoFinanceira movimentacaoFinaceira = new MovimentacaoFinanceira(id,valor, 
    			TipoMovimentacaoFinanceira.deposito, conta);
      	
    	return movimentacaoFinaceira;
    }
    
    public static MovimentacaoFinanceira transferencia(MovimentacaoFinanceiraId id, Money valor, Conta contaDebito, Conta contaCredito) {
    	MovimentacaoFinanceira movimentacaoFinaceira = new MovimentacaoFinanceira(id,valor, 
    			contaCredito, contaDebito);
    	
    	return movimentacaoFinaceira;
    }
    
    public void realizar() {
    	if (this.tipo.equals(TipoMovimentacaoFinanceira.deposito)) {
    	    this.contaCredito.creditarSaldo(this);
    	}
    	
    	if (this.tipo.equals(TipoMovimentacaoFinanceira.saque)) {
            this.contaDebito.debitarSaldo(this);
        }
    	
    	if (this.tipo.equals(TipoMovimentacaoFinanceira.transferencia)) {
            if (this.contaDebito.debitarSaldo(this)){
                this.contaCredito.creditarSaldo(this);
            }
        }    	
    }
}