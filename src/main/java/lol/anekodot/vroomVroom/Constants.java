package lol.anekodot.vroomVroom;

import org.bukkit.Material;

public class Constants {
    /**
     * The item to use for parts
     */
    public static final Material PART_MATERIAL = Material.ANCIENT_DEBRIS;

    /**
     * The shulker type to use for chunks
     */
    public static final Material CHUNK_MATERIAL = Material.BLACK_SHULKER_BOX;

    /**
     * The temporary folder directory.
     */
    public static final String TEMP_FOLDER_DIR = System.getenv("temp");

    public static String buildTempFilePath(String fileName) {
        return TEMP_FOLDER_DIR + "\\" + fileName;
    }
}
