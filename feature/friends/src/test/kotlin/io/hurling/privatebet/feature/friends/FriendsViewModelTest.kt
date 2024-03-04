package io.hurling.privatebet.feature.friends

import io.hurling.privatebet.core.domain.GetFriendsUseCase
import io.hurling.privatebet.core.domain.AcceptInvitationUseCase
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class FriendsViewModelTest {

    @MockK
    lateinit var getFriends: GetFriendsUseCase

    @MockK
    lateinit var acceptInvitation: AcceptInvitationUseCase

    @InjectMockKs
    lateinit var sut: FriendsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { getFriends.invoke() } returns emptyFlow()
    }

    @Test
    fun whenViewModelStarts_wouldEmitLoadingState() = runTest {
        val state = sut.state.first()

        state.shouldBeInstanceOf<FriendsScreenState.Loading>()
    }
}
