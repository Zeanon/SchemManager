package de.zeanon.schemmanager.worldeditversion.commands;

import de.zeanon.schemmanager.globalutils.MessageUtils;
import de.zeanon.schemmanager.globalutils.ZeanonFileUtils;
import de.zeanon.schemmanager.worldeditversion.utils.Helper;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@SuppressWarnings("Duplicates")
public class RenameFolder {

    public static boolean onRenameFolder(Player p, String[] args) {
        try {
            Path schemPath = Helper.getSchemPath();
            File directory_old = schemPath != null ? schemPath.resolve(args[2]).toFile() : null;
            File directory_new = schemPath != null ? schemPath.resolve(args[3]).toFile() : null;

            if (args.length == 4) {
                if (directory_old == null || !directory_old.exists() || !directory_old.isDirectory()) {
                    p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
                    return false;
                } else if (directory_new.exists() && directory_new.isDirectory()) {
                    p.sendMessage(ChatColor.GREEN + args[3] + ChatColor.RED + " already exists, the folders will be merged.");
                    int id = 0;
                    String[] extension = {"schematic", "schem"};
                    for (File oldFile : FileUtils.listFiles(directory_old, extension, true)) {
                        for (File newFile : FileUtils.listFiles(directory_new, extension, true)) {
                            if (ZeanonFileUtils.removeExtension(newFile.getName()).equalsIgnoreCase(ZeanonFileUtils.removeExtension(oldFile.getName())) && newFile.toPath().relativize(directory_new.toPath()).equals(oldFile.toPath().relativize(directory_old.toPath()))) {
                                if (id == 0) {
                                    p.sendMessage(ChatColor.RED + "These schematics already exist in " + ChatColor.GREEN + args[3] + ChatColor.RED + ", they will be overwritten.");
                                }
                                String name;
                                String path;
                                if (ZeanonFileUtils.getExtension(newFile.getName()).equals("schem")) {
                                    name = ZeanonFileUtils.removeExtension(newFile.getName());
                                    path = FilenameUtils.separatorsToUnix(ZeanonFileUtils.removeExtension(schemPath.toRealPath().relativize(newFile.toPath().toRealPath()).toString()));
                                } else {
                                    name = newFile.getName();
                                    path = FilenameUtils.separatorsToUnix(schemPath.toRealPath().relativize(newFile.toPath().toRealPath()).toString());
                                }
                                MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ", ChatColor.GOLD + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Load " + ChatColor.GOLD + ZeanonFileUtils.removeExtension(newFile.getName()) + ChatColor.RED + " to your clipboard", "//schem load " + path, p);
                                id++;
                            }
                        }
                    }

                    int i = 0;
                    for (File oldFolder : ZeanonFileUtils.getFolders(directory_old, true)) {
                        for (File newFolder : ZeanonFileUtils.getFolders(directory_new, true)) {
                            if (newFolder.getName().equalsIgnoreCase(oldFolder.getName()) && newFolder.toPath().relativize(directory_new.toPath()).equals(oldFolder.toPath().relativize(directory_old.toPath()))) {
                                if (i == 0) {
                                    p.sendMessage(ChatColor.RED + "These folders already exist in " + ChatColor.GREEN + args[3] + ChatColor.RED + ", they will be merged.");
                                }
                                String name = newFolder.getName();
                                String path = FilenameUtils.separatorsToUnix(schemPath.toRealPath().relativize(newFolder.toPath().toRealPath()).toString());
                                MessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(i + 1) + ": ", ChatColor.GREEN + name + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + path + ChatColor.DARK_GRAY + "]", ChatColor.RED + "Open " + ChatColor.GREEN + name, "//schem list " + path, p);
                                i++;
                            }
                        }
                    }
                    if (id > 0 && i > 0) {
                        p.sendMessage(ChatColor.RED + "There are already " + ChatColor.DARK_PURPLE + id + ChatColor.RED
                                + " schematics and " + ChatColor.DARK_PURPLE + i + ChatColor.RED
                                + " folders with the same name in " + ChatColor.GREEN + args[3] + ChatColor.RED + ".");
                    } else if (id > 0) {
                        p.sendMessage(ChatColor.RED + "There are already " + ChatColor.DARK_PURPLE + id + ChatColor.RED
                                + " schematics with the same name in " + ChatColor.GREEN + args[3] + ChatColor.RED + ".");
                    } else if (i > 0) {
                        p.sendMessage(ChatColor.RED + "There are already " + ChatColor.DARK_PURPLE + i + ChatColor.RED
                                + " folders with the same name in " + ChatColor.GREEN + args[3] + ChatColor.RED + ".");
                    }
                }
                MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to rename " + ChatColor.GREEN + args[2] + ChatColor.RED + "?", "//schem renamefolder " + args[2] + " " + args[3] + " confirm", "//schem renamefolder " + args[2] + " " + args[3] + " deny", p);
                Helper.addRenameFolderRequest(p, args[2]);
                return true;
            } else if (args.length == 5 && Helper.checkRenameFolderRequest(p, args[2])) {
                if (args[4].equalsIgnoreCase("confirm")) {
                    Helper.removeRenameFolderRequest(p);
                    if (directory_old != null && directory_old.exists() && directory_old.isDirectory()) {
                        if (deepMerge(directory_old, directory_new)) {
                            try {
                                FileUtils.deleteDirectory(directory_old);
                                String parentName = Objects.requireNonNull(directory_old.getParentFile().listFiles()).length > 0 ? null : ZeanonFileUtils.deleteEmptyParent(directory_old);
                                if (parentName != null) {
                                    p.sendMessage(ChatColor.RED + "Folder " + ChatColor.GREEN + parentName + ChatColor.RED + " was deleted sucessfully due to being empty.");
                                }
                                p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " was renamed successfully.");
                                return true;
                            } catch (IOException e) {
                                e.printStackTrace();
                                p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " could not be renamed.");
                                Helper.removeRenameFolderRequest(p);
                                return false;
                            }
                        } else {
                            p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " could not be renamed.");
                            return false;
                        }
                    } else {
                        p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
                        return false;
                    }
                } else if (args[4].equalsIgnoreCase("deny")) {
                    Helper.removeRenameFolderRequest(p);
                    p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " was not renamed");
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean deepMerge(File oldFile, File newFile) {
        if (Objects.requireNonNull(oldFile.listFiles()).length == 0) {
            return true;
        } else {
            try {
                for (File tempFile : Objects.requireNonNull(oldFile.listFiles())) {
                    if (new File(newFile, tempFile.getName()).exists()) {
                        if (tempFile.isDirectory()) {
                            if (!deepMerge(tempFile, new File(newFile, tempFile.getName()))) {
                                return false;
                            }
                        } else {
                            if (new File(newFile, tempFile.getName()).delete()) {
                                FileUtils.moveToDirectory(tempFile, newFile, true);
                            } else {
                                return false;
                            }
                        }
                    } else {
                        FileUtils.moveToDirectory(tempFile, newFile, true);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}