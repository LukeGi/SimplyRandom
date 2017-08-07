package bluemonster122.mods.simplerandomstuff.util;

import net.minecraft.nbt.NBTTagCompound;

public class Ticker {
    private int m_max_time;
    private int m_current_time;

    public Ticker(int time) {
        reset(time);
    }

    public void tick( ) {
        m_current_time--;
    }

    public boolean time_up( ) {
        return m_current_time < 1;
    }

    public void reset(int time) {
        m_max_time = time;
        m_current_time = time;
    }

    public double percent_left( ) {
        if (m_max_time > 0) return (double) m_current_time / (double) m_max_time;
        return 0;
    }

    public double percent_gone( ) {
        return 1D - percent_left();
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("[TICKER]c", m_current_time);
        tag.setInteger("[TICKER]m", m_max_time);
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        m_current_time = tag.getInteger("[TICKER]c");
        m_max_time = tag.getInteger("[TICKER]m");
    }
}
