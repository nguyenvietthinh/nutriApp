package com.tma.techday.foodnutrientfact.helper.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tma.techday.foodnutrientfact.model.FatInfo;
import com.tma.techday.foodnutrientfact.model.FatType;
import com.tma.techday.foodnutrientfact.model.FoodInfoDTO;
import com.tma.techday.foodnutrientfact.model.VitaminInfo;
import com.tma.techday.foodnutrientfact.model.VitaminType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class  FoodNutriDAO {

    private SQLiteOpenHelper openHelper;

    private static final String TABLE_NAME = "food_nutri";

    @Inject
    public FoodNutriDAO(SQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    /**
     * Get Food Nutrition from Food Name
     * @param foodName label after detect food image
     * @return
     */
    public FoodInfoDTO getFoodNutri(String foodName){
        String query = "SELECT Energ_Kcal, Lipid_Tot, Protein, FA_Sat, FA_Poly, FA_Mono, Cholestrl, Sodium, Potassium, Vit_C, Vit_A_RAE, Vit_D_IU, Vit_B6, Vit_B12  FROM " + TABLE_NAME + " WHERE Shrt_Desc LIKE ? LIMIT 1 ";
        FoodInfoDTO foodInfoDTO = null;
        String[] foodNameSplited = foodName.split("\\s+");
        for (int i=0; i<foodNameSplited.length; i++)
        {
            String foodNameFinal = foodNameSplited[i];

            try (SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{"%"+ foodNameFinal + "%"})) {
                if (cursor.moveToFirst()) { // Move to first row
                    do {
                        double calories = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Energ_Kcal")));//Energy
                        double totalFat = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Lipid_Tot"))); //Total_Fat
                        double protein = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Protein"))); //Protein_(g)
                        FatInfo fatSat = new FatInfo(FatType.Sat,Double.parseDouble(cursor.getString(cursor.getColumnIndex("FA_Sat"))));
                        FatInfo fatPoly = new FatInfo(FatType.Poly,Double.parseDouble(cursor.getString(cursor.getColumnIndex("FA_Poly"))));
                        FatInfo fatMono = new FatInfo(FatType.Mono,Double.parseDouble(cursor.getString(cursor.getColumnIndex("FA_Mono"))));
                        List<FatInfo> fatInfoList = new ArrayList<>();
                        fatInfoList.add(fatSat);
                        fatInfoList.add(fatPoly);
                        fatInfoList.add(fatMono);
                        int cholesterol = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Cholestrl"))); //Cholestrl_(mg)
                        int sodium = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Sodium"))); //Sodium_(mg)
                        int potassium = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Potassium"))); // Potassium_(mg)9+
                        VitaminInfo vitaminC = new VitaminInfo(VitaminType.C,Double.parseDouble(cursor.getString(cursor.getColumnIndex("Vit_C"))));//Vit_C_(mg)
                        VitaminInfo vitaminA = new VitaminInfo(VitaminType.A,Double.parseDouble(cursor.getString(cursor.getColumnIndex("Vit_A_RAE"))));//Vit_A_RAE
                        VitaminInfo vitaminD = new VitaminInfo(VitaminType.D,Double.parseDouble(cursor.getString(cursor.getColumnIndex("Vit_D_IU"))));//Vit_D_IU
                        VitaminInfo vitaminB6 = new VitaminInfo(VitaminType.B6,Double.parseDouble(cursor.getString(cursor.getColumnIndex("Vit_B6"))));//Vit_B6_(mg)
                        VitaminInfo vitaminB12 = new VitaminInfo(VitaminType.B12,Double.parseDouble(cursor.getString(cursor.getColumnIndex("Vit_B12"))));//Vit_B12_(µg)
                        List<VitaminInfo> vitaminInfoList = Arrays.asList(vitaminC,vitaminA,vitaminD,vitaminB6,vitaminB12);
                        foodInfoDTO = FoodInfoDTO.constructFoodWithDetailInfo(foodName, calories, totalFat, protein, fatInfoList, cholesterol, sodium, potassium, vitaminInfoList);
                    } while (cursor.moveToNext());
                    return foodInfoDTO;
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return foodInfoDTO;
    }

}
