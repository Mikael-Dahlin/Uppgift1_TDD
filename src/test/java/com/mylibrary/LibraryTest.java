package com.mylibrary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryTest {

    private Library library;
    private DataBaseHandler dbHandler;
    private PaymentSystem paymentSystem;

    @BeforeEach
    void setUp() {
        library = new Library();
        dbHandler = mock(DataBaseHandler.class);
        paymentSystem = mock(PaymentSystem.class);

        library.dbHandler = dbHandler;
        library.paymentSystem = paymentSystem;
    }

    @Test
    void should_returnFalse_when_LendBookPaymentFails(){
        when(dbHandler.searchNameOfBook("Hej livet!"))
                .thenReturn(new ArrayList<>(List.of(new Book("Hej livet! : En biografi med en blandning av sött och salt","Lasse Berghagen, Marcus Birro","2021-10-27","Musik",25,true))));

        when(paymentSystem.paymentSuccess()).thenReturn(false);

        assertFalse(library.lendBook("Hej livet!"));
    }

    @Test
    void should_returnFalse_when_LendBookNotAvailable(){
        when(dbHandler.searchNameOfBook("Det osynliga barnet"))
                .thenReturn(new ArrayList<>(List.of(new Book("Det osynliga barnet","Sören Olsson, Leif Eriksson, Martin Svensson","2021-10-15","Fantasy",20,false))));

        assertFalse(library.lendBook("Det osynliga barnet"));
    }

    @Test
    void should_returnFalse_when_LendBookNotFound(){
        when(dbHandler.searchNameOfBook("Obscuritas"))
                .thenReturn(new ArrayList<>(List.of(new Book("Obscuritas","David Lagercrantz","2021-11-01","Deckare",35,true))));

        assertFalse(library.lendBook(""));
    }

    @Test
    void should_returnTrue_when_LendBookSuccess(){
        when(dbHandler.searchNameOfBook("Obscuritas"))
                .thenReturn(new ArrayList<>(List.of(new Book("Obscuritas","David Lagercrantz","2021-11-01","Deckare",35,true))));

        when(paymentSystem.paymentSuccess()).thenReturn(true);

        assertTrue(library.lendBook("Obscuritas"));
    }

    @Test
    void should_returnTrueThenFalse_when_LendBookSuccessAndAttemptedAgain(){
        Book book = new Book("Obscuritas","David Lagercrantz","2021-11-01","Deckare",35,true);
        when(dbHandler.searchNameOfBook("Obscuritas"))
                .thenReturn(new ArrayList<>(List.of(book)));

        when(paymentSystem.paymentSuccess()).thenReturn(true);

        assertTrue(library.lendBook("Obscuritas"));

        verify(dbHandler).putAvailable(book, false);
        book.setAvailable(false);

        when(dbHandler.searchNameOfBook("Obscuritas"))
                .thenReturn(new ArrayList<>(List.of(book)));

        assertFalse(library.lendBook("Obscuritas"));

    }

    @Test
    void should_setBookToAvailableAndAddGradeAndComment_when_ReturnBook(){
        Book book = new Book("Obscuritas","David Lagercrantz","2021-11-01","Deckare",35,false);

        int userInputGrade = 4;
        String userInputComment = "Jag gillade denna boken. Det tog dock ett par kapitel innan den kom igång ordentligt.";

        library.returnBook(book, userInputGrade, userInputComment);

        verify(dbHandler).putGrade(book, userInputGrade);
        verify(dbHandler).putComment(book, userInputComment);
        verify(dbHandler).putAvailable(book, true);

        book.addGrade(userInputGrade);
        book.addComment(userInputComment);
        book.setAvailable(true);
    }

    @Test
    void should_setBookToAvailableAndAddGradeAndComment_when_ReturnBookWhenGradeZeroAndCommentNull(){
        Book book = new Book("Obscuritas","David Lagercrantz","2021-11-01","Deckare",35,false);

        int userInputGrade = 0;
        String userInputComment = null;

        library.returnBook(book, userInputGrade, userInputComment);

        verify(dbHandler).putGrade(book, userInputGrade);
        verify(dbHandler).putComment(book, userInputComment);
        verify(dbHandler).putAvailable(book, true);

        book.addGrade(userInputGrade);
        book.addComment(userInputComment);
        book.setAvailable(true);
    }

    @Test
    void should_returnAnEmptyList_when_usingSearchMethodWithNull(){
        when(dbHandler.searchNameOfBook("Hej"))
                .thenReturn(new ArrayList<>(List.of(new Book("Hej livet! : En biografi med en blandning av sött och salt","Lasse Berghagen, Marcus Birro","2021-10-27","Musik",25,true))));

        ArrayList<Book> results = library.search(null, "Hej");

        int expectedSizeResults = 0;
        int actualSizeResults = -1;
        if (results.isEmpty())
            actualSizeResults = 0;

        assertEquals(expectedSizeResults, actualSizeResults);
    }

    @Test
    void should_returnAnEmptyList_when_usingSearchMethodWithNullasQuery(){
        when(dbHandler.searchNameOfBook("Hej"))
                .thenReturn(new ArrayList<>(List.of(new Book("Hej livet! : En biografi med en blandning av sött och salt","Lasse Berghagen, Marcus Birro","2021-10-27","Musik",25,true))));

        ArrayList<Book> results = library.search("name", null);

        int expectedSizeResults = 0;
        int actualSizeResults = -1;
        if (results.isEmpty())
            actualSizeResults = 0;

        assertEquals(expectedSizeResults, actualSizeResults);
    }

    @Test
    void should_returnAListOfBooks_when_usingSearchMethodForPartOfName(){
        when(dbHandler.searchNameOfBook("Tim"))
                .thenReturn(new ArrayList<>(List.of(new Book("Tim : Biografin om Avicii","Måns Mosesson","2021-11-16","musik",25,true))));

        ArrayList<Book> results = library.search("name", "Tim");

        int expectedSizeResults = 1;
        int actualSizeResults = results.size();

        assertEquals(expectedSizeResults, actualSizeResults);
    }

    @Test
    void should_returnAListOfBooks_when_usingSearchMethodForGenre(){
        when(dbHandler.searchGenre("Musik"))
                .thenReturn(new ArrayList<>(List.of(new Book("Tim : Biografin om Avicii","Måns Mosesson","2021-11-16","Musik",25,true),
                                                    new Book("Hej livet! : En biografi med en blandning av sött och salt","Lasse Berghagen, Marcus Birro","2021-10-27","Musik",25,true))));

        ArrayList<Book> results = library.search("genre", "Musik");

        int expectedSizeResults = 2;
        int actualSizeResults = results.size();

        assertEquals(expectedSizeResults, actualSizeResults);
    }

    @Test
    void should_returnAListOfBooks_when_usingSearchMethodForAuthor(){
        when(dbHandler.searchAuthor("David Lagercrantz"))
                .thenReturn(new ArrayList<>(List.of(new Book("Obscuritas","David Lagercrantz","2021-11-01","Deckare",35,true))));

        ArrayList<Book> results = library.search("author", "David Lagercrantz");

        int expectedSizeResults = 1;
        int actualSizeResults = results.size();

        assertEquals(expectedSizeResults, actualSizeResults);
    }

    @Test
    void should_returnAListOfBooks_when_usingSearchMethodForAuthorWithSpaces(){
        when(dbHandler.searchAuthor("Lass"))
                .thenReturn(new ArrayList<>(List.of(new Book("Hej livet! : En biografi med en blandning av sött och salt","Lasse Berghagen, Marcus Birro","2021-10-27","Musik",25,true),
                                                    new Book("En mental sporre : ta din ridning till nästa nivå","Johanna Lassnack, Susanne Högdahl","2021-11-15","Ridning",30,false))));

        ArrayList<Book> results = library.search("author", "  Lass  ");

        int expectedSizeResults = 2;
        int actualSizeResults = results.size();

        assertEquals(expectedSizeResults, actualSizeResults);
    }

    @Test
    void should_returnAListOfBooks_when_usingSearchMethodForPublishedDate(){
        when(dbHandler.searchPublishedDate("2021-11-16"))
                .thenReturn(new ArrayList<>(List.of(new Book("Tim : Biografin om Avicii","Måns Mosesson","2021-11-16","musik",25,true))));


        ArrayList<Book> results = library.search("published", "2021-11-16");

        int expectedSizeResults = 1;
        int actualSizeResults = results.size();

        assertEquals(expectedSizeResults, actualSizeResults);
    }

    @Test
    void should_returnASortedListOfBooks_when_usingSearchMethodForGrade(){
        Book currentBook;
        ArrayList<Book> books = new ArrayList<>();

        currentBook = new Book("1795","Niklas Natt och Dag","2021-09-20","Deckare",35,false);
        currentBook.addListOfGrades(List.of(1, 1, 5, 5, 3, 10));
        books.add(currentBook);

        currentBook = new Book("En mental sporre : ta din ridning till nästa nivå","Johanna Lassnack, Susanne Högdahl","2021-11-15","Ridning",30,false);
        currentBook.addListOfGrades(null);
        books.add(currentBook);

        currentBook = new Book("Obscuritas","David Lagercrantz","2021-11-01","Deckare",35,true);
        currentBook.addListOfGrades(List.of(2, 3, 0, 0, 5, 5, 3));
        books.add(currentBook);

        currentBook = new Book("Tim : Biografin om Avicii","Måns Mosesson","2021-11-16","Musik",25,true);
        currentBook.addListOfGrades(List.of(4, 2, 5, 5, 3));
        books.add(currentBook);

        currentBook = new Book("Det osynliga barnet","Sören Olsson, Leif Eriksson, Martin Svensson","2021-10-15","Fantasy",20,false);
        currentBook.addListOfGrades(List.of(1, 1, 4, 5, 2));
        books.add(currentBook);

        currentBook = new Book("Kråksommar","Lars Lerin","2021-07-09","Bilderböcker",20,false);
        currentBook.addListOfGrades(List.of(1, 1, 5, 4, 3));
        books.add(currentBook);

        currentBook = new Book("Depphjärnan : Varför mår vi så dåligt när vi har det så bra?","Anders Hansen","2021-10-28","Psykologi",20,false);
        currentBook.addListOfGrades(List.of(1, 3, 5, 5, 3));
        books.add(currentBook);

        currentBook = new Book("Hej livet! : En biografi med en blandning av sött och salt","Lasse Berghagen, Marcus Birro","2021-10-27","Musik",25,true);
        currentBook.addListOfGrades(List.of(1, 2, 5, 5, 3));
        books.add(currentBook);

        when(dbHandler.wholeList())
                .thenReturn(books);

        ArrayList<Book> booksResults = library.search("grade", null);

        ArrayList<Book> booksExpected = new ArrayList<>();
        booksExpected.add(new Book("Tim : Biografin om Avicii","Måns Mosesson","2021-11-16","Musik",25,true));
        booksExpected.add(new Book("Obscuritas","David Lagercrantz","2021-11-01","Deckare",35,true));
        booksExpected.add(new Book("Depphjärnan : Varför mår vi så dåligt när vi har det så bra?","Anders Hansen","2021-10-28","Psykologi",20,false));
        booksExpected.add(new Book("Hej livet! : En biografi med en blandning av sött och salt","Lasse Berghagen, Marcus Birro","2021-10-27","Musik",25,true));
        booksExpected.add(new Book("1795","Niklas Natt och Dag","2021-09-20","Deckare",35,false));
        booksExpected.add(new Book("Kråksommar","Lars Lerin","2021-07-09","Bilderböcker",20,false));
        booksExpected.add(new Book("Det osynliga barnet","Sören Olsson, Leif Eriksson, Martin Svensson","2021-10-15","Fantasy",20,false));
        booksExpected.add(new Book("En mental sporre : ta din ridning till nästa nivå","Johanna Lassnack, Susanne Högdahl","2021-11-15","Ridning",30,false));

        for (int i = 0; i < booksExpected.size(); i++) {
            assertEquals(booksExpected.get(i).name, booksResults.get(i).name);
        }
    }

}