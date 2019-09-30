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

public class Search {

    @SuppressWarnings("Duplicates")
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
            int side;

            File directory = new File(schemFolderPath);
            if (!directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                return false;
            } else {
                Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                ArrayList<File> fileArray = new ArrayList<>();
                for (File file : rawFiles) {
                    if (FilenameUtils.getBaseName(file.getName()).toLowerCase().contains(args[2].toLowerCase())) {
                        fileArray.add(file);
                    }
                }
                File[] files = fileArray.toArray(new File[0]);
                Arrays.sort(files);

                double count = files.length;
                double side_count = count / listmax;
                if (side_count % 1 != 0) {
                    side = (int) side_count + 1;
                } else {
                    side = (int) side_count;
                }

                if (count < listmax) {
                    listmax = (int) count;
                }
                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                    return true;
                } else {
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                    for (int i = 0; i < listmax; i++) {
                        if (!files[i].isDirectory()) {
                            String name = files[i].getName();
                            String path = FilenameUtils.getBaseName(files[i].getAbsolutePath()).replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
                            if (FilenameUtils.getExtension(name).equals("schem")) {
                                path = path + " " + FilenameUtils.getExtension(name);
                                name = FilenameUtils.getBaseName(name);
                            }
                            Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
                        }
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
                int side;
                int side_number = Integer.parseInt(args[3]);

                File directory = new File(schemFolderPath);
                if (!directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                    return false;
                } else {
                    Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                    ArrayList<File> fileArray = new ArrayList<>();
                    for (File file : rawFiles) {
                        if (FilenameUtils.getBaseName(file.getName()).toLowerCase().contains(args[2].toLowerCase())) {
                            fileArray.add(file);
                        }
                    }
                    File[] files = fileArray.toArray(new File[0]);
                    Arrays.sort(files);

                    double count = files.length;
                    double side_count = count / listmax;
                    if (side_count % 1 != 0) {
                        side = (int) side_count + 1;
                    } else {
                        side = (int) side_count;
                    }

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

                        if (count >= listmax * side_number) {
                            int id = (side_number - 1) * listmax;
                            for (int i = 0; i < listmax; i++) {
                                if (!files[id].isDirectory()) {
                                    String name = files[id].getName();
                                    String path = FilenameUtils.getBaseName(files[id].getAbsolutePath()).replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
                                    if (FilenameUtils.getExtension(name).equals("schem")) {
                                        path = path + " " + FilenameUtils.getExtension(name);
                                        name = FilenameUtils.getBaseName(name);
                                    }
                                    Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
                                }
                                id++;
                            }
                        } else {
                            int id = (side_number - 1) * listmax;
                            for (int i = 0; i < count - ((side_number - 1) * listmax); i++) {
                                if (!files[id].isDirectory()) {
                                    String name = files[id].getName();
                                    String path = FilenameUtils.getBaseName(files[id].getAbsolutePath()).replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
                                    if (FilenameUtils.getExtension(name).equals("schem")) {
                                        path = path + " " + FilenameUtils.getExtension(name);
                                        name = FilenameUtils.getBaseName(name);
                                    }
                                    Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
                                }
                                id++;
                            }
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
                int side;

                File directory = new File(schemFolderPath + args[2]);
                if (!directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
                    return false;
                } else {
                    Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                    ArrayList<File> fileArray = new ArrayList<>();
                    for (File file : rawFiles) {
                        if (FilenameUtils.getBaseName(file.getName()).toLowerCase().contains(args[2].toLowerCase())) {
                            fileArray.add(file);
                        }
                    }
                    File[] files = fileArray.toArray(new File[0]);
                    Arrays.sort(files);

                    double count = files.length;
                    double side_count = count / listmax;
                    if (side_count % 1 != 0) {
                        side = (int) side_count + 1;
                    } else {
                        side = (int) side_count;
                    }

                    if (count < listmax) {
                        listmax = (int) count;
                    }
                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Schematics | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                        return true;
                    } else {
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                        for (int i = 0; i < listmax; i++) {
                            if (!files[i].isDirectory()) {
                                String name = files[i].getName();
                                String path = FilenameUtils.getBaseName(files[i].getAbsolutePath()).replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
                                if (FilenameUtils.getExtension(name).equals("schem")) {
                                    path = path + " " + FilenameUtils.getExtension(name);
                                    name = FilenameUtils.getBaseName(name);
                                }
                                Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
                            }
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
            int side;
            int side_number = Integer.parseInt(args[4]);
            File directory = new File(schemFolderPath + args[2]);
            if (!directory.exists() || !directory.isDirectory()) {
                p.sendMessage(ChatColor.RED + args[2] + " is no folder.");
                return false;
            } else {
                Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                ArrayList<File> fileArray = new ArrayList<>();
                for (File file : rawFiles) {
                    if (FilenameUtils.getBaseName(file.getName()).toLowerCase().contains(args[2].toLowerCase())) {
                        fileArray.add(file);
                    }
                }
                File[] files = fileArray.toArray(new File[0]);
                Arrays.sort(files);

                double count = files.length;
                double side_count = count / listmax;
                if (side_count % 1 != 0) {
                    side = (int) side_count + 1;
                } else {
                    side = (int) side_count;
                }

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

                    if (count >= listmax * side_number) {
                        int id = (side_number - 1) * listmax;
                        for (int i = 0; i < listmax; i++) {
                            if (!files[id].isDirectory()) {
                                String name = files[id].getName();
                                String path = FilenameUtils.getBaseName(files[id].getAbsolutePath()).replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
                                if (FilenameUtils.getExtension(name).equals("schem")) {
                                    path = path + " " + FilenameUtils.getExtension(name);
                                    name = FilenameUtils.getBaseName(name);
                                }
                                Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
                            }
                            id++;
                        }
                    } else {
                        int id = (side_number - 1) * listmax;
                        for (int i = 0; i < count - ((side_number - 1) * listmax); i++) {
                            if (!files[id].isDirectory()) {
                                String name = files[id].getName();
                                String path = FilenameUtils.getBaseName(files[id].getAbsolutePath()).replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
                                if (FilenameUtils.getExtension(name).equals("schem")) {
                                    path = path + " " + FilenameUtils.getExtension(name);
                                    name = FilenameUtils.getBaseName(name);
                                }
                                Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
                            }
                            id++;
                        }
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
}