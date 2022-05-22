package cat.jiu.core.api.events.game;

public interface IWorldEvent extends
	IOreGeneratePre,	
	IOreGenerate,
	IOreGeneratePost,
	IFluidPlaceBlock,
	IFluidCreateSourceFluid
{}
