package com.bradesco.minhasfinancas.servicos;

import com.bradesco.minhasfinancas.model.entity.Lancamento;
import com.bradesco.minhasfinancas.model.entity.enums.StatusLancamento;

import java.util.List;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);

    Lancamento atualizar(Lancamento lancamento);

    void deletar(Lancamento lancamento);

    List<Lancamento> buscar(Lancamento lancamentoFiltro);

    void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento);

    void validar (Lancamento lancamento);
}
