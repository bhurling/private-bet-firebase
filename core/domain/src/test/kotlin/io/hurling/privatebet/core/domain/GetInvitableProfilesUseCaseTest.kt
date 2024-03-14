package io.hurling.privatebet.core.domain

import io.hurling.privatebet.core.auth.Auth
import io.hurling.privatebet.core.data.LinksRepository
import io.hurling.privatebet.core.data.ProfilesRepository
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
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
class GetInvitableProfilesUseCaseTest {
    @MockK
    lateinit var auth: Auth

    @MockK
    lateinit var profilesRepository: ProfilesRepository

    @MockK
    lateinit var linksRepository: LinksRepository

    @InjectMockKs
    lateinit var sut: GetInvitableProfilesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { auth.currentUserId } returns "[me]"
        every { linksRepository.outgoing(any()) } returns flowOf(emptyList())
        every { linksRepository.confirmed(any()) } returns flowOf(emptyList())

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun wouldFilterOutMyOwnProfile() = runTest {
        every { profilesRepository.profiles() } returns flowOf(
            listOf(ProfileFactory.create().copy(id = "[me]"))
        )

        val flowEmissions = sut.invoke().toList()
        val profiles = flowEmissions.last()

        profiles.shouldHaveSize(0)
    }

    @Test
    fun wouldFilterOutConfirmedLinks() = runTest {
        every { profilesRepository.profiles() } returns flowOf(
            listOf(ProfileFactory.create().copy(id = "[confirmed]"))
        )

        every { linksRepository.confirmed(any()) } returns flowOf(
            listOf("[confirmed]")
        )

        val flowEmissions = sut.invoke().toList()
        val profiles = flowEmissions.last()

        profiles.shouldHaveSize(0)
    }

    @Test
    fun wouldMarkOutgoingLinksAsInvited() = runTest {
        every { profilesRepository.profiles() } returns flowOf(
            listOf(ProfileFactory.create().copy(id = "[outgoing]"))
        )

        every { linksRepository.outgoing(any()) } returns flowOf(
            listOf("[outgoing]")
        )

        val flowEmissions = sut.invoke().toList()
        val profiles = flowEmissions.last()

        with(profiles.first()) {
            isInvited.shouldBeTrue()
        }
    }
}