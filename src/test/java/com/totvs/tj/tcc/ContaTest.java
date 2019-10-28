package com.totvs.tj.tcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.javamoney.moneta.Money;
import org.junit.Test;

import com.totvs.tj.tcc.app.AbrirContaCommand;
import com.totvs.tj.tcc.app.ContaApplicationService;
import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.conta.Empresa;
import com.totvs.tj.tcc.domain.conta.EmpresaId;
import com.totvs.tj.tcc.domain.conta.ExtratoConta;
import com.totvs.tj.tcc.domain.conta.ResponsavelId;

public class ContaTest {

    private final ContaId idConta = ContaId.generate();

    private final EmpresaId idEmpresa = EmpresaId.generate();

    private final ResponsavelId idResponsavel = ResponsavelId.generate();
  
    private Empresa empresa;
    
    public Empresa getEmpresa() {
            return this.empresa = Empresa.builder()
                    .id(idEmpresa)
                    .CPNJ("11111111111")
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
    
    @Test
    public void aoCreditarSaldo() throws Exception {
        
        List<ExtratoConta> extratoConta = new ArrayList<ExtratoConta>();
        
        // WHEN
        Empresa empresa = this.getEmpresa();
        Conta conta = getConta();
        
        // THEN
        extratoConta.add(ExtratoConta.from(Money.of(150, "BRL")));
        assertNotNull(conta);
        conta.creditarSaldo(Money.of(150, "BRL"));
        assertEquals(conta.getSaldo(),Money.of(150, "BRL")); 
        assertEquals(extratoConta.get(0).toString(), conta.getExtrato().get(0).toString());
   }
    
    @Test
    public void aoDebitarSaldo() throws Exception {
        
        List<ExtratoConta> extratoConta = new ArrayList<ExtratoConta>();
        
        // WHEN
        Empresa empresa = this.getEmpresa();
        Conta conta = getConta();
        
        // THEN
        extratoConta.add(ExtratoConta.from(Money.of(-200, "BRL")));
        assertNotNull(conta);
        conta.debitarSaldo(Money.of(200, "BRL"));
        assertEquals(conta.getSaldo(),Money.of(-200, "BRL")); 
        assertEquals(extratoConta.get(0).toString(), conta.getExtrato().get(0).toString());
   }

    static class ContaRepositoryMock implements ContaRepository {
        @Override
        public void save(Conta conta) {
            System.out.println("Salvou a conta: " + conta);
        }
    }
}
