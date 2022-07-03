package com.bradesco.minhasfinancas.servicos.impl;

import com.bradesco.minhasfinancas.exceptions.RegraNegocioException;
import com.bradesco.minhasfinancas.model.entity.Lancamento;
import com.bradesco.minhasfinancas.model.entity.enums.StatusLancamento;
import com.bradesco.minhasfinancas.model.entity.enums.TipoLancamento;
import com.bradesco.minhasfinancas.model.repository.LancamentoRepository;
import com.bradesco.minhasfinancas.servicos.LancamentoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private LancamentoRepository repository;

    public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
        this.repository = lancamentoRepository;
    }

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        //salvar lancamento sempre com status pendente
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        //Tem que passar um lancamento com id se não lança nullpointer
        Objects.requireNonNull(lancamento.getId());
        validar(lancamento);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        repository.delete(lancamento);
    }

    @Override
    //apenas leitura
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        //forma de buscar com filtro ignorando o maiusculo e por ex: alguma letra que ele esteja procurando ele pega tudo que tem no banco com a letra
        Example<Lancamento> example = Example.of(lancamentoFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento) {
        lancamento.setStatus(statusLancamento);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {

        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma descrição válida");
        }
        if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
            throw new RegraNegocioException("Informe uma mês válido");
        }

        if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
            throw new RegraNegocioException("Informe um ano válido");
        }

        if (lancamento.getUsuario() == null) {
            throw new RegraNegocioException("Informe um usuário");
        }
        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um valor válido");
        }
        if (lancamento.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo de lançamento");
        }
    }

    @Override
    public Optional<Lancamento> obterPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obterSaldoPorUsuario(Long id) {

        BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEusuario(id, TipoLancamento.RECEITA.name());
        BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEusuario(id, TipoLancamento.DESPESA.name());

        if (receitas == null) {
            receitas = BigDecimal.ZERO;
        }

        if (despesas == null){
            despesas = BigDecimal.ZERO;
        }

        return receitas.subtract(despesas);
    }
}
