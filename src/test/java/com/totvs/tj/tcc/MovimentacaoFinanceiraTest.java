package com.totvs.tj.tcc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.javamoney.moneta.Money;
import org.junit.Test;

import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.Empresa;
import com.totvs.tj.tcc.domain.conta.EmpresaId;
import com.totvs.tj.tcc.domain.conta.ExtratoConta;
import com.totvs.tj.tcc.domain.conta.GerenteId;
import com.totvs.tj.tcc.domain.conta.MovimentacaoFinanceira;
import com.totvs.tj.tcc.domain.conta.MovimentacaoFinanceiraId;
import com.totvs.tj.tcc.domain.conta.ResponsavelId;
import com.totvs.tj.tcc.domain.conta.StatusMovimentacaoFinanceira;
import com.totvs.tj.tcc.domain.conta.TipoMovimentacaoFinanceira;

public class MovimentacaoFinanceiraTest {
    private final ResponsavelId idResponsavel = ResponsavelId.generate();
    private final GerenteId idGerente = GerenteId.generate();
    private final ContaId idContaCredito = ContaId.generate();
    private final ContaId idContaDebito = ContaId.generate();
    private final EmpresaId idEmpresaCredito = EmpresaId.generate();
    private final EmpresaId idEmpresaDebito = EmpresaId.generate();
      
    private final Empresa empresaCredito = Empresa.builder()
            .id(idEmpresaCredito)
            .responsavel(idResponsavel)
            .CPNJ("48.206.442/0001-15")
            .valorDeMercado(Money.of(1000000.00, "BRL"))
            .quantidadeFuncionarios(10)
            .build();
    private final Empresa empresaDebito = Empresa.builder()
            .id(idEmpresaDebito)
            .responsavel(idResponsavel)
            .CPNJ("83.603.369/0001-16")
            .valorDeMercado(Money.of(1000000.00, "BRL"))
            .quantidadeFuncionarios(10)
            .build();      
    
    @Test
    public void aoCriarUmaMovimentacaoFinanceiraDeposito() throws Exception {
        Conta contaCredito = Conta.from(idContaCredito,empresaCredito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.deposito(idMovimentacao,Money.of(100.00,"BRL"), contaCredito);        
        
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(100.00,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.deposito, movimentacao.getTipo());
        assertEquals(contaCredito, movimentacao.getContaCredito());                                              
    }
    
    @Test
    public void aoCriarUmaMovimentacaoFinanceiraSaque() throws Exception {
        Conta contaDebito = Conta.from(idContaDebito,empresaDebito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.saque(idMovimentacao,Money.of(500.74,"BRL"), contaDebito);
        
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(500.74,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.saque, movimentacao.getTipo());
        assertEquals(contaDebito, movimentacao.getContaDebito());                                              
    }
    
    @Test
    public void aoCriarUmaMovimentacaoFinanceiraTransferencia() throws Exception {
        Conta contaDebito = Conta.from(idContaDebito,empresaDebito);
        Conta contaCredito = Conta.from(idContaCredito,empresaCredito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.transferencia(idMovimentacao,Money.of(304.65,"BRL"),contaDebito,contaCredito);
        
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(304.65,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.transferencia, movimentacao.getTipo());
        assertEquals(contaDebito, movimentacao.getContaDebito());                                              
    }    
    
    @Test
    public void aoMovimentarStatusDeUmaMovimentacaoFinanceiraSaque() throws Exception {
        
        Conta contaDebito = Conta.from(idContaDebito,empresaDebito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.saque(idMovimentacao,Money.of(500.74,"BRL"), contaDebito);

        assertNotNull(movimentacao);
       
        movimentacao.aguardarAprovacao();
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.aguardandoAprovacao);
        
        movimentacao.reprovar(idGerente);
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.recusada);
        assertEquals(idGerente.toString(), movimentacao.getGerente().toString());
        
        movimentacao.recusar();
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.recusada);
        
        movimentacao.aprovar(idGerente);
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.aprovada);
        assertEquals(idGerente.toString(), movimentacao.getGerente().toString());
        
        movimentacao.finalizar();
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.finalizada);
        
    }    
    
    @Test
    public void aoRealizarUmaMovimentacaoFinanceiraDeDeposito() throws Exception {
        Conta contaCredito = Conta.from(idContaCredito,empresaCredito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.deposito(idMovimentacao,Money.of(106.74,"BRL"), contaCredito);        
        
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(106.74,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.deposito, movimentacao.getTipo());
        assertEquals(contaCredito, movimentacao.getContaCredito());  
        
        movimentacao.realizar();
        
        assertEquals(contaCredito.getSaldo(), Money.of(106.74,"BRL"));
        assertEquals(StatusMovimentacaoFinanceira.finalizada, movimentacao.getStatus());
        
        List<ExtratoConta> extratoContaCredito = new ArrayList<ExtratoConta>();
        extratoContaCredito.add(ExtratoConta.from(Money.of(106.74, "BRL")));
        assertEquals(extratoContaCredito.get(0).toString(), contaCredito.getExtrato().get(0).toString());
        
        assertFalse(contaCredito.debitarSaldo(movimentacao));
    }
    
    @Test
    public void aoRealizarUmaMovimentacaoFinanceiraDeSaque() throws Exception {
        Conta contaDebito = Conta.from(idContaDebito,empresaDebito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.saque(idMovimentacao,Money.of(249.99,"BRL"), contaDebito);        
        
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(249.99,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.saque, movimentacao.getTipo());
        assertEquals(contaDebito, movimentacao.getContaDebito());
        
        movimentacao.realizar();
        
        assertEquals(contaDebito.getSaldo(), Money.of(249.99,"BRL").negate());
        assertEquals(StatusMovimentacaoFinanceira.finalizada, movimentacao.getStatus());
        
        List<ExtratoConta> extratoContaDebito = new ArrayList<ExtratoConta>();
        extratoContaDebito.add(ExtratoConta.from(Money.of(249.99, "BRL").negate()));
        assertEquals(extratoContaDebito.get(0).toString(), contaDebito.getExtrato().get(0).toString());
    }
    
    @Test
    public void aoRealizarUmaMovimentacaoFinanceiraDeTransferencia() throws Exception {
        Conta contaDebito = Conta.from(idContaDebito,empresaDebito);
        Conta contaCredito = Conta.from(idContaCredito,empresaCredito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.transferencia(idMovimentacao,Money.of(249.99,"BRL"),contaDebito,contaCredito);
                
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(249.99,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.transferencia, movimentacao.getTipo());
        assertEquals(contaDebito, movimentacao.getContaDebito());
        
        movimentacao.realizar();
        
        assertEquals(contaDebito.getSaldo(), Money.of(249.99,"BRL").negate());
        assertEquals(contaCredito.getSaldo(), Money.of(249.99,"BRL"));
        assertEquals(StatusMovimentacaoFinanceira.finalizada, movimentacao.getStatus());
        
        List<ExtratoConta> extratoContaDebito = new ArrayList<ExtratoConta>();
        extratoContaDebito.add(ExtratoConta.from(Money.of(249.99, "BRL").negate()));
        assertEquals(extratoContaDebito.get(0).toString(), contaDebito.getExtrato().get(0).toString());
        
        List<ExtratoConta> extratoContaCredito = new ArrayList<ExtratoConta>();
        extratoContaCredito.add(ExtratoConta.from(Money.of(249.99, "BRL")));
        assertEquals(extratoContaCredito.get(0).toString(), contaCredito.getExtrato().get(0).toString());
    }
    
    @Test
    public void aoTentarRealizarUmaMovimentacaoFinanceiraDeDepositoEmContaSuspensa() throws Exception {
        Conta contaCredito = Conta.from(idContaCredito,empresaCredito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.deposito(idMovimentacao,Money.of(106.74,"BRL"), contaCredito);        
        
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(106.74,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.deposito, movimentacao.getTipo());
        assertEquals(contaCredito, movimentacao.getContaCredito());  
        
        contaCredito.suspender();
        movimentacao.realizar();
        
        assertEquals(contaCredito.getSaldo(), Money.of(0.00,"BRL"));
        assertEquals(StatusMovimentacaoFinanceira.recusada, movimentacao.getStatus());
    }
    
    @Test
    public void aoTentarRealizarUmaMovimentacaoFinanceiraDeSaqueEmContaSuspensa() throws Exception {
        Conta contaDebito = Conta.from(idContaDebito,empresaDebito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.saque(idMovimentacao,Money.of(1000.01,"BRL"), contaDebito);        
        
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(1000.01,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.saque, movimentacao.getTipo());
        assertEquals(contaDebito, movimentacao.getContaDebito());
        
        contaDebito.suspender();
        movimentacao.realizar();
        
        assertEquals(contaDebito.getSaldo(), Money.of(0,"BRL"));
        assertEquals(StatusMovimentacaoFinanceira.recusada, movimentacao.getStatus());
    }
    
    @Test
    public void aoTentarRealizarUmaMovimentacaoFinanceiraDeSaqueAcimaDoLimite() throws Exception {
        Conta contaDebito = Conta.from(idContaDebito,empresaDebito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.saque(idMovimentacao,Money.of(1000.01,"BRL"), contaDebito);        
        
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(1000.01,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.saque, movimentacao.getTipo());
        assertEquals(contaDebito, movimentacao.getContaDebito());
        
        movimentacao.realizar();
        
        assertEquals(contaDebito.getSaldo(), Money.of(0,"BRL"));
        assertEquals(StatusMovimentacaoFinanceira.recusada, movimentacao.getStatus());
    }
    
    @Test
    public void aoRealizarUmaMovimentacaoFinanceiraDeSaqueComValorMaiorQue25PorcentoDoLimiteComAprovacao() throws Exception {
        Conta contaDebito = Conta.from(idContaDebito,empresaDebito);
        MovimentacaoFinanceiraId idMovimentacao = MovimentacaoFinanceiraId.generate();
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.saque(idMovimentacao,Money.of(250.01,"BRL"), contaDebito);        
        
        assertNotNull(movimentacao);
        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
        assertEquals(Money.of(250.01,"BRL"), movimentacao.getValor());
        assertEquals(TipoMovimentacaoFinanceira.saque, movimentacao.getTipo());
        assertEquals(contaDebito, movimentacao.getContaDebito());
        
        movimentacao.realizar();
        assertEquals(StatusMovimentacaoFinanceira.aguardandoAprovacao, movimentacao.getStatus());
        
        movimentacao.aprovar(idGerente);
        movimentacao.realizar();
        
        assertEquals(contaDebito.getSaldo(), Money.of(250.01,"BRL").negate());
        assertEquals(StatusMovimentacaoFinanceira.finalizada, movimentacao.getStatus());        
        
        List<ExtratoConta> extratoContaDebito = new ArrayList<ExtratoConta>();
        extratoContaDebito.add(ExtratoConta.from(Money.of(250.01, "BRL").negate()));
        assertEquals(extratoContaDebito.get(0).toString(), contaDebito.getExtrato().get(0).toString());
    }
    
}
