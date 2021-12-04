package cat.jiu.core.util.base;

import net.minecraft.world.biome.Biome;

public class BaseBiome extends Biome {

	public BaseBiome(String name, float baseHeight, float heightVariation,float temperature, float rainfall, int waterColor) {
		super(new BiomeProperties(name)
				.setBaseHeight(baseHeight)// 基础高度
				.setHeightVariation(heightVariation)// 高度变化
				.setTemperature(temperature)// 温度
				.setRainfall(rainfall)// 降雨量
				.setWaterColor(waterColor)// 水的颜色
		);
		
	}
}
