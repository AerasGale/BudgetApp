package com.example.budgetapp.utils.roomutils;

import android.icu.util.TimeZone;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Currency;

public class RoomTypeConverters {
    @TypeConverter
    public static long DateTimeToEpochSecond(ZonedDateTime date){
        return date.toEpochSecond();
    }
    @TypeConverter
    public static ZonedDateTime EpochSecondToDateTime(long epochSecond){
        Instant instant = Instant.ofEpochSecond(epochSecond);
        return ZonedDateTime.ofInstant(instant, ZoneId.of(TimeZone.getDefault().getID()));
    }

    // Just shift by 4 everytime, as the most decimal places is 4
    @TypeConverter
    public static int BigDecimalToInt(BigDecimal money){
        return money.movePointRight(4).intValue();
    }
    @TypeConverter
    public static BigDecimal IntToBigDecimal(int money){
        BigDecimal bd = new BigDecimal(money);
        return bd.movePointLeft(4);
    }

    @TypeConverter
    public static String CurrencyToString(Currency currency){
        return currency.getCurrencyCode();
    }
    @TypeConverter
    public Currency StringToCurrency(String code){
        Currency currency;
        try {
            currency = Currency.getInstance(code);
        } catch (IllegalArgumentException iae){
            currency = null;
        }

        return currency;
    }
}
