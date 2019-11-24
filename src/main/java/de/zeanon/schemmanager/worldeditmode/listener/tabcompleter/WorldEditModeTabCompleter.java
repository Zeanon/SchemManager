package de.zeanon.schemmanager.worldeditmode.listener.tabcompleter;

import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storage.internal.utility.utils.basic.BaseFileUtils;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
class WorldEditModeTabCompleter {

	@NotNull
	static List<String> onTab(final @NotNull String[] args, final @NotNull String buffer, final boolean alreadyDeep, final boolean argumentEnded) {
		@NotNull ArrayList<String> completions = new ArrayList<>();
		if ((args.length == 2 && !argumentEnded) || (args.length == 1 && argumentEnded)) {
			if (argumentEnded) {
				return Arrays.asList("help", "load", "formats", "save", "rename", "renamefolder", "copy", "copyfolder", "del", "delete", "delfolder", "deletefolder", "list", "listfolder", "search", "searchfolder");
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
				if ("listfolder".startsWith(args[1]) && !"listfolder".equals(args[1])) {
					completions.add("listfolder");
				}
				if ("search".startsWith(args[1]) && !"search".equals(args[1])) {
					completions.add("search");
				}
				if ("searchfolder".startsWith(args[1]) && !"searchfolder".equals(args[1])) {
					completions.add("searchfolder");
				}
			}
		} else if ((args.length == 3 && !argumentEnded) || args.length == 2) {
			if (argumentEnded) {
				if (!alreadyDeep && buffer.endsWith(" ") && (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolder") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder"))) {
					completions.add("-d");
					completions.add("-deep");
				}
				if (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					Path schemPath = WorldEditModeSchemUtils.getSchemPath();
					@Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null) {
						for (@NotNull File file : getFileArray(pathFile)) {
							completions.add(file.getName());
						}
						for (@NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("delfolder") || args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolder") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder") || args[1].equalsIgnoreCase("copyfolder")) {
					Path schemPath = WorldEditModeSchemUtils.getSchemPath();
					@Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null && pathFile.exists() && pathFile.isDirectory()) {
						for (@NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				}
			} else {
				if (!alreadyDeep && (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolder") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder"))) {
					if ("-d".startsWith(args[2]) && !"-d".equals(args[2])) {
						completions.add("-d");
					}
					if ("-deep".startsWith(args[2]) && !"-deep".equals(args[2])) {
						completions.add("-deep");
					}
				}
				if (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					Path tempDirectory = WorldEditModeSchemUtils.getSchemPath();
					@NotNull String[] pathArgs = args[2].split("/");
					if (tempDirectory != null) {
						if (!args[2].endsWith("/")) {
							for (byte i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (@NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						@NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							@NotNull String regex = args[2].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
							for (@NotNull File file : getFileArray(pathFile)) {
								addFileToCompletions(regex, completions, file);
							}
							for (@NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
								addFileToCompletions(regex, completions, file);
							}
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("delfolder") || args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolder") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder") || args[1].equalsIgnoreCase("copyfolder")) {
					Path tempDirectory = WorldEditModeSchemUtils.getSchemPath();
					@NotNull String[] pathArgs = args[2].split("/");
					if (tempDirectory != null) {
						if (!args[2].endsWith("/")) {
							for (byte i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (@NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						@NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							for (@NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
								@NotNull String regex = args[2].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
								addFileToCompletions(regex, completions, file);
							}
						}
					}
				}
			}
		} else if ((args.length == 4 && !argumentEnded) || args.length == 3) {
			if (argumentEnded) {
				if (args[1].equalsIgnoreCase("load")) {
					completions.addAll(Objects.notNull(ConfigUtils.getStringList("File Extensions")));
				} else if (args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					Path schemPath = WorldEditModeSchemUtils.getSchemPath();
					@Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null) {
						for (@NotNull File file : getFileArray(pathFile)) {
							completions.add(file.getName());
						}
						for (@NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("copyfolder")) {
					Path schemPath = WorldEditModeSchemUtils.getSchemPath();
					@Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null && pathFile.exists() && pathFile.isDirectory()) {
						for (@NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				}
			} else {
				if (args[1].equalsIgnoreCase("load")) {
					for (@NotNull String extension : Objects.notNull(ConfigUtils.getStringList("File Extensions"))) {
						if (extension.toLowerCase().startsWith(args[3]) && !extension.equals(args[3])) {
							completions.add(extension);
						}
					}
				} else if (args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					Path tempDirectory = WorldEditModeSchemUtils.getSchemPath();
					if (tempDirectory != null) {
						@NotNull String[] pathArgs = args[3].split("/");
						if (!args[3].endsWith("/")) {
							for (byte i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (@NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						@NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							@NotNull String regex = args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
							for (@NotNull File file : getFileArray(pathFile)) {
								addFileToCompletions(regex, completions, file);
							}
							for (@NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
								addFileToCompletions(regex, completions, file);
							}
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("copyfolder")) {
					Path tempDirectory = WorldEditModeSchemUtils.getSchemPath();
					if (tempDirectory != null) {
						@NotNull String[] pathArgs = args[3].split("/");
						if (!args[3].endsWith("/")) {
							for (byte i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (@NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						@NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							for (@NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
								@NotNull String regex = args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
								addFileToCompletions(regex, completions, file);
							}
						}
					}
				}
			}
		}
		return completions;
	}

	@NotNull
	private static File[] getFileArray(final @NotNull File directory) {
		@Nullable List<String> extensions = ConfigUtils.getStringList("File Extensions");
		@NotNull Collection<File> rawFiles = BaseFileUtils.listFiles(directory, Objects.notNull(extensions), false);
		return rawFiles.toArray(new File[0]);
	}

	private static void addFileToCompletions(final @NotNull String regex, final @NotNull ArrayList<String> completions, final @NotNull File file) {
		try {
			if (file.getName().toLowerCase().startsWith(regex.toLowerCase()) && !file.getName().equalsIgnoreCase(regex)) {
				Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				if (schemPath != null) {
					String path = FilenameUtils.separatorsToUnix(schemPath.toRealPath().relativize(file.toPath().toRealPath()).toString());
					completions.add(path);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}