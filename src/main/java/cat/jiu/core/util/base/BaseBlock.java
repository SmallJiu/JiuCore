package cat.jiu.core.util.base;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cat.jiu.core.CoreLoggers;
import cat.jiu.core.api.IHasModel;
import cat.jiu.core.api.IMetaName;
import cat.jiu.core.api.ISubBlockSerializable;
import cat.jiu.core.types.StackCaches;
import cat.jiu.core.util.RegisterModel;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseBlock {
	public static abstract class Base extends Block {
		protected final String modid;
		protected final String name;
		protected final CreativeTabs tab;
		private final boolean hasSubtypes;
		
		public Base(String modid, String name, Material materialIn, SoundType soundType, CreativeTabs tab, float hardness, boolean hasSubType) {
			super(materialIn);
			this.modid = modid;
			this.name = name;
			this.tab = tab;
			this.hasSubtypes = hasSubType;
			this.setSoundType(soundType);
			this.setTranslationKey(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
			if(hardness < 0) {
				this.setHardness(Integer.MAX_VALUE);
				this.setExplosionResistance(Integer.MAX_VALUE);
			}else {
				this.setHardness(hardness);
				this.setExplosionResistance(hardness * 5.0F);
			}
			
			ForgeRegistries.BLOCKS.register(this);
			ItemBlock ib = this.getRegisterItemBlock();
			if(hasSubType) {
				ib.setHasSubtypes(true);
			}
			ForgeRegistries.ITEMS.register(ib.setRegistryName(this.getRegistryName()));
		}
		
		/**
		 * No need to setRegistryName and setHasSubtypes, JiuCore will help you set
		 */
		@Nonnull
		public abstract ItemBlock getRegisterItemBlock();
		
		boolean isOpaqueCube = false;
		
		public Base setIsOpaqueCube() {
			this.isOpaqueCube = !this.isOpaqueCube;
			return this;
		}
		
		@SideOnly(Side.CLIENT)
		@Override
		public boolean isOpaqueCube(IBlockState state) {
			return this.isOpaqueCube;
		}
		
		private boolean isBeaconBase = false;
		
		public Base isBeaconBase() {
			this.isBeaconBase = !this.isBeaconBase;
			return this;
		}
		
		@Override
		public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
			return this.isBeaconBase;
		}
		
		private boolean isNormalCube = false;
		
		public Base isNormalCube() {
			this.isNormalCube = !this.isNormalCube;
			return this;
		}
		
		@Override
		public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
			return this.isNormalCube;
		}
		
		private int lightValue = 0;
		
		public Base setLightValue(int value) {
			this.lightValue = (int)(15.0F * value);
			return this;
		}
		
		@Override
		public int getLightValue(IBlockState state) {
			return this.lightValue;
		}
		
		@Override
		public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
			return this.lightValue;
		}
		
		public Base setLightOpacity(int opacity) {
	        this.lightOpacity = opacity;
	        return this;
	    }
		
		@Override
		public int getLightOpacity(IBlockState state) {
			return this.lightOpacity;
		}
		
		@Override
		public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
			return this.lightOpacity;
		}
		
		private int weakPower = 0;
		
		public Base setWeakPower(int i) {
			this.weakPower = i;
			return this;
		}
		
		@Override
		public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
			return this.weakPower;
		}
		
		private float explosionResistance = 0F;
		
		public Base setExplosionResistance(float i) {
			this.explosionResistance = i;
			return this;
		}
		
		@Override
		public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
			return this.explosionResistance;
		}
		
		NonNullList<ItemStack> drops = null;
		
		public Base setDrops(NonNullList<ItemStack> drops) {
			this.drops = drops;
			return this;
		}
		
		@Override
		public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
			if(this.drops != null) {
				drops = this.drops;
			}
			super.getDrops(drops, world, pos, state, fortune);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public BlockRenderLayer getRenderLayer() {
			return BlockRenderLayer.TRANSLUCENT;
		}
		
		public boolean getHasSubtypes() {
			return this.hasSubtypes;
		}
	}
	
	public static abstract class Air extends Base implements IHasModel {
		public Air(String modid, String name, Material materialIn, SoundType soundType, CreativeTabs tab, float hardness, boolean hasSubType) {
			super(modid, name, materialIn, soundType, tab, hardness, hasSubType);
		}
		
	    public EnumBlockRenderType getRenderType(IBlockState state) {return EnumBlockRenderType.INVISIBLE;}
	    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {return NULL_AABB;}
	    public boolean isOpaqueCube(IBlockState state) {return false;}
	    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) { return false;}
	    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
	    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {return false;}
	    public boolean isFullCube(IBlockState state) {return false;}
	    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {return BlockFaceShape.UNDEFINED;}
	    public ItemBlock getRegisterItemBlock() {return new ItemBlock(this);}
	}
	
	public static abstract class Normal extends Base implements IHasModel {
		@Deprecated
		protected final RegisterModel model = new RegisterModel(this.modid);
		
		public Normal(String modid, String nameIn,  Material materialIn, SoundType soundIn, CreativeTabs tabIn, float hardnessIn) {
			super(modid, nameIn, materialIn, soundIn, tabIn, hardnessIn, false);
			RegisterModel.addNeedRegistryModel(modid, this);
		}
		
		public Normal(String modid, String nameIn, Material materialIn, SoundType soundIn, CreativeTabs tabIn) {
			this(modid, nameIn,  materialIn, soundIn, tabIn, 10);
		}
		
		public Normal(String modid, String nameIn, Material materialIn,  CreativeTabs tabIn) {
			this(modid, nameIn, materialIn, SoundType.STONE, tabIn);
		}
		
		public Normal(String modid, String nameIn, SoundType soundIn, CreativeTabs tabIn) {
			this(modid, nameIn, Material.IRON, soundIn, tabIn);
		}
		
		public Normal(String modid, String nameIn, CreativeTabs tabIn) {
			this(modid, nameIn, Material.IRON, SoundType.STONE, tabIn);
		}
		
		private String[] model_res = null;
		
		public BaseBlock.Normal setBlockModelResourceLocation(String name, String resname) {
			this.model_res = new String[] { name, resname };
			return this;
		}
		
		/**
		 * if you add more BlockState, you must override {@link #getMetaFromState(IBlockState)} and {@link #getStateFromMeta(int)}}
		 */
		protected IProperty<?>[] addBlockOthersProperty() {
			return null;
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		protected BlockStateContainer createBlockState() {
			ArrayList<IProperty> pros = new ArrayList<>();
			
			if(this.addBlockOthersProperty() != null) {
				if(this.addBlockOthersProperty().length != 0) {
					for(IProperty pro : this.addBlockOthersProperty()) {
						pros.add(pro);
					}
				}
			}
			return new BlockStateContainer(this, pros.toArray(new IProperty[0]));
		}
		
		@Override
		public int getMetaFromState(IBlockState state) {
			return 0;
		}
		
		@SideOnly(Side.CLIENT)
		@Override
		public void getItemModel(RegisterModel util) {
			util.setBlockStateMapper(this, true, "normal/"+this.name);
			if(this.model_res != null) {
				util.registerItemModel(this, this.model_res[0], this.model_res[1]);
			}else {
				util.registerItemModel(this, "block/normal/", this.name);
			}
		}
		
		public String[] getBlockModelResourceLocation() {
			return this.model_res;	
		}
	}
	
	public static abstract class Sub<T extends Comparable<T> & ISubBlockSerializable> extends Base implements IHasModel, IMetaName{
		private final StackCaches stackCaches;
		public Sub(String modid, String nameIn, Material materialIn, SoundType soundIn, CreativeTabs tabIn, float hardnessIn) {
			super(modid, nameIn, materialIn, soundIn, tabIn, hardnessIn, true);
			this.stackCaches = new StackCaches(this, this.getPropertyArray().length);
			
			RegisterModel.addNeedRegistryModel(modid, this);
			this.setDefaultState(this.blockState.getBaseState().withProperty(this.getBlockStateProperty(), this.getPropertyArray()[0]));
		}
		
		private String[] model_res = null;
		public BaseBlock.Sub<T> setBlockModelResourceLocation(String name) {
			this.model_res = new String[] { name};
			return this;
		}
		
		public final StackCaches getStackCaches() {
			return stackCaches;
		}
		
		@Override
		public int damageDropped(IBlockState state) {
			return this.getMetaFromState(state);
		}
		
		/**
		 * @return block PropertyEnum
		 * 
		 * @author small_jiu
		 */
		protected abstract IProperty<T> getBlockStateProperty();
		
		protected IProperty<?>[] addBlockOtherPropertys() {
			return null;
		}
		
		private T[] propertyArray = null;
		// if you property is not enum, you must override this method
		protected T[] getPropertyArray() {
			if(this.propertyArray==null) {
				if(Enum.class.isAssignableFrom(this.getBlockStateProperty().getValueClass())) {
					this.propertyArray = this.getBlockStateProperty().getValueClass().getEnumConstants();
				}else {
					CoreLoggers.getLogOS().warning("property is not enum, it will CRASH the game! block: '{}'  property class: {}", this.getClass(), this.getBlockStateProperty().getValueClass());
				}
			}
			return this.propertyArray;
		}
		
		@Override
		public int getMetaFromState(IBlockState state) {
			return state.getValue((IProperty<T>) this.getBlockStateProperty()).getMeta();
		}
		
		@Override
		public IBlockState getStateFromMeta(int meta) {
			return this.getDefaultState().withProperty(this.getBlockStateProperty(), this.getPropertyArray()[meta]);
		}
		
		@Override
		public String getName(ItemStack stack) {
			return this.getPropertyArray()[stack.getMetadata()].getName();
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			ArrayList<IProperty<?>> pros = new ArrayList<IProperty<?>>();
			
			pros.add(this.getBlockStateProperty());
			if(this.addBlockOtherPropertys() != null && this.addBlockOtherPropertys().length != 0) {
				for(IProperty<?> pro : this.addBlockOtherPropertys()) {
					if(pro!=null) pros.add(pro);
				}
			}
			return new BlockStateContainer(this, pros.toArray(new IProperty[0]));
		}
		protected boolean useStateName;
		public BaseBlock.Sub<T> useStateNameToSetItemStackModel() {
			this.useStateName = true;
			return this;
		}
		
		@Override
		public void getItemModel(RegisterModel util) {
			util.setBlockStateMapper(this, false, "sub/"+this.name);
			if(this.model_res != null) {
				for(T t : this.getPropertyArray()) {
					util.registerItemModel(this, t.getMeta(), this.model_res[0], this.name + "." + (this.useStateName ? t.getName() : t.getMeta()));
				}
			}else {
				for(T t : this.getPropertyArray()) {
					util.registerItemModel(this, t.getMeta(), "block/sub/" + this.name, this.name + "." + (this.useStateName ? t.getName() : t.getMeta()));
				}
			}
		}
		
		@Override
		public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
			if(this.getHasSubtypes()){
				for(int i = 0; i < this.getPropertyArray().length; ++i) {
					items.add(new ItemStack(this, 1, i));
				}
			}else{
				super.getSubBlocks(tab, items);
			}
		}
	}
	
	public static class BaseFluid extends Fluid {
		protected final String name;
		protected final String modid;
		public BaseFluid(String fluidName, String modid){
			super(fluidName, new ResourceLocation(modid, "blocks/fluid/" + fluidName + "_still"), new ResourceLocation(modid, "blocks/fluid/" + fluidName + "_flow"));
			this.name = fluidName;
			this.modid = modid;
			FluidRegistry.registerFluid(this);
			FluidRegistry.addBucketForFluid(this);
		}
		
		public BaseFluid(String fluidName, String modid, Color color){
			super(fluidName, new ResourceLocation(modid, "blocks/fluid/" + fluidName + "_still"), new ResourceLocation(modid, "blocks/fluid/" + fluidName + "_flow"), color);
			this.name = fluidName;
			this.modid = modid;
			FluidRegistry.registerFluid(this);
			FluidRegistry.addBucketForFluid(this);
	    }
		
	    public BaseFluid(String fluidName, String modid, @Nullable ResourceLocation overlay) {
	    	super(fluidName, new ResourceLocation(modid, "blocks/fluid/" + fluidName + "_still"), new ResourceLocation(modid, "blocks/fluid/" + fluidName + "_flow"), overlay);
	    	this.name = fluidName;
			this.modid = modid;
			FluidRegistry.registerFluid(this);
			FluidRegistry.addBucketForFluid(this);
	    }
	}
	
	@SuppressWarnings("unused")
	public static class FluidBlock extends BlockFluidClassic implements IHasModel {
		private final String modid;
		private final String name;
		public FluidBlock(Fluid fluid, String modid, String name, CreativeTabs tab, Material material, List<Block> BLOCKS) {
	        this(fluid, modid, name, tab, material, material == null ? Material.WATER.getMaterialMapColor() : material.getMaterialMapColor(), BLOCKS);
	    }
		
	    public FluidBlock(Fluid fluid, String modid, String name, CreativeTabs tab, Material material, MapColor mapColor, List<Block> BLOCKS) {
	    	super(fluid, material == null ? Material.WATER : material, mapColor == null ? Material.WATER.getMaterialMapColor() : mapColor);
	    	RegisterModel.addNeedRegistryModel(modid, this);
	    	this.modid = modid;
	    	this.name = name;
	    	this.setTranslationKey(modid + "." + name);
	    	if(tab != null) {
	    		this.setCreativeTab(tab);
	    	}
	    	if(BLOCKS != null) {
				BLOCKS.add(this);
			}
			this.setRegistryName(modid, name);
			ForgeRegistries.BLOCKS.register(this);
			ForgeRegistries.ITEMS.register(new BaseBlockItem(this) {
				@SideOnly(Side.CLIENT)
				@Override
				public String getItemStackDisplayName(ItemStack stack) {
					return I18n.format(fluid.getUnlocalizedName());
				}
			}.setRegistryName(modid, fluid.getName()));
	    }
	    
	    @Override
	    public void getItemModel(RegisterModel util) {
	    	util.registerFluidModel(this, "fluid/fluid_");
	    }
	}
	
	public static class BaseBlockItem extends ItemBlock {
		public BaseBlockItem(Block block) {
			this(block, false);
		}
		public BaseBlockItem(Block block, boolean hasSubtypes) {
			super(block);
			this.setHasSubtypes(hasSubtypes);
			this.setMaxDamage(0);
		}
		
		@Override
		public int getMetadata(int damage) {
			return damage;
		}
		
		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack stack) {
			ResourceLocation res = this.block.getRegistryName();
			if(this.hasSubtypes) {
				return I18n.format("tile." + res.getNamespace() + "." + res.getPath()  + "." + stack.getItemDamage() + ".name");
			}else {
				return I18n.format("tile." + res.getNamespace() + "." + res.getPath() + ".name");
			}
		}
	}
}
