package myfood.Exception;

public class ContaComEmailJaExisteException extends Exception {
    public ContaComEmailJaExisteException() {
        super("Conta com esse email ja existe");
    }
}
