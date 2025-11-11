package myfood.factories;

import myfood.models.*;

public class UsuarioFactory {

    public static Usuario criarCliente(String nome, String email, String senha, String endereco) {
        return new Cliente(nome, email, senha, endereco);
    }

    public static Usuario criarDono(String nome, String email, String senha, String endereco, String cpf) {
        return new DonoEmpresa(nome, email, senha, endereco, cpf);
    }
}
