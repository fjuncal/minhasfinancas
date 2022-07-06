package com.bradesco.minhasfinancas.model.repository;

import com.bradesco.minhasfinancas.model.entity.Despesa;
import com.bradesco.minhasfinancas.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    @Query(value = "select sum(l.valor) from Despesa l join l.usuario u " +
            " where u.id = :idUsuario group by u")
    BigDecimal obterSaldoPorUsuario(@Param("idUsuario") Long idUsuario);

    List<Despesa> findByAno(Integer ano);

}
