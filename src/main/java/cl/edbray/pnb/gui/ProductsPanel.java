/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cl.edbray.pnb.gui;

import cl.edbray.pnb.app.ApplicationContext;
import cl.edbray.pnb.controller.ProductController;
import cl.edbray.pnb.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author eduardo
 */
public class ProductsPanel extends javax.swing.JPanel {

    private final ProductController controller;

    private ProductTableModel productsTableModel;
    private DefaultComboBoxModel categoriesFilterModel;
    private DefaultComboBoxModel categoriesModel;
    private DefaultComboBoxModel typesModel;

    private Product selectedProduct;

    // TODO: move this to a category model?
    private final Map<String, List<String>> typesByCategory = Map.of(
        "BEBIDA", List.of("CAFE", "GASEOSA"),
        "SNACK", List.of("POSTRE", "SALADO"),
        "TIEMPO", List.of("ARCADE")
    );

    /**
     * Creates new form ProductsPanel
     */
    public ProductsPanel() {
        controller = ApplicationContext.getInstance().getProductController();

        initComponents();
        setupTable();
        setupComboBoxes();
        setupListeners();
        loadProducts();
        cleanForm();
    }

    private void setupTable() {
        productsTableModel = new ProductTableModel();
        productsTable.setModel(productsTableModel);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        productsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        productsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        productsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        productsTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        productsTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        productsTable.getColumnModel().getColumn(5).setPreferredWidth(40);
    }

    private void setupComboBoxes() {
        categoriesFilterModel = new DefaultComboBoxModel<>();
        categoriesModel = new DefaultComboBoxModel<>();
        typesModel = new DefaultComboBoxModel<>();

        categoriesFilterModel.addElement("TODOS");
        for (String category : typesByCategory.keySet()) {
            categoriesModel.addElement(category);
            categoriesFilterModel.addElement(category);
        }
        categoryFilterComboBox.setModel(categoriesFilterModel);
        categoryComboBox.setModel(categoriesModel);
        typeComboBox.setModel(typesModel);

        categoryComboBox.setSelectedIndex(-1);
    }

    private void setupListeners() {
        productsTable.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                selectProductInTable();
            }
        });

        productsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent ev) {
                if (ev.getClickCount() == 2) {
                    selectProductInTable();
                    nameField.requestFocus();
                }
            }
        });

        categoryFilterComboBox.addActionListener(e -> {
            String category = (String) categoryFilterComboBox.getSelectedItem();
            List<Product> matches;

            if (category.equals("TODOS")) {
                matches = controller.listAll();
            } else {
                matches = controller.listByCategory(category);
            }

            productsTableModel.setProducts(matches);
        });

        categoryComboBox.addActionListener(e -> {
            if (categoryComboBox.getSelectedIndex() == -1) {
                typeComboBox.removeAllItems();
            }
            String category = (String) categoryComboBox.getSelectedItem();
            updateTypes(category);
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void filter() {
                String search = searchField.getText().trim();
                productsTableModel.setProducts(controller.searchByName(search));
            }
        });
    }

    private void loadProducts() {
        List<Product> products = controller.listAll();
        productsTableModel.setProducts(products);
    }

    private void updateTypes(String category) {
        typesModel.removeAllElements();
        typesByCategory.getOrDefault(category, List.of())
            .forEach(typesModel::addElement);
    }

    private void selectProductInTable() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedProduct = productsTableModel.getProductAt(selectedRow);
            loadInForm(selectedProduct);
        }
    }

    private void cleanForm() {
        selectedProduct = null;
        productsTable.clearSelection();
        nameField.setText("");
        categoryComboBox.setSelectedIndex(0);
        typeComboBox.setSelectedIndex(0);
        priceField.setText("0");
        enabledCheck.setEnabled(false);
        deleteButton.setEnabled(false);
        changeStateButton.setEnabled(false);
        nameField.requestFocus();
    }

    private void loadInForm(Product product) {
        nameField.setText(product.getName());
        categoryComboBox.setSelectedItem(product.getCategory());
        typeComboBox.setSelectedItem(product.getType());
        priceField.setText(String.format("$%,.0f", product.getPrice()));
        enabledCheck.setSelected(product.isActive());

        deleteButton.setEnabled(true);
        changeStateButton.setEnabled(true);
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del producto es obligatorio.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return false;
        }

        if (categoryComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            categoryComboBox.requestFocus();
            return false;
        }

        if (priceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El precio del producto es obligatorio.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            priceField.requestFocus();
            return false;
        }

        int price;
        try {
            price = Integer.parseInt(getPriceValue());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            priceField.requestFocus();
            return false;
        }

        if (price < 0) {
            JOptionPane.showMessageDialog(this, "El precio no puede ser negativo.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            priceField.requestFocus();
            return false;
        }

        return true;
    }

    private String getPriceValue() {
        String rawValue = priceField.getText();
        String cleanValue = rawValue
            .replace("$", "")
            .replace(".", "")
            .replace(",", "")
            .trim();
        return cleanValue;
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
        categoryFilterLabel = new javax.swing.JLabel();
        categoryFilterComboBox = new javax.swing.JComboBox<>();
        newButton = new javax.swing.JButton();
        ContentPanel = new javax.swing.JPanel();
        tablePanel = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        productDataPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        categoryLabel = new javax.swing.JLabel();
        categoryComboBox = new javax.swing.JComboBox<>();
        typeLabel = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox<>();
        priceLabel = new javax.swing.JLabel();
        priceField = new javax.swing.JTextField();
        enabledCheck = new javax.swing.JCheckBox();
        productDataFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        buttonsPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        changeStateButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(900, 500));
        setLayout(new java.awt.BorderLayout());

        searchPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        java.awt.GridBagLayout SearchPanelLayout = new java.awt.GridBagLayout();
        SearchPanelLayout.columnWidths = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        SearchPanelLayout.rowHeights = new int[] {0};
        searchPanel.setLayout(SearchPanelLayout);

        searchLabel.setText("Buscar:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.05;
        searchPanel.add(searchLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        searchPanel.add(searchField, gridBagConstraints);

        categoryFilterLabel.setText("Filtro por Categoría:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.075;
        searchPanel.add(categoryFilterLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.2;
        searchPanel.add(categoryFilterComboBox, gridBagConstraints);

        newButton.setText("Nuevo");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        searchPanel.add(newButton, gridBagConstraints);

        add(searchPanel, java.awt.BorderLayout.NORTH);

        ContentPanel.setPreferredSize(new java.awt.Dimension(900, 500));
        ContentPanel.setLayout(new java.awt.GridBagLayout());

        productsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Categoría", "Precio", "Tipo", "Activo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablePanel.setViewportView(productsTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        ContentPanel.add(tablePanel, gridBagConstraints);

        productDataPanel.setPreferredSize(new java.awt.Dimension(300, 80));
        java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
        jPanel1Layout.columnWidths = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0};
        jPanel1Layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        productDataPanel.setLayout(jPanel1Layout);

        nameLabel.setText("Nombre:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        productDataPanel.add(nameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 9);
        productDataPanel.add(nameField, gridBagConstraints);

        categoryLabel.setText("Categoría:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        productDataPanel.add(categoryLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 9);
        productDataPanel.add(categoryComboBox, gridBagConstraints);

        typeLabel.setText("Tipo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        productDataPanel.add(typeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 9);
        productDataPanel.add(typeComboBox, gridBagConstraints);

        priceLabel.setText("Precio:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        productDataPanel.add(priceLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 9);
        productDataPanel.add(priceField, gridBagConstraints);

        enabledCheck.setEnabled(false);
        enabledCheck.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        enabledCheck.setLabel("Activo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 14;
        productDataPanel.add(enabledCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        productDataPanel.add(productDataFiller, gridBagConstraints);

        buttonsPanel.setLayout(new java.awt.GridLayout(2, 2, 10, 5));

        saveButton.setText("Guardar");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(saveButton);

        cancelButton.setText("Cancelar");
        buttonsPanel.add(cancelButton);

        deleteButton.setText("Eliminar");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(deleteButton);

        changeStateButton.setText("Cambiar estado");
        changeStateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeStateButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(changeStateButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        productDataPanel.add(buttonsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        ContentPanel.add(productDataPanel, gridBagConstraints);

        add(ContentPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        cleanForm();
    }//GEN-LAST:event_newButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            boolean newProductMode = (selectedProduct == null);

            if (!validateForm()) {
                return;
            }

            String name = nameField.getText().trim();
            String category = categoryComboBox.getSelectedItem().toString();
            String type = typeComboBox.getSelectedItem().toString();
            int price = Integer.parseInt(getPriceValue());
            boolean active = enabledCheck.isSelected();

            if (newProductMode) {
                controller.create(name, category, type, price);
                JOptionPane.showMessageDialog(
                    this,
                    "Producto creado exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                controller.update(selectedProduct.getId(), name, category, type, price, active);
                JOptionPane.showMessageDialog(
                    this,
                    "Producto actualizado exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

            cleanForm();
            loadProducts();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (selectedProduct == null) {
            return;
        }

        int userChoice = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar el producto '" + selectedProduct.getName() + "'?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        if (userChoice == JOptionPane.YES_OPTION) {
            try {
                controller.delete(selectedProduct.getId());
                JOptionPane.showMessageDialog(
                    this,
                    "Producto eliminado exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );

                cleanForm();
                loadProducts();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar usuarios: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void changeStateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeStateButtonActionPerformed
        controller.changeState(selectedProduct.getId());
        loadInForm(selectedProduct);
        cleanForm();
    }//GEN-LAST:event_changeStateButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ContentPanel;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JComboBox<String> categoryFilterComboBox;
    private javax.swing.JLabel categoryFilterLabel;
    private javax.swing.JLabel categoryLabel;
    private javax.swing.JButton changeStateButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JCheckBox enabledCheck;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton newButton;
    private javax.swing.JTextField priceField;
    private javax.swing.JLabel priceLabel;
    private javax.swing.Box.Filler productDataFiller;
    private javax.swing.JPanel productDataPanel;
    private javax.swing.JTable productsTable;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JComboBox<String> typeComboBox;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables

    private class ProductTableModel extends AbstractTableModel {

        private List<Product> products = new ArrayList<>();
        private final String[] columnNames = {"ID", "Nombre producto", "Categoría", "Tipo", "Precio", "Activo"};

        public void setProducts(List<Product> products) {
            this.products = products;
            fireTableDataChanged();
        }

        public Product getProductAt(int row) {
            return products.get(row);
        }

        @Override
        public int getRowCount() {
            return products.size();
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
        public Object getValueAt(int row, int column) {
            Product p = products.get(row);
            return switch (column) {
                case 0 ->
                    p.getId();
                case 1 ->
                    p.getName();
                case 2 ->
                    p.getCategory();
                case 3 ->
                    p.getType();
                case 4 ->
                    String.format("$%,d", (int) p.getPrice()).replace(",", ".");
                case 5 ->
                    p.isActive() ? "Sí" : "No";
                default ->
                    null;
            };
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
