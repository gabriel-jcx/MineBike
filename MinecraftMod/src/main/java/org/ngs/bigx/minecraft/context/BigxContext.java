package org.ngs.bigx.minecraft.context;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.ngs.bigx.dictionary.objects.game.BiGXSuggestedGameProperties;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.quests.QuestManager;

public class BigxContext {
	protected QuestManager QuestManagerClient;
	protected BiGX main = null;
	
	public BiGXSuggestedGameProperties suggestedGameProperties = null;
	public boolean suggestedGamePropertiesReady = false;

	public static BigxContext clientSelf = null;
	public static BigxContext serverSelf = null;
	
	public static enum LOGTYPE {LOCATION, TAG, };
	
	protected QuestManager questManager = null;
	
	public BigxContext(BiGX main)
	{
		this.main = main;
	}

	public void setQuestManager(QuestManager questManager)
	{
		this.questManager = questManager;
	}
	
	public QuestManager getQuestManager()
	{
		return this.questManager;
	}

	public boolean isSuggestedGamePropertiesReady() {
		return suggestedGamePropertiesReady;
	}
	
	public static void logWriter(LOGTYPE logtype, String data)
	{
		String text = "" + getTimeString() + "\t" + logtype + "\t" + data;
		
		try {
			writeDataToFile(makeFileName(), text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String makeFileName() {
		DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH");
		Date today = Calendar.getInstance().getTime();
		return df.format(today) + ".log";
	}

	private static String getTimeString() {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd\tHH:mm:ss\t");
		Date today = Calendar.getInstance().getTime();
		return df.format(today) + System.currentTimeMillis();
	}

	private static void writeDataToFile(String fileName, String text) throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
	    writer.append(text + "\n");
	    writer.close();
	}
}
