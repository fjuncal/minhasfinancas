package com.bradesco.minhasfinancas.servicos;

import com.bradesco.minhasfinancas.model.entity.Despesa;
import com.bradesco.minhasfinancas.model.entity.Lancamento;
import com.bradesco.minhasfinancas.model.entity.Receita;
import com.bradesco.minhasfinancas.model.entity.enums.StatusLancamento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DespesaService {

    Despesa salvar(Despesa despesa);

    Despesa atualizar(Despesa despesa);

    void deletar(Despesa despesa);

    List<Despesa> buscar(Despesa despesaFiltro);

    void atualizarStatus(Despesa despesa, StatusLancamento statusLancamento);

    void validar (Despesa despesa);

    Optional<Despesa> obterPorId(Long id);

}
