package myfood.services;

import myfood.models.*;
import java.io.*;
import java.beans.*;
import java.util.*;

public class ArmazenamentoService {
    private Map<String, Usuario> usuarios;
    private int idAtual;

    private static final String CAMINHO_USUARIOS = "data/usuarios.xml";

    public ArmazenamentoService(Map<String, Usuario> usuarios, int id) {
        this.usuarios = usuarios;
        this.idAtual = id;
    }

    public void salvarSistema() throws Exception {
        persistirUsuarios();
    }

    public void carregarSistema() throws Exception {
        restaurarUsuarios();
    }

    public void zerarSistema() {
        usuarios.clear();
        idAtual = 0;
    }

    public void encerrarSistema() throws Exception {
        salvarSistema();
    }

    private void persistirUsuarios() throws Exception {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(CAMINHO_USUARIOS)))) {
            encoder.writeObject(usuarios);
            encoder.writeObject(idAtual);
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private void restaurarUsuarios() throws Exception {
        File arquivo = new File(CAMINHO_USUARIOS);
        if (!arquivo.exists())
            return;

        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)))) {
            Map<String, Usuario> recuperados = (Map<String, Usuario>) decoder.readObject();
            int novoId = (int) decoder.readObject();
            usuarios.clear();
            usuarios.putAll(recuperados);
            idAtual = novoId;
        }
    }
}
