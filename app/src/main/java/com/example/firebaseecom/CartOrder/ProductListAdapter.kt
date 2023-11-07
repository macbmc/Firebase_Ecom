import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.CartOrder.ProductListActivity
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.CartViewBinding
import com.example.firebaseecom.model.ProductHomeModel
import javax.inject.Inject

class ProductListAdapter @Inject constructor(
    val activityFunctionClass: ProductListActivity.ActivityFunctionClass
) : RecyclerView.Adapter<ProductListAdapter.MyViewHolder>() {
    interface ActivityFunctionInterface {

        fun navigateToDetails(productHomeModel: ProductHomeModel)
        fun deleteFromCart(productHomeModel: ProductHomeModel)
    }

    var productList: MutableList<ProductHomeModel> = mutableListOf()
    lateinit var cartViewBinding: CartViewBinding
    override fun onCreateViewHolder(

        parent: ViewGroup,
        viewType: Int
    ): ProductListAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        cartViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.cart_view, parent, false)
        return MyViewHolder(cartViewBinding)

    }

    override fun onBindViewHolder(holder: ProductListAdapter.MyViewHolder, position: Int) {
        val productHome = productList[position]
        holder.bind(productHome)
        Glide.with(holder.itemView)
            .load(productHome.productImage)
            .error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.productImage))
        holder.itemView.setOnClickListener {
            activityFunctionClass.navigateToDetails(productHome)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProduct(productList: List<ProductHomeModel>) {
        this.productList = productList.toMutableList()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val cartViewBinding: CartViewBinding) :
        RecyclerView.ViewHolder(cartViewBinding!!.root) {
        fun bind(productHomeModel: ProductHomeModel) {
            cartViewBinding.productHome = productHomeModel
            cartViewBinding.deleteBtn.setOnClickListener {
                val status = activityFunctionClass.deleteFromCart(productHomeModel)
                productList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)

            }
        }

    }

}


