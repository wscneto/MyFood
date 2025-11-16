package myfood.services;

import myfood.Exception.NomeInvalidoException;
import myfood.models.*;
import java.util.*;

public class ProdutoService {
    private final Map<Integer, Empresa> empresas;
    private final Map<Integer, Produto> produtos = new HashMap<>();
    private final ArmazenamentoService armazenamento;
    private int nextProdutoId = 1;

    public ProdutoService(Map<Integer, Empresa> empresas, ArmazenamentoService armazenamento) {
        this.empresas = empresas;
        this.armazenamento = armazenamento;
        for (Empresa e : empresas.values()) {
            if (e.getProdutos() != null) {
                for (Produto p : e.getProdutos()) {
                    produtos.put(p.getId(), p);
                    if (p.getId() >= nextProdutoId)
                        nextProdutoId = p.getId() + 1;
                }
            }
        }
    }

    public void zerarSistema() {
        produtos.clear();
        nextProdutoId = 1;
    }

    public Produto getProdutoById(int id) {
        return produtos.get(id);
    }

    public int criarProduto(int empresaId, String nome, float valor, String categoria) throws Exception {
        Empresa empresa = empresas.get(empresaId);
        if (empresa == null)
            throw new Exception("Empresa nao encontrada");

        if (nome == null || nome.trim().isEmpty())
            throw new Exception("Nome invalido");

        if (valor < 0)
            throw new Exception("Valor invalido");

        if (categoria == null || categoria.trim().isEmpty())
            throw new Exception("Categoria invalido");

        for (Produto p : empresa.getProdutos()) {
            if (p.getNome().equalsIgnoreCase(nome))
                throw new Exception("Ja existe um produto com esse nome para essa empresa");
        }

        Produto produto = new Produto(nextProdutoId++, nome, valor, categoria, empresa);
        produtos.put(produto.getId(), produto);
        empresa.getProdutos().add(produto);

        armazenamento.salvarSistema();
        return produto.getId();
    }

    public void editarProduto(int id, String nome, float valor, String categoria) throws Exception {
        Produto produto = produtos.get(id);
        if (produto == null)
            throw new Exception("Produto nao cadastrado");

        if (nome == null || nome.trim().isEmpty())
            throw new NomeInvalidoException();

        if (valor < 0)
            throw new Exception("Valor invalido");

        if (categoria == null || categoria.trim().isEmpty())
            throw new Exception("Categoria invalido");

        produto.setNome(nome);
        produto.setValor(valor);
        produto.setCategoria(categoria);

        armazenamento.salvarSistema();
    }

    public String getProduto(String nome, int empresaId, String atributo) throws Exception {
        Empresa empresa = empresas.get(empresaId);
        if (empresa == null)
            throw new Exception("Empresa nao encontrada");

        Produto produto = null;
        for (Produto p : empresa.getProdutos()) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                produto = p;
                break;
            }
        }

        if (produto == null)
            throw new Exception("Produto nao encontrado");

        switch (atributo) {
            case "valor":
                return String.format(java.util.Locale.US, "%.2f", produto.getValor());
            case "categoria":
                return produto.getCategoria();
            case "empresa":
                return produto.getEmpresa().getNome();
            default:
                throw new Exception("Atributo nao existe");
        }
    }

    public String listarProdutos(int empresaId) throws Exception {
        Empresa empresa = empresas.get(empresaId);
        if (empresa == null)
            throw new Exception("Empresa nao encontrada");

        List<String> nomes = new ArrayList<>();
        for (Produto p : empresa.getProdutos()) {
            nomes.add(p.getNome());
        }

        return "{[" + String.join(", ", nomes) + "]}";
    }
}
