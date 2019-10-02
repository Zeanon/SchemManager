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

    @SuppressWarnings("Duplicates")
    static List<String> onTab(String[] args, String buffer) {
        if (args.length == 1) {
            return Arrays.asList("help", "load", "formats", "save", "rename", "renamfolder", "delete", "deletefolder", "list", "folder", "search", "searchfolder");
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("help") || args[1].equalsIgnoreCase("formats")) {
                return new ArrayList<>();
            } else if (buffer.endsWith(" ") && (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename"))) {
                ArrayList<String> completions = new ArrayList<>();
                File pathFile = new File(Helper.getSchemPath());
                for (File file : getFileArray(pathFile)) {
                    completions.add(file.getName());
                }
                for (File file : DefaultHelper.getFolders(pathFile, false)) {
                    completions.add(file.getName());
                }
                return completions;
            } else {
                ArrayList<String> completions = new ArrayList<>();
                if ("-d".startsWith(args[1].toLowerCase())) {
                    completions.add("-d");
                }
                if ("-deep".startsWith(args[1].toLowerCase())) {
                    completions.add("-deep");
                }
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
                if ("del".startsWith(args[1].toLowerCase())) {
                    completions.add("del");
                }
                if ("deletefolder".startsWith(args[1].toLowerCase())) {
                    completions.add("deletefolder");
                }
                if ("delfolder".startsWith(args[1].toLowerCase())) {
                    completions.add("delfolder");
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
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("help") || args[1].equalsIgnoreCase("formats")) {
                return new ArrayList<>();
            } else if (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename")) {
                ArrayList<String> completions = new ArrayList<>();
                String[] pathArgs = args[2].split("/");
                StringBuilder pathBuilder = new StringBuilder(Helper.getSchemPath());
                for (int i = 0; i < pathArgs.length - 1; i++) {
                    pathBuilder.append(pathArgs[i]).append(DefaultHelper.slash);
                }
                if (args[2].endsWith("/")) {
                    pathBuilder.append(pathArgs[pathArgs.length - 1]).append(DefaultHelper.slash);
                    pathArgs[pathArgs.length - 1] = "";
                }

                File pathFile = new File(pathBuilder.toString());
                if (pathFile.exists() && pathFile.isDirectory()) {
                    for (File file : getFileArray(pathFile)) {
                        addFileToCompletions(pathArgs, completions, file);
                    }
                    for (File file : DefaultHelper.getFolders(pathFile, false)) {
                        addFileToCompletions(pathArgs, completions, file);
                    }
                }
                return completions;
            } else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("delfolder") || args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("folder") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder")) {
                ArrayList<String> completions = new ArrayList<>();
                String[] pathArgs = args[2].split("/");
                StringBuilder pathBuilder = new StringBuilder(Helper.getSchemPath());
                for (int i = 0; i < pathArgs.length - 1; i++) {
                    pathBuilder.append(pathArgs[i]).append(DefaultHelper.slash);
                }
                if (args[2].endsWith("/")) {
                    pathBuilder.append(pathArgs[pathArgs.length - 1]).append(DefaultHelper.slash);
                    pathArgs[pathArgs.length - 1] = "";
                }

                File pathFile = new File(pathBuilder.toString());
                if (pathFile.exists() && pathFile.isDirectory()) {
                    for (File file : DefaultHelper.getFolders(pathFile, false)) {
                        addFileToCompletions(pathArgs, completions, file);
                    }
                }
                return completions;
            }
        }
        return new ArrayList<>();
    }

    private static void addFileToCompletions(String[] pathArgs, ArrayList<String> completions, File file) {
        if ((file.getName().toLowerCase()).startsWith(pathArgs[pathArgs.length - 1].toLowerCase())) {
            String path = file.getAbsolutePath().replaceFirst(Helper.getSchemPath(), "").replaceAll("\\\\", "/");
            completions.add(path);
        }
    }

    private static File[] getFileArray(File directory) {
        String[] extensions = DefaultHelper.getStringList("File Extensions").toArray(new String[0]);

        Collection<File> rawFiles = FileUtils.listFiles(directory, extensions, false);
        File[] files = rawFiles.toArray(new File[0]);
        Arrays.sort(files);
        return files;
    }
}
