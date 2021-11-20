package de.zeanon.schemmanager.plugin.handlers.tabcompleter;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.session.SessionManager;
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
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class SchematicTabCompleter {

	public @NotNull List<String> getCompletions(final @NotNull CommandSender sender, final @NotNull String message) throws IOException {
		final @NotNull String[] args = message.split(" ");
		final boolean argumentEnded = message.endsWith(" ");
		if (args[0].equals("//schem")
			|| args[0].equals("//schematic")
			|| args[0].equals("/schem")
			|| args[0].equals("/schematic")) {
			if (message.contains("./")
				|| message.contains(".\\")) {
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

				return SchematicTabCompleter.generateCompletions(args, deep, caseSensitive, modifierCount, argumentEnded);
			}
		} else if ((args[0].equals("//session")
					|| args[0].equals("/session"))
				   && sender instanceof Player) {
			return SchematicTabCompleter.sessionCompletions(new BukkitPlayer((Player) sender), args, argumentEnded);
		} else {
			return Collections.emptyList();
		}
	}

	public static @NotNull List<String> getCompletions(final @NotNull String arg, final @NotNull String... completions) {
		final List<String> result = new GapList<>();
		for (final @NotNull String completion : completions) {
			if (completion.startsWith(arg.toLowerCase()) && !completion.equalsIgnoreCase(arg)) {
				result.add(completion);
			}
		}
		return result;
	}

	private @NotNull List<String> sessionCompletions(final @NotNull BukkitPlayer p, final @NotNull String[] args, final boolean argumentEnded) {
		if ((args.length == 2 && !argumentEnded) || (args.length == 1 && argumentEnded)) {
			if (argumentEnded) {
				return Arrays.asList("delete", "list", "search", "load", "save", "swap");
			} else {
				return SchematicTabCompleter.getCompletions(args[1], "delete", "list", "search", "load", "save", "swap");
			}
		} else if ((args.length == 3 && !argumentEnded) || args.length == 2) {
			if (argumentEnded) {
				if (args[1].equalsIgnoreCase("delete")
					|| args[1].equalsIgnoreCase("load")
					|| args[1].equalsIgnoreCase("save")
					|| args[1].equalsIgnoreCase("swap")) {
					final @Nullable Map<String, SessionManager.SessionHolder> sessions = WorldEdit.getInstance().getSessionManager().listSessions(p);
					return sessions == null ? Collections.emptyList()
											: sessions.keySet()
													  .stream()
													  .filter(name -> !name.equals("current"))
													  .collect(Collectors.toList());
				}
			} else {
				if (args[1].equalsIgnoreCase("delete")
					|| args[1].equalsIgnoreCase("load")
					|| args[1].equalsIgnoreCase("save")
					|| args[1].equalsIgnoreCase("swap")) {
					final @Nullable Map<String, SessionManager.SessionHolder> sessions = WorldEdit.getInstance().getSessionManager().listSessions(p);
					return sessions == null ? Collections.emptyList()
											: sessions.keySet()
													  .stream()
													  .filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase())
																	  && !name.equals("current"))
													  .collect(Collectors.toList());
				}
			}
		}
		return Collections.emptyList();
	}

	private @NotNull List<String> generateCompletions(final @NotNull String[] args, final boolean alreadyDeep, final boolean alreadyCaseSensitive, final int modifierCount, final boolean argumentEnded) throws IOException {
		final @NotNull List<String> completions = new GapList<>();
		if ((args.length == 2 && !argumentEnded) || (args.length == 1 && argumentEnded)) {
			if (argumentEnded) {
				return Arrays.asList("help", "load", "formats", "save", "rename", "renamefolder", "copy", "copyfolder", "del", "delete", "delfolder", "deletefolder", "list", "listschems", "listfolders", "search", "searchschem", "searchfolder", "download");
			} else {
				return SchematicTabCompleter.getCompletions(args[1], "help", "load", "formats", "save", "rename", "renamefolder", "copy", "copyfolder", "del", "delete", "delfolder", "deletefolder", "list", "listschems", "listfolders", "search", "searchschem", "searchfolder", "download");
			}
		} else if ((args.length == 3 + modifierCount && !argumentEnded) || (args.length == 2 + modifierCount && argumentEnded)) {
			if (argumentEnded) {
				if (!alreadyDeep && (args[1].equalsIgnoreCase("list")
									 || args[1].equalsIgnoreCase("listschems")
									 || args[1].equalsIgnoreCase("listfolders")
									 || args[1].equalsIgnoreCase("search")
									 || args[1].equalsIgnoreCase("searchschem")
									 || args[1].equalsIgnoreCase("searchfolder"))) {
					completions.add("-d");
					completions.add("-deep");
				}

				if (!alreadyCaseSensitive && (args[1].equalsIgnoreCase("search") || args[1].equalsIgnoreCase("searchfolder") || args[1].equalsIgnoreCase("searchschem"))) {
					completions.add("-c");
					completions.add("-casesensitive");
				}

				if (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy")) {
					SchematicTabCompleter.addFilesToCompletions(null, completions, SchematicTabCompleter.getFileList(SchemUtils.getSchemFolder()));
				} else if (args[1].equalsIgnoreCase("renamefolder")
						   || args[1].equalsIgnoreCase("delfolder")
						   || args[1].equalsIgnoreCase("deletefolder")
						   || args[1].equalsIgnoreCase("list")
						   || args[1].equalsIgnoreCase("listschems")
						   || args[1].equalsIgnoreCase("listfolders")
						   || args[1].equalsIgnoreCase("search")
						   || args[1].equalsIgnoreCase("searchschem")
						   || args[1].equalsIgnoreCase("searchfolder")
						   || args[1].equalsIgnoreCase("copyfolder")) {
					final @NotNull File pathFile = SchemUtils.getSchemFolder();
					if (pathFile.exists() && pathFile.isDirectory()) {
						SchematicTabCompleter.addFilesToCompletions(null, completions, Objects.notNull(BaseFileUtils.listFolders(pathFile, false)));
					}
				}
			} else {
				if (!alreadyDeep && (args[1].equalsIgnoreCase("list")
									 || args[1].equalsIgnoreCase("listschems")
									 || args[1].equalsIgnoreCase("listfolders")
									 || args[1].equalsIgnoreCase("search")
									 || args[1].equalsIgnoreCase("searchschem")
									 || args[1].equalsIgnoreCase("searchfolder"))) {
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

				@Nullable Path tempDirectory = SchemUtils.getSchemPath();
				final @NotNull String[] pathArgs = args[2 + modifierCount].split("/");
				if (!args[2 + modifierCount].endsWith("/")) {
					for (int i = 0; i < pathArgs.length - 1; i++) {
						tempDirectory = tempDirectory.resolve(pathArgs[i]);
					}
				} else {
					tempDirectory = tempDirectory.resolve(args[2 + modifierCount].substring(0, args[2 + modifierCount].length() - 1));
				}

				final @NotNull File pathFile = tempDirectory.toFile();
				if (pathFile.exists() && pathFile.isDirectory()) {
					if (args[1].equalsIgnoreCase("load")
						|| args[1].equalsIgnoreCase("save")
						|| args[1].equalsIgnoreCase("del")
						|| args[1].equalsIgnoreCase("delete")
						|| args[1].equalsIgnoreCase("rename")
						|| args[1].equalsIgnoreCase("copy")) {
						SchematicTabCompleter.addFilesToCompletions(args[2].endsWith("/") ? "" : pathArgs[pathArgs.length - 1], completions, Objects.notNull(BaseFileUtils.listFiles(pathFile, false)));
					} else if (args[1].equalsIgnoreCase("renamefolder")
							   || args[1].equalsIgnoreCase("delfolder")
							   || args[1].equalsIgnoreCase("deletefolder")
							   || args[1].equalsIgnoreCase("list")
							   || args[1].equalsIgnoreCase("listschems")
							   || args[1].equalsIgnoreCase("listfolders")
							   || args[1].equalsIgnoreCase("search")
							   || args[1].equalsIgnoreCase("searchschem")
							   || args[1].equalsIgnoreCase("searchfolder")
							   || args[1].equalsIgnoreCase("copyfolder")) {
						SchematicTabCompleter.addFilesToCompletions(args[2 + modifierCount].endsWith("/") ? "" : pathArgs[pathArgs.length - 1], completions, Objects.notNull(BaseFileUtils.listFolders(pathFile, false)));
					}
				}
			}
		} else if ((args.length == 4 + modifierCount && !argumentEnded) || (args.length == 3 + modifierCount && argumentEnded)) {
			if (argumentEnded) {
				if (args[1].equalsIgnoreCase("load")) {
					completions.addAll(Objects.notNull(ConfigUtils.getStringList("File Extensions")));
				} else if (args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy") || args[1].equalsIgnoreCase("download")) {
					SchematicTabCompleter.addFilesToCompletions(null, completions, SchematicTabCompleter.getFileList(SchemUtils.getSchemFolder()));
				} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("copyfolder")) {
					final @NotNull File pathFile = SchemUtils.getSchemFolder();
					if (pathFile.exists() && pathFile.isDirectory()) {
						SchematicTabCompleter.addFilesToCompletions(null, completions, Objects.notNull(BaseFileUtils.listFolders(pathFile, false)));
					}
				}
			} else {
				if (args[1].equalsIgnoreCase("load")) {
					for (final @NotNull String extension : Objects.notNull(ConfigUtils.getStringList("File Extensions"))) {
						if (extension.toLowerCase().startsWith(args[3]) && !extension.equals(args[3])) {
							completions.add(extension);
						}
					}
				} else {
					@NotNull Path tempDirectory = SchemUtils.getSchemPath();
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
						if (args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("copy") || args[1].equalsIgnoreCase("download")) {
							SchematicTabCompleter.addFilesToCompletions(args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1], completions, Objects.notNull(BaseFileUtils.listFiles(pathFile, false)));
						} else if (args[1].equalsIgnoreCase("renamefolder") || args[1].equalsIgnoreCase("copyfolder")) {
							SchematicTabCompleter.addFilesToCompletions(args[3].endsWith("/") ? "" : pathArgs[pathArgs.length - 1], completions, Objects.notNull(BaseFileUtils.listFolders(pathFile, false)));
						}
					}
				}
			}
		}
		return completions;
	}


	private @NotNull List<File> getFileList(final @NotNull File directory) throws IOException {
		return Objects.notNull(BaseFileUtils.listFilesOfTypeAndFolders(directory, false, Objects.notNull(ConfigUtils.getStringList("File Extensions"))));
	}

	private void addFilesToCompletions(final @Nullable String sequence, final @NotNull List<String> completions, final @NotNull List<File> fileList) {
		if (sequence == null) {
			completions.addAll(fileList.stream().map(File::getName).collect(Collectors.toSet()));
		} else {
			completions.addAll(fileList.stream()
									   .filter(file -> file.getName().toLowerCase().startsWith(sequence.toLowerCase()) && !file.getName().equalsIgnoreCase(sequence))
									   .map(file -> {
										   try {
											   return BaseFileUtils.removeExtension(FilenameUtils.separatorsToUnix(SchemUtils.getSchemPath().toRealPath().relativize(file.toPath().toRealPath()).toString()));
										   } catch (final @NotNull IOException e) {
											   e.printStackTrace();
											   return null;
										   }
									   })
									   .filter(java.util.Objects::nonNull)
									   .collect(Collectors.toSet()));
		}
	}
}