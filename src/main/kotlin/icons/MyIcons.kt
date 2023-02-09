package icons

import com.intellij.openapi.util.IconLoader

object MyIcons {
    @JvmField
    val ErrorIcon = IconLoader.getIcon("/icons/Error Icon 12x12.svg", javaClass)

    @JvmField
    val InfoIcon = IconLoader.getIcon("/icons/Info Icon 12x12.svg", javaClass)

    @JvmField
    val PluginIcon = IconLoader.getIcon("/icons/Main Icon 16x16.svg", javaClass)

    @JvmField
    val WarningIcon = IconLoader.getIcon("/icons/Warning Icon 12x12.svg", javaClass)
}