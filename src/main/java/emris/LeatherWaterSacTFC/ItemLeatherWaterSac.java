/**
 *  Copyright (C) 2013  emris
 *  https://github.com/emris/LeatherWaterSacTFC
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package emris.LeatherWaterSacTFC;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet18Animation;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import TFC.API.ISize;
import TFC.API.Constant.TFCBlockID;
import TFC.API.Enums.EnumItemReach;
import TFC.API.Enums.EnumSize;
import TFC.API.Enums.EnumWeight;
import TFC.Core.TFCTabs;
import TFC.Core.TFC_Core;
import TFC.Core.Player.FoodStatsTFC;
import TFC.Items.ItemTerra;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLeatherWaterSac extends Item implements ISize
{
	public static int itemID;

	public ItemLeatherWaterSac(int par1)
	{
		super(par1);
		this.maxStackSize = 1;
		this.setCreativeTab(TFCTabs.TFCMisc);
		this.setMaxDamage(12);
		this.hasSubtypes = false;
		this.setUnlocalizedName("LeatherWaterSac");
		this.itemID = 256 + par1;
		this.canStack();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister registerer)
	{
		this.itemIcon = registerer.registerIcon("leatherwatersac:LeatherWaterSac");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack sac, World world, EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode)
			sac.setItemDamage(0);

		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
		if (mop == null)
		{
			if(sac.getItemDamage() == sac.getMaxDamage())
			{
				if(player instanceof EntityPlayerMP)
					player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("sac.empty"));
			}
			else
			{
				player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
			}
			return sac;
		}
		else
		{
			if(mop.typeOfHit == EnumMovingObjectType.TILE)
			{
				int x = mop.blockX;
				int y = mop.blockY;
				int z = mop.blockZ;

				if(!world.canMineBlock(player, x,  y, z))
					return sac;

				if(!player.canPlayerEdit(x, y, z, mop.sideHit, sac))
					return sac;

				if(world.getBlockMaterial(x, y, z) == Material.water)
				{
					if(sac.getItemDamage() > 0)
					{
						if (!player.capabilities.isCreativeMode)
						{
							fillSac(world, sac, x, y, z);
							world.spawnParticle("splash", x, y + 2, z, 0.0D, 0.0D, 0.0D);
							world.playSoundAtEntity(player, "random.splash", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
						}
					}
					else
					{
						player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
					}
				}
				else
				{
					if(sac.getItemDamage() == sac.getMaxDamage())
					{
						if(player instanceof EntityPlayerMP)
							player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("sac.empty"));
					}
					else
					{
						player.setItemInUse(sac, this.getMaxItemUseDuration(sac));
					}
				}
			}
			return sac;
		}
	}

	private void fillSac(World world, ItemStack sac, int x, int y, int z)
	{
		int id = world.getBlockId(x, y, z);
		if (id == TFCBlockID.FreshWaterStill || id == TFCBlockID.FreshWaterFlowing ||
			id == TFCBlockID.HotWaterStill || id == TFCBlockID.HotWaterFlowing)
		{
			sac.setItemDamage(sac.getItemDamage() - 6);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("LiquidType", "freshWater");
			sac.setTagCompound(nbt);
		}

		if (id == Block.waterStill.blockID || id == Block.waterMoving.blockID)
		{
			sac.setItemDamage(sac.getItemDamage() - 6);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("LiquidType", "saltWater");
			sac.setTagCompound(nbt);
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack sac)
	{
		return EnumAction.drink;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack sac)
	{
		return 32;
	}

	@Override
	public ItemStack onEaten(ItemStack sac, World world, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			if(sac.getItemDamage() != sac.getMaxDamage() || player.capabilities.isCreativeMode)
			{
				if(player instanceof EntityPlayerMP)
				{
					EntityPlayerMP p = (EntityPlayerMP)player;
					FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(p);
					float nwl = fs.getMaxWater(p);
					int rw = (int)nwl / 6;
					Boolean isThirsty = fs.getMaxWater(p) - 500 > fs.waterLevel;
					String liquidType = "freshWater";
					if (sac.hasTagCompound() && sac.stackTagCompound.hasKey("LiquidType") && sac.stackTagCompound.getString("LiquidType").contentEquals("saltWater"))
						liquidType = "saltWater";

					if (liquidType.contains("freshWater"))
					{
						if (isThirsty)
						{
							fs.restoreWater(p, rw);
							if (!p.capabilities.isCreativeMode)
								sac.setItemDamage(sac.getItemDamage() + 1);
						}
						else
						{
							world.playSoundAtEntity(p, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
							p.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("drink.full"));
						}
					}

					if (liquidType.contains("saltWater"))
					{
						fs.restoreWater(p, -rw);
						if (!p.capabilities.isCreativeMode)
							sac.setItemDamage(sac.getItemDamage() + 1);
						p.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("drink.salt"));
						p.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(p, new Packet18Animation(p, 6));
					}
				}
			}
		}
		return sac;
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		ItemTerra.addSizeInformation(is, arraylist);
	}

	@Override
	public EnumSize getSize(ItemStack is)
	{
		return EnumSize.SMALL;
	}

	@Override
	public EnumWeight getWeight(ItemStack is)
	{
		return EnumWeight.LIGHT;
	}

	@Override
	public boolean canStack()
	{
		return false;
	}

	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return EnumItemReach.SHORT;
	}

}
