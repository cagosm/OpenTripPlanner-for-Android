package edu.usf.cutr.opentripplanner.android.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MarkerDbAdapter {
	
	public static final String KEY_TITLE = "title";
	public static final String KEY_TYPE = "type";
	public static final String KEY_CONTRIBUTOR = "contributor";
    public static final String KEY_DESCRIPTION = "body";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ROWID = "_id";
    
    private static final String DATABASE_NAME = "markers.db";
    private static final String DATABASE_TABLE = "markers";
	private static final int DATABASE_VERSION = 1;
	
	private MarkerDbHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;
	
public class MarkerDbHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_MARKERS = "markers";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_DATE = "dateAdded";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_CONTRIBUTOR = "contributor";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ TABLE_MARKERS + "( "
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
			+ COLUMN_TITLE + " TEXT NOT NULL, "
			+ COLUMN_CONTRIBUTOR + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_LATITUDE + " INTEGER, "
			+ COLUMN_LONGITUDE + " INTEGER, "
			+ ");";
	
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKERS);
		onCreate(db);

	}
}

public MarkerDbAdapter(Context ctx) {
    this.mCtx = ctx;
}

public MarkerDbAdapter open() throws SQLException {
    mDbHelper = new MarkerDbHelper(mCtx);
    mDb = mDbHelper.getWritableDatabase();
    return this;
}

public void close() {
    mDbHelper.close();
}



public long createMarker(String title, String type, String contributor, String description, Integer latitude, Integer longitude) {
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_TITLE, title);
    initialValues.put(KEY_TYPE, type);
    initialValues.put(KEY_CONTRIBUTOR, contributor);
    initialValues.put(KEY_DESCRIPTION, description);
    initialValues.put(KEY_LATITUDE, latitude);
    initialValues.put(KEY_LONGITUDE, longitude);

    return mDb.insert(DATABASE_TABLE, null, initialValues);
}

public boolean deleteMarker(long rowId) {

    return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
}


public Cursor fetchAllMarkers() {

    return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,KEY_TYPE, 
    		KEY_CONTRIBUTOR, KEY_DESCRIPTION, KEY_LATITUDE, KEY_LONGITUDE}, null, null, null, null, null);
}


public Cursor fetchMarker(long rowId) throws SQLException {

    Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_TITLE, KEY_TYPE, KEY_CONTRIBUTOR, KEY_DESCRIPTION, KEY_LATITUDE, KEY_LONGITUDE}, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
    if (mCursor != null) {
        mCursor.moveToFirst();
    }
    return mCursor;

}


public boolean updateMarker(long rowId, String title, String type, String contributor, String description, Integer latitude, Integer longitude) {
    ContentValues args = new ContentValues();
    args.put(KEY_TITLE, title);
    args.put(KEY_TYPE, type);
    args.put(KEY_CONTRIBUTOR, contributor);
    args.put(KEY_DESCRIPTION, description);
    args.put(KEY_LATITUDE, latitude);
    args.put(KEY_LONGITUDE, longitude);

    return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
}

}