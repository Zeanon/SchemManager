package de.zeanon.schemmanager.worldeditversion.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("Duplicates")
public class Search {

    public static boolean onSearch(Player p, String[] args, Boolean deepSearch) {
        int listmax = Helper.getInt("Listmax");
        String schemFolderPath = Helper.getSchemPath();
        boolean spaceLists = Helper.getBoolean("Space Lists");
        String[] extensions = Helper.getStringList("File Extensions").toArray(new String[0]);

        String deep = "";
        if (deepSearch) {
            deep = "-deep ";
        }

        if (args.length == 3) {
            File directory = new File(schemFolderPath);
            if (!directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                return false;
            } else {
                File[] files = getFileArray(directory, extensions, deepSearch, args[2]);
                double count = files.length;
                int side = getSide(listmax, count);

                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                    return true;
                } else {
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                    if (count < listmax) {
                        listmax = (int) count;
                    }
                    for (int i = 0; i < listmax; i++) {
                        sendListLine(p, schemFolderPath, files[i], i, deepSearch);
                    }

                    if (side > 1) {
                        Helper.sendScrollMessage("//schem search " + deep + args[2] + " 2", "//schem search " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                        return true;
                    } else {
                        Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        } else if (args.length == 4) {
            if (StringUtils.isNumeric(args[3])) {
                File directory = new File(schemFolderPath);
                if (!directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                    return false;
                } else {
                    File[] files = getFileArray(directory, extensions, deepSearch, args[2]);
                    double count = files.length;
                    int side = getSide(listmax, count);
                    int side_number = Integer.parseInt(args[3]);

                    if (side_number > side) {
                        Helper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " schematics in this list", "", ChatColor.GRAY + "global", p);
                        return false;
                    }
                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                        return true;
                    } else {
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

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
                                    Helper.sendScrollMessage("//schem search " + deep + args[2] + " " + (side_number + 1), "//schem search " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                } else {
                                    Helper.sendScrollMessage("//schem search " + deep + args[2] + " 1", "//schem search " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                Helper.sendScrollMessage("//schem search " + deep + args[2] + " " + (side_number + 1), "//schem search " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            } else {
                File directory = new File(schemFolderPath + args[2]);
                if (!directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
                    return false;
                } else {
                    File[] files = getFileArray(directory, extensions, deepSearch, args[2]);
                    double count = files.length;
                    int side = getSide(listmax, count);

                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                        return true;
                    } else {
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                        if (count < listmax) {
                            listmax = (int) count;
                        }
                        for (int i = 0; i < listmax; i++) {
                            sendListLine(p, schemFolderPath, files[i], i, deepSearch);
                        }

                        if (side > 1) {
                            Helper.sendScrollMessage("//schem search " + deep + args[3] + " 2", "//schem search " + deep + args[3] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        } else {
                            Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            }
        } else {
            File directory = new File(schemFolderPath + args[2]);
            if (!directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
                return false;
            } else {
                File[] files = getFileArray(directory, extensions, deepSearch, args[2]);
                double count = files.length;
                int side = getSide(listmax, count);
                int side_number = Integer.parseInt(args[4]);

                if (side_number > side) {
                    Helper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " schematics in this list", "", ChatColor.GRAY + args[2], p);
                    return false;
                }
                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                    return true;
                } else {
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

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
                                Helper.sendScrollMessage("//schem search " + deep + args[3] + " " + (side_number + 1), "//schem search " + deep + args[3] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            } else {
                                Helper.sendScrollMessage("//schem search " + deep + args[3] + " 1", "//schem search " + deep + args[3] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            Helper.sendScrollMessage("//schem search " + deep + args[3] + " " + (side_number + 1), "//schem search " + deep + args[3] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        }
                    } else {
                        Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        }
    }


    private static void sendListLine(Player p, String schemFolderPath, File file, int id, boolean deepSearch) {
        String name = file.getName();
        String path = FilenameUtils.getBaseName(file.getAbsolutePath()).replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
        String format = "";
        if (FilenameUtils.getExtension(name).equals("schem")) {
            name = FilenameUtils.getBaseName(name);
        } else {
            format = " " + FilenameUtils.getExtension(name);
        }
        if (deepSearch) {
            Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path + format, p);
        } else {
            Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name, ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path + format, p);
        }
    }

    private static int getSide(int listmax, Double count) {
        if (count / listmax % 1 != 0) {
            return (int) (count / listmax) + 1;
        } else {
            return (int) (count / listmax);
        }
    }

    private static File[] getFileArray(File directory, String[] extensions, boolean deepSearch, String regex) {
        Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
        ArrayList<File> fileArray = new ArrayList<>();
        for (File file : rawFiles) {
            if (FilenameUtils.getBaseName(file.getName()).toLowerCase().contains(regex.toLowerCase())) {
                fileArray.add(file);
            }
        }
        File[] files = fileArray.toArray(new File[0]);
        Arrays.sort(files);
        return files;
    }
}