package batterijstatus;

import com.sun.jna.ptr.IntByReference;

import java.io.Console;

public class JNAMain {

    public static void main(String[] args) {

        //====================================Systeem Time==========================================

        I_SYSTEMTIJD lib = I_SYSTEMTIJD.INSTANCE;

        SYSTEMTIME systemTime = new SYSTEMTIME();
        lib.GetSystemTime(systemTime);
        long nanoTime = System.nanoTime();
        System.out.println(systemTime.toString());

        for (int i = 0; i < 1000000000; i++) {

        }

        lib.GetSystemTime(systemTime);
        System.out.println(systemTime.toString());

        long nanoTime2 = System.nanoTime();
        System.out.println(String.format("nanoTime : %d ms", nanoTime2 - nanoTime));

        //====================================Disk space==========================================
//        FREEDISKSPACE freediskspace = new FREEDISKSPACE();

        IntByReference sectorsPerCluster = new IntByReference();
        IntByReference bytesPerSector = new IntByReference();
        IntByReference numberOfFreeClusters = new IntByReference();
        IntByReference totalNumberOfClusters = new IntByReference();

        lib.GetDiskFreeSpaceA("C:\\",
                sectorsPerCluster,
                bytesPerSector,
                numberOfFreeClusters,
                totalNumberOfClusters
        );
        System.out.println(sectorsPerCluster.getValue() );
        System.out.println(bytesPerSector.getValue() );
        System.out.println(numberOfFreeClusters.getValue() );
        System.out.println(totalNumberOfClusters.getValue());

        long freeSpace = ((long)(numberOfFreeClusters.getValue() * sectorsPerCluster.getValue()) * bytesPerSector.getValue())/1024;
        System.out.println(String.format("Free disk space : %d kB",freeSpace));
        Console cons = System.console();
        if (cons != null) { // when run from console
            cons.printf("Enter to exit ");
            cons.readLine();
        }

    }
}
