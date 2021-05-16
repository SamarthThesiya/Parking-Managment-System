package utils;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateTimeUtil {

    public static final String FORMAT_FOR_CONVERT_TO_TIMESTAMP = "dd/MM/yyyy hh:mm aaa";

    public static float getHourDifferenceBetweenTimeStamps(Timestamp timestamp1, Timestamp timestamp2) {
        long diff = timestamp2.getTime() - timestamp1.getTime();

        float hours = diff/(60*60*1000f);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Float.parseFloat(decimalFormat.format(hours));
    }


    public static Timestamp getTimestampFromDateTime(String dateTime) {

        SimpleDateFormat dateFormatObj = new SimpleDateFormat(FORMAT_FOR_CONVERT_TO_TIMESTAMP);
        Date             parsedDate = null;
        try {
            parsedDate = dateFormatObj.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(parsedDate.getTime());
    }

    public static String convertServerToClientDateTime(@NotNull String date) {
        return convertDateTimFormat(date, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd h:mm aaa");
    }

    public static String convertDateTimFormat(@NotNull String date, String from, String to) {
        SimpleDateFormat fromFormat = new SimpleDateFormat(from);
        SimpleDateFormat toFormat = new SimpleDateFormat(to);
        try {
            return toFormat.format(Objects.requireNonNull(fromFormat.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
