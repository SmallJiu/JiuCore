package cat.jiu.core.util;

import cat.jiu.core.capability.EnergyUtils;
import cat.jiu.core.util.helpers.BigIntegerUtils;
import cat.jiu.core.util.helpers.DayUtils;
import cat.jiu.core.util.helpers.EntityUtils;
import cat.jiu.core.util.helpers.ItemNBTUtils;
import cat.jiu.core.util.helpers.ItemUtils;
import cat.jiu.core.util.helpers.JsonUtil;
import cat.jiu.core.util.helpers.OtherUtils;

public final class JiuUtils {
	public static final DayUtils day = new DayUtils();
	public static final EntityUtils entity = new EntityUtils();
	public static final ItemUtils item = new ItemUtils();
	public static final OtherUtils other = new OtherUtils();
	public static final ItemNBTUtils nbt = new ItemNBTUtils();
	public static final EnergyUtils energy = new EnergyUtils();
	public static final BigIntegerUtils big_integer = new BigIntegerUtils();
	public static final JsonUtil json = new JsonUtil();
}
