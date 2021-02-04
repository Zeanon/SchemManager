package de.zeanon.schemmanager.plugin.handlers.tabcompleter;

import de.zeanon.schemmanager.plugin.handlers.SchemManagerTabCompleter;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
class WorldEditModeTabCompleter {

	@NotNull List<String> execute(final @NotNull String message) throws IOException {
		final @NotNull String[] args = message.replace("worldedit:", "/").split(" ");
		final boolean argumentEnded = message.endsWith(" ");
		if (args[0].equalsIgnoreCase("//schem") || args[0].equalsIgnoreCase("//schematic")) {
			if (message.contains("./")) {
				return Collections.emptyList();
			} else {
				boolean deep = false;
				boolean caseSensitive = false;
				int modifierCount = 0;

				if (args.length > 2 && (args[2].equalsIgnoreCase("-deep") || args[2].equalsIgnoreCase("-d"))) {
					deep = true;
					modifierCount++;
				}

				if (args.length > 2 + modifierCount && (args[2 + modifierCount].equalsIgnoreCase("-casesensitive") || args[2 + modifierCount].equalsIgnoreCase("-c"))) {
					caseSensitive = true;
					modifierCount++;
				}

				if (!deep && (args.length > 2 + modifierCount && (args[2 + modifierCount].equalsIgnoreCase("-deep") || args[2 + modifierCount].equalsIgnoreCase("-d")))) {
					deep = true;
					modifierCount++;
				}

				if (modifierCount > 0 && !argumentEnded && args.length == 2 + modifierCount) {
					modifierCount--;
				}

				return WorldEditModeTabCompleter.onTab(args, deep, caseSensitive, modifierCount, argumentEnded);
			}
		} else if (args[0].equalsIgnoreCase("/stoplag")) {
			if (args.length == 1 || (args.length == 2 && !message.endsWith(" ") && "-c".startsWith(args[1]))) {
				return Collections.singletonList("-c");
			} else {
				return Collections.emptyList();
			}
		} else {
			return Collections.emptyList();
		}
	}

	private @NotNull List<String> onTab(final @NotNull String @NotNull [] args, final boolean alreadyDeep, final boolean alreadyCaseSensitive, final int modifierCount, final boolean argumentEnded) throws IOException {
		final @NotNull List<String> completions = new GapList<>();
		if ((args.length == 2 && !argumentEnded) || (args.length == 1 && argumentEnded)) {
			if (argumentEnded) {
				return Arrays.asList("help", "load", "formats", "save", "rename", "renamefolder", "copy", "copyfolder", "del", "delete", "delfolder", "deletefolder", "list", "listfolder", "search", "searchfolder");
			} else {
				return SchemManagerTabCompleter.getCompletions(args[1], "help", "load", "formats", "save", "rename", "renamefolder", "copy", "copyfolder", "del", "delete", "delfolder", "deletefolder", "list", "listfolder", "search", "searchfolder");
			}
		} else if ((args.length == 3 + modifierCount && !argumentEnded) || args.length == 2 + modifierCount) {
			if (argumentEnded) {
				if (!alreadyDeep && (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolder") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder"))) {
					completions.add("-d");
					completions.add("-deep");
				}
				if (!alreadyCaseSensitive && (args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder"))) {
					completions.add("-c");
					completions.add("-casesensitive");
				}

				if (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					final @Nullable Path schemPath = SchemUtils.getSchemPath();
					final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null) {
						for (final @NotNull File file : WorldEditModeTabCompleter.getFileArray(pathFile)) {
							completions.add(file.getName());
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("delfolder") || args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolder") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder") || args[1].equalsIgnoreCase("copyfolder")) {
					final @Nullable Path schemPath = SchemUtils.getSchemPath();
					final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null && pathFile.exists() && pathFile.isDirectory()) {
						for (final @NotNull File file : BaseFileUtils.listFolders(pathFile, false)) {
							completions.add(file.getName());
						}
					}
				}
			} else {
				if (!alreadyDeep && (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolder") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder"))) {
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
					@Nullable Path tempDirectory = SchemUtils.getSchemPath();
					final @NotNull String[] pathArgs = args[2].split("/");
					if (tempDirectory != null) {
						if (!args[2].endsWith("/")) {
							for (int i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (final @NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						final @NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							final @NotNull String sequence = args[2].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
							for (final @NotNull File file : WorldEditModeTabCompleter.getFileArray(pathFile)) {
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("delfolder") || args[1].equalsIgnoreCase("deletefolder") || args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("listfolder") || args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder") || args[1].equalsIgnoreCase("copyfolder")) {
					@Nullable Path tempDirectory = SchemUtils.getSchemPath();
					final @NotNull String[] pathArgs = args[2 + modifierCount].split("/");
					if (tempDirectory != null) {
						if (!args[2 + modifierCount].endsWith("/")) {
							for (int i = 0; i < pathArgs.length - 1; i++) {
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
					final @Nullable Path schemPath = SchemUtils.getSchemPath();
					final @Nullable File pathFile = schemPath != null ? schemPath.toFile() : null;
					if (pathFile != null) {
						for (final @NotNull File file : WorldEditModeTabCompleter.getFileArray(pathFile)) {
							completions.add(file.getName());
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("copyfolder")) {
					final @Nullable Path schemPath = SchemUtils.getSchemPath();
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
						if (extension.toLowerCase().startsWith(args[3]) && !extension.equals(args[3])) {
							completions.add(extension);
						}
					}
				} else if (args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					@Nullable Path tempDirectory = SchemUtils.getSchemPath();
					if (tempDirectory != null) {
						final @NotNull String[] pathArgs = args[3].split("/");
						if (!args[3].endsWith("/")) {
							for (int i = 0; i < pathArgs.length - 1; i++) {
								tempDirectory = tempDirectory.resolve(pathArgs[i]);
							}
						} else {
							for (final @NotNull String pathArg : pathArgs) {
								tempDirectory = tempDirectory.resolve(pathArg);
							}
						}

						final @NotNull File pathFile = tempDirectory.toFile();
						if (pathFile.exists() && pathFile.isDirectory()) {
							final @NotNull String sequence = args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
							for (final @NotNull File file : WorldEditModeTabCompleter.getFileArray(pathFile)) {
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
						}
					}
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("copyfolder")) {
					@Nullable Path tempDirectory = SchemUtils.getSchemPath();
					if (tempDirectory != null) {
						final @NotNull String[] pathArgs = args[3].split("/");
						if (!args[3].endsWith("/")) {
							for (int i = 0; i < pathArgs.length - 1; i++) {
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
								final @NotNull String sequence = args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1];
								WorldEditModeTabCompleter.addFileToCompletions(sequence, completions, file);
							}
						}
					}
				}
			}
		}
		return completions;
	}

	private @NotNull File[] getFileArray(final @NotNull File directory) throws IOException {
		final @Nullable List<String> extensions = ConfigUtils.getStringList("File Extensions");
		final @NotNull Collection<File> rawFiles = BaseFileUtils.listFilesAndFolders(directory, false, Objects.notNull(extensions));
		return rawFiles.toArray(new File[0]);
	}

	private void addFileToCompletions(final @NotNull String sequence, final @NotNull List<String> completions, final @NotNull File file) {
		try {
			if (file.getName().toLowerCase().startsWith(sequence.toLowerCase()) && !file.getName().equalsIgnoreCase(sequence)) {
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