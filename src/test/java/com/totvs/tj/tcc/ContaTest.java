package com.totvs.tj.tcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.javamoney.moneta.Money;
import org.junit.Test;

import com.totvs.tj.tcc.app.AbrirContaCommand;
import com.totvs.tj.tcc.app.ContaApplicationService;
import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.conta.Empresa;
import com.totvs.tj.tcc.domain.conta.EmpresaId;
import com.totvs.tj.tcc.domain.conta.ResponsavelId;

public class ContaTest {

    private final ContaId idConta = ContaId.generate();

    private final EmpresaId idEmpresa = EmpresaId.generate();

    private final ResponsavelId idResponsavel = ResponsavelId.generate();
  
    
    @Test
    public void aoCriarUmaConta() throws Exception {

        // WHEN
        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .CPNJ("11111111111")
                .valorDeMercado(Money.of(50000.00, "BRL"))
                .quantidadeFuncionarios(10)
                .build();
        
        Conta conta = Conta.from(idConta,empresa);
        
        // THEN
        assertNotNull(conta);

        assertEquals(idConta, conta.getId());
        assertEquals(empresa, conta.getEmpresa());

        assertEquals(idConta.toString(), conta.getId().toString());
        assertEquals(empresa.toString(), conta.getEmpresa().toString());
        
        assertTrue(conta.isDisponivel());
        
        assertTrue(conta.getLimite().isLessThanOrEqualTo(Money.of(15000, "BRL")));
        
        assertTrue(conta.getSaldo().isEqualTo(Money.of(0, "BRL")));
    }

    @Test
    public void aoSolicitarAberturaConta() throws Exception {

        // GIVEN
        ContaRepository repository = new ContaRepositoryMock();
        ContaApplicationService service = new ContaApplicationService(repository);
        
        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .CPNJ("11111111111")
                .valorDeMercado(Money.of(50000.00, "BRL"))
                .quantidadeFuncionarios(10)
                .build();
        
        AbrirContaCommand cmd = AbrirContaCommand.builder()
                .empresa(empresa)
            .build();

        // WHEN
        ContaId idConta = service.handle(cmd);

        // THEN
        assertNotNull(idConta);
    }

    @Test
    public void aoSolicitarAumentoDeLimite() throws Exception {

        // GIVEN
        ContaRepository repository = new ContaRepositoryMock();
        ContaApplicationService service = new ContaApplicationService(repository);
        
        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .CPNJ("11111111111")
                .valorDeMercado(Money.of(50000.00, "BRL"))
                .quantidadeFuncionarios(10)
                .build();
        
        Conta conta = Conta.from(idConta,empresa);
        
        // WHEN
        conta.aumentarLimte(Money.of(100.00, "BRL"));
        
        // THEN
        assertTrue(conta.getLimite().isEqualTo(Money.of(150, "BRL")));
    }
    
    static class ContaRepositoryMock implements ContaRepository {
        @Override
        public void save(Conta conta) {
            System.out.println("Salvou a conta: " + conta);
        }
    }
}
