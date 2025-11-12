package myfood.Exception;

public class ProibidoCadastrarEmpresasComMesmoNomeLocalException extends Exception {
    public ProibidoCadastrarEmpresasComMesmoNomeLocalException() {
        super("Proibido cadastrar duas empresas com o mesmo nome e local");
    }
}
