package myfood.services;

import myfood.models.*;
import java.io.*;
import java.beans.*;
import java.util.*;

public class ArmazenamentoService {
    private final Map<String, Usuario> usuarios;
    private final Map<Integer, Empresa> empresas;
    private final String pastaData = "data";
    private final String arquivoUsuarios = pastaData + "/usuarios.xml";
    private final String arquivoEmpresas = pastaData + "/empresas.xml";

    public ArmazenamentoService(Map<String, Usuario> usuarios, Map<Integer, Empresa> empresas) {
        this.usuarios = usuarios;
        this.empresas = empresas;

        File pasta = new File(pastaData);
        if (!pasta.exists())
            pasta.mkdirs();
    }

    /** Remove todos os dados do sistema */
    public void zerarSistema() {
        usuarios.clear();
        empresas.clear();
        File u = new File(arquivoUsuarios);
        File e = new File(arquivoEmpresas);
        if (u.exists())
            u.delete();
        if (e.exists())
            e.delete();
    }

    /** Salva todos os dados (usuários + empresas) */
    public void salvarSistema() {
        salvarUsuarios();
        salvarEmpresas();
    }

    /** Carrega os dados de ambos os arquivos (se existirem) */
    public void carregarSistema() throws IOException {
        carregarUsuarios();
        carregarEmpresas();
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
            for (Usuario u : lista) {
                usuarios.put(u.getEmail(), u);
            }
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
            for (Empresa e : lista) {
                empresas.put(e.getId(), e);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar empresas: " + e.getMessage());
        }
    }
}
