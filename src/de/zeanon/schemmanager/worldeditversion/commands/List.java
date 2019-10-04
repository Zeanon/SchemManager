package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("Duplicates")
public class List {

    public static boolean onList(Player p, String[] args, boolean deepSearch) {
        int listmax = DefaultHelper.getInt("Listmax");
        String schemFolderPath = Helper.getSchemPath();
        boolean spaceLists = DefaultHelper.getBoolean("Space Lists");
        String[] extensions = DefaultHelper.getStringList("File Extensions").toArray(new String[0]);

        String deep = "";
        if (deepSearch) {
            deep = "-deep ";
        }

        if (args.length == 2) {
            File directory = new File(schemFolderPath);
            if (!directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                return false;
            } else {
                Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                File[] files = rawFiles.toArray(new File[0]);
                Arrays.sort(files);
                double count = files.length;
                int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    DefaultHelper.sendHoverMessage("", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                    return true;
                } else {
                    DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                    if (count < listmax) {
                        listmax = (int) count;
                    }
                    for (int i = 0; i < listmax; i++) {
                        sendListLine(p, schemFolderPath, files[i], i, deepSearch);
                    }

                    if (side > 1) {
                        DefaultHelper.sendScrollMessage("//schem list " + deep + "2", "//schem list " + deep + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                        return true;
                    } else {
                        DefaultHelper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        } else if (args.length == 3) {
            if (StringUtils.isNumeric(args[2])) {
                File directory = new File(schemFolderPath);
                if (!directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                    return false;
                } else {
                    Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                    File[] files = rawFiles.toArray(new File[0]);
                    Arrays.sort(files);
                    double count = files.length;
                    int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                    int side_number = Integer.parseInt(args[2]);

                    if (side_number > side) {
                        DefaultHelper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " schematics in this list", "", ChatColor.GRAY + "global", p);
                        return false;
                    }
                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                        return true;
                    } else {
                        DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

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
                                    DefaultHelper.sendScrollMessage("//schem list " + deep + (side_number + 1), "//schem list " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                } else {
                                    DefaultHelper.sendScrollMessage("//schem list " + deep + "1", "//schem list " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                DefaultHelper.sendScrollMessage("//schem list " + deep + (side_number + 1), "//schem list " + deep + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            DefaultHelper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            } else {
                File directory = new File(schemFolderPath + args[2]);
                if (!directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " is no folder.");
                    return false;
                } else {
                    Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                    File[] files = rawFiles.toArray(new File[0]);
                    Arrays.sort(files);
                    double count = files.length;
                    int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                        return true;
                    } else {
                        DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                        if (count < listmax) {
                            listmax = (int) count;
                        }
                        for (int i = 0; i < listmax; i++) {
                            sendListLine(p, schemFolderPath, files[i], i, deepSearch);
                        }

                        if (side > 1) {
                            DefaultHelper.sendScrollMessage("//schem list " + deep + args[2] + " 2", "//schem list " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        } else {
                            DefaultHelper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            }
        } else {
            File directory = new File(schemFolderPath + args[2]);
            if (!directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " is no folder.");
                return false;
            } else {
                Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                File[] files = rawFiles.toArray(new File[0]);
                Arrays.sort(files);
                double count = files.length;
                int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                int side_number = Integer.parseInt(args[3]);

                if (side_number > side) {
                    DefaultHelper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " schematics in this list", "", ChatColor.GRAY + args[2], p);
                    return false;
                }
                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                    return true;
                } else {
                    DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

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
                                DefaultHelper.sendScrollMessage("//schem list " + deep + args[2] + " " + (side_number + 1), "//schem list " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            } else {
                                DefaultHelper.sendScrollMessage("//schem list " + deep + args[2] + " 1", "//schem list " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            DefaultHelper.sendScrollMessage("//schem list " + deep + args[2] + " " + (side_number + 1), "//schem list " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        }
                    } else {
                        DefaultHelper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        }
    }


    private static void sendListLine(Player p, String schemFolderPath, File file, int id, boolean deepSearch) {
        String name = file.getName();
        String path;
        if (Objects.requireNonNull(DefaultHelper.getExtension(name)).equals("schem")) {
            name = DefaultHelper.removeExtension(name);
            path = DefaultHelper.removeExtension(file.getAbsolutePath()).replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
        } else {
            path = file.getAbsolutePath().replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
        }
        if (deepSearch) {
            DefaultHelper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + name + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
        } else {
            DefaultHelper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name, ChatColor.RED + "Load " + ChatColor.GOLD + name + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
        }
    }
}