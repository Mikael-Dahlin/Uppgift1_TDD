package com.mylibrary;

import java.util.ArrayList;
import java.util.List;

public class Book {

    String name, author, published, genre;
    int price;
    boolean available;
    ArrayList<String> comments;
    ArrayList<Integer> grades;

    public Book(String name, String author, String published, String genre,int price, boolean available) {
        setName(name);
        setAuthor(author);
        setPublished(published);
        setGenre(genre);
        setPrice(price);
        setAvailable(available);
        comments = new ArrayList<>();
        grades = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name!=null) this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if(author!=null) this.author = author;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        if(published!=null) this.published = published;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if(genre!=null) this.genre = genre;
    }

    public double getAverageGrade() {
        if (grades == null || grades.isEmpty())
            return 0;
        int sum = 0;
        for (int grade : grades) {
            sum += grade;
        }
        return (double) sum / (double) grades.size();
    }

    public void addGrade(int grade){
        if(grade > 0 && grade < 6)
            grades.add(grade);
    }

    public void addListOfGrades(List<Integer> ints){
        if (ints != null){
            for (int grade: ints) {
                addGrade(grade);
            }
        }
    }

    public void addComment(String comment) {
        if (comment!=null){
            comments.add(comment);
        }
    }

    public ArrayList<String> getComments() {
        return comments;
    }
}
