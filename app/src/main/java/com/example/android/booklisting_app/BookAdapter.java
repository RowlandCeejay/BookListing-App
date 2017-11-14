package com.example.android.booklisting_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Construct a new (@link BookAdapter).
     *
     * @param context of the app.
     *
     * @param books is the list of books, which is the data source of the adapter.
     */

    BookAdapter(Context context, List<Book> books){

        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the book at the given position
     * in the list of books.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }
        // Find the book at the given position in the list of books
        Book currentBook = getItem(position);

        // Find the TextView with view ID book title
        TextView bookTitleView = (TextView) listItemView.findViewById(R.id.book_title);

        // Get the title string from the Book object,
        String bookTitle = null;
        if (currentBook != null) {
            bookTitle = currentBook.getTitle();
        }
        // Display the title of the current book in that TextView
        bookTitleView.setText(bookTitle);

        // Find the TextView with view ID book author
        TextView bookAuthorView = (TextView) listItemView.findViewById(R.id.book_author);
        // Get the author string from the Book object,
        assert currentBook != null;
        List<String> bookAuthor = currentBook.getAuthors();

        StringBuilder authors = new StringBuilder();
        if (bookAuthor.isEmpty()){
            authors.append(R.string.No_author);
        } else {
            authors.append(bookAuthor.get(0));
            for (int i = 1; i < bookAuthor.size(); i++){
                authors.append(", ").append(bookAuthor.get(i));
            }
        }
        // Display the author of the current book in that TextView
        bookAuthorView.setText(authors);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}