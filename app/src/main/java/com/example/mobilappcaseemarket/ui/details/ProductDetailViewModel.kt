import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val repo: ProductRepository) : ViewModel() {

    val product = MutableLiveData<Product>()

    fun fetchProductById(id: String) {
        viewModelScope.launch {
            val result = repo.getProductById(id)
            product.postValue(result)
        }
    }
}

class ProductDetailViewModelFactory(private val repo: ProductRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductDetailViewModel(repo) as T
    }
}
