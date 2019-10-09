package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.ConfigUtils;
import de.zeanon.schemmanager.globalutils.InternalFileUtils;
import de.zeanon.schemmanager.globalutils.MessageUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionSchemUtils;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class Folder {

    public static boolean onFolder(Player p, String[] args, boolean deepSearch) {
        int listmax = ConfigUtils.getInt("Listmax");
        Path schemPath = WorldEditVersionSchemUtils.getSchemPath();
        boolean spaceLists = ConfigUtils.getBoolean("Space Lists");

        String deep = "";
        if (deepSearch) {
            deep = "-deep ";
        }

        if (args.length == 2) {
            File directory = schemPath != null ? schemPath.toFile() : null;
            if (directory == null || !directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                return false;
            } else {
                ArrayList<File> rawFiles = InternalFileUtils.getFolders(directory, deepSearch);
                File[] files = rawFiles.toArray(new File[0]);
                Arrays.sort(files);
                double count = files.length;
                int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                    return true;
                } else {
                    MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                    if (count < listmax) {
                        listmax = (int) count;
                    }
                    for (int i = 0; i < listmax; i++) {
                        sendListLine(p, schemPath, deepSearch ? "" : null, files[i], i, deepSearch);
                    }

                    if (side > 1) {
                        MessageUtils.sendScrollMessage("//schem folder " + deep + "2", "//schem folder " + deep + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                        return true;
                    } else {
                        MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        } else if (args.length == 3) {
            if (StringUtils.isNumeric(args[2])) {
                File directory = schemPath != null ? schemPath.toFile() : null;
                if (directory == null || !directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                    return false;
                } else {
                    ArrayList<File> rawFiles = InternalFileUtils.getFolders(directory, deepSearch);
                    File[] files = rawFiles.toArray(new File[0]);
                    Arrays.sort(files);
                    double count = files.length;
                    int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                    int side_number = Integer.parseInt(args[2]);

                    if (side_number > side) {
                        MessageUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + "global", p);
                        return false;
                    }
                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                        return true;
                    } else {
                        MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                        int id = (side_number - 1) * listmax;
                        if (count < listmax * side_number) {
                            listmax = (int) count - (listmax * (side_number - 1));
                        }
                        for (int i = 0; i < listmax; i++) {
                            sendListLine(p, schemPath, deepSearch ? "" : null, files[id], id, deepSearch);
                            id++;
                        }

                        if (side > 1) {
                            if (side_number > 1) {
                                if (side_number < side) {
                                    MessageUtils.sendScrollMessage("//schem folder " + deep + (side_number + 1), "//schem folder " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                } else {
                                    MessageUtils.sendScrollMessage("//schem folder " + deep + "1", "//schem folder " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendScrollMessage("//schem folder " + deep + (side_number + 1), "//schem folder " + deep + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            } else {
                File directory = schemPath != null ? schemPath.resolve(args[2]).toFile() : null;
                if (directory == null || !directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
                    return false;
                } else {
                    ArrayList<File> rawFiles = InternalFileUtils.getFolders(directory, deepSearch);
                    File[] files = rawFiles.toArray(new File[0]);
                    Arrays.sort(files);
                    double count = files.length;
                    int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                        return true;
                    } else {
                        MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                        if (count < listmax) {
                            listmax = (int) count;
                        }
                        for (int i = 0; i < listmax; i++) {
                            sendListLine(p, schemPath, deepSearch ? args[2] : null, files[i], i, deepSearch);
                        }
                        if (side > 1) {
                            MessageUtils.sendScrollMessage("//schem folder " + deep + args[2] + " 2", "//schem folder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        } else {
                            MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            }
        } else {
            File directory = schemPath != null ? schemPath.resolve(args[2]).toFile() : null;
            if (directory == null || !directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
                return false;
            } else {
                ArrayList<File> rawFiles = InternalFileUtils.getFolders(directory, deepSearch);
                File[] files = rawFiles.toArray(new File[0]);
                Arrays.sort(files);
                double count = files.length;
                int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                int side_number = Integer.parseInt(args[3]);

                if (side_number > side) {
                    MessageUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + args[2], p);
                    return false;
                }
                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                    return true;
                } else {
                    MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                    int id = (side_number - 1) * listmax;
                    if (count < listmax * side_number) {
                        listmax = (int) count - (listmax * (side_number - 1));
                    }
                    for (int i = 0; i < listmax; i++) {
                        sendListLine(p, schemPath, deepSearch ? args[2] : null, files[id], id, deepSearch);
                        id++;
                    }

                    if (side > 1) {
                        if (side_number > 1) {
                            if (side_number < side) {
                                MessageUtils.sendScrollMessage("//schem folder " + deep + args[2] + " " + (side_number + 1), "//schem folder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            } else {
                                MessageUtils.sendScrollMessage("//schem folder " + deep + args[2] + " 1", "//schem folder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            MessageUtils.sendScrollMessage("//schem folder " + deep + args[2] + " " + (side_number + 1), "//schem folder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        }
                    } else {
                        MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        }

    }


    private static void sendListLine(Player p, Path schemFolderPath, String listedPath, File file, int id, boolean deepSearch) {
        try {
            String name = file.getName();
            String path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
            String listPath = deepSearch ? FilenameUtils.separatorsToUnix(schemFolderPath.resolve(listedPath).toRealPath().relativize(file.toPath().toRealPath()).toString()) : null;
            if (deepSearch) {
                MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + listPath + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + path, "//schem list " + path, p);
            } else {
                MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}