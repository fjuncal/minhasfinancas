package com.bradesco.minhasfinancas.model.repository;

import com.bradesco.minhasfinancas.model.entity.Lancamento;
import com.bradesco.minhasfinancas.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
