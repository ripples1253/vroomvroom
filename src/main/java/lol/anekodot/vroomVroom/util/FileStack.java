package lol.anekodot.vroomVroom.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@AllArgsConstructor
public class FileStack {
    private ItemStack stack;
    private int count;
}
