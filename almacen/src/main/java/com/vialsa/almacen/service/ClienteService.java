package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.ClienteDao;
import com.vialsa.almacen.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteDao clienteDao;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public List<Cliente> obtenerClientes() {
        return clienteDao.findAll();
    }
}
