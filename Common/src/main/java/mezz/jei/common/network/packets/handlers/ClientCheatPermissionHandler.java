package mezz.jei.common.network.packets.handlers;

import mezz.jei.common.Internal;
import mezz.jei.common.network.ClientPacketContext;
import mezz.jei.common.util.ChatUtil;
import mezz.jei.common.config.IServerConfig;
import mezz.jei.common.config.IClientToggleState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-side-only functions related to cheat permissions
 */
public class ClientCheatPermissionHandler {
	public static void handleHasCheatPermission(ClientPacketContext context, boolean hasPermission) {
		if (!hasPermission) {
			LocalPlayer player = context.player();
			ChatUtil.writeChatMessage(player, "jei.chat.error.no.cheat.permission.1", ChatFormatting.RED);

			IServerConfig serverConfig = context.serverConfig();
			List<String> allowedCheatingMethods = new ArrayList<>();
			if (serverConfig.isCheatModeEnabledForOp()) {
				allowedCheatingMethods.add("jei.chat.error.no.cheat.permission.op");
			}
			if (serverConfig.isCheatModeEnabledForCreative()) {
				allowedCheatingMethods.add("jei.chat.error.no.cheat.permission.creative");
			}
			if (serverConfig.isCheatModeEnabledForGive()) {
				allowedCheatingMethods.add("jei.chat.error.no.cheat.permission.give");
			}

			if (allowedCheatingMethods.isEmpty()) {
				ChatUtil.writeChatMessage(player, "jei.chat.error.no.cheat.permission.disabled", ChatFormatting.RED);
			} else {
				ChatUtil.writeChatMessage(player, "jei.chat.error.no.cheat.permission.enabled", ChatFormatting.RED);
				for (String allowedCheatingMethod : allowedCheatingMethods) {
					ChatUtil.writeChatMessage(player, allowedCheatingMethod, ChatFormatting.RED);
				}
			}

			IClientToggleState toggleState = Internal.getClientToggleState();
			toggleState.setCheatItemsEnabled(false);
			player.closeContainer();
		}
	}
}
