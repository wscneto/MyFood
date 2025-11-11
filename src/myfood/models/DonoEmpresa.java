package myfood.models;

public class DonoEmpresa extends Usuario {
    private String cpf;

    public DonoEmpresa() {
        super();
    }

    public DonoEmpresa(String nome, String email, String senha, String endereco, String cpf) {
        super(nome, email, senha);
        setEndereco(endereco);
        setCpf(cpf);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "DonoEmpresa [id=" + getId() + ", nome=" + getNome() + ", email=" + getEmail() + ", cpf=" + cpf + "]";
    }
}
