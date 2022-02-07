package uj.java.pwj2020.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Calc {

    BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) {
        ZonedDateTime zonedStart = adapterStringToZonedDate(start);
        ZonedDateTime zonedEnd = adapterStringToZonedDate(end);

        if(zonedStart.isAfter(zonedEnd) || zonedStart.isEqual(zonedEnd)){
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
        } else {
            long hours = zonedStart.until(zonedEnd, ChronoUnit.HOURS) % 24;
            long days = zonedStart.until(zonedEnd, ChronoUnit.DAYS);
            long minutes = zonedStart.until(zonedEnd, ChronoUnit.MINUTES) % 1440;
            BigDecimal extraHours;
            if(minutes == 0 && hours == 0){
                extraHours = BigDecimal.ZERO;
            } else if(hours < 8){
                extraHours = dailyRate.divide(BigDecimal.valueOf(3),RoundingMode.HALF_DOWN);
            } else if(hours < 12){
                extraHours = dailyRate.divide(BigDecimal.valueOf(2),RoundingMode.HALF_DOWN);
            } else {
                extraHours = dailyRate;
            }
            return dailyRate.multiply(BigDecimal.valueOf(days)).add(extraHours);
        }
    }

    ZonedDateTime adapterStringToZonedDate(String input){
        String[] elements = input.split(" ");
        ZoneId zone = ZoneId.of(elements[2]);
        return ZonedDateTime.parse(elements[0] + "T" + elements[1] + zone.getRules().getOffset(LocalDateTime.MAX) + "[" + elements[2] + "]");
    }
}
