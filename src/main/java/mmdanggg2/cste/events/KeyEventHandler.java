package mmdanggg2.cste.events;

import org.lwjgl.input.Keyboard;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTESelectionProcessor;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyEventHandler {

	// named indexes
	public static final int USE_BRUSH = 0;
	public static final int USE_WAND = 1;
	// binding descriptions
	private static final String[] desc = { "cste.keybinding.usebrush", "cste.keybinding.usewand" };
	// binding defaults
	private static final int[] keyValues = { Keyboard.KEY_R, Keyboard.KEY_F };
	private final KeyBinding[] keys;

	public KeyEventHandler() {
		keys = new KeyBinding[desc.length];
		for (int i = 0; i < desc.length; ++i) {
			keys[i] = new KeyBinding(desc[i], keyValues[i],
					"cste.keybinding.category");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}
	
	@SubscribeEvent
	public void onKeyEvent(KeyInputEvent event) {
		if (keys[USE_BRUSH].isPressed()) {
			CSTELogger.logDebug("BRUSH KEY EVENT!!!!");
			Minecraft mc = Minecraft.getMinecraft();
			MovingObjectPosition pos = mc.thePlayer.rayTrace(100, 0);
			if (pos.typeOfHit.equals(MovingObjectType.BLOCK)) {
				CSTELogger.logDebug("Hit Block: " + CSTESelectionProcessor.posToStr(pos.getBlockPos()));
				CSTE.brushProcessor.onBrushActivated(pos.getBlockPos());
			}
		}
		if (keys[USE_WAND].isPressed()) {
			CSTELogger.logDebug("WAND KEY EVENT!!!!");
			Minecraft mc = Minecraft.getMinecraft();
			MovingObjectPosition pos = mc.thePlayer.rayTrace(100, 0);
			if (pos.typeOfHit.equals(MovingObjectType.BLOCK)) {
				CSTELogger.logDebug("Hit Block: " + CSTESelectionProcessor.posToStr(pos.getBlockPos()));
				CSTE.selProcessor.onBlockActivated(pos.getBlockPos());
			}
		}
	}
}
