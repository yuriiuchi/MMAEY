package com.totvs.tj.tcc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.javamoney.moneta.Money;
import org.junit.Test;

import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.GerenteId;
import com.totvs.tj.tcc.domain.conta.MovimentacaoFinanceira;
import com.totvs.tj.tcc.domain.conta.MovimentacaoFinanceiraId;
import com.totvs.tj.tcc.domain.conta.StatusMovimentacaoFinanceira;
import com.totvs.tj.tcc.domain.conta.TipoMovimentacaoFinanceira;

public class MovimentacaoFinanceiraTest {
    private final ContaId idContaCredito = ContaId.generate();
    private final ContaId idContaDebito= ContaId.generate();
    private final MovimentacaoFinanceiraId idMovimentacao= MovimentacaoFinanceiraId.generate();
    private final GerenteId idGerente = GerenteId.generate();
    
    @Test
    public void aoCriarUmaMovimentacaoFinanceira() throws Exception {
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.builder()
                                              .id(idMovimentacao)
                                              .contaCredito(idContaCredito)
                                              .contaDebito(idContaDebito)
                                              .valor(Money.of(100.00,"BRL"))
                                              .tipo(TipoMovimentacaoFinanceira.deposito)
                                              .build();                             
        assertNotNull(movimentacao);

        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());      
                                              
    }
    @Test
    public void aoMovimentarStatusDeUmaMovimentacaoFinanceira() throws Exception {
        
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.builder()
                                              .id(idMovimentacao)
                                              .contaCredito(idContaCredito)
                                              .contaDebito(idContaDebito)
                                              .valor(Money.of(100.00,"BRL"))
                                              .tipo(TipoMovimentacaoFinanceira.deposito)
                                              .build();
                
                
        assertNotNull(movimentacao);
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.iniciada);
       
        movimentacao.aguardarAprovacao();
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.aguardandoAprovacao);
        
        movimentacao.Reprovar(idGerente);
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.recusada);
        assertEquals(idGerente.toString(), movimentacao.getGerente().toString());
        
        movimentacao.recusar();
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.recusada);
        
        movimentacao.Aprovar(idGerente);
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.aprovada);
        assertEquals(idGerente.toString(), movimentacao.getGerente().toString());
        
        movimentacao.finalizar();
        assertEquals(movimentacao.getStatus(),StatusMovimentacaoFinanceira.finalizada);
        
       
    }    
    
}
