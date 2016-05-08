package emengjzs.dacon.context.tool;

import org.apache.velocity.tools.generic.DateTool;

import java.util.Calendar;
import java.util.Date;

public class ExtDateTool extends DateTool {

    public ExtDateTool() {
        // TODO Auto-generated constructor stub
    }

    public Date getFutureDate(int field, int size) {
        Calendar c = Calendar.getInstance();
        c.add(field, size);
        return c.getTime();

    }

}
