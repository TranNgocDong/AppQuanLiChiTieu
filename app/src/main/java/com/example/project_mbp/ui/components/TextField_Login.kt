package com.example.project_mbp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_mbp.R
import kotlinx.coroutines.delay

@Composable
fun TextField_Custom(label: String, value: String, onChange: (String) -> Unit) {
    val checkIconPass = label.contains("Mật khẩu", ignoreCase = true)
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        shape = RoundedCornerShape(15.dp),
        placeholder = {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            )
        },
        leadingIcon = {
            if (label.contains("Email", ignoreCase = true)) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Icon_Email",
                    modifier = Modifier.size(23.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Icon_Lock",
                    modifier = Modifier.size(23.dp)

                )
            }
        },
        trailingIcon = {

            if (checkIconPass) {
                val iconPainter = if (passwordVisible) {
                    painterResource(id = R.drawable.visibility_on)
                } else {
                    painterResource(id = R.drawable.visibility_off)
                }

                IconButton(onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        painter = iconPainter,
                        contentDescription = "Icon_Visible",
                        Modifier.size(20.dp)
                    )
                    if (passwordVisible == true)
                        LaunchedEffect(passwordVisible) {
                            delay(3000)
                            passwordVisible = false
                        }
                }

            }
        },
        modifier = Modifier
            .height(56.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.LightGray,
        ),
        singleLine = true,
        visualTransformation = if (checkIconPass && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }

    )
}