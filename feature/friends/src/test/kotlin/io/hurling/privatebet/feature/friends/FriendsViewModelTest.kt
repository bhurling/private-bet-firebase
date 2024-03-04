package io.hurling.privatebet.feature.friends

import io.hurling.privatebet.core.domain.AcceptInvitationUseCase
import io.hurling.privatebet.core.domain.GetFriendsUseCase
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class FriendsViewModelTest {


    @MockK
    lateinit var getFriends: GetFriendsUseCase

    @MockK(relaxed = true)
    lateinit var acceptInvitation: AcceptInvitationUseCase

    @InjectMockKs
    lateinit var sut: FriendsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { getFriends.invoke() } returns emptyFlow()

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun whenViewModelStarts_wouldEmitLoadingState() = runTest {
        sut.state.value.shouldBeInstanceOf<FriendsScreenState.Loading>()
    }

    @Test
    fun whenUseCaseReturnsFriends_wouldEmitSuccessState() = runTest {
        every { getFriends.invoke() } returns flowOf(
            listOf(
                FriendFactory.create().copy(id = "[userId]", isConfirmed = true)
            )
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            sut.state.collect()
        }

        with(sut.state.value as FriendsScreenState.Success) {
            items.first().id.shouldBe("[userId]")
        }
    }

    @Test
    fun whenCallingAcceptInvitation_wouldPassThroughToUseCase() {
        sut.acceptInvitation("[profileId]")

        verify { acceptInvitation.invoke("[profileId]") }
    }
}
