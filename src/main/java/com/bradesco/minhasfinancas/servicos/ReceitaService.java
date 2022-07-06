package com.bradesco.minhasfinancas.servicos;

import com.bradesco.minhasfinancas.model.entity.Lancamento;
import com.bradesco.minhasfinancas.model.entity.Receita;
import com.bradesco.minhasfinancas.model.entity.enums.StatusLancamento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ReceitaService {

    Receita salvar(Receita receita);

    Receita atualizar(Receita receita);

    void deletar(Receita receita);

    List<Receita> buscar(Receita receitaFiltro);

    void atualizarStatus(Receita receita, StatusLancamento statusLancamento);

    void validar (Receita receita);

    Optional<Receita> obterPorId(Long id);

}
