package com.example.android.booklisting_app;


import java.util.List;

class Book {

    /** Title of the Book */
    private String mTitle;

    /** Author of the Book */
    private List<String> mAuthor;

    /** link of the Book */
    private String mLink;

    /**
     * Constructs a new {@link Book} object.
     *  @param title is the title of the book
     * @param authors are the authors of the book
     * @param link is the web link of the book
     *
     */
    Book(String title, List<String> authors, String link) {

        mTitle = title;
        mAuthor = authors;
        mLink = link;

    }

    /**
     * Returns the title of the book.
     */
    String getTitle() {
        return mTitle;
    }

    /**
     * Returns the author of the book.
     */
    List<String> getAuthors() {
        return mAuthor;
    }


    /**
     * Returns the link url of the book.
     */
    String getLink() {
        return mLink;
    }
}
