package myfood.services;

import myfood.models.*;
import java.io.*;
import java.beans.*;
import java.util.*;

public class ArmazenamentoService {
    private final Map<String, Usuario> usuarios;
    private final Map<Integer, Empresa> empresas;
    private final Map<Integer, Pedido> pedidos;

    private final String pastaData = "data";
    private final String arquivoUsuarios = pastaData + "/usuarios.xml";
    private final String arquivoEmpresas = pastaData + "/empresas.xml";
    private final String arquivoPedidos = pastaData + "/pedidos.xml";

    public ArmazenamentoService(Map<String, Usuario> usuarios, Map<Integer, Empresa> empresas,
            Map<Integer, Pedido> pedidos) {
        this.usuarios = usuarios;
        this.empresas = empresas;
        this.pedidos = pedidos;

        File pasta = new File(pastaData);
        if (!pasta.exists())
            pasta.mkdirs();
    }

    /** Remove todos os dados do sistema */
    public void zerarSistema() {
        usuarios.clear();
        empresas.clear();
        pedidos.clear();
        File u = new File(arquivoUsuarios);
        File e = new File(arquivoEmpresas);
        File p = new File(arquivoPedidos);
        if (u.exists())
            u.delete();
        if (e.exists())
            e.delete();
        if (p.exists())
            p.delete();
    }

    /** Salva todos os dados (usuários + empresas + pedidos) */
    public void salvarSistema() {
        salvarUsuarios();
        salvarEmpresas();
        salvarPedidos();
    }

    /** Carrega os dados de todos os arquivos (se existirem) */
    public void carregarSistema() throws IOException {
        carregarUsuarios();
        carregarEmpresas();
        carregarPedidos();
    }

    /** Encerrar o sistema e persistir tudo */
    public void encerrarSistema() throws Exception {
        salvarSistema();
    }

    // ---------------- USUÁRIOS ---------------- //
    private void salvarUsuarios() {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivoUsuarios)))) {
            encoder.writeObject(new ArrayList<>(usuarios.values()));
        } catch (Exception e) {
            System.out.println("Erro ao salvar usuarios: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarUsuarios() {
        File file = new File(arquivoUsuarios);
        if (!file.exists())
            return;
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)))) {
            List<Usuario> lista = (List<Usuario>) decoder.readObject();
            usuarios.clear();
            for (Usuario u : lista)
                usuarios.put(u.getEmail(), u);
        } catch (Exception e) {
            System.out.println("Erro ao carregar usuarios: " + e.getMessage());
        }
    }

    // ---------------- EMPRESAS ---------------- //
    private void salvarEmpresas() {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivoEmpresas)))) {
            encoder.writeObject(new ArrayList<>(empresas.values()));
        } catch (Exception e) {
            System.out.println("Erro ao salvar empresas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarEmpresas() {
        File file = new File(arquivoEmpresas);
        if (!file.exists())
            return;
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)))) {
            List<Empresa> lista = (List<Empresa>) decoder.readObject();
            empresas.clear();
            for (Empresa e : lista)
                empresas.put(e.getId(), e);
        } catch (Exception e) {
            System.out.println("Erro ao carregar empresas: " + e.getMessage());
        }
    }

    // ---------------- PEDIDOS ---------------- //
    private void salvarPedidos() {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivoPedidos)))) {
            encoder.writeObject(new ArrayList<>(pedidos.values()));
        } catch (Exception e) {
            System.out.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarPedidos() {
        File file = new File(arquivoPedidos);
        if (!file.exists())
            return;
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)))) {
            List<Pedido> lista = (List<Pedido>) decoder.readObject();
            pedidos.clear();
            for (Pedido p : lista)
                pedidos.put(p.getNumero(), p);
        } catch (Exception e) {
            System.out.println("Erro ao carregar pedidos: " + e.getMessage());
        }
    }
}
