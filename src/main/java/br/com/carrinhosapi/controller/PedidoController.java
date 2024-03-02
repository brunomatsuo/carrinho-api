package br.com.carrinhosapi.controller;

import br.com.carrinhosapi.enums.StatusPagamento;
import br.com.carrinhosapi.model.Pedido;
import br.com.carrinhosapi.security.TokenService;
import br.com.carrinhosapi.service.PedidoService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("pedido")
public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @Autowired
    TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarPedido(@RequestBody Pedido pedido, HttpServletRequest request) {
        String usuarioId = tokenService.getUserIdFromToken(request);

        Pedido pedidoEfetuado = pedidoService.criarPedido(usuarioId, pedido.getFormaPagamento());

        if(pedidoEfetuado != null) {
            return ResponseEntity.ok(pedidoEfetuado);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity getPedido(@PathVariable Integer pedidoId) {
        Pedido pedido = pedidoService.findPedido(pedidoId);
        if(pedido != null) {
            return ResponseEntity.ok(pedido);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity atualizarStatusPagamento(@RequestBody Pagamento pagamento) throws IOException {
        Pedido pedidoAtualizado = pedidoService.atualizarStatusPedido(pagamento.getPedidoId(), pagamento.getNovoStatus());

        if(pedidoAtualizado != null) {
            return ResponseEntity.ok(pedidoAtualizado);
        }
        return ResponseEntity.internalServerError().build();
    }

    private static class Pagamento {
        @Getter
        private Integer pedidoId;
        @Getter
        private StatusPagamento novoStatus;
    }
}
