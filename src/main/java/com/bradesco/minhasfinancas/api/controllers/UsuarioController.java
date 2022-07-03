package com.bradesco.minhasfinancas.api.controllers;

import com.bradesco.minhasfinancas.api.dto.UsuarioDTO;
import com.bradesco.minhasfinancas.exceptions.ErroAutenticacaoException;
import com.bradesco.minhasfinancas.exceptions.RegraNegocioException;
import com.bradesco.minhasfinancas.model.entity.Usuario;
import com.bradesco.minhasfinancas.servicos.LancamentoService;
import com.bradesco.minhasfinancas.servicos.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity salvar (@RequestBody UsuarioDTO dto){
        Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();

        try{
            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar (@RequestBody UsuarioDTO dto){
        try {
            Usuario usuarioAutenticado = usuarioService.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);

        }catch (ErroAutenticacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}/saldo")
    public ResponseEntity obterSaldo(@PathVariable("id") Long id){
        Optional<Usuario> usuario = usuarioService.obterPorId(id);

        if (!usuario.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }
}
