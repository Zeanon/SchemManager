package de.zeanon.schemmanager.worldeditversion.listener;

import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.utils.Helper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class TabCompleter {

    @SuppressWarnings("Duplicates")
    static List<String> onTab(String[] args, String buffer, boolean alreadyDeep, boolean argumentEnded) {
        try {
            ArrayList<String> completions = new ArrayList<>();
            if ((args.length == 2 && !argumentEnded) || (args.length == 1 && argumentEnded)) {
                if (argumentEnded) {
                    return Arrays.asList("help", "load", "formats", "save", "rename", "renamfolder", "del", "delete", "delfolder", "deletefolder", "list", "folder", "search", "searchfolder");
                } else {
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
                    if ("del".startsWith(args[1].toLowerCase())) {
                        completions.add("del");
                    }
                    if ("delete".startsWith(args[1].toLowerCase())) {
                        completions.add("delete");
                    }
                    if ("delfolder".startsWith(args[1].toLowerCase())) {
                        completions.add("delfolder");
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
                }
            } else if ((args.length == 3 && !argumentEnded) || args.length == 2) {
                if (argumentEnded) {
                    if (!alreadyDeep && buffer.endsWith(" ") && (args[1].equals("list") || args[1].equals("folder") || args[1].equals("search") || args[1].equals("searchfolder"))) {
                        completions.add("-d");
                        completions.add("-deep");
                    }
                    if (args[1].equals("load") || args[1].equals("save") || args[1].equals("del") || args[1].equals("delete") || args[1].equals("rename")) {
                        File pathFile = Helper.getSchemPath().toFile();
                        for (File file : getFileArray(pathFile)) {
                            completions.add(file.getName());
                        }
                        for (File file : DefaultHelper.getFolders(pathFile, false)) {
                            completions.add(file.getName());
                        }
                    } else if (args[1].equals("renamefolder") || args[1].equals("delfolder") || args[1].equals("deletefolder") || args[1].equals("list") || args[1].equals("folder") || args[1].equals("search") || args[1].equals("searchfolder")) {
                        File pathFile = Helper.getSchemPath().toFile();
                        if (pathFile.exists() && pathFile.isDirectory()) {
                            for (File file : DefaultHelper.getFolders(pathFile, false)) {
                                completions.add(file.getName());
                            }
                        }
                    }
                } else {
                    if (!alreadyDeep && (args[1].equals("list") || args[1].equals("folder") || args[1].equals("search") || args[1].equals("searchfolder"))) {
                        if ("-d".startsWith(args[2])) {
                            completions.add("-d");
                        }
                        if ("-deep".startsWith(args[2])) {
                            completions.add("-deep");
                        }
                    }
                    if (args[1].equals("load") || args[1].equals("save") || args[1].equals("del") || args[1].equals("delete") || args[1].equals("rename")) {
                        Path tempDirectory = Helper.getSchemPath();
                        String[] pathArgs = args[2].split("/");
                        if (!args[2].endsWith("/")) {
                            for (int i = 0; i < pathArgs.length - 1; i++) {
                                tempDirectory = tempDirectory.resolve(pathArgs[i]);
                            }
                        } else {
                            for (String pathArg : pathArgs) {
                                tempDirectory = tempDirectory.resolve(pathArg);
                            }
                        }

                        File pathFile = tempDirectory.toFile();
                        if (pathFile.exists() && pathFile.isDirectory()) {
                            String regex = args[2].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
                            for (File file : getFileArray(pathFile)) {
                                addFileToCompletions(regex, completions, file);
                            }
                            for (File file : DefaultHelper.getFolders(pathFile, false)) {
                                addFileToCompletions(regex, completions, file);
                            }
                        }
                    } else if (args[1].equals("renamefolder") || args[1].equals("delfolder") || args[1].equals("deletefolder") || args[1].equals("list") || args[1].equals("folder") || args[1].equals("search") || args[1].equals("searchfolder")) {
                        Path tempDirectory = Helper.getSchemPath();
                        String[] pathArgs = args[2].split("/");
                        if (!args[2].endsWith("/")) {
                            for (int i = 0; i < pathArgs.length - 1; i++) {
                                tempDirectory = tempDirectory.resolve(pathArgs[i]);
                            }
                        } else {
                            for (String pathArg : pathArgs) {
                                tempDirectory = tempDirectory.resolve(pathArg);
                            }
                        }

                        File pathFile = tempDirectory.toFile();
                        if (pathFile.exists() && pathFile.isDirectory()) {
                            for (File file : DefaultHelper.getFolders(pathFile, false)) {
                                String regex = args[2].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
                                addFileToCompletions(regex, completions, file);
                            }
                        }
                    }
                }
            } else if ((args.length == 4 && !argumentEnded) || args.length == 3) {
                if (argumentEnded) {
                    switch (args[1]) {
                        case "load":
                            completions.addAll(DefaultHelper.getStringList("File Extensions"));
                            break;
                        case "rename": {
                            File pathFile = Helper.getSchemPath().toFile();
                            for (File file : getFileArray(pathFile)) {
                                completions.add(file.getName());
                            }
                            for (File file : DefaultHelper.getFolders(pathFile, false)) {
                                completions.add(file.getName());
                            }
                            break;
                        }
                        case "renamefolder": {
                            File pathFile = Helper.getSchemPath().toFile();
                            if (pathFile.exists() && pathFile.isDirectory()) {
                                for (File file : DefaultHelper.getFolders(pathFile, false)) {
                                    completions.add(file.getName());
                                }
                            }
                            break;
                        }
                    }
                } else {
                    switch (args[1]) {
                        case "load":
                            for (String extension : DefaultHelper.getStringList("File Extensions")) {
                                if ((extension + " ").toLowerCase().startsWith(args[3].toLowerCase())) {
                                    completions.add(extension);
                                }
                            }
                            break;
                        case "rename": {
                            Path tempDirectory = Helper.getSchemPath();
                            String[] pathArgs = args[3].split("/");
                            if (!args[3].endsWith("/")) {
                                for (int i = 0; i < pathArgs.length - 1; i++) {
                                    tempDirectory = tempDirectory.resolve(pathArgs[i]);
                                }
                            } else {
                                for (String pathArg : pathArgs) {
                                    tempDirectory = tempDirectory.resolve(pathArg);
                                }
                            }

                            File pathFile = tempDirectory.toFile();
                            if (pathFile.exists() && pathFile.isDirectory()) {
                                String regex = args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
                                for (File file : getFileArray(pathFile)) {
                                    addFileToCompletions(regex, completions, file);
                                }
                                for (File file : DefaultHelper.getFolders(pathFile, false)) {
                                    addFileToCompletions(regex, completions, file);
                                }
                            }
                            break;
                        }
                        case "renamefolder": {
                            Path tempDirectory = Helper.getSchemPath();
                            String[] pathArgs = args[3].split("/");
                            if (!args[3].endsWith("/")) {
                                for (int i = 0; i < pathArgs.length - 1; i++) {
                                    tempDirectory = tempDirectory.resolve(pathArgs[i]);
                                }
                            } else {
                                for (String pathArg : pathArgs) {
                                    tempDirectory = tempDirectory.resolve(pathArg);
                                }
                            }

                            File pathFile = tempDirectory.toFile();
                            if (pathFile.exists() && pathFile.isDirectory()) {
                                for (File file : DefaultHelper.getFolders(pathFile, false)) {
                                    String regex = args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
                                    addFileToCompletions(regex, completions, file);
                                }
                            }
                            break;
                        }
                    }
                }
            }
            return completions;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static void addFileToCompletions(String regex, ArrayList<String> completions, File file) {
        try {
            if (((file.getName() + " ").toLowerCase()).startsWith(regex.toLowerCase())) {
                String path = FilenameUtils.separatorsToUnix(DefaultHelper.removeExtension(Helper.getSchemPath().relativize(file.toPath().toRealPath()).toString()));
                completions.add(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
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