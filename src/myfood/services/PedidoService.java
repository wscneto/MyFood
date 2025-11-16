package myfood.services;

import java.util.*;
import myfood.models.*;

public class PedidoService {
    private Map<Integer, Pedido> pedidos = new HashMap<>();
    private int contadorPedidos = 1;

    private UsuarioService usuarioService;
    private EmpresaService empresaService;
    private ProdutoService produtoService;
    private ArmazenamentoService armazenamentoService;

    public PedidoService(UsuarioService usuarioService, EmpresaService empresaService,
            ProdutoService produtoService, ArmazenamentoService armazenamentoService) {
        this.usuarioService = usuarioService;
        this.empresaService = empresaService;
        this.produtoService = produtoService;
        this.armazenamentoService = armazenamentoService;
    }

    public int criarPedido(int idCliente, int idEmpresa) {
        Usuario user = usuarioService.getUsuarioById(idCliente);
        if (!(user instanceof Cliente))
            throw new IllegalArgumentException("Dono de empresa nao pode fazer um pedido");

        Empresa empresa = empresaService.getEmpresaById(idEmpresa);

        // Verifica se já existe um pedido aberto entre cliente e empresa
        for (Pedido p : pedidos.values()) {
            if (p.getCliente().equals(user) && p.getEmpresa().equals(empresa) && p.getEstado().equals("aberto"))
                throw new IllegalArgumentException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
        }

        Pedido pedido = new Pedido(contadorPedidos++, (Cliente) user, empresa);
        pedidos.put(pedido.getNumero(), pedido);
        salvar(); // <<< PERSISTE APÓS CRIAR
        return pedido.getNumero();
    }

    public void adicionarProduto(int numero, int idProduto) {
        Pedido pedido = pedidos.get(numero);
        if (pedido == null)
            throw new IllegalArgumentException("Nao existe pedido em aberto");
        if (!pedido.getEstado().equals("aberto"))
            throw new IllegalArgumentException("Nao e possivel adcionar produtos a um pedido fechado");

        Produto produto = produtoService.getProdutoById(idProduto);
        if (produto == null)
            throw new IllegalArgumentException("Produto nao encontrado");

        if (!produto.getEmpresa().equals(pedido.getEmpresa()))
            throw new IllegalArgumentException("O produto nao pertence a essa empresa");

        pedido.adicionarProduto(produto);
        salvar(); // <<< PERSISTE APÓS ADICIONAR PRODUTO
    }

    public String getPedidos(int numero, String atributo) {
        Pedido pedido = pedidos.get(numero);
        if (pedido == null)
            throw new IllegalArgumentException("Pedido nao encontrado");
        if (atributo == null || atributo.isBlank())
            throw new IllegalArgumentException("Atributo invalido");

        switch (atributo) {
            case "cliente":
                return pedido.getCliente().getNome();
            case "empresa":
                return pedido.getEmpresa().getNome();
            case "estado":
                return pedido.getEstado();
            case "produtos":
                List<String> nomes = new ArrayList<>();
                for (Produto p : pedido.getProdutos())
                    nomes.add(p.getNome());
                return "{[" + String.join(", ", nomes) + "]}";
            case "valor":
                return String.format(java.util.Locale.US, "%.2f", pedido.getValorTotal());
            default:
                throw new IllegalArgumentException("Atributo nao existe");
        }
    }

    public void fecharPedido(int numero) {
        Pedido pedido = pedidos.get(numero);
        if (pedido == null)
            throw new IllegalArgumentException("Pedido nao encontrado");
        pedido.setEstado("preparando");
        salvar(); // <<< PERSISTE APÓS FECHAR
    }

    public void removerProduto(int numero, String nomeProduto) {
        Pedido pedido = pedidos.get(numero);
        if (pedido == null)
            throw new IllegalArgumentException("Pedido nao encontrado");
        if (!pedido.getEstado().equals("aberto"))
            throw new IllegalArgumentException("Nao e possivel remover produtos de um pedido fechado");
        if (nomeProduto == null || nomeProduto.isBlank())
            throw new IllegalArgumentException("Produto invalido");

        pedido.removerProduto(nomeProduto);
        salvar(); // <<< PERSISTE APÓS REMOVER PRODUTO
    }

    public int getNumeroPedido(int idCliente, int idEmpresa, int indice) {
        Usuario user = usuarioService.getUsuarioById(idCliente);
        Empresa emp = empresaService.getEmpresaById(idEmpresa);

        List<Pedido> lista = new ArrayList<>();
        for (Pedido p : pedidos.values()) {
            if (p.getCliente().equals(user) && p.getEmpresa().equals(emp))
                lista.add(p);
        }
        lista.sort(Comparator.comparingInt(Pedido::getNumero));

        return lista.get(indice).getNumero();
    }

    public void zerar() {
        pedidos.clear();
        contadorPedidos = 1;
        salvar(); // <<< PERSISTE APÓS LIMPAR
    }

    private void salvar() {
        armazenamentoService.salvarSistema(); // mesmo padrão usado nos outros services
    }

    public Map<Integer, Pedido> getPedidosMap() {
        return pedidos;
    }
}
