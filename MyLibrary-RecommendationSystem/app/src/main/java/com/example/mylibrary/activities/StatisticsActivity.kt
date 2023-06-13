package com.example.mylibrary.activities

import android.animation.ValueAnimator
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.mylibrary.R
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.Book
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StatisticsActivity : BaseActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var barEntriesList: ArrayList<BarEntry>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        //hide the top bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //sets up action bar
        setupActionBar()

        RealTimeDataBase().getBookList(this)
    }

    /**
     * Sets up the action bar for the page with back button
     */
    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_stats))
        findViewById<Toolbar>(R.id.toolbar_stats).setBackgroundColor(resources.getColor(R.color.purple_200))
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionBar.title = resources.getString(R.string.stats)
        }
        findViewById<Toolbar>(R.id.toolbar_stats).setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun populatePieChart(bookList: List<Book>) {
        pieChart = findViewById(R.id.pieChart)

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(resources.getColor(R.color.white))
        pieChart.setTransparentCircleColor(resources.getColor(R.color.white))
        pieChart.setTransparentCircleAlpha(110)
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.setDrawCenterText(true)
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.legend.isEnabled = true
        pieChart.setEntryLabelColor(resources.getColor(R.color.white))
        pieChart.setEntryLabelTextSize(12f)

        // calculate the percentage of books in each genre
        val genreCount = HashMap<String, Int>()
        for (book in bookList) {
            val genre = book.genre
            if (genreCount.containsKey(genre)) {
                genreCount[genre] = genreCount[genre]!! + 1
            } else {
                genreCount[genre] = 1
            }
        }
        val totalBooks = bookList.size
        val entries: ArrayList<PieEntry> = ArrayList()
        for ((genre, count) in genreCount) {
            val percentage = count.toFloat() / totalBooks * 100
            entries.add(PieEntry(percentage, genre))
        }

        // populate the PieChart
        val dataSet = PieDataSet(entries, "Book Genres")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.purple_100))
        colors.add(resources.getColor(R.color.purple_150))
        colors.add(resources.getColor(R.color.purple_200))
        colors.add(resources.getColor(R.color.purple_500))
        colors.add(resources.getColor(R.color.purple_700))
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(resources.getColor(R.color.white))
        pieChart.data = data

        pieChart.highlightValues(null)
        pieChart.invalidate()

    }

    fun populateBarChart(bookList: List<Book>){
        barChart = findViewById(R.id.idBarChart)
        barEntriesList = ArrayList()

        val calendar = Calendar.getInstance()
        val bookCountMap = mutableMapOf<String, Int>()
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        for (book in bookList) {
            val finishDate = book.finishDate

            if (finishDate != "") {
                // Get the finish month and year
                calendar.time = formatter.parse(finishDate)!!
                val finishMonth = calendar.get(Calendar.MONTH)
                val finishYear = calendar.get(Calendar.YEAR)
                // Calculate the key for the map
                val key = "${finishMonth + 1}/${finishYear}"

                // Add the book count for the month to the map
                val bookCount = bookCountMap.getOrDefault(key, 0) + 1
                bookCountMap[key] = bookCount
            }
        }

        // Convert the map entries to BarEntry objects and add to the list
        var i = 1f
        for ((key, value) in bookCountMap) {
            barEntriesList.add(BarEntry(i, value.toFloat()))
            i += 1f
        }

        // Create a BarDataSet with the entries list
        val barDataSet = BarDataSet(barEntriesList, "Books Read per Month")

        // Customize the BarDataSet appearance
        barDataSet.color = resources.getColor(R.color.purple_200)
        barDataSet.valueTextColor = resources.getColor(R.color.black)
        barDataSet.valueTextSize = 16f
        barDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        // Create a BarData object with the BarDataSet
        val barData = BarData(barDataSet)

        // Set the BarData object to the BarChart and customize its appearance
        barChart.data = barData
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.axisMinimum = 0f
        barChart.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }
        barChart.axisRight.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f // This sets the distance between the bars to 1
        barChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt() - 1
                return if (index >= 0 && index < bookCountMap.keys.size) {
                    bookCountMap.keys.toList()[index]
                } else {
                    ""
                }
            }
        }
        barChart.invalidate()
    }

    fun setTheFavouriteBook(bookList: List<Book>){
        var bestBook: Book? = null

        for (book in bookList) {
            if (bestBook == null || book.myRate > bestBook.myRate) {
                bestBook = book
            }
        }
        bestBook?.let {
            // Set the data in the UI item
            Glide
                .with(this@StatisticsActivity)
                .load(it.image)
                .centerCrop()
                .placeholder(R.drawable.ic_book)
                .into(findViewById(R.id.iv_favourite_image))
            findViewById<TextView>(R.id.tv_favourite_title).text = it.title
            findViewById<TextView>(R.id.tv_favourite_author).text = it.author
            findViewById<RatingBar>(R.id.tv_favourite_rate).rating = it.myRate
        }
    }

    fun setNumberOfBooksFinished(bookList: List<Book>){
        val counter =  bookList.count { it.finishDate != "" }
        val animator = ValueAnimator.ofInt(1, counter)
        animator.duration = 1000 // Animation duration in milliseconds
        animator.addUpdateListener { animation ->
            // Update the TextView with the current count
            val count = animation.animatedValue as Int
            findViewById<TextView>(R.id.tv_counter).text = count.toString()
        }
        animator.start()
    }
}