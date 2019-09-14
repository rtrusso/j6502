package j6502.debugger;

import j6502.*;

import java.util.*;

public interface CPUEventListener {
    
    public void cpuChanged (CPUEvent cpue);

    public void cpuReset (CPUEvent cpue);

} // interface CPUEventListener
