package myfood;

import myfood.Exception.ContaComEmailJaExisteException;
import myfood.Exception.CpfInvalidoException;
import myfood.Exception.EmailInvalidoException;
import myfood.Exception.EnderecoInvalidoException;
import myfood.Exception.LoginOuSenhaInvalidosException;
import myfood.Exception.NomeInvalidoException;
import myfood.Exception.SenhaInvalidoException;
import myfood.models.*;
import myfood.services.*;

import java.util.*;

public class Facade {
    private final Map<String, Usuario> usuarios;
    private final ArmazenamentoService armazenamento;
    private final UsuarioService usuarioService;

    public Facade() {
        this.usuarios = new HashMap<>();
        this.armazenamento = new ArmazenamentoService(usuarios, 0);
        this.usuarioService = new UsuarioService(usuarios, armazenamento);

        try {
            armazenamento.carregarSistema(); // carrega XML na inicialização
        } catch (Exception e) {
            System.out.println("Sistema iniciado sem dados persistidos.");
        }
    }

    public void zerarSistema() {
        armazenamento.zerarSistema();
    }

    public void encerrarSistema() throws Exception {
        armazenamento.encerrarSistema();
    }

    public void criarUsuario(String nome, String email, String senha, String endereco)
            throws NomeInvalidoException, EmailInvalidoException, SenhaInvalidoException, EnderecoInvalidoException,
            ContaComEmailJaExisteException {
        usuarioService.criarCliente(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf)
            throws NomeInvalidoException, EmailInvalidoException, SenhaInvalidoException, EnderecoInvalidoException,
            ContaComEmailJaExisteException, CpfInvalidoException {
        usuarioService.criarDonoEmpresa(nome, email, senha, endereco, cpf);
    }

    public int login(String email, String senha) throws LoginOuSenhaInvalidosException {
        return usuarioService.login(email, senha);
    }

    public String getAtributoUsuario(int id, String atributo) {
        return usuarioService.getAtributoUsuario(id, atributo);
    }
}
