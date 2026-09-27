package com.example.data.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.ui.theme.*

enum class AgeCategory(val titleAr: String, val titleEn: String, val ageRange: String) {
    ALL("الكل", "All", "جميع الفئات"),
    NURSERY_SCHOOL("الحضانات والمدارس", "Nurseries & Schools", "زي موحد وجملة"),
    INFANTS("حديثي الولادة والرضع", "Infants & Babies", "0 - 2 سنة"),
    TODDLERS("الأطفال الصغار", "Toddlers", "3 - 5 سنوات"),
    KIDS("اليافعين والابتدائي", "Kids & Juniors", "6 - 10 سنوات")
}

enum class ApparelType(val titleAr: String, val titleEn: String) {
    ROMPER("سالوبيت قطني", "Baby Romper"),
    TSHIRT("تيشرت كلاسيكي", "Kids T-Shirt"),
    BIB("مريلة أطفال", "Baby Bib"),
    BEANIE("قبعة مواليد", "Newborn Beanie"),
    HOODIE("هودي شتوي", "Winter Hoodie"),
    DRESS("فستان يومي", "Cotton Dress"),
    NURSERY_UNIFORM("زي موحد للحضانة", "Nursery Uniform"),
    GRADUATION_TEE("تيشرت تخرج الروضة", "Graduation Tee"),
    NURSERY_APRON("مريلة رسم وأنشطة", "Kids Activity Apron"),
    COTTON_TOTE("حقيبة قماشية بالاسم", "School Cotton Tote")
}

data class FabricColorOption(
    val id: String,
    val nameAr: String,
    val color: Color
)

val availableFabricColors = listOf(
    FabricColorOption("white", "أبيض ناصع", FabricWhite),
    FabricColorOption("pastel_blue", "أزرق سماوي ناعم", FabricPastelBlue),
    FabricColorOption("pastel_pink", "وردي باستيل لطيف", FabricPastelPink),
    FabricColorOption("pastel_mint", "نعناعي هادئ", FabricPastelMint),
    FabricColorOption("pastel_yellow", "أصفر ليموني دافئ", FabricPastelYellow),
    FabricColorOption("lavender", "لافندر ساحر", FabricLavender),
    FabricColorOption("heather_gray", "رمادي ميلانج", FabricHeatherGray),
    FabricColorOption("school_navy", "كحلي مدرسي موحد", FabricSchoolNavy),
    FabricColorOption("charcoal", "كحلي فحمي", FabricCharcoal)
)

data class Product(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val category: AgeCategory,
    val apparelType: ApparelType,
    val basePrice: Double, // in SAR
    val rating: Float = 4.95f,
    val reviewsCount: Int = 128,
    val descriptionAr: String,
    val featuresAr: List<String>,
    val sizes: List<String>,
    val defaultSize: String,
    @DrawableRes val blankDrawableRes: Int,
    val isBestSeller: Boolean = false,
    val allowsBackPrint: Boolean = true,
    val isBulkEligible: Boolean = true, // Nurseries & Schools bulk orders
    val bulkDiscountNote: String = "خصم حتى 35% لطلبات الحضانات والمدارس (10 قطع فأكثر)"
)

// Comprehensive catalog including Mothers, Babies, Nurseries, and Schools
val sampleProducts = listOf(
    // 1. قسم الحضانات والمدارس (Nurseries & Schools)
    Product(
        id = "prod_nursery_uniform_01",
        titleAr = "زي الحضانة والروضة الموحد (بشعار الحضانة واسم الطفل)",
        titleEn = "Nursery Class Uniform with Logo & Child Name",
        category = AgeCategory.NURSERY_SCHOOL,
        apparelType = ApparelType.NURSERY_UNIFORM,
        basePrice = 49.0,
        rating = 4.98f,
        reviewsCount = 420,
        descriptionAr = "زي قطني ناعم ومريح معتمد للحضانات ورياض الأطفال، نسيج قطني نقي 100% يتحمل الغسيل اليومي المتكرر. يدعم طباعة شعار الحضانة على الصدر واسم الطفل ورقم الفصل.",
        featuresAr = listOf(
            "قطن ناعم 100% مريح للعب طوال اليوم بالحضانة",
            "ألوان زاهية ثابتة مع تقنية الطباعة الرقمية DTG",
            "طباعة قائمة أسماء طلاب الفصل بلمسة واحدة",
            "خصم خاص للكميات 25% للحضانات والمدارس"
        ),
        sizes = listOf("سنتين (2Y)", "3 سنوات (3Y)", "4 سنوات (4Y)", "5 سنوات (5Y)", "6 سنوات (6Y)"),
        defaultSize = "3 سنوات (3Y)",
        blankDrawableRes = R.drawable.img_blank_tshirt,
        isBestSeller = true
    ),
    Product(
        id = "prod_grad_tee_01",
        titleAr = "تيشرت تخرج أطفال الروضة والتمهيدي 2026",
        titleEn = "Kindergarten Graduation 2026 Celebration Tee",
        category = AgeCategory.NURSERY_SCHOOL,
        apparelType = ApparelType.GRADUATION_TEE,
        basePrice = 45.0,
        rating = 4.96f,
        reviewsCount = 380,
        descriptionAr = "تيشرت تذكاري لحفل تخرج براعم الروضة والتمهيدي، متوفر بطباعة قبعة التخرج وعبارة مبروك التخرج واسم كل طالب وطالبة مع شعار المدرسة.",
        featuresAr = listOf(
            "تصميم احتفالي مبهج مع قبعة التخرج والنجوم",
            "تخصيص الاسم لكل طالب مع اسم الروضة",
            "خامة قطنية فائقة النعومة ومريحة للحفل",
            "تغليف فردي لكل طالب مع بطاقة تهنئة"
        ),
        sizes = listOf("3 - 4 سنوات", "4 - 5 سنوات", "5 - 6 سنوات", "6 - 7 سنوات"),
        defaultSize = "5 - 6 سنوات",
        blankDrawableRes = R.drawable.img_blank_tshirt,
        isBestSeller = true
    ),
    Product(
        id = "prod_school_trip_01",
        titleAr = "تيشرت الأنشطة والرحلات المدرسية واليوم الرياضي",
        titleEn = "School Field Trip & Sports Day Cotton Tee",
        category = AgeCategory.NURSERY_SCHOOL,
        apparelType = ApparelType.TSHIRT,
        basePrice = 42.0,
        rating = 4.93f,
        reviewsCount = 210,
        descriptionAr = "تيشرت قطني مخصص للرحلات المدرسية والفعاليات الرياضية واليوم الوطني، سهل التمييز بألوان موحدة وأرقام أو أسماء الطلاب لسلامتهم وسهولة الإشراف.",
        featuresAr = listOf(
            "ألوان باستيل وزاهية لسهولة تمييز طلاب الفصل",
            "طباعة شعار المدرسة ورقم طوارئ المشرف بالخلف",
            "قماش بارد وخفيف قابل للتنفس أثناء اللعب والنشاط",
            "أسعار جملة مخفضة جداً للمدارس"
        ),
        sizes = listOf("4 - 5 سنوات", "6 - 7 سنوات", "8 - 9 سنوات", "10 سنوات"),
        defaultSize = "6 - 7 سنوات",
        blankDrawableRes = R.drawable.img_blank_tshirt,
        isBestSeller = false
    ),
    Product(
        id = "prod_art_apron_01",
        titleAr = "مريلة الأنشطة والرسم والتلوين لرياض الأطفال",
        titleEn = "Kids Waterproof Art & Activity Apron",
        category = AgeCategory.NURSERY_SCHOOL,
        apparelType = ApparelType.NURSERY_APRON,
        basePrice = 35.0,
        rating = 4.91f,
        reviewsCount = 150,
        descriptionAr = "مريلة أنشطة مدرسية لحماية ملابس الأطفال أثناء حصص الرسم والصلصال وأنشطة الحضانة، مع جيب أمامي وطباعة اسم الطفل وشعار الحضانة.",
        featuresAr = listOf(
            "طبقة مقاومة للبقع سهلة المسح والغسيل",
            "أربطة مرنة سهلة الارتداء للأطفال بمفردهم",
            "جيب أمامي لحفظ أقلام التلوين والفرشاة",
            "طباعة مخصصة باسم الطفل وفصله"
        ),
        sizes = listOf("صغير (2-4 سنوات)", "وسط (5-7 سنوات)"),
        defaultSize = "صغير (2-4 سنوات)",
        blankDrawableRes = R.drawable.img_blank_romper,
        isBestSeller = false
    ),

    // 2. قسم حديثي الولادة والرضع (0 - 2 سنة)
    Product(
        id = "prod_romper_01",
        titleAr = "سالوبيت مواليد قطني بأزرار سفلية ناعمة",
        titleEn = "Organic Cotton Baby Romper / Onesie",
        category = AgeCategory.INFANTS,
        apparelType = ApparelType.ROMPER,
        basePrice = 65.0,
        rating = 4.97f,
        reviewsCount = 340,
        descriptionAr = "قطن مصري ممشط 100% ناعم فائق الجودة، فتحة رقبة متمددة وأزرار سفلية خالية من النيكل لسهولة التبديل. طباعة رقمية DTG بأحبار مائية آمنة لجلد الرضيع.",
        featuresAr = listOf(
            "خامة قطنية 220 GSM ناعمة كالحرير",
            "أزرار كبس خالية من النيكل لسلامة الرضيع",
            "أحبار نباتية عضوية غير مسببة للحساسية",
            "جاهز للغسيل المتكرر دون بهتان"
        ),
        sizes = listOf("0 - 3 أشهر", "3 - 6 أشهر", "6 - 12 شهر", "12 - 18 شهر", "18 - 24 شهر"),
        defaultSize = "3 - 6 أشهر",
        blankDrawableRes = R.drawable.img_blank_romper,
        isBestSeller = true
    ),
    Product(
        id = "prod_bib_01",
        titleAr = "مريلة أطفال قطنية ناعمة بطبقتين عازلتين",
        titleEn = "Double-Layer Cotton Baby Feeding Bib",
        category = AgeCategory.INFANTS,
        apparelType = ApparelType.BIB,
        basePrice = 35.0,
        rating = 4.92f,
        reviewsCount = 110,
        descriptionAr = "مريلة أطفال ناعمة من القطن الخالص الماص لامتصاص اللعاب وحماية ملابس الرضيع مع إمكانية طباعة الاسم وعبارات لطيفة وظريفة أو شعار الحضانة.",
        featuresAr = listOf(
            "طبقة أمامية قطنية ناعمة",
            "طبقة خلفية ماصة ومريحة",
            "إغلاق كبس مريح بمقاسين قابل للتعديل",
            "مناسبة كهدية سبوع وميلاد وحضانات الرضع"
        ),
        sizes = listOf("مقاس موحد (0-24 شهر)"),
        defaultSize = "مقاس موحد (0-24 شهر)",
        blankDrawableRes = R.drawable.img_blank_romper,
        isBestSeller = false
    ),

    // 3. قسم الأطفال الصغار (3 - 5 سنوات)
    Product(
        id = "prod_tshirt_01",
        titleAr = "تيشرت أطفال قطني كلاسيكي مبهج",
        titleEn = "Classic Toddler Cotton T-Shirt",
        category = AgeCategory.TODDLERS,
        apparelType = ApparelType.TSHIRT,
        basePrice = 55.0,
        rating = 4.90f,
        reviewsCount = 260,
        descriptionAr = "تيشرت قطني كلاسيكي مريح للأطفال الصغار بحياكة مزدوجة قوية وياقة مريحة، مثالي للحفلات وأيام الحضانة والأنشطة اليومية والطباعة بالاسم والرسمة المحببة.",
        featuresAr = listOf(
            "قطن ناعم 100% قابل للتنفس ومريح",
            "قصة واسعة مريحة للحركة واللعب",
            "مقاوم للانكماش والبهتان بعد الغسيل",
            "طباعة أمامية وخلفية متاحة"
        ),
        sizes = listOf("سنتين (2Y)", "3 سنوات (3Y)", "4 سنوات (4Y)", "5 سنوات (5Y)"),
        defaultSize = "3 سنوات (3Y)",
        blankDrawableRes = R.drawable.img_blank_tshirt,
        isBestSeller = true
    ),
    Product(
        id = "prod_dress_01",
        titleAr = "فستان قطني كاجوال بأكمام قصيرة للبنات",
        titleEn = "Toddler Casual Cotton Dress",
        category = AgeCategory.TODDLERS,
        apparelType = ApparelType.DRESS,
        basePrice = 79.0,
        rating = 4.89f,
        reviewsCount = 92,
        descriptionAr = "فستان بناتي ناعم وقابل للتمدد بقصة تنورة خفيفة، مثالي للاحتفال بأعياد الميلاد الأولى وحفلات الروضة بطباعة التاج والاسم وأرقام العمر.",
        featuresAr = listOf(
            "قطن عضوي خفيف ولطيف على البشرة",
            "قصة واسعة لطيفة وسهلة اللبس",
            "ألوان باستيل هادئة تدوم طويلاً",
            "تصاميم تيجان وزهور مخصصة"
        ),
        sizes = listOf("18 - 24 شهر", "2 - 3 سنوات", "4 - 5 سنوات"),
        defaultSize = "2 - 3 سنوات",
        blankDrawableRes = R.drawable.img_blank_tshirt,
        isBestSeller = false
    ),

    // 4. قسم اليافعين (6 - 10 سنوات)
    Product(
        id = "prod_hoodie_01",
        titleAr = "هودي أطفال شتوي بقلنسوة وجيب دافئ",
        titleEn = "Kids Cozy Fleece Pullover Hoodie",
        category = AgeCategory.KIDS,
        apparelType = ApparelType.HOODIE,
        basePrice = 110.0,
        rating = 4.94f,
        reviewsCount = 185,
        descriptionAr = "سويت شيرت هودي شتوي مبطن بفرو ناعم دافئ وخفيف الوزن، قلنسوة مزدوجة مع جيب كنغر عملي، رائع لطباعة اسم الطفل وشخصيته المفضلة أو شعار مدرسته.",
        featuresAr = listOf(
            "صوف قطني مبطن دافئ وعالي الجودة",
            "قلنسوة آمنة بدون حبال لسلامة الطفل بالمدرسة",
            "طباعة DTF بارزة وعالية التباين",
            "حواف مطاطية مريحة عند المعصم"
        ),
        sizes = listOf("6 - 7 سنوات", "8 - 9 سنوات", "10 سنوات"),
        defaultSize = "6 - 7 سنوات",
        blankDrawableRes = R.drawable.img_blank_tshirt,
        isBestSeller = true
    )
)
