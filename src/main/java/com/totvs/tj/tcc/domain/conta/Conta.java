package com.totvs.tj.tcc.domain.conta;

import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.ABERTO;
import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.SUSPENSO;
import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import org.javamoney.moneta.Money;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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
    }
    
    public void creditarSaldo(Money valor) {
        this.saldo = this.saldo.add(valor);
    }
    
    public void aumentarLimte(Money valor) {
        this.limite = this.limite.add(valor);
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
    
}
