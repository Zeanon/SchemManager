package de.zeanon.schemmanager.plugin.handlers.tabcompleter;

import de.zeanon.schemmanager.plugin.handlers.SchemManagerTabCompleter;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.storagemanagercore.external.browniescollections.GapList;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
class WorldEditModeTabCompleter {

	@NotNull List<String> execute(final @NotNull String message) throws IOException {
		final @NotNull String[] args = message.toLowerCase().split(" ");
		final boolean argumentEnded = message.endsWith(" ");
		if (args[0].equals("//schem") || args[0].equals("//schematic")) {
			if (message.contains("./")) {
				return Collections.emptyList();
			} else {
				boolean deep = false;
				boolean caseSensitive = false;
				int modifierCount = 0;

				if (args.length > 2 && (args[2].equals("-deep") || args[2].equals("-d"))) {
					deep = true;
					modifierCount++;
				}

				if (args.length > 2 + modifierCount && (args[2 + modifierCount].equals("-casesensitive") || args[2 + modifierCount].equals("-c"))) {
					caseSensitive = true;
					modifierCount++;
				}

				if (!deep && (args.length > 2 + modifierCount && (args[2 + modifierCount].equals("-deep") || args[2 + modifierCount].equals("-d")))) {
					deep = true;
					modifierCount++;
				}

				if (modifierCount > 0 && !argumentEnded && args.length == 2 + modifierCount) {
					modifierCount--;
				}

				return WorldEditModeTabCompleter.onTab(args, deep, caseSensitive, modifierCount, argumentEnded);
			}
		} else {
			return Collections.emptyList();
		}
	}

	private @NotNull List<String> onTab(final @NotNull String @NotNull [] args, final boolean alreadyDeep, final boolean alreadyCaseSensitive, final int modifierCount, final boolean argumentEnded) throws IOException {
		final @NotNull List<String> completions = new GapList<>();
		if ((args.length == 2 && !argumentEnded) || (args.length == 1 && argumentEnded)) {
			if (argumentEnded) {
				return Arrays.asList("help", "load", "formats", "save", "rename", "renamefolder", "copy", "copyfolder", "del", "delete", "delfolder", "deletefolder", "list", "listfolder", "search", "searchfolder", "download");
			} else {
				return SchemManagerTabCompleter.getCompletions(args[1], "help", "load", "formats", "save", "rename", "renamefolder", "copy", "copyfolder", "del", "delete", "delfolder", "deletefolder", "list", "listfolder", "search", "searchfolder", "download");
			}
		} else if ((args.length == 3 + modifierCount && !argumentEnded) || args.length == 2 + modifierCount) {
			if (argumentEnded) {
				if (!alreadyDeep && (args[1].equals("list") || args[1].equals("listfolder") || args[1].equals("search") || args[1].equals("searchfolder"))) {
					completions.add("-d");
					completions.add("-deep");
				}

				if (!alreadyCaseSensitive && (args[1].equals("search") || args[1].equals("searchfolder"))) {
					completions.add("-c");
					completions.add("-casesensitive");
				}

				if (args[1].equals("load") || args[1].equals("save") || args[1].equals("del") || args[1].equals("delete") || args[1].equals("rename") || args[1].equals("copy")) {
					final @Nullable Path schemPath = SchemUtils.getSchemPath();
					final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null) {
						for (final @NotNull File file : WorldEditModeTabCompleter.getFileList(pathFile)) {
							completions.add(file.getName());
						}
					}
				} else if (args[1].equals("renamefolder") || args[1].equals("delfolder") || args[1].equals("deletefolder") || args[1].equals("list") || args[1].equals("listfolder") || args[1].equals("search") || args[1].equals("searchfolder") || args[1].equals("copyfolder")) {
					final @Nullable Path schemPath = SchemUtils.getSchemPath();
					final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null && pathFile.exists() && pathFile.isDirectory()) {
						for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				}
			} else {
				if (!alreadyDeep && (args[1].equals("list") || args[1].equals("listfolder") || args[1].equals("search") || args[1].equals("searchfolder"))) {
					if ("-d".startsWith(args[2 + modifierCount]) && !"-d".equals(args[2 + modifierCount])) {
						completions.add("-d");
					}
					if ("-deep".startsWith(args[2 + modifierCount]) && !"-deep".equals(args[2 + modifierCount])) {
						completions.add("-deep");
					}
				}

				if (!alreadyCaseSensitive && (args[1].equals("search") || args[1].equals("searchfolder"))) {
					if ("-c".startsWith(args[2 + modifierCount]) && !"-c".equals(args[2 + modifierCount])) {
						completions.add("-c");
					}
					if ("-casesensitive".startsWith(args[2 + modifierCount]) && !"-casesensitive".equals(args[2 + modifierCount])) {
						completions.add("-casesensitive");
					}
				}

				if (args[1].equals("load") || args[1].equals("save") || args[1].equals("del") || args[1].equals("delete") || args[1].equals("rename") || args[1].equals("copy")) {
					@Nullable Path tempDirectory = SchemUtils.getSchemPath();
					final @NotNull String[] pathArgs = args[2].split("/");
					if (tempDirectory != null) {
						if (!args[2].endsWith("/")) {
							for (int i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							tempDirectory = tempDirectory.resolve(args[2]);
						}

						final @NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							final @NotNull String sequence = args[2].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
							for (final @NotNull File file : WorldEditModeTabCompleter.getFileList(pathFile)) {
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
						}
					}
				} else if (args[1].equals("renamefolder") || args[1].equals("delfolder") || args[1].equals("deletefolder") || args[1].equals("list") || args[1].equals("listfolder") || args[1].equals("search") || args[1].equals("searchfolder") || args[1].equals("copyfolder")) {
					@Nullable Path tempDirectory = SchemUtils.getSchemPath();
					final @NotNull String[] pathArgs = args[2 + modifierCount].split("/");
					if (tempDirectory != null) {
						if (!args[2 + modifierCount].endsWith("/")) {
							for (int i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							tempDirectory = tempDirectory.resolve(args[2 + modifierCount].substring(0, args[2 + modifierCount].length() - 1));
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
				switch (args[1]) {
					case "load":
						completions.addAll(Objects.notNull(ConfigUtils.getStringList("File Extensions")));
						break;
					case "rename":
					case "copy":
					case "download": {
						final @Nullable Path schemPath = SchemUtils.getSchemPath();
						final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
						if (pathFile != null) {
							for (final @NotNull File file : WorldEditModeTabCompleter.getFileList(pathFile)) {
								completions.add(file.getName());
							}
						}
						break;
					}
					case "renamefolder":
					case "copyfolder": {
						final @Nullable Path schemPath = SchemUtils.getSchemPath();
						final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
						if (pathFile != null && pathFile.exists() && pathFile.isDirectory()) {
							for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
								completions.add(file.getName());
							}
						}
						break;
					}
					default:
						break;
				}
			} else {
				switch (args[1]) {
					case "load":
						for (final @NotNull String extension : Objects.notNull(ConfigUtils.getStringList("File Extensions"))) {
							if (extension.toLowerCase().startsWith(args[3]) && !extension.equals(args[3])) {
								completions.add(extension);
							}
						}
						break;
					case "rename":
					case "copy":
					case "download": {
						@Nullable Path tempDirectory = SchemUtils.getSchemPath();
						if (tempDirectory != null) {
							final @NotNull String[] pathArgs = args[3].split("/");
							if (!args[3].endsWith("/")) {
								for (int i = 0; i < pathArgs.length - 1; i++) {
									tempDirectory = tempDirectory.resolve(pathArgs[i]);
								}
							} else {
								tempDirectory = tempDirectory.resolve(args[3].substring(0, args[3].length() - 1));
							}

							final @NotNull File pathFile = tempDirectory.toFile();
							if (pathFile.exists() && pathFile.isDirectory()) {
								final @NotNull String sequence = args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
								for (final @NotNull File file : WorldEditModeTabCompleter.getFileList(pathFile)) {
									WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
								}
							}
						}
						break;
					}
					case "renamefolder":
					case "copyfolder": {
						@Nullable Path tempDirectory = SchemUtils.getSchemPath();
						if (tempDirectory != null) {
							final @NotNull String[] pathArgs = args[3].split("/");
							if (!args[3].endsWith("/")) {
								for (int i = 0; i < pathArgs.length - 1; i++) {
									tempDirectory = tempDirectory.resolve(pathArgs[i]);
								}
							} else {
								tempDirectory = tempDirectory.resolve(args[3].substring(0, args[3].length() - 1));
							}

							final @NotNull File pathFile = tempDirectory.toFile();
							if (pathFile.exists() && pathFile.isDirectory()) {
								for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
									final @NotNull String sequence = args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
									WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
								}
							}
						}
						break;
					}
					default:
						break;
				}
			}
		}
		return completions;
	}

	private @NotNull List<File> getFileList(final @NotNull File directory) throws IOException {
		final @Nullable List<String> extensions = ConfigUtils.getStringList("File Extensions");
		final @NotNull List<File> rawFiles = BaseFileUtils.listFilesOfTypeAndFolders(directory, false, Objects.notNull(extensions));
		return rawFiles;
	}

	private void addFileToCompletions(final @NotNull String sequence, final @NotNull List<String> completions, final @NotNull File file) {
		try {
			if (file.getName().toLowerCase().startsWith(sequence.toLowerCase()) && !file.getName().equals(sequence)) {
				final @Nullable Path schemPath = SchemUtils.getSchemPath();
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