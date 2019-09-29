package de.zeanon.schemmanager.worldeditversion.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;

public class SearchFolder {

    @SuppressWarnings("Duplicates")
    public static boolean onSearchFolder(Player p, String[] args, Boolean deepSearch) {
        int listmax = Helper.getInt("Listmax");
        String schemFolderPath = Helper.getSchemPath();
        boolean spaceLists = Helper.getBoolean("Space Lists");

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
                ArrayList<File> fileArray = new ArrayList<>();
                for (File file : Helper.getFolders(directory, deepSearch)) {
                    String name = file.getName().toLowerCase();
                    if (name.contains(args[2].toLowerCase())) {
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
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Folder | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                    return true;
                } else {
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                    for (int i = 0; i < listmax; i++) {
                        if (files[i].isDirectory()) {
                            String name = files[i].getName();
                            String path = files[i].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
                            Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
                        }
                    }

                    if (side > 1) {
                        Helper.sendScrollMessage("//schem searchfolder " + deep + args[2] + " 2", "//schem searchfolder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                        return true;
                    } else {
                        Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
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
                    ArrayList<File> fileArray = new ArrayList<>();
                    for (File file : Helper.getFolders(directory, deepSearch)) {
                        String name = file.getName().toLowerCase();
                        if (name.contains(args[2].toLowerCase())) {
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
                        Helper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + "global", p);
                        return false;
                    }
                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Folder | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                        return true;
                    } else {
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                        if (count >= listmax * side_number) {
                            int id = (side_number - 1) * listmax;
                            for (int i = 0; i < listmax; i++) {
                                if (files[id].isDirectory()) {
                                    String name = files[id].getName();
                                    String path = files[id].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
                                    Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
                                }
                                id++;
                            }
                        } else {
                            int id = (side_number - 1) * listmax;
                            for (int i = 0; i < count - ((side_number - 1) * listmax); i++) {
                                if (files[id].isDirectory()) {
                                    String name = files[id].getName();
                                    String path = files[id].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
                                    Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
                                }
                                id++;
                            }
                        }

                        if (side > 1) {
                            if (side_number > 1) {
                                if (side_number < side) {
                                    Helper.sendScrollMessage("//schem searchfolder " + deep + args[2] + " " + (side_number + 1), "//schem searchfolder " + deep + args[2] + " " + (side_number - 1), ChatColor.RED + "Page " + (side_number + 1), ChatColor.RED + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                } else {
                                    Helper.sendScrollMessage("//schem searchfolder " + deep + args[2] + " 1", "//schem searchfolder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                Helper.sendScrollMessage("//schem searchfolder " + deep + args[2] + " " + (side_number + 1), "//schem searchfolder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
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
                    ArrayList<File> fileArray = new ArrayList<>();
                    for (File file : Helper.getFolders(directory, deepSearch)) {
                        String name = file.getName().toLowerCase();
                        if (name.contains(args[2].toLowerCase())) {
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
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Folder | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                        return true;
                    } else {
                        Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                        for (int i = 0; i < listmax; i++) {
                            if (files[i].isDirectory()) {
                                String name = files[i].getName();
                                String path = files[i].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
                                Helper.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
                            }
                        }

                        if (side > 1) {
                            Helper.sendScrollMessage("//schem searchfolder " + deep + args[3] + " 2", "//schem searchfolder " + deep + args[3] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        } else {
                            Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
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
                ArrayList<File> fileArray = new ArrayList<>();
                for (File file : Helper.getFolders(directory, deepSearch)) {
                    String name = file.getName().toLowerCase();
                    if (name.contains(args[2].toLowerCase())) {
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
                    Helper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + args[2], p);
                    return false;
                }
                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "0 Folder | Page 0/0", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                    return true;
                } else {
                    Helper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                    if (count >= listmax * side_number) {
                        int id = (side_number - 1) * listmax;
                        for (int i = 0; i < listmax; i++) {
                            if (files[id].isDirectory()) {
                                String name = files[id].getName();
                                String path = files[id].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
                                Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
                            }
                            id++;
                        }
                    } else {
                        int id = (side_number - 1) * listmax;
                        for (int i = 0; i < count - ((side_number - 1) * listmax); i++) {
                            if (files[id].isDirectory()) {
                                String name = files[id].getName();
                                String path = files[id].getAbsolutePath().replaceAll(schemFolderPath, "").replaceAll("\\\\", "/");
                                Helper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
                            }
                            id++;
                        }
                    }

                    if (side > 1) {
                        if (side_number > 1) {
                            if (side_number < side) {
                                Helper.sendScrollMessage("//schem searchfolder " + deep + args[3] + " " + (side_number + 1), "//schem searchfolder " + deep + args[3] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            } else {
                                Helper.sendScrollMessage("//schem searchfolder " + deep + args[3] + " 1", "//schem searchfolder " + deep + args[3] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            Helper.sendScrollMessage("//schem searchfolder " + deep + args[3] + " " + (side_number + 1), "//schem searchfolder " + deep + args[3] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        }
                    } else {
                        Helper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        }
    }
}