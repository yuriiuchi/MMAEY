package com.totvs.tj.tcc.app;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.ContaRepository;

@Service
public class ContaApplicationService {
    
    private ContaRepository repository;
    
    public ContaApplicationService(ContaRepository repository) {
        this.repository = repository;
    }
    
    public ContaId handle(AbrirContaCommand cmd) {
        
        ContaId idConta = ContaId.generate();
        
        Conta conta = Conta.from(idConta,cmd.getEmpresa());
        
        repository.save(conta);
        
        return idConta; 
    }
    
}
