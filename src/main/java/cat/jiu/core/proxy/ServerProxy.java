package cat.jiu.core.proxy;

import cat.jiu.core.api.IProxy;

public class ServerProxy implements IProxy<ServerProxy, ClientProxy>{
	public ClientProxy getAsClientProxy() {return null;}
	public ServerProxy getAsServerProxy() {return this;}
}
