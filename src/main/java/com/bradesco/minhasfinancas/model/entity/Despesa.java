package com.bradesco.minhasfinancas.model.entity;

import com.bradesco.minhasfinancas.model.entity.enums.TipoDespesa;

import javax.persistence.*;

@Entity
@Table(name = "DESPESA", schema = "financas")
@DiscriminatorValue("DESPESA")
public class Despesa extends Lancamento{

    @Column(name = "tipo_despesa")
    @Enumerated(value = EnumType.STRING)
    TipoDespesa tipoDespesa;
}
