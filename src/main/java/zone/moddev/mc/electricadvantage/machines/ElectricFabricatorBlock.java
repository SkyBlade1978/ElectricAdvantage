package zone.moddev.mc.electricadvantage.machines;

import net.minecraft.world.World;

public class ElectricFabricatorBlock extends ElectricMachineBlock{

	@Override
	public ElectricMachineTileEntity createNewTileEntity(World w, int m) {
		return new ElectricFabricatorTileEntity();
	}

}
