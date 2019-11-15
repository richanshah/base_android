package com.example.util;

import android.util.Log;

import com.cisner.cisnerapp.Cisner;
import com.cisner.cisnerapp.cometchat.utils.MyLg;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by HTISPL on 3/22/2017.
 */

public class DateUtils {

    private static String inputFormat = "dd MMM yyyy";
    private static String outputFormat = "dd.MM.yyyy";
//2018-01-31 15:00:00
    //17,May 2018
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_24 = "yyyy-MM-dd HH:mm:ss";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd.MM.yyyy hh:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DD_MM_YYYY = "dd - MM - yyyy";
    public static final String DD_MMM_YYYY = "dd MMM yyyy";
    public static final String DAY_DD_MMM_YYYY = "dd MMM yyyy\nEEEE";
    public static final String HH_MM = "hh:mm";
    public static final String HH_MM_24 = "HH:mm";
    public static final String HH_MM_SS_24 = "HH:mm:ss";
    public static final String HH_MM_PM = "hh:mm a";
    public static final String DD_MMMM = "dd MMM";
    public static final String DD_MMMM_HH_MM = "dd MMM hh:mm";
    public static final String IST_TO_EDT = "UTC−05:00";
    public static final String YYYY__MM__DD = "yyyy-MM-dd";
    public static final String DD__M__YYYY = "dd, MMMM yyyy";
    public static final String DDS_MMS_YYYY = "dd/MM/yyyy";
    public static final String YYYY_MMS_DD = "yyyy/MM/dd";
    public static final String DD_MM_YYYY_no = "dd-MM-yyyy";
    public static final String DD__MMM_YYYY = "dd,MMM yyyy";
    public static DateFormat defaultFormatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

    public static String getFormattedDate(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = null;

        if(inputDate != null && isValidFormat(YYYY__MM__DD,inputDate)) {
            df_input = new SimpleDateFormat(YYYY__MM__DD, java.util.Locale.getDefault());
        }else  if(inputDate != null && isValidFormat(YYYY_MMS_DD,inputDate)) {
            df_input = new SimpleDateFormat(YYYY_MMS_DD, java.util.Locale.getDefault());
        }
        SimpleDateFormat df_output = new SimpleDateFormat(DD__MMM_YYYY, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {
            MyLg.d("date ", e.getMessage()+" getFormattedDate");
        }
        return outputDate;
    }

    public static Date getFormattedDateStory(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_24);
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);
        Date outputDate=null;
        try {
            outputDate = dateFormatter.parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDate;
    }

    public static Date getFormattedDateStoryold(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, java.util.Locale.getDefault());
        df_input.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            parsed = df_input.parse(inputDate);
        } catch (ParseException e) {
            MyLg.d("date", e.getMessage());
        }
        return parsed;
    }

    public static String formateDateFromstring(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(DD_MM_YYYY, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(DD_MMM_YYYY, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {
            MyLg.d("date", e.getMessage() + " formateDateFromstring");
        }
        return outputDate;
    }

    public static String getFormattedDateWatcher(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(DD_MM_YYYY_no, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(DDS_MMS_YYYY, java.util.Locale.getDefault());

        try {
            if(inputDate !=null) {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);
            }
        } catch (ParseException e) {
            outputDate=inputDate;
            if (Cisner.IS_DEBUG) Log.d("date", e.getMessage()+" getFormattedDateWatcher");
        }
        return outputDate;
    }

    public static String getFormattedDateCreateEvent(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(YYYY_MMS_DD, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(DD_MM_YYYY_no, java.util.Locale.getDefault());

        try {
            if(inputDate !=null) {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);
            }
        } catch (ParseException e) {
            if (Cisner.IS_DEBUG) Log.d("date", e.getMessage()+" getFormattedDateCreateEvent");
        }
        return outputDate;
    }

    private static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
        }
        return date != null;
    }

    public static String getFormattedDateEvent(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = null;

        if(inputDate != null && isValidFormat(YYYY__MM__DD,inputDate)) {
            df_input = new SimpleDateFormat(YYYY__MM__DD, java.util.Locale.getDefault());
        }else  if(inputDate != null && isValidFormat(YYYY_MMS_DD,inputDate)) {
            df_input = new SimpleDateFormat(YYYY_MMS_DD, java.util.Locale.getDefault());
        }

        SimpleDateFormat df_output = new SimpleDateFormat(DD_MMMM, java.util.Locale.getDefault());

        try {
            if(inputDate !=null) {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);
            }
        } catch (ParseException e) {
            if (Cisner.IS_DEBUG) Log.d("date", e.getMessage()+" getFormattedDateEvent");
        }
        return outputDate;
    }

    public static String getFormattedDateEventAbout(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(YYYY_MMS_DD, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(DD__M__YYYY, java.util.Locale.getDefault());

        try {
            if(inputDate !=null) {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);
            }
        } catch (ParseException e) {
            if (Cisner.IS_DEBUG) Log.d("date", e.getMessage()+" getFormattedDateEventAbout");
        }
        return outputDate;
    }

    public static String getFormattedTimeEvent(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(HH_MM_SS_24, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(HH_MM_PM, java.util.Locale.getDefault());

        try {
            if(inputDate !=null) {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);
            }
        } catch (ParseException e) {
            if (Cisner.IS_DEBUG)  Log.d("date", e.getMessage()+" getFormattedTimeEvent");
        }
        return outputDate;
    }

    public static Date getDate(String format, String date) {
        SimpleDateFormat outPutFormate = new SimpleDateFormat(format, java.util.Locale.getDefault());
//        outPutFormate.setTimeZone(TimeZone.getTimeZone("GMT+05:00"));//"GMT+05:00" fot EDT time zone
        //outPutFormate.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        Date parsed = null;
        try {
            parsed = outPutFormate.parse(date);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return parsed;
    }

    public static String getDate(String format, Date date) {
        if (date == null)
            return "";
        SimpleDateFormat outPutFormate = new SimpleDateFormat(format, java.util.Locale.getDefault());
       // outPutFormate.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        return outPutFormate.format(date);
    }

    public static String getDate(String timeZone, String format, Date date) {
        if (date == null)
            return "";
        SimpleDateFormat outPutFormate = new SimpleDateFormat(format, java.util.Locale.getDefault());
        outPutFormate.setTimeZone(TimeZone.getTimeZone(timeZone));
        return outPutFormate.format(date);
    }

    /**
     * @param timestamp
     * @return
     */
    public static boolean isToday(long timestamp) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(timestamp);
        return (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);
        now.add(Calendar.DATE, -1);
        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }

    public static String convertDateTime(Date date) {
        if (date != null) {
            if (DateUtils.isToday(date.getTime())) {
                return "Today " + DateUtils.getDate(DateUtils.HH_MM, date);
            } else if (DateUtils.isYesterday(date.getTime())) {
                return "Yesterday " + DateUtils.getDate(DateUtils.HH_MM, date);
            } else {
                return DateUtils.getDate(DateUtils.DD_MMMM_HH_MM, date);
            }
        }
        return "";
    }

    public static String getFormattedDateWalletDashboard(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(YYYY_MMS_DD+" "+HH_MM_24, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(HH_MM_PM+", "+DD_MMM_YYYY, java.util.Locale.getDefault());

        try {
            if(inputDate !=null) {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);
            }
        } catch (ParseException e) {
            if (Cisner.IS_DEBUG)  Log.d("date", e.getMessage()+" getFormattedDateWalletDashboard");
        }
        return outputDate;
    }

    public static String getFormattedDateWallet(String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(YYYY__MM__DD+" "+HH_MM_SS_24, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(HH_MM_PM+", "+DD_MMM_YYYY, java.util.Locale.getDefault());

        try {
            if(inputDate !=null) {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);
            }
        } catch (ParseException e) {
            if (Cisner.IS_DEBUG)  Log.d("date", e.getMessage()+" getFormattedDateWallet");
        }
        return outputDate;
    }

    public static String getTimeDiff(String time) {
        //2016-07-27T16:14:01.027

        /*if (!time.contains(".")) {
            time = time + ".000";
        }*/
        Long millis = Long.parseLong(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        //2017-08-11 10:36:57
        //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        //dateFormat.setTimeZone(TimeZone.getTimeZone(Utils.getTimeZone(context)));

        try {
            Date oldDate = defaultFormatter.parse(calendar.getTime().toString());
            Date currentDate = new Date();

            long diff = currentDate.getTime() - oldDate.getTime();

            long days = TimeUnit.MILLISECONDS.toDays(diff);
            diff -= TimeUnit.DAYS.toMillis(days);

            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            diff -= TimeUnit.HOURS.toMillis(hours);

            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            diff -= TimeUnit.MINUTES.toMillis(minutes);

            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

            StringBuilder sb = new StringBuilder();

            if (days != 00) {

                if (days >= 365) {
                    int year = (int) (days / 365);
                    sb.append(year);
                    sb.append(" ");
                    sb.append(year == 1 ? "Year ago" : "Years ago");

                } else {
                    if (days >= 30) {

                        int month = (int) (days / 30);
                        sb.append(month);
                        sb.append(" ");
                        sb.append(month == 1 ? "Month ago" : "Months ago");

                    } else {
                        sb.append(days);
                        sb.append(" ");
                        sb.append(days == 1 ? "Day ago" : "Days ago");
                    }
                }
            } else if (hours != 00) {

                sb.append(hours);
                sb.append(" ");
                sb.append(hours == 1 ? "Hour ago" : "Hours ago");

            } else if (minutes != 00) {

                sb.append(minutes);
                sb.append(" ");
                sb.append(minutes == 1 ? "Minute ago" : "Minutes ago");

            } else if (seconds >= 00) {

//                sb.append(seconds);
                sb.append(" ");
                sb.append("just now");

            } else {

                // sb.append("0");
                sb.append(" ");
                sb.append("just now");
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }//End of getTimeDiff


    public static String SecToMinute(double seconds) {
        //int day = (int)TimeUnit.SECONDS.toDays(seconds);
        //long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        //long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        //long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
        //minutes =
        return  ""+(int)((seconds % 3600) / 60);
    }

}
