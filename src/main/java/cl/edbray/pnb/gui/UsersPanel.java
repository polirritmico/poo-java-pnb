/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cl.edbray.pnb.gui;

import cl.edbray.pnb.app.ApplicationContext;
import cl.edbray.pnb.controller.UserController;
import cl.edbray.pnb.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author eduardo
 */
public class UsersPanel extends javax.swing.JPanel {

    private final UserController controller;

    private User selectedUser;
    private UserTableModel tableModel;

    /**
     * Creates new form UsersPanel
     */
    public UsersPanel() {
        controller = ApplicationContext.getInstance().getUserController();

        initComponents();
        setupTable();
        setupListeners();
        loadUsers();
        cleanForm();
    }

    private void setupTable() {
        tableModel = new UserTableModel();
        usersTable.setModel(tableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        usersTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        usersTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        usersTable.getColumnModel().getColumn(4).setPreferredWidth(60);
    }

    private void loadUsers() {
        try {
            List<User> users = controller.listAll();
            tableModel.setUsers(users);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al cargar usuarios: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cleanForm() {
        selectedUser = null;
        usernameField.setText("");
        passwordField.setText("");
        fullNameField.setText("");
        roleComboBox.setSelectedIndex(0);
        activeCheck.setSelected(true);
        deleteButton.setEnabled(false);
        usernameField.requestFocus();
    }

    private void setupListeners() {
        usersTable.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                selectUserInTable();
            }
        });

        usersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent ev) {
                if (ev.getClickCount() == 2) {
                    editRowUser();
                }
            }
        });

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent ev) {
                filterUsers();
            }

            @Override
            public void removeUpdate(DocumentEvent ev) {
                filterUsers();
            }

            @Override
            public void changedUpdate(DocumentEvent ev) {
            }

            private void filterUsers() {
                String search = searchField.getText().trim();
                tableModel.setUsers(controller.search(search));
            }
        });
    }

    private void selectUserInTable() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedUser = tableModel.getUserAt(selectedRow);
            loadInForm(selectedUser);
        }
    }

    private void editRowUser() {
        selectUserInTable();
        usernameField.requestFocus();
    }

    private void loadInForm(User user) {
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        fullNameField.setText(user.getFullName());
        roleComboBox.setSelectedItem(user.getRole());
        activeCheck.setSelected(user.isActive());

        deleteButton.setEnabled(true);
    }

    private void updateUser(
        User user,
        String username,
        String password,
        String fullName,
        String role,
        boolean active) {
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setRole(role);
        user.setActive(active);
    }

    private boolean validateNewUserForm() {
        if (!validateUsernameLength()) {
            return false;
        }
        if (!validateUsernameIsAvaliable()) {
            return false;
        }
        if (!validatePassword()) {
            return false;
        }
        if (!validateFullName()) {
            return false;
        }
        return true;
    }

    private boolean validateEditUserForm() {
        if (!validateUsernameLength()) {
            return false;
        }
        if (!validatePassword()) {
            return false;
        }
        if (!validateFullName()) {
            return false;
        }
        return true;
    }

    private boolean validateUsernameIsAvaliable() {
        String username = usernameField.getText().trim();

        boolean alreadyExists = controller.listAll().stream()
            .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));

        if (alreadyExists) {
            JOptionPane.showMessageDialog(
                this,
                "El nombre ingresado ya existe.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            usernameField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateUsernameLength() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "El nombre de usuario es obligatorio.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            usernameField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        if (passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(
                this,
                "La contraseña es obligatoria.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            passwordField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateFullName() {
        if (fullNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "El nombre completo es obligatorio",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            fullNameField.requestFocus();
            return false;
        } else {
            return true;
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
        java.awt.GridBagConstraints gridBagConstraints;

        searchPanel = new javax.swing.JPanel();
        searchLabel = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        newUserButton = new javax.swing.JButton();
        tablePanel = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        formPanel = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        fullNameLabel = new javax.swing.JLabel();
        fullNameField = new javax.swing.JTextField();
        roleLabel = new javax.swing.JLabel();
        roleComboBox = new javax.swing.JComboBox<>();
        activeCheck = new javax.swing.JCheckBox();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 7, 10, 7));
        setPreferredSize(new java.awt.Dimension(900, 500));
        setLayout(new java.awt.BorderLayout(10, 10));

        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchLabel.setText("Buscar:");
        searchLabel.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        searchPanel.add(searchLabel, gridBagConstraints);

        searchField.setPreferredSize(new java.awt.Dimension(300, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        searchPanel.add(searchField, gridBagConstraints);

        newUserButton.setText("Nuevo Usuario");
        newUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUserButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.3;
        searchPanel.add(newUserButton, gridBagConstraints);

        add(searchPanel, java.awt.BorderLayout.PAGE_START);

        usersTable.setModel(new javax.swing.table.DefaultTableModel(
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
        tablePanel.setViewportView(usersTable);

        add(tablePanel, java.awt.BorderLayout.CENTER);

        formPanel.setLayout(new java.awt.GridLayout(3, 4, 15, 7));

        usernameLabel.setText("Usuario:");
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        passwordLabel.setText("Contraseña");
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        fullNameLabel.setText("Nombre completo:");
        formPanel.add(fullNameLabel);
        formPanel.add(fullNameField);

        roleLabel.setText("Rol:");
        formPanel.add(roleLabel);

        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "OPERADOR" }));
        formPanel.add(roleComboBox);

        activeCheck.setText("Usuario activo");
        formPanel.add(activeCheck);

        saveButton.setText("Guardar");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        formPanel.add(saveButton);

        cancelButton.setText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        formPanel.add(cancelButton);

        deleteButton.setText("Eliminar");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        formPanel.add(deleteButton);

        add(formPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void newUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserButtonActionPerformed
        cleanForm();
    }//GEN-LAST:event_newUserButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        cleanForm();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        boolean newUserMode = selectedUser == null;
        if (newUserMode && !validateNewUserForm()) {
            return;
        }
        if (!newUserMode && !validateEditUserForm()) {
            return;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String fullName = fullNameField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();
        boolean active = activeCheck.isSelected();

        try {
            if (newUserMode) {
                controller.create(username, password, fullName, role);
                JOptionPane.showMessageDialog(
                    this,
                    "Usuario creado exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                controller.update(
                    selectedUser.getId(), username, password, fullName, role, active
                );
                JOptionPane.showMessageDialog(
                    this,
                    "Usuario actualizado exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

            cleanForm();
            loadUsers();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (selectedUser == null) {
            return;
        }

        int userChoice = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar el usuario '" + selectedUser.getUsername() + "'?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        if (userChoice == JOptionPane.YES_OPTION) {
            try {
                controller.delete(selectedUser.getId());

                JOptionPane.showMessageDialog(
                    this, "Usuario eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE
                );

                cleanForm();
                loadUsers();

            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox activeCheck;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel formPanel;
    private javax.swing.JTextField fullNameField;
    private javax.swing.JLabel fullNameLabel;
    private javax.swing.JButton newUserButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JComboBox<String> roleComboBox;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables

    private class UserTableModel extends AbstractTableModel {

        private List<User> users = new ArrayList<>();
        private String[] columnNames = {"ID", "Usuario", "Nombre completo", "Rol", "Activo"};

        public void setUsers(List<User> users) {
            this.users = users;
            fireTableDataChanged();
        }

        public User getUserAt(int row) {
            return users.get(row);
        }

        @Override
        public int getRowCount() {
            return users.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Object getValueAt(int row, int column) {
            User user = users.get(row);
            return switch (column) {
                case 0 ->
                    user.getId();
                case 1 ->
                    user.getUsername();
                case 2 ->
                    user.getFullName();
                case 3 ->
                    user.getRole();
                case 4 ->
                    user.isActive() ? "Sí" : "No";
                default ->
                    null;
            };
        }
    }
}
