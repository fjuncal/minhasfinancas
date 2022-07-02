package com.bradesco.minhasfinancas.servicos;

import com.bradesco.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

    Usuario autenticar (String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);
}
