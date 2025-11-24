package com.aura.ui.login

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aura.R
import com.aura.domain.model.UserCredentials
import com.aura.domain.usecase.LoginUseCase
import com.aura.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any


@OptIn (ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var loginUseCase : LoginUseCase

    @Mock
    private lateinit var context : Context

    private lateinit var viewModel : LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        `when`(context.getString(R.string.error_invalid_credentials)).thenReturn("Identifiants invalides")
        `when`(context.getString(R.string.error_network)).thenReturn("Connexion impossible")

        viewModel = LoginViewModel(loginUseCase, context)
    }

    @After
    fun tearDown() { Dispatchers.resetMain()}

    /**
     * tests the disabled state of the button
     *
     */

    @Test
    fun `button should be disabled when identifier is empty`() {
        //Given
        viewModel.onIdentifierChange("")
        viewModel.onPasswordChange("password")

        //When
        val loginData = viewModel.loginData.value

        //Then
        assertFalse(loginData.isButtonEnabled)
    }
    @Test
    fun `button should be disabled when password is empty`() {
        // GIVEN
        viewModel.onIdentifierChange("1234")
        viewModel.onPasswordChange("")

        // WHEN
        val loginData = viewModel.loginData.value

        // THEN
        assertFalse(loginData.isButtonEnabled)
    }

        /**
     * test the activated state of the button
     */

    @Test
    fun `button should be enabled when identifier and password are not empty`() {
    //Given
        viewModel.onIdentifierChange("1234")
        viewModel.onPasswordChange("p@sswOrd")

    //When
        val loginData = viewModel.loginData.value

    //Then
        assertTrue(loginData.isButtonEnabled)
    }

    /**
     * Test update state success or error when login
     */

    @Test
    fun `login should update state to success when login is successful`() = runTest {
        //GIVEN
        `when`(loginUseCase(any())).thenReturn(true)
        viewModel.onIdentifierChange("1234")
        viewModel.onPasswordChange("p@sswOrd")

        //WHEN
        viewModel.connect()
        advanceUntilIdle()

        //THEN
        val loginState = viewModel.loginState.value
        assertTrue(loginState is UiState.Success)
    }

    @Test
    fun `login should update state to error when login fails`() = runTest {
        //GIVEN
        `when`(loginUseCase(any())).thenReturn(false)
        viewModel.onIdentifierChange("badUser")
        viewModel.onPasswordChange("badPassword")

        //WHEN
        viewModel.connect()
        advanceUntilIdle()

        //THEN
        val loginState = viewModel.loginState.value
        assertTrue(loginState is UiState.Error)
        assertEquals("Identifiants invalides", (loginState as UiState.Error).message)
    }


}
