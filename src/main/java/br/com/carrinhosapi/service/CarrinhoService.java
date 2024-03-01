package br.com.carrinhosapi.service;

import br.com.carrinhosapi.model.Carrinho;
import br.com.carrinhosapi.model.Item;

public interface CarrinhoService {
    Carrinho getCarrinhoById(Integer id);
    Carrinho getCarrinhoByUsuarioId(String usuarioId);
    Carrinho createCarrinho (String usuarioId);
    Carrinho addItem (String userId, Item item);
    Carrinho addUnidadeItem (String userId, Integer itemId);
    Carrinho removeUnidadeItem (String userId, Integer itemId);
    Carrinho removeItem (String userId, Integer itemId);
    Carrinho limparCarrinho (String userId);
}
