package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty //Controller의 @Valid에서 검증됨.
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore //api 스펙에서 제외
    @OneToMany(mappedBy = "member")//멤버1 - 주문 n
    private List<Order> orders = new ArrayList<>();



}
