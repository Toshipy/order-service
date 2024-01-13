package com.example.OrderService.service;

import com.example.OrderService.entity.Order;
import com.example.OrderService.external.client.ProductService;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;
    @Override
    public long placeOrder(OrderRequest orderRequest) {

        //Order Entity -> Save the data with Status Order Created
        //Product Service -> Block Products (Reduce the Quantity)
        //Payment Service -> Payments -> Success -> COMPLETE, Else
        //CANCELLED

        log.info("Placing Order Request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order with Status CREATED");

        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .amount(orderRequest.getTotalAmount())
                .build();

        order = orderRepository.save(order);

        log.info("Order Process successfully with Order Id: {}", order.getId());

        return order.getId();
    }


}
