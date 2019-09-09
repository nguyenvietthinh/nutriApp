package com.thinh.foodnutrientfact.helper.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    private static final String TABLE_NAME = "food_nutri";
    Cursor cursor = null;

    /**
     * private constructor so that object creation from outside the class is avoided
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * return the single instance of databases
     * @param context
     */
    public static DatabaseAccess getInstance(Context context){
        if(instance == null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * open connection databases
     */
    public void open(){
        this.db = openHelper.getWritableDatabase();
    }

    /**
     * close the databases
     */
    public void close(){
        if(db!=null){
            this.db.close();
        }
    }

    /**
     * Get Food Nutrition from Food Name
     * @param foodName label after detect food image
     * @return
     */
//    public String getFoodNutri(String foodName){
//
//        String query = "SELECT * FROM " + TABLE_NAME + " WHERE Shrt_Desc LIKE '%"+foodName+"%'";
//        StringBuffer buffer = new StringBuffer();
//        try {
//            cursor = db.rawQuery(query, new String[]{});
//            if (cursor.moveToFirst()) { // Move to first row
//                do {
//
//                    buffer.append("Water_(g): "+ cursor.getString(2)+"\n");
//                    buffer.append("Energ_Kcal: "+ cursor.getString(3)+"\n");
//                    buffer.append("Protein_(g): "+ cursor.getString(4)+"\n");
//                    buffer.append("Lipid_Tot_(g): "+ cursor.getString(5)+"\n");
//                    buffer.append("Ash_(g): "+ cursor.getString(6)+"\n");
//                    buffer.append("Carbohydrt_(g): "+ cursor.getString(7)+"\n");
//                    buffer.append("Fiber_TD_(g): "+ cursor.getString(8)+"\n");
//                    buffer.append("Sugar_Tot_(g): "+ cursor.getString(9)+"\n");
//                    buffer.append("Calcium_(mg): "+ cursor.getString(10)+"\n");
//                    buffer.append("Iron_(mg): "+ cursor.getString(11)+"\n");
//                    buffer.append("Magnesium_(mg): "+ cursor.getString(12)+"\n");
//                    buffer.append("Phosphorus_(mg): "+ cursor.getString(13)+"\n");
//                    buffer.append("Potassium_(mg): "+ cursor.getString(14)+"\n");
//                    buffer.append("Sodium_(mg): "+ cursor.getString(15)+"\n");
//                    buffer.append("Zinc_(mg): "+ cursor.getString(16)+"\n");
//                    buffer.append("Copper_(mg): "+ cursor.getString(17)+"\n");
//                    buffer.append("Manganese_(mg): "+ cursor.getString(18)+"\n");
//                    buffer.append("Selenium_(µg): "+ cursor.getString(19)+"\n");
//                    buffer.append("Vit_C_(mg): "+ cursor.getString(20)+"\n");
//                    buffer.append("Thiamin_(mg): "+ cursor.getString(21)+"\n");
//                    buffer.append("Riboflavin_(mg): "+ cursor.getString(22)+"\n");
//                    buffer.append("Niacin_(mg): "+ cursor.getString(23)+"\n");
//                    buffer.append("Panto_Acid_(mg): "+ cursor.getString(24)+"\n");
//                    buffer.append("Vit_B6_(mg): "+ cursor.getString(25)+"\n");
//                    buffer.append("Folate_Tot_(µg): "+ cursor.getString(26)+"\n");
//                    buffer.append("Folic_Acid_(µg): "+ cursor.getString(27)+"\n");
//                    buffer.append("Folate_DFE_(µg): "+ cursor.getString(28)+"\n");
//                    buffer.append("Choline_Tot_(mg): "+ cursor.getString(29)+"\n");
//                    buffer.append("Vit_B12_(µg): "+ cursor.getString(30)+"\n");
//                    buffer.append("Vit_A_IU: "+ cursor.getString(31)+"\n");
//                    buffer.append("Vit_A_RAE: "+ cursor.getString(32)+"\n");
//                    buffer.append("Retinol_(µg): "+ cursor.getString(33)+"\n");
//                    buffer.append("Alpha_Carot_(µg): "+ cursor.getString(34)+"\n");
//                    buffer.append("Beta_Carot_(µg): "+ cursor.getString(35)+"\n");
//                    buffer.append("Beta_Crypt_(µg): "+ cursor.getString(36)+"\n");
//                    buffer.append("Lycopene_(µg): "+ cursor.getString(37)+"\n");
//                    buffer.append("Lut+Zea_(µg): "+ cursor.getString(38)+"\n");
//                    buffer.append("Vit_E_(mg): "+ cursor.getString(39)+"\n");
//                    buffer.append("Vit_D_(µg): "+ cursor.getString(40)+"\n");
//                    buffer.append("Vit_D_IU: "+ cursor.getString(41)+"\n");
//                    buffer.append("Vit_K_(µg): "+ cursor.getString(42)+"\n");
//                    buffer.append("FA_Sat_(g): "+ cursor.getString(43)+"\n");
//                    buffer.append("FA_Mono_(g): "+ cursor.getString(44)+"\n");
//                    buffer.append("FA_Poly_(g): "+ cursor.getString(45)+"\n");
//                    buffer.append("Cholestrl_(mg): "+ cursor.getString(46)+"\n");
//                    buffer.append("GmWt_1: "+ cursor.getString(47)+"\n");
//                    buffer.append("GmWt_Desc1: "+ cursor.getString(48)+"\n");
//                    buffer.append("GmWt_2: "+ cursor.getString(49)+"\n");
//                    buffer.append("GmWt_Desc2: "+ cursor.getString(50)+"\n");
//                    buffer.append("Refuse_Pct: "+ cursor.getString(51)+"\n");
//
//                } while (cursor.moveToNext());
//                return buffer.toString();
//            }
//        }
//        finally{
//            cursor.close();
//            db.close();
//        }
//
//        return buffer.toString();
//    }

    public ArrayList<String> getFoodNutri(String foodName){

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE Shrt_Desc LIKE '%"+foodName+"%'";
       ArrayList<String> detailNutri = new ArrayList<>();
        try {
            cursor = db.rawQuery(query, new String[]{});
            if (cursor.moveToFirst()) { // Move to first row
                do {

                    detailNutri.add(cursor.getString(3));//Energy
                    detailNutri.add(cursor.getString(5)); //Total_Fat
                    detailNutri.add(cursor.getString(43)); //FA_Sat_(g)
                    detailNutri.add(cursor.getString(45)); //FA_Poly_(g)
                    detailNutri.add(cursor.getString(44)); //FA_Mono_(g)
                    detailNutri.add(cursor.getString(46)); //Cholestrl_(mg)
                    detailNutri.add(cursor.getString(15)); //Sodium_(mg)
                    detailNutri.add(cursor.getString(14)); //Potassium_(mg)
                    detailNutri.add(cursor.getString(4)); //Protein_(g)
                    detailNutri.add(cursor.getString(20)); //Vit_C_(mg)
                    detailNutri.add(cursor.getString(32)); //Vit_A_RAE
                    detailNutri.add(cursor.getString(41)); //Vit_D_IU
                    detailNutri.add(cursor.getString(25)); //Vit_B6_(mg)
                    detailNutri.add(cursor.getString(30)); //Vit_B12_(µg)


                } while (cursor.moveToNext());
                return detailNutri;
            }
        }
        finally{
            cursor.close();
            db.close();
        }

        return detailNutri;
    }
}
