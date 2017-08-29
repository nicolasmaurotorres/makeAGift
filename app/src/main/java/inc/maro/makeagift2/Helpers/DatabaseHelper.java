package inc.maro.makeagift2.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Containers.Target;

import static android.content.ContentValues.TAG;

/**
 * Created by hIT on 9/8/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "dbMakeAGift";
    private final static String GIFT_TABLE_NAME = "gift";
    private final static String TARGET_TABLE_NAME = "target";
    private final static String CREATE_TABLE_GIFT = "CREATE TABLE " + GIFT_TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "idName INTEGER REFERENCE " + TARGET_TABLE_NAME + ", " + // referencia a la tabla target
            "description TEXT NOT NULL," +
            "date TEXT," + // todo parsear el string como Date al momento de usarlo
            "coorMap TEXT," +
            "x REAL ," +
            "y REAL );";
    private final static String CREATE_TABLE_TARGETS = "CREATE TABLE " + TARGET_TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL);";
    private static int DATABASE_VERSION = 2;
    private static DatabaseHelper sInstance = null;
    private static boolean WIPE_DATABASE = true;


    private DatabaseHelper(Context context)
    {
        super(context, DatabaseHelper.DATABASE_NAME, null, DATABASE_VERSION);

            //context.deleteDatabase(DATABASE_NAME);


    }

    public static synchronized DatabaseHelper getInstance(Context cont) {
        if (sInstance == null)
        {
          //  if (WIPE_DATABASE == true)
            //    cont.deleteDatabase(DATABASE_NAME);
            //else WIPE_DATABASE = false;
            sInstance = new DatabaseHelper(cont);

        }
        return sInstance;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_GIFT);
        db.execSQL(CREATE_TABLE_TARGETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        int databaseNewVersion = DATABASE_VERSION + 1;
        Log.w("UPDATE DATABASE",
                "Upgrading database from version " + DATABASE_VERSION + " to "
                        + databaseNewVersion + ", which will destroy all old data");

        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", GIFT_TABLE_NAME));
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", TARGET_TABLE_NAME));

        onCreate(sqLiteDatabase);
        DATABASE_VERSION = databaseNewVersion;
    }

    // CRUD OPERATIONS
    // CREATE Target
    // Crea una nueva instancia en la tabla de Target y devuelve el id del auto increment
    public long createTarget(String targetName)
    {
        long toReturn = -1;
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();
        long userId = existsTarget(targetName);
        if (userId == -1) {
            // el usuario ya existe, no hago nada
            db.beginTransaction();
            try {
                // The user might already exist in the database (i.e. the same user created multiple posts).
                ContentValues values = new ContentValues();
                values.put("name", targetName);
                // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
                toReturn = db.insertOrThrow(TARGET_TABLE_NAME, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.d(TAG, "Error al agregar un nuevo target");
            } finally {
                db.endTransaction();
            }
        }
        return toReturn;
    }

    // CREATE Gift
    public long createGift(String target, String description, String date, String where)
    {
        long toReturn = -1;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            long idTarget = existsTarget(target);
            if (idTarget == -1) { //significa que es un target nuevo
                idTarget = this.createTarget(target);
            }
            //es un target existente en la bd
            values.put("idName", idTarget);
            values.put("description", description);
            values.put("coorMap", where);
            values.put("date", date);
            toReturn = db.insertOrThrow(GIFT_TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error al crear un nuevo regalo");
        } finally {
            db.endTransaction();
        }
        return toReturn;
    }

    /// READ Targets
    /// devuelve todas las instancias del target
    public ArrayList<Target> getTargets()
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Target> toReturn = new ArrayList<Target>();

        String TARGETS_INSTANCES_QUERY = String.format("SELECT id,name FROM %s", TARGET_TABLE_NAME);
        Cursor cursor = db.rawQuery(TARGETS_INSTANCES_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Target targetAux = new Target();
                    targetAux.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    targetAux.setName(cursor.getString(cursor.getColumnIndex("name")));
                    toReturn.add(targetAux);
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error al recuperar todos los targets");
        } finally {
            db.close();
            cursor.close();
        }

        return toReturn;
    }

    // READ Target
    // checkeo si el target ya existe en la base de datos, si asi, devuelvo el id
    public long existsTarget(String targetName)
    {
        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        long targetId = -1;

        String TARGET_ID_QUERY = String.format("SELECT id,name FROM %s WHERE name = ?",TARGET_TABLE_NAME);

        Cursor cursor = db.rawQuery(TARGET_ID_QUERY, new String[]{targetName});

        try {
            if (cursor.moveToFirst()) // se supone que son unicos los nombres
            {
                targetId = cursor.getInt(cursor.getColumnIndex("id"));
            }
        } catch (Exception e) {
            Log.d(TAG, "Error al intentar consultar la tabla Targets");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return targetId;
    }

    // READ Gifts
    // devuelve una lista de todos los gifts en la base de datos
    public ArrayList<Gift> getAllGift()
    {
        ArrayList<Gift> toReturn = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String TARGETS_INSTANCES_QUERY = String.format("SELECT id,idName,description,date,coorMap,x,y FROM %s", GIFT_TABLE_NAME);
        Cursor cursor = db.rawQuery(TARGETS_INSTANCES_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Gift giftAux = new Gift();
                    giftAux.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    giftAux.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    giftAux.setWhenToGift(cursor.getString(cursor.getColumnIndex("date")));
                    giftAux.setIdTarget(cursor.getInt(cursor.getColumnIndex("idName")));
                    giftAux.setTarget(getNameById(giftAux.getIdTarget()));
                    giftAux.setWhereToBuy(cursor.getString(cursor.getColumnIndex("coorMap")));
                    toReturn.add(giftAux);
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error al intentar retornar todos los gifts");
        } finally {
            cursor.close();
        }
        return toReturn;
    }

    // READ Target
    // Devuelve el nombre dado un ID
    public String getNameById(int idName)
    {
        String toReturn = "";
        SQLiteDatabase db = getReadableDatabase();
        String QUERY_NAME = String.format("SELECT name FROM %s WHERE %s = %d", GIFT_TABLE_NAME, "id", idName);
        Cursor cursor = db.rawQuery(QUERY_NAME, null);
        try {
            if (cursor.moveToFirst()){
                toReturn = cursor.getString(cursor.getColumnIndex("name"));
            }
        } catch (Exception e){
            Log.d(TAG, "Error al intentar obtener un nombre por un id de target");
        } finally {
            cursor.close();
        }
        return toReturn;
    }

    //siempre trabajo con cache para evitar usar la base de datos, al cerrar actualizo la base de datos
    public void updateGifts(Gift modifiedGift) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("id", modifiedGift.getId());
            values.put("idName", modifiedGift.getIdTarget());
            values.put("description", modifiedGift.getDescription());
            values.put("date", modifiedGift.getWhenGift());
            values.put("coorMap", modifiedGift.getWhereToBuy());

            db.update(GIFT_TABLE_NAME, values, "id = ?", new String[]{String.valueOf(modifiedGift.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "error al intentar actualizar e");
        } finally {
            db.endTransaction();
        }
    }

    public void updateIdTargetGift(long idGift, int idTarget)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put("idName",idTarget);

            db.update(GIFT_TABLE_NAME,values,"id = ?",new String[]{String.valueOf(idGift)});
            db.setTransactionSuccessful();
        }
        catch (Exception e){
            Log.d(TAG,"Error al actualizar el IdName del Regalo");
        }
        finally {
            db.endTransaction();
        }
    }
}