package cat.jiu.core.test;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.api.events.player.IPlayerJump;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemDebug extends BaseItem.Normal{
	public ItemDebug() {
		super(JiuCore.MODID, "debug", CreativeTabs.TOOLS, false);
		Init.ITEMS.add(this);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		for(Class<?> clazz : IPlayerJump.class.getInterfaces()) {
			JiuUtils.entity.sendMessage(playerIn, clazz.toString());
		}
		
		JiuUtils.entity.sendMessage(playerIn, (IPlayerJump.class.getInterfaces()[0] == IJiuEvent.class) + "");
//		JiuCore.instance.log.info(JiuUtils.item.getTexture(playerIn.getHeldItemMainhand()).toString());
//		JsonParser parser = new JsonParser();
//		
//		try {
//			JsonObject array = parser.parse(new FileReader("./config/jiu/core/test/cb.json")).getAsJsonObject();
//			for(Map.Entry<String, JsonElement> jobj : array.entrySet()) {
//				
//				JsonArray arr = (JsonArray) jobj.getValue();// 主清单
//				for(int i = 0; i < arr.size(); ++i) {
//					JsonObject obj = (JsonObject) arr.get(i);// 子清单
//					
//					JiuUtils.entity.sendClientMessage(playerIn, "============");
//					
//					JiuUtils.entity.sendClientMessage(playerIn, obj.get("block").getAsString());
//					
//					JsonArray metas = obj.getAsJsonArray("meta");// 子子清单
//					for(int meta = 0; meta < metas.size(); ++meta) {
//						JiuUtils.entity.sendClientMessage(playerIn, Integer.toString(meta));
//					}
//					
//					JiuUtils.entity.sendClientMessage(playerIn, obj.get("time").getAsString());
//					
//					if(obj.has("drops")) {
//						for(Map.Entry<String, JsonElement> dropobj : obj.getAsJsonObject("drops").entrySet()) {
//							JsonElement drop = dropobj.getValue();
//							
//							if(drop instanceof JsonObject) {
//								JsonObject drop0 = (JsonObject) drop;
//								
//								JsonArray dropitems = (JsonArray) drop0.get("item");// 子子子子清单
//								for(int item = 0; item < dropitems.size(); ++item) {
//									JiuUtils.entity.sendClientMessage(playerIn, dropitems.get(item).getAsString());
//								}
//							}else {
//								JiuUtils.entity.sendClientMessage(playerIn, drop.getAsString());
//							}
//						}
//					}else {
//						JiuUtils.entity.sendClientMessage(playerIn, obj.get("canDrop").getAsBoolean()+"");
//						
//						if(!obj.get("canDrop").getAsBoolean()) {
//							JiuUtils.entity.sendClientMessage(playerIn, obj.get("changed").getAsString());
//						}
//					}
//				}
//			}
//		} catch(Exception e) {
//			JiuCore.instance.log.error(e.getMessage());
//			e.printStackTrace();
//		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
