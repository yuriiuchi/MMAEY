package com.totvs.tj.tcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
  
    private Empresa empresa;
    
    public Empresa getEmpresa() {
            return this.empresa = Empresa.builder()
                    .id(idEmpresa)
                    .CPNJ("48.206.442/0001-15")
                    .valorDeMercado(Money.of(50000.00, "BRL"))
                    .quantidadeFuncionarios(10)
                    .build();
    }
    
    public Conta getConta() {
        return Conta.from(idConta,empresa);
    }
    
    @Test
    public void aoCriarUmaConta() throws Exception {
        // WHEN
        Empresa empresa = this.getEmpresa();
        Conta conta = getConta();
        // THEN
        assertNotNull(conta);
        assertEquals(idConta, conta.getId());
        assertEquals(idConta.toString(), conta.getId().toString());        
        assertTrue(conta.isDisponivel());        
        assertTrue(conta.getLimite().isLessThanOrEqualTo(Money.of(15000, "BRL")));
        assertTrue(conta.getSaldo().isEqualTo(Money.of(0, "BRL")));
    }

    @Test
    public void aoSolicitarAberturaConta() throws Exception {

        // GIVEN
        ContaRepository repository = new ContaRepositoryMock();
        ContaApplicationService service = new ContaApplicationService(repository);
        
        Empresa empresa = this.getEmpresa();
        
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
        
        Empresa empresa = this.getEmpresa();
        
        Conta conta = getConta();
        
        // WHEN
        conta.aumentarLimte();
        
        // THEN
        assertEquals(conta.getLimite(),(Money.of(75, "BRL")));
        conta.aumentarLimte();
        assertEquals(conta.getLimite(),(Money.of(75, "BRL")));
    }
    
    @Test
    public void aoSuspenderConta() throws Exception {
        
        // WHEN
        Empresa empresa = this.getEmpresa();
        Conta conta = getConta();
        
        // THEN
        assertNotNull(conta);
        conta.suspender();
        assertFalse(conta.isDisponivel());
    }    
   
    
    static class ContaRepositoryMock implements ContaRepository {
        @Override
        public void save(Conta conta) {
            System.out.println("Salvou a conta: " + conta);
        }
    }
}
