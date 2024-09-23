package lol.anekodot.vroomVroom.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Flag;
import com.jonahseguin.drink.annotation.Sender;
import lol.anekodot.vroomVroom.Constants;
import lol.anekodot.vroomVroom.Developer;
import lol.anekodot.vroomVroom.util.FileStack;
import lol.anekodot.vroomVroom.util.Messages;
import lol.anekodot.vroomVroom.util.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

public class ShulkerStoreCommand {
    private SortedSet<FileStack> parts;
    private String tmpFileName = "";

    @Command(name = "createparts",
            desc = "Create parts from file URI",
            usage = "<fileURI> [-f (force download)]")
    public void createFileStacks(@Sender Player sender, String fileURI, @Flag('f') boolean force) throws IOException {
        String[] uriSplit = fileURI.split("/");
        String fileName = uriSplit[uriSplit.length - 1];
        File tempFile = new File(Constants.buildTempFilePath(fileName));

        this.tmpFileName = fileName;

        if (!fileURI.startsWith("https") && !force) {
            sender.sendMessage(Messages.NO_HTTPS.get());
            return;
        }

        if (this.parts == null) this.parts = new TreeSet<>(Comparator.comparing(FileStack::getCount));
        this.parts.clear(); // ensure parts is clear so we can just do this without restarting constantly.

        /*
         here's where the fun starts :)
         FOLLOW MEEEE, I'M STANDING ON THE BORDER OF EVERYTHINGGGGGGG
         LISTEN CLOSEEEE
         CAN YOU HEAR THE SPIRITS SINGGGGG
         LOST MESSAGES OF LONGGGG AGOOOOOO
        */

        FileUtils.copyURLToFile(new URL(fileURI), tempFile);

        String file = StringUtil.bytesToHex(FileUtils.readFileToByteArray(tempFile));
        String[] fileData = file
                .split("(?<=\\G.{65535})");

        tempFile.delete();

        int count = 0;

        for (String data : fileData) {
            ItemStack stack = new ItemStack(Constants.PART_MATERIAL, 1, (short) 100);
            ItemMeta meta = stack.getItemMeta();

            meta.displayName(Component.text(fileName + " part " + count).color(TextColor.fromHexString("#ff8f3b")));

            List<Component> lore = new ArrayList<>();

            lore.add(Component.text(data));

            meta.lore(lore);

            stack.setItemMeta(meta);
            stack.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

            parts.add(new FileStack(stack, count));
            count++;

            Developer.log("Created %s part %s.", fileName, count);
        }

        sender.sendMessage(Messages.SS_WARNING.get("Created " + parts.size() + " parts."));
        sender.sendMessage(Messages.SS_INFO.get("FileStacks created! Use <b>/shulkerstore stacks</b> to start getting them!"));
    }

    @Command(name = "stacks",
            desc = "Get the stacks.")
    public synchronized void stackSplit(@Sender Player sender) { // why
        Iterator<FileStack> stacks = parts.iterator();
        SortedSet<FileStack> shulkers = new TreeSet<>(Comparator.comparing(FileStack::getCount));
        ItemStack currentShulkerStack = null;
        ShulkerBox currentShulker = null;
        int currentChunk = 1;

        while (stacks.hasNext()) {
            if (currentShulker == null) {
                currentShulkerStack = new ItemStack(Constants.CHUNK_MATERIAL);

                BlockStateMeta im = (BlockStateMeta) currentShulkerStack.getItemMeta();
                currentShulker = (ShulkerBox) im.getBlockState();
            }

            if (sender.getInventory().firstEmpty() == -1) break;

            if (currentShulker.getInventory().firstEmpty() == -1) {
                BlockStateMeta meta = ((BlockStateMeta) currentShulkerStack.getItemMeta());
                meta.setBlockState(currentShulker);
                meta.itemName(Component.text(tmpFileName + ": Chunk " + currentChunk));
                meta.displayName(Component.text(tmpFileName + ": Chunk " + currentChunk));
                currentShulkerStack.setItemMeta(meta);

                shulkers.add(new FileStack(currentShulkerStack, currentChunk));

                currentShulkerStack = new ItemStack(Constants.CHUNK_MATERIAL);
                BlockStateMeta im = (BlockStateMeta) currentShulkerStack.getItemMeta();
                currentShulker = (ShulkerBox) im.getBlockState();

                currentChunk++;
            }

            currentShulker.getInventory().addItem(stacks.next().getStack());

            Developer.log("Processed chunk %d.", currentChunk);

            stacks.remove();

            if (!stacks.hasNext()) {
                BlockStateMeta meta = ((BlockStateMeta) currentShulkerStack.getItemMeta());
                meta.setBlockState(currentShulker);
                meta.itemName(Component.text(tmpFileName + ": Chunk " + currentChunk));
                meta.displayName(Component.text(tmpFileName + ": Chunk " + currentChunk));
                currentShulkerStack.setItemMeta(meta);

                shulkers.add(new FileStack(currentShulkerStack, currentChunk));
            }
        }

        for (FileStack stack : shulkers) sender.getInventory().addItem(stack.getStack());

        sender.sendMessage(Messages.SS_INFO.get((parts.isEmpty() ? "Finished!" : "There's still more to go!")));
    }

    @Command(name = "read",
            desc = "Read from inv",
            usage = "<filename>")
    public synchronized void read(@Sender Player sender, String fileName) {
        SortedSet<FileStack> shulkers = new TreeSet<>(Comparator.comparing(FileStack::getCount)); // yes, I'm reusing this class. get fucked.
        SortedSet<FileStack> parts = new TreeSet<>(Comparator.comparing(FileStack::getCount)); // yes, I'm reusing this class. get fucked.

        for (ItemStack shulker : sender.getInventory().all(Constants.CHUNK_MATERIAL).values()) {
            String[] nameSplit = shulker.getItemMeta().getDisplayName().split(" ");
            int part = Integer.parseInt(nameSplit[nameSplit.length - 1]);

            shulkers.add(new FileStack(shulker, part));
        }

        sender.sendMessage(Messages.SS_WARNING.get("Loaded " + shulkers.size() + " chunks. Attempting part read!"));

        Iterator<FileStack> stacks = shulkers.iterator();

        while (stacks.hasNext()) {
            FileStack stack = stacks.next();
            sender.sendMessage(Messages.SS_WARNING.get("Reading chunk " + stack.getCount() + "..."));

            BlockStateMeta im = (BlockStateMeta) stack.getStack().getItemMeta();
            ShulkerBox shulker = (ShulkerBox) im.getBlockState();

            for (ItemStack part : shulker.getInventory().all(Constants.PART_MATERIAL).values()) {
                String[] nameSplit = part.getItemMeta().getDisplayName().split(" ");
                int partNumber = Integer.parseInt(nameSplit[nameSplit.length - 1]);

                parts.add(new FileStack(part, partNumber));
            }

            stacks.remove();
        }

        sender.sendMessage(Messages.SS_SUCCESS.get("Successfully read " + parts.size() + " parts. Attempting data read!"));

        byte[] readData = readData(parts, sender);

        if (readData == null) {
            sender.sendMessage(Messages.SS_ERROR.get("Unable to read data! Quitting!"));
            return;
        }

        sender.sendMessage(Messages.SS_SUCCESS.get("Successfully read " + readData.length + 1 + " bytes from file shulkers."));

        try {
            saveFile(readData, fileName);
        } catch (IOException e) {
            sender.sendMessage(Messages.SS_ERROR.get("Unable to save file!! This is NOT a RamBus Go Vroom Vroom moment!!!"));
            return;
        }

        sender.sendMessage(Messages.SS_SUCCESS.get("Saved file to temporary directory."));
        sender.sendMessage(Messages.SS_SUCCESS.get("Data read successful!"));
        sender.sendMessage(Messages.SS_SUCCESS.get("It's the leupleurodon, Charlie! The magical leopleurodon, it's gunna guide us the way to candy mountainnnn!"));
    }

    private void saveFile(byte[] data, String fileName) throws IOException {
        Files.write(new File(Constants.TEMP_FOLDER_DIR + "\\" + fileName).toPath(), data);
    }

    private byte[] readData(SortedSet<FileStack> parts, Player sender) {
        Iterator<FileStack> stacks = parts.iterator();
        String hexData = "";

        while (stacks.hasNext()) {
            FileStack stack = stacks.next();

            TextComponent c = (TextComponent) stack.getStack().getItemMeta().lore().get(0);
            Developer.log("Processing part %d.", stack.getCount());
            hexData = hexData + c.content();

            stacks.remove();
        }

        byte[] bytes;

        try {
            bytes = StringUtil.hexStringToByteArray(hexData);
        } catch (StringIndexOutOfBoundsException e) {
            sender.sendMessage(Messages.SS_ERROR.get("Error processing parts! Part Read Failure!!"));
            e.printStackTrace();
            return null;
        }

        return bytes;
    }
}
