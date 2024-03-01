package br.com.carrinhosapi.repository;

import br.com.carrinhosapi.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Integer> {
    Optional<Carrinho> findByUsuarioId(String id);
}
