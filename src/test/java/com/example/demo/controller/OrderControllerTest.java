package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.demo.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void init(){
        User user = createUserTest();

        when(userRepository.findByUsername("")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(createOrdersTest());
    }

    @Test
    public void submit(){

        ResponseEntity<UserOrder> response = orderController.submit("user name 1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        assertEquals(createAItemTestsTest(), order.getItems());
        assertEquals(createUserTest().getId(), order.getUser().getId());


        verify(orderRepository, times(1)).save(order);

    }

    @Test
    public void submitInvalid(){

        ResponseEntity<UserOrder> response = orderController.submit("user name 2");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull( response.getBody());

        verify(userRepository, times(1)).findByUsername("user name 2");
    }

    @Test
    public void getOrdersForAnUser(){

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user name 1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();


        assertEquals(createOrdersTest().size(), orders.size());

    }

}
