package org.github.komnazsk.kmpainting;

import org.bukkit.Art;
import org.bukkit.Material;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

/**
 * Listener class when holding a painting and clicking on the painting
 */
public class PaitingClickListener implements Listener {
    private static final int PLAYER_HAND_REACH_SIZE = 5;

    /**
     * Called when a player interactions.
     * <p>
     * When the player right-clicks a painting while holding a painting in his hand,
     * the next numbered painting will be pasted.
     * </p>
     * @param event Player Interact Event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        EquipmentSlot equipmentSlot = event.getHand();

        // For non-hand player interaction events (e.g. off-hand), do nothing
        if (equipmentSlot != EquipmentSlot.HAND) {
            return;
        }

        // If player doesn't have a painting, do nothing
        if (itemInHand.getType() != Material.PAINTING) {
            return;
        }

        Painting clickedPainting = getClickedPainting(player);
        if (clickedPainting != null) {
            switch (event.getAction()) {
                // Tip: Left-click events are not registered.
                // This is because onBlockHangingBreak is called
                // as an event when a painting is destroyed.
                case RIGHT_CLICK_BLOCK:
                    // Fall through
                case RIGHT_CLICK_AIR:
                    // The event will be canceled to prevent the paintings from being torn down.
                    event.setCancelled(true);
                    // If you right-click a painting, replace it with the next painting.
                    // Only place paintings that can be placed.
                    Art startArt = clickedPainting.getArt();
                    Art rightClickArt = ArtCycler.getNext(startArt);
                    while ((rightClickArt != startArt) && !clickedPainting.setArt(rightClickArt)) {
                        rightClickArt = ArtCycler.getNext(rightClickArt);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Called when an entity break the hanging.
     * <p>
     * When the player left-clicks a painting while holding a painting in his hand,
     * the previous numbered painting will be pasted.
     * </p>
     * @param event Hanging Break By Entity Event
     */
    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        Player player;
        // Not applicable if broken by an entity other than the player
        if (event.getRemover() instanceof Player) {
            player = (Player)event.getRemover();
        } else {
            return;
        }

        // If player doesn't have a painting, do nothing
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.PAINTING) {
            return;
        }

        if (event.getEntity() instanceof Painting clickedPainting) {
            // The event will be canceled to prevent the paintings from being torn down.
            event.setCancelled(true);
            // If you left-click on a painting, replace it with the previous painting.
            // Only place paintings that can be placed.
            Art startArt = clickedPainting.getArt();
            Art leftClickArt = ArtCycler.getPrev(startArt);
            while ((leftClickArt != startArt) && !clickedPainting.setArt(leftClickArt)) {
                leftClickArt = ArtCycler.getPrev(leftClickArt);
            }
        }
    }

    /**
     * Get the object of the clicked painting
     *
     * @param player Player object
     * @return Clicked painting object
     */
    private Painting getClickedPainting(Player player) {
        Painting retPainting = null;
        // Raytraces up to 5 blocks in the direction of the player's line of sigh
        RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(
                player.getEyeLocation(), player.getEyeLocation().getDirection(),
                PLAYER_HAND_REACH_SIZE, entity -> entity instanceof Painting);

        // If the painting is found, return the painting object
        if ((rayTraceResult != null) && (rayTraceResult.getHitEntity() instanceof Painting)) {
            retPainting = (Painting) rayTraceResult.getHitEntity();
        }

        return retPainting;
    }
}
