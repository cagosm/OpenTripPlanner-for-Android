package edu.usf.cutr.opentripplanner.android;


import java.util.HashMap;

import edu.usf.cutr.opentripplanner.android.sqlite.MySQLiteHelper;
import edu.usf.cutr.opentripplanner.android.MarkerList.Markers;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MarkerProvider extends ContentProvider{
	
	public static final String KEY_TITLE = "title";
	public static final String KEY_TYPE = "type";
	public static final String KEY_CONTRIBUTOR = "contributor";
    public static final String KEY_DESCRIPTION = "body";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ROWID = "_id";
    
    private static final String DATABASE_NAME = "markers.db";
    private static final String MARKERS_TABLE_NAME = "markers";

	private static final int DATABASE_VERSION = 1;
	
	private static HashMap<String, String> sMarkersProjectionMap;

    private static final int MARKERS = 1;
    private static final int MARKER_ID = 2;
	
	private MarkerDbHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;
	
	private static final UriMatcher sUriMatcher;
	
public class MarkerDbHelper extends SQLiteOpenHelper {
	
//	public static final String TABLE_MARKERS = "markers";
//	public static final String COLUMN_ID = "id";
//	public static final String COLUMN_DATE = "dateAdded";
//	public static final String COLUMN_TITLE = "title";
//	public static final String COLUMN_TYPE = "type";
//	public static final String COLUMN_CONTRIBUTOR = "contributor";
//	public static final String COLUMN_DESCRIPTION = "description";
//	public static final String COLUMN_LATITUDE = "latitude";
//	public static final String COLUMN_LONGITUDE = "longitude";
	
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ MARKERS_TABLE_NAME + " ( "
			+ Markers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ Markers.TITLE + " TEXT NOT NULL, "
			+ Markers.TYPE + " TEXT NOT NULL, "
			+ Markers.CONTRIBUTOR + " TEXT NOT NULL, "
			+ Markers.LATITUDE + " INTEGER, "
			+ Markers.LONGITUDE + " INTEGER, "
			+ Markers.DESCRIPTION + " TEXT NOT NULL, "
			+ Markers.CREATED_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
			+ Markers.MODIFIED_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
			+ " ); ";
	
	public MarkerDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + MARKERS_TABLE_NAME);
		onCreate(db);

	}
}

public MarkerProvider(Context ctx) {
    this.mCtx = ctx;
}

public MarkerProvider open() throws SQLException {
    mDbHelper = new MarkerDbHelper(mCtx);
    mDb = mDbHelper.getWritableDatabase();
    return this;
}

public void close() {
    mDbHelper.close();
}


//public boolean deleteMarker(long rowId) {
//
//    return mDb.delete(MARKERS_TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
//}


//public Cursor fetchAllMarkers() {
//
//    return mDb.query(MARKERS_TABLE_NAME, new String[] {KEY_ROWID, KEY_TITLE,KEY_TYPE, 
//    		KEY_CONTRIBUTOR, KEY_DESCRIPTION, KEY_LATITUDE, KEY_LONGITUDE}, null, null, null, null, null);
//}
//
//
//public Cursor fetchMarker(long rowId) throws SQLException {
//
//    Cursor mCursor =
//
//        mDb.query(true, MARKERS_TABLE_NAME, new String[] {KEY_ROWID,
//                KEY_TITLE, KEY_TYPE, KEY_CONTRIBUTOR, KEY_DESCRIPTION, KEY_LATITUDE, KEY_LONGITUDE}, KEY_ROWID + "=" + rowId, null,
//                null, null, null, null);
//    if (mCursor != null) {
//        mCursor.moveToFirst();
//    }
//    return mCursor;
//
//}


//public boolean updateMarker(long rowId, String title, String type, String contributor, String description, Integer latitude, Integer longitude) {
//    ContentValues args = new ContentValues();
//    args.put(KEY_TITLE, title);
//    args.put(KEY_TYPE, type);
//    args.put(KEY_CONTRIBUTOR, contributor);
//    args.put(KEY_DESCRIPTION, description);
//    args.put(KEY_LATITUDE, latitude);
//    args.put(KEY_LONGITUDE, longitude);
//
//    return mDb.update(MARKERS_TABLE_NAME, args, KEY_ROWID + "=" + rowId, null) > 0;
//}

@Override
public int delete(Uri uri, String selection, String[] selectionArgs) {
	mDb = mDbHelper.getWritableDatabase();
    int count;
    switch (sUriMatcher.match(uri)) {
    case MARKERS:
        count = mDb.delete(MARKERS_TABLE_NAME, selection, selectionArgs);
        break;

    case MARKER_ID:
        String noteId = uri.getPathSegments().get(1);
        count = mDb.delete(MARKERS_TABLE_NAME, Markers._ID + "=" + noteId
                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;

    default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
	
}

@Override
public String getType(Uri uri) {
	switch (sUriMatcher.match(uri)) {
    case MARKERS:
    
    case MARKER_ID:
       return Markers.CONTENT_ITEM_TYPE;

    default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
	
}

@Override
public Uri insert(Uri uri, ContentValues initialValues) {
	
	if(sUriMatcher.match(uri) != MARKERS){
		throw new IllegalArgumentException("Unknown URI " + uri);
	}
	
	ContentValues values; 
	if(initialValues != null){
		values = new ContentValues(initialValues);
	} else {
		values = new ContentValues();
	}
	
	Long now = Long.valueOf(System.currentTimeMillis());
	
	if(values.containsKey(MarkerList.Markers.CREATED_DATE) == false){
		values.put(MarkerList.Markers.CREATED_DATE, now);
	}
	
	if(values.containsKey(MarkerList.Markers.MODIFIED_DATE) == false){
		values.put(MarkerList.Markers.MODIFIED_DATE, now);
	}
	
	if(values.containsKey(MarkerList.Markers.TITLE) == false){
		Resources r = Resources.getSystem();
		values.put(MarkerList.Markers.TITLE, r.getString(android.R.string.untitled));
	}
	
	if(values.containsKey(MarkerList.Markers.TYPE) == false){
		values.put(MarkerList.Markers.TYPE, "");
	}
    
	if(values.containsKey(MarkerList.Markers.CONTRIBUTOR) == false){
		values.put(MarkerList.Markers.CONTRIBUTOR, "");
	}
	
	if(values.containsKey(MarkerList.Markers.DESCRIPTION) == false){
		values.put(MarkerList.Markers.DESCRIPTION, "");
	}
	
	if(values.containsKey(MarkerList.Markers.LATITUDE) == false){
		values.put(MarkerList.Markers.LATITUDE, 0L);
	}
	
	if(values.containsKey(MarkerList.Markers.LONGITUDE) == false){
		values.put(MarkerList.Markers.LONGITUDE, 0L);
	}
	
    mDb = mDbHelper.getWritableDatabase();
    
    long rowId = mDb.insert(MARKERS_TABLE_NAME, Markers.DESCRIPTION, values);

    if(rowId > 0){
    	Uri markerUri 
    		= ContentUris.withAppendedId(MarkerList.Markers.CONTENT_URI, rowId);
    	getContext().getContentResolver().notifyChange(markerUri, null);
    	return markerUri;
    }
    
	throw new SQLException("Failed row insertion into " + uri);
}

@Override
public boolean onCreate() {
	mDbHelper = new MarkerDbHelper(getContext());
	return true;
}

@Override
public Cursor query(Uri uri, String[] projection, String selection,
		String[] selectionArgs, String sortOrder) {
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(MARKERS_TABLE_NAME);

    switch (sUriMatcher.match(uri)) {
    case MARKERS:
        qb.setProjectionMap(sMarkersProjectionMap);
        break;

    case MARKER_ID:
        qb.setProjectionMap(sMarkersProjectionMap);
        qb.appendWhere(Markers._ID + "=" + uri.getPathSegments().get(1));
        break;


    default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    // If no sort order is specified use the default
    String orderBy;
    if (TextUtils.isEmpty(sortOrder)) {
        orderBy = MarkerList.Markers.DEFAULT_SORT_ORDER;
    } else {
        orderBy = sortOrder;
    }

    // Get the database and run the query
    mDb = mDbHelper.getReadableDatabase();
    Cursor c = qb.query(mDb, projection, selection, selectionArgs, null, null, orderBy);

    // Tell the cursor what uri to watch, so it knows when its source data changes
    c.setNotificationUri(getContext().getContentResolver(), uri);
    return c;
}

@Override
public int update(Uri uri, ContentValues values, String selection,
		String[] selectionArgs) {
	mDb = mDbHelper.getWritableDatabase();
    int count;
    switch (sUriMatcher.match(uri)) {
    case MARKERS:
        count = mDb.update(MARKERS_TABLE_NAME, values, selection, selectionArgs);
        break;

    case MARKER_ID:
        String markerId = uri.getPathSegments().get(1);
        count = mDb.update(MARKERS_TABLE_NAME, values, Markers._ID + "=" + markerId
                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;

    default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
}

static {
    sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    sUriMatcher.addURI(MarkerList.AUTHORITY, "markers", MARKERS);
    sUriMatcher.addURI(MarkerList.AUTHORITY, "markers/#", MARKER_ID);
    

    sMarkersProjectionMap = new HashMap<String, String>();
    sMarkersProjectionMap.put(Markers._ID, Markers._ID);
    sMarkersProjectionMap.put(Markers.TITLE, Markers.TITLE);
    sMarkersProjectionMap.put(Markers.TYPE, Markers.TYPE);
    sMarkersProjectionMap.put(Markers.CONTRIBUTOR, Markers.CONTRIBUTOR);
    sMarkersProjectionMap.put(Markers.LATITUDE, Markers.LATITUDE);
    sMarkersProjectionMap.put(Markers.LONGITUDE, Markers.LONGITUDE);
    sMarkersProjectionMap.put(Markers.DESCRIPTION, Markers.DESCRIPTION);
    sMarkersProjectionMap.put(Markers.CREATED_DATE, Markers.CREATED_DATE);
    sMarkersProjectionMap.put(Markers.MODIFIED_DATE, Markers.MODIFIED_DATE);
    
}

}