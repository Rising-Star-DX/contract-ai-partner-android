package com.poscodx.contract_ai_partner.data.mapper

import com.poscodx.contract_ai_partner.R
import com.poscodx.contract_ai_partner.domain.model.Contract
import com.poscodx.contract_ai_partner.feature.contractlist.DocumentItemUi
import java.time.format.DateTimeFormatter

fun Contract.toUi(): DocumentItemUi =
    DocumentItemUi(
        fileTypeIcon = when (fileType) {
            "PDF" -> R.drawable.ic_pdf
            "DOCX" -> R.drawable.ic_doc
            "JPEG", "PNG" -> R.drawable.ic_jpg
            else -> R.drawable.ic_pdf
        },
        fileName   = name,
        department = category,
        dateTime   = createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd a h:mm:ss")),
        aiStatus   = aiStatus.name.replace('_', ' ')
    )