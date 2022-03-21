package cat.jiu.core.test;

import cat.jiu.core.network.messages.MessageString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TestMessageEnergy extends MessageString {
	@Override
	public IMessage handle(MessageContext ctx) {
		return null;
	}

	@Override
	public boolean isClientMessage() {
		return false;
	}
}
