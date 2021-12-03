package com.mylibrary;

import java.util.*;

public class Library {
    DataBaseHandler dbHandler;
    PaymentSystem paymentSystem;

    public Library (){
        dbHandler = new DataBaseHandler();
        paymentSystem = new PaymentSystem();
    }

    public boolean lendBook(String name){
        Book availableBook = null;
        for (Book book : search("name", name)) {
            if (book.isAvailable()){
                availableBook = book;
                break;
            }
        }

        if (availableBook == null){
            return false;
        }

        if (paymentSystem.paymentSuccess()){
            dbHandler.putAvailable(availableBook, false);
            return true;
        }

        return false;
    }

    public void returnBook(Book book, int grade, String comment){
        dbHandler.putGrade(book, grade);
        dbHandler.putComment(book, comment);
        dbHandler.putAvailable(book, true);
    }

    public ArrayList<Book> search(String searchFor, String searchQuery) {
        if (searchFor == null)
            searchFor = "";
        if (searchQuery == null)
            searchQuery = "";

        searchQuery = searchQuery.trim();
        switch (searchFor.toLowerCase()){
            case "name":
                return dbHandler.searchNameOfBook(searchQuery);
            case "genre":
                return dbHandler.searchGenre(searchQuery);
            case "author":
                return dbHandler.searchAuthor(searchQuery);
            case "published":
                return dbHandler.searchPublishedDate(searchQuery);
            case "grade":
                ArrayList<Book> list = dbHandler.wholeList();
                list.sort((o1, o2) -> (int) ((o2.getAverageGrade() * 100) - (o1.getAverageGrade() * 100)));
                return list;
            default:
                return new ArrayList<>();
        }
    }
}
