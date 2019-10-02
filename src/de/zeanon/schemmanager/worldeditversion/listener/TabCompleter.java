package de.zeanon.schemmanager.worldeditversion.listener;

import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class TabCompleter {

    static List<String> onTab(String[] args, String buffer) {
        if (args.length == 1) {
            return Arrays.asList("help", "load", "formats", "save", "rename", "renamfolder", "delete", "deletefolder", "list", "folder", "search", "searchfolder");
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("help") || args[1].equalsIgnoreCase("formats")) {
                return new ArrayList<>();
            } else if (buffer.endsWith(" ") && (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename"))) {
                ArrayList<String> completions = new ArrayList<>();
                String schemFolderPath = Helper.getSchemPath();
                for (File file : getFileArray(schemFolderPath)) {
                    completions.add(file.getName());
                }
                for (File file : DefaultHelper.getFolders(new File(schemFolderPath), false)) {
                    completions.add(file.getName());
                }
                return completions;
            } else {
                ArrayList<String> completions = new ArrayList<>();
                if ("help".startsWith(args[1].toLowerCase())) {
                    completions.add("help");
                }
                if ("load".startsWith(args[1].toLowerCase())) {
                    completions.add("load");
                }
                if ("formats".startsWith(args[1].toLowerCase())) {
                    completions.add("formats");
                }
                if ("save".startsWith(args[1].toLowerCase())) {
                    completions.add("save");
                }
                if ("rename".startsWith(args[1].toLowerCase())) {
                    completions.add("rename");
                }
                if ("renamefolder".startsWith(args[1].toLowerCase())) {
                    completions.add("renamefolder");
                }
                if ("delete".startsWith(args[1].toLowerCase())) {
                    completions.add("delete");
                }
                if ("deletefolder".startsWith(args[1].toLowerCase())) {
                    completions.add("deletefolder");
                }
                if ("list".startsWith(args[1].toLowerCase())) {
                    completions.add("list");
                }
                if ("folder".startsWith(args[1].toLowerCase())) {
                    completions.add("folder");
                }
                if ("search".startsWith(args[1].toLowerCase())) {
                    completions.add("search");
                }
                if ("searchfolder".startsWith(args[1].toLowerCase())) {
                    completions.add("searchfolder");
                }
                return completions;
            }
        } else if (args.length >= 3) {
            if (args[1].equalsIgnoreCase("help") || args[1].equalsIgnoreCase("formats")) {
                return new ArrayList<>();
            } else if (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename")) {
                if (args[2].endsWith("/")) {
                    args[2] += " ";
                }
                String[] pathArgs = args[2].split("/");
                ArrayList<String> completions = new ArrayList<>();
                StringBuilder pathBuilder = new StringBuilder(Helper.getSchemPath());
                for (int i = 0; i < pathArgs.length - 1; i++) {
                    pathBuilder.append(pathArgs[i]).append(DefaultHelper.slash);
                }
                String schemFolderPath = pathBuilder.toString();
                System.out.println(schemFolderPath);
                for (File file : getFileArray(schemFolderPath)) {
                    getFiles(pathArgs, completions, schemFolderPath, file);
                }
                for (File file : DefaultHelper.getFolders(new File(schemFolderPath), false)) {
                    getFiles(pathArgs, completions, schemFolderPath, file);
                }
                return completions;

            }
        }
        return new ArrayList<>();
    }

    private static void getFiles(String[] pathArgs, ArrayList<String> completions, String schemFolderPath, File file) {
        System.out.println(file.getName());
        if ((" " + file.getName().toLowerCase()).startsWith(pathArgs[pathArgs.length - 1].toLowerCase())) {
            String path = file.getAbsolutePath().replaceFirst(schemFolderPath, "").replaceAll("\\\\", "/");
            completions.add(path);
        }
    }

    private static File[] getFileArray(String path) {
        String[] extensions = DefaultHelper.getStringList("File Extensions").toArray(new String[0]);
        File directory = new File(path);

        Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, false);
        File[] files = rawFiles.toArray(new File[0]);
        Arrays.sort(files);
        return files;
    }
}
