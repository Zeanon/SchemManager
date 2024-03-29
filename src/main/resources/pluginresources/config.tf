################################################################################################################
#||==========================================================================================================||#
#||                                                                                                          ||#
#||                                         SchemManager Config-File                                         ||#
#||                                             Plugin by Zeanon                                             ||#
#||                            Config-Management-System: StorageManager by Zeanon                            ||#
#||                                                                                                          ||#
#||==========================================================================================================||#
################################################################################################################

################################################################################################################
# For Syntax-Highlighting for the config file or information about the syntax, please visit                    #
# https://github.com/Zeanon/StorageManager                                                                     #
#                                                                                                              #
# To get information about the custom commands, just type /schem or //schem.                                   #
# The plugin will reload the config on it's own when the config file gets updated                              #
# You can perform a deep search or deep listing by using -d.                                                   #
################################################################################################################

################################################################################################################
# SchemManager mostly uses the standard WorldEdit and FAWE permissions                                         #
# rename and renamefolder use the save permission                                                              #
# search and searchfolder use the list permission                                                              #
# update uses schemmanager.update                                                                              #
# disable uses schemmanager.disable                                                                            #
################################################################################################################


# Defines the version of the plugin (mostly used to check whether the config needs to be updated [DO NOT EDIT UNLESS NESCESSARY])
Plugin Version = ${project.version}


# File Extensions: here you can specify the file-types the plugin recognizes as schematics.
# The first extension in the list will be used as the default one.
File Extensions = [schem, schematic]

# Listmax: The maximum amount of schematics per page to be shown.
Listmax = 10

# Space Lists: Should there be a blank line before each schem or folder page?
Space Lists = true

# Delete empty Folders: deletes a folder when it is empty after deleting a schematic in it.
Delete empty Folders = true

# Save Funtion Override: Should the basic save function be overridden by the plugin?
Save Function Override = true

# Automatic Reload: Should the plugin automatically reload the Server after it got updated?
# If you have PlugMan installed, it will only reload itself, not the whole server.
# PlugMan: https://dev.bukkit.org/projects/plugman
Automatic Reload = true
