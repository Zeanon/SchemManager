package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.ConfigUtils;
import de.zeanon.schemmanager.globalutils.InternalFileUtils;
import de.zeanon.schemmanager.globalutils.MessageUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionSchemUtils;
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

@SuppressWarnings("Duplicates")
public class List {

    public static boolean onList(Player p, String[] args, boolean deepSearch) {
        int listmax = ConfigUtils.getInt("Listmax");
        Path schemPath = WorldEditVersionSchemUtils.getSchemPath();
        boolean spaceLists = ConfigUtils.getBoolean("Space Lists");
        String[] extensions = ConfigUtils.getStringList("File Extensions").toArray(new String[0]);

        String deep = "";
        if (deepSearch) {
            deep = "-deep ";
        }

        if (args.length == 2) {
            try {
                Path listPath = schemPath != null ? schemPath.toRealPath() : null;
                File directory = listPath != null ? listPath.toFile() : null;
                if (directory == null || !directory.isDirectory()) {
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
                        MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No schematics found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                        return true;
                    } else {
                        MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                        if (count < listmax) {
                            listmax = (int) count;
                        }
                        for (int i = 0; i < listmax; i++) {
                            if (sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
                                return false;
                            }
                        }

                        if (side > 1) {
                            MessageUtils.sendScrollMessage("//schem list " + deep + "2", "//schem list " + deep + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                            return true;
                        } else {
                            MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                return false;
            }
        } else if (args.length == 3) {
            if (StringUtils.isNumeric(args[2])) {
                try {
                    Path listPath = schemPath != null ? schemPath.toRealPath() : null;
                    File directory = listPath != null ? listPath.toFile() : null;
                    if (directory == null || !directory.isDirectory()) {
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
                            MessageUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " schematics in this list", "", ChatColor.GRAY + "global", p);
                            return false;
                        }
                        if (spaceLists) {
                            p.sendMessage(" ");
                        }
                        if (count < 1) {
                            MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No schematics found", ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);
                            return true;
                        } else {
                            MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + "global", p);

                            int id = (side_number - 1) * listmax;
                            if (count < listmax * side_number) {
                                listmax = (int) count - (listmax * (side_number - 1));
                            }
                            for (int i = 0; i < listmax; i++) {
                                if (sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
                                    return false;
                                }
                                id++;
                            }

                            if (side > 1) {
                                if (side_number > 1) {
                                    if (side_number < side) {
                                        MessageUtils.sendScrollMessage("//schem list " + deep + (side_number + 1), "//schem list " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                        return true;
                                    } else {
                                        MessageUtils.sendScrollMessage("//schem list " + deep + "1", "//schem list " + deep + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                        return true;
                                    }
                                } else {
                                    MessageUtils.sendScrollMessage("//schem list " + deep + (side_number + 1), "//schem list " + deep + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                                return true;
                            }
                        }
                    }
                } catch (IOException e) {
                    p.sendMessage(ChatColor.RED + "There is no schematic folder.");
                    return false;
                }
            } else {
                try {
                    Path listPath = schemPath != null ? schemPath.resolve(args[2]).toRealPath() : null;
                    File directory = listPath != null ? listPath.toFile() : null;
                    if (directory == null || !directory.isDirectory()) {
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
                            MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No schematics found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                            return true;
                        } else {
                            MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page 1/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                            if (count < listmax) {
                                listmax = (int) count;
                            }
                            for (int i = 0; i < listmax; i++) {
                                if (sendListLineFailed(p, schemPath, listPath, files[i], i, deepSearch)) {
                                    return false;
                                }
                            }

                            if (side > 1) {
                                MessageUtils.sendScrollMessage("//schem list " + deep + args[2] + " 2", "//schem list " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page 2", ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            } else {
                                MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                                return true;
                            }
                        }
                    }
                } catch (IOException e) {
                    p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
                    return false;
                }
            }
        } else {
            try {
                Path listPath = schemPath != null ? schemPath.resolve(args[2]).toRealPath() : null;
                File directory = listPath != null ? listPath.toFile() : null;
                if (directory == null || !directory.isDirectory()) {
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
                        MessageUtils.sendHoverMessage("", ChatColor.RED + "There are only " + side + " schematics in this list", "", ChatColor.GRAY + args[2], p);
                        return false;
                    }
                    if (spaceLists) {
                        p.sendMessage(" ");
                    }
                    if (count < 1) {
                        MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "No schematics found", ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);
                        return true;
                    } else {
                        MessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ", ChatColor.AQUA + "" + (int) count + " Schematics | Page " + side_number + "/" + side, ChatColor.AQUA + " ===", ChatColor.GRAY + args[2], p);

                        int id = (side_number - 1) * listmax;
                        if (count < listmax * side_number) {
                            listmax = (int) count - (listmax * (side_number - 1));
                        }
                        for (int i = 0; i < listmax; i++) {
                            if (sendListLineFailed(p, schemPath, listPath, files[id], id, deepSearch)) {
                                return false;
                            }
                            id++;
                        }

                        if (side > 1) {
                            if (side_number > 1) {
                                if (side_number < side) {
                                    MessageUtils.sendScrollMessage("//schem list " + deep + args[2] + " " + (side_number + 1), "//schem list " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                } else {
                                    MessageUtils.sendScrollMessage("//schem list " + deep + args[2] + " 1", "//schem list " + deep + args[2] + " " + (side_number - 1), ChatColor.DARK_PURPLE + "Page 1", ChatColor.DARK_PURPLE + "Page " + (side_number - 1), p, ChatColor.DARK_AQUA);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendScrollMessage("//schem list " + deep + args[2] + " " + (side_number + 1), "//schem list " + deep + args[2] + " " + side, ChatColor.DARK_PURPLE + "Page " + (side_number + 1), ChatColor.DARK_PURPLE + "Page " + side, p, ChatColor.DARK_AQUA);
                                return true;
                            }
                        } else {
                            MessageUtils.sendScrollMessage("", "", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", ChatColor.DARK_PURPLE + "There is only one page of schematics in this list", p, ChatColor.BLUE);
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is no folder.");
                return false;
            }
        }
    }


    private static boolean sendListLineFailed(Player p, Path schemFolderPath, Path listPath, File file, int id, boolean deepSearch) {
        return (!sendListLine(p, schemFolderPath, listPath, file, id, deepSearch));
    }

    private static boolean sendListLine(Player p, Path schemFolderPath, Path listPath, File file, int id, boolean deepSearch) {
        try {
            String name;
            String path;
            String shortenedRelativePath;
            if (InternalFileUtils.getExtension(file.getName()).equals("schem")) {
                name = InternalFileUtils.removeExtension(file.getName());
                path = FilenameUtils.separatorsToUnix(InternalFileUtils.removeExtension(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString()));
                shortenedRelativePath = deepSearch ? FilenameUtils.separatorsToUnix(InternalFileUtils.removeExtension(listPath.relativize(file.toPath().toRealPath()).toString())) : null;
            } else {
                name = file.getName();
                path = FilenameUtils.separatorsToUnix(schemFolderPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
                shortenedRelativePath = deepSearch ? FilenameUtils.separatorsToUnix(listPath.relativize(file.toPath().toRealPath()).toString()) : null;
            }
            if (deepSearch) {
                MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + shortenedRelativePath + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
                return true;
            } else {
                MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name, ChatColor.RED + "Load " + ChatColor.GOLD + path + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.RED + "An Error occured while getting the filepaths for the schematics, please see console for further information.");
            return false;
        }
    }
}