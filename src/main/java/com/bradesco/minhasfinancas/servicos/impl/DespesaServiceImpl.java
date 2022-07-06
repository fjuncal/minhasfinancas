package com.bradesco.minhasfinancas.servicos.impl;

import com.bradesco.minhasfinancas.exceptions.RegraNegocioException;
import com.bradesco.minhasfinancas.model.entity.Despesa;
import com.bradesco.minhasfinancas.model.entity.enums.StatusLancamento;
import com.bradesco.minhasfinancas.model.repository.DespesaRepository;
import com.bradesco.minhasfinancas.servicos.DespesaService;
import com.bradesco.minhasfinancas.servicos.DespesaService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DespesaServiceImpl implements DespesaService {

    private DespesaRepository repository;

    public DespesaServiceImpl(DespesaRepository DespesaRepository) {
        this.repository = DespesaRepository;
    }

    @Override
    @Transactional
    public Despesa salvar(Despesa Despesa) {
        validar(Despesa);
        //salvar Despesa sempre com status pendente
        Despesa.setStatus(StatusLancamento.PENDENTE);
        return repository.save(Despesa);
    }

    @Override
    @Transactional
    public Despesa atualizar(Despesa Despesa) {
        //Tem que passar um Despesa com id se não lança nullpointer
        Objects.requireNonNull(Despesa.getId());
        validar(Despesa);
        return repository.save(Despesa);
    }

    @Override
    @Transactional
    public void deletar(Despesa Despesa) {
        Objects.requireNonNull(Despesa.getId());
        repository.delete(Despesa);
    }

    @Override
    //apenas leitura
    @Transactional(readOnly = true)
    public List<Despesa> buscar(Despesa DespesaFiltro) {
        //forma de buscar com filtro ignorando o maiusculo e por ex: alguma letra que ele esteja procurando ele pega tudo que tem no banco com a letra
        Example example = Example.of(DespesaFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher( ExampleMatcher.StringMatcher.EXACT) );

        List<Despesa> all = repository.findAll(example);

        return all;


    }

    @Override
    public void atualizarStatus(Despesa Despesa, StatusLancamento StatusLancamento) {
        Despesa.setStatus(StatusLancamento);
        atualizar(Despesa);
    }

    @Override
    public void validar(Despesa Despesa) {

        if (Despesa.getDescricao() == null || Despesa.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma descrição válida");
        }
        if (Despesa.getMes() == null || Despesa.getMes() < 1 || Despesa.getMes() > 12) {
            throw new RegraNegocioException("Informe uma mês válido");
        }

        if (Despesa.getAno() == null || Despesa.getAno().toString().length() != 4) {
            throw new RegraNegocioException("Informe um ano válido");
        }

        if (Despesa.getUsuario() == null) {
            throw new RegraNegocioException("Informe um usuário");
        }
        if (Despesa.getValor() == null || Despesa.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um valor válido");
        }
    }

    @Override
    public Optional<Despesa> obterPorId(Long id) {
        return repository.findById(id);
    }


}
