package com.totvs.tj.tcc.domain.conta;
import org.javamoney.moneta.Money;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Emprestimo {
	EmprestimoId id;
	Empresa empresa;
	Money valor;
	StatusEmprestimo status;
	
	public Emprestimo(Empresa empresa, Money valor) {
		this.id = EmprestimoId.generate();
		this.empresa = empresa;
		this.valor = valor;
		this.status =  StatusEmprestimo.aguardandoAprovacao;
	}
	
	public Emprestimo(EmprestimoId id) {
		this.id = id;
	}
	
	public void aprovar() {
		this.status =  StatusEmprestimo.aprovado;
	}
	
	public void recusar() {
		this.status =  StatusEmprestimo.reprovado;
	}
	
	public void pagar() {
			this.status =  StatusEmprestimo.pago;
	}
	
}

