package com.totvs.tj.tcc;

import org.javamoney.moneta.Money;

import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.Empresa;
import com.totvs.tj.tcc.domain.conta.EmpresaId;
import com.totvs.tj.tcc.domain.conta.MovimentacaoFinanceira;
import com.totvs.tj.tcc.domain.conta.MovimentacaoFinanceiraId;
import com.totvs.tj.tcc.domain.conta.ResponsavelId;
import com.totvs.tj.tcc.domain.conta.StatusMovimentacaoFinanceira;
import com.totvs.tj.tcc.domain.conta.TipoMovimentacaoFinanceira;

public class teste {
	
	public static void main(String[] args) {
		
	
	
	EmpresaId idEmpresa = EmpresaId.generate();
    ResponsavelId idResponsavel = ResponsavelId.generate();        
    
    ContaId idContaDeposito = ContaId.generate(); 
    ContaId idContaDebito   = ContaId.generate();        

    Empresa empresa = Empresa.builder()
            .id(idEmpresa)
            .CPNJ("11111111111")
            .valorDeMercado(Money.of(50000.00, "BRL"))
            .quantidadeFuncionarios(10)
            .build();
    MovimentacaoFinanceiraId movimentacaoFinanceiraId = MovimentacaoFinanceiraId.generate();
    
    System.out.println("---------------------------------SAQUE-PERMITIDO--------------------------------------");
    Conta contaDebito  = Conta.from(idContaDeposito,empresa);
    
	MovimentacaoFinanceira movimentacaoFinanceiraSaque = MovimentacaoFinanceira.saque(movimentacaoFinanceiraId, 
			Money.of(12.00,"BRL"), contaDebito);
				
	System.out.println(movimentacaoFinanceiraSaque);
	 
	movimentacaoFinanceiraSaque.realizar();
	System.out.println(movimentacaoFinanceiraSaque.getContaDebito());
	System.out.println(movimentacaoFinanceiraSaque);
	System.out.println("--------------------------------------------------------------------------------------");
	
	System.out.println("--------------------------------------APROVAR-----------------------------------------");
	movimentacaoFinanceiraSaque.aprovar(null);
	movimentacaoFinanceiraSaque.realizar();

	System.out.println("--------------------------------------------------------------------------------------");

	
	Conta contaCredito = Conta.from(idContaDebito,empresa);
	MovimentacaoFinanceira movimentacaoFinanceiraDeposito = MovimentacaoFinanceira.deposito(movimentacaoFinanceiraId, 
			Money.of(100.00,"BRL"), contaCredito);
	
	movimentacaoFinanceiraDeposito.realizar();
	System.out.println(movimentacaoFinanceiraDeposito.getContaDebito());
	System.out.println(movimentacaoFinanceiraDeposito);
	}
}