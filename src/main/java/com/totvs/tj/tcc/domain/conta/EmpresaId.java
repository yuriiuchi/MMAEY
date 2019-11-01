package com.totvs.tj.tcc.domain.conta;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "from")
public class EmpresaId {

    private String value;

    public static EmpresaId generate() {
        return EmpresaId.from(UUID.randomUUID().toString());
    }

}
