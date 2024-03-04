package io.hurling.privatebet.core.domain

import io.hurling.privatebet.core.auth.Auth
import io.hurling.privatebet.core.data.LinksRepository
import io.hurling.privatebet.core.data.ProfilesRepository
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.booleans.shouldNotBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetFriendsUseCaseTest {

    @MockK
    lateinit var auth: Auth

    @MockK
    lateinit var profilesRepository: ProfilesRepository

    @MockK
    lateinit var linksRepository: LinksRepository

    @InjectMockKs
    lateinit var sut: GetFriendsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { auth.currentUserId } returns "[me]"

        val slot = slot<List<String>>()
        every { profilesRepository.profilesByIds(capture(slot)) } answers {
            flowOf(slot.captured.map { id -> ProfileFactory.create().copy(id) })
        }

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun whenFriendIsBothIncomingAndConfirmed_wouldFilterOutDuplicates() = runTest {
        every { linksRepository.confirmed("[me]") } returns flowOf(listOf("[friend1]"))
        every { linksRepository.incoming("[me]") } returns flowOf(listOf("[friend1]"))

        val flowEmissions = sut.invoke().toList()
        val friends = flowEmissions.last()

        friends.size.shouldBe(1)
        with(friends.first()) {
            isConfirmed.shouldBeTrue()
            isIncoming.shouldNotBeTrue()
        }
    }
}