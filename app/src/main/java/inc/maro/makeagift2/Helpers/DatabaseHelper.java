package inc.maro.makeagift2.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Containers.GiftDisplayed;
import inc.maro.makeagift2.Containers.Target;

import static android.content.ContentValues.TAG;

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


    private DatabaseHelper(Context context){
        super(context, DatabaseHelper.DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context cont) {
        if (sInstance == null){
            sInstance = new DatabaseHelper(cont);
        }
        return sInstance;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db){
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_GIFT);
        db.execSQL(CREATE_TABLE_TARGETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
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
    public long createTarget(String targetName){
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
                Log.e(TAG, "Error al agregar un nuevo target " + e.getMessage());
            } finally {
                db.endTransaction();
            }
        }
        return toReturn;
    }

    // CREATE Gift
    public long createGift(String target, String description, String date, String where){
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
            values.put("x",Gift.DEFAULT_PERCENTAGE_X);
            values.put("y",Gift.DEFAULT_POSITION_Y);

            toReturn = db.insertOrThrow(GIFT_TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error al crear un nuevo regalo "+ e.getMessage());
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
            Log.e(TAG, "Error al recuperar todos los targets "+ e.getMessage());
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
            Log.e(TAG, "Error al intentar consultar la tabla Targets "+ e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return targetId;
    }

    // READ Gifts
    // devuelve una lista de todos los id de los gifts
    public ArrayList<GiftDisplayed> getGifts(){
        ArrayList<GiftDisplayed> toReturn = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String TARGETS_INSTANCES_QUERY = String.format("SELECT id,x,y FROM %s", GIFT_TABLE_NAME);
        Cursor cursor = db.rawQuery(TARGETS_INSTANCES_QUERY, null);
        int id;
        float x,y;
        try {
               while (cursor.moveToNext()){
                   id = cursor.getInt(cursor.getColumnIndex("id"));
                   x = cursor.getFloat(cursor.getColumnIndex("x"));
                   y = cursor.getFloat(cursor.getColumnIndex("y"));
                   toReturn.add(new GiftDisplayed(id,x,y));
               }
        } catch (Exception e) {
            Log.e(TAG, "Error al intentar retornar todos los gifts " + e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }
        return toReturn;
    }

    private int getIdByName(String name){
        int toReturn = -1;
        SQLiteDatabase db = getReadableDatabase();
        String QUERY_NAME = "SELECT id FROM "+TARGET_TABLE_NAME+" WHERE name = ?";
        Cursor cursor = db.rawQuery(QUERY_NAME, new String[]{String.valueOf(name)});
        try {
            if (cursor.moveToFirst()){
                toReturn = cursor.getInt(cursor.getColumnIndex("id"));
            } else {
                //el target no existe, lo creo
                return (int) createTarget(name);
            }
        } catch (Exception e){
            Log.e(TAG,"Error al intentar obtener un id por un nombre");
        } finally {
            cursor.close();
            db.close();
        }
        return toReturn;
    }

    // READ Target
    // Devuelve el nombre dado un ID
    private String getNameById(int idName){
        String toReturn = "";
        SQLiteDatabase db = getReadableDatabase();
        String QUERY_NAME = "SELECT name FROM "+TARGET_TABLE_NAME+" WHERE id = ?";
        Cursor cursor = db.rawQuery(QUERY_NAME, new String[]{String.valueOf(idName)});
        try {
            if (cursor.moveToFirst()){
                toReturn = cursor.getString(cursor.getColumnIndex("name"));
            }
        } catch (Exception e){
            Log.e(TAG, "Error al intentar obtener un nombre por un id de target "+ e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }
        return toReturn;
    }

    //siempre trabajo con cache para evitar usar la base de datos, al cerrar actualizo la base de datos
    public void updateGifts(Gift modifiedGift) {
        int idName = getIdByName(modifiedGift.getTarget());
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("id", modifiedGift.getId());
            values.put("idName", idName);
            values.put("description", modifiedGift.getDescription());
            values.put("date", modifiedGift.getDate());
            values.put("coorMap", modifiedGift.getWhereToBuy());

            db.update(GIFT_TABLE_NAME, values, "id = ?", new String[]{String.valueOf(modifiedGift.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "error al intentar actualizar el regalo "+ e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
    /*
    public void updateIdTargetGift(long idGift, int idTarget){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put("idName",idTarget);
            db.update(GIFT_TABLE_NAME,values,"id = ?",new String[]{String.valueOf(idGift)});
            db.setTransactionSuccessful();
        }
        catch (Exception e){
            Log.e(TAG,"Error al actualizar el IdName del Regalo "+ e.getMessage());
        }
        finally {
            db.endTransaction();
        }
    }*/

    public void clearTables(){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            db.execSQL("delete from "+ TARGET_TABLE_NAME);
            db.execSQL("delete from "+ GIFT_TABLE_NAME);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e(TAG,"Error al borrar los datos de las tablas "+ e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void updateGiftsPositions(ArrayList<GiftDisplayed> gifts){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            for (GiftDisplayed g: gifts){
                ContentValues values = new ContentValues();
                values.put("x",g.getX());
                values.put("y",g.getY());
                db.update(GIFT_TABLE_NAME,values,"id = ?",new String[]{String.valueOf(g.getId())});
            }
        } catch (Exception e){
            Log.e(TAG,"Error al actualizar las posiciones en la pantalla "+ e.getMessage());
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public void deleteGift(Gift possibleGift) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            db.delete(GIFT_TABLE_NAME,"id = ?",new String[]{String.valueOf(possibleGift.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e(TAG,"Error al borrar un regalo "+ e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public Gift getGiftById(Integer currentGift) {
        Gift toReturn = null;
        SQLiteDatabase db = getReadableDatabase();
        String GIFT_BY_ID = "SELECT description, date, idName FROM "+GIFT_TABLE_NAME +" WHERE id = ?";
        Cursor cursor = db.rawQuery(GIFT_BY_ID, new String[]{String.valueOf(currentGift.intValue())});
        try {
                if (cursor.moveToFirst()) {
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String nameTarget = getNameById(cursor.getInt(cursor.getColumnIndex("idName")));
                    toReturn = new Gift(currentGift, nameTarget, description, "", date);
                }
        } catch (Exception e) {
            Log.e(TAG, "Error al intentar retornar regalo por id" + e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }
        return toReturn;
    }
}