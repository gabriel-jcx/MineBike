package org.ngs.bigx.minecraft.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

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
		//word wrap
		int i=0,j=0,k=0;
		while (i<line.length()) {
			while (i<line.length()) {
				while(i<line.length()&&line.charAt(i)!=' ') {
					i++;
				}
				if (font.getStringWidth(line.substring(j,i))>width) {
					i = k;
					break;
				}
				i++;
				k = i;
			}
			lines.add(line.substring(j,Math.min(i,line.length())));
			j = i;
		}
		//lines.add(line.substring(j,line.length()));
	}
}
