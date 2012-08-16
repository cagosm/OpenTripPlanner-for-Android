package edu.usf.cutr.opentripplanner.android.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MarkerDBHelper {
	
	public static final String MARKERS_TABLE_NAME = "markers";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "dateAdded";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_CONTRIBUTOR = "contributor";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_CREATED_DATE = "created";
	public static final String COLUMN_MODIFIED_DATE = "modified";
	
	private static final String DATABASE_NAME = "markerdata";
    
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private final Context mCtx;
	
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ MARKERS_TABLE_NAME + " ( "
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_TITLE + " TEXT NOT NULL, "
			+ COLUMN_TYPE + " TEXT NOT NULL, "
			+ COLUMN_CONTRIBUTOR + " TEXT NOT NULL, "
			+ COLUMN_LATITUDE + " INTEGER, "
			+ COLUMN_LONGITUDE + " INTEGER, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_CREATED_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
			+ COLUMN_MODIFIED_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
			+ " ); ";
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
	
	public DatabaseHelper(Context context) {
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

public MarkerDBHelper(Context ctx) {
    this.mCtx = ctx;
}

public MarkerDBHelper open() throws SQLException {
    mDbHelper = new DatabaseHelper(mCtx);
    mDb = mDbHelper.getWritableDatabase();
    return this;
}

public void close() {
    mDbHelper.close();
}

public long createMarker(String title, String type, String contributor, String description, Integer latitude, Integer longitude) {
    ContentValues initialValues = new ContentValues();
    initialValues.put(COLUMN_TITLE, title);
    initialValues.put(COLUMN_TYPE, type);
    initialValues.put(COLUMN_CONTRIBUTOR, contributor);
    initialValues.put(COLUMN_LATITUDE, latitude);
    initialValues.put(COLUMN_LONGITUDE, longitude);
    initialValues.put(COLUMN_DESCRIPTION, description);

    return mDb.insert(MARKERS_TABLE_NAME, null, initialValues);
}

public boolean deleteMarker(long rowId) {

    return mDb.delete(MARKERS_TABLE_NAME, COLUMN_ID + "=" + rowId, null) > 0;
}

public Cursor fetchAllMarkers() {

    return mDb.query(MARKERS_TABLE_NAME, new String[] {COLUMN_ID, COLUMN_TITLE,COLUMN_TYPE, 
    		COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_CONTRIBUTOR, COLUMN_DESCRIPTION }, null, null, null, null, null);
}


public Cursor fetchMarker(long rowId) throws SQLException {

    Cursor mCursor =

        mDb.query(true, MARKERS_TABLE_NAME, new String[] {COLUMN_ID,
        		COLUMN_TITLE, COLUMN_TYPE, COLUMN_CONTRIBUTOR, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_DESCRIPTION }, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
    if (mCursor != null) {
        mCursor.moveToFirst();
    }
    return mCursor;

}


public boolean updateMarker(long rowId, String title, String type, String contributor, String description, Integer latitude, Integer longitude) {
    ContentValues args = new ContentValues();
    args.put(COLUMN_TITLE, title);
    args.put(COLUMN_TYPE, type);
    args.put(COLUMN_CONTRIBUTOR, contributor);
    args.put(COLUMN_LATITUDE, latitude);
    args.put(COLUMN_LONGITUDE, longitude);
    args.put(COLUMN_DESCRIPTION, description);
    

    return mDb.update(MARKERS_TABLE_NAME, args, COLUMN_ID + "=" + rowId, null) > 0;
}



}