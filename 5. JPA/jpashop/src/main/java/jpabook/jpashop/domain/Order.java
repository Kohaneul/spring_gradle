package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;    //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    //주문상태(ORDER,CANCEL)


    //연관관계 편의 메서드(양방향일때 사용)
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItems(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);

    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //생성메서드
    public static Order createOrder(Member member, Delivery delivery,OrderItem...orderItems){
        Order order= new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItems(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);    //처음 상태를 강제로
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비즈니스 로직
    /**
     * 주문취소
     * */
    public void cancel(){
        if(delivery.getStatus()==DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }
        this.setOrderStatus(OrderStatus.CANCEL);
        //loop 돌며 재고 원상복귀
        for(OrderItem orderItem : orderItems){
            orderItem.cancel(); //orderItem 에 각각 cancel
        }
    }

    //조회로직
    //전체 주문 가격 조회
    public int getTotalPrice(){
//        int totalPrice = 0;
//        for (OrderItem orderItem : orderItems) {
//            totalPrice+= orderItem.getTotalPrice();
//        }
//        return totalPrice;
        return orderItems.stream().mapToInt(OrderItem :: getTotalPrice).sum();
    }



}
