package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.ClienteDao;
import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.model.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio para operaciones con clientes.
 */
public class ClienteService {

    private final ClienteDao clienteDao;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente registrarCliente(Cliente cliente) {
        try {
            return clienteDao.guardar(cliente);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible registrar al cliente", e);
        }
    }

    public Optional<Cliente> obtenerCliente(Long id) {
        try {
            return clienteDao.buscarPorId(id);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible obtener al cliente", e);
        }
    }

    public List<Cliente> listarClientes() {
        try {
            return clienteDao.buscarTodos();
        } catch (DaoException e) {
            throw new ServiceException("No fue posible listar los clientes", e);
        }
    }

    public void actualizarCliente(Cliente cliente) {
        try {
            clienteDao.actualizar(cliente);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible actualizar al cliente", e);
        }
    }

    public void eliminarCliente(Long id) {
        try {
            clienteDao.eliminar(id);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible eliminar al cliente", e);
        }
    }
}
