package com.bradesco.minhasfinancas.api.dto;

import com.bradesco.minhasfinancas.model.entity.Usuario;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class LancamentoDTO {

    private Long id;
    private String descricao;
    private Integer mes;
    private Integer ano;
    private BigDecimal valor;
    private Integer usuario;
    private String tipo;
    private String status;
}
