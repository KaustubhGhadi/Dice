package in.programmeraki.glaty;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.programmeraki.glaty.model.CalenderFeed;
import in.programmeraki.glaty.model.TrackerAlert;
import in.programmeraki.glaty.roomdb.AppDatabase;
import in.programmeraki.glaty.utils.Constant;

public class Common extends Application {

    public CalenderFeed notificationFeed;

    public static Common instance = new Common();
    public ArrayList<TrackerAlert> trackerAlerts = new ArrayList<>();
    public AppDatabase db;

    public ArrayList<TrackerAlert> getPulseTrackerAlerts() {
        ArrayList<TrackerAlert> temp = new ArrayList<>();
        for (int i = 0; i < trackerAlerts.size(); i++) {
            if(trackerAlerts.get(i).getType() == TrackerAlert.pulseType){
                temp.add(trackerAlerts.get(i));
            }
        }
        return temp;
    }

    public ArrayList<TrackerAlert> getTempTrackerAlerts() {
        ArrayList<TrackerAlert> temp = new ArrayList<>();
        for (int i = 0; i < trackerAlerts.size(); i++) {
            if(trackerAlerts.get(i).getType() == TrackerAlert.tempType){
                temp.add(trackerAlerts.get(i));
            }
        }
        return temp;
    }
//
//    public String getAreaName(int feedType){
//        if(feedType == Constant.AREA_LEFT_ARM){
//            return "Left Arm";
//        } else if(feedType == Constant.AREA_RIGHT_ARM) {
//            return "Right Arm";
//        } else if(feedType == Constant.AREA_LEFT_THIGH) {
//            return "Left Thigh";
//        } else if(feedType == Constant.AREA_RIGHT_THIGH) {
//            return "Right Thigh";
//        }  else if(feedType == Constant.AREA_LEFT_BUTT) {
//            return "Left Hip";
//        }  else if(feedType == Constant.AREA_RIGHT_BUTT) {
//            return "Right Hip";
//        } else if(feedType == Constant.AREA_STOMACH) {
//            return "Abdomen";
//        } else {
//            return "Unknown";
//        }
//    }
//
//    public int getAreaImageId(int feedType){
//        if(feedType == Constant.AREA_LEFT_ARM){
//            return R.drawable.area_left_arm;
//        } else if(feedType == Constant.AREA_RIGHT_ARM) {
//            return R.drawable.area_right_arm;
//        } else if(feedType == Constant.AREA_LEFT_THIGH) {
//            return R.drawable.area_left_thigh;
//        } else if(feedType == Constant.AREA_RIGHT_THIGH) {
//            return R.drawable.area_right_thigh;
//        }  else if(feedType == Constant.AREA_LEFT_BUTT) {
//            return R.drawable.area_left_butt;
//        }  else if(feedType == Constant.AREA_RIGHT_BUTT) {
//            return R.drawable.area_right_butt;
//        } else {
//            return R.drawable.area_stomach;
//        }
//    }
//
//    public int getAreaColor(int feedType){
//        if(feedType == Constant.AREA_LEFT_ARM){
//            return R.color.colorGlatyYellow;
//        } else if(feedType == Constant.AREA_RIGHT_ARM) {
//            return R.color.colorGlatyOrange;
//        } else if(feedType == Constant.AREA_LEFT_THIGH) {
//            return R.color.colorGlatyPurple;
//        } else if(feedType == Constant.AREA_RIGHT_THIGH) {
//            return R.color.colorGlatyRed;
//        }  else if(feedType == Constant.AREA_LEFT_BUTT) {
//            return R.color.colorGlatyBlue;
//        }  else if(feedType == Constant.AREA_RIGHT_BUTT) {
//            return R.color.colorGlatyGreen;
//        } else {
//            return R.color.colorGlatyBrown;
//        }
//    }


    public String getAreaName(int feedType){
        if(feedType == Constant.DICEONE){
            return "ONE";
        } else if(feedType == Constant.DICETWO) {
            return "TWO";
        } else if(feedType == Constant.DICETHREE) {
            return "THREE";
        } else if(feedType == Constant.DICEFOUR) {
            return "FOUR";
        }  else if(feedType == Constant.DICEFIVE) {
            return "FIVE";
        }  else if(feedType == Constant.DICESIX) {
            return "SIX";
        }  else {
            return "Unknown";
        }
    }

    public int getAreaImageId(int feedType){
        if(feedType == Constant.DICEONE){
            return R.drawable.dice1;
        } else if(feedType == Constant.DICETWO) {
            return R.drawable.dice2;
        } else if(feedType == Constant.DICETHREE) {
            return R.drawable.dice3;
        } else if(feedType == Constant.DICEFOUR) {
            return R.drawable.dice4;
        }  else if(feedType == Constant.DICEFIVE) {
            return R.drawable.dice5;
        }  else {
            return R.drawable.dice6;
        }
    }

    public int getAreaColor(int feedType){
        if(feedType == Constant.DICEONE){
            return R.color.colorGlatyYellow;
        } else if(feedType == Constant.DICETWO) {
            return R.color.colorGlatyOrange;
        } else if(feedType == Constant.DICETHREE) {
            return R.color.colorGlatyPurple;
        } else if(feedType == Constant.DICEFOUR) {
            return R.color.colorGlatyRed;
        } else if(feedType == Constant.DICEFIVE) {
            return R.color.colorGlatyGreen;
        } else {
            return R.color.colorGlatyBlue;
        }
    }

    public String getDateStr(Calendar c){
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM");

        String str = ""+ formatter.format(c.getTime()) + " ";
        formatter = new SimpleDateFormat("dd");
        str += "" + formatter.format(c.getTime());
        return str;
    }

    public String getTimeStr(Calendar c){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        return "" + formatter.format(c.getTime());
    }

    public int getFeedAreaType(int input) {
        return input;
    }
}
