package com.example.realestatemanager.data.repository.photo

import com.example.realestatemanager.data.localData.PhotoDao
import com.example.realestatemanager.data.model.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImp @Inject constructor(
    private val photoDao: PhotoDao
) : PhotoRepository {
    override suspend fun insertPhoto(photo: Photo) {
        photoDao.insertPhoto(photo)
    }

    override suspend fun deletePhoto(photo: Photo) {
        photoDao.deletePhoto(photo)
    }

    override fun getListOfPhotos(): Flow<List<Photo>> = photoDao.getListOfPhotos()

    override fun getPhoto(id: Int): Flow<Photo> = photoDao.getPhotoWithId(id)

    override fun getPhotoWithName(name: String): Flow<Photo> = photoDao.getPhotoWithName(name)
}