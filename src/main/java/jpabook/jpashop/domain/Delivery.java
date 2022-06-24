package jpabook.jpashop.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)//언제나 꼭 EnumType.STRING 만 쓸것!!!!!장애남
    private DeliveryStatus status; //READY, COMP

}
