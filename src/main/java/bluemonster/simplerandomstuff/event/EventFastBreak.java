package bluemonster.simplerandomstuff.event;

import bluemonster.simplerandomstuff.core.TileCoreTicking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EventFastBreak extends Event {
    private TileCoreTicking tile;
    private EntityPlayer player;

    public EventFastBreak(TileCoreTicking tile, EntityPlayer player) {
        this.tile = tile;
        this.player = player;
    }
}
