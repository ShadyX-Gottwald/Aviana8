package student.projects.aviana8.Screens

import android.R
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password

import androidx.compose.material.icons.rounded.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import student.projects.aviana8.Viewmodels.AuthViewModel
import student.projects.aviana8.Viewmodels.LoginUserDTO
import student.projects.aviana8.Viewmodels.NetworkResponse
import student.projects.aviana8.ui.theme.Brown
import student.projects.aviana8.ui.theme.DarkPurpleBackGround
import student.projects.aviana8.ui.theme.GreenOutline
import student.projects.aviana8.ui.theme.Peach
import student.projects.aviana8.ui.theme.TextBrown
import student.projects.aviana8.ui.theme.WhiteNew
import student.projects.aviana8.ui.theme.WhiteishBg

@Composable
fun LoginPage(
    viewModel: AuthViewModel,
    navToRegisterPage: () -> Unit,
    loginButtonClick: suspend (LoginUserDTO) -> Unit,
    navToHomePage: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var scope = CoroutineScope(CoroutineName("Scope"))

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkPurpleBackGround)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkPurpleBackGround)
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Handle auth state
                val authState by viewModel.AuthState.collectAsState()

                LaunchedEffect(authState) {
                    when (authState) {
                        is NetworkResponse.Success<*> -> {
                            // Login Success, Show Message and Navigate
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                            navToHomePage()
                        }
                        is NetworkResponse.Error<*> -> {
                            val errorMessage =  (authState as NetworkResponse.Error<*>).message
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            // Handle other states if needed
                        }
                    }
                }

                // Branding Section
                LoginHeaderSection()

                Spacer(modifier = Modifier.height(30.dp))

                // Input Section
                var emailText by remember {
                    mutableStateOf("")
                }
                var passwordText by remember {
                    mutableStateOf("")
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 50.dp, end = 10.dp, bottom = 5.dp)
                    ,
                    verticalArrangement = Arrangement.Center
                ) {

                    OutlinedTextField(modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                        value = emailText,
                        onValueChange = {
                            emailText = it
                            viewModel.UpdateLoginValues(
                                emailText,
                                passwordText,

                                )
                        },
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        label = {
                            Text(text = "Email")

                        },
                        //Email Keyboard
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {

                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                            )
                        }

                    )


                    //Password Field
                    OutlinedTextField(modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                        value = passwordText,
                        onValueChange = {
                            passwordText = it
                            viewModel.UpdateLoginValues(
                                emailText,
                                passwordText,

                                )
                        },
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        label = {
                            Text(text = "Password")

                        },
                        //Password Keyboard
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Password,
                                contentDescription = "Password",
                            )
                        }

                    )

                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(Brown),
                    onClick = {
                        //Validate

                        scope.launch(Dispatchers.IO){
                            //LoginButtonClick.invoke(vIewModel.loginUser)
                            viewModel.loginToFirebase(viewModel.loginUser)
                        }

                    } ,

                    ) {
                    Text(
                        text = "Login" , fontWeight = FontWeight.SemiBold ,
                        color = WhiteishBg ,
                        textAlign = TextAlign.Center ,
                        fontSize = 12.sp
                    )

                }

               // Spacer(modifier = Modifier.height(20.dp))

                // Action Section
                ActionSection(
                    viewModel = viewModel,
                    authState = authState,
                    onLoginClick = {
                        coroutineScope.launch {
                            loginButtonClick(viewModel.loginUser)
                        }
                    },
                    onRegisterClick = navToRegisterPage
                )
            }
        }
    }
}

@Composable
fun LoginHeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Logo
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Peach, CircleShape)
                .border(2.dp, GreenOutline, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_up_float),
                contentDescription = "Aviana Logo",
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .padding(10.dp)
                    .clip(CircleShape)
                , contentScale = ContentScale.FillWidth

            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            color = WhiteNew,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Sign in to continue your bird watching journey",
            style = MaterialTheme.typography.bodyMedium,
            color = WhiteNew.copy(alpha = 0.8f),
            modifier = Modifier.padding(horizontal = 40.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginInputs(viewModel: AuthViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhiteNew
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineSmall,
                color = TextBrown,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Email Input
            OutlinedTextField(
                value = viewModel.loginUser.email,
                onValueChange = { viewModel.updateEmail(it) },
                label = {
                    Text(
                        "Email",
                        color = TextBrown.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WhiteNew,
                    unfocusedContainerColor = WhiteNew,
                    focusedTextColor = TextBrown,
                    unfocusedTextColor = TextBrown,
                    focusedLabelColor = GreenOutline,
                    unfocusedLabelColor = TextBrown.copy(alpha = 0.6f),
                    focusedIndicatorColor = GreenOutline,
                    unfocusedIndicatorColor = TextBrown.copy(alpha = 0.3f)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            OutlinedTextField(
                value = viewModel.loginUser.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = {
                    Text(
                        "Password",
                        color = TextBrown.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WhiteNew,
                    unfocusedContainerColor = WhiteNew,
                    focusedTextColor = TextBrown,
                    unfocusedTextColor = TextBrown,
                    focusedLabelColor = GreenOutline,
                    unfocusedLabelColor = TextBrown.copy(alpha = 0.6f),
                    focusedIndicatorColor = GreenOutline,
                    unfocusedIndicatorColor = TextBrown.copy(alpha = 0.3f)
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password
            TextButton(
                onClick = { /* TODO: Implement forgot password */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Forgot Password?",
                    color = GreenOutline,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ActionSection(
    viewModel: AuthViewModel,
    authState: NetworkResponse<Any?>,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Login Button
       /* Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Peach,
                contentColor = Brown
            ),
            enabled = authState !is NetworkResponse.Loading &&
                    viewModel.loginUser.email.isNotBlank() &&
                    viewModel.loginUser.password.isNotBlank(),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            if (authState is NetworkResponse.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Brown,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        */


        Spacer(modifier = Modifier.height(16.dp))

        // Divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = WhiteNew.copy(alpha = 0.3f)
            )
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = WhiteNew.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = WhiteNew.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create Account Button
        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = WhiteNew
            ),
            border = BorderStroke(2.dp, Peach)
        ) {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}