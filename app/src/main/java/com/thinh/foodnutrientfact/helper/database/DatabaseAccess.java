package com.thinh.foodnutrientfact.helper.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thinh.foodnutrientfact.model.FatInfo;
import com.thinh.foodnutrientfact.model.FatType;
import com.thinh.foodnutrientfact.model.FoodInfoDTO;
import com.thinh.foodnutrientfact.model.VitaminInfo;
import com.thinh.foodnutrientfact.model.VitaminType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DatabaseAccess {

    public static final int Idx_Energy = 3;
    public static final int Idx_Total_Fat = 5;
    public static final int Idx_FA_Sat = 43;
    public static final int Idx_FA_Poly = 45;
    public static final int Idx_FA_Mono = 44;
    public static final int Idx_Chol = 47;
    public static final int Idx_Sod = 15;
    public static final int Idx_Pot = 14;
    public static final int Idx_Protein = 4;
    public static final int Idx_Vit_C = 20;
    public static final int Idx_Vit_A_RAE = 32;
    public static final int Idx_Vit_D = 41;
    public static final int Idx_Vit_B6 = 25;
    public static final int Idx_Vit_B12 = 30;
    private SQLiteOpenHelper openHelper;
    private static DatabaseAccess instance;
    private static final String TABLE_NAME = "food_nutri";

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

    public FoodInfoDTO getFoodNutri(String foodName){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE Shrt_Desc LIKE ? LIMIT 1 ";
        FoodInfoDTO foodInfoDTO = null;
        try(SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{ "%" + foodName + "%"})){
            if (cursor.moveToFirst()) { // Move to first row
                do {
                    double calories = Double.parseDouble(cursor.getString(Idx_Energy));//Energy
                    double totalFat = Double.parseDouble(cursor.getString(Idx_Total_Fat)); //Total_Fat
                    double protein = Double.parseDouble(cursor.getString(Idx_Protein)); //Protein_(g)
                    FatInfo fatSat = new FatInfo(FatType.Sat,Double.parseDouble(cursor.getString(Idx_FA_Sat)));
                    FatInfo fatPoly = new FatInfo(FatType.Poly,Double.parseDouble(cursor.getString(Idx_FA_Poly)));
                    FatInfo fatMono = new FatInfo(FatType.Mono,Double.parseDouble(cursor.getString(Idx_FA_Mono)));
                    List<FatInfo> fatInfoList = new ArrayList<>();
                    fatInfoList.add(fatSat);
                    fatInfoList.add(fatPoly);
                    fatInfoList.add(fatMono);
                    int cholesterol = Integer.parseInt(cursor.getString(Idx_Chol)); //Cholestrl_(mg)
                    int sodium = Integer.parseInt(cursor.getString(Idx_Sod)); //Sodium_(mg)
                    int potassium = Integer.parseInt(cursor.getString(Idx_Pot)); // Potassium_(mg)9+
                    VitaminInfo vitaminC = new VitaminInfo(VitaminType.C,Double.parseDouble(cursor.getString(Idx_Vit_C)));//Vit_C_(mg)
                    VitaminInfo vitaminA = new VitaminInfo(VitaminType.A,Double.parseDouble(cursor.getString(Idx_Vit_A_RAE)));//Vit_A_RAE
                    VitaminInfo vitaminD = new VitaminInfo(VitaminType.D,Double.parseDouble(cursor.getString(Idx_Vit_D)));//Vit_D_IU
                    VitaminInfo vitaminB6 = new VitaminInfo(VitaminType.B6,Double.parseDouble(cursor.getString(Idx_Vit_B6)));//Vit_B6_(mg)
                    VitaminInfo vitaminB12 = new VitaminInfo(VitaminType.B12,Double.parseDouble(cursor.getString(Idx_Vit_B12)));//Vit_B12_(µg)
                    List<VitaminInfo> vitaminInfoList = Arrays.asList(vitaminC,vitaminA,vitaminD,vitaminB6,vitaminB12);
                    foodInfoDTO = FoodInfoDTO.constructFoodWithDetailInfo(foodName, calories, totalFat, protein, fatInfoList, cholesterol, sodium, potassium, vitaminInfoList);
                } while (cursor.moveToNext());
                return foodInfoDTO;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return foodInfoDTO;
    }
}
