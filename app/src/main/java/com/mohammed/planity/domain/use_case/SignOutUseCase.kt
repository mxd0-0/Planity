package com.mohammed.planity.domain.use_case

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke() {
        auth.signOut()
    }
}