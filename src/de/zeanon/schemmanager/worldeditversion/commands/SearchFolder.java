package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.DefaultHelper;
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
public class SearchFolder {

    public static boolean onSearchFolder(Player p, String[] args, Boolean deepSearch) {
        try {
            int listmax = DefaultHelper.getInt("Listmax");
            Path schemFolderPath = Helper.getSchemPath();
            boolean spaceLists = DefaultHelper.getBoolean("Space Lists");

            String deep = "";
            if (deepSearch) {
                deep = "-deep ";
            }

            if (args.length == 3) {
                File directory = schemFolderPath.toFile();
                if (!directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                    return false;
                } else {
                    File[] files = getFileArray(directory, deepSearch, args[2]);
                    double count = files.length;
                    int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                        return true;
                    } else {
                        DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                        if (count < listmax) {
                            listmax = (int) count;
                        }
                        for (int i = 0; i < listmax; i++) {
                            sendListLine(p, schemFolderPath, files[i], i, deepSearch);
                        }

                        if (side > 1) {
                            DefaultHelper.sendScrollMessage("//schem searchfolder " + deep + args[2] + " 2", "//schem searchfolder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        } else {
                            DefaultHelper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            } else if (args.length == 4) {
                if (StringUtils.isNumeric(args[3])) {
                    File directory = schemFolderPath.toFile();
                    if (!directory.exists() || !directory.isDirectory()) {
                        p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                        return false;
                    } else {
                        File[] files = getFileArray(directory, deepSearch, args[2]);
                        double count = files.length;
                        int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                        int side_number = Integer.parseInt(args[3]);

                        if (side_number > side) {
                            DefaultHelper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + "global", p);
                            return false;
                        }
                        if (spaceLists) {
                            p.sendMessage(" ");
                        }
                        if (count < 1) {
                            DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                            return true;
                        } else {
                            DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

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
                                        DefaultHelper.sendScrollMessage("//schem searchfolder " + deep + args[2] + " " + (side_number + 1), "//schem searchfolder " + deep + args[2] + " " + (side_number - 1), ChatColor.RED + "Page " + (side_number + 1), ChatColor.RED + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                        return true;
                                    } else {
                                        DefaultHelper.sendScrollMessage("//schem searchfolder " + deep + args[2] + " 1", "//schem searchfolder " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                        return true;
                                    }
                                } else {
                                    DefaultHelper.sendScrollMessage("//schem searchfolder " + deep + args[2] + " " + (side_number + 1), "//schem searchfolder " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                DefaultHelper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
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
                        File[] files = getFileArray(directory, deepSearch, args[2]);
                        double count = files.length;
                        int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

                        if (spaceLists) {
                            p.sendMessage(" ");
                        }
                        if (count < 1) {
                            DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                            return true;
                        } else {
                            DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                            if (count < listmax) {
                                listmax = (int) count;
                            }
                            for (int i = 0; i < listmax; i++) {
                                sendListLine(p, schemFolderPath, files[i], i, deepSearch);
                            }

                            if (side > 1) {
                                DefaultHelper.sendScrollMessage("//schem searchfolder " + deep + args[3] + " 2", "//schem searchfolder " + deep + args[3] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            } else {
                                DefaultHelper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
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
                    File[] files = getFileArray(directory, deepSearch, args[2]);
                    double count = files.length;
                    int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                    int side_number = Integer.parseInt(args[4]);

                    if (side_number > side) {
                        DefaultHelper.sendHoverMessage("", ChatColor.RED + "There are only " + side + " pages of folders in this list", "", ChatColor.GRAY + args[2], p);
                        return false;
                    }
                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No folders found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                        return true;
                    } else {
                        DefaultHelper.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Folder | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

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
                                    DefaultHelper.sendScrollMessage("//schem searchfolder " + deep + args[3] + " " + (side_number + 1), "//schem searchfolder " + deep + args[3] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                } else {
                                    DefaultHelper.sendScrollMessage("//schem searchfolder " + deep + args[3] + " 1", "//schem searchfolder " + deep + args[3] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                DefaultHelper.sendScrollMessage("//schem searchfolder " + deep + args[3] + " " + (side_number + 1), "//schem searchfolder " + deep + args[3] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            DefaultHelper.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", ChatColor.DARK_PURPLE + "There is only one page of folders in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.RED + "Could not find Schematic folder.");
            return false;
        }
    }


    private static void sendListLine(Player p, Path schemFolderPath, File file, int id, boolean deepSearch) {
        try {
            String name = file.getName();
            String path = FilenameUtils.separatorsToUnix(schemFolderPath.relativize(file.toPath().toRealPath()).toString());
            if (deepSearch) {
                DefaultHelper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
            } else {
                DefaultHelper.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GREEN + name, ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File[] getFileArray(File directory, boolean deepSearch, String regex) {
        ArrayList<File> files = new ArrayList<>();
        for (File file : DefaultHelper.getFolders(directory, deepSearch)) {
            if (file.getName().toLowerCase().contains(regex.toLowerCase())) {
                files.add(file);
            }
        }
        File[] fileArray = files.toArray(new File[0]);
        Arrays.sort(fileArray);
        return fileArray;
    }
}