package myfood;

import myfood.Exception.AtributoInvalidoException;
import myfood.Exception.ContaComEmailJaExisteException;
import myfood.Exception.CpfInvalidoException;
import myfood.Exception.EmailInvalidoException;
import myfood.Exception.EmpresaComNomeJaExisteException;
import myfood.Exception.EmpresaNaoCadastradaException;
import myfood.Exception.EnderecoInvalidoException;
import myfood.Exception.IndiceInvalidoException;
import myfood.Exception.IndiceMaiorQueEsperadoException;
import myfood.Exception.LoginOuSenhaInvalidosException;
import myfood.Exception.NaoExisteEmpresaComNomeException;
import myfood.Exception.NomeInvalidoException;
import myfood.Exception.ProibidoCadastrarEmpresasComMesmoNomeLocalException;
import myfood.Exception.SenhaInvalidoException;
import myfood.Exception.UsuarioNaoCadastradoException;
import myfood.Exception.UsuarioNaoPodeCriarEmpresaException;
import myfood.models.*;
import myfood.services.*;

import java.util.*;

public class Facade {
    private final Map<String, Usuario> usuarios;
    private final Map<Integer, Empresa> empresas;
    private final Map<Integer, Pedido> pedidos;
    private final ArmazenamentoService armazenamento;
    private final UsuarioService usuarioService;
    private final EmpresaService empresaService;
    private final ProdutoService produtoService;
    private PedidoService pedidoService;

    public Facade() {
        this.usuarios = new HashMap<>();
        this.empresas = new HashMap<>();
        this.pedidos = new HashMap<>();
        this.armazenamento = new ArmazenamentoService(usuarios, empresas, pedidos);
        this.usuarioService = new UsuarioService(usuarios, armazenamento);
        this.empresaService = new EmpresaService(usuarios, armazenamento, empresas);
        this.produtoService = new ProdutoService(empresas, armazenamento);
        this.pedidoService = new PedidoService(usuarioService, empresaService, produtoService, armazenamento);

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

    /* USUARIOS */

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

    /* EMPRESAS */

    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
            throws ProibidoCadastrarEmpresasComMesmoNomeLocalException, EmpresaComNomeJaExisteException,
            UsuarioNaoPodeCriarEmpresaException, UsuarioNaoCadastradoException {
        return empresaService.criarEmpresa(tipoEmpresa, dono, nome, endereco, tipoCozinha);
    }

    public String getEmpresasDoUsuario(int idDono)
            throws UsuarioNaoPodeCriarEmpresaException, UsuarioNaoCadastradoException {
        return empresaService.getEmpresasDoUsuario(idDono);
    }

    public int getIdEmpresa(int idDono, String nome, int indice)
            throws NomeInvalidoException, IndiceInvalidoException, UsuarioNaoPodeCriarEmpresaException,
            NaoExisteEmpresaComNomeException, IndiceMaiorQueEsperadoException, UsuarioNaoCadastradoException {
        return empresaService.getIdEmpresa(idDono, nome, indice);
    }

    public String getAtributoEmpresa(int empresa, String atributo)
            throws EmpresaNaoCadastradaException, AtributoInvalidoException {
        return empresaService.getAtributoEmpresa(empresa, atributo);
    }

    /* PRODUTOS */

    public int criarProduto(int empresa, String nome, float valor, String categoria) throws Exception {
        return produtoService.criarProduto(empresa, nome, valor, categoria);
    }

    public void editarProduto(int produto, String nome, float valor, String categoria) throws Exception {
        produtoService.editarProduto(produto, nome, valor, categoria);
    }

    public String getProduto(String nome, int empresa, String atributo) throws Exception {
        return produtoService.getProduto(nome, empresa, atributo);
    }

    public String listarProdutos(int empresa) throws Exception {
        return produtoService.listarProdutos(empresa);
    }

    /* PEDIDOS */
    public int criarPedido(int cliente, int empresa) {
        return pedidoService.criarPedido(cliente, empresa);
    }

    public void adicionarProduto(int numero, int produto) {
        pedidoService.adicionarProduto(numero, produto);
    }

    public String getPedidos(int pedido, String atributo) {
        return pedidoService.getPedidos(pedido, atributo);
    }

    public void fecharPedido(int numero) {
        pedidoService.fecharPedido(numero);
    }

    public void removerProduto(int pedido, String produto) {
        pedidoService.removerProduto(pedido, produto);
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) {
        return pedidoService.getNumeroPedido(cliente, empresa, indice);
    }

}
