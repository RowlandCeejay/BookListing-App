package com.example.android.booklisting_app;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainBookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = MainBookActivity.class.getName();

    ListView bookListView;

    LoaderManager loaderManager;

    View loadingIndicator;

    EditText mEditSearch;

    Button sButton;

    TextView mEmptyStateTextView;

    String searchInput;

    ConnectivityManager connMgr;

    /** Adapter for the list of Books */
    private BookAdapter mAdapter;

    /**
     * Create a new loader for the given URL.
     * URL for Book data from the  Google Books API dataSet.
     */
    private static final String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";

    /**
     * Constant value for the Books loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: Book Activity onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_book);

        // Find a reference to the {@link ListView} in the layout
        bookListView = (ListView) findViewById(R.id.list);

        // Find a reference to the EditText in the layout
        mEditSearch = (EditText) findViewById(R.id.editSearch);

        // Find a reference to the Progress bar in the layout
        loadingIndicator = findViewById(R.id.loading_indicator);

        // Find a reference to the TextView in the layout
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, MainBookActivity.this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);
            // Show the empty state with no connection error message
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

        }

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = null;
                if (currentBook != null) {
                    bookUri = Uri.parse(currentBook.getLink());
                }

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Find a reference to the Search Button in the layout
        sButton = (Button) findViewById(R.id.search_button);
        //listener for button
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAdapter.clear();

                loadingIndicator.setVisibility(View.VISIBLE);

                searchInput = mEditSearch.getText().toString().toLowerCase();

                if (myConnection()) {
                    mEmptyStateTextView.setVisibility(View.GONE);
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    loaderManager = getLoaderManager();
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainBookActivity.this);
                } else {
                    // Otherwise, display error
                    // First, hide loading indicator so error message will be visible
                    loadingIndicator.setVisibility(View.GONE);

                    // Update empty state with no connection error message
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });

    }

    private boolean myConnection(){
        // Get a reference to the ConnectivityManager to check state of network connectivity
        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle bundle) {
        Log.i ( LOG_TAG, "TEST: onCreateLoader() called..." );
        String search = GOOGLE_BOOKS_URL + searchInput;
        return new BookLoader(this, search  );
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        Log.i ( LOG_TAG, "TEST: onLoadFinished() called..." );
        // Clear the adapter of previous book data
        mAdapter.clear();
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Show the empty state with No books found error message
            mEmptyStateTextView.setVisibility(View.VISIBLE);

            // Set empty state text to display "No books found."
            mEmptyStateTextView.setText(R.string.no_books_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.i ( LOG_TAG, "TEST: onLoaderReset() called..." );
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
