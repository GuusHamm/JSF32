package batterijstatus;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

import java.util.List;

/**
 * Created by wouter on 19-1-2016.
 */
public class FREEDISKSPACE extends Structure {

    public IntByReference sectorsPerCluster ;
    public IntByReference bytesPerSector ;
    public IntByReference numberOfFreeClusters ;
    public IntByReference totalNumberOfClusters ;

    public IntByReference getSectorsPerCluster() {
        return sectorsPerCluster;
    }

    public IntByReference getBytesPerSector() {
        return bytesPerSector;
    }

    public IntByReference getNumberOfFreeClusters() {
        return numberOfFreeClusters;
    }

    public IntByReference getTotalNumberOfClusters() {
        return totalNumberOfClusters;
    }

    @Override
    protected List getFieldOrder() {
        return null;
    }
}

