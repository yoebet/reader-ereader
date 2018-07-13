package wjy.yo.ereader.db;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeConverter {

    private static final String iso8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static SimpleDateFormat sdf = new SimpleDateFormat(iso8601, Locale.CHINA);

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public Date fromString(String dateString) {
        if (dateString == null) {
            return null;
        }

        Date date = null;
        try {
            date = sdf.parse(dateString);
            System.out.println(dateString + " -> " + date);
        } catch (ParseException e) {
            Log.d("TypeConverter", e.getMessage());
        }

        return date;
    }

    @TypeConverter
    public String dateToString(Date date) {
        if (date == null) {
            return null;
        } else {
            return sdf.format(date);
        }
    }
}
