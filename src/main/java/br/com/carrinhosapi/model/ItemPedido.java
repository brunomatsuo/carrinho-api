package br.com.carrinhosapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "item_pedido")
@Entity(name = "item_pedido")
@Data
@EqualsAndHashCode(of = "id")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Integer produtoId;
    private int quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    public BigDecimal calculateValorTotal() {
        BigDecimal teste = this.getValorUnitario();
        BigDecimal teste2 = teste.multiply(new BigDecimal(this.getQuantidade()));
        return new BigDecimal(this.getQuantidade()).multiply(this.getValorUnitario());
    }
}
