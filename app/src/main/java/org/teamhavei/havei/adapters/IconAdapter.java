package org.teamhavei.havei.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.teamhavei.havei.R;

public class IconAdapter {

    private static String TAG = "DEBUG";

    //只能加，不能改！
    public static final int ID_ACCOMMODATION = 1;
    public static final int ID_AIR_TICKETS = 2;
    public static final int ID_ASTRONOMY = 3;
    public static final int ID_ATHLETE = 4;
    public static final int ID_BONUS = 5;
    public static final int ID_BORROW = 6;
    public static final int ID_CAR_FARE = 7;
    public static final int ID_CLOTHING = 8;
    public static final int ID_DRINKS = 9;
    public static final int ID_ELSE = 10;
    public static final int ID_EXERCISE = 11;//cs_exercize.xml
    public static final int ID_EXPENSE_MANAGEMENT = 12;
    public static final int ID_FRUITS = 13;
    public static final int ID_GAME = 14;
    public static final int ID_GIFT1 = 15;
    public static final int ID_GIFT2 = 16;
    public static final int ID_GIFT3 = 17;
    public static final int ID_HOBBY = 18;
    public static final int ID_HOSPITAL = 19;
    public static final int ID_IN_SCHOOL = 20;
    public static final int ID_JOURNEY = 21;
    public static final int ID_LAUNDRY_DETERGENT = 22;
    public static final int ID_TREND = 23;//cs_lend_out.xml
    public static final int ID_MAKE_UP = 24;//TODO:BROKEN!
    public static final int ID_MEDICINE = 25;
    public static final int ID_REPAY1 = 26;//cs_others_repay_you.xml
    public static final int ID_REPAY2 = 27;//cs_others_repay_you2.xml
    public static final int ID_TISSUE_PAPER_ROLL = 28;//cs_paper.xml
    public static final int ID_PARTY = 29;
    public static final int ID_PETS = 30;
    public static final int ID_PHOTOGRAPHING = 31;
    public static final int ID_TOY = 32;//cs_plaything.xml
    public static final int ID_READING = 33;
    public static final int ID_RECEIPT = 34;
    public static final int ID_RECREATION = 35;
    public static final int ID_RED_PACKET = 36;
    public static final int ID_REPAY3 = 37;//cs_repay_others.xml
    public static final int ID_SALARY = 38;
    public static final int ID_SCENIC_SPOT = 39;
    public static final int ID_SKIN_CARE = 40;
    public static final int ID_SLIPPER = 41;
    public static final int ID_SNACKS = 42;
    public static final int ID_STAPLE_FOOD = 43;
    public static final int ID_TEXTBOOK = 44;
    public static final int ID_TRANSPORTATION = 45;
    public static final int ID_UMBRELLA = 46;
    //只能加，不能改！


    Context mContext;

    public IconAdapter(Context context) {
        mContext = context;
    }

    public Drawable getIcon(int iconID) {
        switch (iconID) {
            case ID_ACCOMMODATION:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_accommodation);
            case ID_AIR_TICKETS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_air_tickets);
            case ID_ASTRONOMY:
                return ContextCompat.getDrawable(mContext,R.drawable.cs_astronomy);
            case ID_ATHLETE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_athlete);
            case ID_BONUS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_bonus);
            case ID_BORROW:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_borrow);
            case ID_CAR_FARE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_car_fare);
            case ID_CLOTHING:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_clothing);
            case ID_DRINKS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_drinks);
            case ID_ELSE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_else);
            case ID_EXERCISE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_exercize);
            case ID_EXPENSE_MANAGEMENT:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_expense_management);
            case ID_FRUITS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_fruits);
            case ID_GAME:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_game);
            case ID_GIFT1:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_gift1);
            case ID_GIFT2:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_gift2);
            case ID_GIFT3:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_gift3);
            case ID_HOBBY:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_hobby);
            case ID_HOSPITAL:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_hospital);
            case ID_IN_SCHOOL:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_in_school);
            case ID_JOURNEY:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_journey);
            case ID_LAUNDRY_DETERGENT:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_laundry_detergent);
            case ID_TREND:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_lend_out);
            case ID_MAKE_UP:
                Log.d(TAG, "getIcon: MAKE_UP is currently down, returning first icon");
                return ContextCompat.getDrawable(mContext, R.drawable.cs_accommodation);
            case ID_MEDICINE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_medicine);
            case ID_REPAY1:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_others_repay_you);
            case ID_REPAY2:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_others_repay_you2);
            case ID_TISSUE_PAPER_ROLL:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_paper);
            case ID_PARTY:
                Log.d(TAG, "getIcon: PARTY is currently down, returning first icon");
                return ContextCompat.getDrawable(mContext, R.drawable.cs_accommodation);
            case ID_PETS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_pets);
            case ID_PHOTOGRAPHING:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_photographing);
            case ID_TOY:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_plaything);
            case ID_READING:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_reading);
            case ID_RECEIPT:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_receipt);
            case ID_RECREATION:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_recreation);
            case ID_RED_PACKET:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_red_packet);
            case ID_REPAY3:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_repay_others);
            case ID_SALARY:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_salary);
            case ID_SCENIC_SPOT:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_scenic_spot);
            case ID_SKIN_CARE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_skin_care);
            case ID_SLIPPER:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_slipper);
            case ID_SNACKS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_snacks);
            case ID_STAPLE_FOOD:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_staple_food);
            case ID_TEXTBOOK:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_textbook);
            case ID_TRANSPORTATION:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_transportation);
            case ID_UMBRELLA:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_umbrella);
            default:
                Log.d(TAG, "getIcon: not found");
                return null;
        }
    }
}
