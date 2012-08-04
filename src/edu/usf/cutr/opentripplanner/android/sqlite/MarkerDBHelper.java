package edu.usf.cutr.opentripplanner.android.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MarkerDBHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_MARKERS = "Markers";
	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_DATE = "DateAdded";
	public static final String COLUMN_TITLE = "Title";
	public static final String COLUMN_DESCRIPTION = "Description";
	

	private static final String DATABASE_NAME = "Markers.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ TABLE_MARKERS + "( "
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
			+ COLUMN_TITLE + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ ");";
	
	public MarkerDBHelper(Context context) {
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
