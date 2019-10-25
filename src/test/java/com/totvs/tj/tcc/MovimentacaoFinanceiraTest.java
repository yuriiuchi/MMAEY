package com.totvs.tj.tcc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.javamoney.moneta.Money;
import org.junit.Test;

import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.MovimentacaoFinanceira;
import com.totvs.tj.tcc.domain.conta.MovimentacaoFinanceiraId;
import com.totvs.tj.tcc.domain.conta.TipoMovimentacaoFinanceira;

public class MovimentacaoFinanceiraTest {
    private final ContaId idContaCredito = ContaId.generate();
    private final ContaId idContaDebito= ContaId.generate();
    private final MovimentacaoFinanceiraId idMovimentacao= MovimentacaoFinanceiraId.generate();
    
    @Test
    public void aoCriarUmaMovimentacaoFinanceira() throws Exception {
        MovimentacaoFinanceira movimentacao = MovimentacaoFinanceira.builder()
                                              .id(idMovimentacao)
                                              .contaCredito(idContaCredito)
                                              .contaDebito(idContaDebito)
                                              .valor(Money.of(100.00,"BRL"))
                                              .tipo(TipoMovimentacaoFinanceira.transferencia)
                                              .build();                             
        assertNotNull(movimentacao);

        assertEquals(idMovimentacao, movimentacao.getId());
        assertEquals(idMovimentacao.toString(), movimentacao.getId().toString());
      
                                              
    }
}
