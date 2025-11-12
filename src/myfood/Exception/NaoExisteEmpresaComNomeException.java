package myfood.Exception;

public class NaoExisteEmpresaComNomeException extends Exception {
    public NaoExisteEmpresaComNomeException() {
        super("Nao existe empresa com esse nome");
    }
}
