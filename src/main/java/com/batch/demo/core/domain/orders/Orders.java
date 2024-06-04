package com.batch.demo.core.domain.orders;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@Entity
@Table(name = "orders")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_item")
    private String orderItem;

    private Integer price;

    @Column(name = "order_date")
    private Date orderDate;
}
