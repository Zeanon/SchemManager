package de.zeanon.schemmanager.worldeditmode.listener.tabcompleter;

import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
class WorldEditModeTabCompleter {

	@NotNull
	static List<String> onTab(final @NotNull String[] args, final @NotNull String buffer, final boolean alreadyDeep, final boolean alreadyCaseSensitive, final int modifierCount, final boolean argumentEnded) throws IOException {
		final @NotNull List<String> completions = new GapList<>();
		if ((args.length == 2 + modifierCount && !argumentEnded) || (args.length == 1 + modifierCount && argumentEnded)) {
			if (argumentEnded) {
				return Arrays.asList("help", "load", "formats", "save", "rename", "renamefolder", "copy", "copyfolder", "del", "delete", "delfolder", "deletefolder", "list", "listfolders", "search", "searchfolder");
			} else {
				if ("help".startsWith(args[1]) && !"help".equals(args[1])) {
					completions.add("help");
				}
				if ("load".startsWith(args[1]) && !"load".equals(args[1])) {
					completions.add("load");
				}
				if ("formats".startsWith(args[1]) && !"formats".equals(args[1])) {
					completions.add("formats");
				}
				if ("save".startsWith(args[1]) && !"save".equals(args[1])) {
					completions.add("save");
				}
				if ("rename".startsWith(args[1]) && !"rename".equals(args[1])) {
					completions.add("rename");
				}
				if ("renamefolder".startsWith(args[1]) && !"renamefolder".equals(args[1])) {
					completions.add("renamefolder");
				}
				if ("copy".startsWith(args[1]) && !"copy".equals(args[1])) {
					completions.add("copy");
				}
				if ("copyfolder".startsWith(args[1]) && !"copyfolder".equals(args[1])) {
					completions.add("copyfolder");
				}
				if ("del".startsWith(args[1]) && !"del".equals(args[1])) {
					completions.add("del");
				}
				if ("delete".startsWith(args[1]) && !"delete".equals(args[1])) {
					completions.add("delete");
				}
				if ("delfolder".startsWith(args[1]) && !"delfolder".equals(args[1])) {
					completions.add("delfolder");
				}
				if ("deletefolder".startsWith(args[1]) && !"deletefolder".equals(args[1])) {
					completions.add("deletefolder");
				}
				if ("list".startsWith(args[1]) && !"list".equals(args[1])) {
					completions.add("list");
				}
				if ("listfolders".startsWith(args[1]) && !"listfolders".equals(args[1])) {
					completions.add("listfolders");
				}
				if ("search".startsWith(args[1]) && !"search".equals(args[1])) {
					completions.add("search");
				}
				if ("searchfolder".startsWith(args[1]) && !"searchfolder".equals(args[1])) {
					completions.add("searchfolder");
				}
			}
		} else if ((args.length == 3 + modifierCount && !argumentEnded) || args.length == 2 + modifierCount) {
			if (argumentEnded) {
				if (!alreadyDeep && buffer.endsWith(" ") && (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolders") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder"))) {
					completions.add("-d");
					completions.add("-deep");
				}
				if (!alreadyCaseSensitive && buffer.endsWith(" ") && (args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder"))) {
					completions.add("-c");
					completions.add("-casesensitive");
				}
				if (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					Path schemPath = WorldEditModeSchemUtils.getSchemPath();
					final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null) {
						for (final @NotNull File file : WorldEditModeTabCompleter.getFileArray(pathFile)) {
							completions.add(file.getName());
						}
						for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("delfolder") || args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolders") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder") || args[1].equalsIgnoreCase("copyfolder")) {
					final @NotNull Path schemPath = WorldEditModeSchemUtils.getSchemPath();
					final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null && pathFile.exists() && pathFile.isDirectory()) {
						for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				}
			} else {
				if (!alreadyDeep && (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolders") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder"))) {
					if ("-d".startsWith(args[2 + modifierCount]) && !"-d".equals(args[2 + modifierCount])) {
						completions.add("-d");
					}
					if ("-deep".startsWith(args[2 + modifierCount]) && !"-deep".equals(args[2 + modifierCount])) {
						completions.add("-deep");
					}
				}

				if (!alreadyCaseSensitive && (args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder"))) {
					if ("-c".startsWith(args[2 + modifierCount]) && !"-c".equals(args[2 + modifierCount])) {
						completions.add("-c");
					}
					if ("-casesensitive".startsWith(args[2 + modifierCount]) && !"-casesensitive".equals(args[2 + modifierCount])) {
						completions.add("-casesensitive");
					}
				}
				if (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					@NotNull Path tempDirectory = WorldEditModeSchemUtils.getSchemPath();
					final @NotNull String[] pathArgs = args[2 + modifierCount].split("/");
					if (tempDirectory != null) {
						if (!args[2 + modifierCount].endsWith("/")) {
							for (byte i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (final @NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						final @NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							@NotNull String sequence = args[2 + modifierCount].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
							for (final @NotNull File file : WorldEditModeTabCompleter.getFileArray(pathFile)) {
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
							for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("delfolder") || args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolders") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder") || args[1].equalsIgnoreCase("copyfolder")) {
					@NotNull Path tempDirectory = WorldEditModeSchemUtils.getSchemPath();
					final @NotNull String[] pathArgs = args[2 + modifierCount].split("/");
					if (tempDirectory != null) {
						if (!args[2 + modifierCount].endsWith("/")) {
							for (byte i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (final @NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						final @NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
								final @NotNull String sequence = args[2 + modifierCount].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
						}
					}
				}
			}
		} else if ((args.length == 4 + modifierCount && !argumentEnded) || args.length == 3 + modifierCount) {
			if (argumentEnded) {
				if (args[1].equalsIgnoreCase("load")) {
					completions.addAll(Objects.notNull(ConfigUtils.getStringList("File Extensions")));
				} else if (args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					final @NotNull Path schemPath = WorldEditModeSchemUtils.getSchemPath();
					final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null) {
						for (final @NotNull File file : WorldEditModeTabCompleter.getFileArray(pathFile)) {
							completions.add(file.getName());
						}
						for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("copyfolder")) {
					final @NotNull Path schemPath = WorldEditModeSchemUtils.getSchemPath();
					final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null && pathFile.exists() && pathFile.isDirectory()) {
						for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				}
			} else {
				if (args[1].equalsIgnoreCase("load")) {
					for (final @NotNull String extension : Objects.notNull(ConfigUtils.getStringList("File Extensions"))) {
						if (extension.toLowerCase().startsWith(args[3 + modifierCount]) && !extension.equals(args[3 + modifierCount])) {
							completions.add(extension);
						}
					}
				} else if (args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					@NotNull Path tempDirectory = WorldEditModeSchemUtils.getSchemPath();
					if (tempDirectory != null) {
						final @NotNull String[] pathArgs = args[3 + modifierCount].split("/");
						if (!args[3 + modifierCount].endsWith("/")) {
							for (byte i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (final @NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						final @NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							final @NotNull String sequence = args[3 + modifierCount].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
							for (final @NotNull File file : WorldEditModeTabCompleter.getFileArray(pathFile)) {
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
							for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("copyfolder")) {
					@NotNull Path tempDirectory = WorldEditModeSchemUtils.getSchemPath();
					if (tempDirectory != null) {
						final @NotNull String[] pathArgs = args[3 + modifierCount].split("/");
						if (!args[3 + modifierCount].endsWith("/")) {
							for (byte i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (final @NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						final @NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
								final @NotNull String sequence = args[3 + modifierCount].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
						}
					}
				}
			}
		}
		return completions;
	}

	@NotNull
	private static File[] getFileArray(final @NotNull File directory) throws IOException {
		final @Nullable List<String> extensions = ConfigUtils.getStringList("File Extensions");
		final @NotNull Collection<File> rawFiles = BaseFileUtils.listFiles(directory, false, Objects.notNull(extensions));
		return rawFiles.toArray(new File[0]);
	}

	private static void addFileToCompletions(final @NotNull String sequence, final @NotNull List<String> completions, final @NotNull File file) {
		try {
			if (file.getName().toLowerCase().startsWith(sequence.toLowerCase()) && !file.getName().equalsIgnoreCase(sequence)) {
				final @NotNull Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				if (schemPath != null) {
					final @NotNull String path = FilenameUtils.separatorsToUnix(schemPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
					completions.add(path);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}