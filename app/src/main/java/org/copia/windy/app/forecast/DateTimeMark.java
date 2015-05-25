package org.copia.windy.app.forecast;

import java.util.Calendar;
import java.util.Date;

public enum DateTimeMark {
    TODAY( 0, "Today" ),
    TOMORROW( 1, "Tomorrow" ),
    DA_TOMORROW( 2, "D.A. Tomorrow" );

    private int shift;

    private String readableName;

    DateTimeMark( int shift, String readableName ) {
        this.shift = shift;
        this.readableName = readableName;
    }

    public Date getStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR_OF_DAY, 0 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );
        calendar.set( Calendar.DATE, calendar.get( Calendar.DATE ) + shift );
        return calendar.getTime();
    }

    public String getReadableName() {
        return readableName;
    }

    public boolean isBelong( Date date ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( getStartDate() );
        calendar.set( Calendar.DATE, calendar.get( Calendar.DATE ) + 1 );

        Date endPoint = calendar.getTime();
        return date.equals( getStartDate() ) || (date.after( getStartDate() ) && date.before( endPoint ));
    }

    public static DateTimeMark getLastMark() {
        return DA_TOMORROW;
    }

    public static Date getLastTimeMark() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( DA_TOMORROW.getStartDate() );
        calendar.set( Calendar.DATE, calendar.get( Calendar.DATE ) + 1 );

        return calendar.getTime();
    }
}
