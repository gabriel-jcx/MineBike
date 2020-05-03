package org.ngs.bigx.minecraft.gamestate;

public class GameSaveConfig {
	private String serveraccount = "";
	private String serverpassword = "";
	private String ipgameserver = "";
	private String ipbike = "";
	
	public String getServeraccount() {
		return serveraccount;
	}
	public void setServeraccount(String serveraccount) {
		this.serveraccount = serveraccount;
	}
	public String getServerpassword() {
		return serverpassword;
	}
	public void setServerpassword(String serverpassword) {
		this.serverpassword = serverpassword;
	}
	public String getIpgameserver() {
		return ipgameserver;
	}
	public void setIpgameserver(String ipgameserver) {
		this.ipgameserver = ipgameserver;
	}
	public String getIpbike() {
		return ipbike;
	}
	public void setIpbike(String ipbike) {
		this.ipbike = ipbike;
	}
}
