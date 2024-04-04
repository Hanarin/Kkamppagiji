package com.example.afinal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalDatabase {

	public static final String TAG = "LocalDatabase";

	private static LocalDatabase database;

	public static String DATABASE_NAME = "database.db";

	public static String TABLE_BELONGING_INFO = "BELONGING_INFO";
	public static String TABLE_TIMELINE_INFO = "TIMELINE_INFO";
	public static String TABLE_POSSESSION_INFO = "POSSESSION_INFO";
	public static String[] TABLE_INFO = {TABLE_BELONGING_INFO, TABLE_TIMELINE_INFO, TABLE_POSSESSION_INFO};
	public static int DATABASE_VERSION = 1;

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    private Context context;

	private LocalDatabase(Context context) {
		this.context = context;
	}

	public static LocalDatabase getInstance(Context context) {
		if (database == null) {
			database = new LocalDatabase(context);
		}
		return database;
	}

    public boolean open() {
    	println("opening database [" + DATABASE_NAME + "].");

    	dbHelper = new DatabaseHelper(context);
    	db = dbHelper.getWritableDatabase();

    	return true;
    }

    public void close() {
    	println("closing database [" + DATABASE_NAME + "].");
    	db.close();
    	database = null;
    }

    public Cursor rawQuery(String SQL) {
		println("\nexecuteQuery called.\n");

		Cursor c1 = null;
		try {
			c1 = db.rawQuery(SQL, null);
			println("cursor count : " + c1.getCount());
		} catch(Exception ex) {
    		Log.e(TAG, "Exception in executeQuery", ex);
    	}
		return c1;
	}

    public boolean execSQL(String SQL) {
		println("\nexecute called.\n");

		try {
			Log.d(TAG, "SQL : " + SQL);
			db.execSQL(SQL);
	    } catch(Exception ex) {
			Log.e(TAG, "Exception in executeQuery", ex);
			return false;
		}
		return true;
	}

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase _db) {

			for (int i = 0; i < TABLE_INFO.length; i++) {
				println("creating table [" + TABLE_INFO[i] + "].");

				// drop existing table
				String DROP_SQL = "drop table if exists " + TABLE_INFO[i];
				try {
					_db.execSQL(DROP_SQL);
				} catch (Exception ex) {
					Log.e(TAG, "Exception in DROP_SQL", ex);
				}
			}

        	// create table
			String CREATE_BELONGING_SQL = "create table " + TABLE_BELONGING_INFO + "("
					+ "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ "  NAME TEXT"
					+ ")";

        	String CREATE_TIMELINE_SQL = "create table " + TABLE_TIMELINE_INFO + "("
		        			+ "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
		        			+ "  X REAL, "
		        			+ "  Y REAL, "
		        			+ "  CREATE_DATETIME DATETIME DEFAULT CURRENT_TIMESTAMP "
		        			+ ")";

			String CREATE_POSSESSION_SQL = "create table " + TABLE_POSSESSION_INFO + "("
					+ "  BELONGING_ID INTEGER NOT NULL, "
					+ "  TIMELINE_ID INTEGER NOT NULL, "
					+ "  POSSESSION INTEGER, "
					+ "  FOREIGN KEY(BELONGING_ID) REFERENCES BELONGING_INFO(_id), "
					+ "  FOREIGN KEY(TIMELINE_ID) REFERENCES TIMELINE_INFO(_id),"
					+ "  PRIMARY KEY(BELONGING_ID, TIMELINE_ID)"
					+ ")";
            try {
            	_db.execSQL(CREATE_BELONGING_SQL);
				_db.execSQL(CREATE_TIMELINE_SQL);
				_db.execSQL(CREATE_POSSESSION_SQL);
            } catch(Exception ex) {
        		Log.e(TAG, "Exception in CREATE_SQL", ex);
        	}

			// insert 3 belonging records
			insertBelongingRecord(_db, "휴대폰");
			insertBelongingRecord(_db, "지갑");
			insertBelongingRecord(_db, "이어폰");

			// insert 5 timeline records
			insertTimelineRecord(_db, 35.1341, 129.1031, "2023-12-10 06:56:02");
			insertTimelineRecord(_db, 35.1283, 129.0969, "2023-12-11 08:30:00");
			insertTimelineRecord(_db, 35.1275, 129.0939, "2023-12-11 09:30:00");
			insertTimelineRecord(_db, 35.132, 129.0922, "2023-12-12 10:00:00");
			insertTimelineRecord(_db, 35.1365, 129.1038, "2023-12-12 11:00:00");

			// insert 15 possession records
			insertPossessionRecord(_db, 1, 1, true);
			insertPossessionRecord(_db, 2, 1, true);
			insertPossessionRecord(_db, 3, 1, true);
			insertPossessionRecord(_db, 1, 2, true);
			insertPossessionRecord(_db, 2, 2, true);
			insertPossessionRecord(_db, 3, 2, true);
			insertPossessionRecord(_db, 1, 3, true);
			insertPossessionRecord(_db, 2, 3, true);
			insertPossessionRecord(_db, 3, 3, false);
			insertPossessionRecord(_db, 1, 4, true);
			insertPossessionRecord(_db, 2, 4, true);
			insertPossessionRecord(_db, 3, 4, false);
			insertPossessionRecord(_db, 1, 5, true);
			insertPossessionRecord(_db, 2, 5, true);
			insertPossessionRecord(_db, 3, 5, false);
		}

        public void onOpen(SQLiteDatabase db) {
        	println("opened database [" + DATABASE_NAME + "].");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");

        	if (oldVersion < 2) {   // version 1

        	}
        }

		private int insertBelongingRecord(SQLiteDatabase _db, String name) {
			try {
				_db.execSQL( "insert into " + TABLE_BELONGING_INFO + "(NAME) values ('" + name + "');" );
				Cursor cursor = _db.rawQuery("select last_insert_rowid() from " + TABLE_BELONGING_INFO, null);
				cursor.moveToFirst();
				return cursor.getInt(0);
			} catch(Exception ex) {
				Log.e(TAG, "Exception in executing insert SQL.", ex);
				return -1;
			}
		}

		private int insertTimelineRecord(SQLiteDatabase _db, double x, double y) {
			try {
				_db.execSQL( "insert into " + TABLE_TIMELINE_INFO + "(X, Y) values (" + x + ", " + y + ");" );
				Cursor cursor = _db.rawQuery("select last_insert_rowid() from " + TABLE_TIMELINE_INFO, null);
				cursor.moveToFirst();
				return cursor.getInt(0);
			} catch(Exception ex) {
				Log.e(TAG, "Exception in executing insert SQL.", ex);
				return -1;
			}
		}

		private int insertTimelineRecord(SQLiteDatabase _db, double x, double y, String datetime) {
			try {
				_db.execSQL( "insert into " + TABLE_TIMELINE_INFO + "(X, Y, CREATE_DATETIME) values (" + x + ", " + y + ", '" + datetime + "');" );
				Cursor cursor = _db.rawQuery("select last_insert_rowid() from " + TABLE_TIMELINE_INFO, null);
				cursor.moveToFirst();
				return cursor.getInt(0);
			} catch(Exception ex) {
				Log.e(TAG, "Exception in executing insert SQL.", ex);
				return -1;
			}
		}

		private int insertTimelineRecord(SQLiteDatabase _db, double x, double y, int year, int month, int date, int hour, int minute, int second) {
			try {
				String datetime = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, date, hour, minute, second);
				_db.execSQL( "insert into " + TABLE_TIMELINE_INFO + "(X, Y, CREATE_DATETIME) values (" + x + ", " + y + ", '" + datetime + "');" );
				Cursor cursor = _db.rawQuery("select last_insert_rowid() from " + TABLE_TIMELINE_INFO, null);
				cursor.moveToFirst();
				return cursor.getInt(0);
			} catch(Exception ex) {
				Log.e(TAG, "Exception in executing insert SQL.", ex);
				return -1;
			}
		}

		private int insertTimelineRecord(SQLiteDatabase _db, double x, double y, Date datetime) {
			try {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				_db.execSQL( "insert into " + TABLE_TIMELINE_INFO + "(X, Y, CREATE_DATETIME) values (" + x + ", " + y + ", '" + format.format(datetime) + "');" );
				Cursor cursor = _db.rawQuery("select last_insert_rowid() from " + TABLE_TIMELINE_INFO, null);
				cursor.moveToFirst();
				return cursor.getInt(0);
			} catch(Exception ex) {
				Log.e(TAG, "Exception in executing insert SQL.", ex);
				return -1;
			}
		}

		private void insertPossessionRecord(SQLiteDatabase _db, int belongingId, int timelineId, boolean possession) {
			try {
				int possessionToInt;
				if (possession) possessionToInt = 1;
				else possessionToInt = 0;
				_db.execSQL( "insert into " + TABLE_POSSESSION_INFO + "(BELONGING_ID, TIMELINE_ID, POSSESSION) values (" + belongingId + ", " + timelineId + ", " + possessionToInt + ");" );
			} catch(Exception ex) {
				Log.e(TAG, "Exception in executing insert SQL.", ex);
			}
		}
    }

	// SQL of table BELONGING_INFO
	public int insertBelongingRecord(String name) {
		try {
			db.execSQL( "insert into " + TABLE_BELONGING_INFO + "(NAME) values ('" + name + "');" );
			Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_BELONGING_INFO, null);
			cursor.moveToFirst();
			return cursor.getInt(0);
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
			return -1;
		}
	}

	public void updateBelonging(int id, String name) {
		try {
			db.execSQL( "update " + TABLE_BELONGING_INFO + " set NAME='" + name + "' where _ID=" + id );
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}
	}

	public void deleteBelonging(int id) {
		try {
			db.execSQL( "delete from " + TABLE_POSSESSION_INFO + " where BELONGING_ID=" + id );
			db.execSQL( "delete from " + TABLE_BELONGING_INFO + " where _ID=" + id );
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}
	}

	public ArrayList<BelongingInfo> selectAllBelonging() {
		ArrayList<BelongingInfo> result = new ArrayList<BelongingInfo>();

		try {
			Cursor cursor = db.rawQuery("select _id, NAME from " + TABLE_BELONGING_INFO, null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				int id = cursor.getInt(0);
				String name = cursor.getString(1);

				BelongingInfo info = new BelongingInfo(id, name);
				result.add(info);
			}
			println(result.toString());
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}

		return result;
	}

	public BelongingInfo selectBelonging(int id) {
		try {
			Cursor cursor = db.rawQuery("select NAME from " + TABLE_BELONGING_INFO + " where _ID=" + id, null);
			cursor.moveToFirst();
			return new BelongingInfo(id, cursor.getString(0));
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
			return null;
		}
	}

	public int getMaxBelongingId() {
		Cursor cursor = db.rawQuery("select max(_id) from " + TABLE_BELONGING_INFO, null);
		cursor.moveToFirst();
		int maxId = cursor.getInt(0);
		return maxId;
	}

	// SQL of table TIMELINE_INFO
	public int insertTimelineRecord(double x, double y) {
		try {
			db.execSQL( "insert into " + TABLE_TIMELINE_INFO + "(X, Y) values (" + x + ", " + y + ");" );
			Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_TIMELINE_INFO, null);
			cursor.moveToFirst();
			return cursor.getInt(0);
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
			return -1;
		}
	}

	public int insertTimelineRecord(double x, double y, String datetime) {
		try {
			db.execSQL( "insert into " + TABLE_TIMELINE_INFO + "(X, Y, CREATE_DATETIME) values (" + x + ", " + y + ", '" + datetime + "');" );
			Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_TIMELINE_INFO, null);
			cursor.moveToFirst();
			return cursor.getInt(0);
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
			return -1;
		}
	}

	public int insertTimelineRecord(double x, double y, int year, int month, int date, int hour, int minute, int second) {
		try {
			String datetime = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, date, hour, minute, second);
			db.execSQL( "insert into " + TABLE_TIMELINE_INFO + "(X, Y, CREATE_DATETIME) values (" + x + ", " + y + ", '" + datetime + "');" );
			Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_TIMELINE_INFO, null);
			cursor.moveToFirst();
			return cursor.getInt(0);
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
			return -1;
		}
	}

	public int insertTimelineRecord(double x, double y, Date datetime) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			db.execSQL( "insert into " + TABLE_TIMELINE_INFO + "(X, Y, CREATE_DATETIME) values (" + x + ", " + y + ", '" + format.format(datetime) + "');" );
			Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_TIMELINE_INFO, null);
			cursor.moveToFirst();
			return cursor.getInt(0);
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
			return -1;
		}
	}

	public Date getDate(Cursor cursor, int columnIndex) {
		String dateString = cursor.getString(columnIndex);
		if (dateString == null) {
			return null;
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public Date getDateTime(Cursor cursor, int columnIndex) {
		String dateString = cursor.getString(columnIndex);
		if (dateString == null) {
			return null;
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public ArrayList<TimelineInfo> selectAllTimeline() {
		ArrayList<TimelineInfo> result = new ArrayList<TimelineInfo>();

		try {
			Cursor cursor = db.rawQuery("select _id, X, Y, DATETIME(CREATE_DATETIME, 'localtime') from " + TABLE_TIMELINE_INFO, null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				int id = cursor.getInt(0);
				double x = cursor.getDouble(1);
				double y = cursor.getDouble(2);
				Date datetime = getDateTime(cursor, 3);
				TimelineInfo info = new TimelineInfo(id, x, y, datetime);
				result.add(info);
			}
			println(result.toString());
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}

		return result;
	}

	public TimelineInfo selectTimeline(int id) {
		try {
			Cursor cursor = db.rawQuery("select X, Y, DATETIME(CREATE_DATETIME, 'localtime') from " + TABLE_TIMELINE_INFO + " where _ID=" + id, null);
			cursor.moveToFirst();
			double x = cursor.getDouble(0);
			double y = cursor.getDouble(1);
			Date datetime = getDateTime(cursor, 2);
			return new TimelineInfo(id, x, y, datetime);
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
			return null;
		}
	}

	public int getMaxTimelineId() {
		Cursor cursor = db.rawQuery("select max(_id) from " + TABLE_TIMELINE_INFO, null);
		cursor.moveToFirst();
		int maxId = cursor.getInt(0);
		return maxId;
	}

	public ArrayList<Date> getDateList() {
		ArrayList<Date> result = new ArrayList<>();
		try {
			Cursor cursor = db.rawQuery("select distinct(date(CREATE_DATETIME)) as date from " + TABLE_TIMELINE_INFO + " order by date;", null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				Date date = getDate(cursor, 0);
				result.add(date);
			}
			println(result.toString());
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}
		return result;
	}

	public ArrayList<TimelineInfo> getTimelineFromDate(Date date) {
		ArrayList<TimelineInfo> result = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(date);
		try {
			Cursor cursor = db.rawQuery("select _id, X, Y, DATETIME(CREATE_DATETIME, 'localtime') from " + TABLE_TIMELINE_INFO + " where date(CREATE_DATETIME)='" + dateString + "'", null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				int id = cursor.getInt(0);
				double x = cursor.getDouble(1);
				double y = cursor.getDouble(2);
				Date dateTime = getDateTime(cursor, 3);
				result.add(new TimelineInfo(id, x, y, dateTime));
			}
			println(result.toString());
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}
		return result;
	}

	// SQL of table POSSESSION_INFO
	public void insertPossessionRecord(int belongingId, int timelineId, boolean possession) {
		try {
			int possessionToInt;
			if (possession) possessionToInt = 1;
			else possessionToInt = 0;
			db.execSQL( "insert into " + TABLE_POSSESSION_INFO + "(BELONGING_ID, TIMELINE_ID, POSSESSION) values (" + belongingId + ", " + timelineId + ", " + possessionToInt + ");" );
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}
	}

	public ArrayList<PossessionInfo> selectAllPossession() {
		ArrayList<PossessionInfo> result = new ArrayList<PossessionInfo>();

		try {
			Cursor cursor = db.rawQuery("select BELONGING_ID, TIMELINE_ID, POSSESSION from " + TABLE_POSSESSION_INFO, null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				int belongingId = cursor.getInt(0);
				int timelineId = cursor.getInt(1);
				boolean possession = cursor.getInt(2) > 0;

				PossessionInfo info = new PossessionInfo(belongingId, timelineId, possession);
				result.add(info);
			}
			println(result.toString());
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}

		return result;
	}

	public PossessionInfo selectPossession(int belongingId, int timelineId) {
		PossessionInfo result = new PossessionInfo(belongingId, timelineId);

		try {
			Cursor cursor = db.rawQuery("select POSSESSION from " + TABLE_POSSESSION_INFO +
					" where BELONGING_ID=" + belongingId + " and TIMELINE_Id=" + timelineId, null);
			cursor.moveToFirst();
			boolean possession = cursor.getInt(0) > 0;

			result.setPossession(possession);
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}
		return result;
	}

	public ArrayList<PossessionInfo> selectPossessionFromTimeline(int timelineId) {
		ArrayList<PossessionInfo> result = new ArrayList<>();

		try {
			Cursor cursor = db.rawQuery("select BELONGING_ID, POSSESSION from " + TABLE_POSSESSION_INFO +
					" where TIMELINE_Id=" + timelineId, null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				int belongingId = cursor.getInt(0);
				boolean possession = cursor.getInt(1) > 0;

				PossessionInfo info = new PossessionInfo(belongingId, timelineId, possession);
				result.add(info);
			}
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}
		return result;
	}

	public void updatePossession(int belongingId, int timelineId, boolean possession) {
		try {
			int possessionToInt;
			if (possession) possessionToInt = 1;
			else possessionToInt = 0;
			db.execSQL( "update " + TABLE_POSSESSION_INFO + " set POSSESSION=" + possessionToInt + " where BELONGING_ID=" + belongingId + " and TIMELINE_ID=" + timelineId);
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}
	}

    private void println(String msg) {
    	Log.d(TAG, msg);
    }
}
