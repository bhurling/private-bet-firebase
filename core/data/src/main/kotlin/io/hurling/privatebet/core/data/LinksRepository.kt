package io.hurling.privatebet.core.data

import io.hurling.privatebet.core.network.NetworkLink
import io.hurling.privatebet.core.network.NetworkLinksDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface LinksRepository {
    fun incoming(id: String): Flow<List<String>>
    fun outgoing(id: String): Flow<List<String>>
    fun confirmed(id: String): Flow<List<String>>
}

internal class DefaultLinksRepository @Inject constructor(
    private val networkDataSource: NetworkLinksDataSource
) : LinksRepository {
    override fun incoming(id: String): Flow<List<String>> {
        return networkDataSource.incomingForId(id)
            .map { links -> links.map(NetworkLink::documentId) }
    }

    override fun outgoing(id: String): Flow<List<String>> {
        return networkDataSource.outgoingForId(id)
            .map { links -> links.map(NetworkLink::documentId) }
    }

    override fun confirmed(id: String): Flow<List<String>> {
        return networkDataSource.confirmedForId(id)
            .map { links -> links.map(NetworkLink::documentId) }
    }
}
