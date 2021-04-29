package com.ssu.project.domain.delivery;

import com.ssu.project.domain.order.Orders;
import com.ssu.project.domain.shipping.ShippingInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 외부 Entity를 삽입하겠다.
    @Embedded
    private ShippingInfo shippingInfo;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    // one to one Orders
    @OneToOne
    private Orders order;
}