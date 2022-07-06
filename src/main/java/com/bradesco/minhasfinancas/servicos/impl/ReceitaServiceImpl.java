package com.bradesco.minhasfinancas.servicos.impl;

import com.bradesco.minhasfinancas.exceptions.RegraNegocioException;
import com.bradesco.minhasfinancas.model.entity.Receita;
import com.bradesco.minhasfinancas.model.entity.enums.StatusLancamento;
import com.bradesco.minhasfinancas.model.repository.ReceitaRepository;
import com.bradesco.minhasfinancas.servicos.ReceitaService;
import com.bradesco.minhasfinancas.servicos.ReceitaService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReceitaServiceImpl implements ReceitaService {

    private ReceitaRepository repository;

    public ReceitaServiceImpl(ReceitaRepository ReceitaRepository) {
        this.repository = ReceitaRepository;
    }

    @Override
    @Transactional
    public Receita salvar(Receita Receita) {
        validar(Receita);
        //salvar Receita sempre com status pendente
        Receita.setStatus(StatusLancamento.PENDENTE);
        return repository.save(Receita);
    }

    @Override
    @Transactional
    public Receita atualizar(Receita Receita) {
        //Tem que passar um Receita com id se não lança nullpointer
        Objects.requireNonNull(Receita.getId());
        validar(Receita);
        return repository.save(Receita);
    }

    @Override
    @Transactional
    public void deletar(Receita Receita) {
        Objects.requireNonNull(Receita.getId());
        repository.delete(Receita);
    }

    @Override
    //apenas leitura
    @Transactional(readOnly = true)
    public List<Receita> buscar(Receita ReceitaFiltro) {
        //forma de buscar com filtro ignorando o maiusculo e por ex: alguma letra que ele esteja procurando ele pega tudo que tem no banco com a letra
        Example example = Example.of(ReceitaFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher( ExampleMatcher.StringMatcher.EXACT) );

        List<Receita> all = repository.findAll(example);

        return all;


    }

    @Override
    public void atualizarStatus(Receita Receita, StatusLancamento StatusLancamento) {
        Receita.setStatus(StatusLancamento);
        atualizar(Receita);
    }

    @Override
    public void validar(Receita Receita) {

        if (Receita.getDescricao() == null || Receita.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma descrição válida");
        }
        if (Receita.getMes() == null || Receita.getMes() < 1 || Receita.getMes() > 12) {
            throw new RegraNegocioException("Informe uma mês válido");
        }

        if (Receita.getAno() == null || Receita.getAno().toString().length() != 4) {
            throw new RegraNegocioException("Informe um ano válido");
        }

        if (Receita.getUsuario() == null) {
            throw new RegraNegocioException("Informe um usuário");
        }
        if (Receita.getValor() == null || Receita.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um valor válido");
        }
    }

    @Override
    public Optional<Receita> obterPorId(Long id) {
        return repository.findById(id);
    }

}
