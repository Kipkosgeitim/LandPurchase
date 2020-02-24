package com.example.landpurchase.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import com.example.landpurchase.Models.Favorites;
import com.example.landpurchase.Models.LandOrder;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME ="Land.db";
    private static final int DB_VER=2;
    public Database(Context context ) {
        super(context, DB_NAME, null, DB_VER);
    }

    public boolean checkLandExists(String landId, String userPhone)
    {
        boolean flag =false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String SQLQuery = String.format("SELECT * from LandDetail WHERE UserPhone='%s' AND LandOrderId='%s'",userPhone,landId);
        cursor = db.rawQuery(SQLQuery,null);
        //noinspection RedundantIfStatement
        if (cursor.getCount()>0)
            flag = true;
        else
            flag = false;
        cursor.close();
        return flag;
    }
    public List<LandOrder> getCarts(String userPhone){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone","LandNameLocation","LandOrderId","SizeOfLand","Price","LandTitle","Image"};
        String sqlTable ="LandDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"UserPhone=?",new String[]{userPhone},null,null,null);

        final List<LandOrder> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new LandOrder(
                        c.getString(c.getColumnIndex("UserPhone")),
                        c.getString(c.getColumnIndex("LandNameLocation")),
                        c.getString(c.getColumnIndex("LandOrderId")),
                        c.getString(c.getColumnIndex("SizeOfLand")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("LandTitle")),
                        c.getString(c.getColumnIndex("Image"))
                ));

            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(LandOrder landOrder){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT OR REPLACE INTO LandDetail(UserPhone,LandNameLocation,LandOrderId,SizeOfLand,Price,LandTitle,Image)VALUES('%s','%s','%s','%s','%s','%s','%s');",
                landOrder.getUserPhone(),
                landOrder.getLandNameLocation(),
                landOrder.getLandOrderId(),
                landOrder.getSizeOfLand(),
                landOrder.getPrice(),
                landOrder.getLandTitle(),
                landOrder.getImage());
        db.execSQL(query);
    }

    public void cleanCart(String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM LandDetail WHERE UserPhone='%s'",userPhone);
        db.execSQL(query);
    }

    public int getCountCart(String userPhone) {
        int count=0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM LandDetail where UserPhone='%s'",userPhone);
        Cursor cursor =db.rawQuery(query,null);

        if (cursor.moveToFirst())
        {
            do {
                count = cursor.getInt(0);
            }while(cursor.moveToNext());

        }
        return count;


    }

    public void updateCart(LandOrder landOrder) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE LandDetail SET SizeOfLand = '%s' WHERE UserPhone = '%s' AND LandOrderId='%s'",landOrder.getSizeOfLand(),landOrder.getUserPhone(),landOrder.getLandOrderId());

        db.execSQL(query);
    }
    public void increaseCart(String phone, String sizeOfLand) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE LandDetail SET SizeOfLand = SizeOfLand+1 WHERE UserPhone = '%s' AND LandOrderId='%s'",phone,sizeOfLand);

        db.execSQL(query);
    }


    public void removeFromCart(String sizeOfLand, String phone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM LandDetail WHERE UserPhone='%s' and SizeOfLand='%s'",phone,sizeOfLand);
        db.execSQL(query);

    }
    /**favorites**/
    public void addFavorites(Favorites land){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(" +
                "Land,LandLocationName,LandPrice,LandTitleDeed,LandImage,SizeOfLand,LandDescription,UserPhone)" +
                " VALUES('%s','%s','%s','%s','%s','%s','%s','%s');",
                land.getLand(),
                land.getLandLocationName(),
                land.getLandPrice(),
                land.getLandTitleDeed(),
                land.getLandImage(),
                land.getSizeOfLand(),
                land.getLandDescription(),
                land.getUserPhone());
        db.execSQL(query);
    }

    public void removeFromFavorites(String landId, String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE Land='%s' and UserPhone='%s';",landId,userPhone);
        db.execSQL(query);
    }

    public boolean isFavorite(String landId, String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE Land='%s' and UserPhone='%s';",landId,userPhone);
        Cursor cursor =db.rawQuery(query,null);

        if (cursor.getCount() <=0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<Favorites> getAllFavorites(String userPhone){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone","Land","LandLocationName","LandPrice","LandTitleDeed","LandImage","SizeOfLand","LandDescription"};
        String sqlTable ="Favorites";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"UserPhone=?",new String[]{userPhone},null,null,null);

        final List<Favorites> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Favorites(
                        c.getString(c.getColumnIndex("Land")),
                        c.getString(c.getColumnIndex("LandLocationName")),
                        c.getString(c.getColumnIndex("LandPrice")),
                        c.getString(c.getColumnIndex("LandTitleDeed")),
                        c.getString(c.getColumnIndex("LandImage")),
                        c.getString(c.getColumnIndex("SizeOfLand")),
                        c.getString(c.getColumnIndex("LandDescription")),
                        c.getString(c.getColumnIndex("UserPhone"))

                ));

            }while (c.moveToNext());
        }
        return result;
    }


}
