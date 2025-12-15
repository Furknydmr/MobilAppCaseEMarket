import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.remote.RetrofitClient
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.data.repository.ProductRepositoryInterface
import com.example.mobilappcaseemarket.ui.home.HomeViewModel
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val repo: ProductRepositoryInterface) : ViewModel() {

    val product = MutableLiveData<Product>()

    fun fetchProductById(id: String) {
        viewModelScope.launch {
            val result = repo.getProductById(id)
            product.postValue(result)
        }
    }
    class ProductDetailViewModelFactory : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val api = RetrofitClient.api
            val repo = ProductRepository(api)

            return ProductDetailViewModel(repo) as T
        }
    }
}


