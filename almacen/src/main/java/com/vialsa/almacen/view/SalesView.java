package com.vialsa.almacen.view;

import com.vialsa.almacen.controller.VentaController;
import com.vialsa.almacen.model.Cliente;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.model.Venta;
import com.vialsa.almacen.model.VentaDetalle;

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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Vista Swing para registrar ventas.
 */
public class SalesView extends JFrame {

    private final VentaController controller;
    private final JComboBox<Cliente> clienteCombo;
    private final JComboBox<Producto> productoCombo;
    private final JTextField cantidadField;
    private final JTextArea resultadoArea;

    public SalesView(VentaController controller) {
        super("Ventas");
        this.controller = controller;
        this.clienteCombo = new JComboBox<>();
        this.productoCombo = new JComboBox<>();
        this.cantidadField = new JTextField(10);
        this.resultadoArea = new JTextArea(8, 40);
        this.resultadoArea.setEditable(false);
        configurarComponentes();
        cargarDatos();
    }

    private void configurarComponentes() {
        JPanel formulario = new JPanel(new GridLayout(4, 2, 5, 5));
        formulario.add(new JLabel("Cliente:"));
        formulario.add(clienteCombo);
        formulario.add(new JLabel("Producto:"));
        formulario.add(productoCombo);
        formulario.add(new JLabel("Cantidad:"));
        formulario.add(cantidadField);

        JButton registrarVentaButton = new JButton("Registrar venta");
        registrarVentaButton.addActionListener(event -> registrarVenta());
        formulario.add(registrarVentaButton);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(resultadoArea), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
    }

    private void cargarDatos() {
        clienteCombo.removeAllItems();
        for (Cliente cliente : controller.listarClientes()) {
            clienteCombo.addItem(cliente);
        }
        productoCombo.removeAllItems();
        for (Producto producto : controller.listarProductos()) {
            productoCombo.addItem(producto);
        }
    }

    private void registrarVenta() {
        Cliente cliente = (Cliente) clienteCombo.getSelectedItem();
        Producto producto = (Producto) productoCombo.getSelectedItem();
        if (cliente == null || producto == null) {
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadField.getText());
        } catch (NumberFormatException ex) {
            return;
        }
        VentaDetalle detalle = new VentaDetalle();
        detalle.setProductoId(producto.getId());
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecioUnitario() != null ? producto.getPrecioUnitario() : BigDecimal.ZERO);
        List<VentaDetalle> detalles = Collections.singletonList(detalle);
        Venta venta = controller.registrarVenta(cliente.getId(), detalles);
        resultadoArea.setText("Venta registrada con ID: " + venta.getId());
    }
}
