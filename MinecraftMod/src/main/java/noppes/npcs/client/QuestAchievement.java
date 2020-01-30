package noppes.npcs.client;

import net.minecraft.init.Items;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import noppes.npcs.CustomItems;

public class QuestAchievement extends Achievement {

   private String description;
   private String message;


   public QuestAchievement(String message, String description) {
      super("", message, 0, 0, CustomItems.letter == null?Items.paper:CustomItems.letter, (Achievement)null);
      this.description = description;
      this.message = message;
   }

   public IChatComponent getStatName() {
      return new ChatComponentText(this.message);
   }

   public String getDescription() {
      return this.description;
   }
}
