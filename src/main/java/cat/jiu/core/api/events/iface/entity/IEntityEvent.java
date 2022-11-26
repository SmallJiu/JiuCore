package cat.jiu.core.api.events.iface.entity;

public interface IEntityEvent extends 
	IEntityDeathEvent,
	IEntityPlaceBlock,
	IEntityInVoidEvent,
	IEntityJoinWorldEvent,
	IEntityTickEvent,
	IEntityJump,
	IEntityInFluidEvent
{}
