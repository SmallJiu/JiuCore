package cat.jiu.core.types;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class StackCaches {
	protected final Map<Integer, ItemStack> levels = Maps.newHashMap();
	protected final Item item;
	public final int maxMeta;
	public StackCaches(Block block, int maxMeta) {
		this(Item.getItemFromBlock(block), maxMeta);
	}
	public StackCaches(Item item, int maxMeta) {
		this.item = item;
		this.maxMeta = maxMeta;
		for(int meta = 0; meta < maxMeta; meta++) {
			this.levels.put(meta, new ItemStack(item, 1, meta));
		}
	}
	public void put(int meta) {
		if(!this.levels.containsKey(meta)) {
			this.levels.put(meta, new ItemStack(this.item, 1, meta));
		}
	}
	public ItemStack get(int meta) {
		return this.levels.get(meta);
	}
	public boolean has(int meta) {return this.levels.containsKey(meta);}
	public void remove(int meta) {this.levels.remove(meta);}
}
