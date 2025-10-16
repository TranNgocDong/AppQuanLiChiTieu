package com.example.project_mbp.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_mbp.R

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mess by remember { mutableStateOf<String?>(null) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC9F0FF))

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_login),
                    contentDescription = "Logo_Login",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 55.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            Card(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxWidth(0.9f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFC9F0FF)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,


                    ) {
                    Text(
                        text = "Đăng Nhập",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold

                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    textfields("Email", email, { email = it })
                    Spacer(modifier = Modifier.height(15.dp))
                    textfields("Mật khẩu", value = password, { password = it })
                    //mess thong bao
                    Spacer(modifier = Modifier.height(10.dp))
                    if (mess != null) {
                        Text(
                            text = mess!!,
                            fontSize = 18.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    // button kiem tra
                    Button(
                        onClick = {
                            val Email = email
                            val Password = password
                            val isInputEmail = email.contains("@")
                            email.trim()
                            password.trim()

                            // check Email and pass
                            when {
                                Email.isBlank() -> {
                                    mess = "Vui lòng nhập Email !"
                                }

                                !isInputEmail -> {
                                    mess = "Email không hợp lệ !"
                                }

                                Password.isBlank() -> {
                                    mess = "Vui lòng nhập mật khẩu !"
                                }

                                else -> {
                                    mess = null
                                }
                            }


                        }, modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF36435)
                        ),
                        shape = RoundedCornerShape(15.dp)

                    ) {
                        Text(
                            text = "Đăng nhập",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    // hoac dang ki
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Hoặc",
                                fontSize = 18.sp,

                                )
                            Spacer(modifier = Modifier.height(15.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(40.dp)
                            ) {
                                //them icon
                                icon_login(R.drawable.icon_google, "icon_google", 40, 45)
                                Image(
                                    painter = painterResource(id = R.drawable.icon_facebook),
                                    contentDescription = "Icon_facebook",
                                    modifier = Modifier
                                        .size(50.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(15.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "Chưa có tài khoản? ",
                                    fontSize=16.sp
                                )
                                Text(
                                    text = "Đăng ký",
                                    color = Color(0xFF0BB7F3),
                                    fontSize = 16.sp
                                )

                            }
                        }

                    }//box2
                }// column

            }

        }
    } //Box 1
}


@Composable
fun textfields(label: String, value: String, onChange: (String) -> Unit) {

    var passwordVisible by remember { mutableStateOf(false) }
    val isPasswordField = label.contains("Mật khẩu", ignoreCase = true)

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        shape = RoundedCornerShape(15.dp),
        placeholder = {
            Text(
                text = label,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            )
        }, leadingIcon = {
            if (label.contains("Email", ignoreCase = true)) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email Icon"
                )
            } else if (isPasswordField) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Lock Icon"
                )
            }
        },
        trailingIcon = {
            if (isPasswordField) {
                val iconPainter = if (passwordVisible)
                    painterResource(id = R.drawable.visibility_on)
                else
                    painterResource(id = R.drawable.visibility_off)

                IconButton(onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        painter = iconPainter, contentDescription = "Hiển thị mật khẩu",
                        modifier = Modifier
                            .size(20.dp)

                    )
                }
            }
        },
        modifier = Modifier
            .height(55.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        ),
        singleLine = true,
        visualTransformation = if (isPasswordField && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None

    )
}

@Composable
fun icon_login(iconId: Int, contentDecrep: String, size1: Int, size2: Int) {
    Box(
        modifier = Modifier
            .size(size2.dp)
            .background(Color.White, shape = CircleShape)
            .border(1.dp, Color.LightGray, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(iconId),
            contentDescription = contentDecrep,
            modifier = Modifier
                .size(size1.dp)
        )
    }


}
