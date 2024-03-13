package com.example.galleryapptask


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapptask.databinding.ActivityGalleryMainBinding



class GalleryMainActivity : AppCompatActivity() {

    private val selectedMedia = mutableListOf<String>()
    private lateinit var mediaAdapter: ImageAdapter
    private lateinit var binding: ActivityGalleryMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        mediaAdapter = ImageAdapter(this, selectedMedia)
        binding.recyclerView.adapter = mediaAdapter

        val pickMediaIntent = createPickMediaIntent()
        binding.addImg.setOnClickListener {
            binding.textView.visibility = View.INVISIBLE
            pickMediaLauncher.launch(pickMediaIntent)  // Launch media selection
        }

        /*binding.openStorySaver.setOnClickListener {
            startActivity(Intent(this, WSMainActivity::class.java))
        }*/

    }

    private val pickMediaLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    handleSelectedMedia(data)
                }
            }
        }

    private fun createPickMediaIntent(): Intent {

        val pickMediaIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)   // Create intent for picking media files
        pickMediaIntent.addCategory(Intent.CATEGORY_OPENABLE)
        pickMediaIntent.type = "*/*"
        pickMediaIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        return pickMediaIntent
    }

    private fun handleSelectedMedia(data: Intent) {
        if (data.clipData != null) {
            val count = data.clipData!!.itemCount
            for (i in 0 until count) {
                val mediaUri = data.clipData!!.getItemAt(i).uri
                selectedMedia.add(mediaUri.toString())
            }
        } else if (data.data != null) {
            val mediaUri = data.data!!
            selectedMedia.add(mediaUri.toString())
        }
        mediaAdapter.notifyDataSetChanged()   //// Notify the adapter about the data change
    }
}
