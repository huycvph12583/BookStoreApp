package com.app.bookstoreapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.bookstoreapp.BuildConfig
import com.app.bookstoreapp.R
import com.app.bookstoreapp.adapters.ForYouAdapter
import com.app.bookstoreapp.adapters.NewBookAdapter
import com.app.bookstoreapp.databinding.FragmentHomeScreenBinding
import com.app.bookstoreapp.models.Book
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class HomeScreenFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private val TAG = "Screen"
    private val sharedPreference by lazy {
        requireActivity().getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}_sharePreferences",
            Context.MODE_PRIVATE
        )
    }
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var rycAdapter: ForYouAdapter
    private lateinit var rycAdapterNB: NewBookAdapter
    private var listBook: MutableList<Book> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        pushDataToFB()
        getData()
        initView()
        lister()
        startPostponedEnterTransition()
    }

    private fun lister() {
        binding.apply {
            imgCart.setOnClickListener { findNavController().navigate(R.id.action_homeScreenFragment_to_cartScreenFragment2) }
        }
    }

    private fun initView() {
        binding.apply {
            tvHelloName.text = sharedPreference.getString("username","name")
            Picasso.get().load(sharedPreference.getString("userImage","https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"))
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .fit()
                .into(imgLogo)
            // List For You
            rycAdapter = ForYouAdapter(requireContext(), listBook)
            rycBookForYou.adapter = rycAdapter
            // List New Bie - get item on list
            rycAdapterNB = NewBookAdapter(requireContext(), listBook) { book ->
                clickItem(book)
            }
            rycBookNewBie.adapter = rycAdapterNB
        }
    }

    private fun clickItem(book: Book) {
        val bundle = Bundle()
        bundle.putString("idBook", book.id)
        findNavController().navigate(
            R.id.action_homeScreenFragment_to_bookDetailScreenFragment2, bundle
        )
    }

    // Read data to firebase
    private fun getData() {
        binding.apply {
            database = FirebaseDatabase.getInstance().getReference("Book")
            database.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (listBook.size > 0) {
                        listBook = mutableListOf()
                    }
                    for (postSnapshot in snapshot.children) {
                        val item = postSnapshot.getValue(Book::class.java)
                        if (item != null) {
                            listBook.add(item)
                        }

                    }
                    rycAdapter.notifyDataSetChanged()
                    rycAdapterNB.notifyDataSetChanged()
                    Log.d(TAG, "Size ${listBook.size}")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }

    }
    // Push data to firebase
    private fun pushDataToFB() {
        database = FirebaseDatabase.getInstance().getReference("Book")
        listBook.add(Book("1","Dế mèn phưu lưu kí","Truyện",500,
            "https://sachxuasaigon.com/wp-content/uploads/2020/01/De-men-phieu-luu-ky-1.jpg",
            "Dế Mèn phiêu lưu ký là tác phẩm văn xuôi đặc sắc và nổi tiếng nhất của nhà văn Tô Hoài viết về loài vật," +
                    " dành cho lứa tuổi thiếu nhi. Ban đầu truyện có tên là “Con dế mèn” (chính là ba chương đầu của truyện) do Nhà xuất bản Tân Dân, Hà Nội phát hành năm 1941. Sau đó, được sự ủng hộ nhiệt tình của nhân dân, Tô Hoài viết thêm truyện “Dế Mèn phiêu lưu ký” (là bảy chương cuối của truyện). Năm 1955, ông mới gộp hai chuyện vào với nhau để thành truyện “Dế mèn phiêu lưu ký” như ngày nay"))
        listBook.add(Book("2","Cồn Dừa - Trạng quỳnh","Truyện Thiếu Nhi",300,
            "https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_44197.jpg",
            "Trạng Quỷnh là một bộ truyện tranh thiếu nhi nhiều tập của Việt Nam, tập truyện đầu tiên mang tên Sao sáng xứ Thanh được Nhà xuất bản Đồng Nai phát hành giữa tháng 6 năm 2003."))
        listBook.add(Book("3","Ô long viện - Linh vật sống","Truyện Vui",300,
            "https://salt.tikicdn.com/cache/750x750/media/catalog/product/i/m/img213.u2377.d20161126.t134823.160664.jpg",
            "Trong lúc nóng giận, sư phụ Lông Mày Dài không kiềm chế được, đã lỡ mồm nói ra chuyện của Ê Đi Sờn, khiến bà góa Ê đau khổ ngất xỉu ngay lập tức. Mấy thầy trò còn chưa hết sững sờ khi biết Êphin đã bị Sa Khách Dương bắt cóc, thì tên Cẩu Tử đã xông vào cướp phần đầu Linh vật sống đi mất."))
        for (item in listBook) {
            database.child(item.id).setValue(item)
        }
        listBook = mutableListOf()
    }
}
