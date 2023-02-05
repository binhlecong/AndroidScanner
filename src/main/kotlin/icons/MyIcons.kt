package icons

import com.intellij.openapi.util.IconLoader

object MyIcons {
    @JvmField
    val ErrorIcon = IconLoader.getIcon("/icons/errorIcon.svg", javaClass)

    @JvmField
    val InfoIcon = IconLoader.getIcon("/icons/infoIcon.svg", javaClass)

    @JvmField
    val PluginIcon = IconLoader.getIcon("/icons/pluginIcon.svg", javaClass)

    @JvmField
    val WarningIcon = IconLoader.getIcon("/icons/warningIcon.svg", javaClass)
}