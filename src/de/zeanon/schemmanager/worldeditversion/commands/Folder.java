package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.DefaultUtils;
import de.zeanon.schemmanager.worldeditversion.utils.Helper;
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
        int listmax = DefaultUtils.getInt("Listmax");
        Path schemFolderPath = Helper.getSchemPath();
        boolean spaceLists = DefaultUtils.getBoolean("Space Lists");

        String deep = "";
        if (deepSearch) {
            deep = "-deep ";
        }

        if (args.length == 2) {
            File directory = schemFolderPath.toFile();
            if (!directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                return false;
            } else {
                ArrayList<File> rawFiles = DefaultUtils.getFolders(directory, deepSearch);
                File[] files = rawFiles.toArray(new File[0]);
                Arrays.sort(files);
                double count = files.length;
                int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                    return true;
                } else {
                    DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                    if (count < listmax) {
                        listmax = (int) count;
                    }
                    for (int i = 0; i < listmax; i++) {
                        sendListLine(p, schemFolderPath, files[i], i, deepSearch);
                    }

                    if (side > 1) {
                        DefaultUtils.sendScrollMessage("//schem folder " + deep + "2", "//schem folder " + deep + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                        return true;
                    } else {
                        DefaultUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        } else if (args.length == 3) {
            if (StringUtils.isNumeric(args[2])) {
                File directory = schemFolderPath.toFile();
                if (!directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                    return false;
                } else {
                    ArrayList<File> rawFiles = DefaultUtils.getFolders(directory, deepSearch);
                    File[] files = rawFiles.toArray(new File[0]);
                    Arrays.sort(files);
                    double count = files.length;
                    int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                    int side_number = Integer.parseInt(args[2]);

                    if (side_number > side) {
                        DefaultUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + "global", p);
                        return false;
                    }
                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                        return true;
                    } else {
                        DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                        int id = (side_number - 1) * listmax;
                        if (count < listmax * side_number) {
                            listmax = (int) count - (listmax * (side_number - 1));
                        }
                        for (int i = 0; i < listmax; i++) {
                            sendListLine(p, schemFolderPath, files[id], id, deepSearch);
                            id++;
                        }

                        if (side > 1) {
                            if (side_number > 1) {
                                if (side_number < side) {
                                    DefaultUtils.sendScrollMessage("//schem folder " + deep + (side_number + 1), "//schem folder " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                } else {
                                    DefaultUtils.sendScrollMessage("//schem folder " + deep + "1", "//schem folder " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                DefaultUtils.sendScrollMessage("//schem folder " + deep + (side_number + 1), "//schem folder " + deep + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            DefaultUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            } else {
                File directory = schemFolderPath.resolve(args[2]).toFile();
                if (!directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
                    return false;
                } else {
                    ArrayList<File> rawFiles = DefaultUtils.getFolders(directory, deepSearch);
                    File[] files = rawFiles.toArray(new File[0]);
                    Arrays.sort(files);
                    double count = files.length;
                    int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                        return true;
                    } else {
                        DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                        if (count < listmax) {
                            listmax = (int) count;
                        }
                        for (int i = 0; i < listmax; i++) {
                            sendListLine(p, schemFolderPath, files[i], i, deepSearch);
                        }
                        if (side > 1) {
                            DefaultUtils.sendScrollMessage("//schem folder " + deep + args[2] + " 2", "//schem folder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        } else {
                            DefaultUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            }
        } else {
            File directory = schemFolderPath.resolve(args[2]).toFile();
            if (!directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
                return false;
            } else {
                ArrayList<File> rawFiles = DefaultUtils.getFolders(directory, deepSearch);
                File[] files = rawFiles.toArray(new File[0]);
                Arrays.sort(files);
                double count = files.length;
                int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                int side_number = Integer.parseInt(args[3]);

                if (side_number > side) {
                    DefaultUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + args[2], p);
                    return false;
                }
                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                    return true;
                } else {
                    DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                    int id = (side_number - 1) * listmax;
                    if (count < listmax * side_number) {
                        listmax = (int) count - (listmax * (side_number - 1));
                    }
                    for (int i = 0; i < listmax; i++) {
                        sendListLine(p, schemFolderPath, files[id], id, deepSearch);
                        id++;
                    }

                    if (side > 1) {
                        if (side_number > 1) {
                            if (side_number < side) {
                                DefaultUtils.sendScrollMessage("//schem folder " + deep + args[2] + " " + (side_number + 1), "//schem folder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            } else {
                                DefaultUtils.sendScrollMessage("//schem folder " + deep + args[2] + " 1", "//schem folder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            DefaultUtils.sendScrollMessage("//schem folder " + deep + args[2] + " " + (side_number + 1), "//schem folder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        }
                    } else {
                        DefaultUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        }

    }


    private static void sendListLine(Player p, Path schemFolderPath, File file, int id, boolean deepSearch) {
        try {
            String name = file.getName();
            String path = FilenameUtils.separatorsToUnix(schemFolderPath.relativize(file.toPath().toRealPath()).toString());
            if (deepSearch) {
                DefaultUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
            } else {
                DefaultUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}