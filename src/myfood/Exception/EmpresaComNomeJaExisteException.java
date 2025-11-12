package myfood.Exception;

public class EmpresaComNomeJaExisteException extends Exception {
    public EmpresaComNomeJaExisteException() {
        super("Empresa com esse nome ja existe");
    }
}
