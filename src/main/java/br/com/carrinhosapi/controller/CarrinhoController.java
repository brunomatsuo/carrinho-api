package br.com.carrinhosapi.controller;

import br.com.carrinhosapi.model.Carrinho;
import br.com.carrinhosapi.model.Item;
import br.com.carrinhosapi.security.TokenService;
import br.com.carrinhosapi.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("carrinho")
public class CarrinhoController {

    @Autowired
    CarrinhoService carrinhoService;

    @Autowired
    TokenService tokenService;

    @GetMapping("/{id}")
    public ResponseEntity getCarrinho(@PathVariable Integer id) {
        Carrinho carrinho = carrinhoService.getCarrinhoById(id);
        if (carrinho != null) {
            return ResponseEntity.ok(carrinho);
        }
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario")
    public ResponseEntity getCarrinhoByUsuario(HttpServletRequest request) {
        String usuarioId = tokenService.getUserIdFromToken(request);
        Carrinho carrinho = carrinhoService.getCarrinhoByUsuarioId(usuarioId);
        if (carrinho != null) {
            return ResponseEntity.ok(carrinho);
        }
        else return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity newCarrinho(HttpServletRequest request) {
        String usuarioId = tokenService.getUserIdFromToken(request);
        Carrinho carrinho = carrinhoService.createCarrinho(usuarioId);
        if (carrinho != null) {
            return ResponseEntity.ok(carrinho);
        }
        else return ResponseEntity.internalServerError().build();
    }

    @PutMapping("/addItem")
    public ResponseEntity addItem(HttpServletRequest request, @RequestBody Item item) {
        String usuarioId = tokenService.getUserIdFromToken(request);
        Carrinho carrinho = carrinhoService.addItem(usuarioId, item);
        if (carrinho != null) {
            return ResponseEntity.ok(carrinho);
        }
        else return ResponseEntity.internalServerError().build();
    }

    @PutMapping("/addItemUnidade/{itemId}")
    public ResponseEntity addItemUnidade(HttpServletRequest request, @PathVariable Integer itemId) {
        String usuarioId = tokenService.getUserIdFromToken(request);
        Carrinho carrinho = carrinhoService.addUnidadeItem(usuarioId, itemId);
        if (carrinho != null) {
            return ResponseEntity.ok(carrinho);
        }
        else return ResponseEntity.internalServerError().build();
    }

    @PutMapping("/removeItemUnidade/{itemId}")
    public ResponseEntity removeItemUnidade(HttpServletRequest request, @PathVariable Integer itemId) {
        String usuarioId = tokenService.getUserIdFromToken(request);
        Carrinho carrinho = carrinhoService.removeUnidadeItem(usuarioId, itemId);
        if (carrinho != null) {
            return ResponseEntity.ok(carrinho);
        }
        else return ResponseEntity.internalServerError().build();
    }

    @PutMapping("/removeItem/{itemId}")
    public ResponseEntity removeItem(HttpServletRequest request, @PathVariable Integer itemId) {
        String usuarioId = tokenService.getUserIdFromToken(request);
        Carrinho carrinho = carrinhoService.removeItem(usuarioId, itemId);
        if (carrinho != null) {
            return ResponseEntity.ok(carrinho);
        }
        else return ResponseEntity.internalServerError().build();
    }

    @PutMapping("/limpar")
    public ResponseEntity limparCarrinho(HttpServletRequest request) {
        String usuarioId = tokenService.getUserIdFromToken(request);
        Carrinho carrinho = carrinhoService.limparCarrinho(usuarioId);
        if (carrinho != null) {
            return ResponseEntity.ok(carrinho);
        }
        else return ResponseEntity.internalServerError().build();
    }
}
