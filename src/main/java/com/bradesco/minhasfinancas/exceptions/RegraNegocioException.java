package com.bradesco.minhasfinancas.exceptions;

public class RegraNegocioException extends RuntimeException{

    public RegraNegocioException(String mensagem){
        super(mensagem);
    }
}
