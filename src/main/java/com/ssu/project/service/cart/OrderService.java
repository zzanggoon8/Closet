package com.ssu.project.service.cart;

import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.item.ItemRepository;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberRepository;
import com.ssu.project.domain.order.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public void addCart(Member member, List<Long> itemIdList) {
        member = memberRepository.getOne(member.getId());

        Orders orders = orderRepository.findByMemberAndOrderStatus(member, OrderStatus.CART);

        if (orders == null) {
            orders = new Orders();
            orders.setOrderStatus(OrderStatus.CART);
            orders.setMember(member);

            orderRepository.save(orders);
        }

        final Orders tmpOrders = orders;
        List<Item> itemList = itemRepository.findAllById(itemIdList);
        List<OrderItem> orderItemList = itemList.stream().map(
                item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setItem(item);
                    orderItem.setOrder(tmpOrders);
                    orderItem.setCount(1);
                    orderItem.setOrderPrice((int)item.getPrice());

                    return orderItem;
                }
        ).collect(Collectors.toList());

        orders = orderRepository.getOne(orders.getId());

        if (orders.getOrderItems() == null) {
            orders.setOrderItems(new ArrayList<>());
        }
        orders.getOrderItems().addAll(orderItemList);
    }


    public List<OrderItem> getCart(Member member) {
        Orders cartOrder = orderRepository.findByMemberAndOrderStatus(member, OrderStatus.CART);

        if (cartOrder == null) {
            throw new IllegalArgumentException("empty.cart");
        }

        log.info("get cart list complete.");

        return cartOrder.getOrderItems();
    }

    /**
     * 장바구니 목록 삭제
     * @param member
     * @param deleteItemId
     */
    @Transactional
    public void minusCart(Member member, Long deleteItemId) {
        Orders orders = orderRepository.findByMemberAndOrderStatus(member, OrderStatus.CART);
        List<OrderItem> orderItemList = orders.getOrderItems();

        log.info("deleteItemId : " + deleteItemId);
        orderItemList.removeIf(item -> item.getId().equals(deleteItemId));
        orderItemRepository.deleteById(deleteItemId);
        log.info("orderItemList : " + orderItemList.toString());

        //orders.setOrderItems(orderItemList);
        orderRepository.save(orders);

    }

    /**
     * 장바구니에 담긴 Item 객체의 총 가격 계산
     * @param list
     * @return
     */
    public int getTotalPrice(List<OrderItem> list){
        return list.stream().mapToInt(orderItem -> orderItem.getOrderPrice()).sum();
    }
}
