package org.teamhavei.havei.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.teamhavei.havei.R;

public class IconAdapter {

    private static String TAG = "DEBUG";

    //只能加，不能改！
    public static final int ID_LOST = 0;
    public static final int ID_CS_ACCOMMODATION = 1;
    public static final int ID_CS_AIR_TICKETS = 2;
    public static final int ID_CS_ASTRONOMY = 3;
    public static final int ID_CS_ATHLETE = 4;
    public static final int ID_CS_BONUS = 5;
    public static final int ID_CS_BORROW = 6;
    public static final int ID_CS_CAR_FARE = 7;
    public static final int ID_CS_CLOTHING = 8;
    public static final int ID_CS_DRINKS = 9;
    public static final int ID_CS_ELSE = 10;
    public static final int ID_CS_EXERCISE = 11;//cs_exercize.xml
    public static final int ID_CS_EXPENSE_MANAGEMENT = 12;
    public static final int ID_CS_FRUITS = 13;
    public static final int ID_CS_GAME = 14;
    public static final int ID_CS_GIFT1 = 15;
    public static final int ID_CS_GIFT2 = 16;
    public static final int ID_CS_GIFT3 = 17;
    public static final int ID_CS_HOBBY = 18;
    public static final int ID_CS_HOSPITAL = 19;
    public static final int ID_CS_IN_SCHOOL = 20;
    public static final int ID_CS_JOURNEY = 21;
    public static final int ID_CS_LAUNDRY_DETERGENT = 22;
    public static final int ID_CS_LEND_OUT = 23;//cs_lend_out.xml
    //WARN: BROKEN!!
    public static final int ID_CS_MAKE_UP = 24;
    public static final int ID_CS_MEDICINE = 25;
    public static final int ID_CS_OTHERS_REPAY_YOU1 = 26;//cs_others_repay_you.xml
    public static final int ID_CS_OTHERS_REPAY_YOU2 = 27;//cs_others_repay_you2.xml
    public static final int ID_CS_TISSUE_PAPER_ROLL = 28;//cs_paper.xml
    public static final int ID_CS_PARTY = 29;
    public static final int ID_CS_PETS = 30;
    public static final int ID_CS_PHOTOGRAPHING = 31;
    public static final int ID_CS_PLAYTHING = 32;//cs_plaything.xml
    public static final int ID_CS_READING = 33;
    public static final int ID_CS_RECEIPT = 34;
    public static final int ID_CS_RECREATION = 35;
    public static final int ID_CS_RED_PACKET = 36;
    public static final int ID_CS_REPAY_OTHERS = 37;//cs_repay_others.xml
    public static final int ID_CS_SALARY = 38;
    public static final int ID_CS_SCENIC_SPOT = 39;
    public static final int ID_CS_SKIN_CARE = 40;
    public static final int ID_CS_SLIPPER = 41;
    public static final int ID_CS_SNACKS = 42;
    public static final int ID_CS_STAPLE_FOOD = 43;
    public static final int ID_CS_TEXTBOOK = 44;
    public static final int ID_CS_TRANSPORTATION = 45;
    public static final int ID_CS_UMBRELLA = 46;
    //以上是cs系列图标
    public static final int ID_HS_ALARM_CLOCK = 47;
    public static final int ID_HS_BACHELOR = 48;
    public static final int ID_HS_BASKETBALL = 49;
    public static final int ID_HS_BONUS = 50;
    public static final int ID_HS_BOOK = 51;
    public static final int ID_HS_BOOKKEEPING = 52;
    public static final int ID_HS_BOOKS = 53;
    public static final int ID_HS_CAR = 54;
    public static final int ID_HS_CLOCK1 = 55;
    public static final int ID_HS_CLOCK2 = 56;
    public static final int ID_HS_COUPLE = 57;
    public static final int ID_HS_CUP = 58;
    public static final int ID_HS_CUTLERY = 59;
    public static final int ID_HS_DELETE = 60;
    public static final int ID_HS_DESSERT = 61;
    public static final int ID_HS_DRESS_UP = 62;
    public static final int ID_HS_DRINK = 63;
    public static final int ID_HS_EAT = 64;
    public static final int ID_HS_ENTERTAINMENT = 65;
    public static final int ID_HS_EXERCISE = 66;
    public static final int ID_HS_FOOT = 67;
    public static final int ID_HS_FRUIT = 68;
    public static final int ID_HS_GAME_MACHINE = 69;
    public static final int ID_HS_GIFT1 = 70;
    public static final int ID_HS_GIFT2 = 71;
    public static final int ID_HS_GIFT3 = 72;
    public static final int ID_HS_HEART = 73;
    public static final int ID_HS_HEART2 = 74;
    public static final int ID_HS_HEART3 = 75;
    public static final int ID_HS_BEATING = 76;
    public static final int ID_HS_BEATING2 = 77;
    public static final int ID_HS_HOTEL = 78;
    public static final int ID_HS_JOURNEY1 = 79;
    public static final int ID_HS_JOURNEY2 = 80;
    public static final int ID_HS_LEND = 81;
    public static final int ID_HS_LOCATING = 82;
    //WARN: BROKEN!!
    public static final int ID_HS_MAKE_UP = 83;
    public static final int ID_HS_MEDIACARE = 84;
    public static final int ID_HS_MEDICINE = 85;
    public static final int ID_HS_MEDICINE3 = 86;
    public static final int ID_HS_MEMORY = 87;
    public static final int ID_HS_MONEY = 88;
    public static final int ID_HS_MOON = 89;
    public static final int ID_HS_MOON3 = 90;
    public static final int ID_HS_MOON4 = 91;
    public static final int ID_HS_MOON5 = 92;
    public static final int ID_HS_MUSIC = 93;
    public static final int ID_HS_PAPER = 94;
    public static final int ID_HS_PAPER_AIRPLANE = 95;
    public static final int ID_HS_PENCIL = 96;
    public static final int ID_HS_PETS = 97;
    public static final int ID_HS_PHOTOGRAPH = 98;
    public static final int ID_HS_PIG = 99;
    public static final int ID_HS_PLANE = 100;
    public static final int ID_HS_PRIZE = 101;
    public static final int ID_HS_READING = 102;
    public static final int ID_HS_RECORD = 103;
    public static final int ID_HS_RECORD2 = 104;
    public static final int ID_HS_RECREATION = 105;
    public static final int ID_HS_RED_PACKET = 106;
    public static final int ID_HS_REPAIR = 107;
    public static final int ID_HS_SALARY = 108;
    public static final int ID_HS_SCISSORS = 109;
    public static final int ID_HS_SCISSORS2 = 110;
    public static final int ID_HS_SKIN_CARE = 111;
    public static final int ID_HS_SPORT = 112;
    public static final int ID_HS_STAR1 = 113;
    public static final int ID_HS_STAR2 = 114;
    public static final int ID_HS_STAR3 = 115;
    public static final int ID_HS_STAR_TRAILS = 116;
    public static final int ID_HS_STATISTICS = 117;
    public static final int ID_HS_STUDY = 118;
    public static final int ID_HS_SUN = 119;
    public static final int ID_HS_SUN3 = 120;
    public static final int ID_HS_SUN4 = 121;
    public static final int ID_HS_SUN_AND_MOON = 122;
    public static final int ID_HS_SUN_RISE = 123;
    public static final int ID_HS_SUN_RISE2 = 124;
    public static final int ID_HS_TOY = 125;
    public static final int ID_HS_TRANSPORT = 126;
    public static final int ID_HS_UMBRELLA = 127;
    public static final int ID_HS_WATER = 128;
    public static final int ID_HS_WATER_DROP = 129;
    //只能加，不能改！


    Context mContext;

    public IconAdapter(Context context) {
        mContext = context;
    }

    public Drawable getIcon(int iconID) {
        switch (iconID) {
            case ID_CS_ACCOMMODATION:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_accommodation);
            case ID_CS_AIR_TICKETS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_air_tickets);
            case ID_CS_ASTRONOMY:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_astronomy);
            case ID_CS_ATHLETE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_athlete);
            case ID_CS_BONUS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_bonus);
            case ID_CS_BORROW:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_borrow);
            case ID_CS_CAR_FARE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_car_fare);
            case ID_CS_CLOTHING:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_clothing);
            case ID_CS_DRINKS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_drinks);
            case ID_CS_ELSE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_else);
            case ID_CS_EXERCISE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_exercize);
            case ID_CS_EXPENSE_MANAGEMENT:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_expense_management);
            case ID_CS_FRUITS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_fruits);
            case ID_CS_GAME:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_game);
            case ID_CS_GIFT1:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_gift1);
            case ID_CS_GIFT2:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_gift2);
            case ID_CS_GIFT3:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_gift3);
            case ID_CS_HOBBY:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_hobby);
            case ID_CS_HOSPITAL:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_hospital);
            case ID_CS_IN_SCHOOL:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_in_school);
            case ID_CS_JOURNEY:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_journey);
            case ID_CS_LAUNDRY_DETERGENT:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_laundry_detergent);
            case ID_CS_LEND_OUT:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_lend_out);
            case ID_CS_MAKE_UP:
                Log.d(TAG, "getIcon: MAKE_UP is currently down, returning lost");
                return ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_help_24);
            case ID_CS_MEDICINE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_medicine);
            case ID_CS_OTHERS_REPAY_YOU1:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_others_repay_you);
            case ID_CS_OTHERS_REPAY_YOU2:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_others_repay_you2);
            case ID_CS_TISSUE_PAPER_ROLL:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_paper);
            case ID_CS_PARTY:
                Log.d(TAG, "getIcon: PARTY is currently down, returning first icon");
                return ContextCompat.getDrawable(mContext, R.drawable.cs_accommodation);
            case ID_CS_PETS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_pets);
            case ID_CS_PHOTOGRAPHING:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_photographing);
            case ID_CS_PLAYTHING:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_plaything);
            case ID_CS_READING:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_reading);
            case ID_CS_RECEIPT:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_receipt);
            case ID_CS_RECREATION:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_recreation);
            case ID_CS_RED_PACKET:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_red_packet);
            case ID_CS_REPAY_OTHERS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_repay_others);
            case ID_CS_SALARY:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_salary);
            case ID_CS_SCENIC_SPOT:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_scenic_spot);
            case ID_CS_SKIN_CARE:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_skin_care);
            case ID_CS_SLIPPER:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_slipper);
            case ID_CS_SNACKS:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_snacks);
            case ID_CS_STAPLE_FOOD:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_staple_food);
            case ID_CS_TEXTBOOK:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_textbook);
            case ID_CS_TRANSPORTATION:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_transportation);
            case ID_CS_UMBRELLA:
                return ContextCompat.getDrawable(mContext, R.drawable.cs_umbrella);
            case ID_HS_ALARM_CLOCK:
                return ContextCompat.getDrawable(mContext, R.drawable.alarm_clock);
            case ID_HS_BACHELOR:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_bachelor);
            case ID_HS_BASKETBALL:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_basketball);
            case ID_HS_BONUS:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_bonus);
            case ID_HS_BOOK:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_book);
            case ID_HS_BOOKKEEPING:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_bookkeeping);
            case ID_HS_BOOKS:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_books);
            case ID_HS_CAR:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_car);
            case ID_HS_CLOCK1:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_clock1);
            case ID_HS_CLOCK2:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_clock2);
            case ID_HS_COUPLE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_couple);
            case ID_HS_CUP:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_cup);
            case ID_HS_CUTLERY:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_cutlery);
            case ID_HS_DELETE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_delete);
            case ID_HS_DESSERT:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_dessert);
            case ID_HS_DRESS_UP:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_dress_up);
            case ID_HS_DRINK:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_drink);
            case ID_HS_EAT:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_eat);
            case ID_HS_ENTERTAINMENT:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_entertainment);
            case ID_HS_EXERCISE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_exercise);
            case ID_HS_FOOT:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_foot);
            case ID_HS_FRUIT:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_fruit);
            case ID_HS_GAME_MACHINE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_gamemachine);
            case ID_HS_GIFT1:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_gift1);
            case ID_HS_GIFT2:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_gift2);
            case ID_HS_GIFT3:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_gift3);
            case ID_HS_HEART:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_heart);
            case ID_HS_HEART2:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_heart2);
            case ID_HS_HEART3:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_heart3);
            case ID_HS_BEATING:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_heart_beating);
            case ID_HS_BEATING2:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_heart_beating2);
            case ID_HS_HOTEL:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_hotel);
            case ID_HS_JOURNEY1:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_journey1);
            case ID_HS_JOURNEY2:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_journey2);
            case ID_HS_LEND:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_lend);
            case ID_HS_LOCATING:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_locating);
            case ID_HS_MAKE_UP:
                Log.d(TAG, "getIcon: MAKE_UP is currently down, returning lost");
                return ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_help_24);
            case ID_HS_MEDIACARE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_mediacare);
            case ID_HS_MEDICINE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_medicine);
            case ID_HS_MEDICINE3:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_medicine3);
            case ID_HS_MEMORY:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_memory);
            case ID_HS_MONEY:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_money);
            case ID_HS_MOON:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_moon);
            case ID_HS_MOON3:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_moon3);
            case ID_HS_MOON4:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_moon4);
            case ID_HS_MOON5:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_moon5);
            case ID_HS_MUSIC:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_music);
            case ID_HS_PAPER:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_paper);
            case ID_HS_PAPER_AIRPLANE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_paper_airplane);
            case ID_HS_PENCIL:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_pencil);
            case ID_HS_PETS:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_pets);
            case ID_HS_PHOTOGRAPH:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_photograph);
            case ID_HS_PIG:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_pig);
            case ID_HS_PLANE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_plane);
            case ID_HS_PRIZE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_prize);
            case ID_HS_READING:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_reading);
            case ID_HS_RECORD:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_record);
            case ID_HS_RECORD2:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_record2);
            case ID_HS_RECREATION:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_recreation);
            case ID_HS_RED_PACKET:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_red_packet);
            case ID_HS_REPAIR:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_repair);
            case ID_HS_SALARY:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_salary);
            case ID_HS_SCISSORS:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_scissors);
            case ID_HS_SCISSORS2:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_scissors2);
            case ID_HS_SKIN_CARE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_skin_care);
            case ID_HS_SPORT:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_sport);
            case ID_HS_STAR1:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_star1);
            case ID_HS_STAR2:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_star2);
            case ID_HS_STAR3:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_star3);
            case ID_HS_STAR_TRAILS:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_star_trails);
            case ID_HS_STATISTICS:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_statistics);
            case ID_HS_STUDY:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_study);
            case ID_HS_SUN:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_sun);
            case ID_HS_SUN3:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_sun3);
            case ID_HS_SUN4:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_sun4);
            case ID_HS_SUN_AND_MOON:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_sun_and_moon);
            case ID_HS_SUN_RISE:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_sun_rise);
            case ID_HS_SUN_RISE2:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_sun_rise2);
            case ID_HS_TOY:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_toy);
            case ID_HS_TRANSPORT:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_transport);
            case ID_HS_UMBRELLA:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_umbrella);
            case ID_HS_WATER:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_water);
            case ID_HS_WATER_DROP:
                return ContextCompat.getDrawable(mContext, R.drawable.hs_water_drop);
            default:
                Log.d(TAG, "getIcon: not found, returning lost");
                return ContextCompat.getDrawable(mContext,R.drawable.ic_baseline_help_24);
        }
    }
}