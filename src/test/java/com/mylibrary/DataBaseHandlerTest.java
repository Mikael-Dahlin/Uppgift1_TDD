package com.mylibrary;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseHandlerTest {

    @Test
    void should_returnEmptyLists_when_callingUnimplementedMethods(){
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        ArrayList<Book> expected = new ArrayList();

        assertEquals(expected, dataBaseHandler.searchNameOfBook(null));
        assertEquals(expected, dataBaseHandler.searchAuthor(null));
        assertEquals(expected, dataBaseHandler.searchGenre(null));
        assertEquals(expected, dataBaseHandler.searchPublishedDate(null));
        assertEquals(expected, dataBaseHandler.wholeList());
    }
}