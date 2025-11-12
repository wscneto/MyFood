package myfood.services;

import myfood.Exception.AtributoInvalidoException;
import myfood.Exception.EmpresaComNomeJaExisteException;
import myfood.Exception.EmpresaNaoCadastradaException;
import myfood.Exception.IndiceInvalidoException;
import myfood.Exception.IndiceMaiorQueEsperadoException;
import myfood.Exception.NaoExisteEmpresaComNomeException;
import myfood.Exception.NomeInvalidoException;
import myfood.Exception.ProibidoCadastrarEmpresasComMesmoNomeLocalException;
import myfood.Exception.UsuarioNaoCadastradoException;
import myfood.Exception.UsuarioNaoPodeCriarEmpresaException;
import myfood.models.*;
import java.util.*;

public class EmpresaService {
    private final Map<Integer, Empresa> empresas;
    private int idAtual = 1;
    private final Map<String, Usuario> usuarios;
    private final ArmazenamentoService armazenamento;

    public EmpresaService(Map<String, Usuario> usuarios, ArmazenamentoService armazenamento,
            Map<Integer, Empresa> empresas) {
        this.usuarios = usuarios;
        this.armazenamento = armazenamento;
        this.empresas = empresas;
    }

    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String tipoCozinha)
            throws ProibidoCadastrarEmpresasComMesmoNomeLocalException, EmpresaComNomeJaExisteException,
            UsuarioNaoPodeCriarEmpresaException, UsuarioNaoCadastradoException {

        Usuario u = getUsuarioPorId(idDono);
        if (!(u instanceof DonoEmpresa))
            throw new UsuarioNaoPodeCriarEmpresaException();

        DonoEmpresa dono = (DonoEmpresa) u;

        // Validações:
        // - Não pode ter duas empresas com mesmo nome e donos diferentes;
        // - Duas empresas com mesmo nome e mesmo dono não podem estar no mesmo
        // endereço.
        for (Empresa e : empresas.values()) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                if (e.getDono().getId().equals(dono.getId())) {
                    if (e.getEndereco().equalsIgnoreCase(endereco))
                        throw new ProibidoCadastrarEmpresasComMesmoNomeLocalException();
                } else {
                    throw new EmpresaComNomeJaExisteException();
                }
            }
        }

        Empresa nova = new Empresa(idAtual++, nome, endereco, tipoCozinha, dono);
        empresas.put(nova.getId(), nova);
        salvar();
        return nova.getId();
    }

    public String getEmpresasDoUsuario(int idDono)
            throws UsuarioNaoPodeCriarEmpresaException, UsuarioNaoCadastradoException {
        Usuario u = getUsuarioPorId(idDono);
        if (!(u instanceof DonoEmpresa))
            throw new UsuarioNaoPodeCriarEmpresaException();

        List<String> lista = new ArrayList<>();
        for (Empresa e : empresas.values()) {
            if (e.getDono().getId().equals(u.getId()))
                lista.add("[" + e.getNome() + ", " + e.getEndereco() + "]");
        }
        return "{" + lista.toString() + "}";
    }

    public int getIdEmpresa(int idDono, String nome, int indice)
            throws NomeInvalidoException, IndiceInvalidoException, UsuarioNaoPodeCriarEmpresaException,
            NaoExisteEmpresaComNomeException, IndiceMaiorQueEsperadoException, UsuarioNaoCadastradoException {
        if (nome == null || nome.isBlank())
            throw new NomeInvalidoException();
        if (indice < 0)
            throw new IndiceInvalidoException();

        Usuario u = getUsuarioPorId(idDono);
        if (!(u instanceof DonoEmpresa))
            throw new UsuarioNaoPodeCriarEmpresaException();

        List<Empresa> lista = new ArrayList<>();
        for (Empresa e : empresas.values()) {
            if (e.getDono().getId().equals(u.getId()) && e.getNome().equalsIgnoreCase(nome))
                lista.add(e);
        }

        if (lista.isEmpty())
            throw new NaoExisteEmpresaComNomeException();
        if (indice >= lista.size())
            throw new IndiceMaiorQueEsperadoException();

        return lista.get(indice).getId();
    }

    public String getAtributoEmpresa(int idEmpresa, String atributo)
            throws EmpresaNaoCadastradaException, AtributoInvalidoException {
        Empresa e = empresas.get(idEmpresa);
        if (e == null)
            throw new EmpresaNaoCadastradaException();
        if (atributo == null || atributo.isBlank())
            throw new AtributoInvalidoException();

        switch (atributo) {
            case "nome":
                return e.getNome();
            case "endereco":
                return e.getEndereco();
            case "tipoCozinha":
                return e.getTipoCozinha();
            case "dono":
                return e.getDono().getNome();
            default:
                throw new AtributoInvalidoException();
        }
    }

    private Usuario getUsuarioPorId(int id) throws UsuarioNaoCadastradoException {
        for (Usuario u : usuarios.values())
            if (Integer.parseInt(u.getId()) == id)
                return u;
        throw new UsuarioNaoCadastradoException();
    }

    private void salvar() {
        try {
            armazenamento.salvarSistema();
        } catch (Exception e) {
            System.out.println("Erro ao salvar empresas.");
        }
    }
}
