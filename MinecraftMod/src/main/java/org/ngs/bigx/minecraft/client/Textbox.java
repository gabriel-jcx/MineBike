package org.ngs.bigx.minecraft.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;

public class Textbox {
	private List<String> lines;
	private int width;
	
	public Textbox(int width) {
		lines = new ArrayList<String>();
		this.width = width;
	}
	
	public List<String> getLines() {
		List<String> result = new ArrayList<String>();
		result.addAll(lines);
		return result;
	}
	
	public void addLine(String line,FontRenderer font) {
		if (line.equals("")) {
			lines.add(line);
			return;
		}
		if (font.getStringWidth(line)<=width) {
			lines.add(line);
			return;
		}
		String l = "";
		for (int i=0;i<line.length();i++) {
			l+=line.charAt(i);
			if (font.getStringWidth(l)>=width) {
				lines.add(l);
				l = "";
			}
		}
		lines.add(l);
	}
}
