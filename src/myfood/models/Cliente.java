package myfood.models;

public class Cliente extends Usuario {

    public Cliente() {
        super();
    }

    public Cliente(String nome, String email, String senha, String endereco) {
        super(nome, email, senha);
        setEndereco(endereco);
    }

    @Override
    public String toString() {
        return "Cliente [id=" + getId() + ", nome=" + getNome() + ", email=" + getEmail() + "]";
    }
}
