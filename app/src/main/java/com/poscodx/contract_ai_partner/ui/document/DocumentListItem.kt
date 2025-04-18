package com.poscodx.contract_ai_partner.ui.document

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poscodx.contract_ai_partner.R

@Composable
fun DocumentListItem(document: DocumentItemUi) {
    // 완료=파란색(#2962FF 근사값), 실패=빨간색(#FF5252 근사값)
    val statusColor =
        if (document.aiStatus == "완료") colorResource(R.color.primary) else Color(0xFFCD6155)
    val statusText = if (document.aiStatus == "완료") "AI 분석 완료" else "AI 분석 실패"

    // 아이템 배경색(이미지 보면 왼쪽만 약간 파란 배경)
    // 여기서는 Row 전체를 Card로 감싸고, 왼쪽 부분만 별도 Box 배경
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF)), // 약간의 라이트 블루 느낌
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
                    .background(Color(0xFFE8F2FF)) // 왼쪽 파란 배경
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(document.fileTypeIcon),
                    contentDescription = "Document Thumbnail",
                    modifier = Modifier.size(40.dp)
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF333333)
                )
                Text(
                    text = document.department, fontSize = 12.sp, color = Color(0xFF888888)
                )
                Text(
                    text = document.dateTime, fontSize = 12.sp, color = Color(0xFF888888)
                )
            }

            // 상태 뱃지
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50)) // 모서리를 조금 둥글게 하고 싶다면 corner
                    .background(statusColor)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = statusText, color = Color.White, fontSize = 12.sp
                )
            }
        }
    }
}