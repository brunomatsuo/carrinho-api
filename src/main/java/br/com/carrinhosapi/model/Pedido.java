package br.com.carrinhosapi.model;

import br.com.carrinhosapi.enums.FormaPagamento;
import br.com.carrinhosapi.enums.StatusPagamento;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "pedido")
@Entity(name = "pedido")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String usuarioId;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ItemPedido> itens;
    private BigDecimal valorTotal;
    private FormaPagamento formaPagamento;
    private StatusPagamento statusPagamento;
}
