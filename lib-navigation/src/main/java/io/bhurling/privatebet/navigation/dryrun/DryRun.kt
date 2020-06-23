package io.bhurling.privatebet.navigation.dryrun

import io.bhurling.privatebet.navigation.EntryPoint

class DryRunException(message: String) : Throwable(message)

class DryRun {
    companion object {
        fun checkEntryPoints() {
            EntryPoint.values().forEach { entryPoint ->
                try {
                    Class.forName(entryPoint.classPath)
                } catch (e: ClassNotFoundException) {
                    throw DryRunException(
                        "Can't find entry point '${entryPoint.classPath}' " +
                                "referenced from ${entryPoint.name}."
                    )
                }
            }
        }
    }
}