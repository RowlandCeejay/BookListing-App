package com.example.android.booklisting_app;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;


class BookLoader extends AsyncTaskLoader<List<Book>> {

    /** Tag for log messages */
    private static final String LOG_TAG = BookLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link BookLoader}.
     *
     */

    BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i ( LOG_TAG, "TEST: onStartLoading() called..." );
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Book> loadInBackground() {
        Log.i ( LOG_TAG, "TEST: loadInBackground() called..." );
       if (mUrl ==null) {
           return null;
       }
        return BookQueryUtils.fetchBookData(mUrl);
    }
}