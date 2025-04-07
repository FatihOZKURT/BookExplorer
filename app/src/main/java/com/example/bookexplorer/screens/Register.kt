package com.example.bookexplorer.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookexplorer.R
import com.example.bookexplorer.util.Constants.GOOGLE_CLIENT_ID
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun RegisterScreen(onLoginSuccess: () -> Unit, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) }
    var passwordConfirm by remember { mutableStateOf("") }
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    val signInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnSuccessListener { onLoginSuccess() }
                .addOnFailureListener { error = it.localizedMessage }
        } catch (e: ApiException) {
            error = e.localizedMessage
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.book_and_earth_register),
                contentDescription = "Register Image",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(300.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-posta adresinizi giriniz", color = colorScheme.onBackground) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = colorScheme.primary)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = colorScheme.primary,
                    focusedLabelColor = colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifrenizi giriniz", color = colorScheme.onBackground) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = colorScheme.primary)
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword) R.drawable.pass_visibility_off else R.drawable.pass_visibility_on
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = if (showPassword) "Şifreyi Gizle" else "Şifreyi Göster",
                            modifier = Modifier.size(24.dp),
                            tint = colorScheme.primary
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = colorScheme.primary,
                    focusedLabelColor = colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordConfirm,
                onValueChange = { passwordConfirm = it },
                label = { Text("Şifre (Tekrar)", color = colorScheme.onBackground) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = colorScheme.primary)
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword) R.drawable.pass_visibility_off else R.drawable.pass_visibility_on
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = if (showPassword) "Şifreyi Gizle" else "Şifreyi Göster",
                            modifier = Modifier.size(24.dp),
                            tint = colorScheme.primary
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = colorScheme.primary,
                    focusedLabelColor = colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (password != passwordConfirm) {
                        error = "Şifreler aynı değil"
                        return@Button
                    }
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            navController.navigate("books_screen") {
                                popUpTo("register_screen") { inclusive = true }
                            }
                        }
                        .addOnFailureListener { error = it.localizedMessage }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Text("Kayıt Ol", fontSize = 18.sp, color = colorScheme.onPrimary)
            }

            error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("veya", fontSize = 15.sp, color = colorScheme.onBackground.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val signInIntent = signInClient.signInIntent
                    launcher.launch(signInIntent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.surface),
                shape = RoundedCornerShape(25),
                modifier = Modifier
                    .size(50.dp)
                    .shadow(10.dp, RoundedCornerShape(25))
                    .border(1.dp, colorScheme.primary, RoundedCornerShape(25))
                    .clip(RoundedCornerShape(25)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.google_icon),
                    contentDescription = "Google ile Giriş",
                    modifier = Modifier
                        .size(40.dp)
                        .background(colorScheme.surface),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(60.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Zaten hesabınız var mı?", color = colorScheme.onBackground)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Giriş Yap",
                color = colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        }
    }
}

