package com.bradesco.minhasfinancas.servicos.impl;

import com.bradesco.minhasfinancas.exceptions.ErroAutenticacaoException;
import com.bradesco.minhasfinancas.exceptions.RegraNegocioException;
import com.bradesco.minhasfinancas.model.entity.Usuario;
import com.bradesco.minhasfinancas.model.repository.UsuarioRepository;
import com.bradesco.minhasfinancas.servicos.UsuarioService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (!usuario.isPresent()){
            throw new ErroAutenticacaoException("Usuário não encontrado para o email informado");
        }

        if (!usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacaoException("Senha inválida");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {

        boolean existsByEmail = usuarioRepository.existsByEmail(email);

        if (existsByEmail) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com esse email");
        }

    }

    @Override
    public Optional<Usuario> obterPorId(Integer id) {
        return usuarioRepository.findById(Long.valueOf(id));
    }
}
