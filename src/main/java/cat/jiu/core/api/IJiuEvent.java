package cat.jiu.core.api;

public interface IJiuEvent {
	default Class<IJiuEvent> getSuperEventClass(){
		return IJiuEvent.class;
	}
}
