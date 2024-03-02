package br.com.carrinhosapi.service;

import br.com.carrinhosapi.enums.FormaPagamento;
import br.com.carrinhosapi.enums.StatusPagamento;
import br.com.carrinhosapi.model.Carrinho;
import br.com.carrinhosapi.model.Item;
import br.com.carrinhosapi.model.ItemPedido;
import br.com.carrinhosapi.model.Pedido;
import br.com.carrinhosapi.repository.PedidoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    CarrinhoService carrinhoService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Pedido criarPedido(String userId, FormaPagamento formaPagamento) {
        Carrinho carrinho = carrinhoService.getCarrinhoByUsuarioId(userId);
        if (carrinho == null) {
            return null;
        }
        Pedido pedido = new Pedido();
        List<ItemPedido> itensPedido = new ArrayList<>();
        for(Item item : carrinho.getItens()) {
            itensPedido.add(gerarItemPedido(item));
        }
        pedido.setItens(itensPedido);
        pedido.setUsuarioId(carrinho.getUsuarioId());
        pedido.setValorTotal(carrinho.getValorTotal());
        pedido.setFormaPagamento(formaPagamento);
        pedido.setStatusPagamento(StatusPagamento.ENVIADO);

        for(ItemPedido item : pedido.getItens()) {
            Boolean disponivel = verificarDisponibilidade(item);
            if(!disponivel) {
                throw new NoSuchElementException("Um ou mais itens não está disponível na quantidade selecionada.");
            }
        }

        pedido = pedidoRepository.save(pedido);
        carrinhoService.limparCarrinho(userId);

        String status = efetuaPagamento(pedido);

        return pedido;
    }

    private ItemPedido gerarItemPedido(Item item) {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(item.getQuantidade());
        itemPedido.setProdutoId(item.getProdutoId());
        itemPedido.setValorUnitario(item.getValorUnitario());
        itemPedido.setValorTotal(item.getValorTotal());
        return itemPedido;
    }

    @Override
    public Pedido atualizarStatusPedido(Integer pedidoId, StatusPagamento statusPagamento) throws IOException {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if(pedido == null) {
            return null;
        }
        pedido.setStatusPagamento(statusPagamento);

        if(statusPagamento == StatusPagamento.PAGAMENTO_EFETUADO) {
            removerEstoque(pedidoId);
        }
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido findPedido(Integer pedidoId) {
        return pedidoRepository.findById(pedidoId).orElse(null);
    }

    private Boolean verificarDisponibilidade(ItemPedido item) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:8083/items/" + item.getProdutoId(),
                String.class);
        if(response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return false;
        }
        else {
            try {
                JsonNode json = objectMapper.readTree(response.getBody());
                int quantidadeEstoque = json.get("quantidade").asInt();

                if (quantidadeEstoque < item.getQuantidade()) {
                    return false;
                }
            }
            catch (IOException ex) {
                return false;
            }
        }
        return true;
    }

    private String efetuaPagamento(Pedido pedido) {
        JSONObject json = new JSONObject();
        json.put("formaPagamento", pedido.getFormaPagamento());
        json.put("pedidoId", pedido.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

        ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:8085/pagamento", HttpMethod.POST, entity, String.class);

        if(response.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        else {
            try {
                JsonNode pagamento = objectMapper.readTree(response.getBody());
                String statusPagamento = pagamento.get("statusPagamento").toString();
                return statusPagamento;
            }
            catch (IOException ex) {
                return null;
            }
        }
    }

    private void removerEstoque(Integer pedidoId) throws IOException {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);

        if(pedido == null) {
            throw new NoSuchElementException("Pedido não encontrado.");
        }

        for(ItemPedido itemPedido : pedido.getItens()) {
            try {
                ResponseEntity<String> response = restTemplate
                        .exchange("http://localhost:8083/estoque/" + itemPedido.getProdutoId() + "/removerEstoque/" + itemPedido.getQuantidade(), HttpMethod.PUT, null, String.class);

                if(response.getStatusCode() != HttpStatus.OK) {
                    throw new IOException("Erro ao remover estoque.");
                }
            }
            catch (IOException ex) {
                throw new IOException(ex.getMessage());
            }
        }
    }
}
