/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads3.agenda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fernando.tsuda
 */
public class Agenda extends ConexaoBD {

    private static Scanner entrada = new Scanner(System.in);

    public void incluir() {

        System.out.print("Digite o nome completo do contato: ");
        String nome = entrada.nextLine();

        System.out.print("Digite a data de nascimento no formato dd/mm/aaaa: ");
        String strDataNasc = entrada.nextLine();

        System.out.print("Digite o e-mail: ");
        String email = entrada.nextLine();

        System.out.print("Digite o telefone no formato 99 99999-9999: ");
        String telefone = entrada.nextLine();

        // 1) Abrir conexao
        PreparedStatement stmt = null;
        Connection conn = null;

        String sql = "INSERT INTO TB_CONTATO (NM_CONTATO, DT_NASCIMENTO,VL_TELEFONE, VL_EMAIL, DT_CADASTRO) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            conn = obterConexao();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);

            DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            Date dataNasc = null;
            try {
                dataNasc = formatador.parse(strDataNasc);
            } catch (ParseException ex) {
                System.out.println("Data de nascimento inválida.");
                return;
            }
            stmt.setDate(2, new java.sql.Date(dataNasc.getTime()));
            stmt.setString(3, telefone);
            stmt.setString(4, email);
            stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));

            // 2) Executar SQL
            stmt.executeUpdate();
            System.out.println("Contato cadastrado com sucesso");

        } catch (SQLException e) {
            System.out.println("Não foi possível executar.");
        } catch (ClassNotFoundException e) {
            System.out.println("Não foi possível executar.");
            // 3) Fechar conexao
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fechar stmt.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fechar conn.");
                }
            }
        }
    }

    public void listarContatos() {

        // 1) Abrir conexao
        Statement stm = null;
        Connection conn = null;
        ResultSet rs = null;

        String sql = "(SELECT * FROM TB_CONTATO)";

        try {
            conn = obterConexao();
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);

            System.out.println("********** LISTA DE CONTATOS **********");
            while (rs.next()) {
                System.out.println("ID : " + rs.getString(1));
                System.out.println("Nome : " + rs.getString(2));
                System.out.println("Data Nasc : " + rs.getString(3));
                System.out.println("Telefone: " + rs.getString(4));
                System.out.println("E-mail: " + rs.getString(5));
                System.out.println("Data Cadastro: " + rs.getString(6));
                System.out.println("==========================================");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fechar stmt.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fechar conn.");
                }
            }
        }
    }

    public void deletar() {

        System.out.println("Informe o ID do contato a ser Excluido: ");
        int id = entrada.nextInt();

        PreparedStatement stmt = null;
        Connection conn = null;

        String sql = "DELETE FROM TB_CONTATO WHERE ID_CONTATO = " + id;

        try {
            conn = obterConexao();
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("************** deletado com sucesso ****************\n");
        } catch (Exception ex) {
            System.out.println("erro 1" + ex.getMessage());
        }

    }

    public void editar() {

        System.out.println("Informe o ID do contato a ser Editado: ");
        int id = entrada.nextInt();

        PreparedStatement stmt = null;
        Connection conn = null;
        String sql = null;

        System.out.println("***** DIGITE UMA OPÇÃO *****");
        System.out.println("(1) Editar Nome");
        System.out.println("(2) Editar Data Nasc.");
        System.out.println("(3) Editar Email");
        System.out.println("(4) Editar Telefone");
        System.out.println("(9) Sair");
        System.out.println("Opção: ");
        System.out.println("==========================================");
        entrada = new Scanner(System.in);
        String strOpcao = entrada.nextLine();
        int opcao = Integer.parseInt(strOpcao);
        switch (opcao) {
            case 1:
                System.out.print("Digite o nome completo do contato: ");
                String nome = entrada.nextLine();
                sql = "UPDATE TB_CONTATO SET NM_CONTATO = '" + nome + "' WHERE ID_CONTATO= " + id;
                break;
            case 2:
                System.out.print("Digite a data de nascimento no formato dd/mm/aaaa: ");
                String strDataNasc = entrada.nextLine();
                sql = "UPDATE TB_CONTATO SET DT_NASCIMENTO = '" + strDataNasc + "' WHERE ID_CONTATO= " + id;
                break;
            case 3:
                System.out.print("Digite o e-mail: ");
                String email = entrada.nextLine();
                sql = "UPDATE TB_CONTATO SET VL_EMAIL = '" + email + "' WHERE ID_CONTATO= " + id;
                break;
            case 4:
                System.out.print("Digite o telefone no formato 99 99999-9999: ");
                String telefone = entrada.nextLine();
                sql = "UPDATE TB_CONTATO SET VL_TELEFONE = '" + telefone + "' WHERE ID_CONTATO= " + id;
                break;
            case 9:
                System.exit(0);
                break;
            default:
                System.out.println("OPÇÃO INVÁLIDA");
        }

        try {
            conn = obterConexao();
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("************** Alterado com sucesso ****************");
        } catch (Exception ex) {
            System.out.println("erro 1" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Agenda instancia = new Agenda();

        do {
            System.out.println("***** DIGITE UMA OPÇÃO *****");
            System.out.println("(1) Listar contatos");
            System.out.println("(2) Incluir novo contato");
            System.out.println("(3) Deletar contato");
            System.out.println("(4) Editar contato");
            System.out.println("(9) Sair");
            System.out.println("Opção: ");
            System.out.println("==========================================");
            entrada = new Scanner(System.in);
            String strOpcao = entrada.nextLine();
            int opcao = Integer.parseInt(strOpcao);
            switch (opcao) {
                case 1:
                    instancia.listarContatos();
                    break;
                case 2:
                    instancia.incluir();
                    break;
                case 3:
                    instancia.deletar();
                    break;
                case 4:
                    instancia.editar();
                    break;
                case 9:
                    System.exit(0);
                    break;
                default:
                    System.out.println("OPÇÃO INVÁLIDA");
            }
        } while (true);

    }

}
