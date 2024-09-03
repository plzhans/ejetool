package com.ejetool.jwt.generator;

import java.util.List;
import java.util.ArrayList;

import lombok.Getter;

public class JwtKeyStoreValidatorOptions {
    @Getter
    private List<String> issuerList;

    JwtKeyStoreValidatorOptions(){
        this.issuerList = new ArrayList<>();
    }

    public void addIssuer(String issuer){
        this.issuerList.add(issuer);
    }

    public void addIssuer(String[] ...array){
        this.issuerList.addAll(issuerList);
    }
}
