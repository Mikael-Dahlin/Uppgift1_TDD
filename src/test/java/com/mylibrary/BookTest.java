package com.mylibrary;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void should_setValuesWithSettersAndGetValuesWithGetters(){
        Book book = new Book("","","","",0,false);

        book.setName("setName");
        assertEquals("setName", book.getName());

        book.setAuthor("setAuthor");
        assertEquals("setAuthor", book.getAuthor());

        book.setPublished("2000-01-01");
        assertEquals("2000-01-01", book.getPublished());

        book.setGenre("setGenre");
        assertEquals("setGenre", book.getGenre());

        book.setPrice(30);
        assertEquals(30, book.getPrice());

        book.setAvailable(true);
        assertTrue(book.isAvailable());

        book.addListOfGrades(List.of(2,3,5,5,5,0,7,6));
        assertEquals(4.0, book.getAverageGrade());

        book.addComment(null);
        book.addComment("min kommentar!");
        assertEquals("min kommentar!", book.getComments().get(0));
        assertEquals(1, book.getComments().size());
    }

    void should_notSetValuesWithSettersWhenNull(){
        Book book = new Book("notNull","notNull","notNull","notNull",0,false);

        book.setName(null);
        assertEquals("notNull", book.getName());

        book.setAuthor(null);
        assertEquals("notNull", book.getAuthor());

        book.setPublished(null);
        assertEquals("notNull", book.getPublished());

        book.setGenre(null);
        assertEquals("notNull", book.getGenre());
    }
}