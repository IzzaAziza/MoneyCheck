package com.baiqizzaaziza0105.moneycheck.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.baiqizzaaziza0105.moneycheck.R
import com.baiqizzaaziza0105.moneycheck.navigation.Screen
import com.baiqizzaaziza0105.moneycheck.ui.theme.MoneyCheckTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.about_screen),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var pemasukan by rememberSaveable { mutableStateOf("") }
    var pemasukanError by rememberSaveable { mutableStateOf(false) }

    var pengeluaran by rememberSaveable { mutableStateOf("") }
    var pengeluaranError by rememberSaveable { mutableStateOf(false) }

    var punyaTabungan by rememberSaveable { mutableStateOf(false) }
    var jumlahTabungan by rememberSaveable { mutableStateOf("") }

    var hasil by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.money),
            contentDescription = "Money Image",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.MC_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = pemasukan,
            onValueChange = {
                pemasukan = it
                pemasukanError = false
            },
            label = { Text(text = stringResource(R.string.pemasukan)) },
            leadingIcon = { IconPicker(pemasukanError, "Rp")},
            supportingText = { ErrorHint(pemasukanError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = pengeluaran,
            onValueChange = {
                pengeluaran = it
                pengeluaranError = false
            },
            label = {Text(text = stringResource(id = R.string.pengeluaran)) },
            leadingIcon = { IconPicker(pengeluaranError, "Rp") },
            supportingText = {ErrorHint(pengeluaranError)},
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = punyaTabungan,
                onCheckedChange = { punyaTabungan = it },
            )
            Text(text = stringResource(id = R.string.tabungan))
        }
        if (punyaTabungan) {
            OutlinedTextField(
                value = jumlahTabungan,
                onValueChange = { jumlahTabungan = it },
                label = { Text( text =stringResource(R.string.jumlah_tabungan)) },
                leadingIcon = { Text("Rp") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Button(
                onClick = {
                    pemasukanError = pemasukan.isBlank()
                    pengeluaranError = pengeluaran.isBlank()

                    if (pemasukanError || pengeluaranError) return@Button

                    val pemasukanInt = pemasukan.toIntOrNull() ?: 0
                    val pengeluaranInt = pengeluaran.toIntOrNull() ?: 0
                    val tabunganInt = jumlahTabungan.toIntOrNull() ?: 0

                    val sisa = hitungHasil(pemasukanInt, pengeluaranInt, tabunganInt, punyaTabungan)

                    val kategori = if (pemasukanInt != 0) {
                        getKategori(sisa, pemasukanInt, context)
                    } else {
                        "Tidak ada pemasukan"
                    }
                    hasil = "Sisa uang:$sisa\n$kategori"
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.hitung))
            }

            Button(
                onClick = {
                    pemasukan = ""
                    pengeluaran = ""
                    jumlahTabungan = ""
                    punyaTabungan = false

                    pemasukanError = false
                    pengeluaranError = false

                    hasil = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.reset))
            }
        }
        Text(
            text = hasil,
            modifier = Modifier.padding(top = 16.dp),
            textAlign = TextAlign.Center
        )

        val pemasukanInt = pemasukan.toIntOrNull() ?: 0
        val pengeluaranInt = pengeluaran.toIntOrNull() ?: 0
        val tabunganInt = jumlahTabungan.toIntOrNull() ?: 0

        val sisa = hitungHasil(pemasukanInt, pengeluaranInt, tabunganInt, punyaTabungan)
        val kategori = if (pemasukanInt != 0) {
            getKategori(sisa, pemasukanInt, context)
        } else {
            "Tidak ada Pemasukan"
        }
        val message = stringResource(R.string.bagikan_template, pemasukanInt, pengeluaranInt, if (punyaTabungan) tabunganInt else 0, sisa, kategori.uppercase())

        if (hasil.isNotEmpty()) {
            Button(
                onClick = { shareData(context, message) },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.bagikan))
            }
        }
    }
}

private fun hitungHasil(pemasukan: Int, pengeluaran: Int, tabungan: Int, adaTabungan: Boolean): Int {
    return if (adaTabungan) {
        pemasukan - pengeluaran + tabungan
    } else {
        pemasukan - pengeluaran
    }
}

private fun getKategori(sisa: Int, pemasukan: Int, context: Context): String {
    val persentase = (sisa.toFloat() / pemasukan) * 100

    return when {
        persentase >= 70 -> context.getString(R.string.status_aman)
        persentase >= 50 -> context.getString(R.string.status_cukup)
        persentase > 0 -> context.getString(R.string.status_boros)
        else -> context.getString(R.string.status_sangat_boros)
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}
@Composable
fun IconPicker(isError: Boolean, unit: String){
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint (isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.input_invalid))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    MoneyCheckTheme {
        MainScreen(rememberNavController())
    }
}