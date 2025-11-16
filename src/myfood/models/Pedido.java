package myfood.models;

import myfood.services.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private int numero;
    private int clienteId;
    private int empresaId;
    private String estado;
    private List<Integer> produtosIds;
    private double valorTotal;

    private transient Cliente cliente;
    private transient Empresa empresa;
    private transient List<Produto> produtos;

    public Pedido() {
        this.produtosIds = new ArrayList<>();
        this.estado = "aberto";
    }

    public Pedido(int numero, Cliente cliente, Empresa empresa) {
        this();
        this.numero = numero;
        setCliente(cliente);
        setEmpresa(empresa);
    }

    // Métodos para definir as relações (atualizam tanto os objetos quanto os IDs)
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.clienteId = Integer.parseInt(cliente.getId());
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        this.empresaId = empresa.getId();
    }

    public void adicionarProduto(Produto produto) {
        if (this.produtos == null) {
            this.produtos = new ArrayList<>();
        }
        this.produtos.add(produto);
        this.produtosIds.add(produto.getId());
        this.valorTotal += produto.getValor();
    }

    public void removerProduto(String nomeProduto) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getNome().equalsIgnoreCase(nomeProduto)) {
                Produto removido = produtos.remove(i);
                produtosIds.remove(i);
                this.valorTotal -= removido.getValor();
                return;
            }
        }
        throw new IllegalArgumentException("Produto nao encontrado");
    }

    // Getters e Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Integer> getProdutosIds() {
        return produtosIds;
    }

    public void setProdutosIds(List<Integer> produtosIds) {
        this.produtosIds = produtosIds;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    // Método para reconstruir as relações após desserialização
    public void reconstruirRelacoes(UsuarioService usuarioService, EmpresaService empresaService,
            ProdutoService produtoService) {
        // Reconstruir cliente
        Usuario usuario = usuarioService.getUsuarioById(this.clienteId);
        if (usuario instanceof Cliente) {
            this.cliente = (Cliente) usuario;
        }

        // Reconstruir empresa
        this.empresa = empresaService.getEmpresaById(this.empresaId);

        // Reconstruir produtos
        this.produtos = new ArrayList<>();
        this.valorTotal = 0;
        for (Integer produtoId : this.produtosIds) {
            Produto produto = produtoService.getProdutoById(produtoId);
            if (produto != null) {
                this.produtos.add(produto);
                this.valorTotal += produto.getValor();
            }
        }
    }

    @Override
    public String toString() {
        return String.format("{[Pedido %d - ClienteId: %d - EmpresaId: %d - Estado: %s]}",
                numero, clienteId, empresaId, estado);
    }
}