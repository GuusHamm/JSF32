package batterijstatus;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public interface I_SYSTEMTIJD extends Library {

	public I_SYSTEMTIJD INSTANCE=(I_SYSTEMTIJD)Native.loadLibrary("Kernel32", I_SYSTEMTIJD.class);
		
	// the function we need
	void GetSystemTime(SYSTEMTIME st);

    boolean GetDiskFreeSpaceA(String path, IntByReference sectorsPerCluster, IntByReference bytesPerSector, IntByReference numberOfFreeClusters, IntByReference totalNumberOfClusters);

    boolean GetDiskFreeSpaceW(char[] chars, IntByReference sectorsPerCluster, IntByReference bytesPerSector, IntByReference numberOfFreeClusters, IntByReference totalNumberOfClusters);
}
