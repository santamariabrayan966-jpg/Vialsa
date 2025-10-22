package com.vialsa.almacen.view;

import com.vialsa.almacen.controller.InventarioController;
import com.vialsa.almacen.model.InventarioMovimiento;
import com.vialsa.almacen.model.Producto;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

/**
 * Vista Swing para gestionar el inventario.
 */
public class InventoryView extends JFrame {

    private final InventarioController controller;
    private final JComboBox<Producto> productoCombo;
    private final JTextField cantidadField;
    private final JTextArea movimientosArea;

    public InventoryView(InventarioController controller) {
        super("Inventario");
        this.controller = controller;
        this.productoCombo = new JComboBox<>();
        this.cantidadField = new JTextField(10);
        this.movimientosArea = new JTextArea(10, 40);
        this.movimientosArea.setEditable(false);
        configurarComponentes();
        cargarProductos();
    }

    private void configurarComponentes() {
        JPanel formulario = new JPanel(new GridLayout(3, 2, 5, 5));
        formulario.add(new JLabel("Producto:"));
        formulario.add(productoCombo);
        formulario.add(new JLabel("Cantidad:"));
        formulario.add(cantidadField);

        JButton entradaButton = new JButton("Registrar entrada");
        entradaButton.addActionListener(event -> registrarMovimiento(true));
        JButton salidaButton = new JButton("Registrar salida");
        salidaButton.addActionListener(event -> registrarMovimiento(false));
        formulario.add(entradaButton);
        formulario.add(salidaButton);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(movimientosArea), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
    }

    private void registrarMovimiento(boolean entrada) {
        Producto producto = (Producto) productoCombo.getSelectedItem();
        if (producto == null) {
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadField.getText());
        } catch (NumberFormatException ex) {
            return;
        }
        if (entrada) {
            controller.registrarEntrada(producto.getId(), cantidad, "Ajuste manual");
        } else {
            controller.registrarSalida(producto.getId(), cantidad, "Ajuste manual");
        }
        actualizarMovimientos(producto.getId());
    }

    private void cargarProductos() {
        productoCombo.removeAllItems();
        List<Producto> productos = controller.listarProductos();
        for (Producto producto : productos) {
            productoCombo.addItem(producto);
        }
        if (!productos.isEmpty()) {
            actualizarMovimientos(productos.get(0).getId());
        }
    }

    private void actualizarMovimientos(Long productoId) {
        StringBuilder builder = new StringBuilder();
        for (InventarioMovimiento movimiento : controller.obtenerMovimientos(productoId)) {
            builder.append(movimiento.getFechaMovimiento())
                    .append(" - ")
                    .append(movimiento.getTipo())
                    .append(" - ")
                    .append(movimiento.getCantidad())
                    .append(" - ")
                    .append(movimiento.getReferencia())
                    .append('\n');
        }
        movimientosArea.setText(builder.toString());
    }
}
