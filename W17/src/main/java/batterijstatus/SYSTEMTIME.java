package batterijstatus;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

//typedef struct _SYSTEMTIME {
//        WORD wYear;
//        WORD wMonth;
//        WORD wDayOfWeek;
//        WORD wDay;
//        WORD wHour;
//        WORD wMinute;
//        WORD wSecond;
//        WORD wMilliseconds;
//        } SYSTEMTIME, *PSYSTEMTIME;

public class SYSTEMTIME extends Structure {

    public short wYear;
    public short wMonth;
    public short wDayOfWeek;
    public short wDay;
    public short wHour;
    public short wMinute;
    public short wSecond;
    public short wMilliseconds;


    public short getwYear(){
        return wYear;
    }

    public short getwMonth() {
        return wMonth;
    }

    public short getwDayOfWeek() {
        return wDayOfWeek;
    }

    public short getwDay() {
        return wDay;
    }

    public short getwHour() {
        return wHour;
    }

    public short getwMinute() {
        return wMinute;
    }

    public short getwSecond() {
        return wSecond;
    }

    public short getwMilliseconds() {
        return wMilliseconds;
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[]{"wYear", "wMonth", "wDayOfWeek", "wDay", "wHour", "wMinute","wSecond","wMilliseconds"});
    }

    @Override
    public String toString(){
        return "Year: " + getwYear() + ". Month: " + getwMonth() + ". Day: " + getwDay() + ". Day of week: " + getwDayOfWeek()
                + ". Hour: " + getwHour() + ". Minute: " + getwMinute() + ". Second:" + getwSecond() + ". Millisecond: " + getwMilliseconds();

    }
}
