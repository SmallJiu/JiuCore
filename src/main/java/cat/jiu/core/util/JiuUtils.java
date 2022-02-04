package cat.jiu.core.util;

import cat.jiu.core.JiuCore;
import cat.jiu.core.energy.EnergyUtils;
import cat.jiu.core.util.crafting.Recipes;
import cat.jiu.core.util.helpers.DayUtils;
import cat.jiu.core.util.helpers.EntityUtils;
import cat.jiu.core.util.helpers.ItemUtils;
import cat.jiu.core.util.helpers.NBTUtils;
import cat.jiu.core.util.helpers.OtherUtils;

public final class JiuUtils {
	public static final DayUtils day = new DayUtils();
	public static final EntityUtils entity = new EntityUtils();
	public static final ItemUtils item = new ItemUtils();
	public static final OtherUtils other = new OtherUtils();
	public static final Recipes recipe = new Recipes(JiuCore.MODID);
	public static final NBTUtils nbt = new NBTUtils();
	public static final EnergyUtils energy = new EnergyUtils();
}
