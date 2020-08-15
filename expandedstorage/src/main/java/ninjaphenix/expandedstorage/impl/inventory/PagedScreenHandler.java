package ninjaphenix.expandedstorage.impl.inventory;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ninjaphenix.expandedstorage.impl.screen.PagedScreenMeta;

import java.util.Arrays;

public final class PagedScreenHandler extends AbstractContainer<PagedScreenMeta>
{
    private static final ImmutableMap<Integer, PagedScreenMeta> SIZES = initializedMap(map ->
    {
        map.put(27, new PagedScreenMeta(9, 3, 1, 27, getTexture("shared", 9, 3), 208, 192)); // Wood
        map.put(54, new PagedScreenMeta(9, 6, 1, 54, getTexture("shared", 9, 6), 208, 240)); // Iron / Large Wood
        map.put(81, new PagedScreenMeta(9, 9, 1, 81, getTexture("shared", 9, 9), 208, 304)); // Gold
        map.put(108, new PagedScreenMeta(9, 6, 2, 108, getTexture("shared", 9, 6), 208, 240)); // Diamond / Large Iron
        map.put(135, new PagedScreenMeta(9, 5, 3, 135, getTexture("shared", 9, 5), 208, 224)); // Netherite
        map.put(162, new PagedScreenMeta(9, 6, 3, 162, getTexture("shared", 9, 6), 208, 240)); // Large Gold
        map.put(216, new PagedScreenMeta(9, 8, 3, 216, getTexture("shared", 9, 8), 208, 288)); // Large Diamond
        map.put(270, new PagedScreenMeta(10, 9, 3, 270, getTexture("shared", 10, 9), 224, 304)); // Large Netherite
    });

    public PagedScreenHandler(ScreenHandlerType<?> type, int syncId, BlockPos pos, Inventory inventory,
            PlayerEntity player, Text displayName)
    {
        super(type, syncId, pos, inventory, player, displayName, getNearestSize(inventory.size()));
        resetSlotPositions(true);
        final int left = (SCREEN_META.WIDTH * 18 + 14) / 2 - 80;
        final int top = 18 + 14 + (SCREEN_META.HEIGHT * 18);
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                addSlot(new Slot(PLAYER_INVENTORY,y * 9 + x + 9, left + 18 * x, top + y * 18));
            }
        }
        for (int i = 0; i < 9; i++) { this.addSlot(new Slot(PLAYER_INVENTORY, i, left + 18 * i, top + 58)); }
    }

    private static PagedScreenMeta getNearestSize(int invSize)
    {
        PagedScreenMeta val = SIZES.get(invSize);
        if (val != null) { return val; }
        final Integer[] keys = SIZES.keySet().toArray(new Integer[]{});
        Arrays.sort(keys);
        final int largestKey = keys[Math.abs(Arrays.binarySearch(keys, invSize)) - 1];
        val = SIZES.get(largestKey);
        if (largestKey > invSize && largestKey - invSize <= val.WIDTH) { return SIZES.get(largestKey); }
        throw new RuntimeException("No screen can show an inventory of size " + invSize + ".");
    }

    public void resetSlotPositions(final boolean createSlots)
    {
        for (int i = 0; i < INVENTORY.size(); i++)
        {
            final int x = i % SCREEN_META.WIDTH;
            int y = MathHelper.ceil((((double) (i - x)) / SCREEN_META.WIDTH));
            if (y >= SCREEN_META.HEIGHT) { y = (18 * (y % SCREEN_META.HEIGHT)) - 2000; }
            else {y = y * 18;}
            if (createSlots) { addSlot(new Slot(INVENTORY, i, x * 18 + 8, y + 18)); }
            else { slots.get(i).y = y + 18; }
        }
    }

    public void moveSlotRange(int min, int max, int yChange) { for (int i = min; i < max; i++) { slots.get(i).y += yChange; } }
}
