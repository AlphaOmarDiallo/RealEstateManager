package com.example.realestatemanager.data.repository.photo

import androidx.test.platform.app.InstrumentationRegistry
import com.example.realestatemanager.data.localData.LocalDatabase
import com.example.realestatemanager.data.model.Photo
import com.example.realestatemanager.data.repository.media.MediaStoreRepositoryImp
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class PhotoRepositoryImpTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var localDatabase: LocalDatabase

    @Inject
    lateinit var photoRepositoryImp: PhotoRepositoryImp

    @Inject
    lateinit var mediaStoreRepositoryImp: MediaStoreRepositoryImp

    @Before
    fun init() {
        hiltRule.inject()

    }
    @After
    fun tearDown() {
        localDatabase.photoDao().nukeTable()
    }

    @Test
    fun add_photo_in_db_and_delete() = runTest {
        val iPL = mediaStoreRepositoryImp.loadPhotosFromInternalStorage(InstrumentationRegistry.getInstrumentation().context)
        val photo = Photo(0, iPL[0].bmp,iPL[0].name)
        val size = photoRepositoryImp.getListOfPhotos().first().size
        photoRepositoryImp.insertPhoto(photo)
        advanceUntilIdle()

        val size1 = photoRepositoryImp.getListOfPhotos().first().size
        advanceUntilIdle()

        assertThat(size).isNotEqualTo(size1)
    }
}