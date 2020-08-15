package ninjaphenix.expandedstorage.impl.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BottomChestModel extends SingleChestModel
{
    public BottomChestModel()
    {
        super(64, 32);
        base.setTextureOffset(0, 0);
        base.addCuboid(0, 0, 0, 14, 16, 14, 0);
        base.setPivot(1, 0, 1);
    }
}