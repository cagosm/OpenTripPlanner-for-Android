package edu.usf.cutr.opentripplanner.android;

import edu.usf.cutr.opentripplanner.android.MarkerList.Markers;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MarkerCreationProviderActivity extends Activity {

private static final String[] PROJECTION = new String[] {
  Markers._ID, // 0
  Markers.TITLE, //1
  Markers.TYPE, //2
  Markers.CONTRIBUTOR, //3
  Markers.DESCRIPTION, //4
  Markers.LATITUDE,   //5
  Markers.LONGITUDE, //6
};

private static final int COLUMN_INDEX_TITLE = 1;
private static final int COLUMN_INDEX_TYPE = 2;
private static final int COLUMN_INDEX_CONTRIBUTOR = 3;
private static final int COLUMN_INDEX_DESCRIPTION = 4;
private static final int COLUMN_INDEX_LATITUDE = 5;
private static final int COLUMN_INDEX_LONGITUDE = 6;


private static final String ORIGINAL_TITLE = "origTitle";
private static final String ORIGINAL_TYPE = "origType";
private static final String ORIGINAL_CONTRIBUTOR = "origContributor";
private static final String ORIGINAL_LATITUDE = "origLatitude";
private static final String ORIGINAL_LONGITUDE = "origLongitude";
private static final String ORIGINAL_DESCRIPTION = "origDescription";


private static final int REVERT_ID = Menu.FIRST;
private static final int DISCARD_ID = Menu.FIRST + 1;
private static final int DELETE_ID = Menu.FIRST + 2;


private static final int STATE_EDIT = 0;
private static final int STATE_INSERT = 1;

private int mState;
private boolean mMarkerOnly = false;
private Uri mUri;
private Cursor mCursor;
private EditText mTitle;
private EditText mType;
private EditText mContributor;
private EditText mLatitude;
private EditText mLongitude;
private EditText mDescription;
private String mOriginalTitle;
private String mOriginalType;
private String mOriginalContributor;
private String mOriginalLatitude;
private String mOriginalLongitude;
private String mOriginalDescription;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final Intent intent = getIntent();

    final String action = intent.getAction();
    if (Intent.ACTION_EDIT.equals(action)) {
        
        mState = STATE_EDIT;
        mUri = intent.getData();
    } else if (Intent.ACTION_INSERT.equals(action)) {
        
        mState = STATE_INSERT;
        mUri = getContentResolver().insert(intent.getData(), null);

        if (mUri == null) {
            Log.e("Markers", "Failed to insert new marker into " + getIntent().getData());
            finish();
            return;
        }

        
        setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));

    } else {
        Log.e("Markers", "Unknown action, exiting");
        finish();
        return;
    }



    setContentView(R.layout.edit_marker);
    
    mTitle = (EditText) findViewById(R.id.title);
    mType = (EditText) findViewById(R.id.type);
    mContributor = (EditText) findViewById(R.id.contributor);
    mLatitude = (EditText) findViewById(R.id.latitude);
    mLongitude = (EditText) findViewById(R.id.longitude);
    mDescription = (EditText) findViewById(R.id.description);

    
    mCursor = managedQuery(mUri, PROJECTION, null, null, null);

    
    if (savedInstanceState != null) {
        mOriginalTitle = savedInstanceState.getString(ORIGINAL_TITLE);
        mOriginalType = savedInstanceState.getString(ORIGINAL_TYPE);
        mOriginalContributor = savedInstanceState.getString(ORIGINAL_CONTRIBUTOR);
        mOriginalLatitude = savedInstanceState.getString(ORIGINAL_LATITUDE);
        mOriginalLongitude = savedInstanceState.getString(ORIGINAL_LONGITUDE);
        mOriginalDescription = savedInstanceState.getString(ORIGINAL_DESCRIPTION);
    }
}

@Override
protected void onResume() {
    super.onResume();

    if (mCursor != null) {
        
        mCursor.moveToFirst();

        
        if (mState == STATE_EDIT) {
            setTitle(getText(R.string.marker_edit));
        } else if (mState == STATE_INSERT) {
            setTitle(getText(R.string.marker_create));
        }

        
        String title = mCursor.getString(COLUMN_INDEX_TITLE);
        mTitle.setTextKeepState(title);
        String type = mCursor.getString(COLUMN_INDEX_TYPE);
        mType.setTextKeepState(type);
        String contributor = mCursor.getString(COLUMN_INDEX_CONTRIBUTOR);
        mContributor.setTextKeepState(contributor);
        String latitude = String.valueOf(mCursor.getLong(COLUMN_INDEX_LATITUDE));
        mLatitude.setTextKeepState(latitude);
        String longitude = String.valueOf(mCursor.getLong(COLUMN_INDEX_LONGITUDE));
        mLongitude.setTextKeepState(longitude);
        String description = mCursor.getString(COLUMN_INDEX_DESCRIPTION);
        mDescription.setTextKeepState(description);
        
        if (mOriginalTitle == null) {
            mOriginalTitle = title;
            mOriginalType = type;
            mOriginalContributor = contributor;
            mOriginalLatitude = latitude;
            mOriginalLongitude = longitude;
            mOriginalDescription = description;
        }

    } else {
        setTitle(getText(R.string.error_title));
        mDescription.setText(getText(R.string.error_message));
    }
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    
    outState.putString(ORIGINAL_TITLE, mOriginalTitle);
    outState.putString(ORIGINAL_TYPE, mOriginalType);
    outState.putString(ORIGINAL_CONTRIBUTOR, mOriginalContributor);
    outState.putString(ORIGINAL_LATITUDE, mOriginalLatitude);
    outState.putString(ORIGINAL_LONGITUDE, mOriginalLongitude);
    outState.putString(ORIGINAL_DESCRIPTION, mOriginalDescription);
}

@Override
protected void onPause() {
    super.onPause();

    if (mCursor != null) {
    	
    	String title = mTitle.getText().toString();
    	String type = mType.getText().toString();
    	String contributor = mContributor.getText().toString();
    	Long latitude = Long.valueOf(mLatitude.getText().toString());
    	Long longitude = Long.valueOf(mLongitude.getText().toString());
        String description = mDescription.getText().toString();
        int length = title.length();

        
        if (isFinishing() && (length == 0) && !mMarkerOnly) {
            setResult(RESULT_CANCELED);
            deleteMarker();

        } else {
            ContentValues values = new ContentValues();

            
            if (!mMarkerOnly) {
                values.put(Markers.MODIFIED_DATE, System.currentTimeMillis());

                if (mState == STATE_INSERT) {
                    //String title = text.substring(0, Math.min(30, length));
//                    if (length > 30) {
//                        int lastSpace = title.lastIndexOf(' ');
//                        if (lastSpace > 0) {
//                            title = title.substring(0, lastSpace);
//                        }
//                    }
                    values.put(Markers.TITLE, title);
                }
            }

            values.put(Markers.TYPE, type);
            values.put(Markers.CONTRIBUTOR, contributor);
            values.put(Markers.LATITUDE, latitude);
            values.put(Markers.LONGITUDE, longitude);
            values.put(Markers.DESCRIPTION, description);

            getContentResolver().update(mUri, values, null, null);
        }
    }
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    
    if (mState == STATE_EDIT) {
        menu.add(0, REVERT_ID, 0, R.string.menu_revert)
                .setShortcut('0', 'r')
                .setIcon(android.R.drawable.ic_menu_revert);
        if (!mMarkerOnly) {
            menu.add(0, DELETE_ID, 0, R.string.menu_delete)
                    .setShortcut('1', 'd')
                    .setIcon(android.R.drawable.ic_menu_delete);
        }

    
    } else {
        menu.add(0, DISCARD_ID, 0, R.string.menu_discard)
                .setShortcut('0', 'd')
                .setIcon(android.R.drawable.ic_menu_delete);
    }

    
    if (!mMarkerOnly) {
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, MarkerCreationProviderActivity.class), null, intent, 0, null);
    }

    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle all of the possible menu actions.
    switch (item.getItemId()) {
    case DELETE_ID:
        deleteMarker();
        finish();
        break;
    case DISCARD_ID:
        cancelMarker();
        break;
    case REVERT_ID:
        cancelMarker();
        break;
    }
    return super.onOptionsItemSelected(item);
}


private final void cancelMarker() {
    if (mCursor != null) {
        if (mState == STATE_EDIT) {
            // Put the original note text back into the database
            mCursor.close();
            mCursor = null;
            ContentValues values = new ContentValues();
            values.put(Markers.TITLE, mOriginalTitle);
            values.put(Markers.TYPE, mOriginalType);
            values.put(Markers.CONTRIBUTOR, mOriginalContributor);
            values.put(Markers.LATITUDE, Long.valueOf(mOriginalLatitude));
            values.put(Markers.LONGITUDE, Long.valueOf(mOriginalLongitude));
            values.put(Markers.DESCRIPTION, mOriginalDescription);
            getContentResolver().update(mUri, values, null, null);
        } else if (mState == STATE_INSERT) {
            deleteMarker();
        }
    }
    setResult(RESULT_CANCELED);
    finish();
}

private final void deleteMarker() {
    if (mCursor != null) {
        mCursor.close();
        mCursor = null;
        getContentResolver().delete(mUri, null, null);
        mTitle.setText("");
        mType.setText("");
        mContributor.setText("");
        mLatitude.setText("");
        mLongitude.setText("");
        mDescription.setText("");
    }
}

}
