/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package db_app;

import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTree;
import javax.swing.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
/**
 *
 * @author Matheus Cruz e Rudinei Kuznier
 * 
 */
public class home_screen extends javax.swing.JFrame {

    /**
     * Creates new form home_screen
     */
    
    DefaultTreeModel model;
    String nodeData;
    
    public home_screen() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); //janela maximizada
        DefaultMutableTreeNode databases = new DefaultMutableTreeNode("Databases"); //inicia o nó raiz
        loadDataJTree(databases);
    }
    
    private void loadDataJTree(DefaultMutableTreeNode root){
        try (Connection connection = DatabaseConnection.getConnection("")) {
            
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet databases = metaData.getCatalogs();
            
            while (databases.next()) {

                   String databaseName = databases.getString(1);
                   DefaultMutableTreeNode databaseNode = new DefaultMutableTreeNode(databaseName);
                   root.add(databaseNode);

                   //tenta realizar a conexão
                   try (Connection dbConnection = DatabaseConnection.getConnection(databaseName)) { //recebe as tabelas para listar
                       DatabaseMetaData dbMetaData = dbConnection.getMetaData();
                       ResultSet tables = dbMetaData.getTables(databaseName, null, "%", new String[]{"TABLE"});
                        while (tables.next()) {
                           String tableName = tables.getString("TABLE_NAME");
                           DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(tableName);
                           databaseNode.add(tableNode);
                           
                           ResultSet columns = dbMetaData.getColumns(databaseName, null, tableName, "%");
                            while (columns.next()) {
                               String columnName = columns.getString("COLUMN_NAME");
                               String columnType = columns.getString("TYPE_NAME");
                               int columnSize = columns.getInt("COLUMN_SIZE");
                               String columnDetail = columnName + " - " + columnType + " (" + columnSize + ")";
                               DefaultMutableTreeNode columnNode = new DefaultMutableTreeNode(columnDetail);
                               tableNode.add(columnNode);
                            }
                        }
                   }
            }
        model = (DefaultTreeModel) jTree2.getModel();
        
        model.setRoot(root);    
        
        jTree2.setModel(model);
        
        }   catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        model.reload(); //notifica alteracao da estrutura
    }
    
    //Carrega a tabela a partir dos SELECT realizados
    private void loadData( String queryData, String databaseNameData, int rowsNumber){
        
          String query = queryData.toLowerCase(); //seta a query para letras minusculas
          String databaseName = databaseNameData;
          int flagRow = 0; //contador para definir a quantidade de linhas a visualizar
          
          //teste para quando o usuario utilizar o comando "use"
          /*
          if (queryData.startsWith("use"))
          {
              
            databaseName = query.split("\\s+")[1]; // Obtém o nome do banco de dados
            databaseName = databaseName.replaceAll(";", ""); // Remove o ponto e vírgula, se existir
              
          }
          else
          {
              databaseName = databaseNameData;
          }
          */
          
          try (Connection conn = DatabaseConnection.getConnection(databaseName);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            String[] columnNames = new String[columnCount]; //nomes das colunas
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            DefaultTableModel  model = new DefaultTableModel(columnNames, 0);

            //preenchimento da JTable
            while (rs.next() && flagRow < rowsNumber) { 
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
                flagRow++;
            }

            jTable1.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
          
    }  
    
    //Carrega os dados das tabelas específicas queando o usuário dá dois clicks na tabela
    private void loadDataFromSpecificTable( String tableName, String databaseName){
         String query = "SELECT * FROM " + tableName;
         
          try (Connection conn = DatabaseConnection.getConnection(databaseName);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            DefaultTableModel  model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            jTable1.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

          

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTree2 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree2.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree2MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree2MousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTree2);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Liberation Mono", 0, 17)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setText("Executar query");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Database: ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jButton1.setText("Exportar consulta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10", "100", "1000" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Número de linhas");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    //Evento para abrir as tabelas do banco ///
    private void jTree2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree2MouseClicked
        //Evento acionado com 2 clicks do mouse
        if (evt.getClickCount() == 2)
        {
            TreePath path = jTree2.getPathForLocation(evt.getX(), evt.getY()); //caminho clicado
            if (path != null)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent(); //nó selecionado
                // Verificando se o nó é uma pasta
                if (node.getAllowsChildren())
                {
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                  
                    if (!("Databases".equals(parent.toString())))
                    {
                               JOptionPane.showMessageDialog(this, "Você clicou duas vezes na pasta: " + node.getUserObject() + "\nPasta pai: " + parent.getUserObject());
                              
                              // loadDataFromSpecificTable(node.getUserObject().toString(), parent.getUserObject().toString());
                    }
                            
                }
            }
        }
    }//GEN-LAST:event_jTree2MouseClicked

    //Método para exportar tabelas CSV
    public static void exportCSV(JTable table, String path) throws IOException{
        TableModel model = table.getModel();
        FileWriter csv = new FileWriter(path);

        // Escreve cabeçalhos
        for (int i = 0; i < model.getColumnCount(); i++) {
            csv.write(model.getColumnName(i) + ",");
        }
        csv.write("\n");

        // Escreve dados
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                csv.write(model.getValueAt(i, j).toString() + ",");
            }
            csv.write("\n");
        }

        csv.close();
    }
     
    //botao exportar consulta
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            exportCSV(jTable1, "dados.csv");
            JOptionPane.showMessageDialog(this, "Dados exportados com sucesso!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar dados: " + ex);
        }
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String numRowsToShowStr =  (String) jComboBox1.getSelectedItem();
        int numRowsToShow = Integer.parseInt(numRowsToShowStr); //converte valor da comboBox
        loadData(jTextArea1.getText(), nodeData, numRowsToShow); //executa o preenchimento da JTable com a quantidade de linhas escolhida
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTree2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree2MousePressed
        //Evento quando seleciono com o botao direito do mouse 
        if (SwingUtilities.isRightMouseButton(evt)) {
            TreePath path = jTree2.getPathForLocation(evt.getX(), evt.getY()); //caminho clicado

            if (path != null)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent(); //nó selecionado

                // Verificando se o nó é uma pasta
                if (node.getAllowsChildren())
                {
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

                    //Verifica se o caminho clicado é um banco pertencente a Pasta Databases
                    nodeData = ((DefaultMutableTreeNode) node).getUserObject().toString();
                    
                    int choice = JOptionPane.showConfirmDialog(this,"Deseja utilizar o banco: " + node + "?","Confirmação",JOptionPane.YES_NO_OPTION);
                    
                        //Verifica se o usuário aceitou utilizar o banco selecionado.
                        if (choice == JOptionPane.YES_OPTION)
                            jLabel2.setText(nodeData); 
                        else
                            jLabel2.setText("");                             
                }
            }
        }
    }//GEN-LAST:event_jTree2MousePressed

    //Evento de atualização dos dados a partir da comboBox
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
         String numRowsToShowStr =  (String) jComboBox1.getSelectedItem();
         int numRowsToShow = Integer.parseInt(numRowsToShowStr);
         loadData(jTextArea1.getText(), nodeData, numRowsToShow); 
         
    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]){
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(home_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(home_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(home_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(home_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
 // Configurar o tema escuro do JTattoo
           // UIManager.setLookAndFeel(new AcrylLookAndFeel());
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new home_screen().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTree jTree2;
    // End of variables declaration//GEN-END:variables
}
