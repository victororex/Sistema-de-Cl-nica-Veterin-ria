package com.uniube.clinica.ui;

import com.uniube.clinica.dominio.*;
import com.uniube.clinica.repositorio.Repositorio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ClinicaVeterinariaApp {

    private JFrame tela;

    private JTextField campoNomeDono, campoTelefoneDono, campoEnderecoDono;
    private DefaultTableModel modeloTabelaDonos;

    private JTextField campoNomePet, campoIdadePet, campoRacaPet;
    private JComboBox<String> comboTipoPet, comboDonoPet;
    private DefaultTableModel modeloTabelaPets;

    private JComboBox<String> comboPetConsulta;
    private JTextField campoDataConsulta;
    private JTextArea campoDescricaoConsulta;
    private DefaultTableModel modeloTabelaConsultas;

    public ClinicaVeterinariaApp() {
        inicializar();
    }

    private void inicializar() {
        tela = new JFrame("Clínica Veterinária — Sistema de Gerenciamento");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setSize(900, 600);
        tela.setLocationRelativeTo(null);
        tela.setLayout(new BorderLayout());
        tela.setResizable(false);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Donos", painelDonos());
        abas.addTab("Pets", painelPets());
        abas.addTab("Consultas", painelConsultas());
        abas.addTab("Buscar/Listagens", painelBusca());

        tela.add(abas, BorderLayout.CENTER);
        tela.setVisible(true);
    }



    private JPanel painelDonos() {
        JPanel painel = new JPanel(new BorderLayout());
        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        campoNomeDono = new JTextField(20);
        campoTelefoneDono = new JTextField(12);
        campoEnderecoDono = new JTextField(25);

        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0; c.gridy = 0;
        formulario.add(new JLabel("Nome:"), c);
        c.gridx = 1;
        formulario.add(campoNomeDono, c);

        c.gridx = 0; c.gridy = 1;
        formulario.add(new JLabel("Telefone:"), c);
        c.gridx = 1;
        formulario.add(campoTelefoneDono, c);

        c.gridx = 0; c.gridy = 2;
        formulario.add(new JLabel("Endereço:"), c);
        c.gridx = 1;
        formulario.add(campoEnderecoDono, c);

        JButton botaoCadastrar = new JButton("Cadastrar Dono");
        botaoCadastrar.addActionListener(e -> cadastrarDono());
        c.gridx = 1; c.gridy = 3;
        formulario.add(botaoCadastrar, c);

        painel.add(formulario, BorderLayout.NORTH);

        modeloTabelaDonos = new DefaultTableModel(new Object[]{"ID","Nome","Telefone","Endereço"}, 0);
        JTable tabelaDonos = new JTable(modeloTabelaDonos);
        tabelaDonos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel acoes = new JPanel();

        JButton botaoEditar = new JButton("Editar");
        botaoEditar.addActionListener(e -> editarDonoSelecionado(tabelaDonos));
        acoes.add(botaoEditar);

        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.addActionListener(e -> excluirDonoSelecionado(tabelaDonos));
        acoes.add(botaoExcluir);

        painel.add(new JScrollPane(tabelaDonos), BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);

        atualizarTabelaDonos();
        return painel;
    }

    private void cadastrarDono() {
        String nome = campoNomeDono.getText().trim();
        String telefone = campoTelefoneDono.getText().trim();
        String endereco = campoEnderecoDono.getText().trim();

        if (nome.isEmpty()) {
            erro("Nome do dono é obrigatório.");
            return;
        }

        Dono d = new Dono(nome, telefone, endereco);
        Repositorio.adicionarDono(d);
        atualizarTabelaDonos();
        atualizarComboDonos();
        campoNomeDono.setText("");
        campoTelefoneDono.setText("");
        campoEnderecoDono.setText("");
        info("Dono cadastrado com sucesso!");
    }

    private void editarDonoSelecionado(JTable tabela) {
        int row = tabela.getSelectedRow();
        if (row == -1) { erro("Selecione um dono."); return; }

        int id = (int) modeloTabelaDonos.getValueAt(row, 0);
        Dono d = Repositorio.buscarDonoPorId(id);

        if (d != null) {
            String novoNome = JOptionPane.showInputDialog(tela, "Nome:", d.getNome());
            if (novoNome == null || novoNome.trim().isEmpty()) return;

            String novoTelefone = JOptionPane.showInputDialog(tela, "Telefone:", d.getTelefone());
            if (novoTelefone == null) return;

            String novoEndereco = JOptionPane.showInputDialog(tela, "Endereço:", d.getEndereco());
            if (novoEndereco == null) return;

            d.setNome(novoNome);
            d.setTelefone(novoTelefone);
            d.setEndereco(novoEndereco);

            atualizarTabelaDonos();
            atualizarComboDonos();
            atualizarComboPetsConsulta();
            atualizarTabelaPets();
            info("Dono atualizado com sucesso!");
        }
    }

    private void excluirDonoSelecionado(JTable tabela) {
        int row = tabela.getSelectedRow();
        if (row == -1) { erro("Selecione um dono."); return; }

        int id = (int) modeloTabelaDonos.getValueAt(row, 0);
        Dono d = Repositorio.buscarDonoPorId(id);

        if (d != null) {
            for (Pet p : Repositorio.getPets()) {
                if (p.getDono().getId() == id) {
                    erro("Não é possível excluir: o dono possui pets cadastrados.");
                    return;
                }
            }
            Repositorio.removerDono(d);
            atualizarTabelaDonos();
            atualizarComboDonos();
            info("Dono excluído com sucesso!");
        }
    }

    private void atualizarTabelaDonos() {
        modeloTabelaDonos.setRowCount(0);
        for (Dono d : Repositorio.getDonos()) {
            modeloTabelaDonos.addRow(new Object[]{
                    d.getId(), d.getNome(), d.getTelefone(), d.getEndereco()
            });
        }
    }



    private JPanel painelPets() {
        JPanel painel = new JPanel(new BorderLayout());
        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        campoNomePet = new JTextField(15);
        campoIdadePet = new JTextField(4);
        campoRacaPet = new JTextField(12);

        comboTipoPet = new JComboBox<>(new String[]{"Cão","Gato","Outro"});
        comboDonoPet = new JComboBox<>();

        c.insets = new Insets(5,5,5,5);

        c.gridx = 0; c.gridy = 0;
        formulario.add(new JLabel("Nome do Pet:"), c);
        c.gridx = 1;
        formulario.add(campoNomePet, c);

        c.gridx = 0; c.gridy = 1;
        formulario.add(new JLabel("Idade:"), c);
        c.gridx = 1;
        formulario.add(campoIdadePet, c);

        c.gridx = 0; c.gridy = 2;
        formulario.add(new JLabel("Raça:"), c);
        c.gridx = 1;
        formulario.add(campoRacaPet, c);

        c.gridx = 0; c.gridy = 3;
        formulario.add(new JLabel("Tipo:"), c);
        c.gridx = 1;
        formulario.add(comboTipoPet, c);

        c.gridx = 0; c.gridy = 4;
        formulario.add(new JLabel("Dono:"), c);
        c.gridx = 1;
        formulario.add(comboDonoPet, c);

        JButton botaoCadastrarPet = new JButton("Cadastrar Pet");
        botaoCadastrarPet.addActionListener(e -> cadastrarPet());
        c.gridx = 1; c.gridy = 5;
        formulario.add(botaoCadastrarPet, c);

        painel.add(formulario, BorderLayout.NORTH);

        modeloTabelaPets = new DefaultTableModel(
                new Object[]{"ID","Nome","Tipo","Raça","Idade","Dono"}, 0
        );
        JTable tabelaPets = new JTable(modeloTabelaPets);
        tabelaPets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel acoes = new JPanel();

        JButton editar = new JButton("Editar");
        editar.addActionListener(e -> editarPetSelecionado(tabelaPets));
        acoes.add(editar);

        JButton excluir = new JButton("Excluir");
        excluir.addActionListener(e -> excluirPetSelecionado(tabelaPets));
        acoes.add(excluir);

        painel.add(new JScrollPane(tabelaPets), BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);

        atualizarComboDonos();
        atualizarTabelaPets();

        return painel;
    }

    private void cadastrarPet() {
        if (Repositorio.getDonos().isEmpty()) {
            erro("Cadastre pelo menos um dono antes de cadastrar um pet.");
            return;
        }

        String nome = campoNomePet.getText().trim();
        String idadeStr = campoIdadePet.getText().trim();
        String raca = campoRacaPet.getText().trim();
        String tipo = (String) comboTipoPet.getSelectedItem();
        String donoSelecionado = (String) comboDonoPet.getSelectedItem();

        if (nome.isEmpty() || idadeStr.isEmpty() || donoSelecionado == null) {
            erro("Preencha todos os campos obrigatórios.");
            return;
        }

        int idade;
        try {
            idade = Integer.parseInt(idadeStr);
            if (idade < 0) {
                erro("Idade deve ser um número inteiro não negativo.");
                return;
            }
        } catch (Exception e) {
            erro("Idade inválida.");
            return;
        }

        int idDono = Integer.parseInt(donoSelecionado.split("\\|")[0].trim());
        Dono dono = Repositorio.buscarDonoPorId(idDono);

        Pet p;
        switch (Objects.requireNonNull(tipo)) {
            case "Cão":  p = new Cachorro(nome, idade, raca, dono); break;
            case "Gato": p = new Gato(nome, idade, raca, dono); break;
            default:     p = new OutroPet(nome, idade, raca, dono); break;
        }

        Repositorio.adicionarPet(p);
        atualizarTabelaPets();
        atualizarComboPetsConsulta();
        campoNomePet.setText("");
        campoIdadePet.setText("");
        campoRacaPet.setText("");
        info("Pet cadastrado.");
    }

    private void editarPetSelecionado(JTable tabela) {
        int row = tabela.getSelectedRow();
        if (row == -1) { erro("Selecione um pet."); return; }

        int id = (int) modeloTabelaPets.getValueAt(row, 0);
        Pet p = Repositorio.buscarPetPorId(id);

        if (p != null) {
            String novoNome = JOptionPane.showInputDialog(tela, "Nome:", p.getNome());
            if (novoNome == null || novoNome.trim().isEmpty()) return;

            String novaIdadeStr = JOptionPane.showInputDialog(tela, "Idade:", p.getIdade());
            if (novaIdadeStr == null) return;

            int novaIdade;
            try {
                novaIdade = Integer.parseInt(novaIdadeStr.trim());
                if (novaIdade < 0) {
                    erro("Idade deve ser >= 0.");
                    return;
                }
            } catch (Exception e) {
                erro("Idade inválida.");
                return;
            }

            String novaRaca = JOptionPane.showInputDialog(tela, "Raça:", p.getRaca());
            if (novaRaca == null) return;

            String[] opcoesDono = gerarOpcoesDonos();
            String donoSel = (String) JOptionPane.showInputDialog(
                    tela, "Dono:", "Selecionar Dono",
                    JOptionPane.PLAIN_MESSAGE, null,
                    opcoesDono, opcoesDono[0]
            );
            if (donoSel == null) return;

            int idDono = Integer.parseInt(donoSel.split("\\|")[0].trim());
            Dono novoDono = Repositorio.buscarDonoPorId(idDono);

            p.setNome(novoNome);
            p.setIdade(novaIdade);
            p.setRaca(novaRaca);
            p.setDono(novoDono);

            atualizarTabelaPets();
            atualizarComboPetsConsulta();
            info("Pet atualizado.");
        }
    }

    private void excluirPetSelecionado(JTable tabela) {
        int row = tabela.getSelectedRow();
        if (row == -1) { erro("Selecione um pet."); return; }

        int id = (int) modeloTabelaPets.getValueAt(row, 0);
        Pet p = Repositorio.buscarPetPorId(id);
        if (p == null) return;

        for (Consulta c : Repositorio.getConsultas()) {
            if (c.getPet().getId() == id) {
                erro("Não é possível excluir: o pet possui consultas cadastradas.");
                return;
            }
        }

        Repositorio.removerPet(p);
        atualizarTabelaPets();
        atualizarComboPetsConsulta();
        info("Pet excluído.");
    }

    private void atualizarTabelaPets() {
        modeloTabelaPets.setRowCount(0);
        for (Pet p : Repositorio.getPets()) {
            modeloTabelaPets.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getTipo(),
                    p.getRaca(), p.getIdade(),
                    p.getDono().getNome()
            });
        }
    }

    private void atualizarComboDonos() {
        comboDonoPet.removeAllItems();
        for (Dono d : Repositorio.getDonos()) {
            comboDonoPet.addItem(d.getId() + " | " + d.getNome());
        }
    }

    private String[] gerarOpcoesDonos() {
        List<Dono> lista = Repositorio.getDonos();
        String[] arr = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            Dono d = lista.get(i);
            arr[i] = d.getId() + " | " + d.getNome();
        }
        return arr;
    }


    private JPanel painelConsultas() {
        JPanel painel = new JPanel(new BorderLayout());
        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        comboPetConsulta = new JComboBox<>();
        campoDataConsulta = new JTextField(16);
        campoDescricaoConsulta = new JTextArea(4, 20);
        campoDescricaoConsulta.setLineWrap(true);
        campoDescricaoConsulta.setWrapStyleWord(true);

        c.insets = new Insets(5,5,5,5);

        c.gridx = 0; c.gridy = 0;
        formulario.add(new JLabel("Pet:"), c);
        c.gridx = 1;
        formulario.add(comboPetConsulta, c);

        c.gridx = 0; c.gridy = 1;
        formulario.add(new JLabel("Data (dd/MM/yyyy HH:mm):"), c);
        c.gridx = 1;
        formulario.add(campoDataConsulta, c);

        c.gridx = 0; c.gridy = 2;
        formulario.add(new JLabel("Descrição:"), c);
        c.gridx = 1;
        formulario.add(new JScrollPane(campoDescricaoConsulta), c);

        JButton botaoRegistrarConsulta = new JButton("Registrar Consulta");
        botaoRegistrarConsulta.addActionListener(e -> cadastrarConsulta());
        c.gridx = 1; c.gridy = 3;
        formulario.add(botaoRegistrarConsulta, c);

        painel.add(formulario, BorderLayout.NORTH);

        modeloTabelaConsultas = new DefaultTableModel(
                new Object[]{"ID","Pet","Dono","Data","Descrição"}, 0
        );
        JTable tabelaConsultas = new JTable(modeloTabelaConsultas);
        tabelaConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel acoes = new JPanel();
        JButton excluirConsulta = new JButton("Excluir Consulta");
        excluirConsulta.addActionListener(e -> excluirConsultaSelecionada(tabelaConsultas));
        acoes.add(excluirConsulta);

        painel.add(new JScrollPane(tabelaConsultas), BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);

        atualizarComboPetsConsulta();
        atualizarTabelaConsultas();

        return painel;
    }

    private void cadastrarConsulta() {
        if (Repositorio.getPets().isEmpty()) {
            erro("Cadastre pelo menos um pet.");
            return;
        }

        String selecionado = (String) comboPetConsulta.getSelectedItem();
        String dataStr = campoDataConsulta.getText().trim();
        String descricao = campoDescricaoConsulta.getText().trim();

        if (selecionado == null || dataStr.isEmpty() || descricao.isEmpty()) {
            erro("Preencha todos os campos.");
            return;
        }

        int idPet = Integer.parseInt(selecionado.split("\\|")[0].trim());
        Pet p = Repositorio.buscarPetPorId(idPet);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date data;

        try {
            data = sdf.parse(dataStr);
        } catch (Exception ex) {
            erro("Formato de data inválido. Use dd/MM/yyyy HH:mm.");
            return;
        }

        Consulta c = new Consulta(p, data, descricao);
        Repositorio.adicionarConsulta(c);

        atualizarTabelaConsultas();
        campoDataConsulta.setText("");
        campoDescricaoConsulta.setText("");
        info("Consulta registrada.");
    }

    private void excluirConsultaSelecionada(JTable tabela) {
        int row = tabela.getSelectedRow();
        if (row == -1) { erro("Selecione uma consulta."); return; }

        int id = (int) modeloTabelaConsultas.getValueAt(row, 0);
        Consulta c = Repositorio.buscarConsultaPorId(id);

        if (c != null) {
            Repositorio.removerConsulta(c);
            atualizarTabelaConsultas();
            info("Consulta excluída.");
        }
    }

    private void atualizarTabelaConsultas() {
        modeloTabelaConsultas.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Consulta c : Repositorio.getConsultas()) {
            modeloTabelaConsultas.addRow(new Object[]{
                    c.getId(),
                    c.getPet().getNome(),
                    c.getPet().getDono().getNome(),
                    sdf.format(c.getData()),
                    c.getDescricao()
            });
        }
    }

    private void atualizarComboPetsConsulta() {
        comboPetConsulta.removeAllItems();
        for (Pet p : Repositorio.getPets()) {
            comboPetConsulta.addItem(
                    p.getId() + " | " + p.getNome() + " (" + p.getDono().getNome() + ")"
            );
        }
    }


    private JPanel painelBusca() {
        JPanel painel = new JPanel(new BorderLayout());
        JPanel topo = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JTextField campoBuscaPet = new JTextField(15);
        JTextField campoBuscaDono = new JTextField(15);

        JButton botaoBuscarPet = new JButton("Buscar Pet por Nome");
        JButton botaoBuscarPorDono = new JButton("Buscar Pets por Dono");

        DefaultTableModel modeloResultados = new DefaultTableModel(
                new Object[]{"ID","Nome","Tipo","Raça","Idade","Dono"}, 0
        );
        JTable tabelaResultados = new JTable(modeloResultados);

        botaoBuscarPet.addActionListener(e -> {
            String q = campoBuscaPet.getText().trim();
            if (q.isEmpty()) {
                erro("Digite o nome do pet.");
                return;
            }

            modeloResultados.setRowCount(0);
            List<Pet> encontrados = Repositorio.buscarPetsPorNome(q);
            for (Pet p : encontrados) {
                modeloResultados.addRow(new Object[]{
                        p.getId(), p.getNome(), p.getTipo(),
                        p.getRaca(), p.getIdade(), p.getDono().getNome()
                });
            }

            if (encontrados.isEmpty()) info("Nenhum pet encontrado com esse nome.");
        });

        botaoBuscarPorDono.addActionListener(e -> {
            String q = campoBuscaDono.getText().trim();
            if (q.isEmpty()) {
                erro("Digite o nome do dono.");
                return;
            }

            modeloResultados.setRowCount(0);
            List<Pet> encontrados = Repositorio.buscarPetsPorNomeDono(q);
            for (Pet p : encontrados) {
                modeloResultados.addRow(new Object[]{
                        p.getId(), p.getNome(), p.getTipo(),
                        p.getRaca(), p.getIdade(), p.getDono().getNome()
                });
            }

            if (encontrados.isEmpty()) info("Nenhum pet encontrado para esse dono.");
        });

        c.insets = new Insets(5,5,5,5);

        c.gridx = 0; c.gridy = 0;
        topo.add(new JLabel("Nome do Pet:"), c);
        c.gridx = 1;
        topo.add(campoBuscaPet, c);
        c.gridx = 2;
        topo.add(botaoBuscarPet, c);

        c.gridx = 0; c.gridy = 1;
        topo.add(new JLabel("Nome do Dono:"), c);
        c.gridx = 1;
        topo.add(campoBuscaDono, c);
        c.gridx = 2;
        topo.add(botaoBuscarPorDono, c);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaResultados), BorderLayout.CENTER);

        return painel;
    }


    private void erro(String msg) {
        JOptionPane.showMessageDialog(tela, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(tela, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void preencherDadosExemplo() {
        Dono d1 = new Dono("Mariana Silva", "34-99911-2233", "Av. Brasil, 123");
        Dono d2 = new Dono("Carlos Souza", "34-98877-6655", "Rua Afonso, 45");

        Repositorio.adicionarDono(d1);
        Repositorio.adicionarDono(d2);

        Pet p1 = new Cachorro("Princesa", 3, "Pitbull", d1);
        Pet p2 = new Gato("Destruidor", 2, "SRD", d2);
        Pet p3 = new OutroPet("Roberto", 1, "Cobaya", d1);

        Repositorio.adicionarPet(p1);
        Repositorio.adicionarPet(p2);
        Repositorio.adicionarPet(p3);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Repositorio.adicionarConsulta(new Consulta(p1, sdf.parse("10/12/2025 14:00"), "Vacinação"));
            Repositorio.adicionarConsulta(new Consulta(p2, sdf.parse("11/12/2025 09:30"), "Check-up"));
        } catch (Exception ignored) { }
    }

    public static void main(String[] args) {
        preencherDadosExemplo();
        SwingUtilities.invokeLater(ClinicaVeterinariaApp::new);
    }
}