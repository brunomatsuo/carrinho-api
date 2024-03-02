package br.com.carrinhosapi.service;

import br.com.carrinhosapi.enums.FormaPagamento;
import br.com.carrinhosapi.enums.StatusPagamento;
import br.com.carrinhosapi.model.Pedido;

import java.io.IOException;

public interface PedidoService {
    Pedido criarPedido(String userId, FormaPagamento formaPagamento);
    Pedido atualizarStatusPedido(Integer pedidoId, StatusPagamento statusPagamento) throws IOException;
    Pedido findPedido(Integer pedidoId);
}
