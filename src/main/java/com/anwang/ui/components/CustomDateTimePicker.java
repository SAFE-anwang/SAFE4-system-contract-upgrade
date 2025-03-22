package com.anwang.ui.components;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import com.github.lgooddatepicker.optionalusertools.TimeVetoPolicy;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTimePicker extends DateTimePicker {
    public CustomDateTimePicker() {
        super();
        this.getDatePicker().getSettings().setVetoPolicy(new DateVetoPolicy() {
            @Override
            public boolean isDateAllowed(LocalDate localDate) {
                return !localDate.isBefore(LocalDate.now());
            }
        });

        this.getTimePicker().getSettings().setFormatForDisplayTime("HH:mm:ss");
        this.getTimePicker().getSettings().setFormatForMenuTimes("HH:mm:ss");
        this.getTimePicker().getSettings().setVetoPolicy(new TimeVetoPolicy() {
            @Override
            public boolean isTimeAllowed(LocalTime localTime) {
                if (getDatePicker().getDate().isAfter(LocalDate.now())) {
                    return true;
                }
                if (getDatePicker().getDate().equals(LocalDate.now())) {
                    return !localTime.isBefore(LocalTime.now());
                }
                return false;
            }
        });

        this.getDatePicker().addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent dateChangeEvent) {
                if (getDateTimePermissive().isBefore(LocalDateTime.now())) {
                    setDateTimePermissive(LocalDateTime.now());
                    getTimePicker().getComponentTimeTextField().setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                }
            }
        });
    }
}
