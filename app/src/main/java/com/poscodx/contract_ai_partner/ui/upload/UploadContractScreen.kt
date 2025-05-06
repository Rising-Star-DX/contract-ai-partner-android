package com.poscodx.contract_ai_partner.ui.upload

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.mlkit.vision.documentscanner.*
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.*
import com.poscodx.contract_ai_partner.R
import java.io.File

/* ───────── Composable ───────── */
@Composable
fun UploadContractScreen() {

    /* ---------- ViewModel ---------- */
    val vm: UploadViewModel = hiltViewModel()
    val context = LocalContext.current

    /* ---------- UI 상태 ---------- */
    var expanded by remember { mutableStateOf(false) }
    val catNames = vm.categories.map { it.name }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    /* ---------- 파일Provider용 임시 파일 ---------- */
    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File.createTempFile("contract_", ".jpg", context.cacheDir)
        )
    }

    /* ---------- MLKit 문서 스캐너 옵션 ---------- */
    val scannerOptions = remember {
        GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(1)
            .setScannerMode(SCANNER_MODE_FULL)
            .setResultFormats(RESULT_FORMAT_JPEG)
            .build()
    }
    val scanner = remember { GmsDocumentScanning.getClient(scannerOptions) }

    /* ---------- ActivityResult 런처 모음 ---------- */
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { imageUri = it } }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success -> if (success) imageUri = photoUri }

    val docScanLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            GmsDocumentScanningResult
                .fromActivityResultIntent(res.data)
                ?.pages
                ?.firstOrNull()
                ?.imageUri
                ?.let { imageUri = it }
        }
    }

    /* ---------- 권한 런처 ---------- */
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val cameraOk = grants[Manifest.permission.CAMERA] == true
        val readOk = grants[Manifest.permission.READ_MEDIA_IMAGES] == true ||
                grants[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        if (cameraOk && readOk) {
            launchScannerOrPicker(
                scanner, context as Activity, docScanLauncher,
                galleryLauncher, cameraLauncher, photoUri
            )
        }
    }

    /* ---------- Column(화면) ---------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        /* ===== ① 카테고리 선택 ===== */
        CategorySelector(
            expanded = expanded,
            catNames = catNames,
            selectedName = vm.selectedCategory?.name ?: "",
            onExpandToggle = { expanded = !expanded },
            onSelect = { name ->
                vm.onCategorySelected(name)
                expanded = false
            }
        )

        /* ===== ② 이미지 선택 ===== */
        ImageSelector(
            imageUri = imageUri,
            onClick = {
                requestImagePermissions(
                    context, permissionLauncher,
                    scanner, docScanLauncher,
                    galleryLauncher, cameraLauncher, photoUri
                )
            }
        )
    }

    /* ---------- 이미지 Bytes 변환 & ViewModel 저장 ---------- */
    LaunchedEffect(imageUri) {
        imageUri?.let { uri ->
            // ① Uri → ByteArray
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: return@LaunchedEffect

            // ② cacheDir 에 임시 파일 생성 (확장자는 분석과 무관, jpg 로 통일해도 OK)
            val tmp = File.createTempFile("agreement_", ".jpg", context.cacheDir)
            tmp.writeBytes(bytes)

            // ③ ViewModel 에 File 전달
            vm.onImageSelected(tmp)          // ← File 전달
        }
    }
}

/* ===================== UI 컴포넌트 ===================== */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelector(
    expanded: Boolean,
    catNames: List<String>,
    selectedName: String,
    onExpandToggle: () -> Unit,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("카테고리 선택", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666))
        Text("계약서의 카테고리를 선택해주세요.", fontSize = 14.sp, color = Color(0xFFAAAAAA))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandToggle() },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedName,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("카테고리를 선택하세요") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFAAAAAA),
                    focusedBorderColor = colorResource(R.color.primary),
                    unfocusedContainerColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(expanded, onDismissRequest = { onExpandToggle() }) {
                catNames.forEach { name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = { onSelect(name) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ImageSelector(
    imageUri: Uri?,
    onClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("이미지 선택", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF666666))
        Text("계약서의 이미지를 선택해주세요.", fontSize = 14.sp, color = Color(0xFFAAAAAA))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE8EEFF))
                .clickable { onClick() },
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

/* ===================== 권한 & 스캐너 헬퍼 ===================== */
private fun requestImagePermissions(
    context: Context,
    launcher: ActivityResultLauncher<Array<String>>,
    scanner: GmsDocumentScanner,
    docLauncher: ActivityResultLauncher<IntentSenderRequest>,
    galleryLauncher: ActivityResultLauncher<String>,
    cameraLauncher: ActivityResultLauncher<Uri>,
    photoUri: Uri
) {
    val needsCamera = ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) != PackageManager.PERMISSION_GRANTED

    val needsRead = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_MEDIA_IMAGES
        ) != PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    }

    val perms = buildList {
        if (needsCamera) add(Manifest.permission.CAMERA)
        if (needsRead) {
            add(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Manifest.permission.READ_MEDIA_IMAGES
                else
                    Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    if (perms.isNotEmpty()) {
        launcher.launch(perms.toTypedArray())
    } else {
        launchScannerOrPicker(
            scanner, context as Activity, docLauncher,
            galleryLauncher, cameraLauncher, photoUri
        )
    }
}

private fun launchScannerOrPicker(
    scanner: GmsDocumentScanner,
    activity: Activity,
    docLauncher: ActivityResultLauncher<IntentSenderRequest>,
    galleryLauncher: ActivityResultLauncher<String>,
    cameraLauncher: ActivityResultLauncher<Uri>,
    photoUri: Uri
) {
    scanner.getStartScanIntent(activity)
        .addOnSuccessListener { docLauncher.launch(IntentSenderRequest.Builder(it).build()) }
        .addOnFailureListener {
            showImageSourcePicker(
                activity,
                onGallery = { galleryLauncher.launch("image/*") },
                onCamera = { cameraLauncher.launch(photoUri) }
            )
        }
}

/* ===================== 이미지 소스 선택 다이얼로그 ===================== */
private fun showImageSourcePicker(
    context: Context,
    onGallery: () -> Unit,
    onCamera: () -> Unit
) {
    val items = arrayOf("갤러리에서 선택", "카메라로 촬영")

    // 🔹 플랫폼 AlertDialog 명시 (패키지 충돌 방지)
    android.app.AlertDialog.Builder(context)
        .setTitle("이미지 소스 선택")
        .setItems(items) { dialog, which ->
            when (which) {
                0 -> onGallery()
                1 -> onCamera()
            }
            dialog.dismiss()
        }
        .show()
}