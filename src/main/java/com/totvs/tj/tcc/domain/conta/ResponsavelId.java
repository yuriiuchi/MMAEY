package com.totvs.tj.tcc.domain.conta;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "from")
public class ResponsavelId {

    private String value;

    public static ResponsavelId generate() {
        return ResponsavelId.from(UUID.randomUUID().toString());
    }
    
}
