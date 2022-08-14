package com.example.realestatemanager.data.repository.property

import com.example.realestatemanager.data.localData.LocalDatabase
import com.example.realestatemanager.data.localData.PropertyDao
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.sampleData.SampleProperties
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
class PropertyRepositoryImpTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var localDatabase: LocalDatabase

    @Inject
    lateinit var propertyDao: PropertyDao

    private val propertyList = SampleProperties.samplePropertyList

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        localDatabase.propertyDao().nukeTable()
    }

    @Test
    fun classes_are_injected_correctly() {
        assertThat(localDatabase).isNotNull()
        assertThat(propertyDao).isNotNull()
    }

    @Test
    fun getPropertyListFromDatabase() = runTest {
        val propertyListTest: List<Property> = propertyDao.getListOfProperties().first()
        advanceUntilIdle()

        assertThat(propertyListTest).isNotNull()
    }

    @Test
    fun properties_are_correctly_added_to_database() = runTest {
        //Given
        val propertyListTest: List<Property> = propertyDao.getListOfProperties().first()
        advanceUntilIdle()

        assertThat(propertyListTest).isNotNull()
        assertThat(propertyListTest.size).isEqualTo(0)

        //When
        addProperties()
        advanceUntilIdle()

        val propertyListTestAfterAddingProperties = propertyDao.getListOfProperties().first()
        advanceUntilIdle()

        //Then
        assertThat(propertyListTest.size).isNotEqualTo(propertyListTestAfterAddingProperties.size)
        assertThat(propertyListTestAfterAddingProperties.size).isEqualTo(4)
    }

    @Test
    fun properties_are_updated_correctly_in_database() = runTest {
        //Given
        var propertyListTest = propertyDao.getListOfProperties().first()
        advanceUntilIdle()

        val property = propertyList[0]
        assertThat(propertyListTest).doesNotContain(property)

        //When
        propertyDao.insertProperty(property)
        propertyListTest = propertyDao.getListOfProperties().first()
        property.id = propertyListTest[0].id
        advanceUntilIdle()

        assertThat(propertyListTest).contains(property)

        //Then
        val newPrice = 456516521
        property.price = newPrice
        propertyDao.updateProperty(property)
        propertyListTest = propertyDao.getListOfProperties().first()
        advanceUntilIdle()

        assertThat(propertyListTest[0].price).isEqualTo(newPrice)
    }

    @Test
    fun get_specific_property_from_dataBase() = runTest {
        //Given
        addProperties()
        val propertyListTest = propertyDao.getListOfProperties().first()
        advanceUntilIdle()

        //When
        val property: Property = propertyDao.getProperty(propertyListTest[0].id).first()
        val property1 = propertyDao.getProperty(propertyListTest[1].id).first()
        advanceUntilIdle()

        //Then
        assertThat(propertyListTest).contains(property)
        assertThat(propertyListTest).contains(property1)
    }

    @Test
    fun test_Search_Request() = runTest {
        //Given
        addProperties()
        advanceUntilIdle()

        //When
        val property = SampleProperties.samplePropertyList[0]
        val isNearTypeProperty: List<String> = listOf(property.type)
        val isNearCity: List<String> = listOf(property.city)
        val isNearNeighbourhood: List<String> = listOf(property.neighbourhood)
        val isNearMinSurface: Int = property.surface - 10
        val isNearMaxSurface: Int = property.surface + 10
        val isNearSchool: List<Boolean> = listOf(property.closeToSchool)
        val isNearStore: List<Boolean> = listOf(property.closeToShops)
        val isNearParc: List<Boolean> = listOf(property.closeToPark)
        val isNearNumberOfPhotos = 0
        val isNearMinPrice = property.price - 1000
        val isNearMaxPrice = property.price + 1000
        val isNearSaleStatus: Boolean = property.saleStatus

        val result =
            propertyDao.getPropertyResearch(
                isNearTypeProperty,
                isNearCity,
                isNearNeighbourhood,
                isNearMinSurface,
                isNearMaxSurface,
                isNearSchool,
                isNearStore,
                isNearParc,
                isNearNumberOfPhotos,
                isNearMinPrice,
                isNearMaxPrice,
                isNearSaleStatus
            )
        advanceUntilIdle()

        // Then
        assertThat(result.first().size).isNotEqualTo(0)
    }

    private suspend fun addProperties() {
        propertyDao.insertProperty(propertyList[0])
        propertyDao.insertProperty(propertyList[1])
        propertyDao.insertProperty(propertyList[2])
        propertyDao.insertProperty(propertyList[3])
    }

}