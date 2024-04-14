<p align="center">
  <img src="https://raw.githubusercontent.com/EinfachGustaf/SMPClaim/master/.img/icon.jpg", height=256, width=256 />
</p>

# SMPClaim
A simple chunk claim system for SMP's used on the **EinfachGustaf.live SMP**.

## Install
- Download the latest version of the plugin. **You need [WorldGuard](https://enginehub.org/worldguard) and [WorldEdit](https://modrinth.com/plugin/worldedit) as a dependency!**
- Drag the plugin into the `plugins/` folder!
- Enter your database credentials into the config file
- You are good to go!

## Supported database types
| Database     | NAME       | Supported | Comment             |
|--------------|------------|-----------|---------------------|
| Local (JSON) | LOCAL_JSON | 🟢        | **not recommended** |
| PostgreSQL   | POSTGRES   | 🟢        | **recommended**     |

## Usage
| Command                       | Description                                    |
|-------------------------------|------------------------------------------------|
| /claim                        | Claim the chunk you are standing on            |
| /unclaim                      | Unclaim the chunk you are standing on          |
| /access <add/remove> <player> | Command to manage access to your chunk         |
| /cinfo                        | Get the Owner of the chunk you are standing on |

## Issues
If you have any issues related to this plugin you can simply open an Issue on this page. Please check our [OpenProject Issue Tracker](https://openproject.internal.einfachgustaf.live/projects/smpclaim/work_packages) first.
