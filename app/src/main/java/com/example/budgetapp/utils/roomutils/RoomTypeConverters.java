package com.example.budgetapp.utils.roomutils;

import android.icu.util.TimeZone;
import android.util.Log;

import androidx.room.TypeConverter;

import com.example.budgetapp.utils.GlobalConsts;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Currency;

public class RoomTypeConverters {
    private static final String TAG = "RoomTypeConverters";
    @TypeConverter
    public static long DateTimeToEpochSecond(ZonedDateTime date){
        return date.toEpochSecond();
    }
    @TypeConverter
    public static ZonedDateTime EpochSecondToDateTime(long epochSecond){
        Instant instant = Instant.ofEpochSecond(epochSecond);
        return ZonedDateTime.ofInstant(instant, ZoneId.of(TimeZone.getDefault().getID()));
    }

    @TypeConverter
    public static long BigDecimalToLong(BigDecimal money){
        try {
            return money.movePointRight(GlobalConsts.CURRENCY_MINOR_PLACES).longValueExact();
        }catch (ArithmeticException arithmeticException){
            Log.e(TAG, "BigDecimalToInt: Conversion Error " + arithmeticException, arithmeticException);
            return Long.MAX_VALUE;
        }
    }
    @TypeConverter
    public static BigDecimal LongToBigDecimal(long money){
        BigDecimal bd = new BigDecimal(money);
        return bd.movePointLeft(GlobalConsts.CURRENCY_MINOR_PLACES);
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
