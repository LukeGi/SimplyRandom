package bluemonster122.mods.simplerandomstuff.core;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EventSuperBrokenUpdate
        extends Event {
    private TileEntitySRS brokenTile;

    public EventSuperBrokenUpdate(TileEntitySRS brokenTile) {
        this.brokenTile = brokenTile;
    }
}
