package com.totvs.tj.tcc;

import static org.junit.Assert.assertNotNull;

import org.javamoney.moneta.Money;
import org.junit.Test;

import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.Empresa;
import com.totvs.tj.tcc.domain.conta.EmpresaId;
import com.totvs.tj.tcc.domain.conta.ResponsavelId;

public class EmpresaTest {
    private final ContaId idConta = ContaId.generate();

    private final EmpresaId idEmpresa = EmpresaId.generate();

    private final ResponsavelId idResponsavel = ResponsavelId.generate();
  
    private Empresa empresa;
    
@Test
public void aoCriarEmpresaComCNPJCorreto() {
    empresa = Empresa.builder()
            .id(idEmpresa)
            .CPNJ("52.211.237/0001-15")
            .valorDeMercado(Money.of(50000.00, "BRL"))
            .quantidadeFuncionarios(10)
            .build();
    assertNotNull(empresa);
    }

@Test(expected = IllegalArgumentException.class)  
public void aoCriarEmpresaComCNPJErrado() throws Exception  {
    empresa = Empresa.builder()
            .id(idEmpresa)
            .CPNJ("52.211.237/0001-1")
            .valorDeMercado(Money.of(0, "BRL"))
            .quantidadeFuncionarios(-1)
            .build();
    assertNotNull("Não deve chegar aqui");
    }
}
