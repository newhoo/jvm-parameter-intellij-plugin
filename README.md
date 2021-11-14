# jvm-parameter-intellij-plugin

[简体中文](./README.zh_CN.md)

Manage jvm run parameter for your app conveniently. Enabled jvm parameters will be added to application run option when running.

If this plugin helps, please **🌟 Star** and [Rating](https://plugins.jetbrains.com/plugin/13204-jvm-parameter/reviews)! If you have any good idea, please let me know.


## Feature
- Project jvm parameter management.
- Global jvm parameter management, applied to all opened project.

## Install
- **Using IDE plugin system**

Recommended <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>find "Jvm Parameter"</kbd> > <kbd>Install Plugin</kbd>

- **Local Install**

Download plugin form <kbd>distributions/jvm-parameter-intellij-plugin-x.x.x.zip</kbd>, then <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Install Plugin from Disk...</kbd>

## Usage
After installed this plugin, you can add custom parameter in `Jvm Parameter` setting.

![setting](./images/setting.png)

- Preview：preview enabled parameter.
- Table column description

|Column Name|Description|
| --- | --- |
| ENABLE | Whether enable current row |
| NAME | Parameter key, not empty |
| VALUE | Parameter value. If empty, current row parameter will be NAME content |
| GLOBAL | Is global parameter |

- Generate jvm parameter: click `Preview` label to generate common parameter.

## Contact & Feedback
[Issues](https://github.com/huzunrong/jvm-parameter-intellij-plugin/issues) | [Email](mailto:huzunrong@foxmail.com) | [Ratings & Previews](https://plugins.jetbrains.com/plugin/13204-jvm-parameter/reviews)

> Note  
> Please provide necessary information when you feedback: IDEA version, plugin version, exception content, recreation way(if can), desire, and etc.
