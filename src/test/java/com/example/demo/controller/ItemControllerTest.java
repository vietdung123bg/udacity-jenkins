package com.example.demo.controller;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static com.example.demo.TestUtils.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;
    @Before
    public void init(){

        when(itemRepository.findById(1L)).thenReturn(Optional.of(createAItemTest(1)));
        when(itemRepository.findAll()).thenReturn(createAItemTestsTest());
        when(itemRepository.findByName("item 1")).thenReturn(Arrays.asList(createAItemTest(1), createAItemTest(2)));

    }

    @Test
    public void getAllItems(){
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();

        assertEquals(createAItemTestsTest(), items);

        verify(itemRepository , times(1)).findAll();
    }

    @Test
    public void getItemById(){

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertEquals(createAItemTest(1L), item);

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void getItemByInvalidId(){

        ResponseEntity<Item> response = itemController.getItemById(10L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull(response.getBody());
        verify(itemRepository, times(1)).findById(10L);
    }

    @Test
    public void getItemByName(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item 1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = Arrays.asList(createAItemTest(1), createAItemTest(2));

        assertEquals(createAItemTestsTest(), items);

        verify(itemRepository , times(1)).findByName("item 1");
    }

    @Test
    public void getItemByInvalidName(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item 2");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull(response.getBody());

        verify(itemRepository , times(1)).findByName("item 2");
    }
}
