package com.savindu.Todo.Application.util;

import com.savindu.Todo.Application.service.impl.TodoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

public class DateTimeValidateUtil {
    private static Logger  logger = LoggerFactory.getLogger(TodoServiceImpl.class);
    public static LocalDate parseLocalDate(String date) {
        try {
            return date != null ? LocalDate.parse(date) : null;
        } catch (DateTimeParseException e) {
            logger.error("Invalid LocalDate format: {}", date, e);
            throw e;
        }
    }

    public static LocalDateTime parseLocalDateTime(String date, boolean startOfDay) {
        try {
            if (date == null) return null;
            LocalDate localDate = LocalDate.parse(date);
            return startOfDay ? localDate.atStartOfDay() : localDate.atTime(LocalTime.MAX);
        } catch (DateTimeParseException e) {
            logger.error("Invalid LocalDateTime format: {}", date, e);
            throw e;
        }
    }
    public static LocalDate convertUtilDateToLocalDate(Date utilDate) {
        if (utilDate == null) return null;
        return utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
