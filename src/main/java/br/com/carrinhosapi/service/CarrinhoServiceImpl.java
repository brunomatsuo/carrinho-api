package br.com.carrinhosapi.service;

import br.com.carrinhosapi.model.Carrinho;
import br.com.carrinhosapi.model.Item;
import br.com.carrinhosapi.repository.CarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CarrinhoServiceImpl implements CarrinhoService {

    @Autowired
    CarrinhoRepository carrinhoRepository;

    @Override
    public Carrinho getCarrinhoById(Integer id) {
        return carrinhoRepository.findById(id).orElse(null);
    }

    @Override
    public Carrinho getCarrinhoByUsuarioId(String usuarioId) {
        return carrinhoRepository.findByUsuarioId(usuarioId).orElse(null);
    }

    @Override
    public Carrinho createCarrinho (String usuarioId) {
        if(!carrinhoRepository.findByUsuarioId(usuarioId).isEmpty()) {
            return null;
        }
        Carrinho carrinho = new Carrinho();
        carrinho.setUsuarioId(usuarioId);
        carrinho.setItens(new ArrayList<Item>());
        try {
            return carrinhoRepository.save(carrinho);
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Carrinho addItem (String userId, Item item) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(userId).orElse(null);
        if (carrinho != null) {
            item.setId(UUID.randomUUID());
            item.setValorTotal(item.calculateValorTotal());
            List<Item> novaLista = carrinho.getItens();
            novaLista.add(item);
            carrinho.setItens(novaLista);
            carrinho.setValorTotal(carrinho.calculateValorTotal());
            try {
                return carrinhoRepository.save(carrinho);
            }
            catch (Exception ex) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public Carrinho addUnidadeItem (String userId, Integer itemId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(userId).orElse(null);
        if (carrinho != null) {
            List<Item> novaLista = carrinho.getItens();
            for(int i = 0; i < novaLista.size(); i++) {
                if(novaLista.get(i).getProdutoId() == itemId) {
                    int quantidadeAtual = novaLista.get(i).getQuantidade();
                    novaLista.get(i).setQuantidade(quantidadeAtual + 1);
                    novaLista.get(i).setValorTotal(novaLista.get(i).calculateValorTotal());
                    carrinho.setItens(novaLista);
                    carrinho.setValorTotal(carrinho.calculateValorTotal());
                    try {
                        return carrinhoRepository.save(carrinho);
                    }
                    catch (Exception ex) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Carrinho removeUnidadeItem (String userId, Integer itemId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(userId).orElse(null);
        if (carrinho != null) {
            List<Item> novaLista = carrinho.getItens();
            for(int i = 0; i < novaLista.size(); i++) {
                if(novaLista.get(i).getProdutoId() == itemId) {
                    int quantidadeAtual = novaLista.get(i).getQuantidade();
                    if (quantidadeAtual > 1) {
                        novaLista.get(i).setQuantidade(quantidadeAtual - 1);
                        novaLista.get(i).setValorTotal(novaLista.get(i).calculateValorTotal());
                        carrinho.setItens(novaLista);
                        carrinho.setValorTotal(carrinho.calculateValorTotal());
                        try {
                            return carrinhoRepository.save(carrinho);
                        }
                        catch (Exception ex) {
                            return null;
                        }
                    }
                    else if (quantidadeAtual == 1) {
                        return this.removeItem(userId, itemId);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Carrinho removeItem (String userId, Integer itemId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(userId).orElse(null);
        if (carrinho != null) {
            List<Item> novaLista = carrinho.getItens();
            for(int i = 0; i < novaLista.size(); i++) {
                if(novaLista.get(i).getProdutoId() == itemId) {
                    Item itemParaRemover = novaLista.get(i);
                    novaLista.remove(itemParaRemover);
                    carrinho.setItens(novaLista);
                    carrinho.setValorTotal(carrinho.calculateValorTotal());
                    try {
                        return carrinhoRepository.save(carrinho);
                    }
                    catch (Exception ex) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Carrinho limparCarrinho (String userId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(userId).orElse(null);
        if (carrinho != null) {
            List<Item> itens = carrinho.getItens();
            itens.removeAll(itens);
            carrinho.setItens(itens);
            carrinho.setValorTotal(BigDecimal.valueOf(0));
            try {
                return carrinhoRepository.save(carrinho);
            }
            catch (Exception ex) {
                return null;
            }
        }
        return null;
    }
}
