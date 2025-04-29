package com.poscodx.contract_ai_partner.ui.upload

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.poscodx.contract_ai_partner.R
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun UploadContractScreen() {
    // ───────── 상태 ─────────
    val categories = listOf("R&D", "가맹사업법", "공사", "구매", "법령", "인사")
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // 카메라 촬영 시 사용할 임시 파일 Uri
    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File.createTempFile("contract_", ".jpg", context.cacheDir)
        )
    }

    // 문서 스캐너 옵션
    val scannerOptions = remember {
        GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(1)
            .setScannerMode(SCANNER_MODE_FULL)          // 전체(자동 캡처 + 편집 UI)
            .setResultFormats(RESULT_FORMAT_JPEG)       // 필요하면 PDF도 추가
            .build()
    }

    /* ───── 런처: 갤러리 ───── */
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) imageUri = uri
        }

    /* ───── 런처: 카메라 ───── */
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) imageUri = photoUri
        }

    /* ───── 런처: 문서 스캐너 ───── */
    val scanner = remember { GmsDocumentScanning.getClient(scannerOptions) }
    val docScanLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { res ->
            if (res.resultCode == Activity.RESULT_OK) {
                GmsDocumentScanningResult.fromActivityResultIntent(res.data)?.let { r ->
                    val firstPageUri = r.pages?.firstOrNull()?.imageUri
                    if (firstPageUri != null) imageUri = firstPageUri   // Glide 미리보기로 교체
                }
            }
        }

    /* ───── 런처: 권한 ───── */
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantedMap ->
            val cameraOk = grantedMap[Manifest.permission.CAMERA] == true
            val readOk   = grantedMap[Manifest.permission.READ_MEDIA_IMAGES] == true ||
                    grantedMap[Manifest.permission.READ_EXTERNAL_STORAGE] == true

            if (cameraOk && readOk) {
                // 권한 얻었으니 → 스캐너 먼저
                tryLaunchScanner(
                    scanner,
                    activity = context as Activity,
                    onFail = {
                        showImageSourcePicker(
                            context,
                            onGallery = { galleryLauncher.launch("image/*") },
                            onCamera  = { cameraLauncher.launch(photoUri) }
                        )
                    },
                    launcher = docScanLauncher
                )
            }
        }

    // ───────── UI ─────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        /* 1) 카테고리 선택 */
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "카테고리 선택",
                fontSize = 20.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Bold
            )
            Text("계약서의 카테고리를 선택해주세요.", fontSize = 14.sp, color = Color(0xFFAAAAAA))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedCategory ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(56.dp),               // 시안 높이
                    placeholder = { Text("카테고리를 선택하세요") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFAAAAAA),
                        focusedBorderColor = colorResource(R.color.primary),
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                selectedCategory = cat
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        /* 2) 이미지 선택 */
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "이미지 선택",
                fontSize = 20.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Bold
            )
            Text("계약서의 이미지를 선택해주세요.", fontSize = 14.sp, color = Color(0xFFAAAAAA))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)                        // 시안 기준 임의 높이
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8EEFF))        // 연한 파란 배경
                    .clickable {
                        /* ------- ① 권한 보유 여부 확인 ------- */
                        val needsCamera = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED

                        // ★ 수정: API 33(TIRAMISU) 기준 분기
                        val needsRead =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_MEDIA_IMAGES        // 33+
                                ) != PackageManager.PERMISSION_GRANTED
                            } else {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_EXTERNAL_STORAGE    // 32 이하
                                ) != PackageManager.PERMISSION_GRANTED
                            }

                        /* ------- ② 권한 배열 준비 ------- */
                        val permList = buildList {
                            if (needsCamera) add(Manifest.permission.CAMERA)
                            if (needsRead) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    add(Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    add(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }
                        }

                        /* ------- ③ 런처 호출 or 다이얼로그 ------- */
                        if (permList.isNotEmpty()) {
                            permissionLauncher.launch(permList.toTypedArray())
                        } else {
                            tryLaunchScanner(
                                scanner,
                                activity = context as Activity,
                                onFail = {
                                    showImageSourcePicker(
                                        context,
                                        onGallery = { galleryLauncher.launch("image/*") },
                                        onCamera  = { cameraLauncher.launch(photoUri) }
                                    )
                                },
                                launcher = docScanLauncher
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri == null) {
                    Icon(
                        painter = painterResource(R.drawable.ic_image),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = Color(0xFF9CC3FF)
                    )
                } else {
                    GlideImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

/* ───── 이미지 소스 선택 다이얼로그 ───── */
private fun showImageSourcePicker(
    context: Context,
    onGallery: () -> Unit,
    onCamera: () -> Unit
) {
    val items = arrayOf("갤러리에서 선택", "카메라로 촬영")
    AlertDialog.Builder(context)
        .setTitle("이미지 소스 선택")
        .setItems(items) { dialog, which ->
            when (which) {
                0 -> onGallery()
                1 -> onCamera()
            }
            dialog.dismiss()
        }.show()
}

private fun tryLaunchScanner(
    scanner: GmsDocumentScanner,
    activity: Activity,
    onFail: () -> Unit,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    scanner.getStartScanIntent(activity)
        .addOnSuccessListener { intentSender ->
            launcher.launch(IntentSenderRequest.Builder(intentSender).build())
        }
        .addOnFailureListener { onFail() }   // 스캐너 준비 실패 → 폴백
}