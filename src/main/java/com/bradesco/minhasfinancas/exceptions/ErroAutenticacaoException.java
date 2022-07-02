package com.bradesco.minhasfinancas.exceptions;

public class ErroAutenticacaoException extends RuntimeException{

    public ErroAutenticacaoException(String mensagem){
        super(mensagem);
    }
}
