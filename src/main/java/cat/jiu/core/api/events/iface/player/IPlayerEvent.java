package cat.jiu.core.api.events.iface.player;

public interface IPlayerEvent extends 
	IPlayerPlaceFluid,
	IPlayerLoggedOutEvent,
	IPlayerLoggedInEvent,
	IPlayerInVoidEvent,
	IPlayerBreakBlock,
	IPlayerJump,
	IPlayerTickEvent,
	IPlayerPlaceBlock,
	IPlayerInFluidEvent,
	IPlayerDeathEvent,
	IPlayerJoinWorldEvent
{}
