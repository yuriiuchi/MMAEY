package com.totvs.tj.tcc.domain.conta;

import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.ABERTO;
import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.SUSPENSO;
import static lombok.AccessLevel.PRIVATE;

import java.util.ArrayList;
import java.util.List;

import org.javamoney.moneta.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = PRIVATE)
public class Conta {

    private ContaId id;
    
    private Empresa empresa;
    
    private Situacao situacao;
    
    private Money saldo;
    
    private Money limite;
    
    private List<AlteracaoLimite> alteracaoLimite;
    
    private List<ExtratoConta> extratoConta;
    
    private Conta(ContaId id,Empresa empresa){
        this.saldo = Money.of(0, "BRL");
        this.situacao = ABERTO;
        this.empresa = empresa;
        this.id = id;
        this.limite = this.calcularLimite();
        this.extratoConta = new ArrayList<ExtratoConta>();
        this.alteracaoLimite = new ArrayList<AlteracaoLimite>();
    }
    
    public static Conta from(ContaId id,Empresa empresa) {
        return new Conta(id,empresa);
    }
    
    public void suspender() {
        situacao = SUSPENSO;
    }

    public boolean isDisponivel() {
        return ABERTO.equals(situacao);
    }
    
    public void debitarSaldo(Money valor) {
        this.saldo = this.saldo.subtract(valor);
        this.extratoConta.add(ExtratoConta.from(valor.negate()));
    }
    
    public void creditarSaldo(Money valor) {
        this.saldo = this.saldo.add(valor);
        this.extratoConta.add(ExtratoConta.from(valor));
    }
    
    public void aumentarLimte() {
        if (this.aumentoDeLimiteDisponivel()) {
            this.limite = this.limite.add(this.limite.multiply(0.50));
            this.alteracaoLimite.add(AlteracaoLimite.from(this.limite));
        }
    }
    
    public boolean aumentoDeLimiteDisponivel() {
        return this.alteracaoLimite.isEmpty();
    }
    
    public Money getLimite() {
        return this.limite;
    }
    
    public List<ExtratoConta> getExtrato(){
        return this.extratoConta;
    }
    
    public List<AlteracaoLimite> getAlteracaoLimite(){
        return this.alteracaoLimite;
    }
    
    public Money calcularLimite() {
        return (((this.empresa.getValorDeMercado().divide(this.empresa.getQuantidadeFuncionarios()))
                .divide(15000))
                .divide(100))
                .multiply(15000);
    }
    
    static enum Situacao {

        ABERTO, SUSPENSO;

    }
    
    public static Conta empty() {
		//return new Conta(null, null, Money.of(0, "BRL"), Money.of(0, "BRL"));
    	return null;
	}
    
    private boolean excedeuLimit(MovimentacaoFinanceira movimentacaoFinanceira) {
		if (movimentacaoFinanceira.getValor()
				.isGreaterThan(this.getSaldo()
				.add(this.getLimite()))) {
			
			movimentacaoFinanceira.recusar();
			return true;
		}
		return false;
	}
	
	private boolean excedeuLimitSemAprovacao(MovimentacaoFinanceira movimentacaoFinanceira) {
		
		if ((movimentacaoFinanceira.getValor()
				.subtract(this.saldo).isGreaterThan(this.getLimite().multiply(0.25)))){
		
			return true;
		}
		return false;
	}
	
	public boolean debitarSaldo(MovimentacaoFinanceira movimentacaoFinanceira) {
		
		//if (conta.getSituacao().equals(Situacao.suspensa)) {
		
		if (!movimentacaoFinanceira.getStatus().equals(StatusMovimentacaoFinanceira.finalizada)){
			return false;
		}
		
		if (excedeuLimit(movimentacaoFinanceira)) {
			return false;
		}
		
		if (!movimentacaoFinanceira.getStatus().equals(StatusMovimentacaoFinanceira.aprovada)){
			if (excedeuLimitSemAprovacao(movimentacaoFinanceira)){
				return false;
			}
		}
		
		this.saldo = this.saldo.subtract(movimentacaoFinanceira.getValor());
		movimentacaoFinanceira.finalizar();
		return true;
	}
	
	public boolean creditarSaldo(MovimentacaoFinanceira movimentacaoFinanceira) {
		
		if (this.creditarSaldo(movimentacaoFinanceira)) {
				return false;
		}
		this.saldo = this.saldo.add(movimentacaoFinanceira.getValor());
		movimentacaoFinanceira.finalizar();
		return true;
	}
    
}
