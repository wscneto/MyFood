package myfood.models;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int numero;
    private Cliente cliente;
    private Empresa empresa;
    private String estado;
    private List<Produto> produtos;

    public Pedido(int numero, Cliente cliente, Empresa empresa) {
        this.numero = numero;
        this.cliente = cliente;
        this.empresa = empresa;
        this.estado = "aberto";
        this.produtos = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    public void removerProduto(String nomeProduto) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getNome().equalsIgnoreCase(nomeProduto)) {
                produtos.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Produto nao encontrado");
    }

    public double getValorTotal() {
        double total = 0;
        for (Produto p : produtos) {
            total += p.getValor();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("{[Pedido %d - %s - %s]}", numero, cliente.getNome(), empresa.getNome());
    }
}
