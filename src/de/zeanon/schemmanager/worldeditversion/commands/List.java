package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.DefaultUtils;
import de.zeanon.schemmanager.worldeditversion.utils.Helper;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("Duplicates")
public class List {

    public static boolean onList(Player p, String[] args, boolean deepSearch) {
        int listmax = DefaultUtils.getInt("Listmax");
        Path schemPath = Helper.getSchemPath();
        boolean spaceLists = DefaultUtils.getBoolean("Space Lists");
        String[] extensions = DefaultUtils.getStringList("File Extensions").toArray(new String[0]);

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
                Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                File[] files = rawFiles.toArray(new File[0]);
                Arrays.sort(files);
                double count = files.length;
                int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    DefaultUtils.sendHoverMessage("", ChatColor.AQUA + "No schematics found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                    return true;
                } else {
                    DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                    if (count < listmax) {
                        listmax = (int) count;
                    }
                    for (int i = 0; i < listmax; i++) {
                        sendListLine(p, schemPath, files[i], i, deepSearch);
                    }

                    if (side > 1) {
                        DefaultUtils.sendScrollMessage("//schem list " + deep + "2", "//schem list " + deep + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                        return true;
                    } else {
                        DefaultUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
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
                    Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                    File[] files = rawFiles.toArray(new File[0]);
                    Arrays.sort(files);
                    double count = files.length;
                    int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                    int side_number = Integer.parseInt(args[2]);

                    if (side_number > side) {
                        DefaultUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " schematics in this list", "", ChatColor.GRAY + "global", p);
                        return false;
                    }
                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No schematics found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                        return true;
                    } else {
                        DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                        int id = (side_number - 1) * listmax;
                        if (count < listmax * side_number) {
                            listmax = (int) count - (listmax * (side_number - 1));
                        }
                        for (int i = 0; i < listmax; i++) {
                            sendListLine(p, schemPath, files[id], id, deepSearch);
                            id++;
                        }

                        if (side > 1) {
                            if (side_number > 1) {
                                if (side_number < side) {
                                    DefaultUtils.sendScrollMessage("//schem list " + deep + (side_number + 1), "//schem list " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                } else {
                                    DefaultUtils.sendScrollMessage("//schem list " + deep + "1", "//schem list " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                DefaultUtils.sendScrollMessage("//schem list " + deep + (side_number + 1), "//schem list " + deep + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            DefaultUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            } else {
                File directory = schemPath != null ? schemPath.resolve(args[2]).toFile() : null;
                if (directory == null || directory.exists() || !directory.isDirectory()) {
                    p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
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
                        DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No schematics found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                        return true;
                    } else {
                        DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                        if (count < listmax) {
                            listmax = (int) count;
                        }
                        for (int i = 0; i < listmax; i++) {
                            sendListLine(p, schemPath, files[i], i, deepSearch);
                        }

                        if (side > 1) {
                            DefaultUtils.sendScrollMessage("//schem list " + deep + args[2] + " 2", "//schem list " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        } else {
                            DefaultUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
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
                Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, deepSearch);
                File[] files = rawFiles.toArray(new File[0]);
                Arrays.sort(files);
                double count = files.length;
                int side = (int) ((count / listmax % 1 != 0) ? (count / listmax) + 1 : (count / listmax));
                int side_number = Integer.parseInt(args[3]);

                if (side_number > side) {
                    DefaultUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " schematics in this list", "", ChatColor.GRAY + args[2], p);
                    return false;
                }
                if (spaceLists) {
                    p.sendMessage(" ");
                }
                if (count < 1) {
                    DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No schematics found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                    return true;
                } else {
                    DefaultUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                    int id = (side_number - 1) * listmax;
                    if (count < listmax * side_number) {
                        listmax = (int) count - (listmax * (side_number - 1));
                    }
                    for (int i = 0; i < listmax; i++) {
                        sendListLine(p, schemPath, files[id], id, deepSearch);
                        id++;
                    }

                    if (side > 1) {
                        if (side_number > 1) {
                            if (side_number < side) {
                                DefaultUtils.sendScrollMessage("//schem list " + deep + args[2] + " " + (side_number + 1), "//schem list " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            } else {
                                DefaultUtils.sendScrollMessage("//schem list " + deep + args[2] + " 1", "//schem list " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            DefaultUtils.sendScrollMessage("//schem list " + deep + args[2] + " " + (side_number + 1), "//schem list " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        }
                    } else {
                        DefaultUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                        return true;
                    }
                }
            }
        }
    }


    private static void sendListLine(Player p, Path schemFolderPath, File file, int id, boolean deepSearch) {
        try {
            String name;
            String path;
            if (Objects.equals(DefaultUtils.getExtension(file.getName()), "schem")) {
                name = DefaultUtils.removeExtension(file.getName());
                path = FilenameUtils.separatorsToUnix(DefaultUtils.removeExtension(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString()));
            } else {
                name = file.getName();
                path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
            }
            if (deepSearch) {
                DefaultUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + name + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
            } else {
                DefaultUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name, ChatColor.RED + "Load " + ChatColor.GOLD + name + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}