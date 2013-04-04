package emris.mods.LeatherWaterSacTFC;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import TFC.TFCItems;
import TFC.Core.Recipes;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="LeatherWaterSac", name="Leather Water Sac", version="1.3.b76", dependencies = "after:TerraFirmaCraft")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, versionBounds = "[1.3.b76]")
public class LeatherWaterSac {
	@Instance("LeatherWaterSac")
	public static LeatherWaterSac instance;
	
	public final static Item itemLeatherWaterSac = new ItemLeatherWaterSac(5252);

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {	}
	
	@Init
	public void load(FMLInitializationEvent event) {
		ItemStack lwSac = new ItemStack(this.itemLeatherWaterSac, 1, this.itemLeatherWaterSac.getMaxDamage());
		ItemStack leather = new ItemStack(TFCItems.PrepHide, 1);
		ItemStack string = new ItemStack(Item.silk, 1);
		
		Item[] tfcKnives = Recipes.Knives;
		for(int j = 0; j < tfcKnives.length; j++) {
			GameRegistry.addShapelessRecipe(lwSac, leather, string, new ItemStack(tfcKnives[j], 1, 32767));
		}
		
		GameRegistry.registerCraftingHandler(new CraftingHandler());
	}
	
}
