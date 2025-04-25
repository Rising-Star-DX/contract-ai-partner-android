package com.poscodx.contract_ai_partner.feature.contractlist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poscodx.contract_ai_partner.R
import com.poscodx.contract_ai_partner.feature.contractlist.DocumentItemUi
import com.poscodx.contract_ai_partner.ui.theme.NanumSquareNeoFontFamily

@Composable
fun DocumentListItem(
    document: DocumentItemUi,
    onClick: (Long) -> Unit = {}
) {
    val isSuccess = document.aiStatus == "SUCCESS"
    val statusColor = if (isSuccess) colorResource(R.color.primary)
    else Color(0xFFCD6155)        // 실패 색상
    val statusIcon = if (isSuccess) R.drawable.ic_success
    else R.drawable.ic_error

    // 아이템 배경색
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(document.id) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFE8F2FF)), // 왼쪽 파란 배경
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(document.fileTypeIcon),
                    contentDescription = "Document Thumbnail",
                    modifier = Modifier.size(40.dp)
                )

                Icon(
                    painter = painterResource(statusIcon),
                    contentDescription = "statusDesc",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            color = statusColor,
                            shape = CircleShape
                        )
                        .padding(2.dp)           // 테두리 두께
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp, horizontal = 8.dp),
            ) {
                Text(
                    text = document.fileName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = NanumSquareNeoFontFamily,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF212121)
                )
                Text(
                    text = document.department, fontSize = 12.sp, color = Color(0xFF888888)
                )
                Text(
                    text = document.dateTime, fontSize = 12.sp, color = Color(0xFF888888)
                )
            }
        }
    }
}