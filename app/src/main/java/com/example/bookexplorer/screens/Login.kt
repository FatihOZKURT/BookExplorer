package com.example.bookexplorer.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
fun LoginScreen(onLoginSuccess: () -> Unit, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(scrollState)
            .padding(top = 90.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.book_and_earth),
            contentDescription = "Login Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(250.dp)
        )

        Text(
            "Book Explorer",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 60.dp)
        )

        Text(
            "E-Posta",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(start = 32.dp, bottom = 2.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-posta adresinizi giriniz") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "E-posta ikonu") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Şifre",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(start = 32.dp, bottom = 2.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifrenizi giriniz") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Şifre ikonu") },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (showPassword) R.drawable.pass_visibility_off else R.drawable.pass_visibility_on
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter = painterResource(id = icon),
                        modifier = Modifier.size(24.dp),
                        tint = colorScheme.primary,
                        contentDescription = if (showPassword) "Şifreyi Gizle" else "Şifreyi Göster"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                "Parolanı mı Unuttun?",
                color = colorScheme.primary,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .clickable {
                        if (email.isNotBlank()) {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Şifre sıfırlama e-postası gönderildi", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Hata: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(context, "Lütfen geçerli bir e-posta girin", Toast.LENGTH_SHORT).show()
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "E-posta ve şifre boş bırakılamaz", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener { onLoginSuccess() }
                        .addOnFailureListener {  Toast.makeText(context,  it.localizedMessage, Toast.LENGTH_LONG).show() }
                },
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(30))
            ) {
                Text("Giriş Yap", fontSize = 20.sp)
            }


            Text("veya", fontSize = 15.sp, color = colorScheme.onBackground.copy(alpha = 0.6f))

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

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("Hesabın yok mu?", color = colorScheme.onBackground)
                Text(
                    text = "Kayıt ol",
                    color = colorScheme.primary,
                    modifier = Modifier.clickable {
                        navController.navigate("register_screen")
                    }
                )
            }
        }
    }
}

