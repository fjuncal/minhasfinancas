package com.bradesco.minhasfinancas.api.controllers;

import com.bradesco.minhasfinancas.api.dto.AtualizaStatusDTO;
import com.bradesco.minhasfinancas.api.dto.LancamentoDTO;
import com.bradesco.minhasfinancas.exceptions.RegraNegocioException;
import com.bradesco.minhasfinancas.model.entity.Lancamento;
import com.bradesco.minhasfinancas.model.entity.Usuario;
import com.bradesco.minhasfinancas.model.entity.enums.StatusLancamento;
import com.bradesco.minhasfinancas.model.repository.LancamentoRepository;
import com.bradesco.minhasfinancas.servicos.LancamentoService;
import com.bradesco.minhasfinancas.servicos.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;
    private final LancamentoRepository repository;

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao, @RequestParam(value = "mes", required = false) Integer mes, @RequestParam(value = "ano", required = false) Integer ano, @RequestParam(value = "usuario") Long idUsuario) {
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(Math.toIntExact(idUsuario));
        if (!usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado");
        } else {
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
        List<Lancamento> byAno = repository.findByAno(lancamentoFiltro.getAno());
        return ResponseEntity.ok(byAno);
    }

    @GetMapping("{id}")
    public Optional<Lancamento> obterPorId(@PathVariable("id") Long id){
        return repository.findById(id);
    }


    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {

        try {
            Lancamento entidadeLancamento = converter(dto);

            entidadeLancamento = lancamentoService.salvar(entidadeLancamento);
            return new ResponseEntity(entidadeLancamento, HttpStatus.OK);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {

        ResponseEntity<?> responseEntity = lancamentoService.obterPorId(id).map(entity -> {
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                lancamentoService.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));

        return responseEntity;
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto){
        return lancamentoService.obterPorId(id).map(entity -> {
            StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());

            if (statusSelecionado == null){
                return ResponseEntity.badRequest().body("Não foi possivel atualizar o status do lançamento, envie um status válido");
            }
            try{
                entity.setStatus(statusSelecionado);
                lancamentoService.atualizar(entity);
                return ResponseEntity.ok(entity);
            }catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {

        return lancamentoService.obterPorId(id).map(entidade -> {
            lancamentoService.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    private LancamentoDTO converter(Lancamento lancamento) {
        return LancamentoDTO.builder()
                .id(lancamento.getId())
                .descricao(lancamento.getDescricao())
                .valor(lancamento.getValor())
                .mes(lancamento.getMes())
                .ano(lancamento.getAno())
                .status(lancamento.getStatus().name())
                .usuario((int) lancamento.getUsuario().getId())
                .build();
    }

    private Lancamento converter(LancamentoDTO dto) {
        Lancamento lancamento = new Lancamento();
        if (dto.getId() != null){
            lancamento.setId(dto.getId());
        }
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());
        if (dto.getUsuario() != null){
            Usuario usuario = usuarioService
                    .obterPorId(dto.getUsuario())
                    .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado.") );
            lancamento.setUsuario(usuario);
        }

        if(dto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }
}
