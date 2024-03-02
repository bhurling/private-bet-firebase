package io.hurling.privatebet.core.data

import io.hurling.privatebet.core.network.NetworkLink
import io.hurling.privatebet.core.network.NetworkLinksDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface LinksRepository {
    fun incoming(uid: String): Flow<List<String>>
    fun outgoing(uid: String): Flow<List<String>>
    fun confirmed(uid: String): Flow<List<String>>
    fun addOutgoingLink(fromId: String, toId: String)
    fun removeOutgoingLink(fromId: String, toId: String)
    fun addConfirmedLink(fromId: String, toId: String)
}

internal class DefaultLinksRepository @Inject constructor(
    private val networkDataSource: NetworkLinksDataSource
) : LinksRepository {
    override fun incoming(uid: String): Flow<List<String>> {
        return networkDataSource.incomingForId(uid)
            .map { links -> links.map(NetworkLink::documentId) }
    }

    override fun outgoing(uid: String): Flow<List<String>> {
        return networkDataSource.outgoingForId(uid)
            .map { links -> links.map(NetworkLink::documentId) }
    }

    override fun confirmed(uid: String): Flow<List<String>> {
        return networkDataSource.confirmedForId(uid)
            .map { links -> links.map(NetworkLink::documentId) }
    }

    override fun addOutgoingLink(fromId: String, toId: String) {
        networkDataSource.addOutgoingLink(fromId, toId)
    }

    override fun removeOutgoingLink(fromId: String, toId: String) {
        networkDataSource.removeOutgoingLink(fromId, toId)
    }

    override fun addConfirmedLink(fromId: String, toId: String) {
        networkDataSource.addConfirmedLink(fromId, toId)
    }
}
