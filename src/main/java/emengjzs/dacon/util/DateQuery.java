package emengjzs.dacon.util;

import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class DateQuery {

    private Calendar c;

    public DateQuery() {
        c = Calendar.getInstance();
    }

    public DateQuery(Calendar c) {
        this.c = c;
    }

    public static DateQuery fromNow() {
        return new DateQuery();
    }

    public DateQuery add(int field, int amount) {
        c.add(field, amount);
        return this;
    }

    public DateQuery sub(int field, int amount) {
        c.add(field, -amount);
        return this;
    }

    public DateQuery set(int field, int amount) {
        c.set(field, amount);
        return this;
    }

    public DateQuery dateBegin() {
        c = DateUtils.truncate(c, Calendar.DATE);
        return this;
    }

    public DateQuery dateEnd() {
        return this.set(Calendar.SECOND, 59)
                .set(Calendar.MINUTE, 59)
                .set(Calendar.HOUR_OF_DAY, 23);
    }


    public Date toDate() {
        return c.getTime();
    }

}
