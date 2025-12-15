package com.example.mobilappcaseemarket.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.model.FavouriteItem


//Bu anotasyon Room’un çalışması için zorunlu.
@Database( //Bu sınıf bir veritabanı sınıfıdır (RoomDatabase)
    entities = [CartItem::class, FavouriteItem::class], //Hangi tablolar var?
    version = 4, //Bir entity değişirse versiyon artmalı.
    exportSchema = false //Room’un DB şema dosyası üretmemesini söyler.
)

//AppDatabase, Room’un ana veritabanı sınıfıdır.
//Neden abstract?
// Room bu sınıfın gerçek implementasyonunu otomatik olarak compile sırasında üretir. Biz sadece DAO bağlantılarını tanımlarız.
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun favouriteDao(): FavouriteDao

    companion object { //Bir uygulamada tek bir veritabanı instance’ı olmalıdır.
        @Volatile //Thread-safe yapar. Yani çoklu thread erişiminde bozulma olmaz. Aynı anda 2 thread DB oluşturmasın diye garanti eder
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase { //Eğer DB instance yoksa → oluştur ---- Eğer varsa → aynısını geri döndür
            return INSTANCE ?: synchronized(this) { //INSTANCE yoksa, kilidi kapat,  aynı anda başka thread DB yaratamasın.

                val instance = Room.databaseBuilder( //Bu Room’un gerçek veritabanını oluşturduğu yer.
                    context.applicationContext, //memory leak olmaz
                    AppDatabase::class.java, //bu sınıf DB olacak
                    "app_db" //DB dosya adı
                )
                    //Bu development aşaması için iyi, production’da tehlikelidir çünkü veri kaybı olur.
                    .fallbackToDestructiveMigration() //Migration yoksa eski veritabanını sil, yenisini kur.
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
