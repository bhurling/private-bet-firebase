package org.koin

import org.koin.Koin.Companion.logger
import org.koin.core.bean.BeanDefinition
import org.koin.core.bean.BeanRegistry
import org.koin.core.instance.InstanceFactory
import org.koin.core.property.PropertyRegistry
import org.koin.error.DependencyResolutionException
import org.koin.error.MissingPropertyException
import org.koin.standalone.StandAloneKoinContext
import java.util.*
import kotlin.reflect.KClass

/**
 * Koin Application Context
 * Context from where you can get beans defines in modules
 *
 * @author Arnaud GIULIANI
 */
class KoinContext(val beanRegistry: BeanRegistry, val propertyResolver: PropertyRegistry,
                  val instanceFactory: InstanceFactory) : StandAloneKoinContext {

    /**
     * call stack - bean definition resolution
     */
    private val resolutionStack = Stack<KClass<*>>()

    /**
     * Retrieve a bean instance
     */
    inline fun <reified T> get(name: String = ""): T = if (name.isEmpty()) resolveByClass() else resolveByName(name)

    /**
     * Resolve a dependency for its bean definition
     * @param name bean definition name
     */
    inline fun <reified T> resolveByName(name: String): T = resolveInstance(T::class) {
        beanRegistry.searchByName(T::class, name)
    }

    /**
     * Resolve a dependency for its bean definition
     * byt Its infered type
     */
    inline fun <reified T> resolveByClass(): T = resolveByClass(T::class)

    /**
     * Resolve a dependency for its bean definition
     * byt its type
     */
    inline fun <reified T> resolveByClass(clazz: KClass<*>): T = resolveInstance(clazz) {
        beanRegistry.searchAll(clazz)
    }

    /**
     * Resolve a dependency for its bean definition
     */
    fun <T> resolveInstance(clazz: KClass<*>, resolver: () -> BeanDefinition<*>): T = synchronized(this) {
        if (resolutionStack.contains(clazz)) {
            throw DependencyResolutionException("Cyclic dependency detected while resolving $clazz")
        }

        // Context isolation
        if (Koin.useContextIsolation) {
            if (!beanRegistry.isVisible(clazz, resolutionStack.toList())) {
                throw DependencyResolutionException("Definition $clazz is not visible for classes : $resolutionStack")
            }
        }

        val beanDefinition: BeanDefinition<*> = resolver()
        val indent = resolutionStack.joinToString(separator = "") { "\t" }
        logger.log("${indent}Resolve [${clazz.java.canonicalName}] ~ $beanDefinition")

        resolutionStack.add(clazz)
        val (instance, created) = instanceFactory.retrieveInstance<T>(beanDefinition)
        if (created) {
            logger.log("${indent}(*) Created")
        }

        val head = resolutionStack.pop()

        if (head != clazz) {
            resolutionStack.clear()
            throw IllegalStateException("Stack resolution error : was $head but should be $clazz")
        }
        return instance
    }

    /**
     * Check the all the loaded definitions - Try to resolve each definition
     */
    fun dryRun() {
        logger.log("(DRY RUN)")
        beanRegistry.definitions.keys.forEach { def ->
            Koin.logger.log("Testing $def ...")
            instanceFactory.retrieveInstance<Any>(def)
//            if (def.bindTypes.isNotEmpty()) {
//                def.bindTypes.forEach {
//                    Koin.logger.log("Testing additional type : $it ...")
//                    resolveByClass(it)
//                }
//            }
        }
    }

    /**
     * Drop all instances for given context
     * @param name
     */
    fun releaseContext(name: String) {
        logger.log("Release context : $name")

        val definitions: List<BeanDefinition<*>> = beanRegistry.getDefinitionsFromScope(name)
        instanceFactory.dropAllInstances(definitions)
    }

    /**
     * Retrieve a property by its key
     * can throw MissingPropertyException if the property is not found
     * @param key
     * @throws MissingPropertyException if key is not found
     */
    inline fun <reified T> getProperty(key: String): T = propertyResolver.getProperty(key)

    /**
     * Retrieve a property by its key or return provided default value
     * @param key - property key
     * @param defaultValue - default value if property is not found
     */
    inline fun <reified T> getProperty(key: String, defaultValue: T): T = propertyResolver.getProperty(key,
            defaultValue)

    /**
     * Set a property
     * @param key
     * @param value
     */
    fun setProperty(key: String, value: Any) = propertyResolver.add(key, value)

    /**
     * Delete properties from keys
     * @param keys
     */
    fun releaseProperties(vararg keys: String) {
        propertyResolver.deleteAll(keys)
    }

    /**
     * Close res
     */
    fun close() {
        logger.log("[Close] Closing Koin context")
        resolutionStack.clear()
        instanceFactory.clear()
        beanRegistry.clear()
        propertyResolver.clear()
    }
}