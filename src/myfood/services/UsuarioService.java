package myfood.services;

import myfood.Exception.ContaComEmailJaExisteException;
import myfood.Exception.CpfInvalidoException;
import myfood.Exception.EmailInvalidoException;
import myfood.Exception.EnderecoInvalidoException;
import myfood.Exception.LoginOuSenhaInvalidosException;
import myfood.Exception.NomeInvalidoException;
import myfood.Exception.SenhaInvalidoException;
import myfood.models.*;
import java.util.*;

public class UsuarioService {
    private final Map<String, Usuario> usuarios;
    private final ArmazenamentoService armazenamento;
    private int idAtual = 1;

    public UsuarioService(Map<String, Usuario> usuarios, ArmazenamentoService armazenamento) {
        this.usuarios = usuarios;
        this.armazenamento = armazenamento;
    }

    public void criarCliente(String nome, String email, String senha, String endereco)
            throws NomeInvalidoException, EmailInvalidoException, SenhaInvalidoException, EnderecoInvalidoException,
            ContaComEmailJaExisteException {
        validarCampos(nome, email, senha, endereco);
        if (usuarios.containsKey(email))
            throw new ContaComEmailJaExisteException();

        Cliente c = new Cliente(nome, email, senha, endereco);
        c.setId(String.valueOf(idAtual++));
        usuarios.put(email, c);
        salvar();
    }

    public void criarDonoEmpresa(String nome, String email, String senha, String endereco, String cpf)
            throws NomeInvalidoException, EmailInvalidoException, SenhaInvalidoException, EnderecoInvalidoException,
            CpfInvalidoException, ContaComEmailJaExisteException {
        validarCampos(nome, email, senha, endereco);
        if (cpf == null || cpf.isBlank() || cpf.length() != 14)
            throw new CpfInvalidoException();
        if (usuarios.containsKey(email))
            throw new ContaComEmailJaExisteException();

        DonoEmpresa d = new DonoEmpresa(nome, email, senha, endereco, cpf);
        d.setId(String.valueOf(idAtual++));
        usuarios.put(email, d);
        salvar();
    }

    public int login(String email, String senha) throws LoginOuSenhaInvalidosException {
        Usuario u = usuarios.get(email);
        if (u == null || !u.getSenha().equals(senha))
            throw new LoginOuSenhaInvalidosException();
        return Integer.parseInt(u.getId());
    }

    public String getAtributoUsuario(int id, String atributo) {
        for (Usuario u : usuarios.values()) {
            if (Integer.parseInt(u.getId()) == id) {
                switch (atributo) {
                    case "nome":
                        return u.getNome();
                    case "email":
                        return u.getEmail();
                    case "senha":
                        return u.getSenha();
                    case "endereco":
                        return u.getEndereco();
                    case "cpf":
                        if (u instanceof DonoEmpresa)
                            return ((DonoEmpresa) u).getCpf();
                        throw new IllegalArgumentException("Atributo invalido");
                }
            }
        }
        throw new IllegalArgumentException("Usuario nao cadastrado.");
    }

    public Usuario getUsuarioById(int id) {
        for (Usuario u : usuarios.values()) {
            if (Integer.parseInt(u.getId()) == id)
                return u;
        }
        return null;
    }

    private void salvar() {
        try {
            armazenamento.salvarSistema();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar sistema");
        }
    }

    private void validarCampos(String nome, String email, String senha, String endereco)
            throws NomeInvalidoException, EmailInvalidoException, SenhaInvalidoException, EnderecoInvalidoException {
        if (nome == null || nome.isBlank())
            throw new NomeInvalidoException();
        if (email == null || email.isBlank() || !email.contains("@"))
            throw new EmailInvalidoException();
        if (senha == null || senha.isBlank())
            throw new SenhaInvalidoException();
        if (endereco == null || endereco.isBlank())
            throw new EnderecoInvalidoException();
    }
}
