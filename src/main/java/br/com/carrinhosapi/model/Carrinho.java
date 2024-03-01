package br.com.carrinhosapi.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Table(name = "carrinho")
@Entity(name = "carrinho")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String usuarioId;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> itens;
    private BigDecimal valorTotal;

    public BigDecimal calculateValorTotal() {
        List<Item> itens = this.getItens();
        Double somaValorItens = 0D;
        for(Item item : itens) {
            somaValorItens += item.getValorTotal().doubleValue();
        }
        return BigDecimal.valueOf(somaValorItens).setScale(2, RoundingMode.CEILING);
    }

}
