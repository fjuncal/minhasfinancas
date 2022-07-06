package com.bradesco.minhasfinancas.model.repository;

import com.bradesco.minhasfinancas.model.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    @Query(value = "select sum(l.valor) from Receita l join l.usuario u " +
            " where u.id = :idUsuario group by u")
    BigDecimal obterSaldoPorTipoLancamentoEusuario(@Param("idUsuario") Long idUsuario);

    List<Receita> findByAno(Integer ano);

}
