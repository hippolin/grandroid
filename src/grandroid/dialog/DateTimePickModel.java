/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 *
 * @author Rovers
 */
public abstract class DateTimePickModel implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    protected int mode;
    protected Calendar cal;
    public static final int DateMode = 0;
    public static final int TimeMode = 1;

    public DateTimePickModel(int mode) {
        this.mode = mode;
        cal = Calendar.getInstance();
    }

    public DateTimePickModel(int mode, Calendar cal) {
        this.mode = mode;
        this.cal = cal;
    }

    public void onDateSet(DatePicker picker, int year, int month, int date) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, date);
        onPicked(DateMode, c);
    }

    public void onTimeSet(TimePicker picker, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        onPicked(TimeMode, c);
    }

    public int getMode() {
        return mode;
    }

    public Dialog createDialog(Context context) {
        switch (mode) {
            case DateMode:
                return new DatePickerDialog(context, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            case TimeMode:
                return new TimePickerDialog(context, this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        }
        return null;
    }

    public abstract void onPicked(int mode, Calendar calendar);
}
