package com.example.mylibrary.database

import android.app.Activity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.mylibrary.R
import com.example.mylibrary.activities.*
import com.example.mylibrary.models.Archive
import com.example.mylibrary.models.Book
import com.example.mylibrary.models.User
import com.example.mylibrary.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter
import java.io.File


class RealTimeDataBase {

    private val database = FirebaseDatabase.getInstance()

    //user
    /**
     * function that creates a new table and adds all the registered users to it
     */
    fun createUser(activity: SignUpActivity, user: User) {
        val ref = database.getReference(Constants.USERS).child(getCurrentUserID())
        ref.setValue(user).addOnSuccessListener {
            activity.userRegisteredSuccess()
        }.addOnFailureListener { e ->
            Log.e(activity.javaClass.simpleName, "Error creating user in Realtime Database: ${e.message}")
        }
    }

    /**
     * function that sign in the user in order to get their data from the data base
     */
    fun loadUserData(activity: Activity) {
        val ref = database.getReference(Constants.USERS).child(getCurrentUserID())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loggedInUser = snapshot.getValue(User::class.java)!!
                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser)
                    }
                    is MyProfileActivity -> {
                        activity.setUserData(loggedInUser)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                when (activity) {
                    is SignInActivity -> {
                        activity.makeProgressDialogInvisible()
                    }
                    is MainActivity -> {
                        activity.makeProgressDialogInvisible()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName, "Error getting data from Realtime Database: ${error.message}"
                )
            }
        })
    }

    /**
     * function that gets the current user id
     */
    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""

        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    /**
     * Update user profile data
     */
    fun updateUserProfile(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
        val ref = database.getReference(Constants.USERS).child(getCurrentUserID())
        ref.updateChildren(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Profile data updated")
                Toast.makeText(activity, "Profile data updated successfully", Toast.LENGTH_SHORT)
                    .show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                activity.makeProgressDialogInvisible()
                Log.e(activity.javaClass.simpleName, "Error while updating profile data", e)
                Toast.makeText(activity, "Profile update error", Toast.LENGTH_SHORT).show()
            }
    }

    //user-book
    /**
     * when a book is added to the BOOKS table is also added to USER-BOOK table to know the user (many to many)
     */
    fun createBookAndAddItToUser(activity: AddBookActivity, book: Book)  {
        val newBookRef = database.reference.child(Constants.BOOKS).push()
        newBookRef.setValue(book)

        val newUserBookRef = database.reference.child(Constants.USER_BOOK).push()
        newUserBookRef.setValue(
            hashMapOf(
                "userId" to getCurrentUserID(), "bookId" to newBookRef.key, "myRate" to book.myRate, "isbn" to book.isbn
            )
        )

        activity.run {
            Toast.makeText(this, "Book created", Toast.LENGTH_SHORT).show()
            bookCreatedSuccessfully()
        }
    }

    //book
    /**
     * gets the book for the user by userID
     */
    fun getBookList(activity: Activity) {
        val userId = getCurrentUserID()
        val database = database.reference
        database.child(Constants.USER_BOOK).orderByChild(Constants.USERID).equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookIds = snapshot.children.map { it.child(Constants.BOOKID).value as String? }.filterNotNull()
                    if (bookIds.isNotEmpty()) {
                        database.child(Constants.BOOKS).orderByKey().startAt(bookIds.first())
                            .endAt(bookIds.last()).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val bookList: ArrayList<Book> = ArrayList()
                                    snapshot.children.forEach { child ->
                                        val book = child.getValue(Book::class.java) ?: return@forEach
                                        book.documentId = child.key!!
                                        bookList.add(book)
                                    }
                                    when(activity){
                                        is MyBooksActivity ->activity.populatesBooksList(bookList)
                                        is StatisticsActivity ->{
                                            activity.populatePieChart(bookList)
                                            activity.populateBarChart(bookList)
                                            activity.setTheFavouriteBook(bookList)
                                            activity.setNumberOfBooksFinished(bookList)
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    when(activity){
                                        is MyBooksActivity ->activity.makeProgressDialogInvisible()
                                        is StatisticsActivity ->activity.makeProgressDialogInvisible()
                                    }

                                    Log.e(
                                        activity.javaClass.simpleName,
                                        "Error while getting the books for user",
                                        error.toException()
                                    )
                                }
                            })
                    } else {
                        when(activity){
                            is MyBooksActivity -> activity.populatesBooksList(ArrayList())
                            is StatisticsActivity ->{
                                activity.populatePieChart(ArrayList())
                                activity.populateBarChart(ArrayList())
                                activity.setTheFavouriteBook(ArrayList())
                                activity.setNumberOfBooksFinished(ArrayList())
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    when(activity){
                        is MyBooksActivity -> activity.makeProgressDialogInvisible()
                        is StatisticsActivity ->activity.makeProgressDialogInvisible()
                    }

                    Log.e(activity.javaClass.simpleName, "Error while getting the books for user", error.toException())
                }
            })
    }


    /**
     * Update book details data
     */
    fun updateBookDetails(documentID: String, activity: UpdateBookActivity, bookHashMap: HashMap<String, Any>) {
        val bookRef = database.getReference(Constants.BOOKS).child(documentID)
        val userBookRef = database.getReference(Constants.USER_BOOK)

        // Get the current rating and ISBN of the book
        bookRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentMyRate = snapshot.child("myRate").getValue(Double::class.java)
                    val currentISBN = snapshot.child("isbn").getValue(String::class.java)

                    // Update the rating and ISBN in the bookHashMap if necessary
                    if (bookHashMap["myRate"] == null) {
                        bookHashMap["myRate"] = currentMyRate.toString().toDouble()
                    } else {
                        bookHashMap["myRate"] = bookHashMap["myRate"].toString().toDouble()
                    }
                    bookHashMap["isbn"] = currentISBN.toString()

                    // Update the book in the database
                    bookRef.updateChildren(bookHashMap).addOnSuccessListener {
                        Log.i(activity.javaClass.simpleName, "Book data updated")

                        // Update the book in the user's book list if it exists
                        userBookRef.orderByChild("bookId").equalTo(documentID).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userBooksToUpdate = mutableListOf<DataSnapshot>()

                                for (userBookSnapshot in snapshot.children) {
                                    val isbn = userBookSnapshot.child("isbn").getValue(String::class.java)
                                    if (isbn == currentISBN) {
                                        userBooksToUpdate.add(userBookSnapshot)
                                    }
                                }

                                if (userBooksToUpdate.isNotEmpty()) {
                                    for (userBookSnapshot in userBooksToUpdate) {
                                        userBookSnapshot.ref.updateChildren(
                                            mapOf(
                                                "myRate" to bookHashMap["myRate"],
                                                "isbn" to bookHashMap["isbn"]
                                            )
                                        )
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(activity.javaClass.simpleName, "Error while updating user book data", error.toException())
                            }
                        })

                        Toast.makeText(activity, "Book data updated successfully", Toast.LENGTH_SHORT).show()
                        activity.bookUpdateSuccess()
                    }.addOnFailureListener { e ->
                        activity.makeProgressDialogInvisible()
                        Log.e(activity.javaClass.simpleName, "Error while updating book data", e)
                        Toast.makeText(activity, "Book update error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(activity.javaClass.simpleName, "No such document")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(activity.javaClass.simpleName, "Error getting document", error.toException())
            }
        })
    }


    /**
     * function that gets the book data
     */
    fun loadBookData(activity: Activity, documentID: String) {
        val databaseRef = database.getReference(Constants.BOOKS).child(documentID)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val book = snapshot.getValue(Book::class.java)!!
                    when (activity) {
                        is UpdateBookActivity -> {
                            activity.setBookData(book)
                        }
                        is BookInfoActivity -> {
                            activity.setBookData(book)
                        }
                    }
                } else {
                    Log.d(activity.javaClass.simpleName, "No such document")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                when (activity) {
                    is UpdateBookActivity -> {
                        activity.makeProgressDialogInvisible()
                    }
                    is BookInfoActivity -> {
                        activity.makeProgressDialogInvisible()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error getting data from Realtime Database",
                    error.toException()
                )
            }
        })
    }


    /**
     * Function that deletes a book from Realtime Database from BOOK and USER_BOOK node
     */
    fun deleteBook(activity: MyBooksActivity, documentID: String) {
        val database = FirebaseDatabase.getInstance()
        val booksRef = database.getReference(Constants.BOOKS)
        val userBooksRef = database.getReference(Constants.USER_BOOK)

        // Delete book from BOOK node
        booksRef.child(documentID).removeValue().addOnSuccessListener {
            Log.i(activity.javaClass.simpleName, "Book deleted")

            // Check if book is present in USER_BOOK node
            userBooksRef.orderByChild(Constants.BOOKID).equalTo(documentID).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(activity, "Book deleted successfully in user book", Toast.LENGTH_SHORT).show()
                        activity.bookUpdateSuccess()
                        return
                    }

                    // Delete book from USER_BOOK node
                    val documentId = dataSnapshot.children.first().key.toString()
                    userBooksRef.child(documentId).removeValue().addOnSuccessListener {
                        Toast.makeText(activity, "Book deleted successfully", Toast.LENGTH_SHORT).show()
                        activity.bookUpdateSuccess()
                    }.addOnFailureListener { e ->
                        Log.e(activity.javaClass.simpleName, "Error while deleting book from USER_BOOK", e)
                        Toast.makeText(activity, "Error deleting book", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(activity.javaClass.simpleName, "Error while querying USER_BOOK node", databaseError.toException())
                    Toast.makeText(activity, "Error deleting book", Toast.LENGTH_SHORT).show()
                }
            })
        }.addOnFailureListener { e ->
            Log.e(activity.javaClass.simpleName, "Error while deleting book", e)
            Toast.makeText(activity, "Error deleting book", Toast.LENGTH_SHORT).show()
        }
    }

    //archive
    /**
     * gets all the elements from archive realtime database
     */
    fun getArchiveList(activity: SearchArchiveActivity, excludedId: String?) {
        val reference = database.getReference(Constants.ARCHIVE)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookList = arrayListOf<Archive>()
                for (bookSnapshot in snapshot.children) {
                    val book = bookSnapshot.getValue(Archive::class.java)
                    if (book != null) {
                        book.documentId = bookSnapshot.key.toString()
                        if (excludedId != null && book.documentId == excludedId) {
                            continue
                        }
                        bookList.add(book)
                    }
                }
                Log.e("books", bookList.toString())
                activity.populatesArchiveList(bookList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error getting books from database", error.toException())
            }
        })
    }

    /**
     * Function that gets the archive element from the archive realtime database and clone it to books
     */
    fun addFromArchiveToBooks(activity: Activity, archiveItemID: String, isbn: String) {
        val archiveRef = database.getReference(Constants.ARCHIVE)
        val booksRef = database.getReference(Constants.BOOKS)
        val userBookRef = database.getReference(Constants.USER_BOOK)
        val userId = getCurrentUserID()

        userBookRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(userBookSnapshot: DataSnapshot) {
                if (userBookSnapshot.exists()) {
                    val bookExists = userBookSnapshot.children.any { it.child("isbn").getValue(String::class.java) == isbn }
                    if (bookExists) {
                        // Book already exists in the user's collection
                        Toast.makeText(activity, "Book already in collection", Toast.LENGTH_SHORT).show()
                    } else {
                        // Book doesn't exist in the user's collection, add it to both BOOKS and USER_BOOKS
                        archiveRef.child(archiveItemID).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(archiveSnapshot: DataSnapshot) {
                                if (!archiveSnapshot.exists()) {
                                    Toast.makeText(activity, "Archive not found", Toast.LENGTH_SHORT).show()
                                    return
                                }

                                val bookData = archiveSnapshot.getValue(Archive::class.java)
                                if (bookData == null) {
                                    Toast.makeText(activity, "Book not found", Toast.LENGTH_SHORT).show()
                                    return
                                }

                                val bookKey = booksRef.push().key
                                if (bookKey == null) {
                                    Toast.makeText(activity, "Error creating book key", Toast.LENGTH_SHORT).show()
                                    return
                                }

                                // Add the book to the BOOKS collection
                                booksRef.child(bookKey).setValue(bookData).addOnSuccessListener {
                                    // Associate the book with the current user in the USER_BOOK table
                                    val userBookData = hashMapOf(
                                        "bookId" to bookKey,
                                        "userId" to userId,
                                        "myRate" to 0,
                                        "isbn" to isbn
                                    )
                                    userBookRef.push().setValue(userBookData).addOnSuccessListener {
                                        Toast.makeText(
                                            activity,
                                            "Book added successfully to collection",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }.addOnFailureListener { e ->
                                        Toast.makeText(
                                            activity,
                                            "Error associating book with user: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }.addOnFailureListener { e ->
                                    Toast.makeText(
                                        activity,
                                        "Error cloning book: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(activity, "Error reading archive: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    // User has no books in the collection, add the book to both BOOKS and USER_BOOKS
                    archiveRef.child(archiveItemID).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(archiveSnapshot: DataSnapshot) {
                            if (!archiveSnapshot.exists()) {
                                Toast.makeText(activity, "Archive not found", Toast.LENGTH_SHORT).show()
                                return
                            }

                            val bookData = archiveSnapshot.getValue(Archive::class.java)
                            if (bookData == null) {
                                Toast.makeText(activity, "Book not found", Toast.LENGTH_SHORT).show()
                                return
                            }

                            val bookKey = booksRef.push().key
                            if (bookKey == null) {
                                Toast.makeText(activity, "Error creating book key", Toast.LENGTH_SHORT).show()
                                return
                            }

                            // Add the book to the BOOKS collection
                            booksRef.child(bookKey).setValue(bookData).addOnSuccessListener {
                                // Associate the book with the current user in the USER_BOOK table
                                val userBookData = hashMapOf(
                                    "bookId" to bookKey,
                                    "userId" to userId,
                                    "myRate" to 0,
                                    "isbn" to isbn
                                )
                                userBookRef.push().setValue(userBookData).addOnSuccessListener {
                                    Toast.makeText(
                                        activity,
                                        "Book added successfully to collection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(
                                        activity,
                                        "Error associating book with user: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }.addOnFailureListener { e ->
                                Toast.makeText(
                                    activity,
                                    "Error cloning book: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(activity, "Error reading archive: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error reading user-books: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    /**
     * Gets the details of a specific archive item by isbn
     */
    fun loadArchiveData(activity: ArchiveInfoActivity, isbn: String) {
        val reference = database.getReference(Constants.ARCHIVE)
        reference.orderByChild(Constants.ISBN).equalTo(isbn)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        Toast.makeText(activity, "Book not found", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val bookData = snapshot.children.first().getValue(Archive::class.java)

                    if (bookData == null) {
                        Toast.makeText(activity, "Book not found", Toast.LENGTH_SHORT).show()
                        return
                    }

                    bookData.documentId = snapshot.children.first().key.toString()

                    activity.setArchiveData(bookData)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        activity, "Error getting book details: ${error.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    /**
     * Gets the first 10 books from archive with the highest rankings
     */
    fun getFirstBestBooks(activity: MainActivity) {
        val reference = database.getReference(Constants.ARCHIVE)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookList = arrayListOf<Archive>()
                for (bookSnapshot in snapshot.children) {
                    val book = bookSnapshot.getValue(Archive::class.java)
                    if (book != null) {
                        book.documentId = bookSnapshot.key.toString()
                        bookList.add(book)
                    }
                }
                bookList.sortByDescending { it.rating }
                val top10Books = bookList.take(10)
                activity.populatesRecommendedList(top10Books)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error getting books from database", error.toException())
            }
        })
    }

    /**
     * Gets the recommended books from model form firebase ml and gets the books from realtime database
     */
    fun getRecommendedBooks(activity: MainActivity, isbnList: List<String>) {
        val reference = database.getReference(Constants.ARCHIVE)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookList = arrayListOf<Archive>()
                for (bookSnapshot in snapshot.children) {
                    val book = bookSnapshot.getValue(Archive::class.java)
                    if (book != null && isbnList.contains(book.isbn)) {
                        book.documentId = bookSnapshot.key.toString()
                        bookList.add(book)
                    }
                }
                bookList.sortByDescending { it.rating }
                activity.populatesRecommendedList(bookList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error reading archive: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}