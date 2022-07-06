package com.bradesco.minhasfinancas.model.entity;

import com.bradesco.minhasfinancas.model.entity.enums.TipoReceita;

import javax.persistence.*;

@Entity
@Table(name = "receita", schema = "financas")
@DiscriminatorValue("RECEITA")
public class Receita extends Lancamento{

        @Column(name = "tipo_receita")
        @Enumerated(value = EnumType.STRING)
        private TipoReceita tipoReceita;
}
