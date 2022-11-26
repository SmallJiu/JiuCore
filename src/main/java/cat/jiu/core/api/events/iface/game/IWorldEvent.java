package cat.jiu.core.api.events.iface.game;

public interface IWorldEvent extends
	IOreGeneratePre,	
	IOreGenerate,
	IOreGeneratePost,
	IFluidPlaceBlock,
	IFluidCreateSourceFluid
{}
