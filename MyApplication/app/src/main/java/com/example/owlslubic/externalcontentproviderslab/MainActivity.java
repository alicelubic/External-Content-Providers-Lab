package com.example.owlslubic.externalcontentproviderslab;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView mEventsList;
    CursorAdapter mCursorAdapter;
    private static final int CALENDAR_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEventsList = (ListView) findViewById(R.id.events_list);
        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, new String[]{CalendarContract.Events.TITLE}, new int[]{android.R.id.text1}, 0);
        mEventsList.setAdapter(mCursorAdapter);

        getSupportLoaderManager().initLoader(CALENDAR_LOADER,null,this);




        mEventsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long id) {
                //get content resolver, delete the event

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure you want to delete?")
                        .setPositiveButton("Uh huh", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //DELETE NO WORKY

                                getContentResolver().delete(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,id),null,null);
                                Toast.makeText(MainActivity.this, "Gone forever!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Nah ah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            //dialog.dismiss();

                            }
                        })
                        .create();
                dialog.show();
                return false;
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CALENDAR_LOADER:
                return new CursorLoader(MainActivity.this, CalendarContract.Events.CONTENT_URI,
                        new String[]{CalendarContract.Events._ID, CalendarContract.Events.TITLE}, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.changeCursor(null);
    }
}
