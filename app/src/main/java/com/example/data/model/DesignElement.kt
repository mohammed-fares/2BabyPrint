package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class DesignElementType {
    TEXT,
    CLIPART,
    IMAGE_STICKER
}

enum class ArabicFontStyle(val displayName: String, val sampleText: String) {
    PLAYFUL("كارتوني مرح للأطفال", "ريان البطل"),
    ELEGANT("ديواني فاخر للمناسبات", "فاطمة الزهراء"),
    MODERN("عصري بسيط وواضح", "حضانة أزهار الغد"),
    BOLD_KIDS("عريض للمدارس والأنشطة", "CLASS OF 2026")
}

data class ClipartItem(
    val id: String,
    val nameAr: String,
    val category: String,
    val iconEmoji: String,
    val defaultColor: Color = Color(0xFFFF758F)
)

val sampleClipartLibrary = listOf(
    // قسم الحضانات والمدارس ورياض الأطفال (NEW)
    ClipartItem("clip_grad_cap", "قبعة التخرج", "الحضانات والمدارس", "🎓", Color(0xFF118AB2)),
    ClipartItem("clip_school_bus", "باص المدرسة الأصفر", "الحضانات والمدارس", "🚌", Color(0xFFFFD166)),
    ClipartItem("clip_school_building", "مبنى المدرسة / الروضة", "الحضانات والمدارس", "🏫", Color(0xFF4361EE)),
    ClipartItem("clip_art_palette", "لوحة الرسم والألوان", "الحضانات والمدارس", "🎨", Color(0xFFFF758F)),
    ClipartItem("clip_books", "كتب وأقلام الدراسة", "الحضانات والمدارس", "📚", Color(0xFF06D6A0)),
    ClipartItem("clip_trophy", "كأس التفوق والنشاط", "الحضانات والمدارس", "🏆", Color(0xFFFFD166)),
    ClipartItem("clip_medal", "ميدالية الطالب المثالي", "الحضانات والمدارس", "🥇", Color(0xFFFFD166)),
    ClipartItem("clip_backpack", "حقيبة الروضة والمدرسة", "الحضانات والمدارس", "🎒", Color(0xFF118AB2)),
    ClipartItem("clip_apple", "تفاحة المعلمة", "الحضانات والمدارس", "🍎", Color(0xFFE63946)),

    // أعياد الميلاد والاحتفالات
    ClipartItem("clip_cake", "كعكة ميلاد", "أعياد الميلاد", "🎂", Color(0xFFFF758F)),
    ClipartItem("clip_balloons", "بالونات ملونة", "أعياد الميلاد", "🎈", Color(0xFF4EA8DE)),
    ClipartItem("clip_party_hat", "قبعة احتفال", "أعياد الميلاد", "🎉", Color(0xFFFFD166)),
    ClipartItem("clip_one", "رقم 1 (أول سنة)", "أعياد الميلاد", "🥇", Color(0xFFFFD166)),
    ClipartItem("clip_gift", "صندوق هدية", "أعياد الميلاد", "🎁", Color(0xFF70D6BC)),

    // حيوانات الغابة اللطيفة
    ClipartItem("clip_lion", "أسد الغابة الصغير", "حيوانات الغابة", "🦁", Color(0xFFFFD166)),
    ClipartItem("clip_bear", "دبدوب محبوب", "حيوانات الغابة", "🐻", Color(0xFF8D6E63)),
    ClipartItem("clip_bunny", "أرنوب لطيف", "حيوانات الغابة", "🐰", Color(0xFFFFB4C2)),
    ClipartItem("clip_panda", "باندا هادئ", "حيوانات الغابة", "🐼", Color(0xFF2B2D42)),
    ClipartItem("clip_fox", "ثعلوب ذكي", "حيوانات الغابة", "🦊", Color(0xFFE76F51)),
    ClipartItem("clip_giraffe", "زرافة مرحة", "حيوانات الغابة", "🦒", Color(0xFFFFD166)),

    // تيجان ونجوم
    ClipartItem("clip_crown_gold", "تاج ملكي ذهبي", "تيجان ونجوم", "👑", Color(0xFFFFD166)),
    ClipartItem("clip_star", "نجمة التميز اللامعة", "تيجان ونجوم", "⭐", Color(0xFFFFD166)),
    ClipartItem("clip_sparkles", "بريق ووميض", "تيجان ونجوم", "✨", Color(0xFFFFB4C2)),
    ClipartItem("clip_heart", "قلب حب حنون", "تيجان ونجوم", "💖", Color(0xFFFF758F)),
    ClipartItem("clip_rainbow", "قوس قزح باستيل", "تيجان ونجوم", "🌈", Color(0xFF4EA8DE)),

    // رمضان والأعياد
    ClipartItem("clip_crescent", "هلال العيد ورمضان", "رمضان والأعياد", "🌙", Color(0xFFFFD166)),
    ClipartItem("clip_lantern", "فانوس رمضان", "رمضان والأعياد", "🏮", Color(0xFFFF758F)),
    ClipartItem("clip_sheep", "خروف العيد الظريف", "رمضان والأعياد", "🐑", Color(0xFF70D6BC)),

    // سبوع ومواليد
    ClipartItem("clip_baby", "ملاك المواليد", "سبوع ومواليد", "👶", Color(0xFFFFB4C2)),
    ClipartItem("clip_bottle", "رضاعة الحليب", "سبوع ومواليد", "🍼", Color(0xFF4EA8DE)),
    ClipartItem("clip_stroller", "عربة بيبي", "سبوع ومواليد", "🚼", Color(0xFF70D6BC)),
    ClipartItem("clip_footprints", "أقدام صغيرة", "سبوع ومواليد", "👣", Color(0xFFFF758F))
)

data class DesignElement(
    val id: String,
    val type: DesignElementType,
    val content: String,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val scale: Float = 1.0f,
    val rotation: Float = 0f,
    val colorHex: Long = 0xFF2B2D42,
    val fontStyle: ArabicFontStyle = ArabicFontStyle.PLAYFUL,
    val fontSizeSp: Float = 22f,
    val isCurved: Boolean = false,
    val isBold: Boolean = true
)

data class TemplatePreset(
    val id: String,
    val titleAr: String,
    val categoryName: String, // "الحضانات والمدارس", "أعياد الميلاد", "رمضان والأعياد", "سبوع ومواليد"
    val recommendedApparelType: ApparelType,
    val defaultFabricColorId: String,
    val previewEmoji: String,
    val elements: List<DesignElement>,
    val targetAudience: String = "الأطفال والأمهات والحضانات"
)

val sampleTemplates = listOf(
    // قوالب الحضانات والمدارس
    TemplatePreset(
        id = "tpl_school_grad_01",
        titleAr = "حفل تخرج الروضة 2026",
        categoryName = "الحضانات والمدارس",
        recommendedApparelType = ApparelType.GRADUATION_TEE,
        defaultFabricColorId = "white",
        previewEmoji = "🎓",
        targetAudience = "رياض الأطفال والمدارس",
        elements = listOf(
            DesignElement(
                id = "sg_1",
                type = DesignElementType.CLIPART,
                content = "🎓",
                offsetX = 0f,
                offsetY = -45f,
                scale = 1.4f
            ),
            DesignElement(
                id = "sg_2",
                type = DesignElementType.TEXT,
                content = "خريج الروضة الصغير",
                offsetX = 0f,
                offsetY = 5f,
                fontStyle = ArabicFontStyle.BOLD_KIDS,
                fontSizeSp = 22f,
                colorHex = 0xFF118AB2
            ),
            DesignElement(
                id = "sg_3",
                type = DesignElementType.TEXT,
                content = "روضة براعم المستقبل • 2026",
                offsetX = 0f,
                offsetY = 38f,
                fontStyle = ArabicFontStyle.MODERN,
                fontSizeSp = 14f,
                colorHex = 0xFF6C757D
            )
        )
    ),
    TemplatePreset(
        id = "tpl_nursery_stars_01",
        titleAr = "فصل النجوم - حضانة وروضة أزهار الغد",
        categoryName = "الحضانات والمدارس",
        recommendedApparelType = ApparelType.NURSERY_UNIFORM,
        defaultFabricColorId = "pastel_yellow",
        previewEmoji = "🌟",
        targetAudience = "الحضانات والزي الموحد",
        elements = listOf(
            DesignElement(
                id = "ns_1",
                type = DesignElementType.CLIPART,
                content = "🌟",
                offsetX = 0f,
                offsetY = -40f,
                scale = 1.3f
            ),
            DesignElement(
                id = "ns_2",
                type = DesignElementType.TEXT,
                content = "أبطال فصل النجوم",
                offsetX = 0f,
                offsetY = 10f,
                fontStyle = ArabicFontStyle.PLAYFUL,
                fontSizeSp = 22f,
                colorHex = 0xFF2B2D42
            ),
            DesignElement(
                id = "ns_3",
                type = DesignElementType.TEXT,
                content = "حضانة أزهار الغد السعيدة",
                offsetX = 0f,
                offsetY = 42f,
                fontStyle = ArabicFontStyle.MODERN,
                fontSizeSp = 14f,
                colorHex = 0xFFFF758F
            )
        )
    ),
    TemplatePreset(
        id = "tpl_school_trip_01",
        titleAr = "رحلة مدرسية ممتعة 🚌",
        categoryName = "الحضانات والمدارس",
        recommendedApparelType = ApparelType.TSHIRT,
        defaultFabricColorId = "pastel_blue",
        previewEmoji = "🚌",
        targetAudience = "الرحلات والأنشطة المدرسية",
        elements = listOf(
            DesignElement(
                id = "st_1",
                type = DesignElementType.CLIPART,
                content = "🚌",
                offsetX = 0f,
                offsetY = -45f,
                scale = 1.3f
            ),
            DesignElement(
                id = "st_2",
                type = DesignElementType.TEXT,
                content = "رحلتنا المدرسية الممتعة",
                offsetX = 0f,
                offsetY = 8f,
                fontStyle = ArabicFontStyle.PLAYFUL,
                fontSizeSp = 20f,
                colorHex = 0xFF118AB2
            ),
            DesignElement(
                id = "st_3",
                type = DesignElementType.TEXT,
                content = "مدارس رواد الإبداع",
                offsetX = 0f,
                offsetY = 40f,
                fontStyle = ArabicFontStyle.MODERN,
                fontSizeSp = 15f,
                colorHex = 0xFF6C757D
            )
        )
    ),

    // قوالب الأمهات والمواليد والأطفال
    TemplatePreset(
        id = "tpl_birthday_01",
        titleAr = "أميرتي كبرت سنة",
        categoryName = "أعياد الميلاد",
        recommendedApparelType = ApparelType.ROMPER,
        defaultFabricColorId = "pastel_pink",
        previewEmoji = "👑",
        targetAudience = "الأمهات والأطفال",
        elements = listOf(
            DesignElement(
                id = "t_1",
                type = DesignElementType.CLIPART,
                content = "👑",
                offsetX = 0f,
                offsetY = -45f,
                scale = 1.3f
            ),
            DesignElement(
                id = "t_2",
                type = DesignElementType.TEXT,
                content = "أميرتي كبرت سنة",
                offsetX = 0f,
                offsetY = 5f,
                fontStyle = ArabicFontStyle.PLAYFUL,
                fontSizeSp = 24f,
                colorHex = 0xFFFF758F
            ),
            DesignElement(
                id = "t_3",
                type = DesignElementType.TEXT,
                content = "Happy 1st Birthday",
                offsetX = 0f,
                offsetY = 40f,
                fontStyle = ArabicFontStyle.MODERN,
                fontSizeSp = 15f,
                colorHex = 0xFF6C757D
            )
        )
    ),
    TemplatePreset(
        id = "tpl_ramadan_01",
        titleAr = "رمضان أحلى مع ريان",
        categoryName = "رمضان والأعياد",
        recommendedApparelType = ApparelType.ROMPER,
        defaultFabricColorId = "pastel_blue",
        previewEmoji = "🌙",
        targetAudience = "الأمهات والمناسبات العائلية",
        elements = listOf(
            DesignElement(
                id = "r_1",
                type = DesignElementType.CLIPART,
                content = "🌙",
                offsetX = 0f,
                offsetY = -48f,
                scale = 1.4f
            ),
            DesignElement(
                id = "r_2",
                type = DesignElementType.TEXT,
                content = "رمضان أحلى مع",
                offsetX = 0f,
                offsetY = -5f,
                fontStyle = ArabicFontStyle.ELEGANT,
                fontSizeSp = 22f,
                colorHex = 0xFF118AB2
            ),
            DesignElement(
                id = "r_3",
                type = DesignElementType.TEXT,
                content = "ريان 👶",
                offsetX = 0f,
                offsetY = 32f,
                fontStyle = ArabicFontStyle.BOLD_KIDS,
                fontSizeSp = 26f,
                colorHex = 0xFFFFD166
            )
        )
    ),
    TemplatePreset(
        id = "tpl_sebouh_01",
        titleAr = "وصل نبينا وقمر دنيانا",
        categoryName = "سبوع ومواليد",
        recommendedApparelType = ApparelType.ROMPER,
        defaultFabricColorId = "pastel_mint",
        previewEmoji = "👶",
        targetAudience = "المواليد الجدد والسبوع",
        elements = listOf(
            DesignElement(
                id = "s_1",
                type = DesignElementType.CLIPART,
                content = "⭐",
                offsetX = 0f,
                offsetY = -45f,
                scale = 1.2f
            ),
            DesignElement(
                id = "s_2",
                type = DesignElementType.TEXT,
                content = "قمر دنيانا وصل",
                offsetX = 0f,
                offsetY = 5f,
                fontStyle = ArabicFontStyle.ELEGANT,
                fontSizeSp = 24f,
                colorHex = 0xFF70D6BC
            ),
            DesignElement(
                id = "s_3",
                type = DesignElementType.TEXT,
                content = "نورت عائلتنا يا صغيري",
                offsetX = 0f,
                offsetY = 40f,
                fontStyle = ArabicFontStyle.MODERN,
                fontSizeSp = 15f,
                colorHex = 0xFF6C757D
            )
        )
    )
)
