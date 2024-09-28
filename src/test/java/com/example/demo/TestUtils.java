package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            if(!declaredField.isAccessible()){
                declaredField.setAccessible(true);
                wasPrivate = true;
            }

            declaredField.set(target, toInject);
            if(wasPrivate){
                declaredField.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public static User createUserTestTest() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user name 1");
        user.setPassword("TestPassword");
        user.setCart(createCartTest(user));

        return user;
    }

    public static Cart createCartTestTest(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        List<Item> items = createAItemTestsTest();
        cart.setItems(createAItemTestsTest());
        cart.setTotal(items.stream().map(item -> item.getPrice()).reduce(BigDecimal::add).get());
        cart.setUser(user);

        return cart;
    }

    public static List<Item> createAItemTestsTestTest() {

        List<Item> items = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            items.add(createAItemTest(i));
        }

        return items;
    }

    public static Item createAItemTest(long id){
        Item item = new Item();
        item.setId(id);

        item.setPrice(BigDecimal.valueOf(id * 1.2));

        item.setName("Item " + item.getId());

        item.setDescription("Description: "+  item.getItemByName());
        return item;
    }

    public static List<UserOrder> createOrdersTestTest(){
        List<UserOrder> orders = new ArrayList<>();

        IntStream.range(0,2).forEach(i -> {
            UserOrder order = new UserOrder();
            Cart cart = createCartTest(createUserTest());

            order.setItems(cart.getItems());
            order.setTotal(cart.getTotal());
            order.setUser(createUserTest());
            order.setId(Long.valueOf(i));

            orders.add(order);
        });
        return orders;
    }

}
