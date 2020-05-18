package org.ngs.bigx.minecraft.gamestate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXConstants;
import org.ngs.bigx.minecraft.client.skills.Skill;
import org.ngs.bigx.minecraft.client.skills.Skill.enumSkillState;
import org.ngs.bigx.minecraft.context.BigxClientContext;


import com.google.gson.Gson;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;


import net.minecraft.entity.player.EntityPlayer;

public class GameSaveManager {

	private Object lockGameSave = new Object();
	private BigxClientContext bigxContext;
	//GamesaveConfigFilename
	public static final String gameSaveConfigRootFolderName = System.getProperty("user.home") + "\\ixercise";

	public static final String gameSaveConfigFolderName = gameSaveConfigRootFolderName + "\\gameconfig";
	public static final String gameSaveConfigFileName = gameSaveConfigFolderName + "\\gamesave.conf";
	
	public static final String gameSaveRootFolderName = System.getProperty("user.home") + "\\ixercise";
	public static final String gameSaveFolderName = gameSaveConfigRootFolderName + "\\gamesave";
	
	public static String serveraccount = "";
	public static String serverpassword = "";
	
	public static final String gameServerListFileName = System.getProperty("user.home") + "\\bigxGameServerList.dat";

//	public static final String serverip = "128.195.54.50";
	public static final String serverip = "192.168.0.151"; //new one
//	public static final String serverip = "localhost";
	public static final int serverportnumber = 2331;

	public static boolean flagEnableChasingQuestClient = false;
	public static boolean flagEnableChasingQuestServer = false;
	public static boolean flagUpdatePlayerLevelClient = false;
	public static boolean flagUpdatePlayerLevelServer = false;

	public static boolean flagEnableFightAndChasingQuestClient = false;
	public static boolean flagEnableFightAndChasingQuestServer = false;
	
	public static boolean flagGameSaveContinue = false;
	
	public static int savedPlayerLevel = 1;
	
	public enum CUSTOMCOMMAND 
	{
		GETGAMESAVES,
		SETGAMESAVES,
	};
	
	public static boolean readGameSaveByUserCaseId(String caseid, EntityPlayer player) throws IOException
	{
		boolean returnValue = false;
		
		File folderCheck = new File(gameSaveRootFolderName);
		
		if(!folderCheck.exists())
		{
			// make the folder
			folderCheck.mkdir();
			return false;
		}
		
		folderCheck = new File(gameSaveFolderName);
		
		if(!folderCheck.exists())
		{
			// make the folder
			folderCheck.mkdir();
			return false;
		}
		
		String fileName = gameSaveFolderName + "\\gamesave_" + caseid + ".sav";
		
		File filedesc = new File(fileName);
		
		if(!filedesc.exists())
		{
			System.out.println("gameSave does not exist for the user[" + caseid + "]");

			Gson gson = new Gson();
			GameSaveJson save = new GameSaveJson();
			
			String parcedSaveObject = gson.toJson(save);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(parcedSaveObject);
			 
			writer.close();
			
			returnValue = false;
		}
		else
		{
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			String saveString = br.readLine();
			
			GameSaveJson gameSaveJson = (new Gson()).fromJson(saveString, GameSaveJson.class);
			
			BigxClientContext context = (BigxClientContext)BigxClientContext.getInstance();
			int i=0;
			
			// UNLOCK SKILLS
			for(i=0; i<3; i++)
			{
				if((gameSaveJson.getSkills() & (0x1 << i)) != 0)
				{
					context.getCurrentGameState().getSkillManager().getSkills().get(i).unlockSkillState();
				}
			}
			
			// READ SAVED PLAYER LEVEL


			
//			QuestTaskChasing.getLevelSystem().getPlayerLevel()
//			save.setPlayerLevel(QuestTaskChasing.getLevelSystem().getPlayerLevel());
			
			br.close();
			
			returnValue = true;
		}
		
		return returnValue;
	}
	


	

	@Deprecated
	public static void updatePlayerLevel()
	{
//		QuestTaskChasing.getLevelSystem().setPlayerLevel(GameSaveManager.savedPlayerLevel);
	}
	


//	public static void registerQuest(CustomQuestAbstract quest)
//	{
////		quests.add(quest);
//	}
	
	public static void loadCustomQuests(String caseid) throws IOException
	{
//		for(CustomQuestAbstract quest : quests)
//		{
//			File folderCheck = new File(gameSaveRootFolderName);
//			
//			if(!folderCheck.exists())
//			{
//				// make the folder
//				folderCheck.mkdir();
//			}
//			
//			folderCheck = new File(gameSaveFolderName);
//			
//			if(!folderCheck.exists())
//			{
//				// make the folder
//				folderCheck.mkdir();
//			}
//			
//			File filedesc = new File(gameSaveFolderName + "\\gamesave_" + caseid + "_" + quest.getName() + ".sav");
//			
//			if(!filedesc.exists())
//			{
//				throw(new IOException("Quest data for user: " + caseid + " and quest: " + quest.getName() + " does not exist yet"));
//			}
//			
//			InputStream in = new FileInputStream(filedesc);
//			byte[] buffer = new byte[1024];
//			in.read(buffer);
//			in.close();
//			
//			String questData = (new String(buffer)).trim();
//			
//			CustomQuestJson questJson = (new Gson()).fromJson(questData, CustomQuestJson.class);
//			
//			
//			quest.loadFromJson(questJson);
//		}
	}

	
	public static void writeGameSaveByUserCaseId(String caseid) throws IOException
	{

		GameSaveJson save = new GameSaveJson();
		


		try
		{
			BigxClientContext context = (BigxClientContext)BigxClientContext.getInstance();
			List<Skill> skills = context.getCurrentGameState().getSkillManager().getSkills();
			int accumulatedSkillState = 0;
			int i=0;
			
			for(i=0; i<skills.size(); i++)
			{
				Skill skill = skills.get(i);
				
				if(skill.getSkillState() != enumSkillState.LOCKED)
				{
					accumulatedSkillState = accumulatedSkillState | (0x1 << i);
				}
			}

			save.setSkills(accumulatedSkillState);
			
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
		
		Gson gson = new Gson();
		String parcedSaveObject = gson.toJson(save);
		
		File folderCheck = new File(gameSaveRootFolderName);
				
		if(!folderCheck.exists())
		{
			// make the folder
			folderCheck.mkdir();
		}
		
		folderCheck = new File(gameSaveFolderName);
		
		if(!folderCheck.exists())
		{
			// make the folder
			folderCheck.mkdir();
		}
		
		String saveFileName = gameSaveFolderName + "\\gamesave_" + caseid + ".sav";
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(saveFileName));
		writer.write(parcedSaveObject);
//		System.out.println("save[" + parcedSaveObject + "]");
		 
		writer.close();
	}
	
	public static GameSaveConfig readGameSaveServerConfigFile() throws IOException
	{
		GameSaveConfig returnValue = null;
		File folderCheck = new File(gameSaveConfigRootFolderName);
		
		if(!folderCheck.exists())
		{
			// make the folder
			folderCheck.mkdir();
		}
		
		folderCheck = new File(gameSaveConfigFolderName);
		
		if(!folderCheck.exists())
		{
			// make the folder
			folderCheck.mkdir();
		}
		
		File filedesc = new File(gameSaveConfigFileName);
		
		if(!filedesc.exists())
		{
			throw(new IOException("gameSaveConfigRootFolderName does not exist"));
		}
		
		InputStream in = new FileInputStream(filedesc);
		byte[] buffer = new byte[1024];
		in.read(buffer);
		in.close();
		
		String gamesaveconfig = (new String(buffer)).trim();
		
		returnValue = (new Gson()).fromJson(gamesaveconfig, GameSaveConfig.class);
		
		return returnValue;
	}
	
	@SuppressWarnings("unused")
	public static void sendCustomCommand(BigxClientContext bigxContext, String host, CUSTOMCOMMAND commandenum) throws IOException
	{
		boolean okayToProceed = true;
		
		if(serveraccount.equals("") || serverpassword.equals(""))
		{
			throw(new IOException("Game Save Configuration File is not read"));
		}
		
	    FileInputStream fis=null;
	    
		try{
		      JSch jsch=new JSch();
		      Session session=jsch.getSession(serveraccount, host, BiGXConstants.SERVERSSHPORTNUM);
		      session.setUserInfo(new UserInfo() {
			
					@Override
					public void showMessage(String arg0) {
					}
					
					@Override
					public boolean promptYesNo(String arg0) {
						return true;
					}
					
					@Override
					public boolean promptPassword(String arg0) {
						if(serverpassword != "")
							return true;
						else
							return false;
					}
					
					@Override
					public boolean promptPassphrase(String arg0) {
						return true;
					}
					
					@Override
					public String getPassword() {
						return serverpassword;
					}
					
					@Override
					public String getPassphrase() {
						return null;
					}
		      });
		      session.setPassword(serverpassword);
		      session.connect();

		      // exec 'scp -t rfile' remotely
		      String command = "";
		      String ip = InetAddress.getLocalHost().getHostAddress();

		      switch(commandenum)
		      {
		      case GETGAMESAVES:
		    	  command="bigx-getgamesave" + serveraccount + " " + serverpassword + " minecraft " + bigxContext.suggestedGameProperties.getPatientCaseId();
		    	  break;
		      case SETGAMESAVES:
		    	  command="bigx-setgamesave " + serveraccount + " " + serverpassword + " minecraft " + bigxContext.suggestedGameProperties.getPatientCaseId();
		    	  break;
		      };
		      
		      Channel channel=session.openChannel("exec");
		      ((ChannelExec)channel).setCommand(command);

		      // get I/O streams for remote scp
		      OutputStream out=channel.getOutputStream();
		      InputStream in=channel.getInputStream();

		      channel.connect();
		      
		      byte[] buffer = new byte[10240];
		      in.read(buffer);
		      String output = (new String(buffer)).trim();
		      
		      
		      switch(commandenum)
		      {
		      case GETGAMESAVES:
		    	  bigxContext.setGameSaveList(new Gson().fromJson(output, GameSaveList.class));
		    	  break;
		      case SETGAMESAVES:
		    	  String gameSaveString = new Gson().toJson(bigxContext.getGameSaveList());
		    	  out.write(gameSaveString.toString().getBytes("US-ASCII"));
		    	  out.write(0);
		    	  out.flush();
		    	  break;
		      };
		      
		      out.close();
		      in.close();
		      
		      channel.disconnect();
		      session.disconnect();
		    }
		    catch(Exception e){
		    	e.printStackTrace();
		    	
			    try{
			    	if(fis != null)
			    		fis.close();
		    	}
			    catch(Exception ee){
			    	ee.printStackTrace();
		    	}
		    }
	}

	public int checkAck(InputStream in) throws IOException{
		int b=in.read();
		// b may be 0 for success,
		//					1 for error,
		//					2 for fatal error,
		//					-1
		if(b==0) return b;
		if(b==-1) return b;

		if(b==1 || b==2){
			StringBuffer sb=new StringBuffer();
			int c;
			do {
				c=in.read();
				sb.append((char)c);
			}
			while(c!='\n');
			if(b==1){ // error
				System.out.print(sb.toString());
			}
			if(b==2){ // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}
	
	public GameSaveManager(BigxClientContext bigxContext)
	{
		this.bigxContext = bigxContext;
	}
	
//	private static void init() {
//		// Initialize timer to remove inactive game server from the list
//		timer = new Timer();
//		timer.scheduleAtFixedRate(new TimerTask() 
//			{
//				@Override
//				public void run() {
//					sendCustomCommand(serverip, CUSTOMCOMMAND.REGISTERGAMESERVER);
//					sendCustomCommand(serverip, CUSTOMCOMMAND.GETGAMESERVERLIST);
//				}
//			},
//		0, timeoutGameServer);
//	}
}
